# Checklist de pruebas de persistencia

**Proyecto:** Tienda Virtual de Libros — Grupo 5  
**Fecha de ejecución:** 20/11/2026

## 1. Matriz 3x3

| Módulo | Archivo plano | JSON | Base de datos |
|---|---|---|---|
| Clientes | [x] | [x] | [x] |
| Libros | [x] | [x] | [x] |
| Compras | [x] | [x] | [x] |

Cada combinación se probó con un arnés temporal:

- Agregar un registro.
- Guardar.
- Crear una instancia nueva del repositorio.
- Verificar que el registro se recuperó.
- Eliminar.
- Guardar.
- Crear otra instancia y verificar que quedó vacío.

Resultado: **9/9 OK**.

## 2. Evidencia por combinación

| Módulo | Persistencia | Archivo o base | Resultado |
|---|---|---|---|
| Clientes | Archivo plano | `datos/clientes.txt` | OK |
| Clientes | JSON | `datos/clientes.json` | OK |
| Clientes | BD | `datos/tienda.db` | OK |
| Libros | Archivo plano | `datos/libros.txt` | OK |
| Libros | JSON | `datos/libros.json` | OK |
| Libros | BD | `datos/tienda.db` | OK |
| Compras | Archivo plano | `datos/compras.txt` | OK |
| Compras | JSON | `datos/compras.json` | OK |
| Compras | BD | `datos/tienda.db` | OK |

## 3. Prueba de cambio de una línea

Se cambió `ProjectConfig.compras()`:

```java
return comprasBD();
```

por:

```java
return comprasJson();
```

Resultados:

```text
Antes:  RepositorioComprasBD
Después: RepositorioComprasJson
Vuelta: RepositorioComprasBD
```

También se ejecutó `mvn clean compile` después de cada cambio: BUILD SUCCESS.

No se modificó ninguna ventana ni controlador.

## 4. Independencia de la GUI

- [x] `model` no importa Swing.
- [x] `persistencia` no importa Swing.
- [x] `service` no importa Swing.
- [x] `controller` no importa Swing.
- [x] `ControladorCompras.finalizarCompra` se probó sin abrir la ventana.

## 5. Pruebas JUnit

- [x] Clientes: últimos 10 y orden.
- [x] Compra persistente: guardar, reiniciar, historial y stock.
- [x] Fallo de guardado: no actualiza stock ni lista.
- [x] Libro con ventas: no se elimina.
- [x] Carrito: guardar, recargar y vaciar en JSON.
- [x] Clientes: correo y contraseña inválidos se rechazan.
- [x] Libros: IVA, año y páginas inválidos se rechazan.
- [x] Compra sin stock no se guarda.
- [x] Compra Premium aplica descuento.
- [x] Compra sin cliente, pago o carrito vacío se rechaza.
- [x] Login con contraseña incorrecta se rechaza.
- [x] Libro sin ventas se puede eliminar.
- [x] Inventario actualiza y rechaza cantidad mayor al stock.

```bash
mvn test
```

Resultado: **13 pruebas, 0 fallos**, ejecutadas individualmente con Maven.

## 6. Compilación

```bash
mvn clean compile
```

Resultado: **BUILD SUCCESS** con 51 fuentes.

## 7. Funciones del caso de estudio

- [x] Eliminar libro solo si no tiene ventas.
- [x] Carrito persistente en `datos/carritos/<correo>.json`.
- [x] Confirmación del resumen antes de comprar.
- [x] Recibo con subtotal, IVA, descuento Premium y total.
- [x] Validación de correo y contraseña en Clientes.
- [x] Validación de año, páginas, IVA y stock en Libros.
- [x] Combo de Compras con búsqueda por ISBN, título o autor.
