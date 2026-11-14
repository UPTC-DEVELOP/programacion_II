CREATE DATABASE IF NOT EXISTS tienda_virtual CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tienda_virtual;
CREATE TABLE IF NOT EXISTS usuarios (
 correo VARCHAR(190) PRIMARY KEY, nombre VARCHAR(150) NOT NULL,
 direccion VARCHAR(255), telefono VARCHAR(30), tipo_cliente VARCHAR(60),
 contrasena VARCHAR(255) NOT NULL
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS carrito_items (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, carrito_id VARCHAR(120) NOT NULL,
 titulo VARCHAR(255) NOT NULL, autor VARCHAR(200) NOT NULL,
 formato VARCHAR(80) NOT NULL, precio INT NOT NULL, cantidad INT NOT NULL,
 INDEX idx_carrito (carrito_id), CONSTRAINT chk_precio CHECK (precio >= 0),
 CONSTRAINT chk_cantidad CHECK (cantidad > 0)
) ENGINE=InnoDB;
