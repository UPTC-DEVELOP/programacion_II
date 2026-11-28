package co.edu.uptc.tienda.interfaz;

import co.edu.uptc.tienda.modelo.Cliente;

public interface ISesionCliente {

    boolean iniciarSesion(String correo);

    void cerrarSesion();

    Cliente getClienteLogueado();
}
