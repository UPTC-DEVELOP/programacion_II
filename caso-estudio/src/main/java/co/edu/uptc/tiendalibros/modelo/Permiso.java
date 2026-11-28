package co.edu.uptc.tiendalibros.modelo;

import java.util.Optional;

/// Representa una acción o un algo que un usuario puede hacer en el sistema.
///
/// El nombre de cada permiso, o sea, la constante se guarda tal cual en la columna
/// `permiso` de la tabla `rol_permiso`.
/// 
/// Cuidado: No se debe cambiar el nombre de una constante sin verificar la tabla y el
/// script (`insertar-rol_permiso.sql`).
///
/// El nombre de un permiso tiene esta forma: `RECURSO_ACCION`. Ejemplo:
/// `ACCEDER_EXPLORADOR_CATALOGO`.
/// 
/// Si un permiso se refiere a un atributo, el nombre tiene esta forma
/// `RECURSO_ACCION_ATRIBUTO`. Por ejemplo: `FORMATO_LIBRO_EDITAR_PRECIO_BASE`.
public enum Permiso {
  
  // Permisos para dar acceso al menú de navegación
  
  /// Explorar el catálogo de libros.
  ACCEDER_EXPLORADOR_CATALOGO,
  /// Entrar a la gestión del catálogo (libros, editoriales, autores y categorías).
  ACCEDER_GESTION_CATALOGO,
  /// Entrar a la gestión de ventas.
  ACCEDER_GESTION_VENTAS,
  /// Entrar a la gestión de clientes.
  ACCEDER_GESTION_CLIENTES,
  /// Entrar a la administración del sistema.
  ACCEDER_ADMINISTRACION,
  
  // Permisos para la gestión del catálogo
  
  /// Crear categorías.
  CATEGORIA_CREAR,
  /// Editar categorías.
  CATEGORIA_EDITAR,
  /// Eliminar categorías.
  CATEGORIA_ELIMINAR,
  /// Crear autores.
  AUTOR_CREAR,
  /// Editar autores.
  AUTOR_EDITAR,
  /// Eliminar autores.
  AUTOR_ELIMINAR,
  /// Crear editoriales.
  EDITORIAL_CREAR,
  /// Editar editoriales.
  EDITORIAL_EDITAR,
  /// Eliminar editoriales.
  EDITORIAL_ELIMINAR,
  /// Crear libros con sus formatos.
  LIBRO_CREAR,
  /// Editar los datos de los libros y de sus formatos.
  LIBRO_EDITAR,
  /// Eliminar libros.
  LIBRO_ELIMINAR,
  
  // Permisos para gestionar las ventas y clientes
  
  /// Cambiar el tipo de un cliente (regular o premium).
  CLIENTE_EDITAR_TIPO,
  /// Eliminar clientes.
  CLIENTE_ELIMINAR,
  /// Editar el descuento de los clientes frecuentes.
  DESCUENTO_FRECUENTES_EDITAR,
  
  // Permisos para gestionar clientes
  
  /// Editar los datos personales del propio cliente.
  DATOS_PROPIOS_EDITAR,
  /// Agregar libros al carrito de compras y mantenerlo.
  CARRITO_GESTIONAR,
  /// Confirmar una compra y pagarla.
  COMPRA_REALIZAR,
  /// Calificar y comentar los libros comprados.
  CALIFICACION_CREAR,
  
  // Permisos para adminitrar las cuentas de usuarios
  
  /// Crear cuentas de usuario para los empleados.
  CUENTA_CREAR_EMPLEADO,
  /// Asignar roles a las cuentas de los empleados.
  CUENTA_ASIGNAR_ROLES,
  /// Habilitar o deshabilitar cuentas de usuario.
  CUENTA_HABILITAR_DESHABILITAR,
  /// Eliminar cuentas de usuario de empleados.
  CUENTA_ELIMINAR_EMPLEADO;
  
  /// Busca el permiso con el nombre dado.
  ///
  /// Si en la tabla `rol_permiso` hay un nombre de permiso que no está en este enum
  /// [Permiso], no se hace la conversión, y ese permiso no se concede porque el código Java
  /// no lo conoce.
  ///
  /// @param nombre Nombre de la constante que se guarda en la base de datos.
  /// @return El permiso con ese nombre. O, un valor vacío si no existe un permiso con ese
  ///         nombre. O, un valor vacío si el nombre del permiso es un valor nulo.
  public static Optional<Permiso> buscarPorNombre(String nombre) {
    // un nombre de permiso vacío, es un permiso vacío
    if (nombre == null) {
      return Optional.empty();
    }
    // buscar a cuál permiso corresponde el nombre del permiso que viene de la base de datos
    for (Permiso permiso : Permiso.values()) {
      if (permiso.name()
        .equals(nombre)) {
        // bien, el nombre del permiso coincide con una constante del enum Permiso
        return Optional.of(permiso);
      }
    }
    // no hubo coincidencias, luego ese permiso guardado en la base de datos no se concede
    return Optional.empty();
  }
}