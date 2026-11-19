# Pruebas manuales - Compras

**Responsable:** Liz Alejandra Rojas
**Configuracion por defecto:** `ProjectConfig.compras()` -> `comprasBD()`; `libros()` -> `librosJson()`; `clientes()` -> `clientesArchivoPlano()`



Libros de prueba que crea `Main`:

| ISBN | Titulo | Precio | IVA | Stock | Formato |
|------|--------|--------|-----|-------|---------|
| 978-001 | Java para principiantes | 50.000 | 19 % | 10 | FISICO |
| 978-002 | Programacion Orientada a Objetos | 65.000 | 19 % | 8 | FISICO |
| 978-003 | Fundamentos de Bases de Datos | 40.000 | 5 % | 15 | DIGITAL |

## Calculo esperado

`Total = Subtotal - (Subtotal x descuento) + IVA`

El descuento es 10 % para Premium y 0 % para Regular. El IVA se calcula sobre el subtotal **sin** descuento.

Ejemplo de referencia: 2 x 978-001 + 1 x 978-002 (Premium)
- Subtotal = 100.000 + 65.000 = 165.000
- IVA 19 % = 31.350
- Descuento 10 % = 16.500
- **Total = 165.000 - 16.500 + 31.350 = 179.850**

## Casos de prueba

| ID | Caso | Pasos | Resultado esperado |
|----|------|-------|--------------------|
| CO-01 | Agregar al carrito | Seleccionar libro, cantidad, **Agregar al carrito**. | El libro aparece en la tabla y se actualizan Subtotal, IVA y TOTAL. |
| CO-02 | Superar stock | Agregar mas unidades que el stock. | No se agrega la cantidad pedida (muestra mensaje de que no hay suficiente y y la cantidad disponible). |
| CO-03 | Modificar cantidad | Seleccionar fila y **Modificar cantidad**. | La cantidad y los totales cambian. |
| CO-04 | Eliminar / vaciar | **Eliminar** una fila y luego **Vaciar carrito**. | El carrito queda vacio y los totales en $0. |
| CO-05 | Descuento Premium | Cliente Premium con el ejemplo de referencia. | "Descuento Premium" y TOTAL coinciden con el calculo, Con Regular no hay descuento. |
| CO-06 | Carrito vacio | **Finalizar compra** sin items. | Mensaje "El carrito esta vacio." |
| CO-07 | Finalizar compra | Elegir metodo de pago y **Finalizar compra**. | Mensaje "COMPRA REALIZADA CORRECTAMENTE" con cliente, pago, fecha y total. Carrito vacio. |

| CO-09 | Historial | Mi Cuenta > **Historial**. | La compra aparece con fecha, pago, items y total correctos. |
| CO-10 | Persistencia | Cerrar la app, abrirla, iniciar sesion y abrir el historial. | La compra y sus items siguen ahi. |
| CO-11 | Dos compras | Hacer dos compras distintas, reiniciar, abrir historial. | Los items no se mezclan entre compras y los ids son distintos. |




