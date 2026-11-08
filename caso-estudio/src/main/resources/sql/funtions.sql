BEGIN;
--*FUNCIONES PARA MANEJO DE ROLES Y TIPO DE CLIENTE: tienda virtual de libros
-- version: 1.2
-- motor postgreSQL 18
-- @author: Equipo de desarrollo grupo 7 UPTC
-- *--
-- Función y Trigger para asignar rol según dominio del correo (@uptc.edu.co)
CREATE OR REPLACE FUNCTION asignar_rol_por_correo()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    v_codigo_rol VARCHAR(10);
BEGIN
    NEW.correo_electronico := lower(trim(NEW.correo_electronico));

    IF NEW.correo_electronico ~ '^[^@[:space:]]+@uptc[.]edu[.]co$' THEN
        v_codigo_rol := 'ADMIN';
    ELSE
        v_codigo_rol := 'USER';
    END IF;

    SELECT id_rol
      INTO NEW.rol_id
      FROM profiles_rols
     WHERE codigo = v_codigo_rol;

    RETURN NEW;
END;
$$;

-- Funciones auxiliares de sesión para RLS
CREATE OR REPLACE FUNCTION app_email()
RETURNS TEXT
LANGUAGE sql
STABLE
AS $$
    SELECT lower(trim(current_setting('app.user_email', true)))
$$;

CREATE OR REPLACE FUNCTION app_es_admin()
RETURNS BOOLEAN
LANGUAGE sql
STABLE
SECURITY DEFINER
SET search_path = public, pg_temp
AS $$
    SELECT EXISTS (
        SELECT 1
          FROM clients c
          JOIN profiles_rols r ON r.id_rol = c.rol_id
         WHERE lower(c.correo_electronico) = app_email()
           AND r.codigo = 'ADMIN'
    )
$$;

--  Función que evalúa el número de compras y actualiza el tipo de cliente
CREATE OR REPLACE FUNCTION actualizar_tipo_cliente_premium()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    v_total_compras INT;
BEGIN
    -- Contar cuántas compras tiene el cliente que acaba de comprar
    SELECT COUNT(*)
      INTO v_total_compras
      FROM buys
     WHERE id_cliente = NEW.id_cliente;

    -- Si el cliente tiene 2 o más compras y aún es 'Regular', se asciende a 'Premium'
    IF v_total_compras >= 2 THEN
        UPDATE clients
           SET tipo_cliente = 'Premium'
         WHERE id_cliente = NEW.id_cliente
           AND tipo_cliente = 'Regular';
    END IF;

    RETURN NEW;
END;
$$;

COMMIT;