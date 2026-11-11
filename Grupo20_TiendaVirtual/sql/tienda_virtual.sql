CREATE DATABASE IF NOT EXISTS tienda_virtual;

USE tienda_virtual;

CREATE TABLE IF NOT EXISTS carrito (
    isbn VARCHAR(13) NOT NULL,
    id_cliente VARCHAR(20),
    cantidad INT NOT NULL,
    PRIMARY KEY (isbn)
);
