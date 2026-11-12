# Manual definitivo para comprender y reconstruir la Tienda Virtual de Libros

> **Objetivo:** aprender Java, programación orientada a objetos, SOLID, arquitectura por capas, persistencia, Swing, eventos, pruebas y depuración usando como ejemplo real esta aplicación.

Este manual está escrito para que pueda estudiar el proyecto sin limitarse a copiar código. La meta es que llegue a reconocer un problema, diseñar sus clases, elegir responsabilidades, implementar la solución, probarla y corregirla por usted mismo.

---

## 1. Cómo estudiar este manual

No intente memorizar las 80 clases del proyecto. En programación profesional no se memoriza cada línea: se aprende a leer una arquitectura, buscar una clase, seguir el flujo de datos y tomar decisiones.

Para cada clase utilice siempre estas preguntas:

1. ¿Qué problema resuelve?
2. ¿Qué información conserva?
3. ¿Qué responsabilidad tiene y cuál no tiene?
4. ¿Quién la crea?
5. ¿Quién la utiliza?
6. ¿Qué recibe cada método?
7. ¿Qué devuelve?
8. ¿Qué errores puede producir?
9. ¿Cómo se prueba?

La forma recomendada de avanzar es:

```text
Java básico
  ↓
POO
  ↓
modelo del dominio
  ↓
interfaces y persistencia
  ↓
servicios
  ↓
Swing y eventos
  ↓
controladores
  ↓
pruebas y depuración
  ↓
reconstrucción desde cero
```

Cuando una explicación incluya un fragmento del proyecto, búsquelo en Eclipse y léalo completo. Después cierre el archivo e intente explicar el código con sus propias palabras.

---

## 2. Qué problema resuelve la aplicación

La aplicación representa una tienda virtual de libros de escritorio. Permite:

- Registrar e iniciar sesión como administrador o cliente.
- Administrar libros del catálogo.
- Diferenciar clientes Regular y Premium.
- Aplicar descuentos.
- Agregar libros a un carrito.
- Validar stock.
- Confirmar pedidos.
- Actualizar inventario.
- Consultar historial.
- Cancelar pedidos y reponer stock.
- Generar recibos TXT.
- Guardar información en archivos JSON.
- Registrar reseñas de libros comprados.
- Aplicar un descuento adicional a clientes frecuentes.

El programa no es una sola clase grande. Es un conjunto de objetos que colaboran:

```text
Usuario
  inicia sesión
      ↓
Cliente
  consulta libros
      ↓
Carrito
  conserva los productos elegidos
      ↓
CompraService
  valida y confirma la compra
      ↓
Pedido
  conserva el historial
      ↓
Repository
  guarda la información
```

---

## 3. Cómo está organizado el proyecto

```text
src/co/edu/uptc/
├── Main.java
├── model/
├── exception/
├── persistence/
│   ├── json/
│   └── txt/
├── service/
├── controller/
├── view/
└── util/
```

### `model`

Contiene los objetos del negocio: libros, clientes, carrito, pedidos y reseñas. No debe conocer Swing ni archivos JSON.

### `exception`

Contiene errores con significado para el dominio: stock insuficiente, credenciales inválidas, ISBN duplicado, etc.

### `persistence`

Define los contratos para guardar y recuperar información. Sus interfaces permiten cambiar JSON por una base de datos en el futuro.

### `persistence.json` y `persistence.txt`

Contienen implementaciones concretas. JSON guarda entidades; TXT registra operaciones y recibos.

### `service`

Contiene los casos de uso y las reglas que coordinan varias entidades: autenticación, carrito, compra, catálogo y reseñas.

### `controller`

Recibe eventos de Swing y llama a los servicios. No debe implementar las reglas principales del negocio.

### `view`

Construye ventanas, paneles, formularios y tablas. No debe guardar directamente en JSON ni calcular reglas de compra.

### `util`

Contiene funciones reutilizables: validación, hash, rutas, fechas y moneda.

### `Main`

Es la raíz de composición. Crea las implementaciones concretas, las conecta y arranca Swing.

---

## 4. Java esencial que necesita dominar

### 4.1 Variables y tipos

```java
String titulo = "Cien años de soledad";
int cantidad = 2;
double precio = 50000.0;
boolean disponible = true;
```

- `String`: texto.
- `int`: números enteros.
- `double`: números decimales.
- `boolean`: verdadero o falso.

En el proyecto, `precioBase`, `impuestoIVA` y `porcentajeDescuento` son `double`; `stock` y `cantidad` son `int`.

### 4.2 Métodos

Un método agrupa una operación:

```java
public double calcularSubtotal(int cantidad) {
    return precioBase * cantidad;
}
```

Partes:

```text
public       visibilidad
 double      tipo devuelto
 calcularSubtotal nombre
 (int ...)    parámetros
 return       resultado
```

Un método `void` no devuelve resultado:

```java
public void vaciar() {
    items.clear();
}
```

### 4.3 Condicionales

