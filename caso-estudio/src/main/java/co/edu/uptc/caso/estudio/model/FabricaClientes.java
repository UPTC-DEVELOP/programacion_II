package co.edu.uptc.caso.estudio.model;

// Principios O y L (SOLID): decide si crear un ClienteRegular o ClientePremium usando herencia y polimorfismo.
public class FabricaClientes {

    private FabricaClientes() {
    }

    // Instancia ClientePremium para tipos PREMIUM/VIP y ClienteRegular en caso contrario
    public static Cliente crear(String id, String nombreCompleto, String correo, String direccionEnvio,
                              String departamento, String ciudad, String telefono, String tipoCliente) {
        if ("PREMIUM".equalsIgnoreCase(tipoCliente) || "VIP".equalsIgnoreCase(tipoCliente)) {
            return new ClientePremium(id, nombreCompleto, correo, direccionEnvio,
                    departamento, ciudad, telefono);
        }
        return new ClienteRegular(id, nombreCompleto, correo, direccionEnvio,
                departamento, ciudad, telefono);
    }

    // Sobrecarga que crea el cliente y asigna su hash de contraseña
    public static Cliente crear(String id, String nombreCompleto, String correo, String direccionEnvio,
                              String departamento, String ciudad, String telefono, String tipoCliente,
                              String contrasenaHash) {
        Cliente cliente = crear(id, nombreCompleto, correo, direccionEnvio,
                departamento, ciudad, telefono, tipoCliente);
        cliente.setContrasenaHash(contrasenaHash);
        return cliente;
    }
}