package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 * Panel de reportes administrativos.
 * 
 * Permite:
 * - Filtrar ventas por rango de fechas
 * - Visualizar tabla de ventas con detalles
 * - Ver totales: ventas, libros vendidos, promedio
 * 
 * NOTA: Este panel está preparado para integrarse con el módulo de ventas.
 * Por ahora muestra la estructura vacía.
 * 
 * @author Oscar Clavijo
 * @version 1.0 - Septiembre 2026
 */
@SuppressWarnings({ "serial", "unused" })
public class PanelAdministradorReportes extends JPanel {

    private JTextField txtFechaInicial;
    private JTextField txtFechaFinal;
    private JButton btnGenerarReporte;
    private JTable tablaReportes;
    private DefaultTableModel modeloTabla;

    private JLabel lblTotalVentas;
    private JLabel lblTotalLibros;
    private JLabel lblPromedioVentas;

    public PanelAdministradorReportes() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //  Panel superior: Filtros de fecha
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros de Reporte"));

        JLabel lblDesde = new JLabel("Desde:");
        txtFechaInicial = new JTextField(10);
        txtFechaInicial.setText(LocalDate.now().withDayOfMonth(1).toString());

        JLabel lblHasta = new JLabel("Hasta:");
        txtFechaFinal = new JTextField(10);
        txtFechaFinal.setText(LocalDate.now().toString());

        btnGenerarReporte = new JButton("Generar Reporte");
        btnGenerarReporte.addActionListener(e -> generarReporte());

        panelFiltros.add(lblDesde);
        panelFiltros.add(txtFechaInicial);
        panelFiltros.add(lblHasta);
        panelFiltros.add(txtFechaFinal);
        panelFiltros.add(btnGenerarReporte);

        add(panelFiltros, BorderLayout.NORTH);

        // Panel central: Tabla de reportes
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Detalle de Ventas"));

        String[] columnas = {"Fecha", "N° Factura", "Cliente", "Cantidad Libros", 
                             "Importe Total", "Método de Pago"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaReportes = new JTable(modeloTabla);
        panelTabla.add(new JScrollPane(tablaReportes), BorderLayout.CENTER);

        add(panelTabla, BorderLayout.CENTER);

        // Panel inferior: Totales
        JPanel panelTotales = new JPanel(new GridLayout(1, 3, 20, 0));
        panelTotales.setBorder(BorderFactory.createTitledBorder("Resumen"));

        lblTotalVentas = new JLabel("Total Ventas: $0");
        lblTotalLibros = new JLabel("Total Libros Vendidos: 0");
        lblPromedioVentas = new JLabel("Promedio Ventas: $0");

        panelTotales.add(lblTotalVentas);
        panelTotales.add(lblTotalLibros);
        panelTotales.add(lblPromedioVentas);

        add(panelTotales, BorderLayout.SOUTH);
    }

    /**
     * Genera el reporte según el rango de fechas seleccionado.
     * 
     * TODO: Integrar con módulo de ventas cuando esté disponible.
     */
    private void generarReporte() {
        try {
            LocalDate fechaInicial = LocalDate.parse(txtFechaInicial.getText());
            LocalDate fechaFinal = LocalDate.parse(txtFechaFinal.getText());

            if (fechaInicial.isAfter(fechaFinal)) {
                JOptionPane.showMessageDialog(this,
                    "La fecha inicial no puede ser mayor a la fecha final.",
                    "Error de fechas", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // TODO: Llamar al gestor de ventas para obtener datos
            // Por ahora, mostrar mensaje informativo
            JOptionPane.showMessageDialog(this,
                "Reporte generado del " + fechaInicial + " al " + fechaFinal + 
                "\n\n(Integración pendiente con módulo de ventas)",
                "Reporte", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar tabla y totales
            modeloTabla.setRowCount(0);
            lblTotalVentas.setText("Total Ventas: $0");
            lblTotalLibros.setText("Total Libros Vendidos: 0");
            lblPromedioVentas.setText("Promedio Ventas: $0");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: Las fechas deben tener el formato YYYY-MM-DD",
                "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}