```java
if (cantidad <= 0) {
    throw new ValidacionException("La cantidad debe ser positiva.");
}
```

El bloque se ejecuta cuando la condición es verdadera.

### 4.4 Ciclos

```java
for (ItemCarrito item : carrito.getItems()) {
    subtotal += item.calcularSubtotal();
}
```

Este ciclo recorre todos los objetos `ItemCarrito` del carrito.

### 4.5 Colecciones

```java
List<Libro> libros = new ArrayList<Libro>();
```

- `List`: contrato de una lista.
- `ArrayList`: implementación concreta.
- `Libro`: tipo de cada elemento.

Se programa contra la interfaz cuando es posible:

```java
List<Libro> libros;
```

y no se expone innecesariamente:

```java
ArrayList<Libro> libros;
```

### 4.6 `Optional`

Un repositorio puede no encontrar un libro:

```java
Optional<Libro> resultado = libros.buscarPorIsbn(isbn);
```

Luego se decide qué hacer:

```java
Libro libro = resultado.orElseThrow(
    () -> new ValidacionException("El libro no existe.")
);
```

`Optional` hace visible que la búsqueda puede no producir resultado.

### 4.7 Excepciones

```java
try {
    compras.finalizarCompra(carrito, metodo);
} catch (TiendaException e) {
    Mensajes.error(vista, e.getMessage());
}
```

El servicio detecta una situación inválida y lanza una excepción. El controlador decide cómo mostrarla.

### 4.8 Enumeraciones

```java
MetodoPago metodo = MetodoPago.EFECTIVO;
FormatoLibro formato = FormatoLibro.FISICO;
```

Una enumeración limita las opciones válidas y evita errores como escribir libremente `"efectibo"`.

### 4.9 `final`

```java
private final LibroRepository libros;
```

Significa que la referencia se asigna en el constructor y no debe cambiar. Es apropiado para dependencias de un servicio.

### 4.10 `static`

Un miembro `static` pertenece a la clase y no a un objeto particular. Se usa en utilidades sin estado:

```java
FormatoMoneda.formatear(valor);
```

No se debe abusar de `static` para guardar el usuario actual o los datos del negocio. Eso produce estado global difícil de probar.

---

## 5. Programación orientada a objetos con el proyecto

La POO organiza el programa alrededor de objetos que poseen estado y comportamiento.

### 5.1 Clase y objeto

Una clase es el molde:

```java
public class Pedido {
    private long id;
    private double total;
}
```

Un objeto es una instancia:

```java
Pedido pedido = new Pedido(...);
```

La clase define qué puede tener un pedido; el objeto representa un pedido concreto.

### 5.2 Encapsulamiento

Los atributos del proyecto normalmente son privados:

```java
private double precioBase;
```

El acceso ocurre mediante métodos:

```java
public double getPrecioBase() {
    return precioBase;
}

public void setPrecioBase(double precioBase) {
    this.precioBase = precioBase;
}
```

`private` evita que cualquier clase cambie el dato sin pasar por la API del objeto. El encapsulamiento no consiste solo en usar getters y setters: también significa proteger reglas.

Por ejemplo, `descontarStock` valida antes de modificar:

```java
public void descontarStock(int cantidad) {
    if (!hayStock(cantidad)) {
        throw new IllegalArgumentException(
            "La cantidad supera el stock disponible."
        );
    }
    stock -= cantidad;
}
```

### 5.3 Abstracción

Una clase abstracta representa lo común y deja lo específico a sus hijos:

```java
public abstract class Producto {
    public abstract String getDescripcionEntrega();
}
```

Un producto conoce precio, descuento, IVA y stock, pero no todos los productos se entregan igual.

### 5.4 Herencia

```text
Producto
   ↓
Libro
   ├── LibroFisico
   └── LibroDigital
```

`Libro` hereda los cálculos de `Producto`. `LibroFisico` y `LibroDigital` completan el comportamiento de entrega.

Otra jerarquía:

```text
Usuario
   ├── Administrador
   └── Cliente
       ├── ClienteRegular
       └── ClientePremium
```

### 5.5 Polimorfismo

```java
Cliente cliente = new ClientePremium(...);
double descuento = cliente.calcularDescuento(total);
```

La variable es `Cliente`, pero Java ejecuta el método de `ClientePremium`. El código que compra no necesita preguntar constantemente:

```java
if (cliente instanceof ClientePremium) { ... }
```

El objeto sabe calcular su propio descuento.

### 5.6 Composición

Un carrito está compuesto por líneas:

```text
Carrito o-- ItemCarrito
Pedido  o-- LineaPedido
```

Esto representa una relación “tiene”. Un carrito tiene varios productos seleccionados; un pedido tiene varias líneas confirmadas.

### 5.7 Asociación

`CompraService` no hereda de `PedidoRepository`; lo utiliza:

```java
private final PedidoRepository pedidos;
```

Es una asociación o dependencia, no una relación de herencia.

### 5.8 Interfaces

Una interfaz define un contrato:

