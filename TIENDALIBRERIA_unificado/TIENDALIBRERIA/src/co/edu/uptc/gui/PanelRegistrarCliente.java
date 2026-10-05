package co.edu.uptc.gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.negocio.ClienteConfig;
import co.edu.uptc.negocio.GestionCliente;

public class PanelRegistrarCliente extends JFrame {
    private JTextField campoNombre;
    private JTextField campoApellido;
    private JTextField campoCedula;
    private JTextField campoTelefono;
    private JTextField campoCorreo;
    private JTextField campoDireccion;
    private JComboBox<String> campoTipoCliente;
    private GestionCliente gestionCliente;

    public PanelRegistrarCliente() {

        ClienteConfig clienteConfig = ClienteConfig.getInstancia();
        gestionCliente = clienteConfig.getGestionCliente();

        setTitle("Registrar Cliente");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));

        panel.add(new JLabel("Nombre:"));
        campoNombre = new JTextField();
        panel.add(campoNombre);

        panel.add(new JLabel("Apellido:"));
        campoApellido = new JTextField();
        panel.add(campoApellido);

        panel.add(new JLabel("Cédula:"));
        campoCedula = new JTextField();
        panel.add(campoCedula);

        panel.add(new JLabel("Teléfono:"));
        campoTelefono = new JTextField();
        panel.add(campoTelefono);

        panel.add(new JLabel("Correo:"));
        campoCorreo = new JTextField();
        panel.add(campoCorreo);

        panel.add(new JLabel("Dirección:"));
        campoDireccion = new JTextField();
        panel.add(campoDireccion);

        panel.add(new JLabel("Tipo de cliente:"));
        campoTipoCliente = new JComboBox<>(
                new String[] {"Premium", "No Premium"}
        );
        panel.add(campoTipoCliente);

        JButton botonRegistrar = new JButton("Registrar");
        JButton botonLimpiar = new JButton("Limpiar");

        panel.add(botonRegistrar);
        panel.add(botonLimpiar);

        JButton botonVolver = new JButton("Volver");
        panel.add(botonVolver);
        panel.add(new JLabel(""));

        add(panel);

        botonRegistrar.addActionListener(e -> registrarCliente());

        botonLimpiar.addActionListener(e -> limpiar());

        botonVolver.addActionListener(e -> {
            new PanelCentral().setVisible(true);
            dispose();
        });
    }

    private void registrarCliente() {

     Cliente cliente = new Cliente(
             campoCedula.getText().trim(),
             campoNombre.getText().trim(),
             campoApellido.getText().trim(),
             campoTelefono.getText().trim(),
             campoCorreo.getText().trim(),
             campoDireccion.getText().trim(),
             campoTipoCliente.getSelectedItem().toString()
     );

        try {
            gestionCliente.guardarCliente(cliente);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Cliente registrado correctamente"
          );

        limpiar();
   }

    private void limpiar() {

     campoNombre.setText("");
     campoApellido.setText("");
     campoCedula.setText("");
     campoTelefono.setText("");
     campoCorreo.setText("");
     campoDireccion.setText("");
     campoTipoCliente.setSelectedIndex(0);
      }
}