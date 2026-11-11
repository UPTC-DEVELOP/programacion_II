# Arquitectura y recorrido del código

## Punto de partida y alcance

La base de TiendaVirtual es el repositorio compartido del Grupo 14. Los módulos de Jhonatan y Elke se incorporaron sin sustituir íntegramente el sistema original. `Main.java` abre las ventanas Java Swing.

```text
TiendaVirtual/
├── pom.xml                    Java 17, controladores JDBC Maven
├── config/database.properties.example
├── sql/esquema.sql            usuarios, carrito_items
├── datos/libros.txt           catálogo de ejemplo
├── src/main/java/
│   ├── util/aplicacion/Main.java
│   ├── vista/                 VistaLogin, VistaRegistro, VistaInicio, VistaCarrito, VistaGestionPedidos...
│   ├── controlador/           ControladorLogin, ControladorInicio, ControladorCarrito, ...
│   ├── modelo/                Libro, Usuario, ItemCarrito, repositorios
│   ├── persistencia/          PersistenciaUsuarios, PersistenciaCarrito y PersistenciaCarritoJdbc
│   ├── configuracion/         ConfiguracionBD
│   └── usuariosjdbc/           modelo, repositorio, conexion y prueba CRUD JDBC de Elke
└── src/test/java/pruebas/PruebaCarrito.java
```

## Flujo de inicio de sesión

`Main` → `VistaLogin` y `ControladorLogin` → `modelo.RepositorioUsuarios.autenticar` → `VistaInicio` y `ControladorInicio`. **Este recorrido usa `datos/usuarios.txt`, no la tabla SQL `usuarios`.**

## Flujo del carrito de Jhonatan

`ControladorLogin` pasa el `modelo.Usuario` autenticado a `ControladorInicio`. Allí se crea `new RepositorioCarrito(usuario.getCorreo())`. El repositorio normaliza el correo y construye un identificador de carrito `usuario:<correo>`. Se abre `VistaCarrito` con `ControladorCarrito`.

El repositorio permite agregar, listar, actualizar cantidad, eliminar y vaciar. `calcularTotal()` suma `precio × cantidad` usando `long`. `ConfiguracionBD.carritoEnJdbc()` selecciona almacenamiento:

- `json`: `PersistenciaCarrito` usa el archivo local `datos/carrito.json`. **Este archivo local no está separado por usuario**.
- `jdbc`: `PersistenciaCarritoJdbc` consulta `carrito_items` filtrando por `carrito_id` y guarda con transacción (`setAutoCommit(false)`, `DELETE` e `INSERT` por lote, `commit` o `rollback`).

El historial antiguo `carrito_id = local` no se migra automáticamente y permanece separado. `RepositorioCarrito()` sin argumento conserva un modo de compatibilidad que internamente usa `usuario:local`; no equivale exactamente al registro histórico `local`.

**Limitaciones:** para encontrar libros ya cargados se comparan título, autor, formato y precio contra el catálogo; el control de duplicados en memoria depende de referencia de objeto (`==`), por lo que hay margen de mejora con ID/ISBN estable.

## CRUD JDBC de Elke

`usuariosjdbc.repositorio.RepositorioUsuarios` expone `guardarUsuario`, `buscarUsuario`, `actualizarUsuario` y `eliminarUsuario`. Se conecta mediante `usuariosjdbc.conexion.ConexionBD` y `configuracion.ConfiguracionBD`; emplea `PreparedStatement` y cierre automático de recursos. La tabla `usuarios` usa `correo` como **clave primaria**, no una columna `id`.

La aplicación original contiene **otra clase distinta** llamada `modelo.RepositorioUsuarios`. Esta se conecta a `persistencia.PersistenciaUsuarios` y `datos/usuarios.txt`. Unificar ambos flujos sin perder cuentas es trabajo pendiente.

## Gestión de pedidos

El módulo pasó de un nombre académico «Evaluación Parte 2» a `VistaGestionPedidos` y `ControladorGestionPedidos`, presentados en Swing como «Gestión de pedidos». Hay que conservar sus reglas funcionales al introducir nuevos cambios.

## Diseño de SQL

- `usuarios`: `correo` (PK), `nombre`, `direccion`, `telefono`, `tipo_cliente`, `contrasena`.
- `carrito_items`: `id` autoincremental, `carrito_id`, `titulo`, `autor`, `formato`, `precio`, `cantidad`; índice por `carrito_id`, restricciones de no negatividad y cantidad positiva.

## Limitación de seguridad

Los usuarios locales y JDBC almacenan contraseñas sin protección de hash. La demostración actual es académica/local. Para uso real se necesita migración a hash de contraseñas con sal, autorización de operaciones y un flujo de recuperación verificado.
