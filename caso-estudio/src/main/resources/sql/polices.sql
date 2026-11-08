BEGIN;
--*POLITICAS DE ALMACENAMIENTO Y RLS: tienda virtual de libros
-- version: 1.2
-- motor postgreSQL 18
-- @author: Equipo de desarrollo grupo 7 UPTC
-- *--
-- ------------------------------------------------------------
-- RLS: Row Level Security (Protección a nivel de fila)
ALTER TABLE clients ENABLE ROW LEVEL SECURITY;
ALTER TABLE books ENABLE ROW LEVEL SECURITY;
ALTER TABLE buys ENABLE ROW LEVEL SECURITY;
ALTER TABLE sale_detais ENABLE ROW LEVEL SECURITY;
ALTER TABLE product_reviews ENABLE ROW LEVEL SECURITY;
ALTER TABLE shopping_carts ENABLE ROW LEVEL SECURITY;
ALTER TABLE cart_items ENABLE ROW LEVEL SECURITY;

-- Limpieza de políticas previas para permitir re-ejecución limpia
DROP POLICY IF EXISTS clients_select_self_or_admin ON clients;
DROP POLICY IF EXISTS clients_insert_self_or_admin ON clients;
DROP POLICY IF EXISTS clients_update_self_or_admin ON clients;
DROP POLICY IF EXISTS clients_delete_admin_only ON clients;

DROP POLICY IF EXISTS books_read_authenticated ON books;
DROP POLICY IF EXISTS books_admin_write ON books;

DROP POLICY IF EXISTS buys_select_owner_or_admin ON buys;
DROP POLICY IF EXISTS buys_insert_owner_or_admin ON buys;
DROP POLICY IF EXISTS buys_update_admin_only ON buys;
DROP POLICY IF EXISTS buys_delete_admin_only ON buys;

DROP POLICY IF EXISTS sale_details_owner_or_admin ON sale_detais;
DROP POLICY IF EXISTS sale_details_insert_owner_or_admin ON sale_detais;
DROP POLICY IF EXISTS sale_details_admin_write ON sale_detais;
DROP POLICY IF EXISTS sale_details_admin_delete ON sale_detais;

DROP POLICY IF EXISTS leer_reviews ON product_reviews;
DROP POLICY IF EXISTS reviews_insert_owner ON product_reviews;
DROP POLICY IF EXISTS actualizaar_reviews_por_admin_o_owner ON product_reviews;
DROP POLICY IF EXISTS elimminar_reviews_por_admin_o_onswer ON product_reviews;

DROP POLICY IF EXISTS cart_owner_or_admin ON shopping_carts;
DROP POLICY IF EXISTS cart_items_owner_or_admin ON cart_items;

-- POLÍTICAS DE ALMACENAMIENTO

-- CLIENTES
CREATE POLICY clients_select_self_or_admin ON clients
FOR SELECT USING (correo_electronico = app_email() OR app_es_admin());

CREATE POLICY clients_insert_self_or_admin ON clients
FOR INSERT WITH CHECK (correo_electronico = app_email() OR app_es_admin());

CREATE POLICY clients_update_self_or_admin ON clients
FOR UPDATE USING (correo_electronico = app_email() OR app_es_admin())
WITH CHECK (correo_electronico = app_email() OR app_es_admin());

CREATE POLICY clients_delete_admin_only ON clients
FOR DELETE USING (app_es_admin());

-- LIBROS
CREATE POLICY books_read_authenticated ON books
FOR SELECT USING (app_email() IS NOT NULL AND app_email() <> '');

CREATE POLICY books_admin_write ON books
FOR ALL USING (app_es_admin())
WITH CHECK (app_es_admin());

-- VENTAS
CREATE POLICY buys_select_owner_or_admin ON buys
FOR SELECT USING (
    app_es_admin() OR EXISTS (
        SELECT 1 FROM clients c
         WHERE c.id_cliente = buys.id_cliente
           AND c.correo_electronico = app_email()
    )
);

