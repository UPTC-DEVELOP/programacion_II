package co.edu.uptc.tienda.persistencia;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.excepcion.ClienteExcepcion;
import co.edu.uptc.tienda.interfaz.IPersistenciaCliente;
import co.edu.uptc.tienda.modelo.Cliente;

public class LocalCliente {

    private ArrayList<Cliente> clientes;
    private List<IPersistenciaCliente> persistencias;

    public LocalCliente() {
        this(crearPersistencias());
    }

    public LocalCliente(List<IPersistenciaCliente> persistencias) {
        this.persistencias = persistencias;
        clientes = new ArrayList<>();
        cargar();
    }

    private static List<IPersistenciaCliente> crearPersistencias() {
        List<IPersistenciaCliente> persistencias = new ArrayList<>();
        persistencias.add(new ClienteBD());
        persistencias.add(new ClienteJson("datos/clientes.json"));
        return persistencias;
    }

    public void guardar(Cliente cliente) {
        clientes.add(cliente);
        persistir();
    }

    public ArrayList<Cliente> listar() {
        return clientes;
    }

    public Cliente buscarPorId(String id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId().equals(id)) {
                return cliente;
            }
        }

        return null;
    }

    public void actualizar(Cliente cliente) {
        Cliente clienteExistente = buscarPorId(cliente.getId());

        if (clienteExistente != null) {
            clienteExistente.setNombreCompleto(cliente.getNombreCompleto());
            clienteExistente.setCorreoElectronico(cliente.getCorreoElectronico());
            clienteExistente.setDireccionEnvio(cliente.getDireccionEnvio());
            clienteExistente.setCelular(cliente.getCelular());
            clienteExistente.setTipoCliente(cliente.getTipoCliente());
            persistir();
        }
    }

    public void eliminar(String id) {
        Cliente cliente = buscarPorId(id);

        if (cliente != null) {
            clientes.remove(cliente);
            persistir();
        }
    }

    private void cargar() {
        for (IPersistenciaCliente persistencia : persistencias) {
            try {
                clientes.addAll(persistencia.leer());
                return;
            } catch (ClienteExcepcion e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void persistir() {
        int fallos = 0;
        String mensaje = "";
        for (IPersistenciaCliente persistencia : persistencias) {
            try {
                persistencia.guardar(clientes);
            } catch (ClienteExcepcion e) {
                System.out.println(e.getMessage());
                fallos++;
                mensaje = e.getMessage();
            }
        }
        if (fallos == persistencias.size()) {
            throw new IllegalArgumentException(mensaje);
        }
    }
}
