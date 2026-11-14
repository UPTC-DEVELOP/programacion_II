# Comandos de MariaDB, JDBC y pruebas — TiendaVirtual (Grupo 14)

Guía para **Linux (CachyOS/Arch)** y **Windows 10/11 (PowerShell)**. Los comandos se ejecutan desde la carpeta `TiendaVirtual` que contiene `pom.xml`, salvo que se indique otra ubicación.

> **Precaución:** los apartados de eliminación borran datos. Haz un respaldo y utilízalos solo sobre tu base local de pruebas. No publiques `config/database.properties` ni capturas que muestren contraseñas.

## 1. Requisitos e instalación

**Linux (CachyOS/Arch):**

```bash
sudo pacman -S --needed jdk17-openjdk maven mariadb
java -version
mvn -version
# Solo en una instalación nueva de MariaDB, si aún no se inicializó:
# sudo mariadb-install-db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
sudo systemctl enable --now mariadb
sudo systemctl is-active mariadb
```

**Windows (PowerShell):** instala **JDK 17**, **Maven 3** y **MariaDB Server** desde sus instaladores oficiales. Después verifica:

```powershell
java -version
mvn -version
mariadb --version
Get-Service *MariaDB*   # Consultar nombre real del servicio
```

Si `mariadb` no se reconoce en PowerShell, agrega la carpeta `bin` de MariaDB al `PATH` o ejecuta `mariadb.exe` usando su ruta completa. Inicia el servicio desde **Servicios** de Windows o con `Start-Service -Name <nombre-del-servicio>` en PowerShell como administrador.

## 2. Crear las tablas y el usuario JDBC

El repositorio incluye `sql/esquema.sql`: crea la base `tienda_virtual` y las tablas `usuarios` y `carrito_items` si todavía no existen.

**Linux:**

```bash
cd "/ruta/al/repositorio/TiendaVirtual"
sudo mariadb < sql/esquema.sql
sudo mariadb
```

**Windows (PowerShell):** entra a MariaDB como administrador, desde la carpeta del proyecto:

```powershell
cd "C:\ruta\al\repositorio\TiendaVirtual"
mariadb -u root -p
```

Dentro del cliente de MariaDB, en **ambos sistemas**, ejecuta:

```sql
SOURCE sql/esquema.sql;
CREATE USER IF NOT EXISTS 'tienda_app'@'127.0.0.1' IDENTIFIED BY 'CAMBIA_ESTA_CLAVE';
CREATE USER IF NOT EXISTS 'tienda_app'@'localhost' IDENTIFIED BY 'CAMBIA_ESTA_CLAVE';
GRANT SELECT, INSERT, UPDATE, DELETE ON tienda_virtual.* TO 'tienda_app'@'127.0.0.1';
GRANT SELECT, INSERT, UPDATE, DELETE ON tienda_virtual.* TO 'tienda_app'@'localhost';
SHOW GRANTS FOR 'tienda_app'@'127.0.0.1';
EXIT;
```

**No ejecutes dos veces la importación por obligación:** el esquema usa `IF NOT EXISTS`. Si ya ejecutaste `sudo mariadb < sql/esquema.sql` en Linux, omite `SOURCE sql/esquema.sql;` en esa misma configuración. Para `SOURCE` en Windows, si la ruta relativa no funciona, usa una ruta absoluta con `/`, por ejemplo `SOURCE C:/proyectos/caso-estudio/sql/esquema.sql;`.

Si `tienda_app` ya existía con otra contraseña, `CREATE USER IF NOT EXISTS` **no la cambia**. En ese caso, establece la nueva contraseña desde la sesión administradora:

```sql
ALTER USER 'tienda_app'@'127.0.0.1' IDENTIFIED BY 'CAMBIA_ESTA_CLAVE';
ALTER USER 'tienda_app'@'localhost' IDENTIFIED BY 'CAMBIA_ESTA_CLAVE';
```

Utiliza una clave privada propia, no el texto del ejemplo. La contraseña de `tienda_app` deberá coincidir con la configuración local del proyecto.

## 3. Configurar JDBC sin publicar contraseñas

**Linux:**

```bash
cd "/ruta/al/repositorio/TiendaVirtual"
cp -n config/database.properties.example config/database.properties
chmod 600 config/database.properties
nano config/database.properties
```

**Windows (PowerShell):**

```powershell
cd "C:\ruta\al\repositorio\TiendaVirtual"
if (-not (Test-Path config/database.properties)) {
    Copy-Item config/database.properties.example config/database.properties
}
notepad config/database.properties
```

Contenido del archivo **local** `config/database.properties`:

```properties
database.url=jdbc:mariadb://127.0.0.1:3306/tienda_virtual
database.user=tienda_app
database.password=TU_CLAVE_PRIVADA
carrito.storage=jdbc
```

