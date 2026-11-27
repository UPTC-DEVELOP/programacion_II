-- Script principal para crear la estructura de la base de datos

/********* 1. CREAR LA BASE DE DATOS *********/

-- Crea la base de datos de la tienda de libros
-- Colación es español insensible a tildes y mayúsculas
CREATE DATABASE IF NOT EXISTS tienda_libros_gr9
  CHARACTER SET utf8mb4
COLLATE utf8mb4_spanish_ci;

/********* 2. CREAR LOS USUARIOS *********/

USE tienda_libros_gr9;

-- Crear el usuario de la aplicación. 
-- O sea, el usuario genérico que desde la aplicación se conecta a la la base de datos
CREATE USER IF NOT EXISTS 'gr9_aplicacion'@'%' 
  IDENTIFIED BY 'gr9_clave_desarrollo';

GRANT SELECT, INSERT, UPDATE, DELETE 
  ON tienda_libros_gr9.* 
  TO 'gr9_aplicacion'@'%';

/********* 3. CREAR LAS TABLAS *********/

--         Crear la tabla Editorial
USE tienda_libros_gr9;

CREATE TABLE IF NOT EXISTS editorial (
  id_editorial BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(60) NOT NULL,
  
  -- restricción de clave primaria
  PRIMARY KEY (id_editorial),

  -- restricciones de valores únicos  
  CONSTRAINT uq_editorial_nombre_unico 
    UNIQUE KEY (nombre),
  
  -- restricciones de texto no vacío
  CONSTRAINT ck_editorial_nombre_no_vacio 
    CHECK (CHAR_LENGTH(TRIM(nombre)) > 0)
);

--         Crear la tabla Categoria

CREATE TABLE IF NOT EXISTS categoria (
  id_categoria BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(60) NOT NULL,
  descripcion VARCHAR(255) NOT NULL,
  codigo VARCHAR(20) NOT NULL,

  PRIMARY KEY (id_categoria),

  CONSTRAINT uq_categoria_codigo UNIQUE (codigo),
  CONSTRAINT uq_categoria_nombre UNIQUE (nombre)
);

--         Crear la tabla Libro

CREATE TABLE IF NOT EXISTS libro (
  id_libro BIGINT NOT NULL AUTO_INCREMENT,
  isbn VARCHAR (17) NOT NULL,
  titulo VARCHAR (255) NOT NULL,
  anio_publicacion INT NOT NULL,
  imagen_libro VARCHAR (255) NOT NULL,
  id_categoria BIGINT NOT NULL,
  id_editorial BIGINT NOT NULL,
  
  -- restricción de clave primaria
  PRIMARY KEY (id_libro),

  -- restricciones de valores únicos
  CONSTRAINT uq_libro_isbn_unico UNIQUE (isbn),

  -- restricciones de clave foránea
  CONSTRAINT fk_libro_id_categoria
    FOREIGN KEY (id_categoria)
    REFERENCES categoria (id_categoria)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
    
  CONSTRAINT fk_libro_id_editorial 
    FOREIGN KEY (id_editorial)
    REFERENCES editorial (id_editorial) 
    ON UPDATE CASCADE 
    ON DELETE RESTRICT,
    
  -- restricciones de texto no vacío
  CONSTRAINT ck_libro_isbn_no_vacio
    CHECK  (CHAR_LENGTH(TRIM(isbn)) > 0),
  CONSTRAINT ck_libro_titulo_no_vacio 
    CHECK (CHAR_LENGTH(TRIM(titulo)) > 0)
);
    

--         Crear la tabla formato_libro