CREATE POLICY buys_insert_owner_or_admin ON buys
FOR INSERT WITH CHECK (
    app_es_admin() OR EXISTS (
        SELECT 1 FROM clients c
         WHERE c.id_cliente = buys.id_cliente
           AND c.correo_electronico = app_email()
    )
);

CREATE POLICY buys_update_admin_only ON buys
FOR UPDATE USING (app_es_admin()) WITH CHECK (app_es_admin());

CREATE POLICY buys_delete_admin_only ON buys
FOR DELETE USING (app_es_admin());

-- DETALLES DE VENTAS
CREATE POLICY sale_details_owner_or_admin ON sale_detais
FOR SELECT USING (
    app_es_admin() OR EXISTS (
        SELECT 1
          FROM buys b
          JOIN clients c ON c.id_cliente = b.id_cliente
         WHERE b.id_venta = sale_detais.id_venta
           AND c.correo_electronico = app_email()
    )
);

CREATE POLICY sale_details_insert_owner_or_admin ON sale_detais
FOR INSERT WITH CHECK (
    app_es_admin() OR EXISTS (
        SELECT 1
          FROM buys b
          JOIN clients c ON c.id_cliente = b.id_cliente
         WHERE b.id_venta = sale_detais.id_venta
           AND c.correo_electronico = app_email()
    )
);

CREATE POLICY sale_details_admin_write ON sale_detais
FOR UPDATE USING (app_es_admin()) WITH CHECK (app_es_admin());

CREATE POLICY sale_details_admin_delete ON sale_detais
FOR DELETE USING (app_es_admin());

-- RESEÑAS (RF19 y RF20)
CREATE POLICY leer_reviews ON product_reviews
FOR SELECT USING (app_email() IS NOT NULL AND app_email() <> '');

CREATE POLICY reviews_insert_owner ON product_reviews
FOR INSERT WITH CHECK (
    EXISTS (
        SELECT 1 FROM clients c
         WHERE c.id_cliente = product_reviews.id_cliente
           AND c.correo_electronico = app_email()
    )
);

CREATE POLICY actualizaar_reviews_por_admin_o_owner ON product_reviews
FOR UPDATE USING (
    app_es_admin() OR EXISTS (
        SELECT 1 FROM clients c
         WHERE c.id_cliente = product_reviews.id_cliente
           AND c.correo_electronico = app_email()
    )
)
WITH CHECK (
    app_es_admin() OR EXISTS (
        SELECT 1 FROM clients c
         WHERE c.id_cliente = product_reviews.id_cliente
           AND c.correo_electronico = app_email()
    )
);

CREATE POLICY elimminar_reviews_por_admin_o_onswer ON product_reviews
FOR DELETE USING (
    app_es_admin() OR EXISTS (
        SELECT 1 FROM clients c
         WHERE c.id_cliente = product_reviews.id_cliente
           AND c.correo_electronico = app_email()
    )
);

-- CARRITO DE COMPRAS (Políticas RLS)
CREATE POLICY cart_owner_or_admin ON shopping_carts
FOR ALL USING (
    app_es_admin() OR EXISTS (
        SELECT 1 FROM clients c
         WHERE c.id_cliente = shopping_carts.id_cliente
           AND c.correo_electronico = app_email()
    )
)
WITH CHECK (
    app_es_admin() OR EXISTS (
        SELECT 1 FROM clients c
         WHERE c.id_cliente = shopping_carts.id_cliente
           AND c.correo_electronico = app_email()
    )
);

CREATE POLICY cart_items_owner_or_admin ON cart_items
FOR ALL USING (
    app_es_admin() OR EXISTS (
        SELECT 1 FROM shopping_carts sc
        JOIN clients c ON c.id_cliente = sc.id_cliente
         WHERE sc.id_carrito = cart_items.id_carrito
           AND c.correo_electronico = app_email()
    )
)
WITH CHECK (
    app_es_admin() OR EXISTS (
        SELECT 1 FROM shopping_carts sc
        JOIN clients c ON c.id_cliente = sc.id_cliente
         WHERE sc.id_carrito = cart_items.id_carrito
           AND c.correo_electronico = app_email()
    )
);

COMMIT;