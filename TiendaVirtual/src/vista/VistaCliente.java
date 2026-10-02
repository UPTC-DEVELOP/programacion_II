package vista;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class VistaCliente extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField campoNombre;
    private JTextField campoCorreo;
    private JTextField campoDireccion;
    private JTextField campoTelefono;
    private JComboBox<String> comboTipoCliente;
    private JButton botonBuscar;
    private JButton botonActualizar;
    private JButton botonEliminar;
    private JButton botonListar;
    public VistaCliente() {
        setTitle("Gestión de Clientes");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel formulario = new JPanel(new GridLayout(5, 2, 10, 10));
        campoNombre = new JTextField();
        campoCorreo = new JTextField();
        campoDireccion = new JTextField();
        campoTelefono = new JTextField();
        comboTipoCliente = new JComboBox<String>(
                new String[] { "Regular", "Premium" });
        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Correo:"));
        formulario.add(campoCorreo);
        formulario.add(new JLabel("Dirección:"));
        formulario.add(campoDireccion);
        formulario.add(new JLabel("Teléfono:"));
        formulario.add(campoTelefono);
        formulario.add(new JLabel("Tipo Cliente:"));
        formulario.add(comboTipoCliente);
        botonBuscar = new JButton("Buscar");
        botonActualizar = new JButton("Actualizar");
        botonEliminar = new JButton("Eliminar");
        botonListar = new JButton("Listar");
        JPanel botones = new JPanel(new FlowLayout());
        botones.add(botonBuscar);
        botones.add(botonActualizar);
        botones.add(botonEliminar);
        botones.add(botonListar);
        add(formulario, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }
}