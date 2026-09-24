package co.edu.uptc.gui;

import co.edu.uptc.negocio.GestorLibros;
import co.edu.uptc.negocio.modelo.Categoria;
import co.edu.uptc.negocio.modelo.Formato;
import co.edu.uptc.negocio.modelo.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel para gestionar el CRUD de libros (RF01-RF04).
 * 
 * Componentes:
 * - Formulario de registro/edición en la parte superior
 * - Tabla con listado de libros en la parte inferior
 * - Botones de acción: Guardar, Actualizar, Eliminar, Limpiar, Buscar
 * 
 * Flujo de uso:
 * 1. El admin llena el formulario y hace clic en "Guardar" → RF01
 * 2. Selecciona un libro de la tabla y hace clic en "Editar" → carga datos en formulario
 * 3. Modifica y hace clic en "Actualizar" → RF02
 * 4. Selecciona y hace clic en "Eliminar" → RF03
 * 5. Usa el buscador para filtrar → RF04
 * 
 * @author Oscar Clavijo
 * @version 1.0 - Septiembre 2026
 */
public class PanelAdministradorRegistro extends JPanel {

    private GestorLibros gestorLibros;

    // Campos del formulario
    private JTextField txtISBN;
    private JTextField txtTitulo;
    private JTextField txtAutores;
    private JTextField txtAnio;
    private JComboBox<Categoria> cmbCategoria;
    private JTextField txtEditorial;
    private JTextField txtPaginas;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JRadioButton rbFisico;
    private JRadioButton rbDigital;

    // Botones de acción
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;

    // Buscador
    private JTextField txtBusqueda;

    // Tabla de libros
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;

    // Libro seleccionado actualmente (para edición)
    private Libro libroSeleccionado;

    public PanelAdministradorRegistro(GestorLibros gestorLibros) {
        this.gestorLibros = gestorLibros;
        inicializarComponentes();
        cargarLibrosEnTabla();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior: Buscador 
        JPanel panelBuscador = crearPanelBuscador();
        add(panelBuscador, BorderLayout.NORTH);

        // Panel central: Formulario + Tabla 
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(crearPanelFormulario());
        splitPane.setBottomComponent(crearPanelTabla());
        splitPane.setDividerLocation(300);
        
        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Crea el panel del buscador.
     */
    
    private JPanel crearPanelBuscador() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Búsqueda"));

        JLabel lblBusqueda = new JLabel("Buscar por ISBN o Título:");
        txtBusqueda = new JTextField(30);
        btnBuscar = new JButton("Buscar");

        btnBuscar.addActionListener(e -> buscarLibros());

        panel.add(lblBusqueda);
        panel.add(txtBusqueda);
        panel.add(btnBuscar);

        return panel;
    }

