package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import negocio.ControladorLibro;
import negocio.FormatoLibro;
import negocio.Libro;

/// MÓDULO DE INTERFAZ GRÁFICA: VENTANA DE GESTIÓN DE LIBROS ///

public class VentanaLibros extends JFrame {
	private static final long serialVersionUID = 1L;
    private static final String IVA_POR_DEFECTO = "19";

    //  ATRIBUTOS DE COMPONENTES INTERNOS DE LA INTERFAZ // 
	
    private JTextField txtISBN, txtTitulo, txtAutor, txtAnio, txtCategoria, txtEditorial, txtPaginas, txtPrecio, txtIva, txtCantidad, txtBuscar;
    private JComboBox<FormatoLibro> cmbFormato;
    private JTable tblLibros;
    private DefaultTableModel modeloTabla;
    private ControladorLibro controladorLibro;
    private JFrame padre;

    //  CONFIGURACION DE LAYOUTS DE LA VENTANA //
    
    public VentanaLibros(JFrame padre, ControladorLibro controladorLibro) {
        super("Gestión de Libros");
        this.padre = padre;
        this.controladorLibro = controladorLibro;

        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        //  PANEL SUPERIOR DE BUSQUEDA Y FILTRADO //
        
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Buscar por ISBN / Título / Autor:"));
        txtBuscar = new JTextField(20);
        panelBusqueda.add(txtBuscar);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiarBusqueda = new JButton("Mostrar Todos");
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnLimpiarBusqueda);
        add(panelBusqueda, BorderLayout.NORTH);

        // PANEL CENTRAL DE FORMULARIO DE CAPTURA DE DATOS //
        
        JPanel panelFormulario = new JPanel(new GridLayout(11, 2, 5, 5));

        panelFormulario.add(new JLabel("ISBN:"));
        txtISBN = new JTextField();
        panelFormulario.add(txtISBN);

        panelFormulario.add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        panelFormulario.add(txtTitulo);

        panelFormulario.add(new JLabel("Autor:"));
        txtAutor = new JTextField();
        panelFormulario.add(txtAutor);

        panelFormulario.add(new JLabel("Año Publicación:"));
        txtAnio = new JTextField();
        panelFormulario.add(txtAnio);

        panelFormulario.add(new JLabel("Categoría:"));
        txtCategoria = new JTextField();
        panelFormulario.add(txtCategoria);

        panelFormulario.add(new JLabel("Editorial:"));
        txtEditorial = new JTextField();
        panelFormulario.add(txtEditorial);

        panelFormulario.add(new JLabel("Número Páginas:"));
        txtPaginas = new JTextField();
        panelFormulario.add(txtPaginas);

        panelFormulario.add(new JLabel("Precio Venta:"));
        txtPrecio = new JTextField();
        panelFormulario.add(txtPrecio);

        panelFormulario.add(new JLabel("Porcentaje IVA (%):"));
        txtIva = new JTextField("19");
        panelFormulario.add(txtIva);

        panelFormulario.add(new JLabel("Cantidad Disponible:"));
        txtCantidad = new JTextField();
        panelFormulario.add(txtCantidad);

        panelFormulario.add(new JLabel("Formato:"));
        cmbFormato = new JComboBox<>(FormatoLibro.values());
        panelFormulario.add(cmbFormato);

        add(panelFormulario, BorderLayout.WEST);

        //  TABLA DE VISUALIZACION DE REGISTROS //
        
        String[] columnas = {"ISBN", "Título", "Autor", "Precio", "Stock", "Formato"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tblLibros = new JTable(modeloTabla);
        add(new JScrollPane(tblLibros), BorderLayout.CENTER);

        // EVENTO DE SELECCION DE FILA DENTRO DE LA TABLA // 
        
        tblLibros.getSelectionModel().addListSelectionListener(e -> cargarSeleccionEnFormulario());

//  MENU DE BOTONES DE ACCION //
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnRegistrar = new JButton("Registrar Libro");
        JButton btnActualizar = new JButton("Actualizar Libro");
        JButton btnEliminar = new JButton("Eliminar Libro");
        JButton btnLimpiar = new JButton("Limpiar Campos");
        JButton btnAtras = new JButton("Atrás");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnAtras);

        add(panelBotones, BorderLayout.SOUTH);

        //  ASIGNACIÓN DE EVENTOS A LOS BOTONES DE LA INTERFAZ  //
        
        btnRegistrar.addActionListener(e -> accionRegistrar());
        btnActualizar.addActionListener(e -> accionActualizar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnBuscar.addActionListener(e -> accionBuscar());
        btnLimpiarBusqueda.addActionListener(e -> actualizarTabla(controladorLibro.listar()));
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnAtras.addActionListener(e -> {
            this.dispose();
            if (this.padre != null) {
                this.padre.setVisible(true);
            }
        });
    }

    //  REGISTRO DE UN NUEVO LIBRO EN EL SISTEMA  //
    
