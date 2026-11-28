package co.edu.uptc.negocio;

public class SesionService {

    private Cliente clienteAutenticado;

    public void iniciarSesion(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        clienteAutenticado = cliente;
    }

    public void cerrarSesion() {
        clienteAutenticado = null;
    }

    public boolean estaAutenticado() {
        return clienteAutenticado != null;
    }

    public Cliente getClienteAutenticado() {
        return clienteAutenticado;
    }

    public void exigirAutenticacion() throws ValidacionException {
        if (!estaAutenticado()) {
            throw new ValidacionException("Debe iniciar sesión antes de realizar la compra.");
        }
    }
}
