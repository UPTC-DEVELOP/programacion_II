# TIENDALIBRERIA — Documentación Técnica Breve

## 1. Descripción del proyecto

**TIENDALIBRERIA** es una aplicación de escritorio desarrollada en **Java** para administrar un catálogo de libros mediante una interfaz gráfica construida con **Java Swing**. El sistema implementa las operaciones principales de un **CRUD**: crear, consultar, actualizar y eliminar libros.

El proyecto aplica **Programación Orientada a Objetos (POO)** y separa la interfaz gráfica, la lógica de negocio, el modelo de datos y la persistencia, facilitando la organización y mantenimiento del código.

| Tecnología / concepto | Aplicación en el proyecto |
|---|---|
| **Java** | Lenguaje principal. |
| **Swing / AWT** | Ventanas, formularios, tablas, botones y eventos. |
| **POO** | Clases, objetos, encapsulamiento, herencia y polimorfismo. |
| **ArrayList / List** | Almacenamiento dinámico de libros. |
| **Stream API** | Búsqueda de libros mediante filtros. |
| **Excepciones** | Control de errores y validaciones del negocio. |
| **DAO** | Estructura destinada a la persistencia de información. |

---

## 2. Arquitectura y clases principales

La aplicación está organizada por responsabilidades:

```text
Interfaz gráfica (Swing)
        ↓
Lógica de negocio
        ↓
Modelo de datos
        ↓
Persistencia
```

| Clase / componente | Función principal |
|---|---|
| `Libro` | Representa los datos de cada libro. |
| `Categoria` / `Formato` | Enumeraciones que limitan valores válidos. |
| `ILibreria` | Define el contrato de operaciones CRUD. |
| `Libreria` | Implementa las reglas de negocio. |
| `LibreriaException` | Gestiona errores propios del sistema. |
| `VentanaPrincipal` | Coordina la interfaz y la lógica del programa. |
| `PanelPadreLibro` | Presenta los libros mediante una `JTable`. |
| `DialogoCrearLibro` | Captura información para registrar libros. |
| `DialogoEditarLibro` | Permite modificar un libro existente. |
| `Evento` | Gestiona acciones generadas por los botones. |
| `LibroDAO` | Clase destinada al almacenamiento y recuperación de datos. |

### Modelo `Libro`

La clase `Libro` encapsula información como ISBN, título, autor, año, categoría, editorial, páginas, precio, cantidad y formato. Utiliza métodos **getter** y **setter** para acceder de forma controlada a sus atributos.

```java
public String getIsbn() {
    return isbn;
}

public void setIsbn(String isbn) {
    this.isbn = isbn;
}
```

También sobrescribe `toString()`, permitiendo generar una representación textual del objeto para mostrar sus datos al usuario.

---

## 3. Operaciones CRUD y métodos principales

La interfaz `ILibreria` define las operaciones que debe proporcionar la lógica del sistema:

```java
void agregarLibro(Libro libro) throws LibreriaException;
Libro buscarLibro(String isbn);
void actualizarLibro(String isbn, Libro nuevo) throws LibreriaException;
void eliminarLibro(String isbn);
List<Libro> getListaLibros();
```

| Método | Descripción |
|---|---|
| `agregarLibro()` | Valida y registra un nuevo libro. |
| `buscarLibro()` | Localiza un libro mediante su ISBN. |
| `actualizarLibro()` | Modifica los datos de un registro existente. |
| `eliminarLibro()` | Elimina un libro según su ISBN. |
| `getListaLibros()` | Retorna todos los libros registrados. |
| `refrescarTabla()` | Actualiza visualmente la tabla de la interfaz. |
| `capturarDatos()` | Convierte los datos del formulario en un objeto `Libro`. |

### Búsqueda mediante Stream API

Uno de los métodos más importantes es `buscarLibro()`:

```java
public Libro buscarLibro(String isbn) {
    return listaLibros.stream()
        .filter(l -> l.getIsbn().equals(isbn))
        .findFirst()
        .orElse(null);
}
```

Su funcionamiento es:

| Instrucción | Función |
|---|---|
| `stream()` | Genera un flujo sobre la colección. |
| `filter()` | Filtra los libros según una condición. |
| `l -> ...` | Expresión lambda que compara el ISBN. |
| `findFirst()` | Obtiene la primera coincidencia. |
| `orElse(null)` | Retorna `null` cuando no existe el libro. |

Para eliminar se emplea igualmente programación funcional:

```java
listaLibros.removeIf(l -> l.getIsbn().equals(isbn));
```

`removeIf()` elimina el elemento cuando la expresión lambda devuelve `true`.

---

## 4. Interfaz gráfica y eventos

`VentanaPrincipal` hereda de `JFrame` y funciona como punto central de la aplicación. Utiliza `CardLayout` para alternar entre el inicio de sesión y el panel principal.

Los eventos se centralizan en la clase:

```java
public class Evento implements ActionListener
```

El método `actionPerformed()` recibe la acción realizada y ejecuta la operación correspondiente mediante comandos.

| Comando | Acción |
|---|---|
| `CREAR_LIBRO` | Abre el formulario de creación. |
| `GUARDAR_LIBRO` | Registra el nuevo libro. |
| `ELIMINAR_LIBRO` | Elimina el libro seleccionado. |
| `ACTUALIZAR_LIBRO` | Abre el formulario de edición. |
| `VER_LIBRO` | Muestra los datos del libro. |
| `BUSCAR_LIBRO` | Busca por ISBN. |

Los registros se presentan mediante `JTable` y `DefaultTableModel`. Cuando ocurre un cambio, `refrescarTabla()` obtiene nuevamente `libreria.getListaLibros()` y reconstruye las filas mostradas.

---

## 5. Conceptos de POO aplicados

| Concepto | Aplicación |
|---|---|
| **Encapsulamiento** | `Libro` protege sus atributos mediante getters y setters. |
| **Abstracción** | `ILibreria`, `PanelCentral` y `DialogoCentralLibro` definen estructuras generales. |
| **Herencia** | `PanelPadreLibro extends PanelCentral` y los diálogos heredan de una clase común. |
| **Polimorfismo** | Una referencia de clase padre puede trabajar con diferentes clases hijas. |
| **Interfaces** | `Libreria implements ILibreria` cumple un contrato definido. |
| **Enums** | `Categoria` y `Formato` restringen los valores disponibles. |
| **Excepciones** | `LibreriaException` representa errores específicos de las reglas del negocio. |

Por ejemplo:

```java
public class Libreria implements ILibreria
```

permite separar **qué operaciones debe ofrecer una librería** (`ILibreria`) de **cómo se implementan** (`Libreria`).

---

## 6. Flujo general

```text
Usuario
   ↓
Interfaz Swing
   ↓
Evento / ActionListener
   ↓
VentanaPrincipal
   ↓
Libreria
   ↓
Objeto Libro / List<Libro>
   ↓
Actualización de JTable
```

Cuando el usuario registra un libro, los datos son capturados desde el formulario y convertidos a un objeto `Libro`. Posteriormente `Libreria` valida la información y la incorpora a la colección. Finalmente, la interfaz actualiza la tabla para reflejar el cambio.

### Conclusión

El proyecto implementa un sistema CRUD funcional utilizando Java y Swing, aplicando una estructura organizada por responsabilidades. La utilización de interfaces, herencia, excepciones, colecciones, expresiones lambda y eventos permite demostrar de forma práctica conceptos fundamentales de **POO y desarrollo de interfaces gráficas en Java**.
