# Instalación

## Requisitos

Java 17, Maven 3 y Eclipse. MariaDB o MySQL para utilizar JDBC.

## Abrir y compilar

1. Importar `TiendaVirtual/` en Eclipse como **Existing Maven Project**.
2. Ejecutar desde `TiendaVirtual/`:

```bash
mvn clean verify
```

## Configurar MariaDB

1. Crear las tablas ejecutando `TiendaVirtual/sql/esquema.sql`.
2. Crear un usuario SQL con permisos de lectura y escritura sobre `tienda_virtual`.
3. Copiar `TiendaVirtual/config/database.properties.example` a `TiendaVirtual/config/database.properties`.
4. Completar URL JDBC, usuario y contraseña locales.
5. Establecer `carrito.storage=jdbc` para guardar carritos separados por usuario.

El archivo `database.properties` es privado y está excluido de Git.

## Iniciar

En Eclipse, ejecutar `util.aplicacion.Main` como **Java Application**, utilizando `TiendaVirtual/` como directorio de trabajo.

[![Volver al README](imagenes/volver-readme.svg)](../README.md)
