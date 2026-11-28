# Tienda Virtual de Libros (Grupo 20)

Proyecto de Programación II, UPTC. Aplicación de escritorio en Java 21 con Swing para administrar los libros, los clientes y el carrito de compras de una librería.

## Qué hace

- **Libros:** registrar, actualizar, eliminar y listar libros (ISBN, título, autor, año, editorial, páginas, precio, IVA de 5 o 19 % y stock).
- **Clientes:** añadir, buscar, cambiar, borrar y listar clientes. Los clientes Premium tienen 10 % de descuento.
- **Carrito:** cada cliente tiene su propio carrito. No deja pedir más unidades que el stock, calcula subtotal, IVA, descuento y total, y al finalizar la compra descuenta el inventario.

Todo se guarda en MariaDB y en archivos JSON de la carpeta `datos/`.

## Cómo ejecutarlo

1. Encender MySQL/MariaDB en XAMPP.
2. Ejecutar `Grupo20_TiendaVirtual/sql/tienda_virtual.sql` en phpMyAdmin. Crea la base `tienda_virtual` con las tablas `libros`, `cliente` y `carrito`.
3. Revisar `Grupo20_TiendaVirtual/db.properties`. Por defecto usa `root` sin clave en `localhost:3306`.
4. En Eclipse: File > Import > Existing Maven Projects y elegir la carpeta `Grupo20_TiendaVirtual`.
5. Ejecutar `co.edu.uptc.tienda.gui.VentanaPrincipal`.

## Organización del código

Paquetes dentro de `co.edu.uptc.tienda`: `modelo`, `interfaz`, `negocio`, `persistencia`, `gui` y `excepcion`.

### Validaciones y consultas del modelo

- Consulta de disponibilidad de libros.
- Validación de ISBN registrado.
- Identificación de clientes Premium.
- Consulta de correo electrónico y dirección de envío.
- Consulta de cantidades y subtotales del carrito.
- Cálculo de impuestos del carrito.
- Consulta de información de los libros agregados.
- Validación de los registros del carrito.

## Integrantes

- Maicol Gutierrez: módulo de libros.
- Nilson Pulido: módulo de clientes.
- Tomas Rojas: carrito, conexión a la base de datos e integración.
