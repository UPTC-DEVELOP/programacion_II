# Gestion de Productos - Grupo 5

Aplicacion de escritorio en Java (Swing) para el control de inventario y ventas
de una tienda.

Programacion II - 2026-II.

Repositorio: https://github.com/UPTC-DEVELOP/programacion_II/tree/feature/grupo5-gestion-productos

## Integrantes

- Juan Pablo Barrero
- Cristhian Camilo Peña
- Liz Alejandra Rojas

## Que hicimos

- `Producto` guarda nombre, precio base, stock, descuento e IVA, y el metodo
  `calcularPrecioFinal()` aplica primero el descuento y luego el IVA.
- El descuento y el IVA los hicimos por separado sobre el mismo archivo, asi que
  al integrarlos salio un conflicto de Git que resolvimos conservando los dos.
- `VentanaPrincipal` tiene un desplegable con los productos; al elegir uno se
  muestran el precio base, el stock, el descuento y el IVA.
- Boton "Registrar Pedido": revisa que la cantidad sea un numero y que no pase el
  stock, calcula el total, baja el stock y agrega el pedido a la tabla.
- Boton "Cancelar Pedido Seleccionado": devuelve el stock, borra la fila de la
  tabla y actualiza la vista.

## Reparto del trabajo

| Integrante | Parte |
|------------|-------|
| Juan Pablo Barrero | Base del proyecto y descuento |
| Cristhian Camilo Peña | IVA |
| Liz Alejandra Rojas | Interfaz (eventos, pedidos, historial y cancelacion) |

## Control de cambios

| Fecha | Integrante | Cambio |
|-------|------------|--------|
| 03/10/2026 | Juan Pablo Barrero | Estructura base del proyecto. |
| 03/10/2026 | Cristhian Camilo Peña | Atributo y calculo del IVA. |
| 03/10/2026 | Juan Pablo Barrero | Atributo y calculo del descuento. |
| 03/10/2026 | Juan Pablo / Cristhian | Resolucion del conflicto en Producto.java (descuento + IVA). |
| 03/10/2026 | Liz Alejandra Rojas | Interfaz: selector, registrar pedido, tabla y cancelar. |

## Como ejecutar

En Eclipse: importar el proyecto (File > Import > Existing Projects into
Workspace), abrir `src/gui/Main.java` y ejecutar con Run As > Java Application.

## Entrega

- Repositorio: https://github.com/UPTC-DEVELOP/programacion_II/tree/feature/grupo5-gestion-productos
- El codigo tambien se entrega comprimido (`.zip`).
