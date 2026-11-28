# Contrato de clases — Tienda Virtual de Libros

**Actualizado:** 19/11/2026  
**Alcance:** clases que existen hoy en `src/main/java`. Si una clase no aparece aquí, no forma parte del contrato actual.

---

## 1. Dependencias entre capas

```text
view  -> service
controller -> service, persistencia, model
persistencia -> model
service -> model
model -> (nada)
config -> persistencia
```

Reglas:

- `view` no debe importar `controller` concretos.
- `controller` no debe importar `javax.swing`.
- `persistencia` no debe importar `view`.
- `model` no debe importar Swing ni repositorios.

---

## 2. Persistencia

### `IRepositorioConsulta<T>`

```java
List<T> listar();
List<T> listarUltimos(int limite);
List<T> listarPagina(int inicio, int cantidad);
int contar();
```

`listarUltimos` devuelve los últimos N registros, del más reciente al más antiguo.
El orden de referencia es el de inserción para Clientes y Libros, y fecha/id descendente para Compras.

### `IRepositorio<T> extends IRepositorioConsulta<T>`

```java
List<T> listar();
void agregar(T elemento);
boolean eliminar(T elemento);
void cargar();
void guardar();

void insertar(T elemento);
void actualizar(T elemento);
boolean eliminarPersistente(T elemento);
```

Los métodos `insertar`, `actualizar` y `eliminarPersistente` tienen implementación por defecto de compatibilidad.
La escritura incremental real se implementa por repositorio en F09-F11.

Los métodos `listar()`, `agregar()`, `eliminar()`, `cargar()` y `guardar()` están **deprecados**.
No se deben usar en código nuevo:
- `listar()` → `listarUltimos()` o `listarPagina()`
- `agregar()` → `insertar()`
- `eliminar()` → `eliminarPersistente()`
- `cargar()` → ya no se necesita; las consultas leen el almacenamiento
- `guardar()` → `insertar()`, `actualizar()` o `eliminarPersistente()`

No agregar métodos abstractos sin actualizar **todas** las implementaciones el mismo día.

### Implementaciones

| Módulo | Archivo plano | JSON | Base de datos |
|---|---|---|---|
| Clientes | `RepositorioClientesArchivoPlano` | `RepositorioClientesJson` | `RepositorioClientesBD` |
| Libros | `RepositorioLibrosArchivoPlano` | `RepositorioLibrosJson` | `RepositorioLibrosBD` |
| Compras | `RepositorioComprasArchivoPlano` | `RepositorioComprasJson` | `RepositorioComprasBD` |

### `GestorCarritoJSON`

```java
GestorCarritoJSON();
GestorCarritoJSON(String rutaCarpeta);
Map<String, Integer> cargar(String correo);
void guardar(Map<String, Integer> items, String correo);
void eliminar(String correo);
```

Guarda el carrito en `datos/carritos/<correo>.json`.

### `RegistroOperacionesTXT`

```java
RegistroOperacionesTXT();
RegistroOperacionesTXT(String ruta);
void registrar(String descripcion);
```

Escribe en `datos/operaciones.txt` con fecha, hora y descripción.

### `PersistenciaException`

Excepción de la capa de persistencia. Se lanza cuando falla la lectura, escritura o validación de datos guardados.

---

## 3. Configuración

### `ProjectConfig`

```java
IRepositorio<Cliente> clientes();
IRepositorio<Cliente> clientesArchivoPlano();
IRepositorio<Cliente> clientesJson();
IRepositorio<Cliente> clientesBD();

IRepositorio<Libro> libros();
IRepositorio<Libro> librosArchivoPlano();
IRepositorio<Libro> librosJson();
IRepositorio<Libro> librosBD();

IRepositorio<Compra> compras();
IRepositorio<Compra> comprasArchivoPlano();
IRepositorio<Compra> comprasJson();
IRepositorio<Compra> comprasBD();
```

Es el único lugar donde se cambia la persistencia de un módulo.

Defaults actuales:

- Clientes: archivo plano.
- Libros: JSON.
- Compras: base de datos.

---

## 4. Servicios

### `IServicioClientes`

```java
boolean registrar(Cliente cliente);
boolean registrarCliente(String tipo, String nombreCompleto, String correo,
        String direccion, String telefono, String contrasena);
List<Cliente> listar();
Cliente buscar(String correo);
boolean actualizarDatos(String correo, String nombreCompleto,
        String direccion, String telefono);
boolean eliminar(String correo);
Cliente iniciarSesion(String correo, String contrasena);
int cantidad();
```

### `IServicioLibros`

