package co.edu.uptc.modelo.dto;

import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;

/**
 * DTO de PERSISTENCIA de un cliente: representa un registro del archivo
 * data/clientes.json.
 *
 * <p>Se usa en lugar de la entidad Cliente porque Cliente es abstracta
 * (ClientePremium / ClienteRegular) y Gson no puede instanciarla. Además
 * guarda datos que ClienteDto no expone a la GUI: fecha de registro e
 * intentos fallidos.</p>
 *
 * <ul>
 *   <li>contrasenia: hash SHA-256, nunca el texto plano.</li>
 *   <li>fechaRegistro: fecha y hora ISO-8601 (por ejemplo 2026-11-06T22:15:30).</li>
 * </ul>
 */
public class ClientePersistenciaDto {

    private int idCliente;
    private String primerNombre;
    private String otrosNombres;
    private String primerApellido;
    private String otrosApellidos;
    private String tipoIdentificacion;
    private String identificacion;
    private String correoElectronico;
    private String celular;
    private String direccion;
    private TipoCliente tipoCliente;
    private Rol rol;
    private String contrasenia;
    private String fechaRegistro;
    private int intentosFallidos;

    public ClientePersistenciaDto() {
    }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }
    public String getOtrosNombres() { return otrosNombres; }
    public void setOtrosNombres(String otrosNombres) { this.otrosNombres = otrosNombres; }
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }
    public String getOtrosApellidos() { return otrosApellidos; }
    public void setOtrosApellidos(String otrosApellidos) { this.otrosApellidos = otrosApellidos; }
    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }
    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public TipoCliente getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(TipoCliente tipoCliente) { this.tipoCliente = tipoCliente; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public int getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(int intentosFallidos) { this.intentosFallidos = intentosFallidos; }
}
