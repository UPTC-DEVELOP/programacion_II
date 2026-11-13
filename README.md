# TiendaVirtual — Grupo 14

Proyecto de Programación II desarrollado con Java 17, Swing, Maven y MariaDB.

## Integrantes y aportes

| Integrante | Desarrollo |
|---|---|
| Jhonatan Giraldo | Carrito de compras: operaciones CRUD, cálculos y almacenamiento JSON/JDBC por usuario. |
| Elke Sommer Daza | CRUD JDBC de usuarios e integración con registro, inicio de sesión y gestión de clientes. |

## Ejecución

Desde `TiendaVirtual/`:

```bash
mvn clean verify
```

En Eclipse, importar `TiendaVirtual/` mediante **Maven > Existing Maven Projects** y ejecutar la clase `util.aplicacion.Main`.

La configuración de MariaDB se realiza mediante `TiendaVirtual/config/database.properties.example`. Para que cada usuario tenga un carrito independiente, utilizar `carrito.storage=jdbc`.

## Documentación

- [Instalación](docs/INSTALACION.md)
- [Pruebas y evidencias](docs/PRUEBAS.md)
- [Comandos de MariaDB, JDBC y pruebas](docs/COMANDOS_MARIADB_JDBC_Y_PRUEBAS.md)
