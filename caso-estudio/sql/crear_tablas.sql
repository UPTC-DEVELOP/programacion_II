USE caso_estudio;

CREATE TABLE IF NOT EXISTS carrito (
    id_producto VARCHAR(50) PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DOUBLE NOT NULL
);