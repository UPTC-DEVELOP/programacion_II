package co.edu.uptc.gui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.modelo.enums.Categoria;
import co.edu.uptc.modelo.enums.Formato;



/**
 * CLASE DialogoLibro  (paquete: gui)  extends JDialog
 * ---------------------------------------------------------------------------
 * Ventana "Registro Libros" del prototipo (también se usa para EDITAR).
 * Campos: ISBN, Título, Autor(es), Año, Categoría (combo), Editorial,
 *         N° Páginas, Precio, Cantidad, Formato (Físico/Digital) y los botones
 *         Guardar / Cancelar.
 *
 * Layout: GridBagLayout (columna de etiquetas + columna de campos), el más
 * adecuado para formularios.
 *
 * VALIDACIONES DE PRESENTACIÓN (guía): obligatorios y tipos de dato (que el año
 * sea entero, el precio numérico, etc.). Las reglas de NEGOCIO (ISBN único,
 * rangos, IVA...) NO van aquí: las valida la capa de negocio.
 *
 * Es MODAL: bloquea la ventana principal hasta pulsar Guardar o Cancelar.
 */
class DialogoLibro extends JDialog {

    private static final long serialVersionUID = 1L;

    private final JTextField txtIsbn = new JTextField(22);
    private final JTextField txtTitulo = new JTextField(22);
    private final JTextField txtAutores = new JTextField(22);
    private final JTextField txtAnio = new JTextField(22);
    private final JComboBox<Categoria> cmbCategoria = new JComboBox<>();
    private final JTextField txtEditorial = new JTextField(22);
    private final JTextField txtPaginas = new JTextField(22);
    private final JTextField txtPrecio = new JTextField(22);
    private final JTextField txtCantidad = new JTextField(22);
    private final JRadioButton rbFisico = new JRadioButton(Formato.FISICO.getEtiqueta());
    private final JRadioButton rbDigital = new JRadioButton(Formato.DIGITAL.getEtiqueta());

    private boolean guardado = false;

    /** Resultado: null mientras no se guarde (o si se cancela). */
    private LibroDto resultado;

    DialogoLibro(Frame padre, LibroDto precargado, boolean modoEdicion) {
        super(padre, modoEdicion ? "Editar Libro" : "Registro Libros", true);
        construirCombo();
        setLayout(new BorderLayout());
        add(construirFormulario(), BorderLayout.CENTER);
        add(construirBotones(), BorderLayout.SOUTH);

        if (precargado != null) {
            cargarDatos(precargado);
        }
        txtIsbn.setEnabled(!modoEdicion);       // RF02: el ISBN (clave primaria) no se edita

        pack();
        setResizable(false);
        setLocationRelativeTo(padre);
    }

    /** Muestra el diálogo (bloquea) y devuelve los datos o null si se canceló. */
    LibroDto mostrar() {
        setVisible(true);
        return resultado;
    }

    // ------------------------------ Construcción ------------------------------

