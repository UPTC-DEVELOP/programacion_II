package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.interfaces.IGestionCarrito;
import co.edu.uptc.model.Carrito;
import co.edu.uptc.service.GestionCarrito;

public class PanelCarrito extends JPanel {

    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregarManual, btnActualizarCantidad, btnEliminarItem, btnVaciarCarrito, btnRealizarCompra;
    private JLabel lblTotal;

    private PanelPedido panelPedido;
    private IGestionCarrito gestionCarrito;

    public PanelCarrito() {
        this.gestionCarrito = new GestionCarrito();
        setLayout(new BorderLayout(10, 10));

        String[] columnas = {"ISBN / ID", "Título", "Cantidad", "Precio Unitario", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCarrito = new JTable(modeloTabla);
        add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout());
        lblTotal = new JLabel("Total a Pagar: $0.00   ");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setHorizontalAlignment(JLabel.RIGHT);
        panelSur.add(lblTotal, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAgregarManual = new JButton("Agregar Libro Prueba");
        btnActualizarCantidad = new JButton("Actualizar Cantidad");
        btnEliminarItem = new JButton("Eliminar Seleccionado");
        btnVaciarCarrito = new JButton("Vaciar Carrito");
        btnRealizarCompra = new JButton("Realizar Compra");

        panelBotones.add(btnAgregarManual);
        panelBotones.add(btnActualizarCantidad);
        panelBotones.add(btnEliminarItem);
        panelBotones.add(btnVaciarCarrito);
        panelBotones.add(btnRealizarCompra);
        panelSur.add(panelBotones, BorderLayout.SOUTH);

        add(panelSur, BorderLayout.SOUTH);

        configurarEventosInternos();
        refrescarTabla();
    }

    public void setPanelPedido(PanelPedido panelPedido) {
        this.panelPedido = panelPedido;
    }

    public void refrescarTabla() {
        modeloTabla.setRowCount(0);
        List<Carrito> items = gestionCarrito.obtenerItems();
        if (items != null) {
            for (Carrito item : items) {
                double subtotal = item.getCantidad() * item.getPrecioUnitario();
                modeloTabla.addRow(new Object[]{
                    item.getIdProducto(),
                    item.getTitulo(),
                    item.getCantidad(),
                    String.format("$%.2f", item.getPrecioUnitario()),
                    String.format("$%.2f", subtotal)
                });
            }
        }
        actualizarTotal();
    }

    public void agregarLibroAlCarrito(String idProducto, String titulo, int cantidad, double precioUnitario) {
        try {
            gestionCarrito.agregarItem(idProducto, titulo, cantidad, precioUnitario);
            refrescarTabla();
            JOptionPane.showMessageDialog(this, "Producto agregado al carrito.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void configurarEventosInternos() {
        btnAgregarManual.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog(this, "Ingrese ISBN / ID del libro:", "978-0134685991");
            if (isbn == null) return;
            String titulo = JOptionPane.showInputDialog(this, "Ingrese Título del libro:", "Effective Java");
            if (titulo == null) return;
            String cantStr = JOptionPane.showInputDialog(this, "Ingrese Cantidad:", "1");
            if (cantStr == null) return;
            String precioStr = JOptionPane.showInputDialog(this, "Ingrese Precio Unitario:", "45.00");
            if (precioStr == null) return;

            try {
                int cant = Integer.parseInt(cantStr.trim());
                double precio = Double.parseDouble(precioStr.trim());
                agregarLibroAlCarrito(isbn, titulo, cant, precio);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad o precio con formato inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnActualizarCantidad.addActionListener(e -> {
            int fila = tablaCarrito.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un libro de la tabla para actualizar.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String nuevaCantStr = JOptionPane.showInputDialog(this, "Ingrese la nueva cantidad:");
            if (nuevaCantStr == null) return;

            try {
                int nuevaCant = Integer.parseInt(nuevaCantStr.trim());
                gestionCarrito.modificarCantidad(fila, nuevaCant);
                refrescarTabla();
                JOptionPane.showMessageDialog(this, "Cantidad actualizada correctamente.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnEliminarItem.addActionListener(e -> {
            int fila = tablaCarrito.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un libro de la tabla para eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                gestionCarrito.eliminarItem(fila);
                refrescarTabla();
                JOptionPane.showMessageDialog(this, "Libro eliminado del carrito.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVaciarCarrito.addActionListener(e -> {
            if (gestionCarrito.estaVacio()) {
                JOptionPane.showMessageDialog(this, "El carrito ya está vacío.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Desea vaciar todo el carrito?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                gestionCarrito.vaciarCarrito();
                refrescarTabla();
            }
        });

        btnRealizarCompra.addActionListener(e -> {
            try {
                if (gestionCarrito.estaVacio()) {
                    throw new Exception("El carrito está vacío. Agregue productos antes de realizar la compra.");
                }
                String cliente = JOptionPane.showInputDialog(this, "Ingrese el nombre del cliente para el pedido:");
                if (cliente == null) return;

                String[] datosPedido = gestionCarrito.procesarCompra(cliente);

                if (panelPedido != null) {
                    panelPedido.agregarPedido(
                        datosPedido[0],
                        datosPedido[1],
                        datosPedido[2],
                        datosPedido[3],
                        datosPedido[4]
                    );
                }

                refrescarTabla();
                JOptionPane.showMessageDialog(
                    this,
                    "¡Compra registrada con éxito!\nPedido: " + datosPedido[0] + " | Total: " + datosPedido[3],
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación de Compra", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public void agregarOyentes(ActionListener listener) {
        btnAgregarManual.addActionListener(listener);
        btnActualizarCantidad.addActionListener(listener);
        btnEliminarItem.addActionListener(listener);
        btnVaciarCarrito.addActionListener(listener);
        btnRealizarCompra.addActionListener(listener);
    }

    public void actualizarTotal() {
        double total = gestionCarrito.calcularTotal();
        lblTotal.setText("Total a Pagar: $" + String.format("%.2f", total) + "   ");
    }

    public IGestionCarrito getGestionCarrito() {
        return gestionCarrito;
    }
}