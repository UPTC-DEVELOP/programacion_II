package co.edu.uptc.caso.estudio.gui;

import co.edu.uptc.caso.estudio.model.ItemCarrito;
import co.edu.uptc.caso.estudio.model.enums.MetodoPago;
import co.edu.uptc.caso.estudio.negocio.dto.PagoDto;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class PanelCarrito extends JPanel {
    private static final long serialVersionUID = 1L;

    private DefaultTableModel modeloTablaCarrito;
    private JTable tablaCarrito;
    private JLabel lblTotalImpuestos;
    private JLabel lblTotalCarrito;
    private JComboBox<String> cbMetodoPago;
    private JComboBox<String> cbBanco;
    private JTextField txtTarjeta;
    private JTextField txtExp;
    private JTextField txtNequi;
    private JPasswordField txtCvv;
    private JButton btnFinalizar;
    private JPanel panelDatosTarjeta;
    private JPanel panelDatosNequi;
    private JPanel panelPago;

    public PanelCarrito(Evento evento) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponentes(evento);
        alternarCamposPago();
    }

    private void initComponentes(Evento evento) {
        String[] columnas = {"ISBN", "Título", "Cantidad", "Precio Unitario", "Subtotal"};
        modeloTablaCarrito = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCarrito = new JTable(modeloTablaCarrito);
        tablaCarrito.setAutoCreateRowSorter(true);
        add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelAcciones.add(crearBoton("Actualizar Cantidad", Evento.ACTUALIZAR_CANTIDAD_CARRITO, evento));
        panelAcciones.add(crearBoton("Eliminar Seleccionados", Evento.ELIMINAR_DEL_CARRITO, evento));
        add(panelAcciones, BorderLayout.NORTH);

        JPanel panelResumenTotales = new JPanel(new GridLayout(2, 1, 2, 2));
        lblTotalImpuestos = new JLabel("Total Impuestos: $0.00", SwingConstants.RIGHT);
        lblTotalImpuestos.setFont(new Font("SansSerif", Font.PLAIN, 14));

        lblTotalCarrito = new JLabel("Total Compra: $0.00", SwingConstants.RIGHT);
        lblTotalCarrito.setFont(new Font("SansSerif", Font.BOLD, 16));

        panelResumenTotales.add(lblTotalImpuestos);
        panelResumenTotales.add(lblTotalCarrito);

        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.add(panelResumenTotales, BorderLayout.NORTH);
        panelInferior.add(crearPanelPago(evento), BorderLayout.CENTER);

        add(panelInferior, BorderLayout.SOUTH);
    }

    private JPanel crearPanelPago(Evento evento) {
        panelPago = new JPanel(new BorderLayout(10, 5));
        panelPago.setBorder(BorderFactory.createTitledBorder("Pasarela de Pago"));

        cbMetodoPago = new JComboBox<>(new String[]{
            MetodoPago.TARJETA.getEtiqueta(),
            MetodoPago.TRANSFERENCIA.getEtiqueta()
        });
        cbBanco = new JComboBox<>(new String[]{
            "Bancolombia", "Davivienda", "Banco de Bogotá", "BBVA"
        });

        txtTarjeta = new JTextField(13);
        txtExp = new JTextField(5);
        txtCvv = new JPasswordField(4);
        txtNequi = new JTextField(10);

        panelDatosTarjeta = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelDatosTarjeta.add(new JLabel("Banco:"));
        panelDatosTarjeta.add(cbBanco);
        panelDatosTarjeta.add(new JLabel("Tarjeta:"));
        panelDatosTarjeta.add(txtTarjeta);
        panelDatosTarjeta.add(new JLabel("MM/AA:"));
        panelDatosTarjeta.add(txtExp);
        panelDatosTarjeta.add(new JLabel("CVV:"));
        panelDatosTarjeta.add(txtCvv);

        panelDatosNequi = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelDatosNequi.add(new JLabel("Número Nequi:"));
        panelDatosNequi.add(txtNequi);
        panelDatosNequi.setVisible(false);

        cbMetodoPago.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alternarCamposPago();
            }
        });

        JPanel panelCamposPago = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panelCamposPago.add(new JLabel("Método:"));
        panelCamposPago.add(cbMetodoPago);
        panelCamposPago.add(panelDatosTarjeta);
        panelCamposPago.add(panelDatosNequi);

        btnFinalizar = crearBoton("Finalizar Compra", Evento.FINALIZAR_COMPRA, evento);
        btnFinalizar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnFinalizar.setPreferredSize(new Dimension(180, 38));

        panelPago.add(panelCamposPago, BorderLayout.CENTER);
        panelPago.add(btnFinalizar, BorderLayout.EAST);
        return panelPago;
    }

    private void alternarCamposPago() {
        boolean esTarjeta = MetodoPago.TARJETA.getEtiqueta().equals(cbMetodoPago.getSelectedItem());
        panelDatosTarjeta.setVisible(esTarjeta);
        panelDatosNequi.setVisible(!esTarjeta);
        if (esTarjeta) {
            txtNequi.setText("");
        } else {
            txtTarjeta.setText("");
            txtExp.setText("");
            txtCvv.setText("");
        }
        panelPago.revalidate();
        panelPago.repaint();
    }

    private JButton crearBoton(String texto, String comando, Evento evento) {
        JButton btn = new JButton(texto);
        btn.setActionCommand(comando);
        if (evento != null) {
            btn.addActionListener(evento);
        }
        return btn;
    }

    public void actualizarTabla(List<ItemCarrito> items, double totalFinal) {
        actualizarTotales(items, 0.0, totalFinal);
    }

    public void actualizarTotales(List<ItemCarrito> items, double totalImpuestos, double totalFinal) {
        tablaCarrito.clearSelection();
        modeloTablaCarrito.setRowCount(0);
        if (items != null) {
            for (ItemCarrito it : items) {
                if (it == null || it.getLibro() == null) {
                    continue;
                }
                double precioUnitario = it.getPrecioUnitarioOverride() != null
                        ? it.getPrecioUnitarioOverride()
                        : it.getLibro().getPrecioVenta();
                double subtotal = it.getSubtotalConIva();
                modeloTablaCarrito.addRow(new Object[]{
                    it.getLibro().getIsbn(),
                    it.getLibro().getTitulo(),
                    it.getCantidad(),
                    precioUnitario,
                    subtotal
                });
            }
        }
        lblTotalImpuestos.setText("Total Impuestos: $" + String.format("%.2f", totalImpuestos));
        lblTotalCarrito.setText("Total Compra: $" + String.format("%.2f", totalFinal));
        btnFinalizar.setEnabled(modeloTablaCarrito.getRowCount() > 0);
    }

    public void limpiarCarrito() {
        tablaCarrito.clearSelection();
        modeloTablaCarrito.setRowCount(0);
        lblTotalImpuestos.setText("Total Impuestos: $0.00");
        lblTotalCarrito.setText("Total Compra: $0.00");
        btnFinalizar.setEnabled(false);
        limpiarFormularioPago();
    }

    // Convierte la fila de la vista a la del modelo, porque la tabla puede estar ordenada por el usuario
    public String getIsbnUnicoSeleccionado() {
        int[] filas = tablaCarrito.getSelectedRows();
        if (filas.length != 1) {
            return null;
        }
        int filaModelo = tablaCarrito.convertRowIndexToModel(filas[0]);
        return (String) modeloTablaCarrito.getValueAt(filaModelo, 0);
    }

    public int getCantidadUnicaSeleccionada() {
        int[] filas = tablaCarrito.getSelectedRows();
        if (filas.length != 1) {
            return 0;
        }
        int filaModelo = tablaCarrito.convertRowIndexToModel(filas[0]);
        Object val = modeloTablaCarrito.getValueAt(filaModelo, 2);
        return (val instanceof Number) ? ((Number) val).intValue() : 0;
    }

    public List<String> getIsbnsSeleccionados() {
        int[] filas = tablaCarrito.getSelectedRows();
        List<String> isbns = new ArrayList<>();
        for (int f : filas) {
            isbns.add((String) modeloTablaCarrito.getValueAt(tablaCarrito.convertRowIndexToModel(f), 0));
        }
        return isbns;
    }

    // Recoge los campos del formulario; la validación real la hace GestionComprasImpl.registrarPago
    public PagoDto obtenerDatosPago() {
        return new PagoDto(
            (String) cbMetodoPago.getSelectedItem(),
            (String) cbBanco.getSelectedItem(),
            txtTarjeta.getText().trim(),
            txtExp.getText().trim(),
            new String(txtCvv.getPassword()).trim(),
            txtNequi.getText().trim()
        );
    }

    public void limpiarFormularioPago() {
        txtTarjeta.setText("");
        txtExp.setText("");
        txtCvv.setText("");
        txtNequi.setText("");
        if (cbBanco.getItemCount() > 0) {
            cbBanco.setSelectedIndex(0);
        }
        if (cbMetodoPago.getItemCount() > 0) {
            cbMetodoPago.setSelectedIndex(0);
        }
        alternarCamposPago();
        lblTotalImpuestos.setText("Total Impuestos: $0.00");
    }
}