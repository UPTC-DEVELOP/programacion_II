# Estado técnico, responsabilidades y pendientes

## Comprobación de esta entrega documental

El ZIP se elaboró desde el último `TiendaVirtual.zip` proporcionado por Jhonatan. El código Java y SQL se conservó sin cambios funcionales: únicamente se revisó y reorganizó la documentación, se amplió `.gitignore` y se excluyeron artefactos locales y secretos de la distribución.

## Resumen de responsabilidades

| Origen | Contenido |
|---|---|
| Repositorio grupal UPTC | Base Swing, modelos, catálogo, login, registro y gestión de pedidos |
| Jhonatan Giraldo | Carrito (añadir, consultar, cantidad, eliminar, vaciar), persistencia JSON y JDBC y aislamiento por correo |
| Elke Sommer Daza | Modelo y CRUD independiente de usuarios JDBC con `PreparedStatement` |

## Secuencia técnica documentada

1. Se tomó la base común de TiendaVirtual usada por ambos integrantes.
2. Se integró la corrección de Elke como paquete independiente `usuariosjdbc` para evitar choque con clases `modelo.Usuario`.
3. Se integró el CRUD de carrito con validaciones, cálculo de totales y persistencia JSON/JDBC.
4. Se configuraron Maven/Java 17 y controladores de MariaDB y MySQL.
5. Se renombró el módulo «Evaluación Parte 2» a «Gestión de pedidos».
6. Se diagnosticó la conexión local en CachyOS y se crearon las tablas `usuarios` y `carrito_items`.
7. Se reportaron `BUILD SUCCESS`, conexión JDBC y pruebas individuales del carrito y usuarios.
8. Se comprobó que el carrito antiguo `local` aparecía para distintas cuentas y se corrigió la propagación del correo autenticado al carrito.
9. Se verificó en SQL un carrito `usuario:<correo>` separado del registro `local`.
10. Se observó que la pantalla de usuarios continuaba trabajando con `datos/usuarios.txt`, no con la tabla JDBC de Elke.
11. En esta revisión se preparó un ZIP para GitHub sin contraseña local, registros de usuarios, binarios compilados ni respaldos.

## Pendientes antes de calificar el proyecto como integrado

- Vincular registro, login, consultas y CRUD de usuarios Swing al repositorio JDBC de Elke, con migración controlada de usuarios locales.
- Evitar contraseñas en texto plano en ambos almacenamientos; definir hashing seguro y recuperación de cuentas.
- Decidir tratamiento del carrito legado `local` y confirmar dos sesiones con carritos no vacíos.
- Revisar la identificación de libros por ID/ISBN y los duplicados por referencia de objeto.
- Considerar el aislamiento por usuario también en JSON o documentar que JSON es únicamente modo local de pruebas.
- Ejecutar el conjunto completo de pruebas y evidencias en Windows 10/11 de Elke.
- Revisar cambios contra rama remota real antes de hacer `commit` / `push`, sin sobrescribir trabajo ajeno.
- Elaborar el DIT solo cuando el usuario lo solicite expresamente.

## Reglas para compartir

Nunca confirmar ni versionar `config/database.properties` o `datos/usuarios.txt`; no compartir contraseñas, archivos JSON de clientes, datos de pruebas personales o carpetas `target/` y `bin/`.
