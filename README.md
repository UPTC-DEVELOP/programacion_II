# Gestion de Productos - Grupo 5

Aplicacion de escritorio en Java (Swing) para el control de inventario y el
registro de ventas de una tienda.

Proyecto academico - Programacion II, 2026-II.

## Integrantes - Grupo 5

- Juan Pablo Barrero
- Cristhian Camilo Peña
- Liz Alejandra Rojas

## Descripcion

La aplicacion permite seleccionar productos, registrar pedidos descontando el
stock disponible y llevar un historial de las ventas, con la opcion de cancelar
un pedido para devolver la cantidad al inventario.

## Estado del proyecto

La estructura base ya esta lista: clase Producto, controlador de productos,
ventana principal y punto de entrada (Main), compilando y ejecutable. A partir de
aqui se agregan el descuento del producto, el IVA y la interfaz de eventos.

## Estructura

```
src/
  negocio/   -> logica del negocio (Producto y su controlador)
  gui/       -> interfaz grafica (ventanas y eventos)
```

## Division del trabajo

| Integrante | Responsabilidad |
|------------|-----------------|
| Juan Pablo Barrero | Estructura base del proyecto y calculo del descuento del producto |
| Cristhian Camilo Peña | Calculo del IVA del producto |
| Liz Alejandra Rojas | Interfaz grafica: seleccion de productos, registro de pedidos, historial y cancelacion |

## Como abrir en Eclipse

1. Abrir Eclipse.
2. Ir a File > Import > General > Existing Projects into Workspace.
3. En "Select root directory" elegir la carpeta del proyecto.
4. Pulsar Finish.
5. Abrir la clase gui.Main y ejecutarla con Run As > Java Application.

## Control de cambios

| Fecha | Integrante | Cambio a realizar |
|-------|------------|-------------------|
| 03/10/2026 | Juan Pablo Barrero | Crea la estructura base del proyecto (clase Producto, ControladorProducto y VentanaPrincipal) y agrega el atributo porcentajeDescuento, aplicandolo en el metodo calcularPrecioFinal(). |
| 03/10/2026 | Cristhian Camilo Peña | Agrega el atributo impuestoIVA al Producto y lo aplica en el metodo calcularPrecioFinal(), dejando el precio final con el IVA incluido. |
| 03/10/2026 | Liz Alejandra Rojas | Construye la interfaz: selector de productos (JComboBox) con evento de seleccion que actualiza precio base, stock, descuento e IVA; boton Registrar Pedido; tabla de historial (JTable); boton Cancelar Pedido Seleccionado con reversion de stock; y validaciones con JOptionPane. |
