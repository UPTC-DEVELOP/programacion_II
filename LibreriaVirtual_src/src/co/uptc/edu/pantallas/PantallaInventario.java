package co.uptc.edu.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Refrescable;
import co.uptc.edu.gui.Vista;
import co.uptc.edu.controlador.AccesoDenegadoException;
import co.uptc.edu.controlador.LibroControlador;
import co.uptc.edu.model.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;

//Administración del inventario: tabla de libros y formulario de alta
public class PantallaInventario extends JPanel implements Refrescable {

    private final LibroControlador libros;
    private final DefaultTableModel tablaModel;

    private final JTextField txtIsbn = new JTextField();
    private final JTextField txtTitulo = new JTextField();
    private final JTextField txtAutor = new JTextField();
    private final JTextField txtAnio = new JTextField();
    private final JComboBox<String> comboCategoria;
    private final JTextField txtEditorial = new JTextField();
    private final JTextField txtPaginas = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JTextField txtStock = new JTextField();
    private final JComboBox<String> comboFormato;
    
    public PantallaInventario(LibroControlador libros, Navegador nav) {
        super(new BorderLayout());
        this.libros = libros;
        comboCategoria = new JComboBox<>(libros.getCategorias().toArray(new String[0]));
        comboFormato = new JComboBox<>(libros.getFormatos().toArray(new String[0]));

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

        for (Libro libro : libros.listarInventario()) {
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
            libros.registrarLibro(txtIsbn.getText(), txtTitulo.getText(), txtAutor.getText(), txtAnio.getText(),
                    (String) comboCategoria.getSelectedItem(), txtEditorial.getText(), txtPaginas.getText(),
                    txtPrecio.getText(), txtStock.getText(), (String) comboFormato.getSelectedItem());
        } catch (AccesoDenegadoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Acceso denegado", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos inválidos", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
            return;
        }
        refrescar();
        limpiarFormulario();
        JOptionPane.showMessageDialog(this, "Libro agregado con éxito al inventario.");
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
