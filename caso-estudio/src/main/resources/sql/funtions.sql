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

-- /*modificacion:Brayan Panqueva*/
-- FUNCION QUE DESCUENTA ES STOCK POR VENTA...
CREATE OR REPLACE FUNCTION descontar_stock_por_venta()
RETURNS TRIGGER
LANGUAGE plpgsql
SECURITY DEFINER
SET search_path = public, pg_temp
AS $$
DECLARE
    v_stock INT;
BEGIN
    SELECT cantidad_inventario INTO v_stock
      FROM books
     WHERE isbn = NEW.isbn_libro
     FOR UPDATE;

    IF v_stock IS NULL THEN
        RAISE EXCEPTION 'El libro % no existe en inventario', NEW.isbn_libro;
    END IF;
    IF NEW.cantidad <= 0 OR NEW.cantidad > v_stock THEN
        RAISE EXCEPTION 'Stock insuficiente para %: solicitado %, disponible %',
            NEW.isbn_libro, NEW.cantidad, v_stock;
    END IF;

    UPDATE books
       SET cantidad_inventario = cantidad_inventario - NEW.cantidad
     WHERE isbn = NEW.isbn_libro;
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION registrar_auditoria_compra()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO audit_logs(id_cliente, id_venta, accion, detalles)
    VALUES (NEW.id_cliente, NEW.id_venta, 'COMPRA',
            'Compra registrada por ' || COALESCE(app_email(), 'usuario no identificado'));
    RETURN NEW;
END;
$$;

COMMIT;
