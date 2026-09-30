package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import negocio.Cliente;
import negocio.ClientePremium;
import negocio.ClienteRegular;
import negocio.ControladorCliente;

/**
 * Ventana de gestion de clientes: formulario de datos, tabla de listado y
 * botones de las operaciones.
 */
public class VentanaClientes extends JFrame {

    private static final long serialVersionUID = 1L;

    private ControladorCliente controlador;

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JPasswordField txtContrasena;
    private JComboBox<String> cmbTipo;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnIniciarSesion;

    public VentanaClientes() {
        controlador = new ControladorCliente();

        setTitle("Gestion de Clientes");
        setSize(780, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(crearFormulario(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionRegistrar();
            }
        });
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 6, 6));

        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtDireccion = new JTextField();
        txtTelefono = new JTextField();
        txtContrasena = new JPasswordField();
        cmbTipo = new JComboBox<String>(new String[] { "Regular", "Premium" });

        panel.add(new JLabel("Nombre completo:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Correo:"));
        panel.add(txtCorreo);
        panel.add(new JLabel("Direccion:"));
        panel.add(txtDireccion);
        panel.add(new JLabel("Telefono:"));
        panel.add(txtTelefono);
        panel.add(new JLabel("Contrasena:"));
        panel.add(txtContrasena);
        panel.add(new JLabel("Tipo:"));
        panel.add(cmbTipo);

        return panel;
    }

    private JScrollPane crearTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[] { "Nombre", "Correo", "Direccion", "Telefono", "Tipo" }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        return new JScrollPane(tabla);
    }

    private JPanel crearBotones() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 6, 6));

        btnRegistrar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnIniciarSesion = new JButton("Iniciar sesion");

        panel.add(btnRegistrar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnIniciarSesion);

        return panel;
    }

    private void accionRegistrar() {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nombre, correo y contrasena son obligatorios.");
            return;
        }

        Cliente cliente;
        if ("Premium".equals(cmbTipo.getSelectedItem())) {
            cliente = new ClientePremium(nombre, correo, contrasena);
        } else {
            cliente = new ClienteRegular(nombre, correo, contrasena);
        }
        cliente.setDireccion(direccion);
        cliente.setTelefono(telefono);

        if (controlador.registrar(cliente)) {
            JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
            limpiarFormulario();
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Ya existe un cliente con ese correo.");
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Cliente cliente : controlador.listar()) {
            modeloTabla.addRow(new Object[] {
                    cliente.getNombreCompleto(),
                    cliente.getCorreo(),
                    cliente.getDireccion(),
                    cliente.getTelefono(),
                    (cliente instanceof ClientePremium) ? "Premium" : "Regular"
            });
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtContrasena.setText("");
        cmbTipo.setSelectedIndex(0);
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextField getTxtCorreo() {
        return txtCorreo;
    }

    public JTextField getTxtDireccion() {
        return txtDireccion;
    }

    public JTextField getTxtTelefono() {
        return txtTelefono;
    }

    public JPasswordField getTxtContrasena() {
        return txtContrasena;
    }

    public JComboBox<String> getCmbTipo() {
        return cmbTipo;
    }

    public JTable getTabla() {
        return tabla;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public JButton getBtnIniciarSesion() {
        return btnIniciarSesion;
    }
}
