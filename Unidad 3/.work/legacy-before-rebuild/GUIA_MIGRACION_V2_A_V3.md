# Guía de migración e integración — Tienda Virtual de Libros V2 → V3

Esta guía describe el orden recomendado para incorporar la V3 sobre la V2 sin romper el módulo de catálogo ni el módulo de pedidos que ya existían.

## 0. Punto de partida y criterios

La V3 conserva los paquetes existentes:

```text
co.edu.uptc.gui
co.edu.uptc.negocio
```

Se mantienen funcionando estas piezas de V2:

- `Producto`
- `Libro`
- `LibroFisico`
- `LibroDigital`
- `FormatoLibro`
- `LibroFactory`
- `LibroRepositorio` y `LibroRepositorioJson`
- `CatalogoService`
- `Pedido`, `PedidoService` y sus repositorios
- `VentaRepositorio` y `VentaRepositorioJson`
- bitácora TXT
- ventanas y controladores de catálogo y pedidos

La V3 añade el flujo completo de cliente → carrito → checkout → compra → recibo → historial.

### Decisión de negocio documentada

El caso de estudio exige un “descuento especial” para clientes Premium pero no indica el porcentaje. En V3 se configuró **10 %** mediante `Cliente.DESCUENTO_PREMIUM`. Si el docente/proyecto define otro porcentaje, se cambia únicamente esa constante y se recompila.

La autenticación necesita una credencial; por eso se añadió `contrasena` al formulario de cliente, aunque el texto del caso solo enumera el correo. La contraseña se persiste como SHA-256, no como texto plano.

JDBC **no se incorpora en V3** porque el propio caso lo ubica como una evolución posterior. Los repositorios siguen siendo interfaces para permitir sustituir JSON por JDBC más adelante.

---

## Commit 1: Modelo de cliente

**Qué hacer:** cree en `src/co/edu/uptc/negocio` los archivos:

- `TipoCliente.java`
- `Cliente.java`
- `PasswordUtil.java`

**Qué hace:**

`TipoCliente` define `REGULAR` y `PREMIUM`.

`Cliente` almacena:

- nombre completo
- correo electrónico
- dirección de envío
- teléfono
- tipo de cliente
- hash de contraseña

`Cliente.esPremium()` identifica si corresponde descuento.

`PasswordUtil` centraliza el hash SHA-256 para no guardar contraseñas en texto plano.

**Cómo se conecta:** todavía no guarda datos ni aparece en Swing. El siguiente commit conecta este modelo con persistencia y negocio.

**Commit:** `agregar modelo de Cliente, tipos de cliente y hash de contraseña`

---

## Commit 2: Persistencia de clientes

**Qué hacer:** cree:

- `ClienteRepositorio.java`
- `ClienteRepositorioJson.java`

**Qué hace:**

La interfaz define las operaciones de persistencia:

```text
obtenerTodos()
buscarPorCorreo()
guardar()
eliminar()
```

`ClienteRepositorioJson` almacena los clientes en `data/clientes.json`.

**Cómo se conecta:** `ClienteService` dependerá de la interfaz, no de la implementación JSON. Esto mantiene DIP.

**Commit:** `agregar repositorio JSON para clientes`

---

## Commit 3: Reglas de negocio de clientes

**Qué hacer:** cree `ClienteService.java`.

**Qué hace:** concentra:

- registro de cliente
- validación de campos obligatorios
- validación de correo
- validación de teléfono
- validación de contraseña
- actualización de datos
- autenticación
- escritura de bitácora

**Cómo se conecta:** recibe `ClienteRepositorio` y `BitacoraRepositorio` por constructor.

```text
ClienteService
   ├── ClienteRepositorio
   └── BitacoraRepositorio
```

**Commit:** `crear servicio de clientes con validaciones y autenticacion`

---

## Commit 4: Sesión del cliente

**Qué hacer:** cree `SesionService.java`.

**Qué hace:** conserva en memoria el cliente autenticado y permite:

```text
iniciarSesion()
cerrarSesion()
estaAutenticado()
getClienteAutenticado()
exigirAutenticacion()
```

