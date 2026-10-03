package co.uptc.edu.negocio;


	import javax.swing.BorderFactory;
	import javax.swing.JButton;
	import javax.swing.JComboBox;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JOptionPane;
	import javax.swing.JPanel;
	import javax.swing.JScrollPane;
	import javax.swing.JTable;
	import javax.swing.JTextField;
	import javax.swing.ListSelectionModel;
	import javax.swing.SwingConstants;
	import javax.swing.table.DefaultTableModel;
	import java.awt.BorderLayout;
	import java.awt.FlowLayout;
	import java.awt.Font;
	import java.awt.GridLayout;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Locale;

	public class ventanaInventario extends JFrame {

	    private final JComboBox<Producto> selectorProductos;

	    private final JTextField campoPrecioBase;
	    private final JTextField campoStock;
	    private final JTextField campoDescuento;
	    private final JTextField campoIVA;
	    private final JTextField campoPrecioFinal;
	    private final JTextField campoCantidad;

	    private final JLabel etiquetaTotal;

	    private final JButton botonRegistrar;
	    private final JButton botonCancelar;

	    private final DefaultTableModel modeloTabla;
	    private final JTable tablaHistorial;

	    private final List<Pedido> historial;

	    public ventanaInventario() {
	        historial = new ArrayList<>();

	        setTitle("Control de Inventario y Registro de Ventas");
	        setSize(950, 600);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setLayout(new BorderLayout(10, 10));

	        JLabel titulo = new JLabel(
	                "CONTROL DE INVENTARIO Y VENTAS",
	                SwingConstants.CENTER
	        );

	        titulo.setFont(new Font("Arial", Font.BOLD, 22));
	        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

	        add(titulo, BorderLayout.NORTH);

	        selectorProductos = new JComboBox<>();

	        campoPrecioBase = crearCampoSoloLectura();
	        campoStock = crearCampoSoloLectura();
	        campoDescuento = crearCampoSoloLectura();
	        campoIVA = crearCampoSoloLectura();
	        campoPrecioFinal = crearCampoSoloLectura();
	        campoCantidad = new JTextField();

	        etiquetaTotal = new JLabel(
	                "Total: $0,00",
	                SwingConstants.CENTER
	        );

	        etiquetaTotal.setFont(new Font("Arial", Font.BOLD, 18));

	        botonRegistrar = new JButton("Registrar Pedido");
	        botonCancelar = new JButton("Cancelar Pedido Seleccionado");

	        String[] columnas = {
	                "N.º",
	                "Fecha",
	                "Producto",
	                "Cantidad",
	                "Precio unitario",
	                "Total"
	        };

	        modeloTabla = new DefaultTableModel(columnas, 0) {
	            @Override
	            public boolean isCellEditable(int fila, int columna) {
	                return false;
	            }
	        };

	        tablaHistorial = new JTable(modeloTabla);
	        tablaHistorial.setSelectionMode(
	                ListSelectionModel.SINGLE_SELECTION
	        );

	        construirPanelFormulario();
	        construirPanelHistorial();
	        cargarProductos();
	        registrarEventos();

	        actualizarInformacionProducto();
	    }

	    private JTextField crearCampoSoloLectura() {
	        JTextField campo = new JTextField();
	        campo.setEditable(false);
	        return campo;
	    }

	    private void construirPanelFormulario() {
	        JPanel panelFormulario = new JPanel(new GridLayout(8, 2, 8, 8));

	        panelFormulario.setBorder(
	                BorderFactory.createTitledBorder("Datos del pedido")
	        );

	        panelFormulario.add(new JLabel("Producto:"));
	        panelFormulario.add(selectorProductos);

	        panelFormulario.add(new JLabel("Precio base:"));
	        panelFormulario.add(campoPrecioBase);

	        panelFormulario.add(new JLabel("Stock disponible:"));
	        panelFormulario.add(campoStock);

	        panelFormulario.add(new JLabel("Descuento:"));
	        panelFormulario.add(campoDescuento);

	        panelFormulario.add(new JLabel("IVA:"));
	        panelFormulario.add(campoIVA);

	        panelFormulario.add(new JLabel("Precio final unitario:"));
	        panelFormulario.add(campoPrecioFinal);

	        panelFormulario.add(new JLabel("Cantidad:"));
	        panelFormulario.add(campoCantidad);

	        panelFormulario.add(botonRegistrar);
	        panelFormulario.add(etiquetaTotal);

	        JPanel contenedor = new JPanel(new BorderLayout());
	        contenedor.setBorder(
	                BorderFactory.createEmptyBorder(0, 15, 0, 15)
	        );
	        contenedor.add(panelFormulario, BorderLayout.NORTH);

	        add(contenedor, BorderLayout.WEST);
	    }

	    private void construirPanelHistorial() {
	        JPanel panelHistorial = new JPanel(new BorderLayout(5, 5));

	        panelHistorial.setBorder(
	                BorderFactory.createTitledBorder("Historial de pedidos")
	        );

	        panelHistorial.add(
	                new JScrollPane(tablaHistorial),
	                BorderLayout.CENTER
	        );

	        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        panelBoton.add(botonCancelar);

	        panelHistorial.add(panelBoton, BorderLayout.SOUTH);

	        add(panelHistorial, BorderLayout.CENTER);
	    }

	    private void cargarProductos() {
	        selectorProductos.addItem(
	                new Producto(
	                        1,
	                        "Computador portátil",
	                        2500000,
	                        8,
	                        10,
	                        19
	                )
	        );

	        selectorProductos.addItem(
	                new Producto(
	                        2,
	                        "Monitor",
	                        850000,
	                        15,
	                        5,
	                        19
	                )
	        );

	        selectorProductos.addItem(
	                new Producto(
	                        3,
	                        "Teclado",
	                        120000,
	                        25,
	                        0,
	                        19
	                )
	        );

	        selectorProductos.addItem(
	                new Producto(
	                        4,
	                        "Ratón inalámbrico",
	                        80000,
	                        30,
	                        8,
	                        19
	                )
	        );
	    }

	    private void registrarEventos() {
	        selectorProductos.addActionListener(
	                evento -> actualizarInformacionProducto()
	        );

	        botonRegistrar.addActionListener(
	                evento -> registrarPedido()
	        );

	        botonCancelar.addActionListener(
	                evento -> cancelarPedidoSeleccionado()
	        );

	        campoCantidad.addActionListener(
	                evento -> registrarPedido()
	        );
	    }

	    private Producto obtenerProductoSeleccionado() {
	        return (Producto) selectorProductos.getSelectedItem();
	    }

	    private void actualizarInformacionProducto() {
	        Producto producto = obtenerProductoSeleccionado();

	        if (producto == null) {
	            limpiarInformacion();
	            return;
	        }

	        campoPrecioBase.setText(formatearMoneda(
	                producto.getPrecioBase()
	        ));

	        campoStock.setText(String.valueOf(producto.getStock()));

	        campoDescuento.setText(
	                formatearPorcentaje(producto.getPorcentajeDescuento())
	        );

	        campoIVA.setText(
	                formatearPorcentaje(producto.getImpuestoIVA())
	        );

	        campoPrecioFinal.setText(
	                formatearMoneda(producto.calcularPrecioFinal())
	        );
	    }

	    private void limpiarInformacion() {
	        campoPrecioBase.setText("");
	        campoStock.setText("");
	        campoDescuento.setText("");
	        campoIVA.setText("");
	        campoPrecioFinal.setText("");
	        etiquetaTotal.setText("Total: $0,00");
	    }

	    private void registrarPedido() {
	        try {
	            Producto producto = obtenerProductoSeleccionado();

	            if (producto == null) {
	                throw new IllegalArgumentException(
	                        "Debe seleccionar un producto."
	                );
	            }

	            String textoCantidad = campoCantidad.getText().trim();

	            if (textoCantidad.isEmpty()) {
	                throw new IllegalArgumentException(
	                        "Debe ingresar una cantidad."
	                );
	            }

	            int cantidad;

	            try {
	                cantidad = Integer.parseInt(textoCantidad);
	            } catch (NumberFormatException excepcion) {
	                throw new IllegalArgumentException(
	                        "La cantidad debe ser un número entero."
	                );
	            }

	            if (cantidad <= 0) {
	                throw new IllegalArgumentException(
	                        "La cantidad debe ser mayor que cero."
	                );
	            }

	            if (cantidad > producto.getStock()) {
	                throw new IllegalArgumentException(
	                        "Stock insuficiente. Solo hay "
	                                + producto.getStock()
	                                + " unidades disponibles."
	                );
	            }

	            Pedido pedido = new Pedido(producto, cantidad);

	            producto.disminuirStock(cantidad);
	            historial.add(pedido);
	            agregarPedidoATabla(pedido);

	            etiquetaTotal.setText(
	                    "Total: " + formatearMoneda(pedido.getTotal())
	            );

	            actualizarInformacionProducto();
	            campoCantidad.setText("");
	            campoCantidad.requestFocus();

	            JOptionPane.showMessageDialog(
	                    this,
	                    "Pedido registrado correctamente.\n"
	                            + "Total: "
	                            + formatearMoneda(pedido.getTotal()),
	                    "Pedido exitoso",
	                    JOptionPane.INFORMATION_MESSAGE
	            );
	        } catch (IllegalArgumentException excepcion) {
	            mostrarError(excepcion.getMessage());
	        }
	    }

	    private void agregarPedidoATabla(Pedido pedido) {
	        modeloTabla.addRow(new Object[]{
	                pedido.getNumero(),
	                pedido.getFechaFormateada(),
	                pedido.getProducto().getNombre(),
	                pedido.getCantidad(),
	                formatearMoneda(pedido.getPrecioUnitario()),
	                formatearMoneda(pedido.getTotal())
	        });
	    }

	    private void cancelarPedidoSeleccionado() {
	        int filaSeleccionada = tablaHistorial.getSelectedRow();

	        if (filaSeleccionada == -1) {
	            mostrarError(
	                    "Debe seleccionar un pedido de la tabla."
	            );
	            return;
	        }

	        int respuesta = JOptionPane.showConfirmDialog(
	                this,
	                "¿Desea cancelar el pedido seleccionado?",
	                "Confirmar cancelación",
	                JOptionPane.YES_NO_OPTION,
	                JOptionPane.QUESTION_MESSAGE
	        );

	        if (respuesta != JOptionPane.YES_OPTION) {
	            return;
	        }

	        int filaModelo = tablaHistorial.convertRowIndexToModel(
	                filaSeleccionada
	        );

	        Pedido pedido = historial.get(filaModelo);

	        pedido.getProducto().aumentarStock(
	                pedido.getCantidad()
	        );

	        historial.remove(filaModelo);
	        modeloTabla.removeRow(filaModelo);

	        actualizarInformacionProducto();

	        JOptionPane.showMessageDialog(
	                this,
	                "Pedido cancelado. Se restauraron "
	                        + pedido.getCantidad()
	                        + " unidades al stock de "
	                        + pedido.getProducto().getNombre()
	                        + ".",
	                "Pedido cancelado",
	                JOptionPane.INFORMATION_MESSAGE
	        );
	    }

	    private String formatearMoneda(double valor) {
	        return String.format(
	                new Locale("es", "CO"),
	                "$%,.2f",
	                valor
	        );
	    }

	    private String formatearPorcentaje(double valor) {
	        return String.format(
	                new Locale("es", "CO"),
	                "%.2f %%",
	                valor
	        );
	    }

	    private void mostrarError(String mensaje) {
	        JOptionPane.showMessageDialog(
	                this,
	                mensaje,
	                "Error",
	                JOptionPane.ERROR_MESSAGE
	        );
	    }
	}

