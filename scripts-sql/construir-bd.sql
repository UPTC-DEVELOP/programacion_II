-- Script maestro o principal para crear la base de datos y cargar los datos de ejemplo.
-- Este script se debe ejecutar en DBeaver con el comando 'Execute SQL Script (Alt+X)'.
-- Ejecutar con un usuario administrador, por ejemplo, root.

-- Paso 0: Eliminar la base de datos anterior si existe.
-- Y así este script se puede ejecutar varias veces y siempre deja la misma estructura actual
DROP DATABASE IF EXISTS tienda_libros_gr9;

-- Paso 1: Crear la base de datos
@include crear-base-datos.sql

USE tienda_libros_gr9;

-- Paso 2: insertar los datos: primero, las tablas padre, y luego las tablas hijas

-- 2a. categoría , editorial, libro y formato_libro
@include insertar-categoria.sql
@include insertar-editorial.sql
@include insertar-libro.sql
@include insertar-formato_libro.sql

-- 2b. rol_permiso, usuario, empleado, usuario_rol, cliente
@include insertar-rol_permiso.sql
@include insertar-usuario.sql
@include insertar-usuario_rol.sql
@include insertar-empleado.sql
@include insertar-cliente.sql
