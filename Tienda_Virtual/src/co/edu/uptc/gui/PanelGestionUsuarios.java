package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelGestionUsuarios extends JPanel {
    private JTextField txtId;
    private JTextField txtNombre;
    private JComboBox<String> comboTipoCliente;
    private JButton btnCrear;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    public PanelGestionUsuarios(Eventos listener) {
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID / Cédula:"), gbc);
        txtId = new JTextField(15);
        gbc.gridx = 1;
        panelFormulario.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Nombre Completo:"), gbc);
        txtNombre = new JTextField(15);
        gbc.gridx = 1;
        panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Tipo Cliente:"), gbc);
        comboTipoCliente = new JComboBox<>(new String[]{"Normal", "Premium"});
        gbc.gridx = 1;
        panelFormulario.add(comboTipoCliente, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnCrear = new JButton("Registrar");
        btnCrear.setActionCommand("CRUD_CREAR_USUARIO");
        btnCrear.addActionListener(listener);

        btnActualizar = new JButton("Modificar");
        btnActualizar.setActionCommand("CRUD_ACTUALIZAR_USUARIO");
        btnActualizar.addActionListener(listener);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setActionCommand("CRUD_ELIMINAR_USUARIO");
        btnEliminar.addActionListener(listener);

        panelBotones.add(btnCrear);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        String[] columnas = {"ID", "Nombre", "Tipo"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaUsuarios = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);

        add(panelFormulario, BorderLayout.WEST);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public String obtenerInputId() { return txtId.getText(); }
    public String obtenerInputNombre() { return txtNombre.getText(); }
    public String obtenerTipoSeleccionado() { return (String) comboTipoCliente.getSelectedItem(); }

    public void vaciarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        comboTipoCliente.setSelectedIndex(0);
    }

    public void refrescarComponenteTabla(java.util.List<co.edu.uptc.model.Cliente> lista) {
        modeloTabla.setRowCount(0);
        for (co.edu.uptc.model.Cliente c : lista) {
            String tipo = (c instanceof co.edu.uptc.model.ClientePremium) ? "Premium" : "Normal";
            modeloTabla.addRow(new Object[]{c.getId(), c.getNombreCompleto(), tipo});
        }
    }
}
