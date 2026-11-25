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

El proyecto (`caso-estudio`) sigue una organización modular por capas basada en Maven y principios **SOLID**:

| Carpeta / Paquete | Contenido |
| :--- | :--- |
| `src/main/java/co/edu/uptc/caso/estudio` | Punto de entrada principal de la aplicación e inicialización del sistema. |
| `src/main/java/co/edu/uptc/caso/estudio/gui` | Interfaz gráfica modular (`VentanaPrincipal`, `PanelUsuario`, `PanelLibros`, `PanelCarrito`, `DialogoLibro`) y manejador central de eventos (`Evento`). |
| `src/main/java/co/edu/uptc/caso/estudio/negocio` | Interfaces y servicios de negocio desacoplados (`IGestionUsuarios`, `IGestionLibros`, `IGestionCompras`, `TiendaConfig`). |
| `src/main/java/co/edu/uptc/caso/estudio/negocio/dto` | Objetos de transferencia de datos entre vista y negocio (`CredencialDto`, `PagoDto`). |
| `src/main/java/co/edu/uptc/caso/estudio/model` | Entidades del dominio con herencia/polimorfismo (`Cliente`, `ClienteRegular`, `ClientePremium`, `FabricaClientes`, `Libro`, `LibroFisico`, `LibroDigital`, `FabricaLibros`, `Carrito`, `Compra`). |
| `src/main/java/co/edu/uptc/caso/estudio/model/enums` | Enumeraciones para constantes del dominio (`TipoCliente`, `MetodoPago`). |
| `src/main/java/co/edu/uptc/caso/estudio/persistence` | Interfaces de repositorio (`IRepositorioUsuario`, `IRepositorioLibros`, `IRepositorioCarrito`, `IRepositorioCompra`) e implementaciones duales en **MySQL** y **JSON** (`ManejadorArchivoJson`, `RegistradorOperacionesTxt`). |
| `src/test/java/co/edu/uptc/caso/estudio/negocio` | Suite de pruebas unitarias en **JUnit 3.8.1** (`GestionUsuariosTest`, `GestionLibrosTest`, `GestionComprasTest`) con repositorios simulados en memoria. |
| `config/` | Archivo de propiedades y parámetros de conexión de la tienda (`tienda.properties`). |
| `data/` | Archivos locales de persistencia JSON (`usuarios.json`, `libros.json`, `carritos.json`, `compras.json`) y registro de auditoría (`operaciones_tienda.txt`). |
| `sql/` | Scripts SQL para la creación y configuración de la base de datos en MySQL. |
| `pom.xml` | Configuración de dependencias Maven (`mysql-connector-j`, `gson`, `junit`) y versión de **Java 25**. |

## Abrir y ejecutar en Eclipse

1. Clonar o descomprimir el proyecto.
2. Ejecutar los scripts de la carpeta `sql` en **MySQL 8.4.0** (puerto `3308`) para crear la base de datos y las tablas.
3. En Eclipse, selecciona **File → Import → Existing Maven Projects**.
4. Elige la carpeta `programacion_II-feature-grupo15/Tienda_Virtual` (que contiene el archivo `pom.xml`).
5. Configura un JRE compatible con **Java 25** y actualiza las dependencias (**Maven → Update Project**).
6. Ejecuta la clase principal en `src/main/java/co/edu/uptc/caso/estudio` (o `VentanaPrincipal.java` en `.gui`) con **Run As → Java Application**.

## Pruebas Unitarias (JUnit 3.8.1)

El proyecto cuenta con pruebas automatizadas del programador desacopladas de MySQL y archivos JSON mediante repositorios en memoria:

1. En Eclipse, despliega `src/test/java`.
2. Haz clic derecho sobre el paquete `co.edu.uptc.caso.estudio.negocio`.
3. Selecciona **Run As → JUnit Test** para verificar la ejecución de los casos de prueba.

## Rama de trabajo

El material corresponde al proyecto de la rama `feature/grupo15` del repositorio del curso: `UPTC-DEVELOP/programacion_II`.

## Tecnologías

- Java 25
- Java Swing
- Maven 3.9
- MySQL Server 8.4.0 (`mysql-connector-j`)
- Google Gson (JSON)
- JUnit 3.8.1
- Eclipse IDE / SourceTree