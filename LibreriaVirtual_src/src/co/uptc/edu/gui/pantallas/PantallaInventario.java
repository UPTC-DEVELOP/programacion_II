package co.uptc.edu.gui.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Refrescable;
import co.uptc.edu.gui.Vista;
import co.uptc.edu.model.Libro;
import co.uptc.edu.model.Tienda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;

/** Administración del inventario: tabla de libros y formulario de alta. */
public class PantallaInventario extends JPanel implements Refrescable {

    private final Tienda tienda;
    private final DefaultTableModel tablaModel;

    private final JTextField txtId = new JTextField();
    private final JTextField txtTitulo = new JTextField();
    private final JTextField txtAutor = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JTextField txtStock = new JTextField();
    private final JTextField txtCategoria = new JTextField();

    public PantallaInventario(Tienda tienda, Navegador nav) {
        super(new BorderLayout());
        this.tienda = tienda;

        add(EstiloUI.crearHeaderSuperior("Administración de Inventario de Libros"), BorderLayout.NORTH);

        String[] columnas = {"ID", "Título", "Autor", "Precio", "Stock", "Categoría"};
        tablaModel = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(tablaModel);
        tabla.setRowHeight(24);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Agregar Nuevo Libro"));
        formPanel.setBackground(Color.WHITE);
        for (String etiqueta : new String[]{"ID:", "Título:", "Autor:", "Precio:", "Stock:", "Categoría:"}) {
            formPanel.add(new JLabel(etiqueta));
        }
        formPanel.add(txtId);
        formPanel.add(txtTitulo);
        formPanel.add(txtAutor);
        formPanel.add(txtPrecio);
        formPanel.add(txtStock);
        formPanel.add(txtCategoria);

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
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    formatter.format(libro.getPrecio()),
                    libro.getStock(),
                    libro.getCategoria()
            });
        }
    }

    private void guardarLibro() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());

            tienda.agregarLibro(new Libro(id, txtTitulo.getText(), txtAutor.getText(), precio, stock, txtCategoria.getText()));
            refrescar();
            limpiarFormulario();

            JOptionPane.showMessageDialog(this, "Libro agregado con éxito al inventario.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Por favor verifique los datos ingresados (Campos numéricos válidos).",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtCategoria.setText("");
    }
}
