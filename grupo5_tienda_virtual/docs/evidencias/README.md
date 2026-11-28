# Evidencias de funcionamiento y pruebas

**Proyecto:** Tienda Virtual de Libros — Grupo 5  
**Fecha:** 28/11/2026  
**Rama:** `feature/grupo5`

## 1. Capturas de pantalla

### Capturas de la ventana

| Archivo | Evidencia |
|---|---|
| `capturas/01_login.png` | Ventana de inicio de sesión |
| `capturas/02_registro_cliente.png` | Registro de cliente |
| `capturas/03_gestion_clientes.png` | Gestión y listado de clientes |
| `capturas/04_gestion_libros.png` | Gestión y catálogo de libros |
| `capturas/05_mi_cuenta.png` | Datos del cliente |
| `capturas/06_compras_carrito.png` | Carrito con productos, subtotal, IVA, descuento y total |
| `capturas/07_recibo.png` | Recibo con subtotal, IVA, descuento y total |
| `capturas/08_historial_compras.png` | Historial de compras |

### Capturas con Eclipse detrás

En `capturas_eclipse/` están las mismas 8 ventanas, pero en pantalla completa
(1920×1080) con Eclipse abierto detrás.

| Archivo | Evidencia |
|---|---|
| `capturas_eclipse/00_eclipse_proyecto.png` | Eclipse con el proyecto abierto |
| `capturas_eclipse/01_login.png` | Login con Eclipse detrás |
| `capturas_eclipse/02_registro_cliente.png` | Registro con Eclipse detrás |
| `capturas_eclipse/03_gestion_clientes.png` | Clientes con Eclipse detrás |
| `capturas_eclipse/04_gestion_libros.png` | Libros con Eclipse detrás |
| `capturas_eclipse/05_mi_cuenta.png` | Mi cuenta con Eclipse detrás |
| `capturas_eclipse/06_compras_carrito.png` | Compras con Eclipse detrás |
| `capturas_eclipse/07_recibo.png` | Recibo con Eclipse detrás |
| `capturas_eclipse/08_historial_compras.png` | Historial con Eclipse detrás |

Las capturas se generaron abriendo las ventanas reales del proyecto con datos de demostración:

- Cliente Premium: `laura@uptc.edu.co`.
- Cliente Regular: `carlos@uptc.edu.co`.
- Libro: “Cien anos de soledad”.
- Compra registrada con dos unidades.

## 2. Pruebas Maven

Las 13 pruebas se ejecutaron una por una con:

```bash
mvn -Dtest=Clase#metodo test
```

| # | Prueba | Resultado | Log |
|---|---|---|---|
| 01 | Clientes: últimos 10 y orden | OK | `pruebas/01_FlujosPersistenciaTest_clientesUltimosRespetaLimiteYOrden.txt` |
| 02 | Compra persistente en plano/JSON/BD | OK | `pruebas/02_FlujosPersistenciaTest_compraSobreviveReinicioEnLosTresAlmacenamientos.txt` |
| 03 | Fallo de guardado no descuenta stock | OK | `pruebas/03_FlujosPersistenciaTest_falloAlGuardarNoActualizaStockNiLista.txt` |
| 04 | Libro con ventas no se elimina | OK | `pruebas/04_FlujosPersistenciaTest_noEliminaLibroConVentas.txt` |
| 05 | Carrito persistente en JSON | OK | `pruebas/05_FlujosPersistenciaTest_carritoSePersisteEnJson.txt` |
| 06 | Validación de correo y contraseña | OK | `pruebas/06_FlujosPersistenciaTest_validaCorreoYContrasenaDeClientes.txt` |
| 07 | Validación de datos de Libros | OK | `pruebas/07_FlujosPersistenciaTest_validaDatosDeLibros.txt` |
| 08 | Compra sin stock | OK | `pruebas/08_ReglasNegocioTest_noFinalizaCompraSinStock.txt` |
| 09 | Descuento Premium | OK | `pruebas/09_ReglasNegocioTest_aplicaDescuentoPremiumEnLaCompra.txt` |
| 10 | Compra sin cliente, pago o carrito | OK | `pruebas/10_ReglasNegocioTest_rechazaCompraSinClienteOSinMetodoPago.txt` |
| 11 | Cliente duplicado y login inválido | OK | `pruebas/11_ReglasNegocioTest_rechazaClienteDuplicadoEInicioSesionInvalido.txt` |
| 12 | Eliminar libro sin ventas | OK | `pruebas/12_ReglasNegocioTest_permiteEliminarLibroSinVentas.txt` |
| 13 | Inventario y stock insuficiente | OK | `pruebas/13_ReglasNegocioTest_actualizaInventarioYRechazaCantidadMayorAlStock.txt` |

Resultado: **13/13 pruebas correctas**.

## 3. Datos de demostración

Los datos usados para las capturas están en una carpeta temporal y no modifican los datos reales del proyecto:

```text
C:\Users\juano\AppData\Local\Temp\opencode\evidencias_datos
```

Se usaron repositorios de demostración para clientes, libros y compras, y un carrito JSON temporal.
