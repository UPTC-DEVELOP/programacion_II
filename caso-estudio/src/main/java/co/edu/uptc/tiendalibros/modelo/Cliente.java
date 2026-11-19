
package co.edu.uptc.tiendalibros.modelo;

public class Cliente extends ElementoBase {

  private String tipoDocumento;
  private String numeroDocumento;
  private String nombres;
  private String apellidos;
  private String correoElectronico;
  private String telefono;
  private String direccionEnvio;
  private String tipoCliente;

  public Cliente() {
    this("", "", "", "", "", "", "", "");
  }

  public Cliente(
    String tipoDocumento,
    String numeroDocumento,
    String nombres,
    String apellidos,
    String correoElectronico,
    String telefono,
    String direccionEnvio,
    String tipoCliente
  ) {
    this(null, tipoDocumento, numeroDocumento, nombres, apellidos,
      correoElectronico, telefono, direccionEnvio, tipoCliente);
  }

  public Cliente(
    Long id,
    String tipoDocumento,
    String numeroDocumento,
    String nombres,
    String apellidos,
    String correoElectronico,
    String telefono,
    String direccionEnvio,
    String tipoCliente
  ) {
    super(id);
    this.tipoDocumento = tipoDocumento;
    this.numeroDocumento = numeroDocumento;
    this.nombres = nombres;
    this.apellidos = apellidos;
    this.correoElectronico = correoElectronico;
    this.telefono = telefono;
    this.direccionEnvio = direccionEnvio;
    this.tipoCliente = tipoCliente;
  }

  public String getTipoDocumento() {
    return this.tipoDocumento;
  }

  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public String getNumeroDocumento() {
    return this.numeroDocumento;
  }

  public void setNumeroDocumento(String numeroDocumento) {
    this.numeroDocumento = numeroDocumento;
  }

  public String getNombres() {
    return this.nombres;
  }

  public void setNombres(String nombres) {
    this.nombres = nombres;
  }

  public String getApellidos() {
    return this.apellidos;
  }

  public void setApellidos(String apellidos) {
    this.apellidos = apellidos;
  }

  public String getCorreoElectronico() {
    return this.correoElectronico;
  }

  public void setCorreoElectronico(String correoElectronico) {
    this.correoElectronico = correoElectronico;
  }

  public String getTelefono() {
    return this.telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getDireccionEnvio() {
    return this.direccionEnvio;
  }

  public void setDireccionEnvio(String direccionEnvio) {
    this.direccionEnvio = direccionEnvio;
  }

  public String getTipoCliente() {
    return this.tipoCliente;
  }

  public void setTipoCliente(String tipoCliente) {
    this.tipoCliente = tipoCliente;
  }
}
