# Tienda Virtual de Libros — Grupo 14

Proyecto académico de **Programación II (2026-II)**. Aplicación de escritorio desarrollada en Java Swing para gestionar libros, usuarios, clientes, carrito de compras y pedidos.

## Funciones incluidas

- Inicio de sesión y registro de usuarios.
- Consulta y administración del catálogo de libros.
- Registro y gestión de clientes.
- Carrito de compras con operaciones para agregar, actualizar cantidades, eliminar elementos y consultar el total.
- Módulo de evaluación de pedidos e inventario, accesible desde la ventana principal.
- Persistencia local de usuarios y libros mediante archivos de texto.

## Estructura

El proyecto sigue una organización MVC (Modelo, Vista y Controlador):

| Carpeta | Contenido |
|---|---|
| `src/modelo` | Entidades y repositorios de libros, usuarios, carrito, productos y pedidos. |
| `src/vista` | Ventanas e interfaces Swing. |
| `src/controlador` | Eventos, validaciones y coordinación entre vistas y modelos. |
| `src/persistencia` | Lectura y escritura de los datos de libros y usuarios. |
| `src/excepciones` | Excepciones de validación. |
| `src/util` | Estilos y clase de inicio de la aplicación. |
| `datos` | Archivos locales de datos iniciales. |

## Abrir y ejecutar en Eclipse

1. Descomprime el ZIP.
2. En Eclipse, selecciona **File → Import → Existing Projects into Workspace**.
3. Elige la carpeta `programacion_II-feature-grupo14/TiendaVirtual` (contiene `.project` y `.classpath`).
4. Configura un JRE compatible con **Java 21**, indicado en `.classpath`.
5. Abre `src/util/aplicacion/Main.java` y selecciona **Run As → Java Application**.

El punto de entrada es `util.aplicacion.Main`. Los archivos locales están en `TiendaVirtual/datos`; si la aplicación no los encuentra, revisa que Eclipse ejecute con `TiendaVirtual` como directorio de trabajo.

## Rama de trabajo

El material corresponde al proyecto de la rama `feature/grupo14` del repositorio del curso: [UPTC-DEVELOP/programacion_II](https://github.com/UPTC-DEVELOP/programacion_II/tree/feature/grupo14).

## Tecnologías

- Java 21
- Java Swing
- Eclipse IDE
- Archivos de texto para persistencia local
