# Gestion de Productos - Grupo 5

Aplicacion de escritorio en Java (Swing) para el control de inventario y el
registro de ventas de una tienda.

Proyecto academico - Programacion II, 2026-II.

## Integrantes - Grupo 5

- Juan Pablo Barrero
- Cristhian Camilo Peña
- Liz Alejandra Rojas

## Descripcion

La aplicacion permite seleccionar productos, registrar pedidos aplicando el
descuento y el IVA del producto, descontar el stock disponible y llevar un
historial de las ventas en una tabla. Tambien permite cancelar un pedido
seleccionado para devolver la cantidad al inventario.

## Estructura

```
src/
  negocio/   -> logica de negocio (Producto y su controlador)
  gui/       -> interfaz grafica (VentanaPrincipal y Main)
```

## Desarrollo del proyecto (que se hizo y como)

### 1. Control de versiones y resolucion de conflictos (Modulo A)
- Se trabajo sobre la rama del grupo `feature/grupo5-gestion-productos`.
- **Estudiante A (Juan Pablo):** agrego el atributo `porcentajeDescuento` y lo
  aplico en el metodo `calcularPrecioFinal()`.
- **Estudiante B (Cristhian):** agrego el atributo `impuestoIVA` y lo aplico en
  el metodo `calcularPrecioFinal()`.
- Como ambos cambios se hicieron sobre las **mismas lineas del mismo archivo**,
  al integrarlos Git genero un **conflicto de fusion (merge conflict)** en
  `Producto.java`.
- El conflicto se resolvio de forma conjunta conservando **las dos**
  funcionalidades, dejando el metodo asi:

```java
public double calcularPrecioFinal() {
    double precioConDescuento = precioBase - (precioBase * porcentajeDescuento);
    return precioConDescuento + (precioConDescuento * impuestoIVA);
}
```
- El commit de resolucion quedo registrado en el historial del repositorio.

### 2. Interfaz grafica y eventos (Modulo B)
- `VentanaPrincipal`: incluye un selector de productos (`JComboBox`). Al
  seleccionar un producto se dispara un evento (`ActionListener`) que actualiza
  en pantalla el **precio base, el stock, el descuento y el IVA**.
- Boton **"Registrar Pedido"**: toma la cantidad digitada por el usuario,
  calcula el valor total a pagar y actualiza los datos.
- Manejo de excepciones: si el usuario ingresa una cantidad **no numerica** o
  que **supera el stock actual**, se captura la excepcion y se muestra un cuadro
  de dialogo (`JOptionPane`) informando del error, **sin cerrar** la aplicacion.

### 3. Logica de negocio e historial de stock (Modulo C)
- Al registrar un pedido exitosamente se **decrementa el stock** del producto en
  memoria.
- Cada transaccion se agrega dinamicamente a un componente de tabla (`JTable`).
- Boton **"Cancelar Pedido Seleccionado"**: al seleccionar una fila se
  **devuelve la cantidad comprada al stock**, se **elimina el registro** de la
  tabla y se **actualiza la vista** en tiempo real.

## Como abrir y ejecutar en Eclipse

1. Abrir Eclipse.
2. Ir a File > Import > General > Existing Projects into Workspace.
3. En "Select root directory" elegir la carpeta del proyecto.
4. Pulsar Finish.
5. Abrir la clase gui.Main y ejecutarla con Run As > Java Application.

## Division del trabajo

| Integrante | Responsabilidad |
|------------|-----------------|
| Juan Pablo Barrero | Estructura base del proyecto, calculo del descuento del producto y resolucion del conflicto |
| Cristhian Camilo Peña | Calculo del IVA del producto |
| Liz Alejandra Rojas | Interfaz grafica: seleccion de productos, registro de pedidos, historial y cancelacion |

## Control de cambios

| Fecha | Integrante | Cambio realizado |
|-------|------------|------------------|
| 03/10/2026 | Juan Pablo Barrero | Se crea la estructura base del proyecto: clase Producto, ControladorProducto, VentanaPrincipal y Main. |
| 03/10/2026 | Cristhian Camilo Peña | Se agrega el atributo `impuestoIVA` y se aplica en `calcularPrecioFinal()`. |
| 03/10/2026 | Juan Pablo Barrero | Se agrega el atributo `porcentajeDescuento` y se aplica en `calcularPrecioFinal()`. |
| 03/10/2026 | Juan Pablo Barrero / Cristhian Camilo Peña | Se integran los cambios y se resuelve el conflicto en `Producto.java` conservando descuento e IVA. |
| 03/10/2026 | Liz Alejandra Rojas | Se construye la interfaz: selector de productos con evento, boton Registrar Pedido con validaciones, tabla de historial (JTable) y boton Cancelar Pedido con reversion de stock. |
