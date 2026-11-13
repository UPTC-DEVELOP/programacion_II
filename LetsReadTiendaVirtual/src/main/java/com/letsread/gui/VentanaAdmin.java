package com.letsread.gui;

import com.letsread.model.Libro;
import com.letsread.service.TiendaService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class VentanaAdmin extends JFrame {
    private static final long serialVersionUID = 1L;

    private TiendaService service;
    private Runnable onCatalogoActualizado;

    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;

    private JTextField txtIsbn, txtTitulo, txtAutor, txtAnio, txtCategoria, txtEditorial, txtPaginas, txtPrecio, txtStock;
    private JComboBox<Double> comboIva;
    private JComboBox<String> comboFormato;

    private String isbnSeleccionado = null;

    public VentanaAdmin(TiendaService service, Runnable onCatalogoActualizado) {
        this.service = service;
        this.onCatalogoActualizado = onCatalogoActualizado;

        setTitle("Administración de Catálogo - Let's Read");
        setSize(980, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 1. PANEL SUPERIOR: Formulario de Registro / Edición
        JPanel panelFormulario = new JPanel(new GridLayout(4, 6, 8, 8));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Libro (Seleccione una fila para editar o llene para registrar)"));

        txtIsbn = new JTextField();
        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtAnio = new JTextField();
        txtCategoria = new JTextField();
        txtEditorial = new JTextField();
        txtPaginas = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();
        comboIva = new JComboBox<>(new Double[]{19.0, 5.0, 0.0});
        comboFormato = new JComboBox<>(new String[]{"Físico", "Digital"});

        panelFormulario.add(new JLabel("ISBN:")); panelFormulario.add(txtIsbn);
        panelFormulario.add(new JLabel("Título:")); panelFormulario.add(txtTitulo);
        panelFormulario.add(new JLabel("Autor:")); panelFormulario.add(txtAutor);

        panelFormulario.add(new JLabel("Año Pub.:")); panelFormulario.add(txtAnio);
        panelFormulario.add(new JLabel("Categoría:")); panelFormulario.add(txtCategoria);
        panelFormulario.add(new JLabel("Editorial:")); panelFormulario.add(txtEditorial);

        panelFormulario.add(new JLabel("Páginas:")); panelFormulario.add(txtPaginas);
        panelFormulario.add(new JLabel("Precio (IVA incl.):")); panelFormulario.add(txtPrecio);
        panelFormulario.add(new JLabel("IVA (%):")); panelFormulario.add(comboIva);

        panelFormulario.add(new JLabel("Inventario (Stock):")); panelFormulario.add(txtStock);
        panelFormulario.add(new JLabel("Formato:")); panelFormulario.add(comboFormato);
        panelFormulario.add(new JLabel("")); panelFormulario.add(new JLabel(""));

        add(panelFormulario, BorderLayout.NORTH);

        // 2. PANEL CENTRAL: Listado completo del catálogo
        String[] columnas = {"ISBN", "Título", "Autor", "Año", "Categoría", "Editorial", "Págs.", "Precio", "IVA %", "Stock", "Formato", "Con Ventas"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaLibros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaLibros.getSelectedRow() != -1) {
                cargarLibroEnFormulario(tablaLibros.getSelectedRow());
            }
        });

        add(new JScrollPane(tablaLibros), BorderLayout.CENTER);

        // 3. PANEL INFERIOR: Botones CRUD
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnRegistrar = new JButton("Registrar Nuevo");
        JButton btnActualizar = new JButton("Actualizar Seleccionado");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        JButton btnLimpiar = new JButton("Limpiar Campos");
        JButton btnCerrar = new JButton("Cerrar");

        btnRegistrar.addActionListener(e -> registrarNuevoLibro());
        btnActualizar.addActionListener(e -> actualizarLibroSeleccionado());
        btnEliminar.addActionListener(e -> eliminarLibroSeleccionado());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);

        listarLibros();
    }

    private void listarLibros() {
        modeloTabla.setRowCount(0);
        for (Libro l : service.getCatalogo()) {
            modeloTabla.addRow(new Object[]{
                l.getIsbn(),
                l.getTitulo(),
                l.getAutor(),
                l.getAnioPublicacion(),
                l.getCategoria(),
                l.getEditorial(),
                l.getNumeroPaginas(),
                l.getPrecioVenta(),
                l.getPorcentajeIva(),
                l.getCantidadInventario(),
                l.getFormato(),
                l.isTieneVentas() ? "Sí" : "No"
            });
        }
    }

    private void cargarLibroEnFormulario(int fila) {
        String isbn = (String) modeloTabla.getValueAt(fila, 0);
        Libro l = service.buscarLibroPorIsbn(isbn);
        if (l != null) {
            isbnSeleccionado = l.getIsbn();
            txtIsbn.setText(l.getIsbn());
            txtTitulo.setText(l.getTitulo());
            txtAutor.setText(l.getAutor());
            txtAnio.setText(String.valueOf(l.getAnioPublicacion()));
            txtCategoria.setText(l.getCategoria());
            txtEditorial.setText(l.getEditorial());
            txtPaginas.setText(String.valueOf(l.getNumeroPaginas()));
            txtPrecio.setText(String.valueOf(l.getPrecioVenta()));
            comboIva.setSelectedItem(l.getPorcentajeIva());
            txtStock.setText(String.valueOf(l.getCantidadInventario()));
            comboFormato.setSelectedItem(l.getFormato());
        }
    }

    private Libro construirLibroDesdeFormulario() throws Exception {
        String isbn = txtIsbn.getText().trim();
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String categoria = txtCategoria.getText().trim();
        String editorial = txtEditorial.getText().trim();

        if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty() || categoria.isEmpty() || editorial.isEmpty()) {
            throw new Exception("Todos los campos de texto son obligatorios.");
        }

        int anio, paginas, stock;
        double precio;
        try {
            anio = Integer.parseInt(txtAnio.getText().trim());
            paginas = Integer.parseInt(txtPaginas.getText().trim());
            precio = Double.parseDouble(txtPrecio.getText().trim());
            stock = Integer.parseInt(txtStock.getText().trim());
        } catch (NumberFormatException ex) {
            throw new Exception("Año, Páginas, Precio y Stock deben ser valores numéricos válidos.");
        }

        if (paginas <= 0 || precio < 0 || stock < 0) {
            throw new Exception("Páginas debe ser mayor a 0; Precio y Stock no pueden ser negativos.");
        }

        double iva = (Double) comboIva.getSelectedItem();
        String formato = (String) comboFormato.getSelectedItem();

        return new Libro(isbn, titulo, autor, anio, categoria, editorial, paginas, precio, iva, stock, formato);
    }

    private void registrarNuevoLibro() {
        try {
            Libro nuevo = construirLibroDesdeFormulario();
            service.registrarLibro(nuevo);
            refrescarVistas();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Libro registrado exitosamente en el catálogo.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al registrar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarLibroSeleccionado() {
        if (isbnSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione primero un libro de la tabla para actualizarlo.");
            return;
        }
        try {
            Libro actualizado = construirLibroDesdeFormulario();
            service.actualizarLibro(isbnSeleccionado, actualizado);
            refrescarVistas();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Información del libro actualizada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al actualizar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarLibroSeleccionado() {
        int fila = tablaLibros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro de la tabla para eliminar.");
            return;
        }

        String isbn = (String) modeloTabla.getValueAt(fila, 0);
        String titulo = (String) modeloTabla.getValueAt(fila, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el libro \"" + titulo + "\"?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.eliminarLibro(isbn);
                refrescarVistas();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Libro eliminado del catálogo.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Operación no permitida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void refrescarVistas() {
        listarLibros();
        if (onCatalogoActualizado != null) {
            onCatalogoActualizado.run();
        }
    }

    private void limpiarCampos() {
        isbnSeleccionado = null;
        tablaLibros.clearSelection();
        txtIsbn.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAnio.setText("");
        txtCategoria.setText("");
        txtEditorial.setText("");
        txtPaginas.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        comboIva.setSelectedIndex(0);
        comboFormato.setSelectedIndex(0);
    }
}