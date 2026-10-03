package co.edu.uptc.gui;

import co.edu.uptc.model.Libro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelCatalogoLibros extends JPanel {
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;

    private JTextField txtIsbn, txtTitulo, txtAutor, txtAnio, txtCategoria, txtEditorial, txtPaginas, txtPrecio, txtInventario;
    private JComboBox<String> cbIva, cbFormato;
    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar;

    public PanelCatalogoLibros(Eventos listener) {
        setLayout(new BorderLayout(10, 10));
        initFormulario(listener);
        initTabla();
    }

    private void initFormulario(Eventos listener) {
        JPanel panelForm = new JPanel(new GridLayout(6, 4, 8, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Gestión de Libros (CRUD)"));

        txtIsbn = new JTextField();
        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtAnio = new JTextField();
        txtCategoria = new JTextField();
        txtEditorial = new JTextField();
        txtPaginas = new JTextField();
        txtPrecio = new JTextField();
        txtInventario = new JTextField();

        cbIva = new JComboBox<>(new String[]{"19.0", "5.0"});
        cbFormato = new JComboBox<>(new String[]{"Físico", "Digital"});

        panelForm.add(new JLabel("ISBN:")); panelForm.add(txtIsbn);
        panelForm.add(new JLabel("Título:")); panelForm.add(txtTitulo);
        panelForm.add(new JLabel("Autor:")); panelForm.add(txtAutor);
        panelForm.add(new JLabel("Año:")); panelForm.add(txtAnio);
        panelForm.add(new JLabel("Categoría:")); panelForm.add(txtCategoria);
        panelForm.add(new JLabel("Editorial:")); panelForm.add(txtEditorial);
        panelForm.add(new JLabel("Páginas:")); panelForm.add(txtPaginas);
        panelForm.add(new JLabel("Precio ($):")); panelForm.add(txtPrecio);
        panelForm.add(new JLabel("IVA (%):")); panelForm.add(cbIva);
        panelForm.add(new JLabel("Inventario:")); panelForm.add(txtInventario);
        panelForm.add(new JLabel("Formato:")); panelForm.add(cbFormato);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnGuardar.setActionCommand("GUARDAR_LIBRO");
        btnGuardar.addActionListener(listener);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setActionCommand("ACTUALIZAR_LIBRO");
        btnActualizar.addActionListener(listener);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setActionCommand("ELIMINAR_LIBRO");
        btnEliminar.addActionListener(listener);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setActionCommand("LIMPIAR_FORMULARIO");
        btnLimpiar.addActionListener(listener);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
    }

    private void initTabla() {
        String[] columnas = {"ISBN", "Título", "Autor", "Año", "Categoría", "Editorial", "Precio", "IVA", "Stock", "Formato"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaLibros = new JTable(modeloTabla);
        add(new JScrollPane(tablaLibros), BorderLayout.CENTER);
    }

    public void cargarDatosTabla(List<Libro> libros) {
        modeloTabla.setRowCount(0);
        for (Libro l : libros) {
            modeloTabla.addRow(new Object[]{
                l.getIsbn(), l.getTitulo(), l.getAutor(), l.getAnioPublicacion(),
                l.getCategoria(), l.getEditorial(), l.getPrecio(), l.getPorcentajeIva(),
                l.getCantidadInventario(), l.getFormato()
            });
        }
    }

    public Libro obtenerLibroDesdeFormulario() {
        return new Libro(
            txtIsbn.getText().trim(),
            txtTitulo.getText().trim(),
            txtAutor.getText().trim(),
            Integer.parseInt(txtAnio.getText().trim()),
            txtCategoria.getText().trim(),
            txtEditorial.getText().trim(),
            Integer.parseInt(txtPaginas.getText().trim()),
            Double.parseDouble(txtPrecio.getText().trim()),
            Double.parseDouble((String) cbIva.getSelectedItem()),
            Integer.parseInt(txtInventario.getText().trim()),
            (String) cbFormato.getSelectedItem()
        );
    }

    public String getIsbnSeleccionado() { return txtIsbn.getText().trim(); }

    public void limpiarCampos() {
        txtIsbn.setText(""); txtTitulo.setText(""); txtAutor.setText("");
        txtAnio.setText(""); txtCategoria.setText(""); txtEditorial.setText("");
        txtPaginas.setText(""); txtPrecio.setText(""); txtInventario.setText("");
        cbIva.setSelectedIndex(0); cbFormato.setSelectedIndex(0);
    }
}