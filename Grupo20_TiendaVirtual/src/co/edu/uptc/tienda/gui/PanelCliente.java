package co.edu.uptc.tienda.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import co.edu.uptc.tienda.modelo.Cliente;

public class PanelCliente extends JPanel {

    private JTextField campoId;
    private JTextField campoNombre;
    private JTextField campoCorreo;
    private JTextField campoDireccion;
    private JTextField campoCelular;
    private JComboBox<String> campoTipoCliente;

    private JButton botonGuardar;
    private JButton botonBuscar;
    private JButton botonActualizar;
    private JButton botonEliminar;
    private JButton botonListar;
    private JButton botonLimpiar;
    private JButton botonIniciarSesion;
    private JButton botonCerrarSesion;
    private JButton botonHistorial;
    private JLabel etiquetaSesion;

    private EventosCliente eventos;

    public PanelCliente(EventosCliente eventos) {

        this.eventos = eventos;

        campoId = new JTextField(15);
        campoNombre = new JTextField(15);
        campoCorreo = new JTextField(15);
        campoDireccion = new JTextField(15);
        campoCelular = new JTextField(15);
        campoTipoCliente = new JComboBox<>(new String[] { "Regular", "Premium" });

        botonGuardar = new JButton(EventosCliente.GUARDAR);
        botonBuscar = new JButton(EventosCliente.BUSCAR);
        botonActualizar = new JButton(EventosCliente.ACTUALIZAR);
        botonEliminar = new JButton(EventosCliente.ELIMINAR);
        botonListar = new JButton(EventosCliente.LISTAR);
        botonLimpiar = new JButton(EventosCliente.LIMPIAR);
        botonIniciarSesion = new JButton(EventosCliente.INICIAR_SESION);
        botonCerrarSesion = new JButton(EventosCliente.CERRAR_SESION);
        botonHistorial = new JButton(EventosCliente.HISTORIAL);
        etiquetaSesion = new JLabel();
        mostrarSesion(null);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("GESTIÓN DE CLIENTES", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 8, 8));
// texto que sale en la interfaz de usuario
        panelFormulario.add(new JLabel("Escriba el ID del cliente:"));
        panelFormulario.add(campoId);

        panelFormulario.add(new JLabel("Escriba el Nombre completo del usuario:"));
        panelFormulario.add(campoNombre);

        panelFormulario.add(new JLabel("Escriba el Correo del usuario:"));
        panelFormulario.add(campoCorreo);

        panelFormulario.add(new JLabel("Escriba la Dirección de vivienda del cliente:"));
        panelFormulario.add(campoDireccion);

        panelFormulario.add(new JLabel("Escriba el número Celular del cliente:"));
        panelFormulario.add(campoCelular);

        panelFormulario.add(new JLabel("Seleccione el Tipo de cliente:"));
        panelFormulario.add(campoTipoCliente);

        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.add(botonGuardar);
        panelBotones.add(botonBuscar);
        panelBotones.add(botonActualizar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonListar);
        panelBotones.add(botonLimpiar);

        JPanel panelSesion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelSesion.add(etiquetaSesion);
        panelSesion.add(botonIniciarSesion);
        panelSesion.add(botonCerrarSesion);
        panelSesion.add(botonHistorial);

        JPanel panelInferior = new JPanel(new GridLayout(2, 1));
        panelInferior.add(panelBotones);
        panelInferior.add(panelSesion);

        add(panelInferior, BorderLayout.SOUTH);
    }

    public String getId() {
        return campoId.getText();
    }

    public String getNombre() {
        return campoNombre.getText();
    }

    public String getCorreo() {
        return campoCorreo.getText();
    }

    public String getDireccion() {
        return campoDireccion.getText();
    }

    public String getCelular() {
        return campoCelular.getText();
    }

    public String getTipoCliente() {
        return (String) campoTipoCliente.getSelectedItem();
    }

    public void mostrarCliente(Cliente cliente) {
        campoId.setText(cliente.getId());
        campoNombre.setText(cliente.getNombreCompleto());
        campoCorreo.setText(cliente.getCorreoElectronico());
        campoDireccion.setText(cliente.getDireccionEnvio());
        campoCelular.setText(cliente.getCelular());
        campoTipoCliente.setSelectedItem("Premium".equalsIgnoreCase(cliente.getTipoCliente()) ? "Premium" : "Regular");
    }

    public void limpiarCampos() {
        campoId.setText("");
        campoNombre.setText("");
        campoCorreo.setText("");
        campoDireccion.setText("");
        campoCelular.setText("");
        campoTipoCliente.setSelectedIndex(0);
        campoId.requestFocus();
    }

    public void mostrarSesion(Cliente cliente) {
        if (cliente == null) {
            etiquetaSesion.setText("Sesión: ninguna");
        } else {
            etiquetaSesion.setText("Sesión: " + cliente.getNombreCompleto() + " (" + cliente.getCorreoElectronico() + ")");
        }
    }

    public String pedirCorreo() {
        return JOptionPane.showInputDialog(this, "Escriba el correo del cliente:", "Iniciar sesión",
                JOptionPane.QUESTION_MESSAGE);
    }

    public void mostrarAlerta(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarMensajeExito(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public void setEventos(EventosCliente eventos) {
        this.eventos = eventos;
        botonGuardar.setActionCommand(EventosCliente.GUARDAR);
        botonBuscar.setActionCommand(EventosCliente.BUSCAR);
        botonActualizar.setActionCommand(EventosCliente.ACTUALIZAR);
        botonEliminar.setActionCommand(EventosCliente.ELIMINAR);
        botonListar.setActionCommand(EventosCliente.LISTAR);
        botonLimpiar.setActionCommand(EventosCliente.LIMPIAR);
        botonIniciarSesion.setActionCommand(EventosCliente.INICIAR_SESION);
        botonCerrarSesion.setActionCommand(EventosCliente.CERRAR_SESION);
        botonHistorial.setActionCommand(EventosCliente.HISTORIAL);
        botonGuardar.addActionListener(eventos);
        botonBuscar.addActionListener(eventos);
        botonActualizar.addActionListener(eventos);
        botonEliminar.addActionListener(eventos);
        botonListar.addActionListener(eventos);
        botonLimpiar.addActionListener(eventos);
        botonIniciarSesion.addActionListener(eventos);
        botonCerrarSesion.addActionListener(eventos);
        botonHistorial.addActionListener(eventos);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                eventos.actualizarSesion();
            }
        });
    }
}
