package co.uptc.edu.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Refrescable;
import co.uptc.edu.gui.Vista;
import co.uptc.edu.controlador.CarritoControlador;
import co.uptc.edu.model.ItemCarrito;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;


public class PantallaCarrito extends JPanel implements Refrescable {

    private final CarritoControlador carrito;
    private final Navegador nav;
    private final DefaultTableModel tablaModel;
    private final JTable tabla;
    private final JLabel lblSubtotal;
    private final JLabel lblIva;
    private final JLabel lblTotal;

    public PantallaCarrito(CarritoControlador carrito, Navegador nav) {
        super(new BorderLayout());
        this.carrito = carrito;
        this.nav = nav;

        // Encabezado + barra para ajustar cantidades y eliminar ítems (RF-05)
        JPanel norte = new JPanel(new BorderLayout());
        norte.add(EstiloUI.crearHeaderSuperior("Carrito de Compras (Simulación)"), BorderLayout.NORTH);

        JPanel panelItem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnMas = new JButton("+ Cantidad");
        JButton btnMenos = new JButton("- Cantidad");
        JButton btnCantidad = new JButton("Cambiar cantidad...");
        JButton btnEliminar = new JButton("Eliminar ítem");
        panelItem.add(btnMas);
        panelItem.add(btnMenos);
        panelItem.add(btnCantidad);
        panelItem.add(btnEliminar);
        norte.add(panelItem, BorderLayout.SOUTH);
        add(norte, BorderLayout.NORTH);

        String[] columnas = {"ID", "Título", "Precio Unitario", "Cantidad", "Subtotal"};
        tablaModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(tablaModel);
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelCheckout = new JPanel(new BorderLayout());
        panelCheckout.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelCheckout.setBackground(Color.WHITE);

        lblSubtotal = new JLabel("Subtotal: $0", SwingConstants.LEFT);
        lblIva = new JLabel("IVA (" + carrito.getPorcentajeIva() + "%): $0", SwingConstants.LEFT);
        lblTotal = new JLabel("Total: $0", SwingConstants.LEFT);
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel panelTotales = new JPanel(new GridLayout(3, 1));
        panelTotales.setOpaque(false);
        panelTotales.add(lblSubtotal);
        panelTotales.add(lblIva);
        panelTotales.add(lblTotal);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVaciar = new JButton("Vaciar Carrito");
        JButton btnVolver = new JButton("Seguir Comprando");
        JButton btnComprar = EstiloUI.crearBotonEstilizado("Finalizar Compra", EstiloUI.PRIMARIO);

        btnMas.addActionListener(e -> modificarSeleccionado(carrito::incrementar));
        btnMenos.addActionListener(e -> modificarSeleccionado(carrito::decrementar));
        btnCantidad.addActionListener(e -> cambiarCantidad());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnVaciar.addActionListener(e -> {
            carrito.vaciar();
            refrescar();
        });
        btnVolver.addActionListener(e -> nav.irA(Vista.CATALOGO));
        btnComprar.addActionListener(e -> finalizarCompra());

        panelAcciones.add(btnVaciar);
        panelAcciones.add(btnVolver);
        panelAcciones.add(btnComprar);

        panelCheckout.add(panelTotales, BorderLayout.WEST);
        panelCheckout.add(panelAcciones, BorderLayout.EAST);
        add(panelCheckout, BorderLayout.SOUTH);
    }

    @Override
    public void refrescar() {
        tablaModel.setRowCount(0);
        NumberFormat formatter = EstiloUI.formatoMoneda();

        for (ItemCarrito item : carrito.getItems()) {
            tablaModel.addRow(new Object[]{
                    item.getLibro().getIsbn(),
                    item.getLibro().getTituloLibro(),
                    formatter.format(item.getLibro().getPrecioVenta()),
                    item.getCantidad(),
                    formatter.format(item.getSubtotal())
            });
        }
        lblSubtotal.setText("Subtotal: " + formatter.format(carrito.getSubtotal()));
        lblIva.setText("IVA (" + carrito.getPorcentajeIva() + "%): " + formatter.format(carrito.getIva()));
        lblTotal.setText("Total a pagar: " + formatter.format(carrito.getTotal()));
    }

    //Devuelve el ISBN del ítem seleccionado o null (y avisa) si no hay selección
    private String isbnSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro del carrito.", "Atención", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return (String) tablaModel.getValueAt(fila, 0);
    }

    private interface AccionItem { void ejecutar(String isbn); }

    private void modificarSeleccionado(AccionItem accion) {
        String isbn = isbnSeleccionado();
        if (isbn == null) {
            return;
        }
        int fila = tabla.getSelectedRow();
        try {
            accion.ejecutar(isbn);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cantidad no válida", JOptionPane.WARNING_MESSAGE);
        }
        refrescar();
        if (fila >= 0 && fila < tablaModel.getRowCount()) {
            tabla.setRowSelectionInterval(fila, fila);
        }
    }

    private void cambiarCantidad() {
        String isbn = isbnSeleccionado();
        if (isbn == null) {
            return;
        }
        String texto = JOptionPane.showInputDialog(this, "Nueva cantidad:", "Ajustar cantidad", JOptionPane.QUESTION_MESSAGE);
        if (texto == null) {
            return;
        }
        modificarSeleccionado(id -> carrito.ajustarCantidad(id, texto));
    }

    private void eliminarSeleccionado() {
        String isbn = isbnSeleccionado();
        if (isbn == null) {
            return;
        }
        carrito.eliminar(isbn);
        refrescar();
    }

    private void finalizarCompra() {
        String comprador;
        try {
            comprador = carrito.finalizarCompra();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo finalizar la compra", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "¡Gracias por tu compra simulada!\nSe ha generado el recibo digital a nombre de: " + comprador,
                "Compra Finalizada", JOptionPane.INFORMATION_MESSAGE);

        refrescar();
        nav.irA(Vista.USUARIO_HOME);
    }
}
