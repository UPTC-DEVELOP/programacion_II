# Tienda Virtual de Libros (Grupo 20)

Proyecto de Programación II, UPTC. Aplicación de escritorio en Java 21 con Swing para administrar los libros, los clientes, el carrito y las compras de una librería.

## Qué hace

- **Libros:** registrar, actualizar, eliminar y listar libros (ISBN, título, autor, año, categoría, editorial, páginas, precio, IVA de 5 o 19 % y stock). Un libro que ya tiene ventas no se puede eliminar.
- **Calificaciones:** un cliente que compró un libro puede calificarlo de 1 a 5 y dejar un comentario. El listado de libros muestra el promedio de cada uno.
- **Clientes:** añadir, buscar, cambiar, borrar y listar clientes. El cliente inicia sesión con su correo y puede ver su historial de compras.
- **Carrito:** cada cliente tiene su propio carrito. No deja pedir más unidades que el stock y calcula subtotal, IVA, descuento y total.
- **Compras:** para finalizar la compra el cliente debe tener la sesión iniciada. Se muestra el resumen del pedido, se elige el método de pago (simulado), se genera el recibo y se descuenta el inventario.
- **Descuentos:** los clientes Premium tienen 10 % y los clientes frecuentes (3 compras o más) tienen 5 % adicional.
- **Registro de operaciones:** las operaciones de libros, clientes, carrito y compras quedan en `datos/operaciones.txt`.

Todo se guarda en MariaDB y también en la carpeta `datos/`: `libros.json`, `clientes.json`, `carrito.json`, `compras.json` y `calificaciones.json`, más los recibos en `datos/recibos/`.

## Cómo ejecutarlo

1. Encender MySQL/MariaDB en XAMPP.
2. Ejecutar `Grupo20_TiendaVirtual/sql/tienda_virtual.sql` en phpMyAdmin. Crea la base `tienda_virtual` con las tablas `libros`, `cliente`, `carrito`, `compra`, `detalle_compra` y `calificacion`. Si la base ya existe, agrega lo que falta sin borrar los datos.
3. Revisar `Grupo20_TiendaVirtual/db.properties`. Por defecto usa `root` sin clave en `localhost:3306`.
4. En Eclipse: File > Import > Existing Maven Projects y elegir la carpeta `Grupo20_TiendaVirtual`.
5. Ejecutar `co.edu.uptc.tienda.gui.VentanaPrincipal`.

## Organización del código

Paquetes dentro de `co.edu.uptc.tienda`: `modelo`, `interfaz`, `negocio`, `persistencia`, `gui` y `excepcion`.

### Validaciones y consultas del modelo

- Consulta de disponibilidad de libros.
- Validación de ISBN registrado.
- Validación de ventas antes de eliminar un libro.
- Validación de compra previa para calificar un libro.
- Identificación de clientes Premium y clientes frecuentes.
- Validación del formato del correo, correo repetido y celular numérico.
- Consulta de correo electrónico y dirección de envío.
- Consulta de cantidades y subtotales del carrito.
- Cálculo de impuestos del carrito.
- Consulta de información de los libros agregados.
- Validación de los registros del carrito.
- Validación de la sesión del cliente antes de finalizar la compra.

## Integrantes

- Maicol Gutierrez: módulo de libros y calificaciones.
- Nilson Pulido: módulo de clientes, inicio de sesión e historial de compras.
- Tomas Rojas: carrito, compras, conexión a la base de datos e integración.
