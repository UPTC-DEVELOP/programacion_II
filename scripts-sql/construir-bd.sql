-- Script maestro o principal para crear la base de datos y cargos los datos de ejemplo.
-- Este script se debe ejecutar en DBeaver con el comando 'Execute SQL Script (Ctrl+X)'.

-- Paso 1: Crear la base de datos
@include crear-base-datos.sql

USE tienda_libros_gr9;

-- Paso 2: Eliminar datos anteriores: primero, eliminar las tablas hijas

DELETE FROM formato_libro;
DELETE FROM libro;
DELETE FROM categoria;
DELETE FROM editorial;

-- Paso 3: insertar los datos: primero, crear las tablas padre

@include insertar-categoria.sql
@include insertar-editorial.sql
@include insertar-libro.sql
@include insertar-formato_libro.sql
@include insertar-cliente.sql


