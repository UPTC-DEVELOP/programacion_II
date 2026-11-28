package co.edu.uptc.tienda.negocio;

import java.util.ArrayList;

import co.edu.uptc.tienda.interfaz.ISesionCliente;
import co.edu.uptc.tienda.modelo.Cliente;

public class ClienteConfig implements ISesionCliente {

    private GestionCliente gestionCliente;

    public ClienteConfig() {
        gestionCliente = new GestionCliente();
    }

    public String getAvisoCarga() {
        return gestionCliente.getAvisoCarga();
    }

    public void guardar(Cliente cliente) {
        gestionCliente.guardar(cliente);
    }

    public ArrayList<Cliente> listar() {
        return gestionCliente.listar();
    }

    public Cliente buscarPorId(String id) {
        return gestionCliente.buscarPorId(id);
    }

    public void actualizar(Cliente cliente) {
        gestionCliente.actualizar(cliente);
    }

    public void eliminar(String id) {
        gestionCliente.eliminar(id);
    }

    @Override
    public boolean iniciarSesion(String correo) {
        return gestionCliente.iniciarSesion(correo);
    }

    @Override
    public void cerrarSesion() {
        gestionCliente.cerrarSesion();
    }

    @Override
    public Cliente getClienteLogueado() {
        return gestionCliente.getClienteLogueado();
    }
}