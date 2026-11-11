# TiendaVirtual — Grupo 14

Proyecto de **Programación II (UPTC)** desarrollado en Java 17 con Swing y Maven, a partir del código compartido en el repositorio del curso.

## Integrantes y aportes

| Integrante | Trabajo realizado |
|---|---|
| Jhonatan Giraldo | CRUD del carrito de compras, cálculo de totales y persistencia JSON/JDBC con carritos identificados por usuario. |
| Elke Sommer Daza | CRUD de usuarios con JDBC: registrar, consultar, actualizar y eliminar. |
| Grupo 14 | Integración de la aplicación, catálogo, acceso y gestión de pedidos. |

## Organización

El proyecto se encuentra en [`TiendaVirtual/`](TiendaVirtual/). Sus clases Java están organizadas en modelos, vistas, controladores y persistencia. El punto de inicio es `util.aplicacion.Main`.

La aplicación dispone de un carrito que puede guardar información en JSON o en MariaDB/MySQL. El módulo `usuariosjdbc` realiza operaciones SQL de usuarios; **la interfaz de registro y acceso todavía usa el repositorio local**, por lo que su integración con ese módulo sigue pendiente.

## Ejecutar

Requisitos: **JDK 17**, **Maven 3** y **Eclipse** (opcional para ejecutar por terminal). Para las funciones JDBC se requiere MariaDB o MySQL.

```bash
cd TiendaVirtual
mvn clean verify
mvn -q dependency:build-classpath -Dmdep.outputFile=target/dependencias.txt
java -cp "target/classes:$(cat target/dependencias.txt)" util.aplicacion.Main
```

En Eclipse: **File > Import > Maven > Existing Maven Projects** y seleccionar `TiendaVirtual/`.

Las instrucciones para configurar la base de datos y ejecutar las pruebas están en [Instalación](TiendaVirtual/docs/INSTALACION.md) y [Pruebas](TiendaVirtual/docs/PRUEBAS.md).
