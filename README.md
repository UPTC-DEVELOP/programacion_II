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

- Java JDK 25
- Maven 3.9
- MariaDB 13
- DBeaver 

Verificar en una ventana de comandos que los siguientes comandos muestren que la versión de Java JDK es la 25.


```sh
java -version 

mvn -v
```

En eclipse, el JDK 25 debe estar registrado en Window → Preferences → Java → Installed JREs.

También se puede trabajar con el Maven de eclipse: Window → Preferences → Maven → Installations: ☑ Maven 3.9 

## Cargar la Base de Datos 

En DBeaver en una conexión activa ejecutar los scripts del directorio `scripts-sql`:

- `crear-base-datos.sql` 
- `poblar.sql`, o individualmente, `insertar-editorial.sql`, `insertar-libro`, `insertar-formato-libro.sql`.

## Cómo Abrir el Proyecto en eclipse

En eclipse: File → Import... → Maven → Existing Maven Projects  , y luego abrir el directorio que tenga el archivo `pom.xml`.

## Cómo Ejecutar el Programa de la Tienda

Abrir una ventana de comandos en el directorio o carpeta que tenga el archivo `pom.xml` y ejecutar este comando:

```sh
mvn compile exec:java
```
