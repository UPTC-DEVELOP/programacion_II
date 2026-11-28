# Acta de pruebas

**Proyecto:** Tienda Virtual de Libros — Grupo 5  
**Fecha:** 28/11/2026  
**Responsable:** Juan Pablo Barrero

## 1. Compilación

| Prueba | Resultado |
|---|---|
| `mvn clean compile` | BUILD SUCCESS |
| Número de fuentes | 51 |
| Java | 21 |

## 2. Matriz 3x3

Se ejecutó un arnés temporal con los nueve repositorios, sin agregar código de prueba al repositorio.

| Módulo | Plano | JSON | BD |
|---|---|---|---|
| Clientes | OK | OK | OK |
| Libros | OK | OK | OK |
| Compras | OK | OK | OK |

Para cada celda se probó:

1. Agregar.
2. Guardar.
3. Recargar con una instancia nueva.
4. Verificar el dato.
5. Eliminar.
6. Guardar.
7. Recargar y verificar que quedó vacío.

Resultado: **9/9 correcto**.

## 3. Pruebas JUnit

Se agregó `src/test/java/persistencia/FlujosPersistenciaTest.java` con JUnit 5.

| Prueba | Resultado |
|---|---|
| Clientes: 12 registros, últimos 10 y orden | OK |
| Compra: guardar, reiniciar repositorios, historial y stock en plano/JSON/BD | OK |
| Fallo de guardado: no actualiza stock ni lista | OK |
| Libro con ventas: no se puede eliminar | OK |
| Carrito: guardar, recargar y vaciar en JSON | OK |
| Clientes: correo y contraseña inválidos se rechazan | OK |
| Libros: IVA/año/páginas inválidos se rechazan | OK |
| Compra sin stock: no se guarda ni descuenta inventario | OK |
| Compra Premium: aplica descuento | OK |
| Compra sin cliente, sin pago o con carrito vacío: rechazada | OK |
| Login con contraseña incorrecta: rechazado | OK |
| Eliminar libro sin ventas: permitido | OK |
| Inventario: actualiza y rechaza cantidad mayor al stock | OK |

Comando:

```bash
mvn test
```

Resultado individual con Maven:

```text
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Se ejecutaron las 16 pruebas una por una con `-Dtest=Clase#metodo`.
Todas pasaron.

Además se agregaron 3 pruebas CRUD completas:
- CRUD de Clientes.
- CRUD de Libros.
- CRUD de Compras y carrito.

Total: **16/16 pruebas correctas**.

Evidencias:

- `docs/evidencias/pruebas/`: logs de las 16 pruebas.
- `docs/evidencias/capturas/`: capturas de login, clientes, libros, carrito, recibo e historial.
- `docs/evidencias/capturas_eclipse/`: las mismas capturas a pantalla completa con Eclipse detrás.
- `docs/evidencias/crud/`: capturas por operación CRUD.
- `docs/evidencias/README.md`: índice de evidencias.

## 4. Prueba de cambio de una línea

Se cambió únicamente `ProjectConfig.compras()`:

- Inicial: `comprasBD()` → `RepositorioComprasBD`.
- Cambio: `comprasJson()` → `RepositorioComprasJson`.
- Vuelta: `comprasBD()` → `RepositorioComprasBD`.

Cada cambio se compiló con `mvn clean compile` y dio BUILD SUCCESS.

No se tocó ninguna ventana ni ningún controlador.

## 5. Finalizar compra sin GUI

- Se creó un libro con IVA 19%.
- Se agregó al carrito.
- Se llamó `ControladorCompras.finalizarCompra`.
- Se verificó compra guardada y stock descontado.

Resultado:

```text
exito=true
compras=1
stock=3
total=$238,00
```

## 6. IVA distinto de 19% y 5%

- Libro de 100 con IVA 10%.
- IVA 19: 0.
- IVA 5: 0.
- Otros IVA: 10.
- Total: 110.
- Total con descuento del 10%: 100.

Resultado: correcto.

## 7. Registro de operaciones

- Se registró un cliente con `ControladorOperaciones`.
- Se verificó la línea en `operaciones.txt`.

Resultado: correcto.

## 8. Pruebas que faltan

- Flujo visual completo de Login, Registro, Clientes, Libros y Mi Cuenta.
- Probar el recibo desde la interfaz.
- Catálogo y búsqueda separados para cliente.
- Combo de Compras corregido: carga los últimos 10 y permite buscar por ISBN, título o autor.

## 9. Observaciones

- La compra y el descuento de stock no son atómicos cuando están en almacenamientos distintos.
- El registro de operaciones es de apoyo y no reemplaza la persistencia principal.
- Las pruebas de persistencia ya están en el repositorio y se ejecutan con `mvn test`.
- El carrito se guarda en `datos/carritos/<correo>.json`.
- El recibo muestra subtotal, IVA, descuento Premium y total.
- La compra pide confirmación con resumen antes de guardar.
- Un libro con ventas asociadas no se puede eliminar.
