package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import negocio.Cliente;
import negocio.ControladorCliente;
import negocio.ControladorLibro;
import negocio.Libro;

/**
 * Ventana "Mi Cuenta": el cliente actualiza sus datos (segun el prototipo).
 */
public class VentanaMiCuenta extends JFrame {

    private static final long serialVersionUID = 1L;

    private ControladorCliente controlador;
    private ControladorLibro controladorLibro;
    private Cliente cliente;
    private JFrame padre;

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JTextField txtTelefono;

    public VentanaMiCuenta(
            ControladorCliente controlador,ControladorLibro controladorLibro,Cliente cliente,
            JFrame padre) {

        this.controlador = controlador;
        this.controladorLibro = controladorLibro;
        this.cliente = cliente;
        this.padre = padre;

        setTitle("Mi Cuenta");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBoton(), BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (padre != null) {
                    padre.setVisible(true);
                }
            }
        });

        cargarDatos();
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        txtNombre = new JTextField(18);
        txtCorreo = new JTextField(18);
        txtCorreo.setEditable(false);
        txtDireccion = new JTextField(18);
        txtTelefono = new JTextField(18);

        agregarCampo(panel, "Nombre completo:", txtNombre, 0);
        agregarCampo(panel, "Correo electr\u00f3nico:", txtCorreo, 1);
        agregarCampo(panel, "Direcci\u00f3n:", txtDireccion, 2);
        agregarCampo(panel, "Tel\u00e9fono:", txtTelefono, 3);

        return panel;
    }

    private void agregarCampo(JPanel panel, String etiqueta, Component campo, int fila) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(campo, gbc);
    }

    private JPanel crearBoton() {
        JPanel panel = new JPanel();
        JButton btnAtras = new JButton("Atr\u00e1s");
        JButton btnGuardar = new JButton("Guardar cambios");
        JButton btnCompras = new JButton("Compras");
        btnAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volver();
            }
        });
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionGuardar();
            }
            
        });
        
        btnCompras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirCompras();
            }
        });
        panel.add(btnAtras);
        panel.add(btnGuardar);
        panel.add(btnCompras);
        return panel;
    }

    private void volver() {
        dispose();
        if (padre != null) {
            padre.setVisible(true);
        }
    }

    private void cargarDatos() {
        if (cliente == null) {
            return;
        }
        txtNombre.setText(cliente.getNombreCompleto());
        txtCorreo.setText(cliente.getCorreo());
        txtDireccion.setText(cliente.getDireccion());
        txtTelefono.setText(cliente.getTelefono());
    }

    private void accionGuardar() {
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "No hay sesion iniciada.");
            return;
        }

        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre completo es obligatorio.");
            return;
        }

        if (controlador.actualizarDatos(cliente.getCorreo(), nombre, direccion, telefono)) {
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudieron actualizar los datos.");
        }
    }
    
 // ABRIR VENTANA DE COMPRAS Liz 
    private void abrirCompras() {

        VentanaCompras ventanaCompras = new VentanaCompras(cliente, controladorLibro);

        for (Object obj : controladorLibro.listar()) {
            Libro libro = (Libro) obj;
            ventanaCompras.getCmbLibro().addItem(libro);
        }

        ventanaCompras.setVisible(true);
    }
}