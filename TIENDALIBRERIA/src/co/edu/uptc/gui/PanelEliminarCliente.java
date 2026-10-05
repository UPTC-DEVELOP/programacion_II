package co.edu.uptc.gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.negocio.ClienteConfig;
import co.edu.uptc.negocio.GestionCliente;

public class PanelEliminarCliente extends JFrame {
    private JTextField campoCorreo;
    private JTextField campoCedula;
    private JTextField campoNombre;
    private JTextField campoTelefono;
    private GestionCliente gestionCliente;
    private Cliente clienteEncontrado;
    public PanelEliminarCliente() {

        ClienteConfig clienteConfig = ClienteConfig.getInstancia();
        gestionCliente = clienteConfig.getGestionCliente();

        setTitle("Eliminar Cliente");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        panel.add(new JLabel("Correo:"));
        campoCorreo = new JTextField();
        panel.add(campoCorreo);

        JButton botonBuscar = new JButton("Buscar");
        panel.add(botonBuscar);
        panel.add(new JLabel(""));

        panel.add(new JLabel("Cédula:"));
        campoCedula = new JTextField();
        campoCedula.setEditable(false);
        panel.add(campoCedula);

        panel.add(new JLabel("Nombre:"));
        campoNombre = new JTextField();
        campoNombre.setEditable(false);
        panel.add(campoNombre);

        panel.add(new JLabel("Teléfono:"));
        campoTelefono = new JTextField();
        campoTelefono.setEditable(false);
        panel.add(campoTelefono);

        JButton botonEliminar = new JButton("Eliminar cliente");
        

        panel.add(botonEliminar);
        

        JButton botonVolver = new JButton("Volver");
        panel.add(botonVolver);
        panel.add(new JLabel(""));

        add(panel);

        botonBuscar.addActionListener(e -> buscarCliente());

        botonEliminar.addActionListener(e -> eliminarCliente());

        botonVolver.addActionListener(e -> {
            new PanelCentral().setVisible(true);
            dispose();
        });
    }

    private void buscarCliente() {

        String correo = campoCorreo.getText().trim();

        clienteEncontrado = null;

        for (Cliente cliente : gestionCliente.listarClientes()) {

            if (cliente.getCorreo().equals(correo)) {

                clienteEncontrado = cliente;

                campoCedula.setText(cliente.getCedula());

                campoNombre.setText(
                        cliente.getNombre() + " " + cliente.getApellido()
                );

                campoTelefono.setText(cliente.getTelefono());

                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Cliente no encontrado"
        );

        limpiarResultados();
    }

    private void eliminarCliente() {

        if (clienteEncontrado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero busque un cliente"
            );

            return;
        }

        gestionCliente.eliminarCliente(
                clienteEncontrado.getCorreo()
        );

        JOptionPane.showMessageDialog(
                this,
                "Cliente eliminado correctamente"
        );

        limpiar();
    }

    private void limpiar() {

        campoCorreo.setText("");
        limpiarResultados();
        clienteEncontrado = null;
    }

    private void limpiarResultados() {

        campoCedula.setText("");
        campoNombre.setText("");
        campoTelefono.setText("");
    }
}