package co.edu.uptc.tienda.modelo;


public abstract class Persona {
    protected String primerNombre;
    protected String otrosNombres;
    protected String primerApellido;
    protected String otrosApellidos;
    protected String tipoIdentificacion;
    protected String identificacion;
    protected String correoElectronico;
    protected String celular;
    protected String direccion;
    
    
    
    //Metodo constructor
	public Persona(String primerNombre, String otrosNombres, String primerApellido, String otrosApellidos,
			String tipoIdentificacion, String identificacion, String correoElectronico, String celular,
			String direccion) {
		super();
		this.primerNombre = primerNombre;
		this.otrosNombres = otrosNombres;
		this.primerApellido = primerApellido;
		this.otrosApellidos = otrosApellidos;
		this.tipoIdentificacion = tipoIdentificacion;
		this.identificacion = identificacion;
		this.correoElectronico = correoElectronico;
		this.celular = celular;
		this.direccion = direccion;
	}

	//Get y Set

	public String getPrimerNombre() {
		return primerNombre;
	}



	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}



	public String getOtrosNombres() {
		return otrosNombres;
	}



	public void setOtrosNombres(String otrosNombres) {
		this.otrosNombres = otrosNombres;
	}



	public String getPrimerApellido() {
		return primerApellido;
	}



	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}



	public String getOtrosApellidos() {
		return otrosApellidos;
	}



	public void setOtrosApellidos(String otrosApellidos) {
		this.otrosApellidos = otrosApellidos;
	}



	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}



	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}



	public String getIdentificacion() {
		return identificacion;
	}



	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}



	public String getCorreoElectronico() {
		return correoElectronico;
	}



	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}



	public String getCelular() {
		return celular;
	}



	public void setCelular(String celular) {
		this.celular = celular;
	}



	public String getDireccion() {
		return direccion;
	}



	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

		
	//  mostrar el nombre completo 
    public String getNombreCompleto() {
        String nombres = primerNombre + (otrosNombres != null && !otrosNombres.isEmpty() ? " " + otrosNombres : "");
        String apellidos = primerApellido + (otrosApellidos != null && !otrosApellidos.isEmpty() ? " " + otrosApellidos : "");
        return nombres + " " + apellidos;
    }
}