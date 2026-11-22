package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import co.edu.uptc.model.ItemCarrito;
import co.edu.uptc.model.enums.MetodoPago;
import co.edu.uptc.negocio.dto.PagoDto;

public class PanelCarrito extends JPanel {
    private static final long serialVersionUID = 1L;

    private DefaultTableModel modeloTablaCarrito;
    private JTable tablaCarrito;
    private JLabel lblTotalCarrito;
    private JComboBox<String> cbMetodoPago, cbBanco;
    private JTextField txtTarjeta, txtExp, txtCvv, txtNequi;
    private JPanel panelDatosTarjeta, panelDatosNequi;

    public PanelCarrito(Evento evento) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponentes(evento);
    }

    private void initComponentes(Evento evento) {
        String[] columnas = {"ISBN", "Título", "Cantidad", "Precio Unitario", "Subtotal"};
        modeloTablaCarrito = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCarrito = new JTable(modeloTablaCarrito);
        add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelAcciones.add(crearBoton("Actualizar Cantidad", Evento.ACTUALIZAR_CANTIDAD_CARRITO, evento));
        panelAcciones.add(crearBoton("Eliminar Seleccionados", Evento.ELIMINAR_DEL_CARRITO, evento));
        add(panelAcciones, BorderLayout.NORTH);

        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        lblTotalCarrito = new JLabel("Total Compra: $0.0", SwingConstants.RIGHT);
        lblTotalCarrito.setFont(new Font("SansSerif", Font.BOLD, 16));
        panelInferior.add(lblTotalCarrito, BorderLayout.NORTH);

        JPanel panelPago = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelPago.setBorder(BorderFactory.createTitledBorder("Pasarela de Pago"));

        cbMetodoPago = new JComboBox<>(new String[]{MetodoPago.TARJETA.getEtiqueta(), MetodoPago.TRANSFERENCIA.getEtiqueta()});
        cbBanco = new JComboBox<>(new String[]{"Bancolombia", "Davivienda", "Banco de Bogotá", "BBVA"});

        txtTarjeta = new JTextField(12);
        txtExp = new JTextField(5);
        txtCvv = new JTextField(4);
        txtNequi = new JTextField(10);

        panelDatosTarjeta = new JPanel();
        panelDatosTarjeta.add(new JLabel("Banco:")); panelDatosTarjeta.add(cbBanco);
        panelDatosTarjeta.add(new JLabel("Tarjeta:")); panelDatosTarjeta.add(txtTarjeta);
        panelDatosTarjeta.add(new JLabel("MM/AA:")); panelDatosTarjeta.add(txtExp);
        panelDatosTarjeta.add(new JLabel("CVV:")); panelDatosTarjeta.add(txtCvv);

        panelDatosNequi = new JPanel();
        panelDatosNequi.add(new JLabel("Número Nequi:")); panelDatosNequi.add(txtNequi);
        panelDatosNequi.setVisible(false);

        cbMetodoPago.addActionListener(e -> {
            boolean esTarjeta = MetodoPago.TARJETA.getEtiqueta().equals(cbMetodoPago.getSelectedItem());
            panelDatosTarjeta.setVisible(esTarjeta);
            panelDatosNequi.setVisible(!esTarjeta);
            panelPago.revalidate();
        });

        panelPago.add(new JLabel("Método:"));
        panelPago.add(cbMetodoPago);
        panelPago.add(panelDatosTarjeta);
        panelPago.add(panelDatosNequi);

        JButton btnFinalizar = crearBoton("Finalizar Compra", Evento.FINALIZAR_COMPRA, evento);
        btnFinalizar.setFont(new Font("SansSerif", Font.BOLD, 14));
        panelPago.add(btnFinalizar);

        panelInferior.add(panelPago, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, String comando, Evento evento) {
        JButton btn = new JButton(texto);
        btn.setActionCommand(comando);
        btn.addActionListener(evento);
        return btn;
    }

    public void actualizarTabla(List<ItemCarrito> items) {
        modeloTablaCarrito.setRowCount(0);
        double total = 0;
        if (items != null) {
            for (ItemCarrito it : items) {
                if (it.getLibro() == null) continue;
                double subtotal = it.getLibro().getPrecioVenta() * it.getCantidad();
                total += subtotal;
                modeloTablaCarrito.addRow(new Object[]{
                    it.getLibro().getIsbn(), it.getLibro().getTitulo(),
                    it.getCantidad(), it.getLibro().getPrecioVenta(), subtotal
                });
            }
        }
        lblTotalCarrito.setText("Total Compra: $" + String.format("%.2f", total));
    }

    public String getIsbnUnicoSeleccionado() {
        int[] filas = tablaCarrito.getSelectedRows();
        if (filas.length != 1) return null;
        int filaModelo = tablaCarrito.convertRowIndexToModel(filas[0]);
        return (String) modeloTablaCarrito.getValueAt(filaModelo, 0);
    }

    public int getCantidadUnicaSeleccionada() {
        int[] filas = tablaCarrito.getSelectedRows();
        int filaModelo = tablaCarrito.convertRowIndexToModel(filas[0]);
        return (int) modeloTablaCarrito.getValueAt(filaModelo, 2);
    }

    public List<String> getIsbnsSeleccionados() {
        int[] filas = tablaCarrito.getSelectedRows();
        List<String> isbns = new ArrayList<>();
        for (int f : filas) {
            isbns.add((String) modeloTablaCarrito.getValueAt(tablaCarrito.convertRowIndexToModel(f), 0));
        }
        return isbns;
    }

    public PagoDto obtenerDatosPago() {
        return new PagoDto(
            (String) cbMetodoPago.getSelectedItem(),
            (String) cbBanco.getSelectedItem(),
            txtTarjeta.getText().trim(),
            txtExp.getText().trim(),
            txtCvv.getText().trim(),
            txtNequi.getText().trim()
        );
    }
}
