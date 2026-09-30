# Guía del equipo — Grupo 5 (Tienda Virtual de Libros)

Documento de apoyo para los integrantes del grupo. La idea es que cualquiera pueda
entender **cómo está construido el proyecto** y **cómo agregar su parte** sin
miedo. Léelo completo antes de escribir código.

---

## 1. ¿Qué estamos construyendo?

Una aplicación de escritorio en **Java (Swing)** para una **tienda virtual de libros**.
El sistema cubre 20 requisitos funcionales (RF), que ya están dibujados en
`prototipos/`:

| RF | Requisito | Módulo |
|----|-----------|--------|
| RF01 | Registrar libro | Libros |
| RF02 | Actualizar libro | Libros |
| RF03 | Eliminar libro | Libros |
| RF04 | Listar libros | Libros |
| RF05 | Registrar cliente | Clientes |
| RF06 | Iniciar sesión | Clientes |
| RF07 | Actualizar datos del cliente | Clientes |
| RF08 | Consultar historial de compras | Compras |
| RF09 | Buscar libro | Libros |
| RF10 | Agregar libro al carrito | Compras |
| RF11 | Modificar cantidad del carrito | Compras |
| RF12 | Eliminar libro del carrito | Compras |
| RF13 | Calcular compra | Compras |
| RF14 | Aplicar descuento Premium | Clientes |
| RF15 | Validar sesión iniciada | Clientes |
| RF16 | Finalizar compra | Compras |
| RF17 | Seleccionar método de pago | Compras |
| RF18 | Validar disponibilidad | Libros |
| RF19 | Actualizar inventario | Libros |
| RF20 | Generar recibo | Compras |

---

## 2. Arquitectura en capas

El código está separado en **tres capas**. Esa separación es clave:

```
src/
  negocio/       -> datos y lógica (reglas del dominio)
  gui/           -> ventanas y eventos (interfaz)
  persistencia/  -> guardar/cargar en archivos (más adelante)
```

**Reglas de dependencia (NO romper):**

- `gui` **sí** puede usar `negocio`.
- `negocio` **NO** debe usar `gui`.
- `persistencia` **NO** debe usar `gui`.

En palabras simples: **la ventana nunca piensa; el controlador piensa.** La ventana
solo captura los datos y llama al controlador.

---

## 3. El molde: cómo se hace un CRUD (¡esto es lo más importante!)

Cada módulo sigue **exactamente el mismo patrón**. Usamos el módulo **Clientes**
(ya terminado) como ejemplo. Son 4 piezas:

1. **El dato** → una clase en `negocio` (ej. `Cliente`).
2. **El controlador** → una clase en `negocio` que administra la lista y las
   operaciones (ej. `ControladorCliente`).
3. **Las ventanas** → clases en `gui` que usan el controlador (ej.
   `VentanaRegistroCliente`).
4. **El arranque** → `gui/Main` crea el controlador y abre la primera ventana.

### Paso 1 — La clase de datos (`negocio/Cliente.java`)

Guarda los atributos (nombreCompleto, correo, direccion, telefono...), con
**getters y setters**. Además implementa `equals` y `hashCode` para comparar
objetos (por ejemplo, dos libros son iguales si tienen el mismo `isbn`).

### Paso 2 — El controlador (`negocio/ControladorCliente.java`)

Maneja **una lista en memoria** y ofrece las operaciones CRUD:

```java
public class ControladorCliente {
    private List<Cliente> clientes;

    public ControladorCliente() { this.clientes = new ArrayList<Cliente>(); }

    public boolean registrar(Cliente cliente) { ... }   // CREAR
    public List<Cliente> listar() { ... }               // LISTAR
    public Cliente buscar(String correo) { ... }        // BUSCAR
    public boolean actualizar(String correo, Cliente d) { ... } // ACTUALIZAR
    public boolean eliminar(String correo) { ... }      // ELIMINAR
}
```

Fíjate que **el controlador no sabe nada de ventanas** (`no importa javax.swing`).
Solo recibe y devuelve datos.

### Paso 3 — Las ventanas (`gui/…`)

Ejemplo real: `VentanaRegistroCliente`.

1. Tiene los **campos** (`JTextField`, `JComboBox`...).
2. Recibe el **controlador por el constructor**.
3. En el botón, **lee los datos y llama al controlador**:

```java
private void accionRegistrarse() {
    String nombre = txtNombre.getText().trim();
    ...
    Cliente cliente = new ClientePremium(nombre, correo, contrasena);
    if (controlador.registrar(cliente)) {
        JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
    } else {
        JOptionPane.showMessageDialog(this, "Ya existe un cliente con ese correo.");
    }
}
```

### Paso 4 — El arranque (`gui/Main.java`)

```java
ControladorCliente controlador = new ControladorCliente();
new VentanaLogin(controlador).setVisible(true);
```