```java
public interface LibroRepository {
    List<Libro> listar();
    Optional<Libro> buscarPorIsbn(String isbn);
    void guardar(Libro libro);
}
```

La interfaz dice qué se puede hacer, pero no cómo se guarda. La implementación JSON decide cómo hacerlo.

---

## 6. Principios SOLID aplicados

### S — Single Responsibility Principle

Una clase debe tener una razón principal para cambiar.

Ejemplos:

- `CompraService`: coordina compras.
- `ReciboService`: genera recibos.
- `FormatoMoneda`: formatea dinero.
- `PanelCarrito`: muestra el carrito.
- `CarritoJsonRepository`: persiste carritos.

Una mala clase sería una ventana que además valida stock, calcula descuentos, escribe JSON y genera recibos.

### O — Open/Closed Principle

El software debe estar abierto a extensión y cerrado a modificaciones innecesarias.

Ejemplo:

```java
public interface PoliticaDescuentoAdicional {
    double calcularDescuento(Cliente cliente,
                             double totalBruto,
                             int pedidosAnteriores);
}
```

Se puede crear:

```java
public class DescuentoPorTemporada
        implements PoliticaDescuentoAdicional { ... }
```

sin modificar `CompraService`.

### L — Liskov Substitution Principle

Un subtipo debe poder usarse donde se espera el tipo base.

```java
List<Libro> catalogo = new ArrayList<>();
catalogo.add(new LibroFisico(...));
catalogo.add(new LibroDigital(...));
```

El catálogo puede tratar ambos objetos como `Libro` porque ambos cumplen el contrato de la clase base.

### I — Interface Segregation Principle

Es mejor tener varias interfaces pequeñas que una interfaz gigante.

El proyecto separa:

```text
LibroRepository
UsuarioRepository
PedidoRepository
CarritoRepository
ResenaRepository
RegistroOperaciones
```

Un servicio de libros no necesita depender de métodos para reseñas o carritos.

### D — Dependency Inversion Principle

Las clases de alto nivel deben depender de abstracciones, no de implementaciones concretas.

`CompraService` depende de:

```java
private final LibroRepository libros;
```

No depende directamente de:

```java
new LibroJsonRepository(...)
```

La implementación concreta se crea en `Main` y se inyecta.

---

## 7. El arranque de la aplicación: `Main.java`

`Main` es la primera clase que se ejecuta. Su trabajo no es implementar compras; es conectar objetos.

Fragmento principal:

```java
SwingUtilities.invokeLater(() -> {
    LibroRepository libros =
        new LibroJsonRepository(Rutas.libros());

    UsuarioRepository usuarios =
        new UsuarioJsonRepository(Rutas.clientes());

    PedidoRepository pedidos =
        new PedidoJsonRepository(Rutas.pedidos());

    CarritoRepository carritos =
        new CarritoJsonRepository(Rutas.carritos());
});
```

### ¿Por qué `SwingUtilities.invokeLater`?

Swing tiene un hilo especial llamado Event Dispatch Thread. Las ventanas y eventos deben crearse en ese hilo para evitar comportamientos inesperados.

### ¿Qué significa la declaración?

```java
LibroRepository libros =
    new LibroJsonRepository(Rutas.libros());
```

Se lee así:

1. La variable se declara con el contrato `LibroRepository`.
2. El objeto real se crea con `LibroJsonRepository`.
3. La ruta se obtiene desde `Rutas`.
4. El servicio solo conocerá la interfaz.

Después se construyen los servicios:

```java
Sesion sesion = new Sesion();

AutenticacionService autenticacion =
    new AutenticacionService(usuarios, registro);

CatalogoService catalogo =
    new CatalogoService(libros, pedidos, registro);

CarritoService carrito =
    new CarritoService(libros, carritos, registro);

CompraService compras =
    new CompraService(
        libros, pedidos, carritos, registro,
        recibos, sesion,
        new DescuentoClienteFrecuente()
    );
```

Esto es una composición manual de dependencias. No hay un framework oculto: usted puede ver cómo se arma todo.

Finalmente:

```java
LoginFrame login = new LoginFrame();
new LoginController(
    login,
    autenticacion,
    sesion,
    () -> new ControladorPrincipal(...)
);
login.setVisible(true);
```

La ventana recibe su controlador y se muestra.

---

## 8. Modelo del dominio

### 8.1 `Producto`

`Producto` concentra datos y cálculos comunes:

```java
public abstract class Producto {
    private String codigo;
    private String nombre;
    private double precioBase;
    private int stock;
    private double porcentajeDescuento;
    private double impuestoIVA;
}
```

Precio sin IVA:

```java
public double calcularPrecioSinIva() {
    return precioBase *
           (1 - porcentajeDescuento / 100.0);
}
```

Precio final:

```java
public double calcularPrecioFinal() {
    return calcularPrecioSinIva() *
           (1 + impuestoIVA / 100.0);
}
```

Ejemplo:

```text
precioBase = 50.000
porcentajeDescuento = 0
IVA = 19
```

