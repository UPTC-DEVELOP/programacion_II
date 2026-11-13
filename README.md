# Tienda Virtual de Libros — Grupo 15

Proyecto académico de **Programación II (2026-II)**. Aplicación de escritorio desarrollada en **Java 25 (Swing)** para gestionar libros, usuarios, clientes y carrito de compras, con persistencia dual en **MySQL 8.4.0** y archivos locales **JSON (Gson)**.

Integrantes:
- **Julián Ricardo Duarte Oliveros** — CRUD de Carrito de Compras
- **José Arturo Manrique Acosta** — CRUD de Usuarios
- **Sharol Sofía Burgos Puerta** — CRUD de Libros

## Funciones incluidas

- **CRUD de Usuarios:** Inicio de sesión, registro y administración de perfiles.
- **CRUD de Libros:** Consulta, creación, actualización (descuentos/stock) y eliminación en el catálogo.
- **CRUD de Carrito de compras:** Operaciones para agregar, actualizar cantidades, eliminar elementos y consultar el total.
- Registro y gestión de clientes, ubicaciones de envío (`UbicacionesColombia`) y datos de transferencia (`DatosTransferencia`).
- Persistencia dual mediante base de datos relacional **MySQL** y archivos locales **JSON (Gson)**.

## Estructura

El proyecto sigue una organización por capas basada en Maven y principios SOLID:

| Carpeta / Paquete | Contenido |
| :--- | :--- |
| `src/main/java/co/edu/uptc/model` | Entidades `Libro`, `Cliente`, `Carrito`, `ItemCarrito`, `DatosTransferencia` y `UbicacionesColombia`. |
| `src/main/java/co/edu/uptc/gui` | Ventana e interfaz gráfica Swing (`VentanaPrincipal.java`). |
| `src/main/java/co/edu/uptc/controller` | Controlador principal (`TiendaController.java`), eventos y validaciones. |
| `src/main/java/co/edu/uptc/persistence` | Conexión `ConexionMySQL`, interfaces (`IRepositorioLibros`, `IRepositorioUsuario`, `IRepositorioCarrito`) e implementaciones para **MySQL (JDBC)** y **JSON (Gson)**. |
| `sql` | Scripts SQL para la creación y configuración de la base de datos en MySQL. |
| `pom.xml` | Configuración de dependencias Maven (`mysql-connector-j`, `gson`) y versión de **Java 25**. |

## Abrir y ejecutar en Eclipse

1. Clonar o descomprimir el proyecto.
2. Ejecutar los scripts de la carpeta `sql` en **MySQL 8.4.0** (puerto `3308`) para crear la base de datos y las tablas.
3. En Eclipse, selecciona **File → Import → Existing Maven Projects**.
4. Elige la carpeta `programacion_II-feature-grupo15/Tienda_Virtual` (que contiene el archivo `pom.xml`).
5. Configura un JRE compatible con **Java 25** y actualiza las dependencias (**Maven → Update Project**).
6. Abre `src/main/java/co/edu/uptc/gui/VentanaPrincipal.java` y selecciona **Run As → Java Application**.

El punto de entrada es la interfaz principal (`VentanaPrincipal.java`), desde donde se gestionan tanto la base de datos MySQL como los archivos JSON locales.

## Rama de trabajo

El material corresponde al proyecto de la rama `feature/grupo15` del repositorio del curso: `UPTC-DEVELOP/programacion_II`.

## Tecnologías

- Java 25
- Java Swing
- Maven 3.9
- MySQL Server 8.4.0 (`mysql-connector-j`)
- Google Gson (JSON)
- Eclipse IDE / SourceTree
