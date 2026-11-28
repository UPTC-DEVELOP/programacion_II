# 10. Evidencias: qué va en el DAD y qué queda en el repositorio

**Verificación de duplicados:** las 56 capturas se compararon recortando la
ventana de la app y contrastando píxel a píxel (ignorando el reloj de Windows).
Resultado: **0 imágenes repetidas**.

## Repositorio

```text
https://github.com/UPTC-DEVELOP/programacion_II/tree/feature/grupo5/grupo5_tienda_virtual/docs/evidencias
```

| Carpeta | Contenido | Enlace directo |
|---|---|---|
| `app/` | Recorrido completo (43) | [ver](https://github.com/UPTC-DEVELOP/programacion_II/tree/feature/grupo5/grupo5_tienda_virtual/docs/evidencias/app) |
| `crud/` | Pruebas individuales (12) | [ver](https://github.com/UPTC-DEVELOP/programacion_II/tree/feature/grupo5/grupo5_tienda_virtual/docs/evidencias/crud) |
| `capturas_eclipse/` | Eclipse con el proyecto (1) | [ver](https://github.com/UPTC-DEVELOP/programacion_II/tree/feature/grupo5/grupo5_tienda_virtual/docs/evidencias/capturas_eclipse) |
| `pruebas/` | Logs Maven (16) | [ver](https://github.com/UPTC-DEVELOP/programacion_II/tree/feature/grupo5/grupo5_tienda_virtual/docs/evidencias/pruebas) |

## 10.1 Capturas que van dentro del DAD (42)

### Arquitectura e integración

| Captura | Sección | Muestra |
|---|---|---|
| `capturas_eclipse/00_eclipse_proyecto.png` | 3 y 6 | Eclipse con el proyecto abierto |

### 4.1 Clientes (Juan Pablo)

| Captura | Sección | Muestra |
|---|---|---|
| `app/01_login_vacio.png` | 4.1 | Login sin datos |
| `app/04_login_llenado.png` | 4.1 | Login diligenciado |
| `app/14_registro_llenado.png` | 4.1 | Registro diligenciado |
| `app/15_registro_exitoso.png` | 4.1 | Cliente registrado |
| `app/17_registro_correo_duplicado.png` | 4.1 | Correo duplicado |
| `app/18_clientes_listado.png` | 4.1 | Listado de clientes |
| `app/19_clientes_confirmar_eliminar.png` | 4.1 | Confirmación de eliminación |
| `app/21_clientes_despues.png` | 4.1 | Listado actualizado |
| `app/36_mi_cuenta_datos.png` | 4.1 | Datos del cliente |
| `app/57_mi_cuenta_guardado_aviso.png` | 4.1 | Datos actualizados |
| `crud/PI-CLI-01_crear_formulario.png` | 4.1.f | Crear |
| `crud/PI-CLI-02_listar.png` | 4.1.f | Listar |
| `crud/PI-CLI-03_actualizar.png` | 4.1.f | Actualizar |
| `crud/PI-CLI-04_eliminar_antes.png` | 4.1.f | Eliminar (antes) |
| `crud/PI-CLI-05_eliminar_despues.png` | 4.1.f | Eliminar (después) |

### 4.2 Libros (Cristhian)

| Captura | Sección | Muestra |
|---|---|---|
| `app/27_libros_formulario_lleno.png` | 4.2 | Formulario diligenciado |
| `app/28_libros_registrado_aviso.png` | 4.2 | Libro registrado |
| `app/29_libros_seleccionado.png` | 4.2 | Libro seleccionado |
| `app/30_libros_actualizado_aviso.png` | 4.2 | Libro actualizado |
| `app/31_libros_confirmar_eliminar.png` | 4.2 | Confirmación de eliminación |
| `app/32_libros_eliminar_con_ventas_error.png` | 4.2 | Bloqueo por ventas |
| `app/35_libros_despues.png` | 4.2 | Catálogo final |
| `crud/PI-LIB-01_crear_formulario.png` | 4.2.f | Crear |
| `crud/PI-LIB-02_listar.png` | 4.2.f | Listar/buscar |
| `crud/PI-LIB-03_actualizar.png` | 4.2.f | Actualizar |
| `crud/PI-LIB-04_eliminar_antes.png` | 4.2.f | Eliminar |

### 4.3 Compras (Liz)

| Captura | Sección | Muestra |
|---|---|---|
| `app/43_compras_carrito_vacio.png` | 4.3 | Compra sin productos |
| `app/44_compras_agregado.png` | 4.3 | Libro agregado |
| `app/46_compras_stock_insuficiente.png` | 4.3 | Stock insuficiente |
| `app/47_compras_modificada.png` | 4.3 | Cantidad modificada |
| `app/50_compras_confirmacion.png` | 4.3 | Confirmación de compra |
| `app/52_recibo.png` | 4.3 | Recibo con descuento |
| `app/54_historial.png` | 4.3 | Historial |
| `crud/PI-COM-01_carrito.png` | 4.3.f | Carrito |
| `crud/PI-COM-02_recibo.png` | 4.3.f | Recibo |
| `crud/PI-COM-03_historial.png` | 4.3.f | Historial |

### 5. Validaciones y errores

| Captura | Muestra |
|---|---|
| `app/02_login_campos_obligatorios.png` | Campos obligatorios |
| `app/03_login_credenciales_invalidas.png` | Credenciales inválidas |
| `app/22_clientes_sin_seleccion.png` | Cliente sin seleccionar |
| `app/45_compras_cantidad_invalida.png` | Cantidad no numérica |
| `app/55_mi_cuenta_nombre_obligatorio.png` | Nombre obligatorio |

## 10.2 Capturas que NO van dentro del DAD (14)

Quedan en el repositorio como respaldo y se consultan con el enlace de la carpeta.

| Captura | Motivo |
|---|---|
| `app/06_admin_menu.png` | Menú intermedio de administración |
| `app/10_registro_vacio.png` | Estado inicial del formulario |
| `app/11_registro_campos_obligatorios.png` | Variante del mismo mensaje de validación |
| `app/12_registro_correo_invalido.png` | Variante del mismo mensaje de validación |
| `app/20_clientes_eliminado_aviso.png` | Aviso intermedio |
| `app/24_libros_eliminar_sin_isbn.png` | Variante de validación |
| `app/25_libros_isbn_duplicado.png` | Variante de validación |
| `app/26_libros_datos_invalidos.png` | Variante de validación |
| `app/33_libros_con_ventas_bloqueado.png` | Repite el estado del bloqueo |
| `app/34_libros_eliminado_aviso.png` | Aviso intermedio |
| `app/40_mi_cuenta_a_compras.png` | Navegación intermedia |
| `app/41_mi_cuenta_a_historial.png` | Navegación intermedia |
| `app/49_compras_lista_pagar.png` | Estado previo a la confirmación |
| `app/51_compras_exitosa_aviso.png` | Aviso intermedio |

Aunque no se inserten en el documento, **todas** se pueden ver en:

```text
https://github.com/UPTC-DEVELOP/programacion_II/tree/feature/grupo5/grupo5_tienda_virtual/docs/evidencias
```
