package co.uptc.edu.negocio;

public class Cliente {
	
	// Atributos según el diagrama de clases (Negocio)
    private int idCliente;
    private String nombreCompleto;
    private String correoElectronico;
    private String direccion;
    private String telefono;
    private String tipoCliente;

    // Constructor vacío
    public Cliente() {
    }

    // Constructor completo para la GUI
    public Cliente(String nombreCompleto, String correoElectronico, String direccion, String telefono, String tipoCliente) {
        this.nombreCompleto = nombreCompleto;
        this.correoElectronico = correoElectronico;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipoCliente = tipoCliente;
    }

public void registrar() {
    // Lógica de simulación para persistir/guardar los datos
    System.out.println("====== CLIENTE REGISTRADO EN EL SISTEMA ======");
    System.out.println("ID Asignado: " + (int)(Math.random() * 1000));
    System.out.println("Nombre: " + this.nombreCompleto);
    System.out.println("Correo: " + this.correoElectronico);
    System.out.println("Dirección: " + this.direccion);
    System.out.println("Teléfono: " + this.telefono);
    System.out.println("Tipo de Cliente: " + this.tipoCliente);
    System.out.println("==============================================");
}
public void actualizarDatos() {
    System.out.println("Datos del cliente " + nombreCompleto + " actualizados.");
}

public boolean iniciarSesion() {
    return true;
}

public String verHistorialCompras() {
    return "Historial de compras disponible para: " + nombreCompleto;
}
//Getters y Setters
public int getIdCliente() { return idCliente; }
public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

public String getNombreCompleto() { return nombreCompleto; }
public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

public String getCorreoElectronico() { return correoElectronico; }
public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

public String getDireccion() { return direccion; }
public void setDireccion(String direccion) { this.direccion = direccion; }

public String getTelefono() { return telefono; }
public void setTelefono(String telefono) { this.telefono = telefono; }

public String getTipoCliente() { return tipoCliente; }
public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
}

