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
    id_cliente VARCHAR(20) NOT NULL,
    isbn VARCHAR(13) NOT NULL,
    cantidad INT NOT NULL,
    PRIMARY KEY (id_cliente, isbn)
);

DELETE FROM carrito WHERE id_cliente IS NULL;

ALTER TABLE carrito DROP FOREIGN KEY IF EXISTS fk_carrito_cliente;

ALTER TABLE carrito
    MODIFY id_cliente VARCHAR(20) NOT NULL,
    ADD INDEX IF NOT EXISTS idx_carrito_isbn (isbn),
    DROP PRIMARY KEY,
    ADD PRIMARY KEY (id_cliente, isbn);

ALTER TABLE carrito
    ADD CONSTRAINT fk_carrito_libro FOREIGN KEY IF NOT EXISTS (isbn)
        REFERENCES libros (isbn) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE carrito
    ADD CONSTRAINT fk_carrito_cliente FOREIGN KEY IF NOT EXISTS (id_cliente)
        REFERENCES cliente (id) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE IF NOT EXISTS compra (
    numero INT NOT NULL,
    id_cliente VARCHAR(20) NOT NULL,
    nombre_cliente VARCHAR(100) NOT NULL,
    fecha DATETIME NOT NULL,
    metodo_pago VARCHAR(40) NOT NULL,
    subtotal DOUBLE NOT NULL,
    impuestos DOUBLE NOT NULL,
    porcentaje_descuento DOUBLE NOT NULL,
    descuento DOUBLE NOT NULL,
    total DOUBLE NOT NULL,
    PRIMARY KEY (numero),
    INDEX idx_compra_cliente (id_cliente)
);

CREATE TABLE IF NOT EXISTS detalle_compra (
    numero_compra INT NOT NULL,
    isbn VARCHAR(13) NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    iva_porcentaje DOUBLE NOT NULL,
    PRIMARY KEY (numero_compra, isbn),
    INDEX idx_detalle_isbn (isbn),
    CONSTRAINT fk_detalle_compra FOREIGN KEY (numero_compra)
        REFERENCES compra (numero) ON DELETE CASCADE
);