```text
precio sin IVA = 50.000
precio final = 50.000 × 1,19 = 59.500
```

Para una compra, el proyecto separa subtotal e impuesto:

```text
subtotal = 50.000
impuesto = 50.000 × 19 % = 9.500
```

### 8.2 `Libro`, `LibroFisico` y `LibroDigital`

`Libro` agrega información editorial:

- Autores.
- Año de publicación.
- Categoría.
- Editorial.
- Número de páginas.
- Formato.

`LibroFisico` y `LibroDigital` permiten variar la descripción de entrega.

### 8.3 `LibroFactory`

Una fábrica centraliza la construcción del subtipo correcto:

```java
Libro libro = LibroFactory.crear(
    FormatoLibro.FISICO,
    ...
);
```

El código que registra un libro no necesita escribir un `if` con `new LibroFisico` y `new LibroDigital` en cada lugar.

### 8.4 Usuarios y roles

`Usuario` conserva datos comunes:

- Nombre.
- Correo.
- Hash de contraseña.
- Rol.

`Cliente` agrega dirección y teléfono, e implementa `Descontable`.

`ClienteRegular` aplica 0 %.

`ClientePremium` aplica 10 %:

```java
return totalCompra * 10.0 / 100.0;
```

El administrador no puede comprar porque no es un `Cliente`. Esa regla se verifica en `CompraService.clienteActual()`.

### 8.5 `Carrito` y `ItemCarrito`

El carrito contiene líneas modificables. Por eso `ItemCarrito` tiene operaciones:

```java
item.aumentar();
item.disminuir();
```

El carrito calcula:

```text
subtotal general
impuestos totales
total bruto
```

### 8.6 `Pedido` y `LineaPedido`

Un pedido representa la compra confirmada. Debe conservar el precio y datos que existían al confirmar, aunque después cambie el catálogo.

Diferencia conceptual:

```text
ItemCarrito  = selección mutable
LineaPedido  = registro histórico confirmado
```

### 8.7 `Resena`

Una reseña relaciona:

```text
ISBN + correo del cliente + calificación + comentario + fecha
```

La entidad conserva datos; `ResenaService` aplica las reglas de quién puede reseñar.

---

## 9. Excepciones y validación

La excepción raíz es `TiendaException`. Sus subtipos permiten expresar el problema:

```text
ValidacionException
StockInsuficienteException
LibroDuplicadoException
LibroConVentasException
CredencialesInvalidasException
SesionNoIniciadaException
PersistenciaException
```

Ejemplo de validación en un servicio:

```java
if (cantidad <= 0) {
    throw new ValidacionException(
        "La cantidad debe ser positiva."
    );
}
```

Ejemplo de captura en el controlador:

```java
try {
    compras.finalizarCompra(carrito, metodo);
} catch (TiendaException e) {
    Mensajes.error(vista, e.getMessage());
}
```

La regla importante es:

```text
Service detecta y lanza.
Controller captura y presenta.
View muestra el mensaje.
```

`Validador` contiene verificaciones reutilizables como correo, contraseña, ISBN, precio, stock, IVA y descuento.

---

## 10. Persistencia y repositorios

### 10.1 El contrato

```java
public interface LibroRepository {
    List<Libro> listar();
    Optional<Libro> buscarPorIsbn(String isbn);
    void guardar(Libro libro);
    void eliminar(String isbn);
    boolean existe(String isbn);
}
```

Esta interfaz permite que el servicio diga “necesito guardar libros” sin saber si se usa JSON, SQL o memoria.

### 10.2 Implementación JSON

`LibroJsonRepository` implementa el contrato y usa `ArchivoJson`.

Los archivos están en:

```text
data/libros.json
data/clientes.json
data/pedidos.json
data/carritos.json
data/resenas.json
```

Los repositorios cargan los datos al construirse. Si están vacíos, crean semillas.

### 10.3 Por qué no se guarda la contraseña original

El registro usa:

```java
SeguridadHash.hash(contrasena)
```

El JSON guarda el hash, no `Admin123`. Al iniciar sesión:

1. Se recibe la contraseña escrita.
2. Se calcula su hash.
3. Se compara con el hash guardado.

### 10.4 Registro TXT

`RegistroOperacionesTxt` escribe operaciones como:

```text
fecha | nivel | correo | operación
```

Esto es independiente del JSON de entidades.

### 10.5 Limitación actual de Gson

El adaptador JSON está encapsulado en `ArchivoJson`. Esto permite sustituir la implementación por Gson o una base de datos sin cambiar los servicios ni el modelo.

---

## 11. Servicios: el corazón de la aplicación

### 11.1 `Sesion`

Conserva el usuario actual:

```java
private Usuario usuarioActual;
```

Métodos principales:

- `iniciar(usuario)`: guarda el usuario autenticado.
- `cerrar()`: elimina la sesión.
- `estaLogueado()`: indica si hay usuario.
- `exigirUsuario()`: devuelve el usuario o lanza excepción.

