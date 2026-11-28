-- =========================================================
-- Tienda Virtual de Libros - Grupo 5
-- Datos de demostracion (SQLite)
-- Ejecutar despues de schema.sql
-- =========================================================

-- ---------------------------------------------------------
-- Clientes
-- ---------------------------------------------------------
INSERT OR REPLACE INTO clientes
    (correo, nombre, direccion, telefono, contrasena, tipo)
VALUES
    ('laura@uptc.edu.co', 'Laura Gomez', 'Calle 20 #10-15', '310 555 7788', '1234', 'Premium');

INSERT OR REPLACE INTO clientes
    (correo, nombre, direccion, telefono, contrasena, tipo)
VALUES
    ('carlos@uptc.edu.co', 'Carlos Perez', 'Calle 25 #12-40', '311 999 8877', '1234', 'Regular');

INSERT OR REPLACE INTO clientes
    (correo, nombre, direccion, telefono, contrasena, tipo)
VALUES
    ('juan@uptc.edu.co', 'Juan Perez', 'Cra 11 #15-20, Tunja', '311 555 2233', '1234', 'Premium');

-- ---------------------------------------------------------
-- Libros
-- ---------------------------------------------------------
INSERT OR REPLACE INTO libros
    (isbn, titulo, autor, anio_publicacion, categoria, editorial,
     numero_paginas, precio, porcentaje_iva, cantidad_disponible, formato)
VALUES
    ('978-001', 'Cien anos de soledad', 'G. Garcia Marquez', 2020, 'Novela',
     'Editorial Demo', 300, 45000, 19.0, 8, 'FISICO');

INSERT OR REPLACE INTO libros
    (isbn, titulo, autor, anio_publicacion, categoria, editorial,
     numero_paginas, precio, porcentaje_iva, cantidad_disponible, formato)
VALUES
    ('978-002', 'El principito', 'A. de Saint-Exupery', 2020, 'Novela',
     'Editorial Demo', 300, 35000, 5.0, 3, 'FISICO');

INSERT OR REPLACE INTO libros
    (isbn, titulo, autor, anio_publicacion, categoria, editorial,
     numero_paginas, precio, porcentaje_iva, cantidad_disponible, formato)
VALUES
    ('978-003', 'Fundamentos de Bases de Datos', 'Luis Martinez', 2022,
     'Tecnologia', 'Editorial U', 280, 40000, 5.0, 15, 'DIGITAL');

-- ---------------------------------------------------------
-- Compra de demostracion
-- 2 unidades de 978-001: subtotal 90000, IVA 17100,
-- descuento Premium 9000, total 98100.
-- ---------------------------------------------------------
INSERT OR REPLACE INTO compras
    (id, fecha, metodo_pago, correo_cliente, total)
VALUES
    (1, '2026-11-28', 'Efectivo', 'laura@uptc.edu.co', 98100.0);

DELETE FROM detalle_compra WHERE compra_id = 1;

INSERT INTO detalle_compra
    (compra_id, isbn, titulo, cantidad, precio_unitario, porcentaje_iva)
VALUES
    (1, '978-001', 'Cien anos de soledad', 2, 45000.0, 19.0);
