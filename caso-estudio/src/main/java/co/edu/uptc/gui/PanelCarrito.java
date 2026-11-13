package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.model.Carrito;
import co.edu.uptc.repository.CarritoJsonRepositorio;

public class PanelCarrito extends JPanel {

    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregarManual, btnEliminarItem, btnVaciarCarrito, btnRealizarCompra;
    private JLabel lblTotal;
    
    private PanelPedido panelPedido;
    private CarritoJsonRepositorio carritoRepo;

    public PanelCarrito() {
        this.carritoRepo = new CarritoJsonRepositorio();
        setLayout(new BorderLayout(10, 10));

        // 1. Configuración de la tabla del carrito
        String[] columnas = {"ISBN / ID", "Título", "Cantidad", "Precio Unitario", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCarrito = new JTable(modeloTabla);
        add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        // 2. Panel inferior (Total + Botones de Acción)
        JPanel panelInferior = new JPanel(new BorderLayout());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 15));

        btnAgregarManual = new JButton("Agregar Libro");
        btnEliminarItem = new JButton("Eliminar Seleccionado");
        btnVaciarCarrito = new JButton("Vaciar Carrito");
        btnRealizarCompra = new JButton("Realizar Compra");

        // ActionCommands para el controlador central (MVC)
        btnAgregarManual.setActionCommand("AGREGAR_CARRITO");
        btnEliminarItem.setActionCommand("ELIMINAR_CARRITO");
        btnVaciarCarrito.setActionCommand("VACIAR_CARRITO");
        btnRealizarCompra.setActionCommand("COMPRAR_CARRITO");

        // Listeners locales
        btnAgregarManual.addActionListener(e -> agregarLibroManual());
        btnEliminarItem.addActionListener(e -> eliminarItemSeleccionado());
        btnVaciarCarrito.addActionListener(e -> vaciarCarrito());
        btnRealizarCompra.addActionListener(e -> realizarCompra());

        panelBotones.add(btnAgregarManual);
        panelBotones.add(btnEliminarItem);
        panelBotones.add(btnVaciarCarrito);
        panelBotones.add(btnRealizarCompra);

        panelInferior.add(lblTotal, BorderLayout.WEST);
        panelInferior.add(panelBotones, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);

        // Cargar registros persistidos en data/carrito.json
        cargarCarrito();
    }

    public void setPanelPedido(PanelPedido panelPedido) {
        this.panelPedido = panelPedido;
    }

    // Carga los elementos guardados desde el archivo JSON al iniciar
    public void cargarCarrito() {
        modeloTabla.setRowCount(0);
        List<Carrito> items = carritoRepo.cargar();
        for (Carrito item : items) {
            double subtotal = item.getCantidad() * item.getPrecioUnitario();
            modeloTabla.addRow(new Object[]{
                item.getIdProducto(),
                item.getTitulo(),
                item.getCantidad(),
                "$" + item.getPrecioUnitario(),
                "$" + subtotal
            });
        }
        actualizarTotal();
    }

    // Actualiza el archivo data/carrito.json tras cualquier cambio en la tabla
    private void guardarEnJson() {
        List<Carrito> lista = new ArrayList<>();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String id = modeloTabla.getValueAt(i, 0).toString();
            String titulo = modeloTabla.getValueAt(i, 1).toString();
            int cantidad = Integer.parseInt(modeloTabla.getValueAt(i, 2).toString());
            String precioStr = modeloTabla.getValueAt(i, 3).toString().replace("$", "").trim();
            double precio = Double.parseDouble(precioStr);

            lista.add(new Carrito(id, titulo, cantidad, precio));
        }
        carritoRepo.guardar(lista);
    }

    // Agregar producto desde un diálogo flotante
    private void agregarLibroManual() {
        String id = JOptionPane.showInputDialog(this, "Ingrese el ISBN / ID del libro:");
        if (id == null || id.trim().isEmpty()) return;

        String titulo = JOptionPane.showInputDialog(this, "Ingrese el título del libro:");
        if (titulo == null || titulo.trim().isEmpty()) return;

        String cantStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad:");
        if (cantStr == null || cantStr.trim().isEmpty()) return;

        String precioStr = JOptionPane.showInputDialog(this, "Ingrese el precio unitario:");
        if (precioStr == null || precioStr.trim().isEmpty()) return;

        try {
            int cantidad = Integer.parseInt(cantStr.trim());
            double precio = Double.parseDouble(precioStr.trim());
            
            agregarItem(id.trim(), titulo.trim(), cantidad, precio);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser numéricos.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método público invocado desde el catálogo de libros o desde el controlador
    public void agregarItem(String id, String titulo, int cantidad, double precioUnitario) {
        double subtotal = cantidad * precioUnitario;
        modeloTabla.addRow(new Object[]{id, titulo, cantidad, "$" + precioUnitario, "$" + subtotal});
        actualizarTotal();
        guardarEnJson();
    }

    private void eliminarItemSeleccionado() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila >= 0) {
            modeloTabla.removeRow(fila);
            actualizarTotal();
            guardarEnJson();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un libro del carrito para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void vaciarCarrito() {
        modeloTabla.setRowCount(0);
        actualizarTotal();
        guardarEnJson();
    }

    private void realizarCompra() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío. Agregue productos antes de realizar la compra.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cliente = JOptionPane.showInputDialog(this, "Ingrese el nombre del cliente:", "Realizar Compra", JOptionPane.QUESTION_MESSAGE);
        
        if (cliente != null && !cliente.trim().isEmpty()) {
            double total = obtenerTotalCalculado();
            String totalFormateado = "$" + String.format("%.2f", total);
            String fechaActual = LocalDate.now().toString();

            // Si está vinculado con PanelPedido, transferir la compra
            if (panelPedido != null) {
                String idPedido = String.format("PED-%04d", (int)(Math.random() * 9000 + 1000));
                panelPedido.agregarPedido(idPedido, cliente.trim(), fechaActual, totalFormateado, "Pendiente");
            }

            // Vaciar el carrito y actualizar data/carrito.json
            vaciarCarrito();
            
            JOptionPane.showMessageDialog(this, "¡Compra registrada con éxito!\nRevisa la pestaña 'Pedidos' para ver el historial.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarTotal() {
        double total = obtenerTotalCalculado();
        lblTotal.setText("Total: $" + String.format("%.2f", total));
    }

    private double obtenerTotalCalculado() {
        double total = 0;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String subtotalStr = modeloTabla.getValueAt(i, 4).toString().replace("$", "").trim();
            try {
                total += Double.parseDouble(subtotalStr);
            } catch (NumberFormatException e) {
                // ignorar formato inválido
            }
        }
        return total;
    }

    // Registro de oyentes externos para el Controlador principal
    public void agregarOyentes(ActionListener listener) {
        btnAgregarManual.addActionListener(listener);
        btnEliminarItem.addActionListener(listener);
        btnVaciarCarrito.addActionListener(listener);
        btnRealizarCompra.addActionListener(listener);
    }
}