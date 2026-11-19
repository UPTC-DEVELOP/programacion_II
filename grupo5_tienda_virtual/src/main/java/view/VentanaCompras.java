package view;

import java.awt.BorderLayout;
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

import service.IServicioCompras;
import service.IServicioLibros;
import model.CarritoCompras;
import model.Cliente;
import model.Libro;
import model.MetodoPago;


public class VentanaCompras extends JFrame {

    
	private static final long serialVersionUID = 1L;

	private CarritoCompras carrito;
	
	// CLIENTE ACTUAL Y CONTROLADOR DE LIBROS
	private Cliente cliente;
	private IServicioLibros controladorLibro;
	private IServicioCompras controladorCompras;
	
	
    // Campos de selección
    private JComboBox<Libro> cmbLibro;
    private JTextField txtCantidad;

    // Tabla del carrito
    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;

    // Información de la compra
    private JLabel lblSubtotal;
    private JLabel lblIva19;
    private JLabel lblIva5;
    private JLabel lblOtrosIva;
    private JLabel lblDescuentoPremium;
    private JLabel lblTotal;

    // Método de pago
    private JComboBox<MetodoPago> cmbMetodoPago;

    // Botones
    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnVaciar;
    private JButton btnComprar;

 // RECIBIR EL CLIENTE Y LOS CONTROLADORES
    public VentanaCompras(Cliente cliente, IServicioLibros controladorLibro,
            IServicioCompras controladorCompras) {

        this.cliente = cliente;
        this.controladorLibro = controladorLibro;
        this.controladorCompras = controladorCompras;
        carrito = new CarritoCompras();

        setTitle("Gestion de Compras");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(crearPanelSuperior(), BorderLayout.NORTH);

        // CARGAR LOS LIBROS DISPONIBLES EN EL COMBOBOX
        for (Libro libro : controladorLibro.listar()) {
            cmbLibro.addItem(libro);
        }

        add(crearTablaCarrito(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        
        new EventosCompras(this, controladorCompras).registrarEventos();
    }
    
    
    // CREAR PANEL SUPERIOR
    	private JPanel crearPanelSuperior() {

    	    JPanel panelPrincipal = new JPanel(new BorderLayout(6, 6));

    	    // FILA SUPERIOR: LIBRO Y CANTIDAD
    	    JPanel panelDatos = new JPanel(new GridLayout(1, 4, 6, 6));

    	    cmbLibro = new JComboBox<Libro>();
    	    txtCantidad = new JTextField();

    	    panelDatos.add(new JLabel("Libro:"));
    	    panelDatos.add(cmbLibro);

    	    panelDatos.add(new JLabel("Cantidad:"));
    	    panelDatos.add(txtCantidad);

    	    // FILA INFERIOR: BOTONES
    	    JPanel panelBotones = new JPanel(new GridLayout(1, 2, 6, 6));

    	    btnAgregar = new JButton("Agregar al carrito");
    	    btnModificar = new JButton("Modificar cantidad");

    	    panelBotones.add(btnAgregar);
    	    panelBotones.add(btnModificar);

    	    panelPrincipal.add(panelDatos, BorderLayout.NORTH);
    	    panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

    	    return panelPrincipal;
    	}

    private JScrollPane crearTablaCarrito() {

        modeloTabla = new DefaultTableModel(
                new Object[] {
                        "Libro",
                        "Cantidad",
                        "Precio",
                        "Subtotal"
                }, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCarrito = new JTable(modeloTabla);

        return new JScrollPane(tablaCarrito);
    }

    private JPanel crearPanelInferior() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(6, 6));

     // CREAR PANEL DE TOTALES
        JPanel panelTotales = new JPanel(new GridLayout(6, 2, 6, 6));

        lblSubtotal = new JLabel("$0.00");
        lblIva19 = new JLabel("$0.00");
        lblIva5 = new JLabel("$0.00");
        lblOtrosIva = new JLabel("$0.00");
        lblDescuentoPremium = new JLabel("$0.00");
        lblTotal = new JLabel("$0.00");

        panelTotales.add(new JLabel("Subtotal:"));
        panelTotales.add(lblSubtotal);

        panelTotales.add(new JLabel("IVA 19%:"));
        panelTotales.add(lblIva19);

        panelTotales.add(new JLabel("IVA 5%:"));
        panelTotales.add(lblIva5);

        panelTotales.add(new JLabel("Otros IVA:"));
        panelTotales.add(lblOtrosIva);
        
     // MOSTRAR DESCUENTO PREMIUM
        panelTotales.add(new JLabel("Descuento Premium:"));
        panelTotales.add(lblDescuentoPremium);

        panelTotales.add(new JLabel("TOTAL:"));
        panelTotales.add(lblTotal);
        
        
        JPanel panelPago = new JPanel(new GridLayout(1, 2, 6, 6));

        cmbMetodoPago = new JComboBox<MetodoPago>(
                new MetodoPago[] {
                        new MetodoPago("Efectivo", "Pago en efectivo"),
                        new MetodoPago("Tarjeta", "Pago con tarjeta"),
                        new MetodoPago("Transferencia", "Pago mediante transferencia")
                });

        panelPago.add(new JLabel("Método de pago:"));
        panelPago.add(cmbMetodoPago);

        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 6, 6));

        btnEliminar = new JButton("Eliminar");
        btnVaciar = new JButton("Vaciar carrito");
        btnComprar = new JButton("Finalizar compra");

        panelBotones.add(btnEliminar);
        panelBotones.add(btnVaciar);
        panelBotones.add(btnComprar);

        JPanel panelFinal = new JPanel(new BorderLayout(6, 6));

        panelFinal.add(panelPago, BorderLayout.NORTH);
        panelFinal.add(panelBotones, BorderLayout.SOUTH);

        panelPrincipal.add(panelTotales, BorderLayout.NORTH);
        panelPrincipal.add(panelFinal, BorderLayout.SOUTH);

        return panelPrincipal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public CarritoCompras getCarrito() {
        return carrito;
    }

    public JComboBox<Libro> getCmbLibro() {
        return cmbLibro;
    }

    public JTextField getTxtCantidad() {
        return txtCantidad;
    }

    public JTable getTablaCarrito() {
        return tablaCarrito;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JLabel getLblSubtotal() {
        return lblSubtotal;
    }

    public JLabel getLblIva19() {
        return lblIva19;
    }

    public JLabel getLblIva5() {
        return lblIva5;
    }

    public JLabel getLblOtrosIva() {
        return lblOtrosIva;
    }
    
    public JLabel getLblDescuentoPremium() {
        return lblDescuentoPremium;
    }

    public JLabel getLblTotal() {
        return lblTotal;
    }

    public JComboBox<MetodoPago> getCmbMetodoPago() {
        return cmbMetodoPago;
    }

    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public JButton getBtnModificar() {
        return btnModificar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnVaciar() {
        return btnVaciar;
    }

    public JButton getBtnComprar() {
        return btnComprar;
    }
}