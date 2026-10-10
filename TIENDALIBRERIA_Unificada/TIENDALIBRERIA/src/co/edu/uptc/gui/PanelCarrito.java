package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import co.edu.uptc.negocio.Carrito;
import co.uptc.edu.libro.modelo.Libro;

public class PanelCarrito extends JPanel {

    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;
    private JLabel labelSubtotal, labelImpuestos, labelDescuento, labelTotal;
    private JCheckBox chkEsPremium;
    private JButton btnComprar, btnVaciar, btnEliminar, btnVolver;
    private Carrito carrito;
    
    // Referencia al panel de pedidos para comunicar la compra
    private PanelPedido panelPedido;

    public PanelCarrito() {
        setLayout(new BorderLayout(10, 10));

        carrito = new Carrito();

        // --- LÍNEA DE PRUEBA TEMPORAL ---
        Libro libroPrueba = new Libro("978-958-0", "Cien años de soledad", "Gabriel García Márquez", 1967, null, "Editorial Planeta", 350, 50000.0, 5, null);
        carrito.getItems().add(libroPrueba);
        // ---------------------------------

        // Configuración de la tabla
        String[] columnas = {"ISBN", "Título", "Autor", "Precio Venta", "Formato"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaCarrito = new JTable(modeloTabla);
        add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        // Panel superior para opciones (cliente premium)
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkEsPremium = new JCheckBox("¿Cliente Premium? (10% de descuento)");
        chkEsPremium.addActionListener(e -> actualizarTabla());
        panelNorte.add(chkEsPremium);
        add(panelNorte, BorderLayout.NORTH);

        // Panel inferior para totales y botones de acción
        JPanel panelSur = new JPanel(new GridLayout(2, 1));

        // Subpanel de totales
        JPanel panelTotales = new JPanel(new GridLayout(4, 1, 2, 2));
        labelSubtotal = new JLabel("Subtotal: $0.00");
        labelImpuestos = new JLabel("Impuestos (IVA): $0.00");
        labelDescuento = new JLabel("Descuento Premium (10%): -$0.00");
        labelTotal = new JLabel("TOTAL A PAGAR: $0.00");
        
        Font fuenteTotal = new Font("Arial", Font.BOLD, 12);
        labelTotal.setFont(fuenteTotal);

        panelTotales.add(labelSubtotal);
        panelTotales.add(labelImpuestos);
        panelTotales.add(labelDescuento);
        panelTotales.add(labelTotal);
        panelSur.add(panelTotales);

        // Subpanel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEliminar = new JButton("Eliminar del Carrito");
        btnVaciar = new JButton("Vaciar Carrito");
        btnComprar = new JButton("Realizar Compra");

        btnEliminar.addActionListener(e -> eliminarLibroSeleccionado());
        btnVaciar.addActionListener(e -> {
            carrito.vaciarCarrito();
            actualizarTabla();
        });
        btnComprar.addActionListener(e -> realizarCompra());

        panelBotones.add(btnEliminar);
        panelBotones.add(btnVaciar);
        panelBotones.add(btnComprar);
        panelSur.add(panelBotones);

        add(panelSur, BorderLayout.SOUTH);
        
        actualizarTabla();
    }

    // Método para conectar el panel de pedidos
    public void setPanelPedido(PanelPedido panelPedido) {
        this.panelPedido = panelPedido;
    }

    // Método clave para recibir libros desde el catálogo o panel de libros
    public void agregarLibro(Libro libro) {
        carrito.getItems().add(libro);
        actualizarTabla();
    }

    private void eliminarLibroSeleccionado() {
        int filaSeleccionada = tablaCarrito.getSelectedRow();
        if (filaSeleccionada >= 0) {
            carrito.getItems().remove(filaSeleccionada);
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para eliminar del carrito.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void realizarCompra() {
        if (carrito.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Generamos los datos del pedido
        String idPedido = "P-" + (int)(Math.random() * 1000);
        String fechaActual = "2026-10-09"; 
        String totalPagar = labelTotal.getText().replace("TOTAL A PAGAR: ", "");

        // Enviamos el pedido al panel de pedidos si está conectado
        if (panelPedido != null) {
            panelPedido.agregarPedido(idPedido, "Cliente General", fechaActual, totalPagar, "Pendiente");
        }

        JOptionPane.showMessageDialog(this, String.format("¡Compra realizada con éxito!\nPedido registrado: %s", idPedido));
        
        carrito.vaciarCarrito();
        actualizarTabla();
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        carrito.setEsPremium(chkEsPremium.isSelected());

        for (Libro libro : carrito.getItems()) {
            Object[] fila = {
                libro.getIsbn(),
                libro.getTituloLibro(),
                libro.getAutor(),
                "$" + libro.getPrecioVenta(),
                libro.getFormato()
            };
            modeloTabla.addRow(fila);
        }

        labelSubtotal.setText(String.format("Subtotal: $%.2f", carrito.calcularSubtotal()));
        labelImpuestos.setText(String.format("Impuestos (IVA): $%.2f", carrito.calcularImpuestos()));
        labelDescuento.setText(String.format("Descuento Premium (10%%): -$%.2f", carrito.calcularDescuento()));
        labelTotal.setText(String.format("TOTAL A PAGAR: $%.2f", carrito.calcularTotalFinal()));
    }
}