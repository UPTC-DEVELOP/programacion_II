# Guion de sustentación — Grupo 5

**Proyecto:** Tienda Virtual de Libros  
**Rama:** `feature/grupo5`

## 1. Apertura

Explicar que la aplicación está en Java Swing, con Maven y separada en capas:

- `model`
- `persistencia`
- `service`
- `controller`
- `view`
- `config`

## 2. Persistencia por módulo

Abrir `config/ProjectConfig.java` y mostrar:

- Clientes: archivo plano.
- Libros: JSON.
- Compras: base de datos.

Explicar que cada módulo puede usar cualquiera de las tres opciones y que se cambia una sola línea.

## 3. Prueba de una línea

1. Mostrar `compras()` devolviendo `comprasBD()`.
2. Cambiar a `comprasJson()`.
3. Ejecutar `mvn clean compile`.
4. Hacer una compra.
5. Mostrar `datos/compras.json`.
6. Volver a `comprasBD()` y mostrar `datos/tienda.db`.
7. Aclarar que no se tocó ninguna ventana ni controlador.

## 4. Compra y recibo

1. Iniciar sesión.
2. Agregar libros al carrito.
3. Finalizar compra.
4. Mostrar el recibo con cliente, fecha, pago, items y total.
5. Mostrar que el historial conserva la compra.

## 5. Eventos

Abrir `view/Eventos.java` y explicar que es la clase base.

Mostrar `EventosCompras` y `EventosHistorialCompras`.

Aclarar que las ventanas ya no crean compras ni guardan datos; solo delegan.

## 6. Registro de operaciones

Mostrar `datos/operaciones.txt` y explicar que los controladores registran altas, cambios y compras.

## 7. Pruebas

Mostrar:

- `docs/CHECKLIST_PRUEBAS.md`
- `docs/ACTA_PRUEBAS.md`
- `mvn clean compile` en verde

## 8. Cierre

Resumir:

- Se eliminó `usarBD` y `-Dtienda.bd`.
- Hay nueve repositorios: plano, JSON y BD por módulo.
- La lógica de compras está en `ControladorCompras`.
- Existe `Eventos`.
- Quedan pendientes los eventos de las ventanas de Cristhian y la matriz completa de pruebas.