    private void accionRegistrar() {
        try {
            Libro libro = extraerLibroDelFormulario();
            if (controladorLibro.registrar(libro)) {
                JOptionPane.showMessageDialog(this, "Libro registrado exitosamente.");
                actualizarTabla(controladorLibro.listar());
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error: El ISBN ya existe o datos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese valores numéricos válidos.", "Error de formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    //   ACTUALIZACION DE DATOS DE UN LIBRO SELECCIONADO  //
    
    private void accionActualizar() {
        try {
            String isbn = txtISBN.getText().trim();
            Libro datosNuevos = extraerLibroDelFormulario();
            if (controladorLibro.actualizar(isbn, datosNuevos)) {
                JOptionPane.showMessageDialog(this, "Libro actualizado correctamente.");
                actualizarTabla(controladorLibro.listar());
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró un libro con el ISBN ingresado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifique los datos numéricos.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    //  ELIMINACION DE UN REGISTRO DE LIBRO  //
    
    private void accionEliminar() {
        String isbn = txtISBN.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione o ingrese un ISBN para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el libro?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controladorLibro.eliminar(isbn)) {
                JOptionPane.showMessageDialog(this, "Libro eliminado con éxito.");
                actualizarTabla(controladorLibro.listar());
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // BUSQUEDA Y FILTRADO DE REGISTROS // 
    
    private void accionBuscar() {
    	String criterio = txtBuscar.getText().trim();
    	List<Libro> resultados = controladorLibro.buscarPorCriterio(criterio);
    	actualizarTabla(resultados);
    }

    //  EXTRAER  DATOS DEL FORMULARIO A UN OBJETO LIBRO //
    
    private Libro extraerLibroDelFormulario() {
        Libro libro = new Libro();
        libro.setIsbn(txtISBN.getText().trim());
        libro.setTitulo(txtTitulo.getText().trim());
        libro.setAutor(txtAutor.getText().trim());
        libro.setAnioPublicacion(txtAnio.getText().isEmpty() ? 0 : Integer.parseInt(txtAnio.getText().trim()));
        libro.setCategoria(txtCategoria.getText().trim());
        libro.setEditorial(txtEditorial.getText().trim());
        libro.setNumeroPaginas(txtPaginas.getText().isEmpty() ? 0 : Integer.parseInt(txtPaginas.getText().trim()));
        libro.setPrecio(txtPrecio.getText().isEmpty() ? 0.0 : Double.parseDouble(txtPrecio.getText().trim()));
        libro.setPorcentajeIva(txtIva.getText().isEmpty() ? 0.0 : Double.parseDouble(txtIva.getText().trim()));
        libro.setCantidadDisponible(txtCantidad.getText().isEmpty() ? 0 : Integer.parseInt(txtCantidad.getText().trim()));
        libro.setFormato((FormatoLibro) cmbFormato.getSelectedItem());
        return libro;
    }

    //  DATOS DE LA LISTA EN EL JTABLE // 
    
    private void actualizarTabla(List<Libro> lista) {
        modeloTabla.setRowCount(0);
        if (lista != null) {
        	for (Libro l : lista) {
                
                Object[] fila = {
                    l.getIsbn(),
                    l.getTitulo(),
                    l.getAutor(),
                    l.getPrecio(),
                    l.getCantidadDisponible(),
                    l.getFormato()
                };
                modeloTabla.addRow(fila);
            }
        }
    }

    // DATOS DE LA FILA SELECCIONADA HACIA LOS CAMPOS DEL FORMULARIO // 
    
    private void cargarSeleccionEnFormulario() {
        int fila = tblLibros.getSelectedRow();
        if (fila >= 0) {
            String isbn = modeloTabla.getValueAt(fila, 0).toString();
            Libro libro = controladorLibro.buscar(isbn);
            if (libro != null) {
                txtISBN.setText(libro.getIsbn());
                txtISBN.setEditable(false);
                txtTitulo.setText(libro.getTitulo());
                txtAutor.setText(libro.getAutor());
                txtAnio.setText(String.valueOf(libro.getAnioPublicacion()));
                txtCategoria.setText(libro.getCategoria());
                txtEditorial.setText(libro.getEditorial());
                txtPaginas.setText(String.valueOf(libro.getNumeroPaginas()));
                txtPrecio.setText(String.valueOf(libro.getPrecio()));
                txtIva.setText(String.valueOf(libro.getPorcentajeIva()));
                txtCantidad.setText(String.valueOf(libro.getCantidadDisponible()));
                cmbFormato.setSelectedItem(libro.getFormato());
            }
        }
    }

    // METODO PARA LIMPIAR LOS CAMPOS DE TEXTO // 
    
    private void limpiarCampos() {
        txtISBN.setText("");
        txtISBN.setEditable(true);
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAnio.setText("");
        txtCategoria.setText("");
        txtEditorial.setText("");
        txtPaginas.setText("");
        txtPrecio.setText("");
        txtIva.setText(IVA_POR_DEFECTO);
        txtCantidad.setText("");
        tblLibros.clearSelection();
    }

    // EJECUCION DIRECTA Y PRUEBAS // 
    
    public static void main(String[] args) {
        VentanaLibros v = new VentanaLibros(null, new ControladorLibro());
        v.setVisible(true);
    }
}