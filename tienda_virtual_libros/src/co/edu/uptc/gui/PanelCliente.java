package co.edu.uptc.tienda.gui;

import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.enums.TipoCliente;
import co.edu.uptc.tienda.negocio.GestionCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelCliente extends JPanel {

    private GestionCliente gestionCliente;

  
    private JComboBox<String> cbTipoIdentificacion;
    private JTextField txtIdentificacion;
    private JTextField txtPrimerNombre, txtOtrosNombres;
    private JTextField txtPrimerApellido, txtOtrosApellidos;
    private JTextField txtCorreo, txtCelular, txtDireccion;
    private JPasswordField txtContrasenia;
    private JComboBox<TipoCliente> cbTipoCliente;
    private JTextField txtBuscarDoc;
    private JButton btnBuscar;

    // botones del CRUD
    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar;

    // tabla de clientes
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    public PanelCliente(GestionCliente gestionCliente) {
        this.gestionCliente = gestionCliente;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initGUI();
        resetearFormulario();
        actualizarTabla();
    }

    private void initGUI() {
        //distribución de 2 columnas 35% Iz 65% De
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, crearPanelIzquierdo(), crearPanelDerecho());
        splitPane.setDividerLocation(380);
        splitPane.setResizeWeight(0.35);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Ficha de Registro / Edición"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel formGrid = new JPanel(new GridLayout(11, 2, 5, 8));

        cbTipoIdentificacion = new JComboBox<>(new String[]{"CC", "CE","TI", "CD","Pasaporte"});
        txtIdentificacion = new JTextField();
        txtPrimerNombre = new JTextField();
        txtOtrosNombres = new JTextField();
        txtPrimerApellido = new JTextField();
        txtOtrosApellidos = new JTextField();
        txtCorreo = new JTextField();
        txtCelular = new JTextField();
        txtDireccion = new JTextField();
        txtContrasenia = new JPasswordField();
        cbTipoCliente = new JComboBox<>(TipoCliente.values());

        formGrid.add(new JLabel("Tipo Doc:"));
        formGrid.add(cbTipoIdentificacion);
        formGrid.add(new JLabel("Documento:"));
        formGrid.add(txtIdentificacion);
        formGrid.add(new JLabel("Primer Nombre:"));
        formGrid.add(txtPrimerNombre);
        formGrid.add(new JLabel("Otros Nombres:"));
        formGrid.add(txtOtrosNombres);
        formGrid.add(new JLabel("Primer Apellido:"));
        formGrid.add(txtPrimerApellido);
        formGrid.add(new JLabel("Otros Apellidos:"));
        formGrid.add(txtOtrosApellidos);
        formGrid.add(new JLabel("Correo Electrónico:"));
        formGrid.add(txtCorreo);
        formGrid.add(new JLabel("Celular:"));
        formGrid.add(txtCelular);
        formGrid.add(new JLabel("Dirección:"));
        formGrid.add(txtDireccion);
        formGrid.add(new JLabel("Contraseña:"));
        formGrid.add(txtContrasenia);
        formGrid.add(new JLabel("Tipo Cliente:"));
        formGrid.add(cbTipoCliente);

        JScrollPane formScroll = new JScrollPane(formGrid);
        formScroll.setBorder(null);

        //botones funcionales
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 8, 8));
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar / Nuevo");

        //colores de cada boton
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);

        btnActualizar.setBackground(new Color(0, 123, 255));
        btnActualizar.setForeground(Color.WHITE);

        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);

        btnLimpiar.setBackground(new Color(108, 117, 125));
        btnLimpiar.setForeground(Color.WHITE);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        panel.add(formScroll, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        // eventos
        btnGuardar.addActionListener(e -> accionGuardar());
        btnActualizar.addActionListener(e -> accionActualizar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnLimpiar.addActionListener(e -> resetearFormulario());

        return panel;
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Directorio General de Clientes"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // barra 
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 0));
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Búsqueda Obligatoria para Editar / Eliminar"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        txtBuscarDoc = new JTextField();
        btnBuscar = new JButton("Buscar Documento");
        btnBuscar.setBackground(new Color(23, 162, 184));
        btnBuscar.setForeground(Color.WHITE);

        panelBusqueda.add(txtBuscarDoc, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        //tabla de datos
        String[] columnas = {"ID", "Tipo", "Documento", "Nombre Completo", "Correo", "Celular", "Tipo Cliente"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setRowHeight(22);

        JScrollPane tableScroll = new JScrollPane(tablaClientes);

        panel.add(panelBusqueda, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);

        //eventos buscar
        btnBuscar.addActionListener(e -> accionBuscar());
        txtBuscarDoc.addActionListener(e -> accionBuscar()); // Buscar al presionar Enter

        return panel;
    }


    private void accionBuscar() {
        String doc = txtBuscarDoc.getText().trim();
        if (doc.isEmpty()) {
            doc = txtIdentificacion.getText().trim();
        }

        if (doc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el número de identificación en la barra de búsqueda.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente c = gestionCliente.buscarPorIdentificacion(doc);
        if (c != null) {
        
        	
            cbTipoIdentificacion.setSelectedItem(c.getTipoIdentificacion());
            txtIdentificacion.setText(c.getIdentificacion());
            txtPrimerNombre.setText(c.getPrimerNombre());
            txtOtrosNombres.setText(c.getOtrosNombres());
            txtPrimerApellido.setText(c.getPrimerApellido());
            txtOtrosApellidos.setText(c.getOtrosApellidos());
            txtCorreo.setText(c.getCorreoElectronico());
            txtCelular.setText(c.getCelular());
            txtDireccion.setText(c.getDireccion());
            cbTipoCliente.setSelectedItem(c.getTipoCliente());

            txtIdentificacion.setEnabled(false);
            txtCorreo.setEnabled(false);
            txtContrasenia.setEnabled(false);

           
            btnGuardar.setEnabled(false);
            btnActualizar.setEnabled(true);
            btnEliminar.setEnabled(true);

            JOptionPane.showMessageDialog(this, "Cliente encontrado. Sus datos han sido cargados en el formulario.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningún cliente registrado con el documento: " + doc, "Sin Resultados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionGuardar() {
        String res = gestionCliente.registrarCliente(
                txtPrimerNombre.getText(), txtOtrosNombres.getText(),
                txtPrimerApellido.getText(), txtOtrosApellidos.getText(),
                cbTipoIdentificacion.getSelectedItem().toString(), txtIdentificacion.getText(),
                txtCorreo.getText(), txtCelular.getText(), txtDireccion.getText(),
                (TipoCliente) cbTipoCliente.getSelectedItem(), new String(txtContrasenia.getPassword())
        );

        if (res.startsWith("OK")) {
            JOptionPane.showMessageDialog(this, res, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarTabla();
            resetearFormulario();
        } else {
            JOptionPane.showMessageDialog(this, res, "Error de Validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionActualizar() {
        String res = gestionCliente.modificarCliente(
                txtIdentificacion.getText(),
                txtPrimerNombre.getText(), txtOtrosNombres.getText(),
                txtPrimerApellido.getText(), txtOtrosApellidos.getText(),
                txtCelular.getText(), txtDireccion.getText(),
                (TipoCliente) cbTipoCliente.getSelectedItem()
        );

        if (res.startsWith("OK")) {
            JOptionPane.showMessageDialog(this, res, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarTabla();
            resetearFormulario();
        } else {
            JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionEliminar() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de eliminar permanentemente al cliente " + txtIdentificacion.getText() + "?", 
                "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String res = gestionCliente.eliminarCliente(txtIdentificacion.getText());
            if (res.startsWith("OK")) {
                JOptionPane.showMessageDialog(this, res, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
                resetearFormulario();
            } else {
                JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetearFormulario() {
        txtIdentificacion.setText("");
        txtPrimerNombre.setText("");
        txtOtrosNombres.setText("");
        txtPrimerApellido.setText("");
        txtOtrosApellidos.setText("");
        txtCorreo.setText("");
        txtCelular.setText("");
        txtDireccion.setText("");
        txtContrasenia.setText("");
        txtBuscarDoc.setText("");

        txtIdentificacion.setEnabled(true);
        txtCorreo.setEnabled(true);
        txtContrasenia.setEnabled(true);

        btnGuardar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<Cliente> lista = gestionCliente.consultarTodosLosClientes();
        for (Cliente c : lista) {
            Object[] fila = {
                c.getIdCliente(),
                c.getTipoIdentificacion(),
                c.getIdentificacion(),
                c.getNombreCompleto(),
                c.getCorreoElectronico(),
                c.getCelular(),
                c.getTipoCliente()
            };
            modeloTabla.addRow(fila);
        }
    }
}