**Cómo se conecta:** no depende de Swing. Los controladores consultan la sesión para permitir o bloquear compras.

**Commit:** `agregar servicio de sesion y control de autenticacion`

---

## Commit 5: Modelo del carrito

**Qué hacer:** cree:

- `CarritoItem.java`
- `Carrito.java`

**Qué hace `CarritoItem`:** representa una línea del carrito y calcula:

- precio unitario sin IVA
- subtotal
- impuesto
- total de línea

**Qué hace `Carrito`:** administra varios libros por ISBN y permite:

- agregar
- aumentar
- disminuir
- eliminar
- vaciar
- calcular subtotal
- calcular impuestos
- calcular total antes del descuento Premium

**Cómo se conecta:** todavía no persiste. El siguiente commit agrega JSON.

**Commit:** `crear modelo de carrito e items con subtotales e impuestos`

---

## Commit 6: Persistencia y servicio del carrito

**Qué hacer:** cree:

- `CarritoRepositorio.java`
- `CarritoRepositorioJson.java`
- `CarritoService.java`

**Qué hace:**

El carrito de cada cliente se persiste en `data/carritos.json`.

`CarritoService` valida stock al agregar o aumentar cantidades y guarda los cambios después de cada operación.

También incluye `refrescarPrecios()` para que, al volver a entrar, el carrito tome los datos actuales del catálogo.

**Cómo se conecta:**

```text
CarritoService
   ├── LibroRepositorio
   ├── CarritoRepositorio
   └── BitacoraRepositorio
```

**Commit:** `persistir carrito en JSON y agregar operaciones de compra`

---

## Commit 7: Modelo de compra y método de pago

**Qué hacer:** cree:

- `MetodoPago.java`
- `DetalleCompra.java`
- `Compra.java`

**Qué hace:**

`MetodoPago` permite simular:

- Tarjeta
- PSE
- Transferencia bancaria
- Efectivo

`DetalleCompra` conserva por línea:

- ISBN
- título
- formato
- cantidad
- precio unitario sin IVA
- subtotal
- impuesto
- total

`Compra` representa el checkout confirmado y guarda:

- ID
- fecha
- cliente
- tipo de cliente
- método de pago
- detalles
- subtotal
- impuestos
- descuento Premium
- total

**Cómo se conecta:** el carrito se transforma en una `Compra` únicamente al finalizar el checkout.

**Commit:** `crear modelo de compra detalles y metodo de pago`

---

## Commit 8: Persistencia e implementación del checkout

**Qué hacer:** cree:

- `CompraRepositorio.java`
- `CompraRepositorioJson.java`
- `CompraService.java`

**Qué hace `CompraService`:**

1. valida que exista cliente autenticado
2. valida que el carrito no esté vacío
3. valida stock actual de cada libro
4. calcula subtotal
5. calcula impuestos
6. calcula descuento Premium
7. calcula total
8. descuenta inventario
9. guarda la compra
10. vacía el carrito
11. registra bitácora
12. solicita la generación del recibo

**Regla de cálculo Premium usada en V3:**

```text
subtotal = suma de subtotales de cada línea
impuestos = suma de IVA de cada línea
totalAntesPremium = subtotal + impuestos

si cliente es Premium:
    descuento = totalAntesPremium * 10%
si no:
    descuento = 0

total = totalAntesPremium - descuento
```

**Cómo se conecta:** `CompraService` depende de interfaces (`LibroRepositorio`, `CompraRepositorio`, etc.), no de Swing.

**Commit:** `implementar checkout con inventario descuento Premium e historial`

---

## Commit 9: Generación del recibo

**Qué hacer:** cree `ReciboService.java`.

**Qué hace:** genera un comprobante TXT en:

```text
data/recibos/recibo-<id>.txt
```

El archivo contiene cliente, fecha, método de pago, productos, subtotales, impuestos, descuento Premium y total.

**Cómo se conecta:** `CompraService` lo llama únicamente después de haber persistido la compra.

**Commit:** `generar recibo TXT de compra`

---

## Commit 10: Pantalla de clientes

**Qué hacer:** cree:

