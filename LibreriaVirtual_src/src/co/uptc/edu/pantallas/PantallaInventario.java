package co.uptc.edu.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Refrescable;
import co.uptc.edu.gui.Vista;
import co.uptc.edu.model.Libro;
import co.uptc.edu.model.Tienda;
import co.uptc.edu.modelo.ValidarLibro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.time.Year;

/** Administración del inventario: tabla de libros y formulario de alta. */
public class PantallaInventario extends JPanel implements Refrescable {

    private final Tienda tienda;
    private final DefaultTableModel tablaModel;

    private final JTextField txtIsbn = new JTextField();
    private final JTextField txtTitulo = new JTextField();
    private final JTextField txtAutor = new JTextField();
    private final JTextField txtAnio = new JTextField();
    private final JComboBox<String> comboCategoria =
            new JComboBox<>(ValidarLibro.CATEGORIAS_POR_DEFECTO.toArray(new String[0]));
    private final JTextField txtEditorial = new JTextField();
    private final JTextField txtPaginas = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JTextField txtStock = new JTextField();
    private final JComboBox<String> comboFormato =
            new JComboBox<>(ValidarLibro.FORMATOS.toArray(new String[0]));
    
    public PantallaInventario(Tienda tienda, Navegador nav) {
        super(new BorderLayout());
        this.tienda = tienda;

        add(EstiloUI.crearHeaderSuperior("Administración de Inventario de Libros"), BorderLayout.NORTH);

        String[] columnas = {"ID", "Título", "Autor", "Precio", "Stock", "Categoría"};
        tablaModel = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(tablaModel);
        tabla.setRowHeight(24);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(4, 5, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Agregar Nuevo Libro"));
        formPanel.setBackground(Color.WHITE);
        for (String etiqueta : new String[]{"ISBN (13 dígitos):", "Título:", "Autor:", "Año:", "Categoría:"}) {
            formPanel.add(new JLabel(etiqueta));
        }
        formPanel.add(txtIsbn);
        formPanel.add(txtTitulo);
        formPanel.add(txtAutor);
        formPanel.add(txtAnio);
        formPanel.add(comboCategoria);
        for (String etiqueta : new String[]{"Editorial:", "Páginas:", "Precio:", "Stock:", "Formato:"}) {
            formPanel.add(new JLabel(etiqueta));
        }
        formPanel.add(txtEditorial);
        formPanel.add(txtPaginas);
        formPanel.add(txtPrecio);
        formPanel.add(txtStock);
        formPanel.add(comboFormato);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAgregar = EstiloUI.crearBotonEstilizado("Guardar Libro", EstiloUI.PRIMARIO);
        JButton btnVolver = new JButton("Volver al Panel Admin");
        btnAgregar.addActionListener(e -> guardarLibro());
        btnVolver.addActionListener(e -> nav.irA(Vista.ADMIN_HOME));
        actionPanel.add(btnAgregar);
        actionPanel.add(btnVolver);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(formPanel, BorderLayout.CENTER);
        panelInferior.add(actionPanel, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);
    }

    @Override
    public void refrescar() {
        tablaModel.setRowCount(0);
        NumberFormat formatter = EstiloUI.formatoMoneda();

        for (Libro libro : tienda.getInventario()) {
            tablaModel.addRow(new Object[]{
                    libro.getIsbn(),
                    libro.getTituloLibro(),
                    libro.getAutorLibro(),
                    formatter.format(libro.getPrecioVenta()),
                    libro.getStock(),
                    libro.getCategoria()
            });
        }
    }

    private void guardarLibro() {
        try {
            String isbn = txtIsbn.getText().trim();
            String tituloLibro = txtTitulo.getText().trim();
            String autorLibro = txtAutor.getText().trim();
            String anioPublicacion = txtAnio.getText().trim();
            String categoria = (String) comboCategoria.getSelectedItem();
            String editorial = txtEditorial.getText().trim();
            int numPaginas = Integer.parseInt(txtPaginas.getText().trim());
            double precioVenta = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            String tipoFormato = (String) comboFormato.getSelectedItem();

            validar(isbn, tituloLibro, autorLibro, anioPublicacion, editorial, numPaginas, precioVenta, stock);
            tienda.agregarLibro(new Libro(isbn, tituloLibro, autorLibro, anioPublicacion, categoria,
                    editorial, numPaginas, precioVenta, stock, tipoFormato));
            refrescar();
            limpiarFormulario();

            JOptionPane.showMessageDialog(this, "Libro agregado con éxito al inventario.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Por favor verifique los datos ingresados (Campos numéricos válidos).",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos inválidos", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Reglas de la especificación de datos del libro (DAD). Lanza IllegalArgumentException si algo no cumple. */
    private void validar(String isbn, String titulo, String autor, String anio, String editorial,
                         int paginas, double precio, int stock) {
        if (!isbn.matches("97[89]\\d{10}")) {
            throw new IllegalArgumentException("El ISBN debe tener 13 dígitos y empezar por 978 o 979.");
        }
        for (Libro existente : tienda.getInventario()) {
            if (existente.getIsbn().equals(isbn)) {
                throw new IllegalArgumentException("Ya existe un libro registrado con el ISBN " + isbn + ".");
            }
        }
        if (titulo.isEmpty() || titulo.length() > 150) {
            throw new IllegalArgumentException("El título es obligatorio y no puede superar 150 caracteres.");
        }
        if (autor.isEmpty() || autor.length() > 100) {
            throw new IllegalArgumentException("El autor es obligatorio y no puede superar 100 caracteres.");
        }
        if (!anio.matches("\\d{4}") || Integer.parseInt(anio) > Year.now().getValue()) {
            throw new IllegalArgumentException("El año debe tener 4 dígitos y no puede ser futuro.");
        }
        if (editorial.isEmpty() || editorial.length() > 100) {
            throw new IllegalArgumentException("La editorial es obligatoria y no puede superar 100 caracteres.");
        }
        if (paginas < 1 || paginas > 99999) {
            throw new IllegalArgumentException("El número de páginas debe estar entre 1 y 99999.");
        }
        if (precio < 0 || precio > 99999999.99) {
            throw new IllegalArgumentException("El precio debe ser un valor entre 0 y 99.999.999,99.");
        }
        if (stock < 0 || stock > 999999) {
            throw new IllegalArgumentException("El stock debe estar entre 0 y 999999.");
        }
    }

    private void limpiarFormulario() {
        txtIsbn.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAnio.setText("");
        txtEditorial.setText("");
        txtPaginas.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        comboCategoria.setSelectedIndex(0);
        comboFormato.setSelectedIndex(0);
    }
}
