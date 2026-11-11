# TiendaVirtual — Grupo 14 · Programación II (UPTC)

Proyecto de escritorio en **Java 17 + Swing + Maven**, desarrollado a partir de la base compartida por el Grupo 14 en el repositorio académico `UPTC-DEVELOP/programacion_II` (rama de trabajo indicada: `feature/grupo14`). Versión Maven: **1.0.18**.

> **Estado:** compilación y pruebas de carrito/JDBC realizadas en CachyOS; **integración del CRUD JDBC de usuarios con las pantallas Swing pendiente**. No presentar este estado como entrega funcional totalmente integrada.

## Responsabilidades

| Integrante | Aporte principal |
|---|---|
| **Jhonatan Giraldo** | CRUD de carrito, persistencia JSON y JDBC, cálculos y carrito individual por correo |
| **Elke Sommer Daza** | CRUD de usuarios mediante JDBC (`usuariosjdbc`): insertar, consultar, actualizar, eliminar |
| **Base del grupo** | Login, registro, catálogo, gestión de clientes y módulo «Gestión de pedidos» |

## Inicio rápido

**Requisitos:** JDK 17, Maven 3.x, Eclipse con soporte Maven; opcional MariaDB Server/MySQL Server para JDBC.

1. **Eclipse:** `File > Import > Maven > Existing Maven Projects` y seleccionar la carpeta que contiene este `pom.xml`.
2. **Terminal:** ejecutar `mvn clean verify`.
3. **Para el carrito con base de datos:** crear el esquema con `sql/esquema.sql`, copiar `config/database.properties.example` a `config/database.properties`, configurar credenciales y poner `carrito.storage=jdbc`. El archivo real de configuración está excluido de Git.
4. **Ejecutar:** en Eclipse, `util.aplicacion.Main` como Java Application, con directorio de trabajo igual a esta carpeta.

**Sin servidor:** la opción `carrito.storage=json` funciona con `datos/carrito.json` y permite la prueba del carrito local. Las funciones del paquete `usuariosjdbc` sí requieren servidor.

## Dónde está el código

- `src/main/java/util/aplicacion/Main.java`: punto de entrada de Swing.
- `src/main/java/vista/`: ventanas y componentes.
- `src/main/java/controlador/`: eventos y flujo entre ventanas.
- `src/main/java/modelo/`: entidades, carrito, catálogo y repositorios originales.
- `src/main/java/persistencia/`: archivos locales y carrito JDBC.
- `src/main/java/configuracion/ConfiguracionBD.java`: URL JDBC, credenciales y modo de carrito.
- `src/main/java/usuariosjdbc/`: CRUD JDBC aportado por Elke, todavía independiente del registro y login Swing.
- `sql/esquema.sql`: tablas SQL `usuarios` y `carrito_items`.
- `src/test/java/pruebas/PruebaCarrito.java`: prueba funcional manual del carrito JSON.

## Pruebas verificadas y límites

- Reportado y observado en CachyOS: `mvn clean verify` / `mvn clean package` con `BUILD SUCCESS`; `usuariosjdbc.pruebas.PruebasUsuarios` indica `Conexion OK`; `pruebas.PruebaCarrito` indica `PRUEBA_CARRITO_OK`.
- Se probaron operaciones SQL CRUD de usuarios y de filas del carrito mediante comandos de prueba, y se confirmó que Swing guardó y recuperó un artículo en MariaDB.
- Se observó el identificador de carrito `usuario:j@mail.com` separado del antiguo `local`. La comprobación de dos carritos **con contenido simultáneo** aún debe repetirse en la versión final si se solicita evidencia formal.
- **No está integrado:** las pantallas de usuarios usan `modelo.RepositorioUsuarios` y `datos/usuarios.txt`, no `usuariosjdbc.repositorio.RepositorioUsuarios`.
- **Riesgo pendiente:** las contraseñas del módulo local y del CRUD JDBC se almacenan sin hash; no usar datos reales ni desplegar en producción.
- Maven no ejecuta automáticamente todas las clases `main()` de pruebas: consultar `docs/PRUEBAS.md`.

## Documentación

| Documento | Contenido |
|---|---|
| [Arquitectura](docs/ARQUITECTURA.md) | Recorrido del código y persistencias |
| [Instalación](docs/INSTALACION.md) | CachyOS KDE, Windows 10/11 y MariaDB |
| [Pruebas](docs/PRUEBAS.md) | Comandos y resultados esperados |
| [Estado y pendientes](docs/ESTADO_Y_PENDIENTES.md) | Cumplimiento parcial, riesgos y próximos pasos |

**No subir** `config/database.properties`, `datos/usuarios.txt`, `datos/carrito.json`, respaldos ni `target/`/`bin/`. Este repositorio conserva únicamente el ejemplo de configuración y el catálogo inicial.
