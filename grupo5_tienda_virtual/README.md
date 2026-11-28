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
  implementaciones en archivo plano, JSON (librería Gson, importada desde el
  `pom.xml`) y base de datos SQLite, por módulo (`RepositorioClientes*`,
  `RepositorioLibros*`, `RepositorioCompras*`).
- `config` — `ProjectConfig`: único lugar donde se decide qué repositorio usa
  cada módulo.
- `service` — interfaces de la lógica de negocio: `IServicioClientes`,
  `IServicioLibros` e `IServicioCompras` (más `ResultadoCompra`).
- `controller` — la lógica concreta (`ControladorCliente`, `ControladorLibro`,
  `ControladorCompras`): implementa las interfaces de `service` y recibe el
  repositorio por inyección de dependencias.
- `model` — clases de datos del dominio (sin depender de la interfaz gráfica ni
  del disco).
- `view` — interfaz gráfica (Swing): depende **solo** de las interfaces de
  `service`, nunca de las implementaciones. `Eventos` es la clase base con
  utilidades comunes de la interfaz.

Principios aplicados:

- **SRP:** cada clase hace una sola cosa: el modelo solo modela datos, el
  repositorio solo persiste y el controlador solo decide.
- **OCP:** la lógica puede cambiar o crecer sin tocar las ventanas.
- **LSP:** `ClienteRegular` y `ClientePremium` se usan indistintamente.
- **ISP:** interfaces pequeñas y específicas (`IServicioClientes`,
  `IServicioLibros`, `IServicioCompras`).
- **DIP:** las ventanas dependen de `service.*` y los controladores de
  `persistencia.IRepositorio`, jamás de una clase concreta.

## Módulos

| Módulo | Responsable | Clases |
|--------|-------------|--------|
| Libros | Cristhian Camilo Peña | `Libro`, `FormatoLibro` |
| Clientes | Juan Pablo Barrero | `Cliente`, `ClienteRegular`, `ClientePremium` |
| Compras | Liz Alejandra Rojas | `CarritoCompras`, `Compra`, `ItemCompra`, `MetodoPago` |

## Persistencia

Los datos no viven en memoria: se guardan y se leen desde archivo plano, JSON
(librería Gson importada con Maven) o base de datos SQLite. Cada módulo usa
**una sola** opción a la vez, elegida en `config/ProjectConfig.java`:

| Módulo | Archivo plano | JSON | Base de datos | Valor por defecto |
|--------|---------------|------|---------------|-------------------|
| Clientes | `datos/clientes.txt` | `datos/clientes.json` (incluye el tipo Regular/Premium) | `datos/tienda.db` | Archivo plano |
| Libros | `datos/libros.txt` | `datos/libros.json` (inventario de libros) | `datos/tienda.db` | JSON |
| Compras | `datos/compras.txt` | `datos/compras.json` | `datos/tienda.db` | Base de datos |

Para cambiar la persistencia de un módulo se modifica **una sola línea** en
`ProjectConfig`. Por ejemplo, para pasar Compras de base de datos a JSON:

```java
public static IRepositorio<Compra> compras() {
    return comprasJson();   // antes: return comprasBD();
}
```

No se toca ninguna ventana ni ningún controlador.

La carpeta `datos/` se crea automáticamente al ejecutar y está ignorada por Git;
borrarla reinicia los datos de demostración.

El carrito de cada cliente se guarda en `datos/carritos/<correo>.json`.
El recibo muestra subtotal, IVA, descuento Premium y total.
Un libro con ventas asociadas no se puede eliminar.


## Estado de la refactorización

Detalle y reparto en `docs/PLAN_REFACTORIZACION_GRUPO5.md`.

| Punto | Estado |
|-------|--------|
| `ProjectConfig` y `Main` sin la bandera `usarBD` | Hecho |
| Repositorios plano, JSON y BD de Clientes, Libros y Compras | Hecho |
| `ResultadoCompra` | Hecho y usado por `ControladorCompras` |
| Lógica de finalizar compra en `ControladorCompras` | Hecho |
| `Eventos` base y eventos de Compras, Historial y Recibo | Hecho |
| Eventos de Login, Registro, Clientes, Libros y Mi Cuenta | Pendiente (Cristhian) |
| Registro de cliente delegado al servicio | Hecho (`registrarCliente`) |
| Recibo de compra (RF20) | Hecho (`VentanaRecibo`) |
| Registro de operaciones en TXT | Hecho (`RegistroOperacionesTXT` + `ControladorOperaciones`) |
| Catálogo y búsqueda separados para cliente | Pendiente (Cristhian) |
| Matriz 3x3 y acta de pruebas | Hecho: 9/9 y prueba de una línea |

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

Si no hay datos, `Main` crea el cliente Premium `juan@uptc.edu.co` / `1234` y
tres libros de demostración.

## Pruebas

Pruebas automáticas:

```bash
mvn test
```

- `src/test/java/persistencia/FlujosPersistenciaTest.java`
- 13 pruebas: límite de Clientes, compra persistente, fallo de guardado, libro con ventas, carrito JSON, validaciones, stock, descuento Premium, login e inventario.

Pruebas manuales documentadas en `docs/`:

- `PRUEBAS_CLIENTES.md`
- `PRUEBAS_COMPRAS.md`
- `CHECKLIST_PRUEBAS.md` (matriz 3x3 de persistencia)
- `ACTA_PRUEBAS.md` (resultados)


## Estructura

```
pom.xml
src/main/java/
  config/         ProjectConfig
  persistencia/   IRepositorio y repositorios (plano, JSON, BD) por módulo
  service/        IServicioClientes, IServicioLibros, IServicioCompras, ResultadoCompra
  controller/     ControladorCliente, ControladorLibro, ControladorCompras
  model/          Cliente, Libro, Compra, CarritoCompras, ...
  view/           Main, Eventos, VentanaLogin, VentanaRegistroCliente, ...
diseno/
prototipos/
docs/             guías, tareas y pruebas
datos/            (generada en runtime: clientes, libros, compras y tienda.db)
```