- `PanelCliente.java`
- `VentanaCliente.java`
- `ControladorCliente.java`

**Qué hace:** permite:

- registrar
- iniciar sesión
- cerrar sesión
- actualizar datos
- seleccionar Regular/Premium

**Cómo se conecta:**

```text
PanelCliente
      ↓ eventos
ControladorCliente
      ↓
ClienteService + SesionService
      ↓
ClienteRepositorio
```

La pantalla no escribe JSON directamente.

**Commit:** `crear interfaz Swing para registro login y clientes`

---

## Commit 11: Pantalla de tienda y carrito

**Qué hacer:** cree:

- `PanelTienda.java`
- `VentanaTienda.java`
- `PanelHistorialCompras.java`
- `VentanaHistorialCompras.java`
- `ControladorTienda.java`

**Qué hace la tienda:**

- mostrar catálogo
- buscar libros
- agregar al carrito
- aumentar cantidad
- disminuir cantidad
- eliminar del carrito
- vaciar carrito
- mostrar subtotal por línea
- mostrar impuestos
- mostrar descuento Premium
- mostrar total
- mostrar sesión activa
- abrir historial del cliente
- finalizar compra

**Cómo se conecta:**

```text
Swing
 ↓
ControladorTienda
 ├── CatalogoService
 ├── CarritoService
 ├── CompraService
 └── SesionService
```

**Commit:** `crear tienda Swing con carrito checkout e historial`

---

## Commit 12: Integración de los controladores

**Qué hacer:** modifique:

- `Comandos.java`
- `MenuPrincipal.java`
- `VentanaPrincipal.java`
- `Runner.java`

**Qué hace:** registra las nuevas ventanas/controladores y crea las dependencias en un único punto de composición.

Se agregan al menú:

```text
Clientes
  Gestionar clientes / Iniciar sesión

Ventas
  Tienda virtual / Carrito
  Registrar pedidos (modo legado)
```

El módulo de pedidos de V2 se conserva como modo legado para no romper lo existente.

**Cómo se conecta:** `Runner` inyecta implementaciones concretas y mantiene la comunicación entre módulos mediante callbacks `Runnable`.

**Commit:** `integrar V3 en Runner y menu principal sin romper V2`

---

## Commit 13: Sincronización catálogo ↔ carrito/pedidos

**Qué hacer:** no cree clases nuevas. Verifique las conexiones de `Runner`.

**Qué hace:**

- si se modifica catálogo, se refresca tienda y pedidos
- si cambia inventario mediante el módulo legado, se refresca catálogo y tienda
- si cambia sesión, tienda vuelve a cargar el carrito correspondiente

**Commit:** `sincronizar catalogo inventario sesion y carrito`

---

## Commit 14: Compatibilidad de ventas asociadas

**Qué hacer:** modifique `VentaRepositorioJson.java`.

**Qué hace:** además de revisar `ventas.json`, revisa `compras.json` de V3. Así, un libro que ya haya sido comprado por el nuevo flujo tampoco puede ser eliminado desde la gestión del catálogo.

**Commit:** `proteger libros con compras asociadas en el catalogo`

---

## Commit 15: Ajuste de cálculo de precio

**Qué hacer:** modifique `Producto.java`.

**Qué hace:** se añade `calcularPrecioSinIva()` y `calcularPrecioFinal()` se apoya en ese cálculo.

Esto permite que el carrito muestre correctamente:

```text
precio con descuento → subtotal → IVA → total
```

El comportamiento anterior de `calcularPrecioFinal()` se conserva.

**Commit:** `separar calculo de precio sin IVA y precio final`

---

## Commit 16: Alinear datos semilla

**Qué hacer:** modifique `LibroRepositorioJson.java` únicamente en los datos semilla.

**Qué hace:** los descuentos semilla quedan alineados con el `data/libros.json` incluido en la V2. Esto evita que una instalación sin `libros.json` se comporte diferente a la instalación que sí lo contiene.

**Commit:** `alinear datos semilla del catalogo con libros.json`

---

## Commit 17: Archivos de datos V3

