# Guía del equipo — Grupo 5

**Proyecto:** Tienda Virtual de Libros  
**Curso:** Programación II — 2026-II  
**Integrantes:** Juan Pablo Barrero, Cristhian Camilo Peña, Liz Alejandra Rojas  
**Actualizado:** 19/11/2026

Esta guía explica cómo está armado el proyecto hoy, después de la refactorización de la Unidad 3. Si algo no coincide con el código, manda el código.

---

## 1. Arquitectura actual

El proyecto es Maven y está en `src/main/java`:

```text
config/         ProjectConfig
model/          Cliente, Libro, Compra, ItemCompra, CarritoCompras, MetodoPago
persistencia/   IRepositorio, repositorios planar/JSON/BD y RegistroOperacionesTXT
service/        IServicioClientes, IServicioLibros, IServicioCompras, ResultadoCompra
controller/     ControladorCliente, ControladorLibro, ControladorCompras, ControladorOperaciones
view/           Main, ventanas y clases Eventos*
```

### Reglas de dependencia

- `model` no depende de Swing ni de la persistencia.
- `persistencia` no depende de `view`.
- `service` solo tiene interfaces y clases de resultado.
- `controller` depende de `IRepositorio` y de las interfaces de servicio.
- `view` depende de las interfaces `IServicio*`, no de los controladores concretos.
- `Main` arma todo y es el único que conoce las implementaciones concretas.

---

## 2. Persistencia por módulo

Cada módulo puede usar tres repositorios:

| Módulo | Archivo plano | JSON | Base de datos | Default actual |
|---|---|---|---|---|
| Clientes | `datos/clientes.txt` | `datos/clientes.json` | `datos/tienda.db` | Archivo plano |
| Libros | `datos/libros.txt` | `datos/libros.json` | `datos/tienda.db` | JSON |
| Compras | `datos/compras.txt` | `datos/compras.json` | `datos/tienda.db` | Base de datos |

El único lugar donde se elige es `config/ProjectConfig.java`.

Ejemplo para cambiar Compras a JSON:

```java
public static IRepositorio<Compra> compras() {
    return comprasJson();
}
```

No se tocan ventanas ni controladores.

No se usa `-Dtienda.bd` ni `usarBD`. Esa variable ya no existe.

---

## 3. Eventos

La clase base es `view/Eventos.java`. Tiene utilidades comunes:

- `mostrarInfo`
- `mostrarError`
- `confirmar`
- `volver`

Los manejadores que ya existen:

- `EventosCompras`
- `EventosHistorialCompras`
- `EventosRecibo`

Regla: un `Eventos` no debe tener SQL, Gson, archivos ni reglas de negocio. Solo escucha la ventana y llama a los servicios.

Todavía faltan los eventos de Login, Registro, Clientes, Libros y Mi Cuenta.

---

## 4. Lógica de compras

La lógica de finalizar compra está en `ControladorCompras.finalizarCompra`.

Ese método:

1. Valida cliente, carrito y método de pago.
2. Valida stock.
3. Crea la `Compra` y sus `ItemCompra`.
4. Calcula el total con descuento.
5. Guarda la compra.
6. Actualiza el inventario.
7. Devuelve un `ResultadoCompra`.

`EventosCompras` solo llama al servicio y muestra el resultado. La ventana ya no crea compras ni guarda datos.

El recibo se muestra con `VentanaRecibo` después de una compra exitosa.

---

## 5. Cómo agregar o cambiar algo

Para un módulo nuevo:

1. Modelo en `model`.
2. Interfaz de servicio en `service`.
3. Implementación en `controller`.
4. Repositorios en `persistencia` (plano, JSON y BD si aplica).
5. Entrada en `ProjectConfig`.
6. Ventana en `view`.
7. Manejador de eventos que herede de `Eventos`.
8. Prueba con las tres persistencias.

No metas lógica de negocio en la ventana ni en el manejador de eventos.

---

## 6. Git

1. Trabajar en `feature/grupo5`.
2. Nunca hacer push a `main`.
3. Hacer commits pequeños y con mensaje claro.
4. Antes de trabajar:

```bash
git pull --rebase origin feature/grupo5
```

5. Antes de commitear:

```bash
mvn clean compile
```

6. No subir `target/`, `datos/` ni archivos locales de Eclipse.

Formato de mensaje:

```text
tipo(alcance): descripción corta
```

---

## 7. Compilar y ejecutar

```bash
mvn clean compile
```

Desde Eclipse: ejecutar `src/main/java/view/Main.java`.

Credenciales de prueba: `juan@uptc.edu.co` / `1234`.

---

## 8. Estado actual

Hecho:

- `ProjectConfig` y `Main` sin `usarBD`.
- Repositorios plano, JSON y BD de Clientes, Libros y Compras.
- `ResultadoCompra`.
- `finalizarCompra` en `ControladorCompras`.
- `Eventos` base y eventos de Compras, Historial y Recibo.
- `VentanaRecibo`.
- `RegistroOperacionesTXT` y `ControladorOperaciones`.
- `registrarCliente` en el servicio.
- README y pruebas manuales de Clientes/Compras.

Pendiente:

- Eventos de Login, Registro, Clientes, Libros y Mi Cuenta.
- Catálogo y búsqueda separados para cliente.
- Matriz 3x3 y acta de pruebas.
- Revisión final del informe y guion.

---

## 9. Reglas de oro

1. La ventana no piensa.
2. El controlador no sabe de Swing.
3. El repositorio no sabe de ventanas.
4. `ProjectConfig` es el único que elige persistencia.
5. `Eventos` no tiene lógica de negocio.
6. Un commit, una idea.
7. Si no compila, no se sube.