Para utilizar el carrito en archivo en lugar de MariaDB, cambia únicamente `carrito.storage=json`. En ese modo, `datos/carrito.json` puede ser compartido entre sesiones; la prueba de carritos independientes debe hacerse con `jdbc`.

Los controladores JDBC están incluidos en las dependencias del `pom.xml`. **No hace falta descargar manualmente un `.jar` del driver** cuando se utiliza Maven.

## 4. Comprobar MariaDB y la conexión JDBC

**Linux:**

```bash
sudo systemctl is-active mariadb
mariadb -h 127.0.0.1 -u tienda_app -p tienda_virtual -e "SHOW TABLES;"
```

**Windows (PowerShell):**

```powershell
Get-Service *MariaDB*
mariadb -h 127.0.0.1 -u tienda_app -p tienda_virtual -e "SHOW TABLES;"
```

En ambos casos debes ver `usuarios` y `carrito_items`. El parámetro `-p` solicita la contraseña sin mostrarla en el comando.

### Consultas SQL de comprobación (ambos sistemas)

Abre el cliente con:

```text
mariadb -h 127.0.0.1 -u tienda_app -p tienda_virtual
```

Ejecuta:

```sql
SHOW TABLES;
DESCRIBE usuarios;
DESCRIBE carrito_items;
SELECT nombre, correo, tipo_cliente FROM usuarios ORDER BY correo;
SELECT carrito_id, titulo, cantidad, precio FROM carrito_items ORDER BY carrito_id, titulo;
SELECT carrito_id, SUM(cantidad * precio) AS total FROM carrito_items GROUP BY carrito_id;
-- Verificar una eliminación; reemplaza el correo por el del usuario de prueba:
SELECT COUNT(*) AS usuarios_encontrados FROM usuarios WHERE correo = 'prueba@ejemplo.com';
EXIT;
```

`usuarios_encontrados = 0` indica que ese correo no está registrado. No consultes ni captures la columna `contrasena`.

## 5. Compilación y pruebas Java/Maven

**Linux:**

```bash
cd "/ruta/al/repositorio/TiendaVirtual"
mvn clean verify
mvn dependency:build-classpath -Dmdep.outputFile=target/dependencias.txt
CARRITO_STORAGE=json java -cp "target/test-classes:target/classes:$(cat target/dependencias.txt)" pruebas.PruebaCarrito
java -cp "target/classes:$(cat target/dependencias.txt)" usuariosjdbc.pruebas.PruebasUsuarios
java -cp "target/classes:$(cat target/dependencias.txt)" util.aplicacion.Main
```

**Windows (PowerShell):**

```powershell
cd "C:\ruta\al\repositorio\TiendaVirtual"
mvn clean verify
mvn dependency:build-classpath "-Dmdep.outputFile=target/dependencias.txt"
$cp = "target\test-classes;target\classes;$(Get-Content target/dependencias.txt -Raw)"
$env:CARRITO_STORAGE = 'json'
java -cp "$cp" pruebas.PruebaCarrito
Remove-Item Env:CARRITO_STORAGE
java -cp "$cp" usuariosjdbc.pruebas.PruebasUsuarios
java -cp "$cp" util.aplicacion.Main
```

**Resultados esperados:** `BUILD SUCCESS` en Maven; `PRUEBA_CARRITO_OK` en el carrito JSON; y `Conexion OK; ...` en el diagnóstico de usuarios JDBC, siempre que la conexión a MariaDB esté configurada. La clase `PruebasUsuarios` comprueba una consulta; **no ejecuta automáticamente el CRUD completo**.

**En Eclipse (ambos sistemas):** importa el proyecto con **File > Import > Maven > Existing Maven Projects**, selecciona la carpeta que contiene `pom.xml` y ejecuta `util.aplicacion.Main` como **Java Application**. Para **Run As > Maven Build...**, escribe `clean verify` en **Goals**. Un Maven Build sin *goals* produce `No goals have been specified`.

### Pruebas funcionales desde Swing

1. Registrar un usuario de prueba e iniciar sesión.
2. Buscar, listar y actualizar ese usuario; comprobar el cambio con una consulta SQL a `usuarios`.
3. Eliminar un usuario **de prueba** y comprobar `COUNT(*) = 0`.
4. Con `carrito.storage=jdbc`, agregar y actualizar libros en el carrito; comprobar total y filas en `carrito_items`.
5. Cerrar y abrir la aplicación; verificar la persistencia.
6. Entrar con una segunda cuenta y comprobar que sus artículos no se mezclan con los de la primera.

Estas acciones requieren verificación visual; `mvn clean verify` por sí solo no demuestra todas las operaciones de la interfaz.

## 6. Respaldar y restaurar la base de datos

Los siguientes comandos crean un archivo SQL de respaldo. Utiliza `mariadb-dump` desde una terminal donde esté disponible el cliente.

**Linux:**

```bash
mariadb-dump -h 127.0.0.1 -u tienda_app -p --single-transaction tienda_virtual > tienda_virtual_respaldo.sql
```

