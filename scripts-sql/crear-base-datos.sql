-- Script principal para crear la estructura de la base de datos

/********* 1. CREAR LA BASE DE DATOS *********/

-- Crea la base de datos de la tienda de libros
-- Colación es_0900_ai_ci: insensible a tildes y mayúsculas
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
    

-- crear la tabla formato_libro

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

