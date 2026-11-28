# Evidencias de funcionamiento y pruebas

**Proyecto:** Tienda Virtual de Libros — Grupo 5  
**Fecha:** 28/11/2026  
**Rama:** `feature/grupo5`

## 1. Capturas de pantalla

Todas están a pantalla completa (1920×1080) con Eclipse abierto detrás.
Se verificó que **no hay imágenes repetidas**: cada archivo muestra un estado
distinto (comparación píxel a píxel de la ventana, ignorando el reloj).

### 1.1 Recorrido completo de la app (`app/`, 43 capturas)

| Archivo | Evidencia |
|---|---|
| `app/01_login_vacio.png` | Login sin datos |
| `app/02_login_campos_obligatorios.png` | Error de campos obligatorios |
| `app/03_login_credenciales_invalidas.png` | Error de credenciales |
| `app/04_login_llenado.png` | Login diligenciado |
| `app/06_admin_menu.png` | Menú de administración |
| `app/10_registro_vacio.png` | Registro vacío |
| `app/11_registro_campos_obligatorios.png` | Registro sin datos obligatorios |
| `app/12_registro_correo_invalido.png` | Registro con correo inválido |
| `app/14_registro_llenado.png` | Registro diligenciado |
| `app/15_registro_exitoso.png` | Cliente registrado correctamente |
| `app/17_registro_correo_duplicado.png` | Correo ya registrado |
| `app/18_clientes_listado.png` | Listado de clientes |
| `app/19_clientes_confirmar_eliminar.png` | Confirmación de eliminación |
| `app/20_clientes_eliminado_aviso.png` | Cliente eliminado |
| `app/21_clientes_despues.png` | Listado actualizado |
| `app/22_clientes_sin_seleccion.png` | Error sin selección |
| `app/24_libros_eliminar_sin_isbn.png` | Error al eliminar sin ISBN |
| `app/25_libros_isbn_duplicado.png` | ISBN duplicado |
| `app/26_libros_datos_invalidos.png` | Datos numéricos inválidos |
| `app/27_libros_formulario_lleno.png` | Libro diligenciado |
| `app/28_libros_registrado_aviso.png` | Libro registrado |
| `app/29_libros_seleccionado.png` | Libro seleccionado con formulario lleno |
| `app/30_libros_actualizado_aviso.png` | Libro actualizado |
| `app/31_libros_confirmar_eliminar.png` | Confirmación de eliminación |
| `app/32_libros_eliminar_con_ventas_error.png` | Bloqueo por ventas asociadas |
| `app/33_libros_con_ventas_bloqueado.png` | El libro con ventas sigue en el catálogo |
| `app/34_libros_eliminado_aviso.png` | Libro sin ventas eliminado |
| `app/35_libros_despues.png` | Catálogo final |
| `app/36_mi_cuenta_datos.png` | Datos del cliente |
| `app/40_mi_cuenta_a_compras.png` | Mi Cuenta abre Compras (carrito vacío) |
| `app/41_mi_cuenta_a_historial.png` | Mi Cuenta abre el historial |
| `app/43_compras_carrito_vacio.png` | Compra sin productos |
| `app/44_compras_agregado.png` | Libro agregado al carrito |
| `app/45_compras_cantidad_invalida.png` | Cantidad no numérica |
| `app/46_compras_stock_insuficiente.png` | Stock insuficiente |
| `app/47_compras_modificada.png` | Cantidad modificada |
| `app/49_compras_lista_pagar.png` | Carrito listo con método de pago |
| `app/50_compras_confirmacion.png` | Resumen de confirmación |
| `app/51_compras_exitosa_aviso.png` | Compra registrada |
| `app/52_recibo.png` | Recibo con subtotal, IVA, descuento y total |
| `app/54_historial.png` | Historial de compras |
| `app/55_mi_cuenta_nombre_obligatorio.png` | Validación de nombre |
| `app/57_mi_cuenta_guardado_aviso.png` | Datos actualizados |

### 1.2 Pruebas individuales CRUD (`crud/`, 12 capturas)

| Archivo | Operación |
|---|---|
| `crud/PI-CLI-01_crear_formulario.png` | Formulario de registro de cliente |
| `crud/PI-CLI-02_listar.png` | Listado de clientes |
| `crud/PI-CLI-03_actualizar.png` | Datos actualizados del cliente |
| `crud/PI-CLI-04_eliminar_antes.png` | Cliente seleccionado para eliminar |
| `crud/PI-CLI-05_eliminar_despues.png` | Listado después de eliminar |
| `crud/PI-LIB-01_crear_formulario.png` | Formulario de libro diligenciado |
| `crud/PI-LIB-02_listar.png` | Búsqueda con formulario lleno |
| `crud/PI-LIB-03_actualizar.png` | Libro actualizado |
| `crud/PI-LIB-04_eliminar_antes.png` | Libro seleccionado para eliminar |
| `crud/PI-COM-01_carrito.png` | Carrito con productos y totales |
| `crud/PI-COM-02_recibo.png` | Recibo de compra |
| `crud/PI-COM-03_historial.png` | Historial de compras |

### 1.3 Eclipse con el proyecto (`capturas_eclipse/`, 1 captura)

| Archivo | Evidencia |
|---|---|
| `capturas_eclipse/00_eclipse_proyecto.png` | Eclipse con el proyecto `grupo5_tienda_virtual [feature/grupo5]` abierto |

### Evidencias completas en GitHub

```text
https://github.com/UPTC-DEVELOP/programacion_II/tree/feature/grupo5/grupo5_tienda_virtual/docs/evidencias
```

Datos de demostración usados:

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
| 14 | CRUD completo de Clientes | OK | `pruebas/14_CrudCompletoTest_clientesCrudCompleto.txt` |
| 15 | CRUD completo de Libros | OK | `pruebas/15_CrudCompletoTest_librosCrudCompleto.txt` |
| 16 | CRUD completo de Compras y carrito | OK | `pruebas/16_CrudCompletoTest_comprasCrudCompleto.txt` |

Resultado: **16/16 pruebas correctas**.

## 3. Datos de demostración

Los datos usados para las capturas están en una carpeta temporal y no modifican los datos reales del proyecto:

```text
C:\Users\juano\AppData\Local\Temp\opencode\evidencias_datos
```

Se usaron repositorios de demostración para clientes, libros y compras, y un carrito JSON temporal.
