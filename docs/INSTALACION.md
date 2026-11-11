# Instalación y ejecución multiplataforma

## Requisitos

- Java/JDK 17, Maven y Eclipse con Maven (m2e).
- Para JDBC: MariaDB Server o MySQL Server; **no es necesario instalar ambos**. Los dos drivers están declarados en `pom.xml`.
- Proyecto abierto desde la carpeta donde están `pom.xml`, `config/`, `datos/` y `sql/`.

## CachyOS KDE / Arch Linux

```bash
sudo pacman -Syu
sudo pacman -S --needed jdk17-openjdk maven mariadb
# Ejecutar solo si MariaDB se instala por primera vez y no hay tablas del sistema:
if ! sudo test -d /var/lib/mysql/mysql; then
  sudo mariadb-install-db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
fi
sudo systemctl enable --now mariadb
```

Desde la raíz del proyecto:

```bash
# Como administrador, crear el esquema (no usar sobre datos ajenos sin revisar)
sudo mariadb < sql/esquema.sql
# Entra a la consola SQL como administrador:
sudo mariadb
```

Una vez dentro del intérprete SQL, asignar una **contraseña personal distinta** en lugar del ejemplo:

```sql
CREATE USER IF NOT EXISTS 'tienda_app'@'127.0.0.1' IDENTIFIED BY 'REEMPLAZAR_CLAVE';
GRANT SELECT, INSERT, UPDATE, DELETE ON tienda_virtual.* TO 'tienda_app'@'127.0.0.1';
```

En la raíz del proyecto:

```bash
cp config/database.properties.example config/database.properties
chmod 600 config/database.properties
# Editar el archivo para poner contraseña local y carrito.storage=jdbc
${EDITOR:-nano} config/database.properties
mvn clean verify
mvn -q dependency:build-classpath -Dmdep.outputFile=target/dependencias.txt
java -cp "target/classes:$(cat target/dependencias.txt)" util.aplicacion.Main
```

El contenido esperado del archivo privado es:

```properties
database.url=jdbc:mariadb://127.0.0.1:3306/tienda_virtual
database.user=tienda_app
database.password=CLAVE_PERSONAL_NO_PUBLICAR
carrito.storage=jdbc
```

Para comprobar las tablas sin revelar contraseñas en capturas, conectarse con el cliente de MariaDB y ejecutar:

```sql
SHOW TABLES FROM tienda_virtual;
SELECT carrito_id, titulo, cantidad, precio FROM tienda_virtual.carrito_items;
SELECT nombre, correo, tipo_cliente FROM tienda_virtual.usuarios;
```

**Importante:** MariaDB utiliza la tabla `usuarios` con clave `correo`; no consultar una columna `id` inexistente.

## Windows 10/11 (PowerShell)

1. Instalar JDK 17 y Maven (por ejemplo con Windows Package Manager si está disponible).
2. Instalar **MariaDB Server** mediante su instalador oficial. No requiere MySQL Server en paralelo.
3. Desde un cliente MariaDB con privilegios administrativos, ejecutar las instrucciones de `sql/esquema.sql`. Después crear el usuario `tienda_app` y otorgar permisos como arriba.
4. Copiar `config\database.properties.example` a `config\database.properties` y completar la contraseña.
5. En PowerShell, dentro del directorio que contiene `pom.xml`:

```powershell
java -version
mvn -version
mvn clean verify
mvn dependency:build-classpath "-Dmdep.outputFile=target/dependencias.txt"
$CP = "target\classes;target\test-classes;$(Get-Content .\target\dependencias.txt -Raw)"
java -cp "$CP" util.aplicacion.Main
```

En Eclipse: `File > Import > Maven > Existing Maven Projects`, seleccionar la carpeta de este `pom.xml`, hacer `Maven > Update Project` y ejecutar `util.aplicacion.Main`. Configurar **Working Directory** como raíz del proyecto.

## Errores frecuentes

- `No existe config/database.properties`: iniciar Java desde la raíz correcta; verificar archivo y ruta.
- `Access denied`: verificar usuario, host `127.0.0.1`, contraseña y privilegios.
- `Unknown column 'id'`: `usuarios` tiene PK `correo`, no `id`.
- `No suitable driver`: resolver dependencias Maven y revisar URL `jdbc:mariadb:` / `jdbc:mysql:`.
- El carrito muestra datos de otro usuario: verificar que `ControladorInicio` cree `RepositorioCarrito(usuario.getCorreo())` y que la versión ejecutada esté actualizada.
- Los usuarios Swing no aparecen en SQL: **estado conocido de integración**, no falla del servidor.

## Protección de archivos

No copiar contraseñas entre equipos. `config/database.properties` y `datos/usuarios.txt` no forman parte del ZIP publicable. Cada integrante crea sus credenciales y datos locales.