No es un singleton. `Main` crea una sesión y la inyecta.

### 11.2 `AutenticacionService`

`registrarCliente`:

1. Valida los campos.
2. Normaliza el correo.
3. Verifica que no exista.
4. Calcula el hash.
5. Crea Regular o Premium.
6. Guarda mediante `UsuarioRepository`.
7. Registra la operación.

`iniciarSesion`:

1. Busca el correo.
2. Calcula el hash de la contraseña recibida.
3. Compara hashes.
4. Devuelve el usuario.
5. Si falla, lanza `CredencialesInvalidasException`.

### 11.3 `CatalogoService`

Responsabilidades:

- Listar libros.
- Buscar por ISBN.
- Registrar.
- Actualizar.
- Eliminar.

Antes de registrar valida el libro y verifica duplicados. Antes de eliminar revisa los pedidos para impedir borrar un libro vendido.

### 11.4 `CarritoService`

Al agregar:

1. Valida cantidad positiva.
2. Busca el libro.
3. Calcula la cantidad nueva.
4. Compara con el stock.
5. Crea o aumenta el `ItemCarrito`.
6. Persiste el carrito.

La vista no modifica directamente el carrito; el controlador llama al servicio.

### 11.5 `CompraService`: lectura completa

La compra desde carrito empieza aquí:

```java
public Pedido finalizarCompra(
        Carrito carrito,
        MetodoPago metodo) {

    Cliente cliente = clienteActual();

    if (carrito == null || carrito.getItems().isEmpty()) {
        throw new ValidacionException(
            "El carrito está vacío."
        );
    }

    if (metodo == null) {
        throw new ValidacionException(
            "Seleccione un método de pago."
        );
    }

    return procesar(cliente, carrito, metodo);
}
```

La línea:

```java
Cliente cliente = clienteActual();
```

impide que un administrador compre.

Luego `procesar` reconstruye cada línea usando el precio actual del catálogo. Esto evita confiar ciegamente en precios antiguos del carrito.

Para cada item:

```java
Libro libro = libros.buscarPorIsbn(item.getIsbn())
    .orElseThrow(() -> new ValidacionException(
        "El libro ya no está disponible."
    ));
```

Después valida stock:

```java
if (item.getCantidad() > libro.getStock()) {
    throw new StockInsuficienteException(...);
}
```

Crea `LineaPedido`, suma subtotal e impuestos y calcula:

```java
double bruto = redondear(subtotal + impuestos);
```

El descuento combina dos políticas:

```java
double descuento = redondear(
    cliente.calcularDescuento(bruto)
    + politica.calcularDescuento(
        cliente,
        bruto,
        pedidos.listarPorCliente(
            cliente.getCorreo()
        ).size()
    )
);
```

Finalmente:

1. Descuenta stock.
2. Guarda el pedido.
3. Elimina el carrito persistido.
4. Vacía el objeto en memoria.
5. Registra la operación.
6. Genera el recibo.
7. Devuelve el pedido.

### 11.6 Descuento frecuente

La interfaz:

```java
public interface PoliticaDescuentoAdicional {
    double calcularDescuento(
        Cliente cliente,
        double totalBruto,
        int pedidosAnteriores
    );
}
```

La implementación aplica 5 % cuando `pedidosAnteriores >= 3`. Por eso el cuarto pedido recibe el descuento.

### 11.7 `ResenaService`

El método `registrar`:

1. Valida calificación de 1 a 5.
2. Revisa pedidos del cliente.
3. Busca el ISBN dentro de las líneas compradas.
4. Rechaza si nunca compró el libro.
5. Rechaza si ya existe reseña.
6. Crea y guarda la reseña.
7. Registra la operación.

---

## 12. Swing y la interfaz gráfica

### 12.1 La vista no es el negocio

`PanelTienda` contiene componentes:

- `JComboBox<Libro>`.
- Etiquetas.
- Campo de cantidad.
- Botones.

La vista pinta información:

```java
public void pintarLibro(Libro libro) {
    lblPrecio.setText(
        String.valueOf(libro.getPrecioBase())
    );
}
```

No decide si hay stock suficiente; eso lo hace `CarritoService`.

### 12.2 Eventos

Cada botón tiene un `actionCommand`:

```java
btnAgregar.setActionCommand("AGREGAR_CARRITO");
```

El controlador escucha:

```java
vista.getBtnAgregar().addActionListener(this);
```

Y responde:

```java
if (Comandos.AGREGAR_CARRITO.equals(
        evento.getActionCommand())) {
    // llamar al servicio
}
```

### 12.3 `LoginController`

El flujo real es:

```text
Usuario pulsa Iniciar sesión
        ↓
LoginFrame genera ActionEvent
        ↓
LoginController.actionPerformed
        ↓
AutenticacionService.iniciarSesion
        ↓
Sesion.iniciar
        ↓
ControladorPrincipal
        ↓
VentanaPrincipal
```

