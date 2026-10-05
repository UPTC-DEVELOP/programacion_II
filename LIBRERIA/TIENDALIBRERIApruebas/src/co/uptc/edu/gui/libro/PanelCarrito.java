package co.uptc.edu.gui.libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import co.uptc.edu.libro.negocio.Carrito;
import co.uptc.edu.libro.modelo.Libro;

public class PanelCarrito extends JPanel {

    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;
    private JLabel labelSubtotal, labelImpuestos, labelDescuento, labelTotal;
    private JCheckBox chkEsPremium;
    private JButton btnComprar, btnVaciar, btnEliminar, btnVolver;
    private Carrito carrito;

    public PanelCarrito(Evento evento) {
        setLayout(new BorderLayout(10, 10));
        carrito = new Carrito();

        String[] columnas = {"ISBN", "Título", "Autor", "Precio Venta", "Formato"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCarrito = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaCarrito);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        JPanel panelTotales = new JPanel(new GridLayout(5, 1, 5, 2));

        chkEsPremium = new JCheckBox("Cliente Premium (10% Descuento)");
        chkEsPremium.setActionCommand("CAMBIO_PREMIUM");
        chkEsPremium.addActionListener(evento);

        labelSubtotal = new JLabel("Subtotal: $0.0");
        labelImpuestos = new JLabel("Impuestos (IVA): $0.0");
        labelDescuento = new JLabel("Descuento Premium: $0.0");
        labelTotal = new JLabel("TOTAL A PAGAR: $0.0");
        labelTotal.setFont(new Font("Arial", Font.BOLD, 14));

        panelTotales.add(chkEsPremium);
        panelTotales.add(labelSubtotal);
        panelTotales.add(labelImpuestos);
        panelTotales.add(labelDescuento);
        panelTotales.add(labelTotal);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnEliminar = new JButton("Eliminar Seleccionado");
        btnEliminar.setActionCommand("ELIMINAR_CARRITO");
        btnEliminar.addActionListener(evento);

        btnVaciar = new JButton("Vaciar Carrito");
        btnVaciar.setActionCommand("VACIAR_CARRITO");
        btnVaciar.addActionListener(evento);
        
        btnComprar = new JButton("Realizar Compra");
        btnComprar.setActionCommand("REALIZAR_COMPRA");
        btnComprar.addActionListener(evento);

        btnVolver = new JButton("Volver a Libros");
        btnVolver.setActionCommand("VOLVER_LIBROS");
        btnVolver.addActionListener(evento);

        panelBotones.add(btnEliminar);
        panelBotones.add(btnVaciar);
        panelBotones.add(btnComprar);
        panelBotones.add(btnVolver);

        panelInferior.add(panelTotales, BorderLayout.WEST);
        panelInferior.add(panelBotones, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);
    }

    public void agregarLibro(Libro libro) {
        carrito.agregarLibro(libro);
        actualizarTabla();
    }

    public void eliminarLibroSeleccionado() {
        int filaSeleccionada = tablaCarrito.getSelectedRow();
        if (filaSeleccionada >= 0) {
            carrito.eliminarLibro(filaSeleccionada);
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un libro de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void vaciarCarrito() {
        carrito.vaciarCarrito();
        actualizarTabla();
    }
//Actualiza la tabla del carrito y recalcula los valores financieros en tiempo real
    public void actualizarTabla() {
    	// Limpia los registros actuales de la interfaz gráfica
        modeloTabla.setRowCount(0);
     // Sincroniza el estado del checkbox premium con la lógica del carrito
        carrito.setEsPremium(chkEsPremium.isSelected());
     // Recorre los elementos agregados para pintarlos en la JTable
        for (Libro l : carrito.getItems()) {
            Object[] fila = {
                l.getIsbn(),
                l.getTituloLibro(),
                l.getAutor(),
                "$" + l.getPrecioVenta(),
                l.getFormato()
            };
            modeloTabla.addRow(fila);
        }
     // Actualiza las etiquetas de totales con formato monetario
        labelSubtotal.setText(String.format("Subtotal: $%.2f", carrito.calcularSubtotal()));
        labelImpuestos.setText(String.format("Impuestos (IVA): $%.2f", carrito.calcularImpuestos()));
        labelDescuento.setText(String.format("Descuento Premium (10%%): -$%.2f", carrito.calcularDescuento()));
        labelTotal.setText(String.format("TOTAL A PAGAR: $%.2f", carrito.calcularTotalFinal()));
    }

    public Carrito getCarrito() {
        return carrito;
    }
}