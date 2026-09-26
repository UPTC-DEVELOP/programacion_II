package co.uptc.edu.negocio;

public class Administrador {
	
	    private int idAdministrador;
	    private String nombre;
	    private String correoElectronico;
	    private String usuario;
	    private String password;

	    public Administrador() {}

	    public Administrador(int idAdministrador, String nombre, String correoElectronico, String usuario, String password) {
	        this.idAdministrador = idAdministrador;
	        this.nombre = nombre;
	        this.correoElectronico = correoElectronico;
	        this.usuario = usuario;
	        this.password = password;
	    }

	    // Métodos del diagrama de negocio
	    public void gestionarLibros() {
	        System.out.println("Accediendo a la gestión de libros...");
	    }

	    public void gestionarClientes() {
	        System.out.println("Accediendo a la gestión de clientes...");
	    }

	    public void gestionarVentas() {
	        System.out.println("Accediendo a la gestión de ventas...");
	    }

	    // Getters y Setters
	    public int getIdAdministrador() { return idAdministrador; }
	    public void setIdAdministrador(int idAdministrador) { this.idAdministrador = idAdministrador; }

	    public String getNombre() { return nombre; }
	    public void setNombre(String nombre) { this.nombre = nombre; }

	    public String getCorreoElectronico() { return correoElectronico; }
	    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

	    public String getUsuario() { return usuario; }
	    public void setUsuario(String usuario) { this.usuario = usuario; }

	    public String getPassword() { return password; }
	    public void setPassword(String password) { this.password = password; }
}