CREATE TABLE IF NOT EXISTS formato_libro (
  id_formato_libro BIGINT NOT NULL AUTO_INCREMENT,
  id_libro BIGINT NOT NULL,
  tipo_formato VARCHAR(1) NOT NULL,
  precio_base DECIMAL (16, 2) NOT NULL,
  tipo_iva VARCHAR(10) NOT NULL,
  inventario INT NOT NULL DEFAULT 0,
  cantidad_paginas INT NOT NULL,
  estado_en_catalogo VARCHAR(1) NOT NULL,
  
  -- restricción de clave primaria
  PRIMARY KEY (id_formato_libro),
  
  -- restricción de único: un libro tiene un solo formato de cada tipo
  CONSTRAINT uq_formato_libro_tipo_formato 
    UNIQUE (id_libro, tipo_formato),
  
  
  -- restricciones de clave foránea
  CONSTRAINT fk_formato_libro_id_libro 
    FOREIGN KEY (id_libro) 
    REFERENCES libro (id_libro)
    ON UPDATE CASCADE 
    ON DELETE RESTRICT,
    
  -- restricciones de valores válidos
   
    --  I: impreso, D: digital
  CONSTRAINT ck_formato_libro_tipo_formato_valido 
    CHECK (tipo_formato IN ('I', 'D')),
  
  CONSTRAINT ck_formato_libro_precio_mayor_a_cero 
    CHECK (precio_base > 0.00),
    
  CONSTRAINT ck_formato_libro_tipo_iva_valido 
     CHECK (tipo_iva IN ('IVA_19', 'IVA_5')),
    
  CONSTRAINT ck_formato_libro_inventario_no_negativo 
    CHECK (inventario >= 0),
    
  CONSTRAINT ck_formato_libro_cantidad_paginas_mayor_a_cero 
    CHECK (cantidad_paginas > 0),
  
    --  P: pendiente, D: disponible, S: suspendido
  CONSTRAINT ck_formato_libro_estado_en_catalogo_valido 
    CHECK (estado_en_catalogo IN ('P', 'D', 'S'))
);


--         Crear la tabla usuario

-- hash_contrasena es VARCHAR(44) porque el Base64 de un hash de 32 bytes con PBKDF2 y 256 bits
--  = 44 caracteres

-- sal es VARCHAR(24) porque el Base64 de un hash de 16 bytes con PBKDF2 y 256 bits
--  = 24 caracteres

CREATE TABLE IF NOT EXISTS usuario (
  id_usuario BIGINT NOT NULL AUTO_INCREMENT,
  correo_electronico VARCHAR(120) NOT NULL, 
  hash_contrasena VARCHAR(44) NOT NULL, 
  sal VARCHAR(24) NOT NULL,
  habilitado BOOLEAN NOT NULL DEFAULT TRUE,

  -- Restricción de clave primaria
  PRIMARY KEY (id_usuario),

  -- Restricciones de valores únicos
  CONSTRAINT uq_usuario_correo
    UNIQUE (correo_electronico),
  
  -- Restricción que permite que un cliente tenga la clave foránea compuesta (id_usuario, correo_electronico)
  CONSTRAINT uq_usuario_id_correo
    UNIQUE (id_usuario, correo_electronico),
    
    -- Restricciones de texto no vacío y de longitud
  CONSTRAINT ck_usuario_correo_no_vacio
    CHECK (CHAR_LENGTH(TRIM(correo_electronico)) > 0),

  CONSTRAINT ck_usuario_hash_longitud
    CHECK (CHAR_LENGTH(hash_contrasena) = 44),

  CONSTRAINT ck_usuario_sal_longitud
    CHECK (CHAR_LENGTH(sal) = 24)
);


--         Crear la tabla empleado 

CREATE TABLE IF NOT EXISTS empleado (
  id_empleado BIGINT NOT NULL AUTO_INCREMENT,
  id_usuario BIGINT NOT NULL,
  tipo_documento VARCHAR(3) NOT NULL,
  numero_documento VARCHAR(20) NOT NULL,
  nombres VARCHAR(100) NOT NULL,
  apellidos VARCHAR(100) NOT NULL,
  
  -- restricción de clave primaria
  PRIMARY KEY (id_empleado),
  
  -- restricción de que un usuario aparezca solo una vez como empleado
  CONSTRAINT uq_empleado_usuario
    UNIQUE (id_usuario),
    
  -- restricción de identificación única del empleado 
  CONSTRAINT uq_empleado_identificacion 
    UNIQUE (tipo_documento, numero_documento),
    
  -- restricción de clave foránea
  CONSTRAINT fk_empleado_id_usuario
    FOREIGN KEY (id_usuario)
    REFERENCES usuario (id_usuario)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  
  -- restricciones de valores válidos
  
  CONSTRAINT ck_empleado_tipo_documento_valido 
    CHECK (tipo_documento IN ('CC', 'CE')),
    
  -- restricciones de texto no vacío
  CONSTRAINT ck_empleado_nombres_no_vacios
    CHECK (CHAR_LENGTH(TRIM(nombres)) > 0),

  CONSTRAINT ck_empleado_apellidos_no_vacios
    CHECK (CHAR_LENGTH(TRIM(apellidos)) > 0), 
    
  CONSTRAINT ck_empleado_numero_documento_no_vacio  
    CHECK (CHAR_LENGTH(TRIM(numero_documento)) > 0)
);

