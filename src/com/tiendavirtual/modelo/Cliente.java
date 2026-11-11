package com.tiendavirtual.modelo;
public class Cliente {

	private String tipoDocumento;
	private int numeroDocumento;
	private String nombre;
	private String apellido;
	private String correo;
	private String telefono;
	private String contrasenia;
	
		public Cliente(String tipoDocumento, int numeroDocumento, String nombre, String apellido, String correo,
			String telefono, String contrasenia) {
		super();
		this.tipoDocumento = tipoDocumento;
		this.numeroDocumento = numeroDocumento;
		this.nombre = nombre;
		this.apellido = apellido;
		this.correo = correo;
		this.telefono = telefono;
		this.contrasenia = contrasenia;
	}

		public String getTipoDocumento() {
			return tipoDocumento;
		}

		public void setTipoDocumento(String tipoDocumento) {
			this.tipoDocumento = tipoDocumento;
		}

		public int getNumeroDocumento() {
			return numeroDocumento;
		}

		public void setNumeroDocumento(int numeroDocumento) {
			this.numeroDocumento = numeroDocumento;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getApellido() {
			return apellido;
		}

		public void setApellido(String apellido) {
			this.apellido = apellido;
		}

		public String getCorreo() {
			return correo;
		}

		public void setCorreo(String correo) {
			this.correo = correo;
		}

		public String getTelefono() {
			return telefono;
		}

		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}

		public String getContrasenia() {
			return contrasenia;
		}

		public void setContrasenia(String contrasenia) {
			this.contrasenia = contrasenia;
		}
	
		
}
