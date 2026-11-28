package co.uptc.edu.caso.estudio.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import co.uptc.edu.caso.estudio.controller.LibroController;
import co.uptc.edu.caso.estudio.negocio.Libro;

public class GestionLibro extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtIsbn;
    private JTextField txtTitulo;
    private JTextField txtAutores;
    private JTextField txtAnio;
    private JTextField txtCategoria;
    private JTextField txtEditorial;
    private JTextField txtPaginas;
    private JTextField txtPrecio;
    private JTextField txtInventario;

    private JComboBox<String> cmbFormato;

    private JTextArea areaSalida;

    private LibroController controller;

    public GestionLibro(LibroController controller) {

        this.controller = controller;

        setTitle("Gestión de Libros - CRUD");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        crearInterfaz();
    }

    private void crearInterfaz() {

        JLabel titulo = new JLabel(
                "GESTIÓN DE LIBROS",
                SwingConstants.CENTER
        );

        titulo.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        add(titulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(
                new GridLayout(10, 2, 5, 5)
        );

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        );

        panelFormulario.add(new JLabel("ISBN:"));
        txtIsbn = new JTextField();
        panelFormulario.add(txtIsbn);

        panelFormulario.add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        panelFormulario.add(txtTitulo);

        panelFormulario.add(new JLabel("Autores:"));
        txtAutores = new JTextField();
        panelFormulario.add(txtAutores);

        panelFormulario.add(new JLabel("Año publicación:"));
        txtAnio = new JTextField();
        panelFormulario.add(txtAnio);

        panelFormulario.add(new JLabel("Categoría:"));
        txtCategoria = new JTextField();
        panelFormulario.add(txtCategoria);

        panelFormulario.add(new JLabel("Editorial:"));
        txtEditorial = new JTextField();
        panelFormulario.add(txtEditorial);

        panelFormulario.add(new JLabel("Número páginas:"));
        txtPaginas = new JTextField();
        panelFormulario.add(txtPaginas);

        panelFormulario.add(new JLabel("Precio venta:"));
        txtPrecio = new JTextField();
        panelFormulario.add(txtPrecio);

        panelFormulario.add(new JLabel("Inventario:"));
        txtInventario = new JTextField();
        panelFormulario.add(txtInventario);

        panelFormulario.add(new JLabel("Formato:"));

        cmbFormato = new JComboBox<>(
                new String[] {"Fisico", "Digital"}
        );

        panelFormulario.add(cmbFormato);

        add(panelFormulario, BorderLayout.NORTH);

        areaSalida = new JTextArea();
        areaSalida.setEditable(false);

        add(
                new JScrollPane(areaSalida),
                BorderLayout.CENTER
        );

        JPanel panelBotones = new JPanel(
                new FlowLayout()
        );

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnConsultar = new JButton("Consultar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnListar = new JButton("Listar");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnConsultar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnListar);
        panelBotones.add(btnLimpiar);

        add(panelBotones, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> registrar());

        btnConsultar.addActionListener(e -> consultar());

        btnActualizar.addActionListener(e -> actualizar());

        btnEliminar.addActionListener(e -> eliminar());

        btnListar.addActionListener(e -> listar());

        btnLimpiar.addActionListener(e -> limpiar());
    }

    private Libro obtenerLibroFormulario() {

        try {

            String isbn = txtIsbn.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String autores = txtAutores.getText().trim();

            int anio = Integer.parseInt(
                    txtAnio.getText().trim()
            );

            String categoria =
                    txtCategoria.getText().trim();

            String editorial =
                    txtEditorial.getText().trim();

            int paginas = Integer.parseInt(
                    txtPaginas.getText().trim()
            );

            double precio = Double.parseDouble(
                    txtPrecio.getText().trim()
            );

            int inventario = Integer.parseInt(
                    txtInventario.getText().trim()
            );

            String formato =
                    (String) cmbFormato.getSelectedItem();

            return new Libro(
                    isbn,
                    titulo,
                    autores,
                    anio,
                    categoria,
                    editorial,
                    paginas,
                    precio,
                    inventario,
                    formato
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Año, páginas, precio e inventario deben ser valores numéricos.",
                    "Datos inválidos",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }
    }

    private void registrar() {

        Libro libro = obtenerLibroFormulario();

        if (libro == null) {
            return;
        }

        if (controller.registrar(libro)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro registrado correctamente."
            );

            limpiar();
            listar();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el libro."
            );
        }
    }

    private void consultar() {

        String isbn = txtIsbn.getText().trim();

        if (isbn.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese el ISBN que desea consultar."
            );

            return;
        }

        Libro libro = controller.buscarPorIsbn(isbn);

        if (libro == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el libro."
            );

            return;
        }

        cargarLibro(libro);
    }

    private void actualizar() {

        Libro libro = obtenerLibroFormulario();

        if (libro == null) {
            return;
        }

        if (controller.actualizar(libro)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro actualizado correctamente."
            );

            listar();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el libro."
            );
        }
    }

    private void eliminar() {

        String isbn = txtIsbn.getText().trim();

        if (isbn.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese el ISBN del libro."
            );

            return;
        }

        if (controller.eliminar(isbn)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro eliminado correctamente."
            );

            limpiar();
            listar();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el libro."
            );
        }
    }

    private void listar() {

        List<Libro> libros = controller.listar();

        areaSalida.setText(
                "LIBROS REGISTRADOS (" +
                libros.size() +
                ")\n\n"
        );

        for (Libro libro : libros) {

            areaSalida.append(
                    "ISBN: " + libro.getIsbn() + "\n"
                    + "Título: " + libro.getTitulo() + "\n"
                    + "Autores: " + libro.getAutores() + "\n"
                    + "Precio: " + libro.getPrecioVenta() + "\n"
                    + "Inventario: "
                    + libro.getCantidadInventario() + "\n"
                    + "Formato: " + libro.getFormato() + "\n"
                    + "-----------------------------\n"
            );
        }
    }

    private void cargarLibro(Libro libro) {

        txtIsbn.setText(libro.getIsbn());
        txtTitulo.setText(libro.getTitulo());
        txtAutores.setText(libro.getAutores());
        txtAnio.setText(
                String.valueOf(
                        libro.getAnioPublicacion()
                )
        );

        txtCategoria.setText(libro.getCategoria());
        txtEditorial.setText(libro.getEditorial());

        txtPaginas.setText(
                String.valueOf(
                        libro.getNumeroPaginas()
                )
        );

        txtPrecio.setText(
                String.valueOf(
                        libro.getPrecioVenta()
                )
        );

        txtInventario.setText(
                String.valueOf(
                        libro.getCantidadInventario()
                )
        );

        cmbFormato.setSelectedItem(
                libro.getFormato()
        );
    }

    private void limpiar() {

        txtIsbn.setText("");
        txtTitulo.setText("");
        txtAutores.setText("");
        txtAnio.setText("");
        txtCategoria.setText("");
        txtEditorial.setText("");
        txtPaginas.setText("");
        txtPrecio.setText("");
        txtInventario.setText("");

        cmbFormato.setSelectedIndex(0);

        areaSalida.setText("");
    }
}