**Qué hacer:** agregue al directorio `data`:

```text
clientes.json
carritos.json
compras.json
```

Inicialmente pueden contener solamente:

```json
[]
```

`bitacora.txt` y los recibos pueden crearse automáticamente al ejecutar.

**Commit:** `agregar archivos JSON iniciales de V3`

---

## Commit 18: Validación final

**Qué hacer:** compile todo el proyecto y pruebe los casos principales.

### Compilación

Desde la raíz del proyecto:

```bash
rm -rf bin
mkdir bin
javac -encoding UTF-8 -d bin $(find src -name '*.java' -print)
```

### Prueba mínima de negocio

Debe comprobarse como mínimo:

1. Registrar cliente Regular.
2. Registrar cliente Premium.
3. Iniciar sesión con correo y contraseña.
4. Agregar un libro al carrito.
5. Aumentar cantidad.
6. Disminuir cantidad.
7. Eliminar un item.
8. Volver a agregar y verificar persistencia del carrito.
9. Intentar superar stock y recibir validación.
10. Mostrar subtotal e impuestos separados.
11. Confirmar compra Premium.
12. Comprobar descuento Premium.
13. Comprobar actualización del stock.
14. Comprobar compra en `compras.json`.
15. Comprobar recibo TXT.
16. Comprobar historial del cliente.
17. Intentar eliminar un libro comprado y comprobar que se rechaza.
18. Verificar que el flujo de pedidos de V2 continúa funcionando.

**Commit:** `validar V3 completa y compatibilidad con V2`

---

# Estructura final esperada

```text
gp1_TiendaVirtual/
├── data/
│   ├── libros.json
│   ├── clientes.json
│   ├── carritos.json
│   ├── compras.json
│   ├── ventas.json
│   ├── bitacora.txt
│   └── recibos/
│
├── src/co/edu/uptc/negocio/
│   ├── Producto.java
│   ├── Libro.java
│   ├── LibroFisico.java
│   ├── LibroDigital.java
│   ├── LibroFactory.java
│   ├── FormatoLibro.java
│   ├── ... clases V2 ...
│   ├── TipoCliente.java
│   ├── Cliente.java
│   ├── PasswordUtil.java
│   ├── ClienteRepositorio.java
│   ├── ClienteRepositorioJson.java
│   ├── ClienteService.java
│   ├── SesionService.java
│   ├── CarritoItem.java
│   ├── Carrito.java
│   ├── CarritoRepositorio.java
│   ├── CarritoRepositorioJson.java
│   ├── CarritoService.java
│   ├── MetodoPago.java
│   ├── DetalleCompra.java
│   ├── Compra.java
│   ├── CompraRepositorio.java
│   ├── CompraRepositorioJson.java
│   ├── CompraService.java
│   └── ReciboService.java
│
└── src/co/edu/uptc/gui/
    ├── ... clases V2 ...
    ├── PanelCliente.java
    ├── VentanaCliente.java
    ├── ControladorCliente.java
    ├── PanelTienda.java
    ├── VentanaTienda.java
    ├── PanelHistorialCompras.java
    ├── VentanaHistorialCompras.java
    └── ControladorTienda.java
```

# Recomendación de fusión en Git

La forma más segura es incorporar los commits en el orden anterior y ejecutar la compilación después de cada commit funcional, especialmente después de los commits 6, 8, 11 y 12.

No copie manualmente archivos `.class`; el directorio `bin` debe regenerarse mediante `javac` o desde Eclipse.

No elimine las clases `Pedido`, `PedidoService`, `PedidoRepositorioJson`, `VentaRepositorioJson` ni las ventanas heredadas: V3 las conserva deliberadamente para compatibilidad.

# Resultado funcional esperado en V3

```text
Cliente
   ↓
Login / Sesión
   ↓
Catálogo
   ↓
Carrito
   ├── cantidades
   ├── subtotales
   ├── impuestos
   └── descuento Premium
   ↓
Resumen
   ↓
Método de pago simulado
   ↓
Compra
   ├── actualiza inventario
   ├── guarda historial
   ├── genera recibo
   └── vacía carrito
```
