DROP DATABASE IF EXISTS tienda_virtual;
CREATE DATABASE tienda_virtual CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tienda_virtual;

-- 1. Tabla de usuarios
CREATE TABLE usuarios (
    id VARCHAR(30) PRIMARY KEY,
    nombre_completo VARCHAR(120) NOT NULL,
    correo VARCHAR(120) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    telefono VARCHAR(30),
    departamento VARCHAR(80),
    ciudad VARCHAR(80),
    direccion VARCHAR(180),
    tipo_cliente VARCHAR(30) NOT NULL DEFAULT 'ESTANDAR'
);

-- 2. Tabla de libros (IVA de 5% o 19%)
CREATE TABLE libros (
    isbn VARCHAR(30) PRIMARY KEY,
    titulo VARCHAR(180) NOT NULL,
    autor VARCHAR(120) NOT NULL,
    anio INT NOT NULL,
    categoria VARCHAR(80) NOT NULL,
    editorial VARCHAR(100) NOT NULL,
    paginas INT NOT NULL,
    precio DECIMAL(12, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    formato VARCHAR(20) NOT NULL DEFAULT 'Físico',
    porcentaje_iva DECIMAL(5, 2) NOT NULL DEFAULT 5.00
);

-- 3. Tabla de items en el carrito por usuario
CREATE TABLE carrito_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id VARCHAR(30) NOT NULL,
    isbn VARCHAR(30) NOT NULL,
    cantidad INT NOT NULL,
    CONSTRAINT fk_carrito_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_carrito_libro FOREIGN KEY (isbn) REFERENCES libros(isbn),
    CONSTRAINT uq_carrito_usuario_libro UNIQUE (usuario_id, isbn)
);

-- 4. Tabla de compras (incluye correlativo por cliente 1, 2, 3...)
CREATE TABLE compras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id VARCHAR(30) NOT NULL,
    numero_compra_cliente INT NOT NULL,
    fecha_compra TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metodo_pago VARCHAR(100) NOT NULL,
    total DECIMAL(12, 2) NOT NULL,
    CONSTRAINT fk_compras_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT uq_compra_cliente_correlativo UNIQUE (usuario_id, numero_compra_cliente)
);

-- 5. Tabla de detalle de cada compra
CREATE TABLE compra_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    compra_id INT NOT NULL,
    isbn VARCHAR(30) NOT NULL,
    titulo VARCHAR(180) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(12, 2) NOT NULL,
    porcentaje_iva DECIMAL(5, 2) NOT NULL DEFAULT 5.00,
    subtotal DECIMAL(12, 2) NOT NULL,
    CONSTRAINT fk_compra_items_compra FOREIGN KEY (compra_id) REFERENCES compras(id) ON DELETE CASCADE,
    CONSTRAINT fk_compra_items_libro FOREIGN KEY (isbn) REFERENCES libros(isbn)
);

-- Datos iniciales de libros (con IVA 5% o 19%)
INSERT INTO libros (isbn, titulo, autor, anio, categoria, editorial, paginas, precio, stock, formato, porcentaje_iva) VALUES
('978-0132350884', 'Clean Code', 'Robert C. Martin', 2008, 'Ingeniería de Software', 'Prentice Hall', 464, 180000.00, 10, 'Físico', 5.00),
('978-0134685991', 'Effective Java', 'Joshua Bloch', 2018, 'Programación', 'Addison-Wesley', 416, 120000.00, 9, 'Físico', 5.00),
('978-0135957059', 'The Pragmatic Programmer', 'Andrew Hunt, David Thomas', 2019, 'Desarrollo de Software', 'Addison-Wesley', 352, 150000.00, 11, 'Digital', 19.00),
('978-0201633610', 'Design Patterns', 'Erich Gamma et al.', 1994, 'Arquitectura de Software', 'Addison-Wesley', 395, 165000.00, 8, 'Físico', 5.00),
('978-8491293545', 'Twisted Love', 'Ana Huang', 2022, 'Romance', 'Crossbooks', 416, 65000.00, 15, 'Físico', 5.00);