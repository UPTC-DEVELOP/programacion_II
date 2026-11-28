-- =========================================================
-- Tienda Virtual de Libros - Grupo 5
-- Esquema de base de datos (SQLite)
-- Archivo: datos/tienda.db
-- =========================================================
-- Coincide con las tablas que crean los repositorios:
--   RepositorioClientesBD, RepositorioLibrosBD, RepositorioComprasBD.
-- =========================================================

PRAGMA foreign_keys = ON;

-- ---------------------------------------------------------
-- Clientes
-- tipo: 'Regular' o 'Premium'
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS clientes (
    correo     TEXT PRIMARY KEY,
    nombre     TEXT,
    direccion  TEXT,
    telefono   TEXT,
    contrasena TEXT,
    tipo       TEXT
);

-- ---------------------------------------------------------
-- Libros
-- formato: 'FISICO' o 'DIGITAL'
-- porcentaje_iva: 19.0 o 5.0
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS libros (
    isbn                TEXT PRIMARY KEY,
    titulo              TEXT,
    autor               TEXT,
    anio_publicacion    INTEGER,
    categoria           TEXT,
    editorial           TEXT,
    numero_paginas      INTEGER,
    precio              REAL,
    porcentaje_iva      REAL,
    cantidad_disponible INTEGER,
    formato             TEXT
);

-- ---------------------------------------------------------
-- Compras (cabecera)
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS compras (
    id             INTEGER PRIMARY KEY,
    fecha          TEXT,
    metodo_pago    TEXT,
    correo_cliente TEXT,
    total          REAL
);

-- ---------------------------------------------------------
-- Detalle de compra
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS detalle_compra (
    compra_id       INTEGER,
    isbn            TEXT,
    titulo          TEXT,
    cantidad        INTEGER,
    precio_unitario REAL,
    porcentaje_iva  REAL
);

-- ---------------------------------------------------------
-- Indices de apoyo
-- ---------------------------------------------------------
CREATE INDEX IF NOT EXISTS idx_clientes_tipo
    ON clientes (tipo);

CREATE INDEX IF NOT EXISTS idx_libros_titulo
    ON libros (titulo);

CREATE INDEX IF NOT EXISTS idx_compras_correo
    ON compras (correo_cliente);

CREATE INDEX IF NOT EXISTS idx_detalle_compra
    ON detalle_compra (compra_id);
