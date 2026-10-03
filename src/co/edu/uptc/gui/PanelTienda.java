package co.edu.uptc.gui;

import co.edu.uptc.negocio.CarritoItem;
import co.edu.uptc.negocio.Cliente;
import co.edu.uptc.negocio.Compra;
import co.edu.uptc.negocio.Libro;
import co.edu.uptc.negocio.MetodoPago;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;

public class PanelTienda extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String[] COLUMNAS_CATALOGO = {"ISBN", "Título", "Precio final", "Stock", "Formato"};
    private static final String[] COLUMNAS_CARRITO = {"ISBN", "Título", "Cant.", "Subtotal", "IVA", "Total"};

    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnMostrarTodos;
    private JTable tblCatalogo;
    private DefaultTableModel modeloCatalogo;
    private JSpinner spCantidad;
    private JButton btnAgregar;

    private JTable tblCarrito;
    private DefaultTableModel modeloCarrito;
    private JButton btnAumentar;
    private JButton btnDisminuir;
    private JButton btnEliminar;
    private JButton btnVaciar;
    private JButton btnFinalizar;
    private JButton btnHistorial;
    private JButton btnClientes;

    private JLabel lblSesion;
    private JLabel lblSubtotal;
    private JLabel lblImpuestos;
    private JLabel lblDescuento;
    private JLabel lblTotal;
    private JLabel lblCantidadItems;

    public PanelTienda() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JPanel cabecera = new JPanel(new BorderLayout(8, 4));
        lblSesion = new JLabel("Sin sesión iniciada. Debe iniciar sesión para comprar.");
        btnClientes = crearBoton("Clientes / Sesión", Comandos.MENU_GESTIONAR_CLIENTES);
        cabecera.add(lblSesion, BorderLayout.CENTER);
        cabecera.add(btnClientes, BorderLayout.EAST);

        modeloCatalogo = new DefaultTableModel(COLUMNAS_CATALOGO, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
        tblCatalogo = new JTable(modeloCatalogo);
        tblCatalogo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCatalogo.setAutoCreateRowSorter(true);
        tblCatalogo.getColumnModel().getColumn(0).setPreferredWidth(115);
        tblCatalogo.getColumnModel().getColumn(1).setPreferredWidth(240);
        tblCatalogo.getColumnModel().getColumn(2).setPreferredWidth(110);
        tblCatalogo.getColumnModel().getColumn(3).setPreferredWidth(60);
        tblCatalogo.getColumnModel().getColumn(4).setPreferredWidth(80);

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        txtBuscar = new JTextField(18);
        txtBuscar.setActionCommand(Comandos.BUSCAR_TIENDA);
        btnBuscar = crearBoton("Buscar", Comandos.BUSCAR_TIENDA);
        btnMostrarTodos = crearBoton("Mostrar todos", Comandos.MOSTRAR_TODOS);
        busqueda.add(new JLabel("Catálogo:"));
        busqueda.add(txtBuscar);
        busqueda.add(btnBuscar);
        busqueda.add(btnMostrarTodos);

        JPanel agregar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        spCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 999999, 1));
        btnAgregar = crearBoton("Agregar al carrito", Comandos.AGREGAR_CARRITO);
        agregar.add(new JLabel("Cantidad:"));
        agregar.add(spCantidad);
        agregar.add(btnAgregar);

        JPanel panelCatalogo = new JPanel(new BorderLayout(0, 4));
        panelCatalogo.setBorder(BorderFactory.createTitledBorder("Libros disponibles"));
        panelCatalogo.add(busqueda, BorderLayout.NORTH);
        panelCatalogo.add(new JScrollPane(tblCatalogo), BorderLayout.CENTER);
        panelCatalogo.add(agregar, BorderLayout.SOUTH);

        modeloCarrito = new DefaultTableModel(COLUMNAS_CARRITO, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
        tblCarrito = new JTable(modeloCarrito);
        tblCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCarrito.setAutoCreateRowSorter(true);
        tblCarrito.getColumnModel().getColumn(0).setPreferredWidth(110);
        tblCarrito.getColumnModel().getColumn(1).setPreferredWidth(220);
        tblCarrito.getColumnModel().getColumn(2).setPreferredWidth(50);
        tblCarrito.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblCarrito.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblCarrito.getColumnModel().getColumn(5).setPreferredWidth(110);

        btnAumentar = crearBoton("+1", Comandos.AUMENTAR_CARRITO);
        btnDisminuir = crearBoton("-1", Comandos.DISMINUIR_CARRITO);
        btnEliminar = crearBoton("Eliminar", Comandos.ELIMINAR_CARRITO);
        btnVaciar = crearBoton("Vaciar carrito", Comandos.VACIAR_CARRITO);
        btnFinalizar = crearBoton("Finalizar compra", Comandos.FINALIZAR_COMPRA);
        btnHistorial = crearBoton("Historial", Comandos.HISTORIAL_COMPRAS);

        JPanel botonesCarrito = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        botonesCarrito.add(btnAumentar);
        botonesCarrito.add(btnDisminuir);
        botonesCarrito.add(btnEliminar);
        botonesCarrito.add(btnVaciar);
        botonesCarrito.add(btnHistorial);
        botonesCarrito.add(btnFinalizar);

        JPanel totales = new JPanel(new GridLayout(0, 2, 8, 4));
        lblCantidadItems = new JLabel("0");
        lblSubtotal = new JLabel(UtilidadesGUI.formatearMoneda(0));
        lblImpuestos = new JLabel(UtilidadesGUI.formatearMoneda(0));
        lblDescuento = new JLabel(UtilidadesGUI.formatearMoneda(0));
        lblTotal = new JLabel(UtilidadesGUI.formatearMoneda(0));
        totales.add(new JLabel("Unidades en carrito:"));
        totales.add(lblCantidadItems);
        totales.add(new JLabel("Subtotal (sin IVA):"));
        totales.add(lblSubtotal);
        totales.add(new JLabel("Impuestos:"));
        totales.add(lblImpuestos);
        totales.add(new JLabel("Descuento Premium:"));
        totales.add(lblDescuento);
        totales.add(new JLabel("Total a pagar:"));
        totales.add(lblTotal);

        JPanel panelCarrito = new JPanel(new BorderLayout(0, 4));
        panelCarrito.setBorder(BorderFactory.createTitledBorder("Carrito de compras"));
        panelCarrito.add(new JScrollPane(tblCarrito), BorderLayout.CENTER);
        JPanel pieCarrito = new JPanel(new BorderLayout(6, 4));
        pieCarrito.add(botonesCarrito, BorderLayout.NORTH);
        pieCarrito.add(totales, BorderLayout.CENTER);
        panelCarrito.add(pieCarrito, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelCatalogo, panelCarrito);
        split.setResizeWeight(0.55);
        add(cabecera, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
        actualizarEstadoSesion(null);
    }

    private JButton crearBoton(String texto, String comando) {
        JButton boton = new JButton(texto);
        boton.setActionCommand(comando);
        return boton;
    }

    public void agregarListener(ActionListener listener) {
        txtBuscar.addActionListener(listener);
        btnBuscar.addActionListener(listener);
        btnMostrarTodos.addActionListener(listener);
        btnAgregar.addActionListener(listener);
        btnAumentar.addActionListener(listener);
        btnDisminuir.addActionListener(listener);
        btnEliminar.addActionListener(listener);
        btnVaciar.addActionListener(listener);
        btnFinalizar.addActionListener(listener);
        btnHistorial.addActionListener(listener);
        btnClientes.addActionListener(listener);
    }

    public String getCriterioBusqueda() { return txtBuscar.getText().trim(); }

    public String getIsbnCatalogoSeleccionado() {
        int fila = tblCatalogo.getSelectedRow();
        if (fila < 0) return null;
        int modelo = tblCatalogo.convertRowIndexToModel(fila);
        return String.valueOf(modeloCatalogo.getValueAt(modelo, 0));
    }

    public String getIsbnCarritoSeleccionado() {
        int fila = tblCarrito.getSelectedRow();
        if (fila < 0) return null;
        int modelo = tblCarrito.convertRowIndexToModel(fila);
        return String.valueOf(modeloCarrito.getValueAt(modelo, 0));
    }

    public int getCantidadAgregar() {
        return ((Number) spCantidad.getValue()).intValue();
    }

    public void mostrarCatalogo(List<Libro> libros) {
        modeloCatalogo.setRowCount(0);
        for (Libro libro : libros) {
            modeloCatalogo.addRow(new Object[] {
                    libro.getIsbn(), libro.getTitulo(), UtilidadesGUI.formatearMoneda(libro.calcularPrecioFinal()),
                    libro.getStock(), libro.getFormato().getEtiqueta()
            });
        }
    }

    public void mostrarCarrito(List<CarritoItem> items) {
        modeloCarrito.setRowCount(0);
        for (CarritoItem item : items) {
            modeloCarrito.addRow(new Object[] {
                    item.getIsbn(), item.getTitulo(), item.getCantidad(),
                    UtilidadesGUI.formatearMoneda(item.getSubtotal()),
                    UtilidadesGUI.formatearMoneda(item.getImpuestoTotal()),
                    UtilidadesGUI.formatearMoneda(item.getTotal())
            });
        }
    }

    public void mostrarTotales(int cantidad, double subtotal, double impuestos,
                               double descuento, double total) {
        lblCantidadItems.setText(String.valueOf(cantidad));
        lblSubtotal.setText(UtilidadesGUI.formatearMoneda(subtotal));
        lblImpuestos.setText(UtilidadesGUI.formatearMoneda(impuestos));
        lblDescuento.setText(UtilidadesGUI.formatearMoneda(descuento));
        lblTotal.setText(UtilidadesGUI.formatearMoneda(total));
    }

    public void actualizarEstadoSesion(Cliente cliente) {
        boolean autenticado = cliente != null;
        btnFinalizar.setEnabled(autenticado);
        btnHistorial.setEnabled(autenticado);
        if (cliente == null) {
            lblSesion.setText("Sin sesión iniciada. Debe iniciar sesión para comprar.");
        } else {
            lblSesion.setText("Cliente: " + cliente.getNombreCompleto()
                    + " | Tipo: " + cliente.getTipoCliente().getEtiqueta());
        }
    }
}
