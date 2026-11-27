-- ==============================================================
--  Tabla de clientes para BaseDeDatosCliente (persistencia en BD)
--  Ejecutar UNA vez en la base de datos del proyecto (MySQL).
--  No modifica las tablas libros ni factura.
-- ==============================================================

CREATE TABLE IF NOT EXISTS clientes (
    id_cliente          INT           NOT NULL AUTO_INCREMENT,
    tipo_cliente        VARCHAR(20)   NOT NULL,            -- REGULAR | PREMIUM | ADMIN
    primer_nombre       VARCHAR(60)   NOT NULL,
    otros_nombres       VARCHAR(60),
    primer_apellido     VARCHAR(60)   NOT NULL,
    otros_apellidos     VARCHAR(60),
    tipo_identificacion VARCHAR(20)   NOT NULL,
    identificacion      VARCHAR(30)   NOT NULL,
    correo_electronico  VARCHAR(120)  NOT NULL,
    celular             VARCHAR(20),
    direccion           VARCHAR(150),
    contrasenia         VARCHAR(100),
    fecha_registro      TIMESTAMP     NULL,
    intentos_fallidos   INT           NOT NULL DEFAULT 0,
    PRIMARY KEY (id_cliente),
    UNIQUE KEY uk_clientes_identificacion (identificacion)
);

-- Para revisar los datos guardados:
-- SELECT id_cliente, tipo_cliente, primer_nombre, primer_apellido, identificacion, correo_electronico
-- FROM clientes;
