# Fase 7 — Integración, pruebas y revisión final

## Compilación

La compilación limpia se ejecuta desde la raíz:

```powershell
Remove-Item bin -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory bin
$files = Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName
javac -encoding UTF-8 -d bin $files
```

Resultado: compilación correcta.

## Pruebas automatizadas realizadas

| Prueba | Resultado | Cobertura |
|---|---|---|
| `Phase1Smoke` | ✅ | Precio final con descuento e IVA, descuento Premium y totales oficiales |
| `Phase2Smoke` | ✅ | Validadores, hash SHA-256, rutas, moneda y fechas |
| `Phase3Smoke` | ✅ | Semillas, libros, usuarios, carrito y TXT |
| `Phase4Smoke` | ✅ | Compra Premium, stock, recibo, historial y cliente frecuente |
| `Phase5Smoke` | ✅ | Construcción headless de paneles y comandos Swing |
| `Phase6Smoke` | ✅ | Registro de listeners y cableado de controladores |
| `Phase8Smoke` | ✅ | Compra previa para reseñas, reseña única y descuento frecuente exacto |

## Lista de verificación funcional

- ✅ Registrar cliente Regular.
- ✅ Registrar cliente Premium.
- ✅ Iniciar sesión con correo y contraseña.
- ✅ Hash SHA-256 de contraseñas.
- ✅ Agregar libros al carrito.
- ✅ Aumentar y disminuir cantidades.
- ✅ Eliminar productos del carrito.
- ✅ Persistir carrito en JSON.
- ✅ Rechazar cantidades superiores al stock.
- ✅ Mostrar subtotal e impuestos separados.
- ✅ Confirmar compra Premium.
- ✅ Aplicar descuento Premium del 10 %.
- ✅ Actualizar inventario.
- ✅ Guardar historial de pedidos.
- ✅ Generar recibo TXT.
- ✅ Aplicar descuento frecuente desde el cuarto pedido.
- ✅ Rechazar eliminación de libros vendidos.
- ✅ Restaurar stock al cancelar un pedido.
- ✅ Validar reseñas sobre libros comprados.
- ✅ Rechazar reseñas de libros no comprados.
- ✅ Rechazar una segunda reseña del mismo cliente para el mismo libro.
- ✅ Validar descuento frecuente del 5 % desde el cuarto pedido.
- ✅ Conectar eventos Swing con `actionCommand`.

## Revisión SOLID

| Principio | Evidencia |
|---|---|
| **S** | `CatalogoService`, `CarritoService`, `CompraService`, `ReciboService`, `Mensajes`, repositorios y paneles tienen responsabilidades separadas. |
| **O** | `ClienteRegular`/`ClientePremium`, `LibroFisico`/`LibroDigital` y `PoliticaDescuentoAdicional` permiten extender comportamiento. |
| **L** | `LibroRepository`, `UsuarioRepository` y las abstracciones de `Libro`/`Cliente` se utilizan sin depender de subtipos concretos. |
| **I** | Existen interfaces pequeñas para libros, usuarios, pedidos, carritos, reseñas y registro. |
| **D** | Servicios reciben interfaces por constructor; las clases JSON solo se crean en `Main`. |

## Revisión de dependencias

- ✅ Ninguna clase de `model` importa Swing.
- ✅ Ninguna clase de `model` importa JSON.
- ✅ Ningún servicio importa Swing.
- ✅ Las vistas no crean repositorios ni servicios.
- ✅ `Main` es la raíz de composición.
- ✅ Las rutas se centralizan en `util.Rutas`.
- ✅ Los controladores muestran errores; los servicios lanzan excepciones.
- ✅ No se usa `System.exit` para errores de negocio.
- ✅ No se usa `null layout`.
- ✅ Las tablas son no editables.

## Limitación documentada

Gson 2.10.1 no pudo descargarse en el entorno por un error TLS. La serialización está aislada en `persistence.json.ArchivoJson`, por lo que la sustitución por Gson no requiere cambios en modelo, servicios, controladores ni vistas.