    /**
     * Crea el panel del formulario de registro/edición.
     */
    
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(5, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Libro"));

        // Fila 1: ISBN, Título
        panel.add(new JLabel("ISBN:"));
        txtISBN = new JTextField();
        panel.add(txtISBN);

        panel.add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        panel.add(txtTitulo);

        // Fila 2: Autores, Año
        panel.add(new JLabel("Autor(es) (separados por coma):"));
        txtAutores = new JTextField();
        panel.add(txtAutores);

        panel.add(new JLabel("Año de Publicación:"));
        txtAnio = new JTextField();
        panel.add(txtAnio);

        // Fila 3: Categoría, Editorial
        panel.add(new JLabel("Categoría:"));
        cmbCategoria = new JComboBox<>(Categoria.values());
        panel.add(cmbCategoria);

        panel.add(new JLabel("Editorial:"));
        txtEditorial = new JTextField();
        panel.add(txtEditorial);

        // Fila 4: Páginas, Precio
        panel.add(new JLabel("N° Páginas:"));
        txtPaginas = new JTextField();
        panel.add(txtPaginas);

        panel.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panel.add(txtPrecio);

        // Fila 5: Stock, Formato
        panel.add(new JLabel("Stock:"));
        txtStock = new JTextField();
        panel.add(txtStock);

        panel.add(new JLabel("Formato:"));
        JPanel panelFormato = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbFisico = new JRadioButton("Físico", true);
        rbDigital = new JRadioButton("Digital");
        ButtonGroup grupoFormato = new ButtonGroup();
        grupoFormato.add(rbFisico);
        grupoFormato.add(rbDigital);
        panelFormato.add(rbFisico);
        panelFormato.add(rbDigital);
        panel.add(panelFormato);

        // Fila 6: Botones de acción
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        btnGuardar.addActionListener(e -> guardarLibro());
        btnActualizar.addActionListener(e -> actualizarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        
        // Agregar botones ocupando toda la fila
        panel.add(new JLabel()); // Espacio vacío
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(panelBotones);

        return panel;
    }

    /**
     * Crea el panel de la tabla de libros.
     */
    
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Catálogo de Libros"));

        String[] columnas = {"ISBN", "Título", "Autor(es)", "Precio", "Stock", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable directamente
            }
        };

        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Evento al seleccionar una fila
        tablaLibros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarLibro();
            }
        });

        panel.add(new JScrollPane(tablaLibros), BorderLayout.CENTER);

        return panel;
    }

    // Acciones CRUD

    /**
     * RF01: Guarda un nuevo libro en el catálogo.
     */
    
    private void guardarLibro() {
        try {
            // Recoger datos del formulario
            String isbn = txtISBN.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String autoresTexto = txtAutores.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());
            Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();
            String editorial = txtEditorial.getText().trim();
            int paginas = Integer.parseInt(txtPaginas.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            Formato formato = rbFisico.isSelected() ? Formato.FISICO : Formato.DIGITAL;

            // Convertir texto de autores a lista
            List<String> autores = new ArrayList<>();
            for (String autor : autoresTexto.split(",")) {
                autores.add(autor.trim());
            }

            // Llamar al gestor
            String resultado = gestorLibros.registrarLibro(
                isbn, titulo, autores, anio, categoria, editorial,
                paginas, precio, stock, formato
            );

            JOptionPane.showMessageDialog(this, resultado);
            
            // Si fue exitoso, recargar tabla y limpiar formulario
            if (resultado.startsWith("Libro registrado")) {
                cargarLibrosEnTabla();
                limpiarFormulario();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Error: Los campos numéricos deben contener valores válidos.",
                "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * RF02: Actualiza un libro existente.
     */
    private void actualizarLibro() {
        if (libroSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un libro de la tabla primero.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String isbn = libroSeleccionado.getIsbn();
            String nuevoTitulo = txtTitulo.getText().trim();
            Double nuevoPrecio = txtPrecio.getText().trim().isEmpty() ? 
                null : Double.parseDouble(txtPrecio.getText().trim());
            Integer nuevoStock = txtStock.getText().trim().isEmpty() ? 
                null : Integer.parseInt(txtStock.getText().trim());
            String nuevaEditorial = txtEditorial.getText().trim();

            String resultado = gestorLibros.actualizarLibro(
                isbn, nuevoTitulo, nuevoPrecio, nuevoStock, nuevaEditorial
            );

            JOptionPane.showMessageDialog(this, resultado);
            
            if (resultado.startsWith("Libro actualizado")) {
                cargarLibrosEnTabla();
                limpiarFormulario();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Error: Los campos numéricos deben contener valores válidos.",
                "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * RF03: Elimina un libro del catálogo.
     */
    private void eliminarLibro() {
        if (libroSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un libro de la tabla primero.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmación antes de eliminar
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar el libro: " + libroSeleccionado.getTitulo() + "?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            String resultado = gestorLibros.eliminarLibro(libroSeleccionado.getIsbn());
            JOptionPane.showMessageDialog(this, resultado);
            
            if (resultado.startsWith("Libro eliminado")) {
                cargarLibrosEnTabla();
                limpiarFormulario();
            }
        }
    }

    /**
     * RF04: Busca libros según el texto ingresado.
     */
    private void buscarLibros() {
        String texto = txtBusqueda.getText().trim();
        
        if (texto.isEmpty()) {
            cargarLibrosEnTabla();
            return;
        }

        // Buscar por ISBN o título
        List<Libro> resultados = gestorLibros.listarLibrosConFiltros(
            null, null, texto, null, null
        );

        actualizarTabla(resultados);
    }

    // Métodos Auxiliares

    /**
     * Carga todos los libros en la tabla.
     */
    private void cargarLibrosEnTabla() {
        List<Libro> libros = gestorLibros.listarTodosLosLibros();
        actualizarTabla(libros);
    }

    /**
     * Actualiza la tabla con la lista de libros proporcionada.
     */
    private void actualizarTabla(List<Libro> libros) {
        modeloTabla.setRowCount(0); // Limpiar tabla

        for (Libro libro : libros) {
            String autores = String.join(", ", libro.getAutores());
            modeloTabla.addRow(new Object[]{
                libro.getIsbn(),
                libro.getTitulo(),
                autores,
                String.format("$%.2f", libro.getPrecioVenta()),
                libro.getStock(),
                "Editar | Eliminar"
            });
        }
    }

    /**
     * Carga los datos del libro seleccionado en el formulario.
     */
    private void seleccionarLibro() {
        int fila = tablaLibros.getSelectedRow();
        if (fila < 0) return;

        String isbn = (String) modeloTabla.getValueAt(fila, 0);
        libroSeleccionado = gestorLibros.buscarPorISBN(isbn);

        if (libroSeleccionado != null) {
            txtISBN.setText(libroSeleccionado.getIsbn());
            txtISBN.setEditable(false); // No permitir editar ISBN
            txtTitulo.setText(libroSeleccionado.getTitulo());
            txtAutores.setText(String.join(", ", libroSeleccionado.getAutores()));
            txtAnio.setText(String.valueOf(libroSeleccionado.getAnioPublicacion()));
            cmbCategoria.setSelectedItem(libroSeleccionado.getCategoria());
            txtEditorial.setText(libroSeleccionado.getEditorial());
            txtPaginas.setText(String.valueOf(libroSeleccionado.getNumPaginas()));
            txtPrecio.setText(String.valueOf(libroSeleccionado.getPrecioVenta()));
            txtStock.setText(String.valueOf(libroSeleccionado.getStock()));
            
            if (libroSeleccionado.getFormato() == Formato.FISICO) {
                rbFisico.setSelected(true);
            } else {
                rbDigital.setSelected(true);
            }
        }
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarFormulario() {
        txtISBN.setText("");
        txtISBN.setEditable(true);
        txtTitulo.setText("");
        txtAutores.setText("");
        txtAnio.setText("");
        cmbCategoria.setSelectedIndex(0);
        txtEditorial.setText("");
        txtPaginas.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        rbFisico.setSelected(true);
        libroSeleccionado = null;
        tablaLibros.clearSelection();
    }
}