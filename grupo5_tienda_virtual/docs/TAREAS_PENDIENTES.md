# Tareas pendientes por integrante

Lista de correcciones detectadas en la revisión del código. Siguen el criterio de
`GUIA_EQUIPO.md` y `CONTRATO_CLASES.md`. Se agrupan por responsable.

> Regla: un commit por arreglo, siempre en `feature/grupo5`, nunca en `main`.

---

## Cristhian Camilo Peña — Módulo Libros

1. **[ALTO] `VentanaLibros.java:44`** — la ventana crea su propio controlador
   (`new ControladorLibro()`). Debe **recibirlo por el constructor** (como
   `VentanaRegistroCliente`), y el controlador se crea **solo una vez en `Main`**.
   Además la ventana **no se abre desde ningún punto** del flujo: hay que
   abrirla (p. ej. desde Login o Mi Cuenta) pasándole el mismo controlador.
   *Motivo:* hoy el módulo es inalcanzable y, si se integra, tendría un catálogo
   distinto al de Compras.
2. **[ALTO] `ControladorLibro.java:24` y `VentanaLibros.java:212`** — validar
   antes de registrar: **ISBN y título no vacíos** y **precio > 0**. Hoy se
   pueden crear libros con ISBN vacío y precio 0.
3. **[MEDIO] `ControladorLibro.java:32`** — `listar()` debe devolver una **copia**
   (`new ArrayList<Libro>(libros)`), como `ControladorCliente.listar()`.
4. **[MEDIO] `VentanaLibros.java:36`** — agregar `padre` al constructor y un botón
   **"Atrás"** (`dispose(); padre.setVisible(true);`), como las otras ventanas.
5. **[BAJO] `ControladorLibro.java:12,17,32,55`** — usar genéricos:
   `List<Libro>`, `new ArrayList<Libro>()`, y quitar los casts. Igual en
   `VentanaLibros.java:29` (`JComboBox<FormatoLibro>`), `:206` y `:230`.
   *Esto elimina el warning "unchecked or unsafe operations".*
6. **[BAJO]** `VentanaLibros.java:24` agregar `serialVersionUID`;
   `:37` quitar "Módulo Camilo" del título; `:97,282` sacar el `"19"` a constante;
   `:165` dejar el ISBN no editable al actualizar.

---

## Liz Alejandra Rojas — Módulo Compras

1. **[ALTO] `VentanaCompras.java:176` y `:270`** — aplicar el **descuento Premium
   (RF14)**: usar `carrito.calcularTotal(cliente.calcularDescuento())` para el
   total mostrado y para la compra. Hoy un cliente Premium paga el 100%.
2. **[MEDIO] `VentanaMiCuenta.java:181`** — no poblar el `JComboBox` de
   `VentanaCompras` desde afuera. `VentanaCompras` ya recibe el
   `controladorLibro`: debe llenar su propio combo en el constructor con
   `controladorLibro.listar()`.
3. **[MEDIO] `VentanaCompras.java:98` y `:121`** — cuando la cantidad no es
   numérica o es ≤ 0, mostrar un `JOptionPane` (hoy se ignora en silencio).
4. **[MEDIO] `CarritoCompras.java`** — la validación de stock no debe fallar en
   silencio: que la GUI valide y **avise** antes de agregar. Y decidir con el
   equipo la sobrecarga `calcularTotal(double)`: documentarla en
   `CONTRATO_CLASES.md` o eliminarla.
5. **[BAJO]** formatear los totales (`String.format("$%,.2f", total)`),
   corregir el comentario `MODIFCAR` (`CarritoCompras.java:40`) y las
   indentaciones con tabulaciones.

---

## Nota
- La regresión de `VentanaLogin` (acentos literales y faltaba ocultar el login
  al entrar) fue **corregida por Juan Pablo**. Evitar deshacerla al volver a
  editar ese archivo.