`LoginController` no compara contraseñas por sí mismo. Esa responsabilidad está en `AutenticacionService`.

### 12.4 `ControladorPrincipal`

Decide qué pestañas se muestran:

```text
Administrador:
  Gestión de Libros
  Historial de Ventas

Cliente:
  Tienda
  Carrito
  Mi Historial
  Mi Cuenta
  Reseñas
```

Para el administrador usa historial global. Para el cliente usa historial propio.

---

## 13. Flujo completo de una compra

### Inicio

1. Eclipse ejecuta `Main.main`.
2. Se crean repositorios.
3. Se crean servicios.
4. Se crea `Sesion`.
5. Se muestra `LoginFrame`.

### Inicio de sesión

1. El usuario escribe correo y contraseña.
2. `LoginFrame` produce un evento.
3. `LoginController` lo recibe.
4. `AutenticacionService` busca el usuario.
5. Compara hashes.
6. `Sesion` conserva el usuario.
7. `ControladorPrincipal` crea las pestañas del rol.

### Agregar al carrito

1. Se selecciona un libro.
2. Se escribe cantidad.
3. Se pulsa Agregar.
4. `TiendaController` lee los campos.
5. `CarritoService` valida stock.
6. Se persiste el carrito.
7. La vista muestra confirmación.

### Confirmar compra

1. `CarritoController` solicita resumen.
2. El usuario confirma.
3. `CompraService` verifica sesión como cliente.
4. Verifica carrito y método de pago.
5. Consulta nuevamente cada libro.
6. Verifica stock.
7. Calcula subtotal, impuestos y descuentos.
8. Descuenta inventario.
9. Guarda pedido.
10. Elimina carrito.
11. Genera recibo.
12. Actualiza historial.

### Reseñar

1. El cliente escribe ISBN.
2. Selecciona calificación.
3. Escribe comentario.
4. `ResenaController` llama al servicio.
5. `ResenaService` comprueba compra previa.
6. Guarda la reseña.

---

## 14. Cómo reconstruir el proyecto desde cero

Si tuviera que volver a construirlo, siga este orden.

### Paso 1: crear el proyecto

```text
TiendaVirtualLibros
└── src
    └── co.edu.uptc
```

Configure Java 17.

### Paso 2: modelar el dominio

Comience con:

```text
Producto
Libro
LibroFisico
LibroDigital
Usuario
Cliente
ClienteRegular
ClientePremium
Administrador
Carrito
Pedido
Resena
```

No empiece por Swing. Primero pruebe las entidades.

### Paso 3: crear enumeraciones e interfaces

```text
Categoria
FormatoLibro
Rol
MetodoPago
Descontable
PoliticaDescuentoAdicional
```

### Paso 4: crear excepciones

Defina la jerarquía de errores antes de implementar servicios.

### Paso 5: definir repositorios

Escriba primero interfaces:

```text
LibroRepository
UsuarioRepository
PedidoRepository
CarritoRepository
ResenaRepository
RegistroOperaciones
```

### Paso 6: implementar persistencia

Construya primero `ArchivoJson`; luego cada repositorio concreto. Pruebe guardar y cargar antes de crear la interfaz.

### Paso 7: implementar utilidades

```text
Validador
SeguridadHash
FechaUtil
FormatoMoneda
Rutas
```

### Paso 8: implementar servicios

Orden recomendado:

```text
Sesion
AutenticacionService
CatalogoService
CarritoService
ReciboService
CompraService
ResenaService
```

### Paso 9: probar servicios sin Swing

Cree pruebas que construyan repositorios temporales y verifiquen resultados.

### Paso 10: crear vistas

Construya formularios sin reglas de negocio. Exponga getters y métodos `pintar`.

### Paso 11: crear controladores

Conecte botones con servicios. Capture excepciones y muéstrelas mediante `Mensajes`.

### Paso 12: crear `Main`

Solo al final conecte todas las implementaciones.

---

## 15. Cómo leer una clase en Eclipse

Cuando abra una clase, siga este orden:

### 1. Paquete e imports

Los imports muestran dependencias. Pregunte:

- ¿Importa Swing?
- ¿Importa servicios?
- ¿Importa repositorios?
- ¿Importa modelos?

### 2. Declaración de clase

Observe:

```java
public class X extends Y implements Z
```

- `extends`: herencia.
- `implements`: contrato.

### 3. Atributos

Indican qué estado conserva la clase.

### 4. Constructor

Indica qué necesita para existir.

### 5. Métodos públicos

Son la API de la clase.

### 6. Métodos privados

Son detalles internos de implementación.

### 7. Llamadas externas

Use `Ctrl + clic` sobre un método para ir a su definición.

Atajos útiles:

```text
Ctrl + clic        Ir a declaración
F3                Abrir declaración
Ctrl + Shift + R  Buscar recurso
Ctrl + H          Búsqueda en proyecto
Ctrl + Shift + F  Formatear código
Ctrl + S          Guardar
Ctrl + B          Compilar
F11               Depurar
F5                Ejecutar línea durante depuración
F6                Siguiente línea
F7                Salir del método
F8                Continuar
```

