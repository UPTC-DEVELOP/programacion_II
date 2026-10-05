package co.uptc.edu.pantallas;

import co.uptc.edu.gui.EstiloUI;
import co.uptc.edu.gui.Navegador;
import co.uptc.edu.gui.Refrescable;
import co.uptc.edu.gui.Vista;
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
    private final JLabel lblTotal;

    public PantallaCarrito(Tienda tienda, Navegador nav) {
        super(new BorderLayout());
        this.tienda = tienda;
        this.nav = nav;

        add(EstiloUI.crearHeaderSuperior("Carrito de Compras (Simulación)"), BorderLayout.NORTH);

        String[] columnas = {"ID", "Título", "Precio Unitario", "Cantidad", "Subtotal"};
        tablaModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabla = new JTable(tablaModel);
        tabla.setRowHeight(25);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelCheckout = new JPanel(new BorderLayout());
        panelCheckout.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelCheckout.setBackground(Color.WHITE);

        lblTotal = new JLabel("Total: $0", SwingConstants.LEFT);
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVaciar = new JButton("Vaciar Carrito");
        JButton btnVolver = new JButton("Seguir Comprando");
        JButton btnComprar = EstiloUI.crearBotonEstilizado("Finalizar Compra", EstiloUI.PRIMARIO);

        btnVaciar.addActionListener(e -> {
            tienda.vaciarCarrito();
            refrescar();
        });
        btnVolver.addActionListener(e -> nav.irA(Vista.CATALOGO));
        btnComprar.addActionListener(e -> finalizarCompra());

        panelAcciones.add(btnVaciar);
        panelAcciones.add(btnVolver);
        panelAcciones.add(btnComprar);

        panelCheckout.add(lblTotal, BorderLayout.WEST);
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
        lblTotal.setText("Total a pagar: " + formatter.format(tienda.getTotalCarrito()));
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
