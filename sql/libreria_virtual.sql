-- Base de datos de la Tienda Virtual de Libros (Grupo 12)
-- Ejecutar en HeidiSQL, sesion "Libreria Virtual" (MariaDB).
-- La aplicacion se conecta con: jdbc:mariadb://localhost:3306/libreria_virtual
-- El script se puede ejecutar varias veces: no borra datos existentes.

CREATE DATABASE IF NOT EXISTS libreria_virtual
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE libreria_virtual;

-- ---------------------------------------------------------------
-- USUARIOS (RF-01, RF-02, RF-04, RF-11)
-- La contrasena no se guarda: solo su hash PBKDF2 y la sal con que se calculo.
-- Las cuentas de demostracion las crea la aplicacion al primer arranque.
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
  id              INT            NOT NULL AUTO_INCREMENT,
  nombre          VARCHAR(50)    NOT NULL,
  apellido        VARCHAR(50)    NOT NULL,
  correo          VARCHAR(100)   NOT NULL,
  direccion       VARCHAR(100)   NOT NULL,
  telefono        VARCHAR(10)    NOT NULL,
  tipo_usuario    ENUM('CLIENTE', 'ADMIN') NOT NULL DEFAULT 'CLIENTE',
  salt            VARBINARY(16)  NOT NULL,
  hash_contrasena VARBINARY(32)  NOT NULL,
  fecha_registro  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_usuarios_correo (correo)
);

-- ---------------------------------------------------------------
-- CATEGORIAS: lista de categorias registradas para los libros
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS categorias (
  nombre VARCHAR(50) NOT NULL,
  PRIMARY KEY (nombre)
);

INSERT IGNORE INTO categorias (nombre) VALUES
  ('Novela'), ('Misterio'), ('Tecnología'), ('Superación'), ('Fábula'), ('Clásico'),
  ('Ciencia'), ('Historia'), ('Biografía'), ('Infantil'), ('Fantasía');

-- ---------------------------------------------------------------
-- LIBROS (RF-07, RF-08, RF-09, RF-10) segun la especificacion de datos del DAD
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS libros (
  isbn             CHAR(13)       NOT NULL,
  titulo_libro     VARCHAR(150)   NOT NULL,
  autor_libro      VARCHAR(100)   NOT NULL,
  anio_publicacion CHAR(4)        NOT NULL,
  categoria        VARCHAR(50)    NOT NULL,
  editorial        VARCHAR(100)   NOT NULL,
  num_paginas      INT            NOT NULL,
  precio_venta     DECIMAL(10, 2) NOT NULL,
  stock            INT            NOT NULL DEFAULT 0,
  tipo_formato     ENUM('Físico', 'Digital') NOT NULL,
  PRIMARY KEY (isbn),
  CONSTRAINT fk_libros_categoria FOREIGN KEY (categoria)
    REFERENCES categorias (nombre) ON UPDATE CASCADE,
  CONSTRAINT ck_libros_isbn    CHECK (isbn REGEXP '^97[89][0-9]{10}$'),
  CONSTRAINT ck_libros_anio    CHECK (anio_publicacion REGEXP '^[0-9]{4}$'),
  CONSTRAINT ck_libros_paginas CHECK (num_paginas BETWEEN 1 AND 99999),
  CONSTRAINT ck_libros_precio  CHECK (precio_venta >= 0),
  CONSTRAINT ck_libros_stock   CHECK (stock BETWEEN 0 AND 999999)
);

-- Catalogo de demostracion (el mismo que carga la aplicacion)
INSERT IGNORE INTO libros
  (isbn, titulo_libro, autor_libro, anio_publicacion, categoria, editorial, num_paginas, precio_venta, stock, tipo_formato)
VALUES
  ('9780000000101', 'Cien Años de Soledad', 'Gabriel García Márquez', '1967', 'Novela', 'Editorial Sudamericana', 417, 45000, 15, 'Físico'),
  ('9780000000102', 'El Código Da Vinci', 'Dan Brown', '2003', 'Misterio', 'Doubleday', 489, 38000, 8, 'Físico'),
  ('9780000000103', 'Clean Code in Java', 'Robert C. Martin', '2008', 'Tecnología', 'Prentice Hall', 464, 120000, 5, 'Digital'),
  ('9780000000104', 'Hábitos Atómicos', 'James Clear', '2018', 'Superación', 'Avery', 320, 52000, 20, 'Físico'),
  ('9780000000105', 'El Principito', 'Antoine de Saint-Exupéry', '1943', 'Fábula', 'Reynal & Hitchcock', 96, 25000, 12, 'Físico'),
  ('9780000000106', 'Don Quijote de la Mancha', 'Miguel de Cervantes', '1605', 'Clásico', 'Francisco de Robles', 863, 60000, 7, 'Digital');

-- ---------------------------------------------------------------
-- CARRITO DE COMPRAS (RF-05): un renglon por libro dentro del carrito de cada usuario
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS items_carrito (
  id         INT      NOT NULL AUTO_INCREMENT,
  usuario_id INT      NOT NULL,
  isbn       CHAR(13) NOT NULL,
  cantidad   INT      NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  UNIQUE KEY uk_items_carrito (usuario_id, isbn),
  CONSTRAINT fk_items_carrito_usuario FOREIGN KEY (usuario_id)
    REFERENCES usuarios (id) ON DELETE CASCADE,
  CONSTRAINT fk_items_carrito_libro FOREIGN KEY (isbn)
    REFERENCES libros (isbn) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT ck_items_carrito_cantidad CHECK (cantidad >= 1)
);

