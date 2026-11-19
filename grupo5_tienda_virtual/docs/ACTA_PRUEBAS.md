# Acta de pruebas

**Proyecto:** Tienda Virtual de Libros — Grupo 5  
**Fecha:** 19/11/2026  
**Responsable:** Juan Pablo Barrero

## 1. Compilación

| Prueba | Resultado |
|---|---|
| `mvn clean compile` | BUILD SUCCESS |
| Número de fuentes | 45 |
| Java | 21 |

## 2. Pruebas ejecutadas

### Clientes en archivo plano

- Se cargaron dos clientes desde un archivo temporal.
- Se verificó un cliente Premium y uno Regular.
- Se probó una línea incompleta: lanzó error con el número de línea.
- Se registró un cliente desde `ControladorCliente` y se escribió la operación en `operaciones.txt`.

Resultado: correcto.

### Compras en archivo plano

- Se cargó una compra con dos items desde un archivo temporal.
- Se verificó el total de 120000.
- Se guardó una compra y se volvió a cargar con otro repositorio.
- Se probaron IDs duplicados: falló con mensaje explícito.

Resultado: correcto.

### Finalizar compra sin GUI

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

### IVA distinto de 19% y 5%

- Libro de 100 con IVA 10%.
- IVA 19: 0.
- IVA 5: 0.
- Otros IVA: 10.
- Total: 110.
- Total con descuento del 10%: 100.

Resultado: correcto.

### Registro de operaciones

- Se registró un cliente con `ControladorOperaciones`.
- Se verificó la línea en `operaciones.txt`.

Resultado: correcto.

## 3. Pruebas pendientes

- Repetir la matriz completa para JSON y BD en los tres módulos.
- Probar el flujo visual completo de Login, Registro, Clientes, Libros y Mi Cuenta.
- Probar el recibo desde la interfaz.
- Probar el cambio de una línea en Compras de BD a JSON y volver a BD.
- Completar eventos de las ventanas que aún tienen listeners internos.

## 4. Observaciones

- El recibo se abre después de una compra exitosa.
- La compra y el descuento de stock no son atómicos cuando están en almacenamientos distintos.
- El registro de operaciones es de apoyo y no reemplaza la persistencia principal.
