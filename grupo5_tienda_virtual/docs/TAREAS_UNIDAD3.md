# Tareas Unidad 3 — Grupo 5 (CRUD por integrante, SOLID + Maven + JSON + BD)

Este documento explica **qué debe hacer cada integrante** para completar la Unidad 3.
La base (arquitectura SOLID, Maven, persistencia JSON y BD de Clientes) **ya está subida**.

---

## 0. Antes de empezar (los dos)

```bash
git checkout feature/grupo5
git pull origin feature/grupo5
```

**Estructura del proyecto (nueva):**

```
src/main/java/
  model/         -> clases de datos (Cliente, Libro, Compra...)
  service/       -> INTERFACES de la lógica de negocio (IServicioClientes, IServicioLibros)
  controller/    -> implementación de esas interfaces (ControladorCliente, ControladorLibro)
  persistencia/  -> IRepositorio<T> + implementaciones JSON y BD
  view/          -> ventanas Swing (usan las interfaces de service)
```

**Reglas:**

- **Nunca** subir a `main`; todo en `feature/grupo5`.
- **Un commit por cambio lógico**, con mensaje claro.
- Hacer **`git pull`** antes de empezar y **no** volver a crear los paquetes viejos (`negocio`, `gui`).
- La app debe funcionar con **JSON y con Base de Datos** (`-Dtienda.bd=true`).

**Referencia para copiar el patrón:** mira
`persistencia/RepositorioClientesBD.java` (BD) y `persistencia/RepositorioClientesJson.java` (JSON).
La ruta de la BD **NO se escribe a mano**: se usa `persistencia/ConfiguracionBD` (`datos/tienda.db`)
para que todos los repositorios apunten a **la misma base**.

---

## 1. Cristhian — CRUD de Libros con Base de Datos

Tu parte ya tiene JSON (`RepositorioLibrosJson`). Falta la **base de datos**.

### 1.1 Crear `src/main/java/persistencia/RepositorioLibrosBD.java`

Debe implementar `IRepositorio<Libro>` igual que el de clientes, usando:

```java
private static final String RUTA_POR_DEFECTO = ConfiguracionBD.RUTA_BD;
// y para conectar:
DriverManager.getConnection(ConfiguracionBD.JDBC_PREFIJO + ruta)
```

**Tabla** (crear con `CREATE TABLE IF NOT EXISTS`):

```sql
CREATE TABLE IF NOT EXISTS libros (
    isbn TEXT PRIMARY KEY,
    titulo TEXT,
    autor TEXT,
    anio_publicacion INTEGER,
    categoria TEXT,
    editorial TEXT,
    numero_paginas INTEGER,
    precio REAL,
    porcentaje_iva REAL,
    cantidad_disponible INTEGER,
    formato TEXT
);
```

- `cargar()`: `SELECT ...` y reconstruye los `Libro` (el `formato` se lee con `FormatoLibro.valueOf(...)`).
- `guardar()`: `DELETE FROM libros` + `INSERT` en lote (como el de clientes).

### 1.2 Conectar en `view/Main.java`

Igual que clientes:

```java
IRepositorio<Libro> repositorioLibros = usarBD
        ? new RepositorioLibrosBD()
        : new RepositorioLibrosJson();
```

### 1.3 Probar

- Modo JSON: `mvn compile` y ejecutar.
- Modo BD: ejecutar con `-Dtienda.bd=true` y revisar que la tabla `libros` de
  `datos/tienda.db` tenga los libros.
- Verifica: registrar, listar/buscar, actualizar y eliminar libro (en **JSON y BD**).

---

## 2. Liz — CRUD de Compras con JSON + Base de Datos

Tu módulo **no tiene persistencia todavía** (el carrito vive en memoria). Hay que crearla
con la misma arquitectura.

### 2.1 Interfaz de negocio: `src/main/java/service/IServicioCompras.java`

```java
public interface IServicioCompras {
    boolean registrarCompra(Compra compra);
    List<Compra> listarCompras();
}
```

### 2.2 Controlador: `src/main/java/controller/ControladorCompras.java`

Que implemente `IServicioCompras` y reciba un `IRepositorio<Compra>` por constructor
(igual que `ControladorCliente`). En `registrarCompra`: `repositorio.agregar(...)` y `guardar()`.

### 2.3 Persistencia JSON: `persistencia/RepositorioComprasJson.java`

- `Compra` tiene una fecha `LocalDate`: usa el **`LocalDateAdapter`** que ya existe al crear el Gson:

```java
new GsonBuilder()
    .registerTypeAdapter(java.time.LocalDate.class, new LocalDateAdapter())
    .setPrettyPrinting()
    .create();
```

- Archivo: `datos/compras.json`.

### 2.4 Persistencia BD: `persistencia/RepositorioComprasBD.java`

Usa `ConfiguracionBD`. Dos tablas (cabecera + detalle):

```sql
CREATE TABLE IF NOT EXISTS compras (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha TEXT,
    metodo_pago TEXT,
    correo_cliente TEXT,
    total REAL
);

CREATE TABLE IF NOT EXISTS detalle_compra (
    compra_id INTEGER,
    isbn TEXT,
    cantidad INTEGER,
    precio_unitario REAL
);
```

### 2.5 Conectar en `Main` y en la ventana

- En `Main`: elegir `RepositorioComprasJson` o `RepositorioComprasBD` según `usarBD`,
  y crear el `ControladorCompras`.
- En `VentanaCompras`, al pulsar **Comprar** (método `finalizarCompra`), además de mostrar
  el total, **guardar la compra** con el servicio (y agregarla al historial del cliente).
- Verifica que la compra quede en `datos/compras.json` y en `datos/tienda.db`.

---

## 3. Los dos: material para el INFORME grupal (Unidad 3)

Cada integrante debe aportar, de **su** CRUD:

1. **Diagrama de caso de uso** específico (Crear, Consultar, Actualizar, Eliminar).
2. **Especificación del requerimiento funcional** (ID RF/CU del DAD).
3. **Diagrama de clases** (entidad + `IRepositorio` + repo JSON + repo BD + servicio + controlador).
4. **Diagrama de secuencia** de un flujo (Vista -> Controlador -> Servicio -> Repositorio -> JSON/BD).
5. **Pruebas individuales**: una captura por operación (C, R, U, D) y una de validación/error.
6. **Pruebas cruzadas**: probar el CRUD del compañero sobre la rama integrada.

> El informe lo sube **una sola persona por grupo** en
> `Grupo5_Unidad3_InformeCRUD.docx` (Word o PDF), con la lista de chequeo diligenciada.

---

## 4. Cómo saber que quedó bien (checklist rápido)

- [ ] `mvn clean install` termina en **BUILD SUCCESS**.
- [ ] La app corre sin errores en **modo JSON** y en **modo BD** (`-Dtienda.bd=true`).
- [ ] Cada CRUD hace **Crear, Listar, Actualizar y Eliminar** con **JSON y BD**.
- [ ] Todos los repositorios de BD usan **`ConfiguracionBD`** (misma `datos/tienda.db`).
- [ ] No se borró ni rompió código de otro compañero.
- [ ] Los commits están en `feature/grupo5` y con el nombre del autor correcto.
