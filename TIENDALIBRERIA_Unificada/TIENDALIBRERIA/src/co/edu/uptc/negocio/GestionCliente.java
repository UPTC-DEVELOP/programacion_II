package co.edu.uptc.negocio;

import java.util.List;

import co.edu.uptc.interfaces.IGestionCliente;
import co.edu.uptc.modelo.Cliente;

public class GestionCliente {

    private IGestionCliente cliente;

    public GestionCliente(IGestionCliente cliente) {

        super();

        this.cliente = cliente;
    }

    public void guardarCliente(Cliente clienteNuevo) {

        validarCliente(clienteNuevo);

        for (Cliente existente : cliente.listar()) {

            if (existente.getCorreo().equals(clienteNuevo.getCorreo())) {

                throw new IllegalArgumentException(
                        "Ya existe un cliente con el correo: "
                        + clienteNuevo.getCorreo());
            }

            if (existente.getCedula().equals(clienteNuevo.getCedula())) {

                throw new IllegalArgumentException(
                        "Ya existe un cliente con la cédula: "
                        + clienteNuevo.getCedula());
            }
        }

        cliente.guardar(clienteNuevo);
    }

    public void actualizarCliente(Cliente clienteNuevo) {

        validarCliente(clienteNuevo);

        boolean existe = false;

        for (Cliente existente : cliente.listar()) {

            if (existente.getCorreo().equals(clienteNuevo.getCorreo())) {

                existe = true;

            } else if (existente.getCedula().equals(clienteNuevo.getCedula())) {

                throw new IllegalArgumentException(
                        "La cédula ya pertenece a otro cliente: "
                        + clienteNuevo.getCedula());
            }
        }

        if (!existe) {

            throw new IllegalArgumentException(
                    "No existe un cliente con el correo: "
                    + clienteNuevo.getCorreo());
        }

        cliente.actualizar(clienteNuevo);
    }

    public void eliminarCliente(String correo) {

        cliente.eliminar(correo);
    }

    public List<Cliente> listarClientes() {

        return cliente.listar();
    }

    // Busca por correo o por cédula; devuelve null si no existe
    public Cliente buscarCliente(String criterio) {

        if (esVacio(criterio)) {

            return null;
        }

        String clave = criterio.trim();

        for (Cliente existente : cliente.listar()) {

            if (existente.getCorreo().equals(clave)
                    || existente.getCedula().equals(clave)) {

                return existente;
            }
        }

        return null;
    }

    private void validarCliente(Cliente c) {

        if (c == null || esVacio(c.getCedula()) || esVacio(c.getNombre())
                || esVacio(c.getApellido()) || esVacio(c.getCorreo())) {

            throw new IllegalArgumentException(
                    "Cédula, nombre, apellido y correo son obligatorios.");
        }

        if (!c.getCorreo().contains("@")) {

            throw new IllegalArgumentException(
                    "El correo no tiene un formato válido.");
        }
    }

    private boolean esVacio(String texto) {

        return texto == null || texto.trim().isEmpty();
    }
}
