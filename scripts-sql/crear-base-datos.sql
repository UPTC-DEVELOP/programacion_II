-- Script principal para crear la estructura de la base de datos

/********* 1. CREAR LA BASE DE DATOS *********/

-- Crea la base de datos de las personas
-- Colación es_0900_ai_ci: insensible a tildes y mayúsculas
CREATE DATABASE IF NOT EXISTS tienda_libros_gr9
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_es_0900_ai_ci;


/********* 2. CREAR LOS USUARIOS *********/

USE tienda_libros_gr9;

-- Crear el usuario de la aplicación. 
-- O sea, el usuario genérico que desde la aplicación se conecta a la la base de datos
CREATE USER 'gr9_aplicacion'@'%' 
  IDENTIFIED BY 'gr9_clave_desarrollo';

GRANT SELECT, INSERT, UPDATE, DELETE 
  ON tienda_libros_gr9.* 
  TO 'gr9_aplicacion'@'%';

/********* 3. CREAR LAS TABLAS *********/

--         Crear la tabla Editoriales
USE tienda_libros_gr9;

CREATE TABLE IF NOT EXISTS editorial (
  id_editorial BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(60) NOT NULL,
  PRIMARY KEY (id_editorial)
);

-- restricción de nombre único

ALTER TABLE editorial
  ADD CONSTRAINT uq_editorial_nombre_unico 
  UNIQUE KEY (nombre);
  
-- restricciones de texto no vacío
ALTER TABLE editorial 
  ADD CONSTRAINT ck_editorial_nombre_no_vacio 
  CHECK (CHAR_LENGTH(TRIM(nombre)) > 0);

