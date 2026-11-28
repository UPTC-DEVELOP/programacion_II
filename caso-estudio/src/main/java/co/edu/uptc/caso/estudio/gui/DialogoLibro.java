package co.edu.uptc.caso.estudio.gui;

import co.edu.uptc.caso.estudio.model.Libro;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DialogoLibro extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final String[] CATEGORIAS = {
        "Programación", "Arquitectura", "Literatura", "Ciencia", "Ingeniería", "Historia"
    };
    private static final String[] FORMATOS = {"Físico", "Digital"};
    private static final Double[] TASAS_IVA = {5.0, 19.0};

    private JTextField txtIsbn;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtAnio;
    private JTextField txtEditorial;
    private JTextField txtPaginas;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JComboBox<Double> cbIva;
    private JComboBox<String> cbCategoria;
    private JComboBox<String> cbFormato;
    private Libro libroResultado;

    public DialogoLibro(JFrame padre, Libro libroExistente) {
        super(padre, libroExistente != null ? "Editar Libro" : "Agregar Nuevo Libro", true);
        initUI(libroExistente);
    }

    private void initUI(Libro libroExistente) {
        setLayout(new BorderLayout(10, 10));
        JPanel panelForm = crearFormulario();
        cargarDatosIniciales(libroExistente);

        add(panelForm, BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
    }

    private JPanel crearFormulario() {
        JPanel panelForm = new JPanel(new GridLayout(11, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        txtIsbn = new JTextField();
        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtAnio = new JTextField();
        cbCategoria = new JComboBox<>(CATEGORIAS);
        txtEditorial = new JTextField();
        txtPaginas = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();
        cbFormato = new JComboBox<>(FORMATOS);
        cbIva = new JComboBox<>(TASAS_IVA);
        cbIva.setSelectedItem(5.0);

        panelForm.add(new JLabel("ISBN:"));
        panelForm.add(txtIsbn);
        panelForm.add(new JLabel("Título:"));
        panelForm.add(txtTitulo);
        panelForm.add(new JLabel("Autor:"));
        panelForm.add(txtAutor);
        panelForm.add(new JLabel("Año Publicación:"));
        panelForm.add(txtAnio);
        panelForm.add(new JLabel("Categoría:"));
        panelForm.add(cbCategoria);
        panelForm.add(new JLabel("Editorial:"));
        panelForm.add(txtEditorial);
        panelForm.add(new JLabel("Páginas:"));
        panelForm.add(txtPaginas);
        panelForm.add(new JLabel("Precio Venta (con IVA):"));
        panelForm.add(txtPrecio);
        panelForm.add(new JLabel("Stock:"));
        panelForm.add(txtStock);
        panelForm.add(new JLabel("Formato:"));
        panelForm.add(cbFormato);
        panelForm.add(new JLabel("Porcentaje IVA (%):"));
        panelForm.add(cbIva);

        return panelForm;
    }

    private void cargarDatosIniciales(Libro libroExistente) {
        if (libroExistente == null) {
            return;
        }
        txtIsbn.setText(libroExistente.getIsbn());
        txtIsbn.setEditable(false);
        txtTitulo.setText(libroExistente.getTitulo());
        txtAutor.setText(libroExistente.getAutor());
        txtAnio.setText(String.valueOf(libroExistente.getAnio()));
        if (libroExistente.getCategoria() != null) {
            cbCategoria.setSelectedItem(libroExistente.getCategoria());
        }
        txtEditorial.setText(libroExistente.getEditorial());
        txtPaginas.setText(String.valueOf(libroExistente.getPaginas()));
        txtPrecio.setText(String.valueOf(libroExistente.getPrecioVenta()));
        txtStock.setText(String.valueOf(libroExistente.getStock()));
        if (libroExistente.getFormato() != null) {
            cbFormato.setSelectedItem(libroExistente.getFormato());
        }
        double ivaExistente = libroExistente.getPorcentajeIva();
        if (ivaExistente == 19.0) {
            cbIva.setSelectedItem(19.0);
        } else {
            cbIva.setSelectedItem(5.0);
        }
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmar();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        getRootPane().setDefaultButton(btnGuardar);
        return panelBotones;
    }

    private void confirmar() {
        String isbn = txtIsbn.getText().trim();
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String editorial = txtEditorial.getText().trim();

        if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty() || editorial.isEmpty()) {
            mostrarError("ISBN, Título, Autor y Editorial son obligatorios.");
            return;
        }

        try {
            int anio = Integer.parseInt(txtAnio.getText().trim());
            int paginas = Integer.parseInt(txtPaginas.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            Double ivaSeleccionado = (Double) cbIva.getSelectedItem();
            double iva = ivaSeleccionado != null ? ivaSeleccionado : 5.0;

            if (anio <= 0 || paginas < 0 || precio < 0 || stock < 0 || iva < 0) {
                mostrarError("Año debe ser mayor a 0; Páginas, Precio, Stock e IVA no pueden ser negativos.");
                return;
            }

            Libro libro = new Libro();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setAnio(anio);
            libro.setCategoria((String) cbCategoria.getSelectedItem());
            libro.setEditorial(editorial);
            libro.setPaginas(paginas);
            libro.setPrecioVenta(precio);
            libro.setStock(stock);
            libro.setFormato((String) cbFormato.getSelectedItem());
            libro.setPorcentajeIva(iva);

            this.libroResultado = libro;
            dispose();
        } catch (NumberFormatException ex) {
            mostrarError("Ingrese valores numéricos válidos en Año, Páginas, Precio y Stock.");
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de Entrada", JOptionPane.ERROR_MESSAGE);
    }

    public Libro getLibroResultado() {
        return libroResultado;
    }
}