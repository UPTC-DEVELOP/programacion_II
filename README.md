# Tienda Virtual de Libros — Grupo 15

Proyecto académico de **Programación II (2026-II)**. Aplicación de escritorio desarrollada en Java Swing para gestionar libros, usuarios, clientes, carrito de compras y pedidos.

Integrantes:
* Julián Ricardo Duarte Oliveros
* José Arturo Manrique Acosta
* Sharol Sofía Burgos Puerta

## Funciones incluidas

* Inicio de sesión y registro de usuarios.
* Consulta y administración del catálogo de libros.
* Registro y gestión de clientes.
* Carrito de compras con operaciones para agregar, actualizar cantidades, eliminar elementos y consultar el total.
* Módulo de evaluación de pedidos e inventario, accesible desde la ventana principal.
* Persistencia local mediante archivos JSON (Gson).

## Estructura

El proyecto sigue una organización por capas basada en Maven y principios SOLID:

| Carpeta / Paquete | Contenido |
| :--- | :--- |
| `src/main/java/co/edu/uptc/model` | Entidades de libros, usuarios, carrito y productos. |
| `src/main/java/co/edu/uptc/gui` | Ventanas e interfaces Swing. |
| `src/main/java/co/edu/uptc/controller` | Eventos, validaciones y coordinación entre vistas y modelos. |
| `src/main/java/co/edu/uptc/persistence` | Interfaces e implementación de persistencia JSON (Gson). |
| `pom.xml` | Configuración de dependencias Maven y versión de Java 25. |

## Abrir y ejecutar en Eclipse

1. Clonar o descomprimir el proyecto.
2. En Eclipse, selecciona **File → Import → Existing Maven Projects**.
3. Elige la carpeta `programacion_II-feature-grupo15/Tienda_Virtual` (que contiene el archivo `pom.xml`).
4. Configura un JRE compatible con **Java 25**.
5. Abre `src/main/java/co/edu/uptc/gui/VentanaPrincipal.java` (o la clase Main) y selecciona **Run As → Java Application**.

El punto de entrada es la interfaz principal. Los archivos JSON locales se gestionan automáticamente a través del proyecto Maven.

## Rama de trabajo

El material corresponde al proyecto de la rama `feature/grupo15` del repositorio del curso: UPTC-DEVELOP/programacion_II.

## Tecnologías

* Java 25
* Maven 3.9
* Java Swing
* Gson (v2.10.1) para persistencia JSON
* Eclipse IDE / SourceTree
