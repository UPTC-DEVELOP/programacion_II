# 4.2 Libros

**Responsable:** Cristhian Camilo Peña  
**Requisitos:** RF01 Registrar libro, RF02 Actualizar libro, RF03 Eliminar libro, RF04 Listar libros, RF09 Buscar libro, RF18 Validar disponibilidad, RF19 Actualizar inventario.

## a) Caso de uso

- Actor: Administrador / Cliente en catálogo.
- Casos: registrar, listar, buscar, actualizar, eliminar, validar stock y actualizar inventario.

**Evidencia:** pendiente de diagrama específico en el DAD final.

## b) Especificación

- ISBN obligatorio y único.
- Título y autor obligatorios.
- Año, páginas, precio, stock y formato válidos.
- IVA de 19% o 5%.
- No eliminar si tiene ventas asociadas.
- Buscar por ISBN, título o autor.

## c) Diagrama de clases

Debe incluir:

- `Libro`, `FormatoLibro`.
- `IRepositorio<Libro>`.
- Repositorios plano, JSON y BD.
- `IServicioLibros`.
- `ControladorLibro`.
- `ConsultaVentas`.
- `EventosLibros`.

**Evidencia:** pendiente de diagrama en el DAD final.

## d) Diagrama de secuencia

Debe mostrar:

```text
Vista → EventosLibros → IServicioLibros → ControladorLibro → IRepositorio → almacenamiento
```

**Evidencia:** pendiente de diagrama en el DAD final.

## e) Código y commits

- `ControladorLibro`.
- `ProjectConfig.libros()`.
- `RepositorioLibrosArchivoPlano`, `RepositorioLibrosJson`, `RepositorioLibrosBD`.

**Evidencia:** pendiente de fragmento y enlace en el DAD final.

## f) Pruebas individuales

| ID | Operación | Evidencia | Estado |
|---|---|---|---|
| PI-LIB-01 | Crear libro | `pruebas/15_CrudCompletoTest_librosCrudCompleto.txt` | OK |
| PI-LIB-02 | Listar/buscar | `pruebas/15_CrudCompletoTest_librosCrudCompleto.txt` | OK |
| PI-LIB-03 | Actualizar | `pruebas/15_CrudCompletoTest_librosCrudCompleto.txt` | OK |
| PI-LIB-04 | Eliminar sin ventas | `pruebas/12_ReglasNegocioTest_permiteEliminarLibroSinVentas.txt` | OK |
| PI-LIB-05 | Bloquear eliminación con ventas | `pruebas/04_FlujosPersistenciaTest_noEliminaLibroConVentas.txt` | OK |
| PI-LIB-06 | Validación de datos | `pruebas/07_FlujosPersistenciaTest_validaDatosDeLibros.txt` | OK |
| PI-LIB-07 | Inventario y stock | `pruebas/13_ReglasNegocioTest_actualizaInventarioYRechazaCantidadMayorAlStock.txt` | OK |

## Capturas

- `capturas_eclipse/04_gestion_libros.png`
- `crud/PI-LIB-01_crear_formulario.png`
- `crud/PI-LIB-02_listar.png`
- `crud/PI-LIB-03_actualizar.png`
- `crud/PI-LIB-04_eliminar.png`
