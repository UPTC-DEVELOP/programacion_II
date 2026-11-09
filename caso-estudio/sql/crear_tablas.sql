CREATE DATABASE IF NOT EXISTS caso_estudio;
USE caso_estudio;

CREATE TABLE IF NOT EXISTS libros (
    isbn VARCHAR(20) PRIMARY KEY,
    titulo_libro VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    fecha_publicacion INT NOT NULL,
    categoria VARCHAR(30) NOT NULL,
    editorial VARCHAR(100),
    paginas INT NOT NULL,
    precio_venta DECIMAL(10,2) NOT NULL,
    cantidad_disponible INT NOT NULL DEFAULT 0,
    formato VARCHAR(30) NOT NULL
);