---

## 16. Depuración práctica

Para entender una compra:

1. Abra `CompraService`.
2. Coloque un breakpoint en `finalizarCompra`.
3. Ejecute el programa en **Debug As → Java Application**.
4. Inicie sesión como cliente.
5. Agregue un producto.
6. Confirme la compra.
7. Eclipse se detendrá en el breakpoint.

Observe en la ventana **Variables**:

```text
carrito
cliente
metodo
```

Presione `F5` para entrar a un método, `F6` para avanzar y `F8` para continuar.

Breakpoints recomendados:

```text
LoginController.iniciar
AutenticacionService.iniciarSesion
CarritoService.agregar
CompraService.finalizarCompra
CompraService.procesar
ResenaService.registrar
LibroJsonRepository.guardar
```

La depuración enseña más que leer pasivamente porque permite observar valores reales.

---

## 17. Pruebas y ejercicios progresivos

### Ejercicio 1: cálculo de producto

Calcule manualmente:

```text
Precio: 80.000
Descuento producto: 10 %
IVA: 19 %
Cantidad: 3
```

Resultado esperado:

```text
Precio sin IVA: 72.000
Subtotal: 216.000
Impuesto: 41.040
```

### Ejercicio 2: stock

¿Qué debe ocurrir si el stock es 5 y se solicitan 6 unidades?

Respuesta esperada: no se modifica el stock y se lanza una excepción.

### Ejercicio 3: cliente Premium

Para un total bruto de 119.000:

```text
Descuento Premium = 11.900
Total = 107.100
```

### Ejercicio 4: cliente frecuente

Para un pedido bruto de 47.250 y 3 pedidos anteriores:

```text
Descuento frecuente = 2.362,50
```

El código redondea a 2.363.

### Ejercicio 5: reseña

Pruebe tres casos:

1. Reseñar sin comprar: debe fallar.
2. Reseñar después de comprar: debe funcionar.
3. Reseñar dos veces: la segunda debe fallar.

### Ejercicio 6: nueva política

Implemente:

```java
public class DescuentoEstudiante
        implements PoliticaDescuentoAdicional {
    @Override
    public double calcularDescuento(
            Cliente cliente,
            double totalBruto,
            int pedidosAnteriores) {
        return totalBruto * 0.03;
    }
}
```

Después inyéctela en `Main` y observe que `CompraService` no tuvo que cambiar.

### Ejercicio 7: nuevo repositorio

Diseñe `LibroMemoriaRepository` que guarde libros en un `List<Libro>` sin escribir archivos. El servicio debería seguir funcionando porque depende de `LibroRepository`.

### Ejercicio 8: nueva categoría

Agregue una constante a `Categoria` y úsela desde el formulario. Observe qué clases necesitan cambiar y cuáles no.

---

## 18. Errores comunes y cómo corregirlos

### Error: “Debe iniciar sesión como cliente para comprar” con administrador

El administrador no puede comprar. Si aparece al abrir su ventana, el historial debe usar `historialGlobal()` y no `historialCliente()`.

### Error: `ClassNotFoundException`

Significa que la clase no está compilada o el proyecto no está configurado como Java Project.

Revise:

- `src` como source folder.
- JDK instalado.
- `Project → Clean`.
- `Run As → Java Application`.

### Error: los cambios no aparecen

Ejecute:

```text
F5
Ctrl + B
Project → Clean
```

Asegúrese de estar ejecutando el proyecto nuevo y no `gp1_TiendaVirtual`.

### Error: datos antiguos

Los archivos persistidos están en `data`. Para iniciar una prueba limpia, cierre el programa y deje los JSON como:

```json
[]
```

### Error: `NullPointerException`

Pregunte qué objeto está vacío. Use el depurador y revise:

- ¿Se construyó la dependencia?
- ¿El repositorio encontró el objeto?
- ¿La sesión está iniciada?
- ¿El componente Swing fue inicializado?

### Error: precio incorrecto

Separe siempre:

```text
precio sin IVA
subtotal
impuesto
bruto
 descuentos
 total
```

No mezcle el precio de catálogo con el total del pedido.

---

## 19. Cómo mejorar el proyecto después

Una vez comprenda la versión actual, puede evolucionar hacia:

1. Base de datos JDBC.
2. `BigDecimal` para dinero.
3. Pruebas JUnit.
4. DTOs para no exponer entidades en la vista.
5. Transacciones de compra.
6. Control de concurrencia del stock.
7. Autorización más explícita para acciones administrativas.
8. Servicio de informes.
9. Exportación de ventas a CSV.
10. Migración de Swing a JavaFX o web.
11. Inyección de dependencias con Spring, si estudia ese framework.
12. Logs profesionales.
13. Configuración externa de rutas.

Una mejora especialmente importante sería reemplazar `double` por `BigDecimal` para cálculos monetarios:

```java
BigDecimal precio = new BigDecimal("50000.00");
```

