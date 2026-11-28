# Tienda de Libros Grupo 9

Proyecto académico de la tienda de libros en Java del Grupo 9.

Asignatura: Programación II

Integrantes:

- Eder Andrés Ávila Niño

- Neyder Benjamín Jiménez Torres

- Juan Sebastián Merchán Salamanca

Semestre: 2026-II

UPTC

## Requisitos del Sistema

Java JDK 25

Maven 3.9

MariaDB 13

DBeaver

Verificar en una ventana de comandos que los siguientes comandos muestren que la versión de Java JDK es la 25.

    ```sh
    java -version 

    mvn -v
    ```

En eclipse, el JDK 25 debe estar registrado en Window → Preferences → Java → Installed JREs.

También se puede trabajar con el Maven de eclipse: Window → Preferences → Maven → Installations: ☑ Maven 3.9

## Cargar la Base de Datos 

En DBeaver en una conexión activa sin tener una base de datos selecciona abrir y ejecutar el script `construir-bd.sql` del directorio `scripts-sql`. Este script se debe ejecutar en DBeaver con el comando 'Execute SQL Script (Alt+X)'.

## Cómo Abrir el Proyecto en eclipse

En eclipse: File → Import... → Maven → Existing Maven Projects , y luego abrir el directorio que tenga el archivo `pom.xml`.

## Cómo Ejecutar el Programa de la Tienda

Abrir una ventana de comandos en el directorio o carpeta que tenga el archivo `pom.xml` y ejecutar este comando:

    ```sh
    mvn compile exec:java
    ```

## Contraseñas de Usuario

La tienda de libros tiene las siguientes cuentas de usuario predeterminadas:

| **Rol**            | **Correo Electrónico**      | **Contraseña**   |
|--------------------|-----------------------------|------------------|
| Administrador      | `admin@tiendalibros.co`     | `Admin1``23@`    |
| Gestor de Catálogo | `catalogo@tiendalibros.co`  | `Catalogo1``23@` |
| Gestor de Ventas   | `ventas@tiendalibros.co`    | `Ventas123@`     |
| Cliente            | `felixfonseca@yocorreo.com` | `Felix123@`      |
| Cliente            | `dianajimenez@yocorreo.com` | `Diana123@`      |

**Nota**: Al iniciar sesión el programa puede tardar unos segundos mientras se están cargando los recursos del programa, particularmente, las imágenes de las cubiertas de los libros en el catálogo.
