package co.uptc.edu.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Refrescable;
import co.uptc.edu.gui.Vista;
import co.uptc.edu.model.CarritoDeCompras;
import co.uptc.edu.model.ItemCarrito;
import co.uptc.edu.model.Tienda;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;


public class PantallaCarrito extends JPanel implements Refrescable {

    private final Tienda tienda;
    private final Navegador nav;
    private final DefaultTableModel tablaModel;
    private final JTable tabla;
    private final JLabel lblSubtotal;
    private final JLabel lblIva;
    private final JLabel lblTotal;

    public PantallaCarrito(Tienda tienda, Navegador nav) {
        super(new BorderLayout());
        this.tienda = tienda;
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
        lblIva = new JLabel("IVA (" + (int) (CarritoDeCompras.TASA_IVA * 100) + "%): $0", SwingConstants.LEFT);
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

        btnMas.addActionListener(e -> modificarSeleccionado(isbn -> tienda.incrementarCantidadCarrito(isbn)));
        btnMenos.addActionListener(e -> modificarSeleccionado(isbn -> tienda.decrementarCantidadCarrito(isbn)));
        btnCantidad.addActionListener(e -> cambiarCantidad());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnVaciar.addActionListener(e -> {
            tienda.vaciarCarrito();
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

        for (ItemCarrito item : tienda.getCarrito()) {
            tablaModel.addRow(new Object[]{
                    item.getLibro().getIsbn(),
                    item.getLibro().getTituloLibro(),
                    formatter.format(item.getLibro().getPrecioVenta()),
                    item.getCantidad(),
                    formatter.format(item.getSubtotal())
            });
        }
        lblSubtotal.setText("Subtotal: " + formatter.format(tienda.getSubtotalCarrito()));
        lblIva.setText("IVA (" + (int) (CarritoDeCompras.TASA_IVA * 100) + "%): " + formatter.format(tienda.getIvaCarrito()));
        lblTotal.setText("Total a pagar: " + formatter.format(tienda.getTotalCarrito()));
    }

    /** Devuelve el ISBN del ítem seleccionado o null (y avisa) si no hay selección. */
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
        int cantidad;
        try {
            cantidad = Integer.parseInt(texto.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un número entero válido.", "Cantidad no válida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modificarSeleccionado(id -> tienda.ajustarCantidadCarrito(id, cantidad));
    }

    private void eliminarSeleccionado() {
        String isbn = isbnSeleccionado();
        if (isbn == null) {
            return;
        }
        tienda.eliminarDelCarrito(isbn);
        refrescar();
    }

    private void finalizarCompra() {
        if (tienda.getCarrito().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito se encuentra vacío.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tienda.finalizarCompra();

        JOptionPane.showMessageDialog(this,
                "🎉 ¡Gracias por tu compra simulada!\nSe ha generado el recibo digital a nombre de: " + tienda.getUsuarioActual(),
                "Compra Finalizada", JOptionPane.INFORMATION_MESSAGE);

        refrescar();
        nav.irA(Vista.USUARIO_HOME);
    }
}
