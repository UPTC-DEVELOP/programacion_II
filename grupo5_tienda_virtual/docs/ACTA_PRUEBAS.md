# Acta de pruebas

**Proyecto:** Tienda Virtual de Libros — Grupo 5  
**Fecha:** 20/11/2026  
**Responsable:** Juan Pablo Barrero

## 1. Compilación

| Prueba | Resultado |
|---|---|
| `mvn clean compile` | BUILD SUCCESS |
| Número de fuentes | 45 |
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

## 3. Prueba de cambio de una línea

Se cambió únicamente `ProjectConfig.compras()`:

- Inicial: `comprasBD()` → `RepositorioComprasBD`.
- Cambio: `comprasJson()` → `RepositorioComprasJson`.
- Vuelta: `comprasBD()` → `RepositorioComprasBD`.

Cada cambio se compiló con `mvn clean compile` y dio BUILD SUCCESS.

No se tocó ninguna ventana ni ningún controlador.

## 4. Finalizar compra sin GUI

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

## 5. IVA distinto de 19% y 5%

- Libro de 100 con IVA 10%.
- IVA 19: 0.
- IVA 5: 0.
- Otros IVA: 10.
- Total: 110.
- Total con descuento del 10%: 100.

Resultado: correcto.

## 6. Registro de operaciones

- Se registró un cliente con `ControladorOperaciones`.
- Se verificó la línea en `operaciones.txt`.

Resultado: correcto.

## 7. Pruebas que faltan

- Flujo visual completo de Login, Registro, Clientes, Libros y Mi Cuenta.
- Probar el recibo desde la interfaz.
- Eventos de las ventanas que aún tienen listeners internos.
- Catálogo y búsqueda separados para cliente.

## 8. Observaciones

- La compra y el descuento de stock no son atómicos cuando están en almacenamientos distintos.
- El registro de operaciones es de apoyo y no reemplaza la persistencia principal.
