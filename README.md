# Tienda Virtual de Libros

Aplicación de escritorio desarrollada en Java 17, Swing y programación orientada a objetos para la asignatura Programación II de la UPTC.

## Arquitectura

El proyecto usa MVC modificado con separación por responsabilidades:

```text
co.edu.uptc/
├── model/         Entidades, herencia y polimorfismo
├── exception/     Excepciones propias del dominio
├── persistence/   Interfaces de repositorios
│   ├── json/      Implementaciones de almacenamiento JSON
│   └── txt/       Registro de operaciones
├── service/       Reglas de negocio y casos de uso
├── controller/    Eventos Swing y coordinación
├── view/          Ventanas, paneles y diálogos
└── util/          Validación, hash, rutas, fechas y moneda
```

`Main.java` es la única raíz de composición. Allí se crean las implementaciones concretas y se inyectan mediante constructores.

## Funcionalidades

- CRUD de libros para el administrador.
- Validación de ISBN, precio, stock, IVA y descuentos.
- Registro e inicio de sesión de clientes.
- Clientes Regular y Premium.
- Contraseñas protegidas con SHA-256.
- Carrito persistente por correo electrónico.
- Compra desde carrito y compra rápida.
- Cálculo separado de subtotal, IVA y total bruto.
- Descuento Premium del 10 %.
- Descuento adicional del 5 % desde el cuarto pedido.
- Actualización automática del inventario.
- Cancelación de pedidos con reposición de stock.
- Historial de compras.
- Recibos TXT.
- Registro de operaciones TXT.
- Reseñas de libros comprados.
- Eventos Swing mediante `ActionListener`, `ItemListener` y `ListSelectionListener`.

## Reglas principales

El precio base se considera antes de IVA:

```text
precioConDescuento = precioBase * (1 - descuentoProducto / 100)
subtotalLinea = precioConDescuento * cantidad
impuestoLinea = subtotalLinea * IVA / 100
totalBruto = subtotal + impuestos
descuentoCliente = cliente.calcularDescuento(totalBruto)
total = totalBruto - descuentos
```

Ejemplo oficial:

```text
2 × Cien años de soledad a $50.000
Subtotal: $100.000
IVA 19 %: $19.000
Total bruto: $119.000
Descuento Premium: $11.900
Total a pagar: $107.100
```

## Usuarios semilla

| Rol             | Correo               | Contraseña  |
| --------------- | -------------------- | ----------- |
| Administrador   | `admin@libreria.com` | `Admin123`  |
| Cliente Regular | `ana@correo.com`     | `Ana12345`  |
| Cliente Premium | `carlos@correo.com`  | `Carlos123` |
