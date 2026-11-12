package co.edu.uptc.tienda.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;

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
    private JButton botonLimpiar;

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
        botonLimpiar = new JButton(EventosCliente.LIMPIAR);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("GESTIÓN DE CLIENTES", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 8, 8));

        panelFormulario.add(new JLabel("ID:"));
        panelFormulario.add(campoId);

        panelFormulario.add(new JLabel("Nombre completo:"));
        panelFormulario.add(campoNombre);

        panelFormulario.add(new JLabel("Correo:"));
        panelFormulario.add(campoCorreo);

        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(campoDireccion);

        panelFormulario.add(new JLabel("Celular:"));
        panelFormulario.add(campoCelular);

        panelFormulario.add(new JLabel("Tipo de cliente:"));
        panelFormulario.add(campoTipoCliente);

        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.add(botonGuardar);
        panelBotones.add(botonBuscar);
        panelBotones.add(botonActualizar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonLimpiar);

        add(panelBotones, BorderLayout.SOUTH);
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

    public void mostrarAlerta(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }

    public void setEventos(EventosCliente eventos) {
        this.eventos = eventos;
        botonGuardar.setActionCommand(EventosCliente.GUARDAR);
        botonBuscar.setActionCommand(EventosCliente.BUSCAR);
        botonActualizar.setActionCommand(EventosCliente.ACTUALIZAR);
        botonEliminar.setActionCommand(EventosCliente.ELIMINAR);
        botonLimpiar.setActionCommand(EventosCliente.LIMPIAR);
        botonGuardar.addActionListener(eventos);
        botonBuscar.addActionListener(eventos);
        botonActualizar.addActionListener(eventos);
        botonEliminar.addActionListener(eventos);
        botonLimpiar.addActionListener(eventos);
    }
}