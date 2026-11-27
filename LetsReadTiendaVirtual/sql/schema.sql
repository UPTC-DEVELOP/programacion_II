-- Script de creacion de la base de datos Let's Read
-- Tabla libro: coincide con la clase com.letsread.model.Libro

CREATE DATABASE IF NOT EXISTS lets_read_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE lets_read_db;

CREATE TABLE IF NOT EXISTS libro (
  isbn                VARCHAR(17)   NOT NULL,
  titulo              VARCHAR(150)  NOT NULL,
  autor               VARCHAR(100)  NOT NULL,
  anio_publicacion    INT           NOT NULL,
  categoria           VARCHAR(50)   NOT NULL,
  editorial           VARCHAR(80)   NOT NULL,
  numero_paginas      INT           NOT NULL,
  precio_venta        DECIMAL(10,2) NOT NULL,
  porcentaje_iva      DECIMAL(5,2)  NOT NULL,
  cantidad_inventario INT           NOT NULL,
  formato             VARCHAR(10)   NOT NULL,
  tiene_ventas        TINYINT(1)    NOT NULL DEFAULT 0,
  PRIMARY KEY (isbn)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;