-- ---------------------------------------------------------------
-- COMPRAS (RF-06 checkout y RF-04 historial de compras)
-- Si se elimina el usuario o el libro, la compra se conserva como historial.
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS compras (
  id           INT            NOT NULL AUTO_INCREMENT,
  usuario_id   INT            NULL,
  fecha_compra TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  metodo_pago  VARCHAR(30)    NOT NULL,
  subtotal     DECIMAL(12, 2) NOT NULL,
  iva          DECIMAL(12, 2) NOT NULL,
  total        DECIMAL(12, 2) NOT NULL,
  PRIMARY KEY (id),
  KEY ix_compras_usuario (usuario_id),
  CONSTRAINT fk_compras_usuario FOREIGN KEY (usuario_id)
    REFERENCES usuarios (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS detalle_compra (
  id              INT            NOT NULL AUTO_INCREMENT,
  compra_id       INT            NOT NULL,
  isbn            CHAR(13)       NULL,
  titulo_libro    VARCHAR(150)   NOT NULL,
  cantidad        INT            NOT NULL,
  precio_unitario DECIMAL(10, 2) NOT NULL,
  subtotal        DECIMAL(12, 2) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_detalle_compra FOREIGN KEY (compra_id)
    REFERENCES compras (id) ON DELETE CASCADE,
  CONSTRAINT fk_detalle_libro FOREIGN KEY (isbn)
    REFERENCES libros (isbn) ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT ck_detalle_cantidad CHECK (cantidad >= 1)
);

-- ---------------------------------------------------------------
-- DATOS DE DEMOSTRACION
-- INSERT IGNORE: si el registro ya existe no se duplica ni se modifica.
-- ---------------------------------------------------------------

-- Mas libros para el catalogo
INSERT IGNORE INTO libros
  (isbn, titulo_libro, autor_libro, anio_publicacion, categoria, editorial, num_paginas, precio_venta, stock, tipo_formato)
VALUES
  ('9780000000107', 'La Sombra del Viento', 'Carlos Ruiz Zafón', '2001', 'Misterio', 'Planeta', 576, 58000, 10, 'Físico'),
  ('9780000000108', 'Sapiens', 'Yuval Noah Harari', '2011', 'Historia', 'Debate', 496, 72000, 9, 'Físico'),
  ('9780000000109', 'Breve Historia del Tiempo', 'Stephen Hawking', '1988', 'Ciencia', 'Crítica', 256, 48000, 6, 'Digital'),
  ('9780000000110', 'Harry Potter y la Piedra Filosofal', 'J. K. Rowling', '1997', 'Fantasía', 'Salamandra', 264, 55000, 14, 'Físico'),
  ('9780000000111', 'El Hobbit', 'J. R. R. Tolkien', '1937', 'Fantasía', 'Minotauro', 310, 50000, 11, 'Físico'),
  ('9780000000112', 'Steve Jobs', 'Walter Isaacson', '2011', 'Biografía', 'Debate', 744, 85000, 4, 'Digital'),
  ('9780000000113', 'El Patito Feo', 'Hans Christian Andersen', '1843', 'Infantil', 'Everest', 32, 18000, 25, 'Físico'),
  ('9780000000114', 'Crónica de una Muerte Anunciada', 'Gabriel García Márquez', '1981', 'Novela', 'La Oveja Negra', 156, 35000, 13, 'Físico'),
  ('9780000000115', 'Effective Java', 'Joshua Bloch', '2018', 'Tecnología', 'Addison-Wesley', 412, 135000, 3, 'Digital'),
  ('9780000000116', 'Orgullo y Prejuicio', 'Jane Austen', '1813', 'Clásico', 'Alianza', 448, 42000, 8, 'Físico');

-- Clientes de ejemplo. Todos entran con la contrasena de demostracion Cliente123
-- (aqui solo va el hash PBKDF2 y la sal, igual que los guarda la aplicacion).
INSERT IGNORE INTO usuarios
  (nombre, apellido, correo, direccion, telefono, tipo_usuario, salt, hash_contrasena)
VALUES
  ('Laura', 'Gómez', 'laura.gomez@bibliotech.com', 'Calle 15 # 8-45, Tunja', '3104567890', 'CLIENTE', x'4CD65B61145D13134ADCBFB5FA488E45', x'172F7D32DE81C0D47D5ACACC9C39776EDCEAACF9F77812B60517165EA2EEB2AF'),
  ('Carlos', 'Rodríguez', 'carlos.rodriguez@bibliotech.com', 'Carrera 10 # 20-15, Duitama', '3125678901', 'CLIENTE', x'D22D72AB4A9158344EF00FA0F0BAFAA5', x'493BB8230BB98FB9C7E97529C546599B60F029755E705BC8F26366E6BCF83D29'),
  ('María', 'Pérez', 'maria.perez@bibliotech.com', 'Avenida Norte # 45-12, Sogamoso', '3156789012', 'CLIENTE', x'66B18308DA19697DCA472EFD3C44DA63', x'F250C4846D66ED6C680D26D68D4BA7376F2BB37E3ED2A934DE79D9816B26837F');
