# Tienda Virtual de Libros

Aplicación de escritorio en Java (Swing) para la gestión de una tienda virtual de libros.

Proyecto académico — Programación II, 2026-II.

## Integrantes — Grupo 5

- Juan Pablo Barrero
- Cristhian Camilo Peña
- Liz Alejandra Rojas

## Descripción

Permite administrar el catálogo de libros y los clientes, gestionar un carrito de
compras y registrar las compras con su recibo.

## Arquitectura (Maven + principios SOLID)

Proyecto Maven (`pom.xml`) organizado en capas dentro de `src/main/java`:

- `persistencia` — contratos y repositorios: la interfaz `IRepositorio<T>` y sus
  implementaciones `RepositorioClientesJson` y `RepositorioLibrosJson`
  (archivos JSON con la librería Gson, importada desde el `pom.xml`).
- `service` — interfaces de la lógica de negocio: `IServicioClientes` e
  `IServicioLibros`.
- `controller` — la lógica concreta (`ControladorCliente`, `ControladorLibro`):
  implementa las interfaces de `service` y recibe el repositorio por inyección
  de dependencias.
- `model` — clases de datos del dominio (sin depender de la interfaz gráfica ni
  del disco).
- `view` — interfaz gráfica (Swing): depende **solo** de las interfaces de
  `service`, nunca de las implementaciones.

Principios aplicados:

- **SRP:** cada clase hace una sola cosa: el modelo solo modela datos, el
  repositorio solo persiste y el controlador solo decide.
- **OCP:** la lógica puede cambiar o crecer sin tocar las ventanas.
- **LSP:** `ClienteRegular` y `ClientePremium` se usan indistintamente.
- **ISP:** interfaces pequeñas y específicas (`IServicioClientes`,
  `IServicioLibros`).
- **DIP:** las ventanas dependen de `service.*` y los controladores de
  `persistencia.IRepositorio`, jamás de una clase concreta.

## Módulos

| Módulo | Responsable | Clases |
|--------|-------------|--------|
| Libros | Cristhian Camilo Peña | `Libro`, `FormatoLibro` |
| Clientes | Juan Pablo Barrero | `Cliente`, `ClienteRegular`, `ClientePremium` |
| Compras | Liz Alejandra Rojas | `CarritoCompras`, `Compra`, `ItemCompra`, `MetodoPago` |

## Persistencia avanzada (JSON)

Los datos ya no viven en memoria: se guardan y se leen desde archivos JSON
(librería Gson importada con Maven):

- `datos/clientes.json` — clientes (incluye el tipo Regular/Premium).
- `datos/libros.json` — inventario de libros.

La carpeta `datos/` se crea automáticamente al ejecutar y está ignorada por Git;
borrárla reinicia los datos de demostración.

## Requisitos

- Java 21.
- Maven (el proyecto se compila con `mvn compile`).

## Compilación y ejecución

```bash
mvn compile
```

Desde Eclipse: importar el proyecto y ejecutar
`src/main/java/view/Main.java` (clic derecho > Run As > Java Application).

## Estructura

```
pom.xml
src/main/java/
  persistencia/   IRepositorio, RepositorioClientesJson, RepositorioLibrosJson
  service/        IServicioClientes, IServicioLibros
  controller/     ControladorCliente, ControladorLibro
  model/          Cliente, Libro, Compra, CarritoCompras, ...
  view/           Main, VentanaLogin, VentanaRegistroCliente, ...
diseno/
prototipos/
datos/            (generada en runtime: clientes.json, libros.json)
```
