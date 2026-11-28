# 4.3 Compras

**Responsables:** Liz Alejandra Rojas / Juan Pablo Barrero en integración  
**Requisitos:** RF08 Historial, RF10 Agregar al carrito, RF11 Modificar cantidad, RF12 Eliminar del carrito, RF13 Calcular compra, RF14 Descuento Premium, RF16 Finalizar compra, RF17 Método de pago, RF18 Validar disponibilidad, RF19 Actualizar inventario, RF20 Generar recibo.

## a) Caso de uso

- Actor: Cliente.
- Casos: agregar libro, modificar cantidad, eliminar, vaciar, calcular, confirmar compra, generar recibo y consultar historial.

**Evidencia:** pendiente de diagrama específico en el DAD final.

## b) Especificación

- Validar sesión y carrito.
- Validar stock.
- Calcular subtotal, IVA, descuento Premium y total.
- Confirmar resumen antes de comprar.
- Guardar compra.
- Actualizar inventario.
- Generar recibo.
- Guardar carrito en JSON.

## c) Diagrama de clases

Debe incluir:

- `CarritoCompras`, `Compra`, `ItemCompra`, `MetodoPago`, `ResultadoCompra`.
- `IServicioCompras`, `IServicioCarrito`.
- `ControladorCompras`, `ControladorCarrito`.
- `IRepositorio<Compra>` y sus tres implementaciones.
- `GestorCarritoJSON`.
- `EventosCompras`, `EventosHistorialCompras`, `EventosRecibo`.

**Evidencia:** pendiente de diagrama en el DAD final.

## d) Diagrama de secuencia

Debe mostrar dos flujos separados:

```text
Guardar compra: EventosCompras → ControladorCompras → IRepositorio → almacenamiento
Listar historial: EventosHistorialCompras → ControladorCompras → IRepositorio → almacenamiento
```

**Evidencia:** pendiente de diagrama en el DAD final.

## e) Código y commits

- `ControladorCompras.finalizarCompra`.
- `EventosCompras`.
- `ControladorCarrito`.
- `VentanaRecibo`.

**Evidencia:** pendiente de fragmento y enlace en el DAD final.

## f) Pruebas individuales

| ID | Operación | Evidencia | Estado |
|---|---|---|---|
| PI-COM-01 | Carrito: agregar/modificar/eliminar | `pruebas/16_CrudCompletoTest_comprasCrudCompleto.txt` | OK |
| PI-COM-02 | Finalizar compra | `pruebas/16_CrudCompletoTest_comprasCrudCompleto.txt` | OK |
| PI-COM-03 | Historial por cliente | `pruebas/16_CrudCompletoTest_comprasCrudCompleto.txt` | OK |
| PI-COM-04 | Compra sin stock | `pruebas/08_ReglasNegocioTest_noFinalizaCompraSinStock.txt` | OK |
| PI-COM-05 | Descuento Premium | `pruebas/09_ReglasNegocioTest_aplicaDescuentoPremiumEnLaCompra.txt` | OK |
| PI-COM-06 | Compra inválida | `pruebas/10_ReglasNegocioTest_rechazaCompraSinClienteOSinMetodoPago.txt` | OK |
| PI-COM-07 | Carrito JSON | `pruebas/05_FlujosPersistenciaTest_carritoSePersisteEnJson.txt` | OK |
| PI-COM-08 | Fallo de guardado | `pruebas/03_FlujosPersistenciaTest_falloAlGuardarNoActualizaStockNiLista.txt` | OK |

## Capturas

- `capturas_eclipse/06_compras_carrito.png`
- `capturas_eclipse/07_recibo.png`
- `capturas_eclipse/08_historial_compras.png`
- `crud/PI-COM-01_carrito.png`
- `crud/PI-COM-02_recibo.png`
- `crud/PI-COM-03_historial.png`
