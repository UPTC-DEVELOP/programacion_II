package co.edu.uptc.tienda.persistencia;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.excepcion.ClienteExcepcion;
import co.edu.uptc.tienda.interfaz.IPersistenciaCliente;
import co.edu.uptc.tienda.modelo.Cliente;

public class LocalCliente {

    private ArrayList<Cliente> clientes;
    private List<IPersistenciaCliente> persistencias;
    private String avisoCarga;

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

    public String getAvisoCarga() {
        return avisoCarga;
    }

    private void cargar() {
        List<String> errores = new ArrayList<>();
        for (IPersistenciaCliente persistencia : persistencias) {
            try {
                for (Cliente cliente : persistencia.leer()) {
                    if (buscarPorId(cliente.getId()) == null) {
                        clientes.add(cliente);
                    }
                }
            } catch (ClienteExcepcion e) {
                errores.add(e.getMessage());
            }
        }
        if (!errores.isEmpty()) {
            avisoCarga = String.join("\n", errores);
        }
    }

    private void persistir() {
        List<String> errores = new ArrayList<>();
        for (IPersistenciaCliente persistencia : persistencias) {
            try {
                persistencia.guardar(clientes);
            } catch (ClienteExcepcion e) {
                errores.add(e.getMessage());
            }
        }
        if (errores.size() == persistencias.size()) {
            throw new IllegalArgumentException("El cambio quedó en el programa, pero no se guardó en ninguna parte:\n"
                    + String.join("\n", errores));
        }
        if (!errores.isEmpty()) {
            throw new IllegalArgumentException("El cambio se guardó, pero no en todas partes:\n"
                    + String.join("\n", errores));
        }
    }
}
