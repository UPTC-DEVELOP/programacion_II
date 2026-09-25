package co.edu.uptc.gui;

import co.edu.uptc.negocio.GestorLibros;
import co.edu.uptc.negocio.modelo.Categoria;
import co.edu.uptc.negocio.modelo.Formato;
import co.edu.uptc.negocio.modelo.Libro;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana independiente para registrar o editar libros.
 * 
 * Según prototipo (imagen 4):
 * - Ventana modal separada del panel principal
 * - Formulario con campos: ISBN, Título, Autor(es), Año, Categoría, 
 *   Editorial, N° Páginas, Precio, Cantidad, Formato
 * - Botones: Guardar y Cancelar
 * - Radio buttons para Formato: Físico / Digital
 * 
 * Se usa tanto para crear nuevos libros como para editar existentes.
 * 
 * @author Oscar Clavijo
 * @version 1.0 - Septiembre 2026
 */
@SuppressWarnings("serial")
public class VentanaRegistroLibros extends JDialog {

    private GestorLibros gestorLibros;
    private PanelManageBooks panelPadre;
    private Libro libroEditar; // null si es registro nuevo

    // Campos del formulario
    private JTextField txtISBN;
    private JTextField txtTitulo;
    private JTextField txtAutores;
    private JTextField txtAnio;
    private JComboBox<Categoria> cmbCategoria;
    private JTextField txtEditorial;
    private JTextField txtPaginas;
    private JTextField txtPrecio;
    private JTextField txtCantidad;
    private JRadioButton rbFisico;
    private JRadioButton rbDigital;
    private JButton btnGuardar;
    private JButton btnCancelar;

    /**
     * Constructor para registrar libro nuevo.
     */
    public VentanaRegistroLibros(JFrame owner, GestorLibros gestorLibros, 
                                  PanelManageBooks panelPadre) {
        super(owner, "Registro Libros", ModalityType.APPLICATION_MODAL);
        this.gestorLibros = gestorLibros;
        this.panelPadre = panelPadre;
        this.libroEditar = null;
        inicializarComponentes();
    }

    /**
     * Constructor para editar libro existente.
     */
    public VentanaRegistroLibros(JFrame owner, GestorLibros gestorLibros,
                                  PanelManageBooks panelPadre, Libro libroEditar) {
        super(owner, "Editar Libro", ModalityType.APPLICATION_MODAL);
        this.gestorLibros = gestorLibros;
        this.panelPadre = panelPadre;
        this.libroEditar = libroEditar;
        inicializarComponentes();
        cargarDatosLibro();
    }

    private void inicializarComponentes() {
        setSize(500, 600);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Campos del formulario
        
        panelPrincipal.add(crearFilaFormulario("ISBN:", txtISBN = new JTextField()));
        panelPrincipal.add(crearFilaFormulario("Título:", txtTitulo = new JTextField()));
        panelPrincipal.add(crearFilaFormulario("Autor(es):", txtAutores = new JTextField()));
        panelPrincipal.add(crearFilaFormulario("Año:", txtAnio = new JTextField()));
        
        // Categoría con JComboBox
        JPanel panelCategoria = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cmbCategoria = new JComboBox<>(Categoria.values());
        cmbCategoria.setPreferredSize(new Dimension(300, 25));
        panelCategoria.add(cmbCategoria);
        panelPrincipal.add(crearFilaFormulario("Categoría:", panelCategoria));
        
        panelPrincipal.add(crearFilaFormulario("Editorial:", txtEditorial = new JTextField()));
        panelPrincipal.add(crearFilaFormulario("N° Páginas:", txtPaginas = new JTextField()));
        panelPrincipal.add(crearFilaFormulario("Precio:", txtPrecio = new JTextField()));
        panelPrincipal.add(crearFilaFormulario("Cantidad:", txtCantidad = new JTextField()));

        // Formato con RadioButtons
        JPanel panelFormato = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rbFisico = new JRadioButton("Físico", true);
        rbDigital = new JRadioButton("Digital");
        ButtonGroup grupoFormato = new ButtonGroup();
        grupoFormato.add(rbFisico);
        grupoFormato.add(rbDigital);
        panelFormato.add(rbFisico);
        panelFormato.add(Box.createHorizontalStrut(30));
        panelFormato.add(rbDigital);
        panelPrincipal.add(crearFilaFormulario("Formato:", panelFormato));

        panelPrincipal.add(Box.createVerticalStrut(20));

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardarLibro());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        panelPrincipal.add(panelBotones);

        setContentPane(panelPrincipal);
    }

    /**
     * Crea una fila del formulario con label y campo.
     */
    private JPanel crearFilaFormulario(String label, JComponent campo) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(120, 25));
        
        if (campo instanceof JTextField) {
            campo.setPreferredSize(new Dimension(300, 25));
        }
        
        fila.add(lbl);
        fila.add(campo);
        fila.add(Box.createHorizontalGlue());
        
        return fila;
    }

    /**
     * Carga los datos del libro en el formulario (modo edición).
     */
    private void cargarDatosLibro() {
        if (libroEditar == null) return;

        txtISBN.setText(libroEditar.getIsbn());
        txtISBN.setEditable(false); // ISBN no editable en edición
        txtTitulo.setText(libroEditar.getTitulo());
        txtAutores.setText(String.join(", ", libroEditar.getAutores()));
        txtAnio.setText(String.valueOf(libroEditar.getAnioPublicacion()));
        cmbCategoria.setSelectedItem(libroEditar.getCategoria());
        txtEditorial.setText(libroEditar.getEditorial());
        txtPaginas.setText(String.valueOf(libroEditar.getNumPaginas()));
        txtPrecio.setText(String.valueOf(libroEditar.getPrecioVenta()));
        txtCantidad.setText(String.valueOf(libroEditar.getStock()));
        
        if (libroEditar.getFormato() == Formato.FISICO) {
            rbFisico.setSelected(true);
        } else {
            rbDigital.setSelected(true);
        }

        btnGuardar.setText("Actualizar");
    }

    /**
     * Guarda o actualiza el libro según el modo.
     */
    private void guardarLibro() {
        try {
            String isbn = txtISBN.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String autoresTexto = txtAutores.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());
            Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();
            String editorial = txtEditorial.getText().trim();
            int paginas = Integer.parseInt(txtPaginas.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            Formato formato = rbFisico.isSelected() ? Formato.FISICO : Formato.DIGITAL;

            List<String> autores = new ArrayList<>();
            for (String autor : autoresTexto.split(",")) {
                if (!autor.trim().isEmpty()) {
                    autores.add(autor.trim());
                }
            }

            String resultado;
            if (libroEditar == null) {
                // Modo registro
                resultado = gestorLibros.registrarLibro(
                    isbn, titulo, autores, anio, categoria, editorial,
                    paginas, precio, cantidad, formato
                );
            } else {
                // Modo edición
                resultado = gestorLibros.actualizarLibro(
                    isbn, titulo, precio, cantidad, editorial
                );
            }

            JOptionPane.showMessageDialog(this, resultado);
            
            if (resultado.startsWith("Libro registrado") || resultado.startsWith("Libro actualizado")) {
                panelPadre.refrescarTabla();
                dispose();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Error: Los campos numéricos deben contener valores válidos.",
                "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}