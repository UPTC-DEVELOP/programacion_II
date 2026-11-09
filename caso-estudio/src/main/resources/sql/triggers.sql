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

-- ACTUALIZA CORREOS ELECTRONICOS DE CLIENTES ADMINS CON DOMINIO @co.edu.uptc A @uptc.edu.co
UPDATE clients c
   SET correo_electronico = regexp_replace(lower(trim(c.correo_electronico)),
                                            '@co[.]edu[.]uptc$', '@uptc.edu.co')
 WHERE lower(trim(c.correo_electronico)) ~ '@co[.]edu[.]uptc$'
   AND NOT EXISTS (
       SELECT 1 FROM clients existente
        WHERE lower(trim(existente.correo_electronico)) = regexp_replace(
            lower(trim(c.correo_electronico)), '@co[.]edu[.]uptc$', '@uptc.edu.co')
   );

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

--- DESCUENTO DE STOCK POR VENTA
DROP TRIGGER IF EXISTS trg_descontar_stock_por_venta ON sale_detais;
CREATE TRIGGER trg_descontar_stock_por_venta
BEFORE INSERT ON sale_detais
FOR EACH ROW
EXECUTE FUNCTION descontar_stock_por_venta();

DROP TRIGGER IF EXISTS trg_auditar_compra ON buys;
CREATE TRIGGER trg_auditar_compra
AFTER INSERT ON buys
FOR EACH ROW
EXECUTE FUNCTION registrar_auditoria_compra();

COMMIT;
