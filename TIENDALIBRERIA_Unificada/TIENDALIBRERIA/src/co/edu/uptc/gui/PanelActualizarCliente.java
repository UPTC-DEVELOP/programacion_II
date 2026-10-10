package co.edu.uptc.gui;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
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

public class PanelActualizarCliente extends JFrame {

    private JTextField campoBuscarCorreo;
    private JTextField campoNombre;
    private JTextField campoApellido;
    private JTextField campoCedula;
    private JTextField campoTelefono;
    private JTextField campoCorreo;
    private JTextField campoDireccion;
    private JComboBox<String> campoTipoCliente;
    private GestionCliente gestionCliente;
    private Cliente clienteEncontrado;

    public PanelActualizarCliente() {
     ClienteConfig clienteConfig = ClienteConfig.getInstancia();
     gestionCliente = clienteConfig.getGestionCliente();

     setTitle("Actualizar Cliente");
     setSize(550, 500);
     setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
     setLocationRelativeTo(null);
     setResizable(false);

    JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
     panelPrincipal.setBorder(
             BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

     JPanel panelBusqueda = new JPanel(new GridLayout(1, 2, 10, 10));

     campoBuscarCorreo = new JTextField();
      JButton botonBuscar = new JButton("Buscar");

     panelBusqueda.add(campoBuscarCorreo);
     panelBusqueda.add(botonBuscar);

     JPanel panelDatos = new JPanel(new GridLayout(7, 2, 10, 10));

     panelDatos.add(new JLabel("Nombre:"));
     campoNombre = new JTextField();
     panelDatos.add(campoNombre);

     panelDatos.add(new JLabel("Apellido:"));
     campoApellido = new JTextField();
     panelDatos.add(campoApellido);

     panelDatos.add(new JLabel("Cédula:"));
     campoCedula = new JTextField();
     panelDatos.add(campoCedula);

     panelDatos.add(new JLabel("Teléfono:"));
     campoTelefono = new JTextField();
     panelDatos.add(campoTelefono);

     panelDatos.add(new JLabel("Correo:"));
     campoCorreo = new JTextField();
     campoCorreo.setEditable(false);
     panelDatos.add(campoCorreo);

     panelDatos.add(new JLabel("Dirección:"));
     campoDireccion = new JTextField();
     panelDatos.add(campoDireccion);

     panelDatos.add(new JLabel("Tipo de cliente:"));
     campoTipoCliente = new JComboBox<>(
             new String[] {"Premium", "No Premium"}
        );
     panelDatos.add(campoTipoCliente);

     JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));

     JButton botonIngresar = new JButton("Actualizar");
     JButton botonCancelar = new JButton("Limpiar");
     JButton botonVolver = new JButton("Volver");

     panelBotones.add(botonIngresar);
     panelBotones.add(botonCancelar);
     panelBotones.add(botonVolver);

     panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
     panelPrincipal.add(panelDatos, BorderLayout.CENTER);
     panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

     add(panelPrincipal);

     botonBuscar.addActionListener(e -> buscarCliente());
     botonIngresar.addActionListener(e -> actualizarCliente());
     botonCancelar.addActionListener(e -> limpiarCampos());

     botonVolver.addActionListener(e -> {
         new PanelCentral().setVisible(true);
         dispose();
        });

     limpiarCampos();
    }

    private void buscarCliente() {

        String correo = campoBuscarCorreo.getText().trim();

        clienteEncontrado = null;

        for (Cliente cliente : gestionCliente.listarClientes()) {

            if (cliente.getCorreo().equals(correo)) {

                clienteEncontrado = cliente;

          campoNombre.setText(cliente.getNombre());
          campoApellido.setText(cliente.getApellido());
          campoCedula.setText(cliente.getCedula());
          campoTelefono.setText(cliente.getTelefono());
          campoCorreo.setText(cliente.getCorreo());
          campoDireccion.setText(cliente.getDireccion());

          campoTipoCliente.setSelectedItem(
                  cliente.getTipoCliente()
                );

                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Cliente no encontrado"
        );
    }

    private void actualizarCliente() {

        if (clienteEncontrado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero busque un cliente"
            );

            return;
        }

        Cliente clienteActualizado = new Cliente(
             campoCedula.getText().trim(),
             campoNombre.getText().trim(),
             campoApellido.getText().trim(),
             campoTelefono.getText().trim(),
             campoCorreo.getText().trim(),
             campoDireccion.getText().trim(),
             campoTipoCliente.getSelectedItem().toString()
        );

        try {
            gestionCliente.actualizarCliente(clienteActualizado);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Cliente actualizado correctamente"
        );

        clienteEncontrado = clienteActualizado;
    }

    private void limpiarCampos() {

        campoBuscarCorreo.setText("");
        campoNombre.setText("");
        campoApellido.setText("");
        campoCedula.setText("");
        campoTelefono.setText("");
        campoCorreo.setText("");
        campoDireccion.setText("");

        campoTipoCliente.setSelectedIndex(0);

        clienteEncontrado = null;
    }
}