No la introduzca antes de comprender la versión actual; primero domine la separación de responsabilidades.

---

## 20. Lista de dominio personal

Considere que domina una parte cuando puede responder sin mirar el código:

### Java básico

- ¿Qué diferencia hay entre una variable y un objeto?
- ¿Qué devuelve un método?
- ¿Qué hace un `for`?
- ¿Qué diferencia hay entre `List` y `ArrayList`?
- ¿Qué es una excepción?

### POO

- ¿Qué es encapsulamiento?
- ¿Qué es herencia?
- ¿Qué es abstracción?
- ¿Qué es polimorfismo?
- ¿Qué diferencia hay entre asociación y composición?

### SOLID

- ¿Qué responsabilidad tiene `CompraService`?
- ¿Por qué usa interfaces de repositorio?
- ¿Cómo agregaría una política nueva sin tocar `CompraService`?
- ¿Por qué la vista no escribe JSON?

### Arquitectura

- ¿Qué ocurre desde que se pulsa un botón?
- ¿Dónde se valida el stock?
- ¿Dónde se guarda un pedido?
- ¿Quién muestra un error?
- ¿Por qué `Main` crea las implementaciones?

### Reconstrucción

- ¿Puede dibujar la jerarquía de clases?
- ¿Puede crear un repositorio falso en memoria?
- ¿Puede implementar un servicio sin Swing?
- ¿Puede crear un panel sin meter reglas de negocio?
- ¿Puede probar un caso de compra sin abrir la ventana?

---

## 21. Proyecto final personal

Para comprobar que ya puede trabajar sin ayuda, cree una versión reducida en otra carpeta:

### Requisitos mínimos

- `Producto` y `Libro`.
- `ClienteRegular` y `ClientePremium`.
- `Carrito`.
- `Pedido`.
- `LibroRepository`.
- Repositorio en memoria.
- `CompraService`.
- Una interfaz Swing sencilla.
- Una prueba de compra.

### Después agregue

- Persistencia JSON.
- Administrador.
- Historial.
- Reseñas.
- Descuento frecuente.
- Controladores separados.

No copie el proyecto original. Consulte únicamente el manual, diseñe primero en papel y escriba cada clase explicando su responsabilidad.

---

## 22. Resumen final para memorizar

La aplicación se entiende con estas frases:

```text
El modelo representa el negocio.
Las excepciones expresan errores.
Los repositorios definen cómo se almacena.
Los servicios aplican las reglas.
Los controladores traducen eventos.
Las vistas muestran información.
Main conecta las dependencias.
```

Y el flujo principal es:

```text
Main
  ↓
LoginFrame + LoginController
  ↓
AutenticacionService + Sesion
  ↓
ControladorPrincipal
  ↓
Paneles y controladores
  ↓
Servicios
  ↓
Entidades del modelo
  ↓
Repositorios
  ↓
Archivos JSON/TXT
```

Si puede explicar ese flujo, justificar la responsabilidad de cada paquete, seguir una compra con el depurador y crear una nueva implementación de una interfaz, ya no está copiando el proyecto: está entendiendo su diseño.

---

## 23. Plan de estudio de 30 días

### Días 1–3: Java básico

Variables, métodos, condicionales, ciclos, colecciones y excepciones.

### Días 4–7: POO

Clases, objetos, constructores, encapsulamiento, herencia, abstractas e interfaces.

### Días 8–10: modelo

Reescriba `Producto`, `Libro`, usuarios, carrito y pedido en una carpeta de práctica.

### Días 11–14: persistencia

Implemente primero un repositorio en memoria y después un archivo.

### Días 15–18: servicios

Implemente autenticación, catálogo, carrito y compra sin Swing.

### Días 19–21: SOLID

Cambie una implementación concreta por una interfaz y agregue una política nueva.

### Días 22–25: Swing

Cree ventanas, formularios, tablas, botones y eventos.

### Días 26–28: controladores

Conecte vistas con servicios sin mover reglas al formulario.

### Días 29–30: reconstrucción

Construya una versión propia y escriba pruebas para los casos principales.

---

## 24. Conclusión

Este proyecto no debe estudiarse como una colección de archivos aislados. Es una conversación entre objetos:

- El usuario interactúa con la vista.
- El controlador interpreta la intención.
- El servicio aplica la regla.
- El modelo representa el estado.
- El repositorio conserva la información.
- Las excepciones comunican errores.
- `Main` conecta las piezas.

Cuando una nueva función sea necesaria, no empiece escribiendo botones. Empiece preguntando:

```text
¿Qué regla de negocio existe?
¿Qué objeto debe conocerla?
¿Qué contrato necesito?
¿Qué servicio coordina el caso de uso?
Qué vista necesita el usuario?
Qué controlador traduce el evento?
Cómo voy a probarlo?
```

Ese proceso de pensamiento es más importante que memorizar cualquier clase concreta y le permitirá construir aplicaciones Java nuevas sin depender de un agente externo.
