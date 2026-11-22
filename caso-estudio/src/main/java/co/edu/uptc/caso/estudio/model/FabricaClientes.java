package co.edu.uptc.caso.estudio.model;

public class FabricaClientes {

    private FabricaClientes() {}

    public static Cliente crear(String id, String nombreCompleto, String correo, String direccionEnvio,
                                String departamento, String ciudad, String telefono, String tipoCliente) {
        if ("PREMIUM".equalsIgnoreCase(tipoCliente)) {
            return new ClientePremium(id, nombreCompleto, correo, direccionEnvio, departamento, ciudad, telefono);
        }
        return new ClienteRegular(id, nombreCompleto, correo, direccionEnvio, departamento, ciudad, telefono);
    }

    public static Cliente crear(String id, String nombreCompleto, String correo, String direccionEnvio,
                                String departamento, String ciudad, String telefono, String tipoCliente,
                                String contrasenaHash) {
        Cliente cliente = crear(id, nombreCompleto, correo, direccionEnvio, departamento, ciudad, telefono, tipoCliente);
        cliente.setContrasenaHash(contrasenaHash);
        return cliente;
    }
}