    /** Combo con una primera opción nula que se pinta como "Seleccione la categoría". */
    private void construirCombo() {
        DefaultComboBoxModel<Categoria> modelo = new DefaultComboBoxModel<>();
        modelo.addElement(null);
        for (Categoria c : Categoria.values()) {
            modelo.addElement(c);
        }
        cmbCategoria.setModel(modelo);
        cmbCategoria.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                                                          boolean seleccionado, boolean foco) {
                return super.getListCellRendererComponent(lista,
                        valor == null ? "Seleccione la categoría" : valor, indice, seleccionado, foco);
            }
        });
    }

    private JPanel construirFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));

        rbFisico.setFont(Estilos.FUENTE);        // fuente normal (el look por defecto la pone en negrita)
        rbDigital.setFont(Estilos.FUENTE);
        cmbCategoria.setFont(Estilos.FUENTE);
        ButtonGroup grupo = new ButtonGroup();      // solo un formato a la vez
        grupo.add(rbFisico);
        grupo.add(rbDigital);
        JPanel panelFormato = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFormato.add(rbFisico);
        panelFormato.add(rbDigital);

        agregarFila(p, 0, "ISBN", txtIsbn);
        agregarFila(p, 1, "Título", txtTitulo);
        agregarFila(p, 2, "Autor(es)", txtAutores);
        agregarFila(p, 3, "Año", txtAnio);
        agregarFila(p, 4, "Categoría", cmbCategoria);
        agregarFila(p, 5, "Editorial", txtEditorial);
        agregarFila(p, 6, "N° Páginas", txtPaginas);
        agregarFila(p, 7, "Precio", txtPrecio);
        agregarFila(p, 8, "Cantidad", txtCantidad);
        agregarFila(p, 9, "Formato", panelFormato);
        txtAutores.setToolTipText("Separe varios autores con coma");
        return p;
    }

    /** Una fila del formulario: etiqueta a la izquierda y campo a la derecha. */
    private void agregarFila(JPanel panel, int fila, String texto, Component campo) {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 8);
        g.gridy = fila;

        g.gridx = 0;
        g.anchor = GridBagConstraints.EAST;
        panel.add(Estilos.etiqueta(texto), g);

        g.gridx = 1;
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        panel.add(campo, g);
    }

    private JPanel construirBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
        JButton guardar = Estilos.boton("Guardar", "GUARDAR", e -> alGuardar());
        JButton cancelar = Estilos.boton("Cancelar", "CANCELAR", e -> dispose());
        p.add(guardar);
        p.add(cancelar);
        getRootPane().setDefaultButton(guardar);     // Enter = Guardar
        return p;
    }

    private void cargarDatos(LibroDto l) {
        txtIsbn.setText(l.getIsbn());
        txtTitulo.setText(l.getTitulo());
        txtAutores.setText(String.join(", ", l.getAutores()));
        txtAnio.setText(l.getAnioPublicacion() > 0 ? String.valueOf(l.getAnioPublicacion()) : "");
        cmbCategoria.setSelectedItem(l.getCategoria());
        txtEditorial.setText(l.getEditorial());
        txtPaginas.setText(l.getNumPaginas() > 0 ? String.valueOf(l.getNumPaginas()) : "");
        txtPrecio.setText(l.getPrecioVenta() > 0 ? String.valueOf(l.getPrecioVenta()) : "");
        txtCantidad.setText(String.valueOf(l.getStock()));
        rbFisico.setSelected(l.getFormato() == Formato.FISICO);
        rbDigital.setSelected(l.getFormato() == Formato.DIGITAL);
    }

    // ------------------------------ Guardar ------------------------------

    /**
     * Valida OBLIGATORIEDAD y TIPOS; si todo está bien arma el LibroDto y cierra.
     * Ante un error muestra el mensaje y deja el diálogo abierto para corregir.
     */
    private void alGuardar() {
        try {
            exigir(txtIsbn, "ISBN");
            exigir(txtTitulo, "Título");
            exigir(txtAutores, "Autor(es)");
            exigir(txtAnio, "Año");
            exigir(txtEditorial, "Editorial");
            exigir(txtPaginas, "N° Páginas");
            exigir(txtPrecio, "Precio");
            exigir(txtCantidad, "Cantidad");
            if (cmbCategoria.getSelectedItem() == null) {
                throw new IllegalArgumentException("Debe seleccionar una categoría.");
            }
            if (!rbFisico.isSelected() && !rbDigital.isSelected()) {
                throw new IllegalArgumentException("Debe seleccionar el formato (Físico o Digital).");
            }

            LibroDto dto = new LibroDto();
            dto.setIsbn(txtIsbn.getText().trim());
            dto.setTitulo(txtTitulo.getText().trim());
            dto.setAutores(separarAutores(txtAutores.getText()));
            dto.setAnioPublicacion(entero(txtAnio, "Año"));
            dto.setCategoria((Categoria) cmbCategoria.getSelectedItem());
            dto.setEditorial(txtEditorial.getText().trim());
            dto.setNumPaginas(entero(txtPaginas, "N° Páginas"));
            dto.setPrecioVenta(decimal(txtPrecio, "Precio"));
            dto.setStock(entero(txtCantidad, "Cantidad"));
            dto.setFormato(rbFisico.isSelected() ? Formato.FISICO : Formato.DIGITAL);

            resultado = dto;
            guardado = true;
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos inválidos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void exigir(JTextField campo, String nombre) {
        if (campo.getText().trim().isEmpty()) {
            campo.requestFocus();
            throw new IllegalArgumentException("El campo \"" + nombre + "\" es obligatorio.");
        }
    }

    private int entero(JTextField campo, String nombre) {
        try {
            return Integer.parseInt(campo.getText().trim());
        } catch (NumberFormatException e) {
            campo.requestFocus();
            throw new IllegalArgumentException("El campo \"" + nombre + "\" debe ser un número entero.");
        }
    }

    private double decimal(JTextField campo, String nombre) {
        try {
            return Double.parseDouble(campo.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            campo.requestFocus();
            throw new IllegalArgumentException("El campo \"" + nombre + "\" debe ser un número (ej. 50000.00).");
        }
    }

    /** "Autor A, Autor B" -> ["Autor A", "Autor B"] (ignora elementos vacíos). */
    private List<String> separarAutores(String texto) {
        List<String> autores = new ArrayList<>();
        for (String a : texto.split(",")) {
            if (!a.trim().isEmpty()) {
                autores.add(a.trim());
            }
        }
        return autores;
    }
    public boolean isGuardado() {
        return guardado;
    }

}