```java
boolean registrar(Libro libro);
List<Libro> listar();
Libro buscar(String isbn);
List<Libro> buscarPorCriterio(String criterio);
boolean actualizar(String isbn, Libro datos);
boolean eliminar(String isbn);
boolean validarDisponibilidad(String isbn, int cantidadSolicitada);
boolean actualizarInventario(String isbn, int cantidadVendida);
```

### `IServicioCompras`

```java
boolean registrarCompra(Compra compra);
ResultadoCompra finalizarCompra(Cliente cliente, CarritoCompras carrito,
        String metodoPago);
List<Compra> listarCompras();
List<Compra> listarPorCliente(String correo);
int cantidad();
```

### `ResultadoCompra`

```java
ResultadoCompra(boolean exitoso, String mensaje, Compra compra);
static ResultadoCompra exito(String mensaje, Compra compra);
static ResultadoCompra error(String mensaje);
boolean isExitoso();
String getMensaje();
Compra getCompra();
```

### `IConsultaVentas`

```java
boolean tieneVentas(String isbn);
```

Sirve para impedir que se elimine un libro que ya tiene ventas asociadas.

### `IServicioCarrito`

```java
CarritoCompras cargar(String correo);
void guardar(CarritoCompras carrito, String correo);
void vaciar(String correo);
```

---

## 5. Controladores

### `ControladorCliente`

```java
ControladorCliente(IRepositorio<Cliente> repositorio);
ControladorCliente(IRepositorio<Cliente> repositorio,
        ControladorOperaciones operaciones);
```

Implementa `IServicioClientes`.

### `ControladorLibro`

```java
ControladorLibro(IRepositorio<Libro> repositorio);
ControladorLibro(IRepositorio<Libro> repositorio,
        ControladorOperaciones operaciones);
```

Implementa `IServicioLibros`.

### `ControladorCompras`

```java
ControladorCompras(IRepositorio<Compra> repositorio);
ControladorCompras(IRepositorio<Compra> repositorio,
        IServicioLibros servicioLibros);
ControladorCompras(IRepositorio<Compra> repositorio,
        IServicioLibros servicioLibros,
        ControladorOperaciones operaciones);
```

Implementa `IServicioCompras`. La lógica de compra está en `finalizarCompra`.

### `ControladorOperaciones`

```java
ControladorOperaciones(RegistroOperacionesTXT registro);
void registrar(String descripcion);
```

### `ControladorCarrito`

```java
ControladorCarrito(IServicioLibros libros, GestorCarritoJSON gestor);
CarritoCompras cargar(String correo);
void guardar(CarritoCompras carrito, String correo);
void vaciar(String correo);
```

### `ConsultaVentas`

```java
ConsultaVentas(IRepositorio<Compra> repositorio);
boolean tieneVentas(String isbn);
```

---

## 6. Modelo

```text
Cliente (abstracta)
  ClienteRegular
  ClientePremium
Libro
FormatoLibro (FISICO, DIGITAL, AUDIO_LIBRO)
CarritoCompras
ItemCompra
Compra
MetodoPago
```

`Compra` contiene `id`, `metodoPago`, `fecha`, `correoCliente`, `total` e `items`.

`ItemCompra` contiene `libro`, `cantidad`, `subtotal` e `impuestos`.

`CarritoCompras` contiene un `Map<Libro, Integer>`.

---

## 7. Vista y eventos

### `Eventos` (abstracta)

```java
protected Eventos(JFrame ventana);
protected void mostrarInfo(String mensaje);
protected void mostrarError(String mensaje);
protected boolean confirmar(String mensaje);
protected void volver(JFrame padre);
```

### Manejadores existentes

- `EventosCompras`
- `EventosHistorialCompras`
- `EventosRecibo`

Pendientes:

- `EventosLogin`
- `EventosRegistroCliente`
- `EventosClientes`
- `EventosLibros`
- `EventosMiCuenta`

### Ventanas principales

- `Main`
- `VentanaLogin`
- `VentanaRegistroCliente`
- `VentanaMiCuenta`
- `VentanaClientes`
- `VentanaLibros`
- `VentanaCompras`
- `VentanaHistorialCompras`
- `VentanaRecibo`

`VentanaCompras` ya no contiene `finalizarCompra` ni la lógica del carrito. Delega en `EventosCompras`.

---

## 8. Reglas para cambiar el contrato

1. Si se agrega un método a una interfaz, todas sus implementaciones se actualizan en el mismo commit.
2. No agregar métodos “por si acaso” a `IRepositorio`.
3. No poner lógica de negocio en `Eventos` ni en las ventanas.
4. No cambiar `ProjectConfig` desde otro integrante sin avisar.
5. Antes de commitear: `mvn clean compile`.
