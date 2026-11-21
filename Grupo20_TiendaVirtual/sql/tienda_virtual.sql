CREATE DATABASE IF NOT EXISTS tienda_virtual;

USE tienda_virtual;

CREATE TABLE IF NOT EXISTS libros (
    isbn VARCHAR(13) NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    anio_publicacion INT NOT NULL,
    editorial VARCHAR(100) NOT NULL,
    num_paginas INT NOT NULL,
    precio DOUBLE NOT NULL,
    iva_porcentaje DOUBLE NOT NULL,
    stock INT NOT NULL,
    formato VARCHAR(20),
    PRIMARY KEY (isbn)
);

CREATE TABLE IF NOT EXISTS cliente (
    id VARCHAR(20) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    correo_electronico VARCHAR(100) NOT NULL,
    direccion_envio VARCHAR(150) NOT NULL,
    celular VARCHAR(20) NOT NULL,
    tipo_cliente VARCHAR(20),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS carrito (
    isbn VARCHAR(13) NOT NULL,
    id_cliente VARCHAR(20),
    cantidad INT NOT NULL,
    PRIMARY KEY (isbn)
);
