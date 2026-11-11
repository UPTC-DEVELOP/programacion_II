# Pruebas — TiendaVirtual

Ejecutar los comandos desde la carpeta `TiendaVirtual/`.

## Compilación

```bash
mvn clean verify
```

Resultado esperado: `BUILD SUCCESS`. Esto comprueba la compilación y las pruebas configuradas en Maven, no todas las acciones de la interfaz.

## Prueba del carrito JSON — CachyOS/Linux

```bash
mvn -q dependency:build-classpath -Dmdep.outputFile=target/dependencias.txt
CP="target/classes:target/test-classes:$(cat target/dependencias.txt)"
CARRITO_STORAGE=json java -cp "$CP" pruebas.PruebaCarrito
```

Resultado esperado: `PRUEBA_CARRITO_OK`.

## Conexión JDBC de usuarios

Con MariaDB activo y `config/database.properties` configurado:

```bash
java -cp "$CP" usuariosjdbc.pruebas.PruebasUsuarios
```

Resultado esperado: un mensaje que comienza por `Conexion OK`. Esta clase comprueba la consulta JDBC, no el CRUD completo desde Swing.

## Prueba visual y persistencia

```bash
java -cp "$CP" util.aplicacion.Main
```

Comprobar inicio de sesión, catálogo, Gestión de pedidos y operaciones del carrito. Agregar un libro, cerrar y abrir el programa, y verificar que el carrito lo conserva. Cambiar de usuario para comprobar que no comparte sus artículos.

Con el carrito en modo JDBC, consultar desde MariaDB:

```sql
USE tienda_virtual;
SELECT carrito_id, titulo, cantidad, precio FROM carrito_items ORDER BY carrito_id;
```

Para el CRUD de usuarios de Elke deben verificarse insertar, consultar, actualizar y eliminar con **registros de prueba**, sin utilizar cuentas reales. El CRUD JDBC de usuarios todavía no está unido a las ventanas de registro y login.

## PowerShell — Windows 10/11

```powershell
mvn clean verify
mvn dependency:build-classpath "-Dmdep.outputFile=target/dependencias.txt"
$CP = "target\classes;target\test-classes;$(Get-Content .\target\dependencias.txt -Raw)"
$env:CARRITO_STORAGE="json"
java -cp "$CP" pruebas.PruebaCarrito
Remove-Item Env:CARRITO_STORAGE
java -cp "$CP" usuariosjdbc.pruebas.PruebasUsuarios
java -cp "$CP" util.aplicacion.Main
```

La prueba JDBC requiere que cada equipo tenga configurada su base de datos local.