> **Resumen del molde:** *dato* + *controlador* + *ventana* + *arranque*.
> Para tu módulo, copia este esquema y cambia `Cliente` por `Libro` o `Compra`.

---

## 4. Navegación entre ventanas (patrón padre/hijo)

Para movernos entre ventanas sin abrir mil ventanas usamos **padre → hijo**:

- Desde una ventana "padre" (ej. Login) abrimos la "hija" (ej. Registro) y le
  pasamos la referencia del padre por el constructor.
- La hija tiene un botón **"Atrás"** que hace:

```java
dispose();                 // cierra la ventana hija
padre.setVisible(true);    // vuelve a mostrar la ventana padre
```

Así siempre se puede regresar y no se duplican ventanas.

---

## 5. Compilar y ejecutar

Desde la carpeta `grupo5_tienda_virtual`:

```bat
javac -encoding UTF-8 -d bin src\negocio\*.java src\gui\*.java
java -cp bin gui.Main
```

Credenciales de prueba: **`juan@uptc.edu.co` / `1234`**.

---

## 6. Cómo trabajar con Git (reglas del curso)

1. Trabajar **siempre** en la rama `feature/grupo5`. **NUNCA** hacer push a `main`
   (eso vale **nota 0**).
2. Hacer **commits pequeños**: uno por unidad lógica (una ventana, una operación...).
   Mensajes claros, en español.
3. **Cada integrante commitea con su propia cuenta** (el profe califica por autoría).
   Antes de empezar, configura tu identidad:

   ```bat
   git config user.name "Tu nombre"
   git config user.email "tu-correo-de-github@users.noreply.github.com"
   ```

4. Flujo normal:

   ```bat
   git checkout feature/grupo5
   git pull origin feature/grupo5
   git add <archivos>
   git commit -m "Mensaje corto y claro"
   git push origin feature/grupo5
   ```

---

## 7. Reparto de tareas y checklist

### Juan Pablo Barrero — Módulo Clientes (RF05, RF06, RF07, RF14, RF15)

- [x] `Cliente`, `ClienteRegular`, `ClientePremium`
- [x] `ControladorCliente` (registrar, listar, buscar, actualizar, eliminar,
      iniciarSesion)
- [x] Ventana Iniciar Sesión (01)
- [x] Ventana Registro de Cliente (02)
- [x] Ventana Mi Cuenta (09)
- [ ] Ventana de gestión: **listar y eliminar** clientes
- [ ] Mensaje de error de sesión (11)

### Camilo — Módulo Libros (RF01, RF02, RF03, RF04, RF09, RF18, RF19)

- [ ] Modelo `Libro` y `FormatoLibro` (ya existen, revísalos)
- [ ] `ControladorLibro` (registrar, listar, buscar, actualizar, eliminar)
- [ ] Ventana Catálogo de Libros (03) → RF04 listar
- [ ] Ventana Buscar Libro (04) → RF09
- [ ] Ventana Gestión de Libros (10) → RF01, RF02, RF03
- [ ] Validar disponibilidad / stock (12) → RF18, RF19

### Liz Rojas — Módulo Compras (RF08, RF10, RF11, RF12, RF13, RF16, RF17, RF20)

- [ ] Revisar `CarritoCompras`, `ItemCompra`, `Compra`, `MetodoPago` (ya existen)
- [ ] Ventana Carrito de Compras (05) → RF10, RF11, RF12
- [ ] Ventana Finalizar Compra (06) → RF13, RF16, RF17
- [ ] Ventana Recibo (07) → RF20
- [ ] Ventana Historial de Compras (08) → RF08

> **Consejo:** empiecen por **listar** (una tabla simple) y luego agreguen
> crear/actualizar/eliminar. Copien el patrón de `ControladorCliente` y sus
> ventanas.

---

## 8. Estado actual del proyecto

**Listo:**

- Capa `negocio` completa: `Libro`, `FormatoLibro`, `Cliente` (+ Regular/Premium),
  `CarritoCompras`, `ItemCompra`, `Compra`, `MetodoPago`, `ControladorCliente`.
- Ventanas de **Clientes**: Login, Registro, Mi Cuenta, con navegación "Atrás".
- `Main` que abre la aplicación.
- Diagramas y prototipos en `diseno/` y `prototipos/`.

**Pendiente:**

- Ventanas de **Libros** (Camilo) y **Compras** (Liz).
- Ventana de gestión de clientes (listar/eliminar).
- Capa de **persistencia** (guardar en archivos) — al final.

---

## 9. Reglas de oro

1. **La ventana no piensa:** lógica en el controlador, datos en el modelo.
2. **Un commit por avance**, con tu propia cuenta.
3. **Nunca `main`**, siempre `feature/grupo5`.
4. **Copiar el molde** de Clientes para los otros módulos.
5. **Ante dudas, preguntar al grupo** antes de inventar arquitectura nueva.
