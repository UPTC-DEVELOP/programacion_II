package co.uptc.edu.caso.estudio.gui;

import co.uptc.edu.caso.estudio.controller.CompraController;
import co.uptc.edu.caso.estudio.controller.LibroController;
import co.uptc.edu.caso.estudio.negocio.Cliente;
import co.uptc.edu.caso.estudio.negocio.Compra;
import co.uptc.edu.caso.estudio.negocio.DetalleCompra;
import co.uptc.edu.caso.estudio.negocio.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaCompra extends JFrame {

    private JTextField txtIdCompra;
    private JTextField txtIsbnLibro;
    private JTextField txtCantidad;
    private JTextField txtEstado;
    private JComboBox<String> cmbMetodoPago;

    private JLabel lblClienteInfo;
    private JLabel lblTotalCompra;

    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private JTextArea areaSalida;

    private LibroController libroController;
    private CompraController compraController;
    private Cliente clienteActivo;
    private Compra compraActual;

    public VentanaCompra(LibroController libroController, CompraController compraController, Cliente clienteActivo) {
        this.libroController = libroController;
        this.compraController = compraController;
        this.clienteActivo = clienteActivo;
        this.compraActual = new Compra();
        this.compraActual.setCliente(clienteActivo);

        setTitle("Tienda Virtual - Carrito y Gestión de Compras");
        setSize(820, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        listarTodas();
    }

    private void inicializarComponentes() {
        // PANEL SUPERIOR: Datos de Compra + Agregar Libro al Carrito
        JPanel panelNorte = new JPanel(new BorderLayout(5, 5));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));

        String infoCli = (clienteActivo != null)
                ? "Cliente: " + clienteActivo.getNombreCompleto() + " | Tipo: " + clienteActivo.getTipoCliente()
                : "Cliente: Invitado";
        lblClienteInfo = new JLabel(infoCli, SwingConstants.CENTER);
        lblClienteInfo.setFont(new Font("Arial", Font.BOLD, 14));
        panelNorte.add(lblClienteInfo, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new GridLayout(3, 4, 8, 8));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos de la Compra y Selección de Libros"));

        panelCampos.add(new JLabel("ID Compra:"));
        txtIdCompra = new JTextField();
        panelCampos.add(txtIdCompra);

        panelCampos.add(new JLabel("Método de Pago:"));
        cmbMetodoPago = new JComboBox<>(new String[]{"Tarjeta de Crédito", "Tarjeta Débito", "PSE", "Efectivo"});
        panelCampos.add(cmbMetodoPago);

        panelCampos.add(new JLabel("ISBN del Libro:"));
        txtIsbnLibro = new JTextField();
        panelCampos.add(txtIsbnLibro);

        panelCampos.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField("1");
        panelCampos.add(txtCantidad);

        panelCampos.add(new JLabel("Estado (Buscar/Act):"));
        txtEstado = new JTextField("Confirmada");
        panelCampos.add(txtEstado);

        JButton btnAgregarLibro = new JButton("+ Agregar al Carrito");
        JButton btnQuitarLibro = new JButton("- Quitar Seleccionado");
        panelCampos.add(btnAgregarLibro);
        panelCampos.add(btnQuitarLibro);

        panelNorte.add(panelCampos, BorderLayout.CENTER);
        add(panelNorte, BorderLayout.NORTH);

        // PANEL CENTRAL DIVIDIDO: Arriba Tabla Carrito, Abajo Historial/Recibo
        JSplitPane splitCentral = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitCentral.setResizeWeight(0.45);

        modeloCarrito = new DefaultTableModel(
                new Object[]{"ISBN", "Título", "Cantidad", "Precio Unitario", "Subtotal"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCarrito = new JTable(modeloCarrito);
        JPanel panelCarrito = new JPanel(new BorderLayout());
        panelCarrito.setBorder(BorderFactory.createTitledBorder("Carrito de Compras Actual (DetalleCompra)"));
        panelCarrito.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        lblTotalCompra = new JLabel("Total a Pagar: $0.00", SwingConstants.RIGHT);
        lblTotalCompra.setFont(new Font("Arial", Font.BOLD, 15));
        lblTotalCompra.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 15));
        panelCarrito.add(lblTotalCompra, BorderLayout.SOUTH);

        splitCentral.setTopComponent(panelCarrito);

        areaSalida = new JTextArea();
        areaSalida.setEditable(false);
        JScrollPane scrollSalida = new JScrollPane(areaSalida);
        scrollSalida.setBorder(BorderFactory.createTitledBorder("Recibo e Historial de Compras (MariaDB + JSON)"));
        splitCentral.setBottomComponent(scrollSalida);

        add(splitCentral, BorderLayout.CENTER);

        // PANEL INFERIOR: Botones CRUD y Navegación
        JPanel panelBotones = new JPanel(new FlowLayout());

        JButton btnConfirmar = new JButton("Confirmar Compra (C)");
        JButton btnBuscar = new JButton("Buscar (R)");
        JButton btnActualizar = new JButton("Actualizar Estado (U)");
        JButton btnEliminar = new JButton("Eliminar (D)");
        JButton btnListar = new JButton("Listar Todo");
        JButton btnLibros = new JButton("Catálogo / Gestión Libros");

        panelBotones.add(btnConfirmar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnListar);
        panelBotones.add(btnLibros);

        add(panelBotones, BorderLayout.SOUTH);

        // EVENTOS
        btnAgregarLibro.addActionListener(e -> agregarLibroAlCarrito());
        btnQuitarLibro.addActionListener(e -> quitarLibroDelCarrito());
        btnConfirmar.addActionListener(e -> confirmarCompra());
        btnBuscar.addActionListener(e -> buscarCompra());
        btnActualizar.addActionListener(e -> actualizarEstado());
        btnEliminar.addActionListener(e -> eliminarCompra());
        btnListar.addActionListener(e -> listarTodas());
        btnLibros.addActionListener(e -> {
            GestionLibro ventanaLibros = new GestionLibro(libroController);
            ventanaLibros.setVisible(true);
        });
    }

    private void agregarLibroAlCarrito() {
        String isbn = txtIsbnLibro.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ISBN del libro a comprar.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero.");
            return;
        }

        Libro libro = libroController.buscarPorIsbn(isbn);
        if (libro == null) {
            JOptionPane.showMessageDialog(this, "No se encontró ningún libro con ISBN: " + isbn
                    + "\n(Puedes crear o consultar ISBNs en el botón 'Catálogo / Gestión Libros')");
            return;
        }

        if (cantidad > libro.getCantidadInventario()) {
            JOptionPane.showMessageDialog(this, "Stock insuficiente. Solo hay "
                    + libro.getCantidadInventario() + " unidades disponibles.");
            return;
        }

        int idDetalle = compraActual.getDetalles().size() + 1;
        DetalleCompra detalle = new DetalleCompra(idDetalle, cantidad, libro.getPrecioVenta(), libro);
        compraActual.agregarDetalle(detalle);

        refrescarTablaCarrito();
        txtIsbnLibro.setText("");
        txtCantidad.setText("1");
    }

    private void quitarLibroDelCarrito() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro de la tabla del carrito para quitarlo.");
            return;
        }
        compraActual.eliminarDetalle(fila);
        refrescarTablaCarrito();
    }

    private void refrescarTablaCarrito() {
        modeloCarrito.setRowCount(0);
        for (DetalleCompra d : compraActual.getDetalles()) {
            modeloCarrito.addRow(new Object[]{
                    d.getLibro().getIsbn(),
                    d.getLibro().getTitulo(),
                    d.getCantidad(),
                    String.format("$%.2f", d.getPrecioUnitario()),
                    String.format("$%.2f", d.calcularSubtotal())
            });
        }
        double total = compraActual.calcularTotal();
        String textoDescuento = compraActual.getDescuento() > 0
                ? " (Incluye 10% Dto. Premium: -$" + String.format("%.2f", compraActual.getDescuento()) + ")"
                : "";
        lblTotalCompra.setText("Total a Pagar: $" + String.format("%.2f", total) + textoDescuento);
    }

    private void confirmarCompra() {
        try {
            int idCompra = Integer.parseInt(txtIdCompra.getText().trim());
            compraActual.setIdCompra(idCompra);
            String metodoPago = (String) cmbMetodoPago.getSelectedItem();

            String resultado = compraController.registrarCompra(compraActual, metodoPago);
            if ("OK".equals(resultado)) {
                JOptionPane.showMessageDialog(this,
                        "¡Compra #" + idCompra + " confirmada!\n"
                        + "Total pagado: $" + String.format("%.2f", compraActual.getTotal())
                        + "\nInventario actualizado correctamente.");

                // Reiniciar carrito para nueva compra
                compraActual = new Compra();
                compraActual.setCliente(clienteActivo);
                refrescarTablaCarrito();
                txtIdCompra.setText("");
                listarTodas();
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Error en la compra", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID de compra numérico válido.");
        }
    }

    private void buscarCompra() {
        try {
            int id = Integer.parseInt(txtIdCompra.getText().trim());
            Compra buscada = compraController.buscarPorId(id);

            if (buscada != null) {
                txtEstado.setText(buscada.getEstado());
                StringBuilder sb = new StringBuilder();
                sb.append("=== DETALLE DE COMPRA #").append(buscada.getIdCompra()).append(" ===\n");
                sb.append(buscada.generarResumen()).append("\n\nLibros comprados:\n");
                for (DetalleCompra d : buscada.getDetalles()) {
                    sb.append(" - ISBN: ").append(d.getLibro().getIsbn())
                      .append(" | ").append(d.getLibro().getTitulo())
                      .append(" | Cant: ").append(d.getCantidad())
                      .append(" | P.Unit: $").append(d.getPrecioUnitario())
                      .append(" | Subtotal: $").append(d.getSubtotal()).append("\n");
                }
                areaSalida.setText(sb.toString());
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ninguna compra con el ID " + id);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido para buscar.");
        }
    }

    private void actualizarEstado() {
        try {
            int id = Integer.parseInt(txtIdCompra.getText().trim());
            String nuevoEstado = txtEstado.getText().trim();
            if (compraController.actualizarEstado(id, nuevoEstado)) {
                JOptionPane.showMessageDialog(this, "Estado actualizado en BD y JSON.");
                listarTodas();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar. Verifique el ID.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido.");
        }
    }

    private void eliminarCompra() {
        try {
            int id = Integer.parseInt(txtIdCompra.getText().trim());
            if (compraController.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Compra eliminada de BD y JSON.");
                txtIdCompra.setText("");
                listarTodas();
            } else {
                JOptionPane.showMessageDialog(this, "No existe una compra con ese ID.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido para eliminar.");
        }
    }

    private void listarTodas() {
        List<Compra> comprasBD = compraController.listar();
        areaSalida.setText("COMPRAS REGISTRADAS EN MARIADB (" + comprasBD.size() + "):\n");

        for (Compra c : comprasBD) {
            areaSalida.append(c.generarResumen() + "\n");
            for (DetalleCompra d : c.getDetalles()) {
                areaSalida.append("    -> Libro: " + d.getLibro().getTitulo()
                        + " (ISBN: " + d.getLibro().getIsbn() + ") x" + d.getCantidad()
                        + " = $" + String.format("%.2f", d.getSubtotal()) + "\n");
            }
        }

        areaSalida.append("\n--- RESPALDO LEÍDO DESDE datos/compras.json ---\n");
        for (Compra c : compraController.listarJson()) {
            areaSalida.append("[JSON] " + c.generarResumen() + "\n");
        }
    }
}