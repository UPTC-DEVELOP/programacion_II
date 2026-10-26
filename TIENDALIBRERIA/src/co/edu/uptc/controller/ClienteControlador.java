package co.edu.uptc.controller;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import co.edu.uptc.gui.DialogoCentralCliente;
import co.edu.uptc.gui.DialogoCrearCliente;
import co.edu.uptc.gui.DialogoEditarCliente;
import co.edu.uptc.gui.PanelPadreCliente;
import co.edu.uptc.interfaces.IGestionCliente;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.util.Comandos;

public class ClienteControlador {

    private final IGestionCliente gestionCliente;
    private final PanelPadreCliente vista;
    private final Component padre;
    private final ActionListener evento;
    private DialogoCentralCliente dialogoCliente;

    public ClienteControlador(IGestionCliente gestionCliente, PanelPadreCliente vista, Component padre, Evento evento) {
        this.gestionCliente = gestionCliente;
        this.vista = vista;
        this.padre = padre;
        this.evento = evento;

        evento.registrar(Comandos.CREAR_CLIENTE, this::lanzarDialogoCliente);
        evento.registrar(Comandos.CANCELAR_CREACION_CLIENTE, this::cerrarDialogoCliente);
        evento.registrar(Comandos.CANCELAR_CLIENTE, this::cerrarDialogoCliente);
        evento.registrar(Comandos.GUARDAR_CLIENTE, this::crearCliente);
        evento.registrar(Comandos.ACTUALIZAR_CLIENTE, this::actualizarCliente);
        evento.registrar(Comandos.GUARDAR_ACTUALIZACION_CLIENTE, this::guardarActualizacionCliente);
        evento.registrar(Comandos.ELIMINAR_CLIENTE, this::eliminarCliente);
        evento.registrar(Comandos.VER_CLIENTE, this::verCliente);
        evento.registrar(Comandos.BUSCAR_CLIENTE, this::buscarCliente);
        evento.registrar(Comandos.LIMPIAR_CLIENTE, this::limpiarTablaClientes);
    }

    public void lanzarDialogoCliente() {
        dialogoCliente = new DialogoCrearCliente(evento, "Crear Cliente", true);
        dialogoCliente.setVisible(true);
    }

    public void cerrarDialogoCliente() {
        if (dialogoCliente != null) {
            dialogoCliente.dispose();
            dialogoCliente = null;
        }
    }

    public void crearCliente() {
        if (dialogoCliente != null) {
            try {
                gestionCliente.guardarCliente(dialogoCliente.capturarDatos());
                cerrarDialogoCliente();
                refrescarTablaClientes();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(padre, "Error al crear el cliente: " + e.getMessage());
            }
        }
    }

    public void actualizarCliente() {
        String cedula = vista.getClienteSeleccionadoCedula();
        if (cedula == null) {
            cedula = JOptionPane.showInputDialog(padre, "Ingrese la cédula del cliente que desea actualizar:");
        }

        if (cedula != null && !cedula.trim().isEmpty()) {
            Cliente clienteEncontrado = gestionCliente.buscarClientePorCedula(cedula);

            if (clienteEncontrado != null) {
                DialogoEditarCliente editarDialogo = new DialogoEditarCliente(evento, "Actualizar Cliente", false);
                editarDialogo.cargarDatosCliente(clienteEncontrado);

                dialogoCliente = editarDialogo;
                dialogoCliente.setVisible(true); // Se abre el dialogo de forma modal
            } else {
                JOptionPane.showMessageDialog(padre, "No se encontró ningún cliente con la cédula: " + cedula);
            }
        }
    }

    public void guardarActualizacionCliente() {
        if (dialogoCliente != null) {
            try {
                gestionCliente.actualizarCliente(dialogoCliente.capturarDatos());
                cerrarDialogoCliente();
                refrescarTablaClientes();
                JOptionPane.showMessageDialog(padre, "¡Cliente actualizado exitosamente!");
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(padre, "Error al actualizar: " + e.getMessage());
            }
        }
    }

    public void eliminarCliente() {
        String correo = vista.getClienteSeleccionadoCorreo();
        if (correo == null) {
            JOptionPane.showMessageDialog(padre, "Seleccione un cliente para eliminar");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(padre,
                "¿Desea eliminar al cliente con correo " + correo + "?",
                "Eliminar cliente", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            gestionCliente.eliminarCliente(correo);
            refrescarTablaClientes();
        }
    }

    public void verCliente() {
        String correo = vista.getClienteSeleccionadoCorreo();
        if (correo == null) {
            JOptionPane.showMessageDialog(padre, "Seleccione un cliente para ver");
            return;
        }

        Cliente cliente = gestionCliente.buscarCliente(correo);
        if (cliente != null) {
            JOptionPane.showMessageDialog(padre, cliente.toString());
        }
    }

    public void buscarCliente() {
        String criterio = JOptionPane.showInputDialog(padre, "Ingrese el correo o la cédula a buscar:");
        if (criterio != null && !criterio.trim().isEmpty()) {
            Cliente cliente = gestionCliente.buscarCliente(criterio);
            if (cliente != null) {
                vista.seleccionarClientePorCorreo(cliente.getCorreo());
                JOptionPane.showMessageDialog(padre, cliente.toString());
            } else {
                JOptionPane.showMessageDialog(padre, "Cliente no encontrado.");
            }
        }
    }

    // Restablece la tabla: quita la seleccion y vuelve a cargar los clientes
    public void limpiarTablaClientes() {
        refrescarTablaClientes();
    }

    public void refrescarTablaClientes() {
        vista.poblarTabla(gestionCliente.listarClientes());
    }
}
