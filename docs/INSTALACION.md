# Instalación — TiendaVirtual

## Requisitos

- JDK 17 y Maven 3.
- Eclipse IDE con soporte Maven, si se utiliza interfaz de desarrollo.
- MariaDB o MySQL para la persistencia JDBC. El carrito en JSON no necesita servidor.

## Preparación del proyecto

Desde `TiendaVirtual/`, comprobar la compilación:

```bash
java -version
mvn -version
mvn clean verify
```

En Eclipse, importar la carpeta como **Existing Maven Project** y ejecutar `util.aplicacion.Main`. El directorio de trabajo debe ser la carpeta que contiene `pom.xml`.

## Base de datos

Instalar y activar MariaDB (CachyOS/Arch Linux):

```bash
sudo pacman -S --needed mariadb jdk17-openjdk maven
# Solo en una instalación nueva, si /var/lib/mysql no está inicializado:
sudo mariadb-install-db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
sudo systemctl enable --now mariadb
```

Importar el esquema desde la carpeta `TiendaVirtual/`:

```bash
sudo mariadb < sql/esquema.sql
```

Crear un usuario de aplicación en la consola SQL del administrador de MariaDB (sustituir la clave):

```sql
CREATE USER IF NOT EXISTS 'tienda_app'@'127.0.0.1' IDENTIFIED BY 'CLAVE_LOCAL';
GRANT SELECT, INSERT, UPDATE, DELETE ON tienda_virtual.* TO 'tienda_app'@'127.0.0.1';
```

Copiar `config/database.properties.example` como `config/database.properties` y ajustar:

```properties
database.url=jdbc:mariadb://127.0.0.1:3306/tienda_virtual
database.user=tienda_app
database.password=CLAVE_LOCAL
carrito.storage=jdbc
```

Para guardar el carrito en archivo, usar `carrito.storage=json`. El archivo `database.properties` es local y no debe subirse a GitHub.

## Windows 10/11

Instalar JDK 17, Maven y MariaDB Server; crear la base con `sql/esquema.sql`, configurar el mismo archivo `.properties` con las credenciales **del equipo de Windows** e importar `TiendaVirtual/` en Eclipse. Los comandos Maven son iguales; los comandos de ejecución Java para PowerShell están en [Pruebas](PRUEBAS.md).
