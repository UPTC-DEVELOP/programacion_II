BEGIN;
--*FUNCIONES TRIGGERS: tienda virtual de libros
-- version: 1.2
-- motor postgreSQL 18
-- @author: Equipo de desarrollo grupo 7 UPTC
-- *--
-- se elimino el trigger mal hecho y se actualzo segun el proyecto y el DAD.
DROP TRIGGER IF EXISTS trg_asignar_rol_por_correo ON clients;
CREATE TRIGGER trg_asignar_rol_por_correo
BEFORE INSERT OR UPDATE OF correo_electronico
ON clients
FOR EACH ROW
EXECUTE FUNCTION asignar_rol_por_correo();

--  Eliminar el trigger si ya existe para permitir recargas limpias
DROP TRIGGER IF EXISTS trg_verificar_ascenso_premium ON buys;

--  Crear el trigger que se ejecuta automáticamente al insertar una venta
CREATE TRIGGER trg_verificar_ascenso_premium
AFTER INSERT
ON buys
FOR EACH ROW
EXECUTE FUNCTION actualizar_tipo_cliente_premium();

--  Opcional: Actualizar de una vez a los clientes existentes que ya cumplan la regla
UPDATE clients c
SET tipo_cliente = 'Premium'
WHERE c.tipo_cliente = 'Regular'
  AND (
        SELECT COUNT(*)
          FROM buys b
         WHERE b.id_cliente = c.id_cliente
      ) >= 2;

COMMIT;