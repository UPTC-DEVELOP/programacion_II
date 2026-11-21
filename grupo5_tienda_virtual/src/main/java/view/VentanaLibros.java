package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.FormatoLibro;
import service.IServicioLibros;

/**
 * Modulo de interfaz grafica: ventana de gestion de libros.
 */
public class VentanaLibros extends JFrame {

    private static final long serialVersionUID = 1L;

    private final EventosLibros eventos;

    private JTextField txtISBN, txtTitulo, txtAutor, txtAnio, txtCategoria,
            txtEditorial, txtPaginas, txtPrecio, txtIva, txtCantidad, txtBuscar;
    private JComboBox<FormatoLibro> cmbFormato;
    private JTable tblLibros;
    private DefaultTableModel modeloTabla;
    private JButton btnBuscar, btnLimpiarBusqueda;
    private JButton btnRegistrar, btnActualizar, btnEliminar, btnLimpiar, btnAtras;

    public VentanaLibros(JFrame padre, IServicioLibros controladorLibro) {
        super("Gesti\u00f3n de Libros");
        this.eventos = new EventosLibros(this, controladorLibro);

        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelBusqueda(), BorderLayout.NORTH);
        add(crearPanelFormulario(), BorderLayout.WEST);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);

        eventos.registrarEventos(
                txtISBN, txtTitulo, txtAutor, txtAnio, txtCategoria, txtEditorial,
                txtPaginas, txtPrecio, txtIva, txtCantidad, txtBuscar, cmbFormato,
                tblLibros, modeloTabla, btnBuscar, btnLimpiarBusqueda,
                btnRegistrar, btnActualizar, btnEliminar, btnLimpiar, btnAtras, padre);
    }

    private JPanel crearPanelBusqueda() {
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Buscar por ISBN / T\u00edtulo / Autor:"));
        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnLimpiarBusqueda = new JButton("Mostrar Todos");
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnLimpiarBusqueda);
        return panelBusqueda;
    }

    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridLayout(11, 2, 5, 5));

        panelFormulario.add(new JLabel("ISBN:"));
        txtISBN = new JTextField();
        panelFormulario.add(txtISBN);

        panelFormulario.add(new JLabel("T\u00edtulo:"));
        txtTitulo = new JTextField();
        panelFormulario.add(txtTitulo);

        panelFormulario.add(new JLabel("Autor:"));
        txtAutor = new JTextField();
        panelFormulario.add(txtAutor);

        panelFormulario.add(new JLabel("A\u00f1o Publicaci\u00f3n:"));
        txtAnio = new JTextField();
        panelFormulario.add(txtAnio);

        panelFormulario.add(new JLabel("Categor\u00eda:"));
        txtCategoria = new JTextField();
        panelFormulario.add(txtCategoria);

        panelFormulario.add(new JLabel("Editorial:"));
        txtEditorial = new JTextField();
        panelFormulario.add(txtEditorial);

        panelFormulario.add(new JLabel("N\u00famero P\u00e1ginas:"));
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

        return panelFormulario;
    }

    private JScrollPane crearPanelTabla() {
        String[] columnas = { "ISBN", "T\u00edtulo", "Autor", "Precio", "Stock", "Formato" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblLibros = new JTable(modeloTabla);
        return new JScrollPane(tblLibros);
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRegistrar = new JButton("Registrar Libro");
        btnActualizar = new JButton("Actualizar Libro");
        btnEliminar = new JButton("Eliminar Libro");
        btnLimpiar = new JButton("Limpiar Campos");
        btnAtras = new JButton("Atr\u00e1s");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnAtras);
        return panelBotones;
    }
}