-- Trigger para que al eliminar un empleado también se elimine su cuenta de usuario, y no quede 
--   un usuario que inicie sesión pero sin ser empleado.
CREATE TRIGGER IF NOT EXISTS trg_empleado_eliminar_usuario
  AFTER DELETE ON empleado
  FOR EACH ROW
  DELETE FROM usuario WHERE id_usuario = OLD.id_usuario;


--         Crear la tabla usuario_rol 

-- el rol de cada usuario
-- un usuario puede tener varios roles

CREATE TABLE IF NOT EXISTS usuario_rol (
  id_usuario BIGINT NOT NULL,
  rol VARCHAR(3) NOT NULL,
  
  -- restricción de clave primaria
  PRIMARY KEY (id_usuario, rol),
  
  -- restricción de clave foránea
  --   nota: ON DELELE CASCADE para que al eliminar un usuario también se eliminen sus roles asociados
  CONSTRAINT fk_usuario_rol_id_usuario
    FOREIGN KEY (id_usuario)
    REFERENCES usuario (id_usuario)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  
  -- restricción de valores válidos
    
  -- CAT: gestor catálogo.  VEN: gestor ventas.  CLI: cliente.  ADM: administrador
  CONSTRAINT ck_usuario_rol_rol_valido 
    CHECK (rol IN ('CAT', 'VEN', 'CLI', 'ADM'))
);


--         Crear la tabla rol_permiso 

-- dice los permisos específicos que cada rol tiene
CREATE TABLE IF NOT EXISTS rol_permiso (
  rol VARCHAR(3) NOT NULL, 
  permiso VARCHAR(62) NOT NULL,
  
  -- restricción de clave primaria
  PRIMARY KEY (rol, permiso),
  
  -- restricción de valores válidos
  
  -- CAT: gestor catálogo.  VEN: gestor ventas.  CLI: cliente.  ADM: administrador
  CONSTRAINT ck_rol_permiso_rol_valido 
    CHECK (rol IN ('CAT', 'VEN', 'CLI', 'ADM'))
);


--         Crear la tabla cliente 

-- nota: ya no se hace el CHECK para correo_electronico porque MariaDB no permite hacer CHECK en una 
--       clave foránea que tiene ON UPDATE CASCADE. De todas maneras, el CHECK de correo electrónico 
--       se hace en la tabla padre usuario. 

CREATE TABLE IF NOT EXISTS cliente (
  id_cliente BIGINT NOT NULL AUTO_INCREMENT,
  id_usuario BIGINT NOT NULL,
  tipo_documento VARCHAR(10) NOT NULL,
  numero_documento VARCHAR(20) NOT NULL,
  nombres VARCHAR(100) NOT NULL,
  apellidos VARCHAR(100) NOT NULL,
  correo_electronico VARCHAR(120) NOT NULL,
  telefono VARCHAR(20) NOT NULL,
  direccion_envio VARCHAR(200) NOT NULL,
  tipo_cliente VARCHAR(15) NOT NULL,

  -- Restricción de clave primaria
  PRIMARY KEY (id_cliente),

   -- Restricciones de valores únicos
  CONSTRAINT uq_cliente_documento
    UNIQUE (tipo_documento, numero_documento),

  CONSTRAINT uq_cliente_correo
    UNIQUE (correo_electronico),

  -- un usuario es un solo cliente
  CONSTRAINT uq_cliente_usuario
    UNIQUE (id_usuario),
    
  -- restricción de clave foránea
  -- el cliente recibe el correo electrónico de la tabla usuario
  CONSTRAINT fk_cliente_usuario_correo
    FOREIGN KEY (id_usuario, correo_electronico)
    REFERENCES usuario (id_usuario, correo_electronico)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
    
  -- Restricciones de textos no vacíos
  CONSTRAINT ck_cliente_nombres_no_vacios
    CHECK (CHAR_LENGTH(TRIM(nombres)) > 0),

  CONSTRAINT ck_cliente_apellidos_no_vacios
    CHECK (CHAR_LENGTH(TRIM(apellidos)) > 0),

  -- Restricción de tipos de cliente válidos
  CONSTRAINT ck_cliente_tipo_valido
    CHECK (tipo_cliente IN ('REGULAR', 'PREMIUM'))  
);

-- Trigger para que al eliminar un cliente también se elimine su cuenta de usuario, y no quede 
--   un usuario que inicie sesión pero sin ser cliente.
CREATE TRIGGER IF NOT EXISTS trg_cliente_eliminar_usuario
  AFTER DELETE ON cliente
  FOR EACH ROW
  DELETE FROM usuario WHERE id_usuario = OLD.id_usuario;

