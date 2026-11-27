package co.edu.uptc.tiendalibros.modelo;

/// Representa a un empleado de la tienda.
/// 
/// Es una persona que tiene una cuenta de usuario como administrador, gestor de catálogo o
/// gestor de ventas.
/// 
/// Esta persona no es un cliente.
public class Empleado extends ElementoBase {
  
  /// ID de la cuenta de usuario del empleado.
  private final Long idUsuario;
  
  /// Tipo de documento de identidad.
  private final String tipoDocumento;
  
  /// Número del documento de identidad.
  private final String numeroDocumento;
  
  /// Nombres del empleado.
  private final String nombres;
  
  /// Apellidos del empleado.
  private final String apellidos;
  
  /// Crea un empleado nuevo sin ID.
  ///
  /// @param idUsuario       ID de la cuenta de usuario del empleado.
  /// @param tipoDocumento   Tipo de documento de identidad.
  /// @param numeroDocumento Número del documento de identidad.
  /// @param nombres         Nombres del empleado.
  /// @param apellidos       Apellidos del empleado.
  public Empleado(
    Long idUsuario,
    String tipoDocumento,
    String numeroDocumento,
    String nombres,
    String apellidos
  ) {
    this(null, idUsuario, tipoDocumento, numeroDocumento, nombres, apellidos);
  }
  
  /// Crea un empleado con todos sus datos.
  ///
  /// @param id              ID del empleado.
  /// @param idUsuario       ID de la cuenta de usuario del empleado.
  /// @param tipoDocumento   Tipo de documento de identidad.
  /// @param numeroDocumento Número del documento de identidad.
  /// @param nombres         Nombres del empleado.
  /// @param apellidos       Apellidos del empleado.
  public Empleado(
    Long id,
    Long idUsuario,
    String tipoDocumento,
    String numeroDocumento,
    String nombres,
    String apellidos
  ) {
    super(id);
    this.idUsuario = idUsuario;
    this.tipoDocumento = tipoDocumento;
    this.numeroDocumento = numeroDocumento;
    this.nombres = nombres;
    this.apellidos = apellidos;
  }
  
  /// Obtiene el ID del usuario del empleado.
  ///
  /// @return ID del usuario del empleado.
  public Long getIdUsuario() { return this.idUsuario; }
  
  /// Obtiene el tipo de documento de identidad.
  ///
  /// @return Tipo de documento.
  public String getTipoDocumento() { return this.tipoDocumento; }
  
  /// Obtiene el número del documento de identidad.
  ///
  /// @return Número de documento.
  public String getNumeroDocumento() { return this.numeroDocumento; }
  
  /// Obtiene los nombres del empleado.
  ///
  /// @return Nombres del empleado.
  public String getNombres() { return this.nombres; }
  
  /// Obtiene los apellidos del empleado.
  ///
  /// @return Apellidos del empleado.
  public String getApellidos() { return this.apellidos; }
  
  /// Muestra solo el ID: los datos personales no deben llegar al log.
  @Override
  public String toString() {
    return String.format("Empleado[id=%d]", getId());
  }
}