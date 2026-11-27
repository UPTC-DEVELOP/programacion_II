# Tienda Virtual de Libros (Programación II, 2026-II)

Aplicación de escritorio en Java (Swing) para gestionar libros, clientes y ventas.

## Requisitos
- Java 17
- Maven (Eclipse lo trae con m2e)
- MySQL con la base `tienda_virtual_libros` (tablas `clientes`, `libros` y `ventas`)

## Configuración de la base de datos
1. Copie `config.properties.example` como `config.properties`.
2. Escriba en `db.password` su contraseña de MySQL.
3. `config.properties` está en `.gitignore`, así que no se sube a Git.

Alternativa: definir las variables de entorno `DB_URL`, `DB_USER` y `DB_PASSWORD`.

## Cómo ejecutar
- Aplicación completa (inicio de sesión y menú): `com.tiendavirtual.gui.Principal`
- Solo CRUD de libros: `com.tiendavirtual.Main`
- Solo ventas: `com.tiendavirtual.MainVentas`
- Probar la conexión: `com.tiendavirtual.util.PruebaConexion`
- Pruebas del CRUD de libros en memoria: `com.tiendavirtual.pruebas.PruebasCrud`

## Estructura (`src/com/tiendavirtual`)
| Paquete | Contenido |
|---|---|
| `gui` | Ventanas Swing |
| `controlador` | Conectan las ventanas con la lógica |
| `negocio` | Reglas y validaciones |
| `repositorio` | Acceso a datos (MySQL, memoria y JSON) |
| `modelo` | Clases de datos: `Libro`, `Cliente`, `ClientePremium`, `Venta` |
| `util` | Conexión a la base de datos y cálculo de IVA |
| `pruebas` | Pruebas manuales |
