package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaUsuarioDialog extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtCelular;
    private JTextField txtCorreo;
    private JComboBox<String> comboTipoCliente;
    private JButton btnCrear, btnActualizar, btnEliminar, btnContinuar;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    public VentanaUsuarioDialog(Frame padre, Eventos listener) {
        setTitle("Gestión de Usuarios - Tienda Virtual");
        setSize(850, 450); // Se amplió el tamaño para acomodar las nuevas columnas
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: ID
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID / Cédula:"), gbc);
        txtId = new JTextField(12);
        gbc.gridx = 1;
        panelFormulario.add(txtId, gbc);

        // Fila 1: Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Nombre Completo:"), gbc);
        txtNombre = new JTextField(12);
        gbc.gridx = 1;
        panelFormulario.add(txtNombre, gbc);

        // Fila 2: Celular (NUEVO)
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("N° Celular:"), gbc);
        txtCelular = new JTextField(12);
        gbc.gridx = 1;
        panelFormulario.add(txtCelular, gbc);

        // Fila 3: Correo Electrónico (NUEVO)
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Correo Electrónico:"), gbc);
        txtCorreo = new JTextField(12);
        gbc.gridx = 1;
        panelFormulario.add(txtCorreo, gbc);

        // Fila 4: Tipo de Cliente
        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(new JLabel("Tipo Cliente:"), gbc);
        comboTipoCliente = new JComboBox<>(new String[]{"Normal", "Premium"});
        gbc.gridx = 1;
        panelFormulario.add(comboTipoCliente, gbc);

        // --- CONFIGURACIÓN DE BOTONES ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        btnCrear = new JButton("Registrar");
        btnCrear.setActionCommand("CRUD_CREAR_USUARIO");
        btnCrear.addActionListener(listener);

        btnActualizar = new JButton("Modificar");
        btnActualizar.setActionCommand("CRUD_ACTUALIZAR_USUARIO");
        btnActualizar.addActionListener(listener);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setActionCommand("CRUD_ELIMINAR_USUARIO");
        btnEliminar.addActionListener(listener);

        btnContinuar = new JButton("Continuar a la Tienda Virtual");
        btnContinuar.setActionCommand("CONTINUAR_TIENDA");
        btnContinuar.addActionListener(listener);

        panelBotones.add(btnCrear);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnContinuar);

        // Columnas extendidas (NUEVAS COLUMNAS ADICIONADAS)
        String[] columnas = {"ID", "Nombre", "Celular", "Correo", "Tipo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaUsuarios = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);

        add(panelFormulario, BorderLayout.WEST);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Sincronización del MouseListener con los nuevos campos de texto
        tablaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int filaSeleccionada = tablaUsuarios.getSelectedRow();
                if (filaSeleccionada != -1) {
                    txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
                    txtNombre.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
                    txtCelular.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
                    txtCorreo.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
                    comboTipoCliente.setSelectedItem(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
                }
            }
        });
    }

    public String obtenerInputId() { return txtId.getText().trim(); }
    public String obtenerInputNombre() { return txtNombre.getText().trim(); }
    public String obtenerInputCelular() { return txtCelular.getText().trim(); }
    public String obtenerInputCorreo() { return txtCorreo.getText().trim(); }
    public String obtenerTipoSeleccionado() { return (String) comboTipoCliente.getSelectedItem(); }

    public void vaciarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtCelular.setText("");
        txtCorreo.setText("");
        comboTipoCliente.setSelectedIndex(0);
    }

    public void refrescarComponenteTabla(java.util.List<co.edu.uptc.model.Cliente> lista) {
        modeloTabla.setRowCount(0);
        for (co.edu.uptc.model.Cliente c : lista) {
            String tipo = "Normal";
            if (c.getClass().getSimpleName().equalsIgnoreCase("ClientePremium") || c instanceof co.edu.uptc.model.ClientePremium) {
                tipo = "Premium";
            }
            // Agrega de forma ordenada las 5 columnas a la interfaz gráfica
            modeloTabla.addRow(new Object[]{c.getId(), c.getNombreCompleto(), c.getTelefono(), c.getCorreo(), tipo});
        }
    }
}
