/// 
package co.edu.uptc.tiendalibros.modelo;

/// Representa un rol que puede tener un usuario del sistema.
///
/// Un usuario puede tener varios roles. Los permisos de un usuario son el conjunto de
/// sus roles.
public enum Rol {
  
  /// Gestiona la información de los libros del catálogo, excepto los precios.
  /// Abreviatura: CAT.
  GESTOR_CATALOGO("CAT", "Gestor de Catálogo"),
  
  /// Gestiona los precios de los libros, el tipo de cliente y el descuento.
  /// Abreviatura: VEN.
  GESTOR_VENTAS("VEN", "Gestor de Ventas"),
  
  /// Explora el catálogo, arma su carrito y hace sus compras. Abreviatura: CLI.
  CLIENTE("CLI", "Cliente"),
  
  /// Administra las cuentas de usuario de los empleados. Abreviatura: ADM.
  ADMINISTRADOR("ADM", "Administrador");
  
  /// Abreviatura quer se usa para representar el rol en la base de datos.
  private final String abreviatura;
  
  /// Nombre del rol para mostrarlo a las usuarios en la interfaz gráfica.
  private final String nombreVisible;
  
  /// Crea un nuevo objeto del enum [Rol].
  ///
  /// @param abreviatura   Abreviatura quer se usa para representar el rol en la base de
  ///                      datos.
  /// @param nombreVisible Nombre del rol para mostrarlo en la interfaz gráfica.
  private Rol(
    String abreviatura,
    String nombreVisible
  ) {
    this.abreviatura = abreviatura;
    this.nombreVisible = nombreVisible;
  }
  
  /// Obtiene la abreviatura que se usa para representar el rol en la base de datos.
  ///
  /// @return Abreviatura del rol.
  public String getAbreviatura() { return this.abreviatura; }
  
  /// Obtiene el nombre del rol para mostrarlo en la interfaz gráfica.
  ///
  /// @return Nombre visible del rol.
  public String getNombreVisible() { return this.nombreVisible; }
  
  /// Convierte la abreviatura guardada en la base de datos a un [Rol].
  ///
  /// @param valor Abreviatura que se quiere convertir.
  /// @return El [Rol] correspondiente a la abreviatura.
  /// @throws IllegalArgumentException Si la abreviatura no corresponde a ningún rol.
  public static Rol convertirAbreviatura(String valor) {
    // mensaje de error para la excepción de conversión
    String mensaje = String
      .format("No se pudo convertir el valor '%s' a un rol válido.", valor);
    
    // no se puede convertir nulos
    if (valor == null) {
      throw new IllegalArgumentException(mensaje);
    }
    
    // intentar convertir el valor dado
    return switch (valor.toUpperCase()) {
      case "CAT" -> Rol.GESTOR_CATALOGO;
      case "VEN" -> Rol.GESTOR_VENTAS;
      case "CLI" -> Rol.CLIENTE;
      case "ADM" -> Rol.ADMINISTRADOR;
      default    ->
        throw new IllegalArgumentException(mensaje);
    };
  }
}
