package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Cliente;
import model.ClientePremium;
import service.IServicioClientes;

/**
 * Manejador de eventos de la ventana de gestion de clientes.
 */
public class EventosClientes extends Eventos {

    private final IServicioClientes controlador;

    public EventosClientes(VentanaClientes vista, IServicioClientes controlador) {
        super(vista);
        this.controlador = controlador;
    }

    public void registrarEventos(JTable tabla, DefaultTableModel modeloTabla,
            JButton btnAtras, JButton btnEliminar, JFrame padre) {
        btnAtras.addActionListener(e -> volver(padre));
        btnEliminar.addActionListener(e -> eliminarCliente(tabla, modeloTabla));

        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (padre != null) {
                    padre.setVisible(true);
                }
            }
        });

        cargarTabla(modeloTabla);
    }

    public void cargarTabla(DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0);
        for (Cliente cliente : controlador.listarUltimos(10)) {
            modeloTabla.addRow(new Object[] {
                    cliente.getNombreCompleto(),
                    cliente.getCorreo(),
                    cliente.getDireccion(),
                    cliente.getTelefono(),
                    (cliente instanceof ClientePremium) ? "Premium" : "Regular"
            });
        }
    }

    private void eliminarCliente(JTable tabla, DefaultTableModel modeloTabla) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un cliente de la tabla.");
            return;
        }

        String correo = (String) modeloTabla.getValueAt(fila, 1);
        if (confirmar("\u00bfDesea eliminar el cliente " + correo + "?")) {
            if (controlador.eliminar(correo)) {
                mostrarInfo("Cliente eliminado correctamente.");
                cargarTabla(modeloTabla);
            } else {
                mostrarError("No se pudo eliminar el cliente. Verifique el almacenamiento.");
            }
        }
    }
}