**Windows (PowerShell):** usa `cmd.exe` para evitar problemas de codificación/redirección de versiones antiguas de PowerShell:

```powershell
cmd /c 'mariadb-dump -h 127.0.0.1 -u tienda_app -p --single-transaction tienda_virtual > tienda_virtual_respaldo.sql'
```

**Restaurar (Linux):**

```bash
mariadb -h 127.0.0.1 -u tienda_app -p tienda_virtual < tienda_virtual_respaldo.sql
```

**Restaurar (Windows PowerShell):**

```powershell
cmd /c 'mariadb -h 127.0.0.1 -u tienda_app -p tienda_virtual < tienda_virtual_respaldo.sql'
```

**Nota:** el usuario `tienda_app` puede carecer de privilegios para restaurar objetos o ejecutar ciertas instrucciones del volcado. En ese caso utiliza una cuenta administradora. El respaldo puede contener información personal y hashes de contraseñas; guárdalo fuera del repositorio y no lo publiques.

## 7. Eliminar datos de prueba o reiniciar MariaDB

### 7.1 Eliminar únicamente un usuario de prueba (ambos sistemas)

En la consola MariaDB, reemplaza el correo por el usuario temporal que desees eliminar:

```sql
USE tienda_virtual;
DELETE FROM usuarios WHERE correo = 'prueba@ejemplo.com';
SELECT ROW_COUNT() AS filas_eliminadas;
```

### 7.2 Eliminar el carrito de una sola cuenta (ambos sistemas)

```sql
USE tienda_virtual;
DELETE FROM carrito_items WHERE carrito_id = 'usuario:prueba@ejemplo.com';
SELECT ROW_COUNT() AS filas_eliminadas;
```

**Atención:** la cuenta debe identificarse por el **correo**, no por el nombre que aparece en pantalla. Eliminar un usuario de `usuarios` no implica necesariamente eliminar sus artículos de `carrito_items`, ya que el esquema no define una clave foránea entre ambas tablas.

### 7.3 Vaciar todas las filas de prueba (destructivo)

**Esto borra todos los usuarios y carritos de `tienda_virtual`.** Confirma el respaldo antes de hacerlo. En una sesión administradora de MariaDB:

```sql
USE tienda_virtual;
DELETE FROM carrito_items;
DELETE FROM usuarios;
```

### 7.4 Borrar toda la base de datos (muy destructivo)

**Solo si necesitas restablecer un entorno local desechable; no usar en una base compartida.** Debe ejecutarlo una cuenta administradora:

```sql
DROP DATABASE IF EXISTS tienda_virtual;
```

Para recrearla, vuelve a ejecutar `sql/esquema.sql` como se explica en la sección 2. Los usuarios y permisos del servidor MariaDB no se eliminan al borrar solamente la base.

### 7.5 Retirar el usuario de acceso JDBC (administrador)

```sql
DROP USER IF EXISTS 'tienda_app'@'127.0.0.1';
DROP USER IF EXISTS 'tienda_app'@'localhost';
```

**No ejecutes este paso si el proyecto aún necesita conectarse:** después de eliminar esas cuentas, Java perderá acceso a MariaDB hasta que las crees nuevamente.

## 8. Errores habituales

| Mensaje | Causa probable | Revisión |
|---|---|---|
| `No goals have been specified` | Maven Build sin objetivo | Escribir `clean verify` en **Goals** |
| `Configure DB_PASSWORD ...` | Configuración local sin contraseña | Crear/completar `config/database.properties` |
| `Access denied (1045)` | Contraseña o permisos incorrectos | Comprobar `tienda_app` y el host `127.0.0.1` |
| `Connection refused` | MariaDB detenido o puerto incorrecto | Comprobar servicio y dirección JDBC |
| `Unknown database` | Esquema aún no creado | Ejecutar `sql/esquema.sql` |
| `No suitable driver` | Falta el driver en el *classpath* | Ejecutar Maven y reconstruir `dependencias.txt` |
| Dos usuarios ven el mismo carrito | `carrito.storage=json` u otra selección compartida | Configurar `jdbc`, reiniciar y verificar `carrito_id` |
| El contenido del carrito no coincide con SQL | Se consultó otro usuario, modo JSON o datos antiguos | Confirmar modo JDBC y `usuario:<correo>` |

## 9. Ubicación de los archivos y documentación

- `caso-estudio/pom.xml`: dependencias y compilación Java 17.
- `caso-estudio/sql/esquema.sql`: definición de base y tablas.
- `caso-estudio/config/database.properties.example`: plantilla pública de configuración.
- `caso-estudio/config/database.properties`: configuración **privada** de cada equipo.
- `docs/PRUEBAS.md`: evidencias y capturas de la ejecución.

[Volver al README principal](../README.md)

[🏠 Inicio Grupo 14](https://github.com/UPTC-DEVELOP/programacion_II/tree/feature/grupo14)
