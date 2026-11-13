CREATE DATABASE IF NOT EXISTS tienda_virtual;

USE tienda_virtual;

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
