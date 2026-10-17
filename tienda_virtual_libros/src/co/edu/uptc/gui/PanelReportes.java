package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.modelo.dto.IngresoClienteDto;
import co.edu.uptc.modelo.dto.LibroVendidoDto;
import co.edu.uptc.modelo.dto.VentaResumenDto;

/**
 * PANEL DE REPORTES  (paquete: gui)
 * ---------------------------------------------------------------------------
 * Muestra 3 reportes del administrador en pestañas (FASE 4):
 *  1. "Ventas"            : reporte COMPLETO de ventas (LocalVenta).
 *  2. "Libros en carritos": libros más vendidos ACUMULADOS en los carritos
 *                            activos (ICarritoRepository + ILibroRepositorio).
 *  3. "Ingresos por cliente": ingresos ESTIMADOS por cada cliente con carrito
 *                            activo (ICarritoRepository + IClienteRepositorio).
 *
 * Se muestra dentro del CardLayout del PanelDashboard cuando el usuario
 * pulsa "Reportes" en el sidebar.
 *
 * PRINCIPIO SOLID:
 *  - SRP: solo muestra las tablas de reportes y delega la carga de datos
 *    en la capa de negocio (GestionDashboard y GestionReporte vía Evento).
 *  - DIP: depende de ActionListener para el evento de "Volver".
 *  - OCP: para agregar un cuarto reporte basta con una pestaña nueva +
 *    un método actualizarXxx(), sin modificar las pestañas existentes.
 */
public class PanelReportes extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Comando para volver a la vista dashboard desde reportes. */
    public static final String CMD_VOLVER_DASHBOARD = "VISTA_DASHBOARD";

    // Modelos de las 3 tablas de reportes
    private DefaultTableModel modeloVentas;
    private DefaultTableModel modeloLibrosCarritos;
    private DefaultTableModel modeloIngresosClientes;

    private final ActionListener manejadorEventos;

    public PanelReportes(ActionListener manejadorEventos) {
        this.manejadorEventos = manejadorEventos;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(236, 240, 241));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Título + botón volver ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        JLabel lblTitulo = new JLabel("Reportes del Administrador");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(44, 62, 80));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        JButton btnVolver = new JButton("Volver al Dashboard");
        btnVolver.setActionCommand(CMD_VOLVER_DASHBOARD);
        btnVolver.addActionListener(manejadorEventos);
        btnVolver.setFocusPainted(false);
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelSuperior.add(btnVolver, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // --- Pestañas con los 3 reportes ---
        JTabbedPane pestanias = new JTabbedPane();
        pestanias.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Pestaña 1: Ventas (reporte existente de LocalVenta)
        modeloVentas = crearModelo(new String[]{"ID", "Fecha", "Cliente", "Libros", "Total", "Estado"});
        pestanias.addTab("Ventas", new JScrollPane(crearTabla(modeloVentas)));

        // Pestaña 2: Libros más vendidos en carritos activos (FASE 4)
        modeloLibrosCarritos = crearModelo(new String[]{"ISBN", "Título", "Cantidad en carritos", "Ingreso potencial"});
        pestanias.addTab("Libros en carritos", new JScrollPane(crearTabla(modeloLibrosCarritos)));

        // Pestaña 3: Ingresos estimados por cliente (FASE 4)
        modeloIngresosClientes = crearModelo(new String[]{"ID Cliente", "Cliente", "Tipo", "Items", "Subtotal", "Total Estimado"});
        pestanias.addTab("Ingresos por cliente", new JScrollPane(crearTabla(modeloIngresosClientes)));

        add(pestanias, BorderLayout.CENTER);
    }

    // =========================== AUXILIARES DE CONSTRUCCIÓN ===========================

    /** Crea un DefaultTableModel de solo lectura con las columnas dadas. */
    private DefaultTableModel crearModelo(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }
        };
    }

    /** Configura una JTable con altura de fila y fuente de encabezado consistentes. */
    private JTable crearTabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(26);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        return tabla;
    }

    // =========================== MÉTODOS DE ACTUALIZACIÓN (llamados por Evento) ===========================

    /**
     * Actualiza la pestaña "Ventas" con la lista completa de ventas.
     * Método público llamado por la capa Evento cada vez que se muestra
     * la vista de reportes.
     */
    public void actualizarTabla(List<VentaResumenDto> ventas) {
        modeloVentas.setRowCount(0);
        if (ventas == null) {
            return;
        }
        for (VentaResumenDto v : ventas) {
            int cantidadLibros = (v.getIsbns() == null) ? 0 : v.getIsbns().size();
            modeloVentas.addRow(new Object[]{
                    v.getIdVenta(),
                    v.getFecha(),
                    v.getNombreCliente(),
                    cantidadLibros + " libro(s)",
                    String.format("%.2f", v.getTotal()),
                    v.getEstado()
            });
        }
    }

    /**
     * FASE 4: Actualiza la pestaña "Libros en carritos" con el reporte de
     * libros más acumulados en los carritos activos (IGestionReporte).
     */
    public void actualizarLibrosCarritos(List<LibroVendidoDto> libros) {
        modeloLibrosCarritos.setRowCount(0);
        if (libros == null) {
            return;
        }
        for (LibroVendidoDto l : libros) {
            modeloLibrosCarritos.addRow(new Object[]{
                    l.getIsbn(),
                    l.getTitulo(),
                    l.getCantidadTotal(),
                    String.format("%.2f", l.getIngresoPotencial())
            });
        }
    }

    /**
     * FASE 4: Actualiza la pestaña "Ingresos por cliente" con el reporte de
     * ingresos estimados por cada cliente con carrito activo (IGestionReporte).
     */
    public void actualizarIngresosClientes(List<IngresoClienteDto> ingresos) {
        modeloIngresosClientes.setRowCount(0);
        if (ingresos == null) {
            return;
        }
        for (IngresoClienteDto i : ingresos) {
            modeloIngresosClientes.addRow(new Object[]{
                    i.getIdCliente(),
                    i.getNombreCliente(),
                    i.getTipoCliente(),
                    i.getCantidadItems(),
                    String.format("%.2f", i.getSubtotalEstimado()),
                    String.format("%.2f", i.getTotalEstimado())
            });
        }
    }
}
