# Contrato de clases — Tienda Virtual de Libros

Definición de las clases compartidas del proyecto: responsabilidades, atributos,
tipos y firmas de métodos. Cualquier cambio debe acordarse con el equipo.

Reglas de dependencia entre capas:

- `negocio` no depende de `gui`.
- `persistencia` no depende de `gui`.
- `gui` depende de `negocio` y `persistencia`.

---

## Capa `negocio`

### `FormatoLibro` (enum)

```
FISICO
DIGITAL
AUDIO_LIBRO
```

### `Libro`

```
- isbn: String
- titulo: String
- autor: String
- anioPublicacion: int
- categoria: String
- editorial: String
- numeroPaginas: int
- precio: double
- porcentajeIva: double
- cantidadDisponible: int
- formato: FormatoLibro

+ Libro()
+ Libro(isbn, titulo, autor, precio, porcentajeIva, cantidadDisponible, formato)
+ getters y setters de todos los atributos
+ equals(Object): boolean    // por isbn
+ hashCode(): int
+ toString(): String
```

### `Cliente` (abstracta)

```
- nombreCompleto: String
- correo: String
- direccion: String
- telefono: String
- contrasena: String
- compras: List<Compra>

# Cliente()                                       // protected
# Cliente(nombreCompleto, correo, contrasena)     // protected
+ calcularDescuento(): double                     // abstracto
+ agregarCompra(compra: Compra): void
+ getters y setters
+ equals(Object): boolean    // por correo
+ hashCode(): int
+ toString(): String
```

### `ClienteRegular extends Cliente`

```
+ calcularDescuento(): double    // devuelve 0.0
```

### `ClientePremium extends Cliente`

```
+ DESCUENTO: double = 0.10       // constante
+ calcularDescuento(): double    // devuelve DESCUENTO
```

### `CarritoCompras`

```
- items: Map<Libro, Integer>

+ CarritoCompras()
+ agregarLibro(libro: Libro, cantidad: int): void
+ modificarCantidad(libro: Libro, nuevaCantidad: int): void
+ eliminarLibro(libro: Libro): void
+ vaciar(): void
+ calcularSubtotal(): double
+ calcularIVA19(): double
+ calcularIVA5(): double
+ calcularTotal(): double
+ getItems(): Map<Libro, Integer>
+ setItems(items: Map<Libro, Integer>): void
+ estaVacio(): boolean
```

### `ItemCompra`

```
- libro: Libro
- cantidad: int
- subtotal: double
- impuestos: double

+ ItemCompra()
+ ItemCompra(libro: Libro, cantidad: int)
+ calcularSubtotal(): void
+ calcularImpuestos(): void
+ getTotalLinea(): double
+ getters y setters
+ toString(): String
```

### `Compra`

```
- metodoPago: String
- fecha: LocalDate
- items: List<ItemCompra>

+ Compra()
+ Compra(metodoPago: String)
+ agregarItem(item: ItemCompra): void
+ calcularTotal(): double
+ getters y setters
```

> `metodoPago` guarda el nombre del método elegido (String, como en el diagrama).
> La clase `MetodoPago` representa el catálogo de opciones disponibles.

### `MetodoPago`

```
- nombre: String
- descripcion: String

+ MetodoPago()
+ MetodoPago(nombre, descripcion)
+ getters y setters
+ toString(): String
```

---

## Capa `persistencia`

### `GestorPersistencia`

```
+ GestorPersistencia()
+ GestorPersistencia(ruta: String)
+ getRuta(): String
+ setRuta(ruta: String): void

+ guardarLibros(libros: List<Libro>): void throws IOException
+ cargarLibros(): List<Libro> throws IOException
+ guardarClientes(clientes: List<Cliente>): void throws IOException
+ cargarClientes(): List<Cliente> throws IOException
+ guardarCompras(compras: List<Compra>): void throws IOException
+ cargarCompras(): List<Compra> throws IOException
```

### `GestorCarritoJSON`

```
+ guardarCarrito(carrito: CarritoCompras, nombreArchivo: String): void throws IOException
+ cargarCarrito(nombreArchivo: String, catalogo: List<Libro>): CarritoCompras throws IOException
```

### `RegistroOperacionesTXT`

```
+ registrarOperacion(descripcion: String): void throws IOException
```

> Utilizado por los controladores para registrar las operaciones del sistema.

---

## Capa `gui`

Punto de entrada y ventana principal. Cada módulo incorpora sus propias pantallas.

```
+ Main.main(args: String[]): void
+ VentanaPrincipal()
```

---

## Relaciones entre clases

```
Cliente (1) ---- (0..*) Compra
Compra (1) ---- (0..*) ItemCompra
ItemCompra (0..*) ---- (1) Libro
CarritoCompras (1) ---- (0..*) Libro
CarritoCompras (1) ---- (0..*) Compra    // el carrito genera una o más compras
```
