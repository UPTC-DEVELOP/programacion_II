# Plan de pruebas y evidencias para el profesor

## Alcance

Se distinguen **pruebas reportadas como realizadas** en CachyOS de **pruebas pendientes** en Windows e integración. No afirmar que `BUILD SUCCESS` garantiza el funcionamiento total de la aplicación. Las clases de prueba provistas tienen método `main()`; Maven puede mostrar `No tests to run` porque no están anotadas como pruebas JUnit.

## 1. Compilación (CachyOS)

```bash
cd /ruta/a/TiendaVirtual
java -version
mvn -version
mvn clean verify
```

Esperado: `BUILD SUCCESS`. Evidencia: terminal y versión de Java.

## 2. Diagnóstico JDBC de Elke

```bash
mvn -q dependency:build-classpath -Dmdep.outputFile=target/dependencias.txt
CP="target/classes:target/test-classes:$(cat target/dependencias.txt)"
java -cp "$CP" usuariosjdbc.pruebas.PruebasUsuarios
```

Esperado: `Conexion OK; usuario de prueba no encontrado` (o encontrado). Es una **lectura**, no prueba automatizada de cuatro operaciones.

## 3. Prueba funcional JSON del carrito

```bash
CARRITO_STORAGE=json java -cp "$CP" pruebas.PruebaCarrito
```

Esperado: `PRUEBA_CARRITO_OK`. El programa restaura el archivo anterior del carrito cuando termina.

## 4. Prueba gráfica Swing

```bash
java -cp "$CP" util.aplicacion.Main
```

Comprobar, capturar y describir: registro/login (local), catálogo, Gestión de pedidos, carrito (agregar, actualizar, eliminar, vaciar), cerrar/abrir, recuperar el mismo carrito JDBC, cambiar de usuario y verificar separación.

## 5. Verificación SQL del carrito guardado desde Swing

Conectar con un cliente MariaDB usando las credenciales locales (no publicarlas) y ejecutar:

```sql
USE tienda_virtual;
SELECT carrito_id, titulo, autor, formato, cantidad, precio
FROM carrito_items ORDER BY carrito_id, titulo;
SELECT COUNT(*) AS total_articulos FROM carrito_items;
```

Un ejemplo observado en CachyOS fue `carrito_id = usuario:j@mail.com` con un libro en dos unidades; también permaneció el registro antiguo `local`. No enviar registros reales como parte del repositorio.

## 6. CRUD SQL de usuarios

El repositorio `usuariosjdbc.repositorio.RepositorioUsuarios` expone cuatro métodos JDBC. Se informaron pruebas independientes de inserción, consulta, actualización y eliminación, sobre usuarios temporales; son diferentes de las operaciones hechas desde la ventana Swing. Para reproducirlas, usar un usuario de prueba de correo único, ejecutar esos cuatro métodos desde una clase temporal fuera del repositorio, validar resultados y borrar el registro temporal. **No ejecutar pruebas destructivas sobre usuarios reales**.

## 7. Windows 10/11

En PowerShell, desde el proyecto:

```powershell
mvn clean verify
mvn dependency:build-classpath "-Dmdep.outputFile=target/dependencias.txt"
$CP = "target\classes;target\test-classes;$(Get-Content .\target\dependencias.txt -Raw)"
java -cp "$CP" usuariosjdbc.pruebas.PruebasUsuarios
$env:CARRITO_STORAGE="json"
java -cp "$CP" pruebas.PruebaCarrito
Remove-Item Env:CARRITO_STORAGE
java -cp "$CP" util.aplicacion.Main
```

Para MariaDB en Windows, cada integrante debe crear su `config/database.properties` local y ejecutar `sql/esquema.sql`.

## 8. Tabla de evidencia

| Caso | Resultado conocido | Pendiente |
|---|---|---|
| Compilar con Maven en CachyOS | `BUILD SUCCESS` reportado | Volver a ejecutar en última copia |
| Conexión MariaDB en CachyOS | `CONEXION CORRECTA` | Repetir en Windows |
| Diagnóstico JDBC Elke | `Conexion OK` | Prueba desde UI |
| CRUD Usuarios independiente | Crear/leer/actualizar/eliminar reportados OK | Integrar UI y guardar evidencia por pantalla |
| Carrito JSON | `PRUEBA_CARRITO_OK` | Reproducir en Windows |
| Carrito JDBC Swing | Guardado/recuperación confirmados | Actualización y eliminación con evidencia |
| Carritos por correo | Identificador individual observado | Probar dos cuentas con contenido |
| Seguridad de contraseñas | Sin hash | Corregir antes de producción |

## Presentación oral sencilla

1. «Este proyecto es Java 17 con Maven; `mvn clean verify` comprueba que se construye».
2. «JDBC conecta Java con MariaDB; las tablas separan usuarios y carritos».
3. «Elke desarrolló el CRUD JDBC de usuarios; hoy es un módulo independiente de la pantalla de registro».
4. «Jhonatan desarrolló el carrito con operaciones CRUD, guardado JSON/JDBC y un identificador distinto por usuario».
5. «Probamos la interfaz y verificamos en SQL que el artículo permanece después de reiniciar».
6. «Los pendientes se informan con precisión y se integrarán antes de considerar cerrada la entrega».
