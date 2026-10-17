package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.modelo.dto.DashboardKpiDto;
import co.edu.uptc.modelo.dto.VentaResumenDto;

/**
 * PANEL DASHBOARD DEL ADMINISTRADOR  (paquete: gui)
 * ---------------------------------------------------------------------------
 * Rediseño según el mockup "Admin DashBoard"
 *
 * PRINCIPIO SOLID APLICADO:
 *  - SRP: este panel SOLO muestra la interfaz del dashboard y delega la
 *    lógica de negocio en IGestionDashboard (inyectado vía Evento).
 *  - DIP: depende de ActionListener (abstracción) para los eventos, y los
 *    datos llegan como DTOs (DashboardKpiDto), nunca como entidades.
 *  - OCP: para agregar una nueva vista al sidebar basta con agregar un
 *    botón + una tarjeta en el CardLayout, sin modificar las existentes.
 *
 * ¿Por que CardLayout en el area de  CONTENIDO?
 *  - Permite alternar entre "vista dashboard", "vista gestión de carritos" y
 *    "vista reportes" SIN recargar toda la ventana: es el mismo patrón que
 *    usa VentanaPrincipal.
 *  - El sidebar permanece fijo: el usuario no pierde el contexto de
 *    navegación al cambiar de vista (mejor UX).
 */
public class PanelDashboard extends JPanel {

    private static final long serialVersionUID = 1L;

    // --- Comandos de acción (deben coincidir con los de Evento.java) ---
    public static final String CMD_VISTA_DASHBOARD = "VISTA_DASHBOARD";
    public static final String CMD_GESTIONAR_LIBROS = "GESTION_LIBROS";
    // botón para la gestión de carritos del admin
    public static final String CMD_GESTIONAR_CARRITOS = "VISTA_CARRITOS";
    public static final String CMD_REPORTES = "VISTA_REPORTES";
    public static final String CMD_REGISTRAR_LIBRO = "NUEVO_LIBRO";
    public static final String CMD_SALIR = "CERRAR_SESION";

    // --- Nombres de las tarjetas del CardLayout del área de contenido ---
    private static final String CARD_DASHBOARD = "CARD_DASHBOARD";
    // tarjeta para la vista de gestión de carritos
    private static final String CARD_CARTOS = "CARD_CARTOS";
    private static final String CARD_REPORTES = "CARD_REPORTES";

    // --- Layout y panel de contenido ---
    private CardLayout contenidoLayout;
    private JPanel contenidoCentral;

    // --- Componentes de la vista DASHBOARD (KPIs + tabla) ---
    private JLabel lblTotalLibros;
    private JLabel lblTotalVentas;
    private JLabel lblIngresosTotales;
    private DefaultTableModel modeloVentasRecientes;

    // --- Componente de la vista GESTIÓN DE CARRITOS (se inyecta como tarjeta) ---
    private final PanelGestionCarritos panelGestionCarritos;

    // --- Componente de la vista REPORTES (se inyecta como tarjeta) ---
    private final PanelReportes panelReportes;

    /**
     * Constructor del panel.
     * @param manejadorEventos Objeto que escuchará los eventos (clase Evento)
     */
    public PanelDashboard(ActionListener manejadorEventos) {
        this.panelGestionCarritos = new PanelGestionCarritos(manejadorEventos);
        this.panelReportes = new PanelReportes(manejadorEventos);
        construirSidebar(manejadorEventos);
        // NOTA: construirSidebar y construirContenido añaden sus regiones
        // a ESTE panel (que usa BorderLayout por defecto aquí).
    }

    // =========================== SIDEBAR ===========================

    /**
     * Construye el panel lateral izquierdo con los botones de navegación.
     * Usa BorderLayout.WEST para que ocupe solo el ancho necesario.
     */
    private void construirSidebar(ActionListener manejadorEventos) {
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(52, 73, 94)); // Azul oscuro del mockup
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Título del sidebar
        JLabel lblTituloSidebar = new JLabel("Admin DashBoard");
        lblTituloSidebar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTituloSidebar.setForeground(Color.WHITE);
        lblTituloSidebar.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblTituloSidebar);
        sidebar.add(Box.createVerticalStrut(30)); // Espaciador

        // Botones de navegación (se agrega "Manage Carts")
        sidebar.add(botonSidebar("Dashboard", CMD_VISTA_DASHBOARD, manejadorEventos));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(botonSidebar("Manage Books", CMD_GESTIONAR_LIBROS, manejadorEventos));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(botonSidebar("Manage Carts", CMD_GESTIONAR_CARRITOS, manejadorEventos));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(botonSidebar("Reportes", CMD_REPORTES, manejadorEventos));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(botonSidebar("Registro libros", CMD_REGISTRAR_LIBRO, manejadorEventos));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(botonSidebar("Cerrar sesión", CMD_SALIR, manejadorEventos));
        sidebar.add(Box.createVerticalGlue()); // Empuja el botón de salir hacia abajo

        add(sidebar, BorderLayout.WEST);

        // Construye el área de contenido a la derecha del sidebar
        construirContenido();
    }

    /** Crea un botón estilo del sidebar (ancho fijo, alineado a la izquierda). */
    private JButton botonSidebar(String texto, String comando, ActionListener manejadorEventos) {
        JButton boton = new JButton(texto);
        boton.setActionCommand(comando);
        boton.addActionListener(manejadorEventos);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        boton.setBackground(new Color(44, 62, 80));
        boton.setForeground(Color.WHITE);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    // =========================== CONTENIDO ===========================

    /**
     * Construye el área de contenido con CardLayout para alternar entre
     * la vista dashboard (KPIs + tabla), la vista de gestión de carritos
     * y la vista reportes.
     */
    private void construirContenido() {
        contenidoLayout = new CardLayout();
        contenidoCentral = new JPanel(contenidoLayout);
        contenidoCentral.setBackground(new Color(236, 240, 241));

        // Vista 1: Dashboard con KPIs y tabla de ventas recientes
        JPanel vistaDashboard = construirVistaDashboard();
        contenidoCentral.add(vistaDashboard, CARD_DASHBOARD);

        // Vista 2: Gestión de carritos 
        contenidoCentral.add(panelGestionCarritos, CARD_CARTOS);

        // Vista 3: Reportes (panel dedicado)
        contenidoCentral.add(panelReportes, CARD_REPORTES);

        add(contenidoCentral, BorderLayout.CENTER);

        // Muestra la vista dashboard por defecto al abrir
        contenidoLayout.show(contenidoCentral, CARD_DASHBOARD);
    }

    /**
     * Construye la vista principal del dashboard:
     *  - 3 tarjetas KPI en fila (GridLayout 1x3)
     *  - Título "Ventas Recientes"
     *  - Tabla con las últimas ventas (ID, Fecha, Cliente, Total, Estado)
     */
    private JPanel construirVistaDashboard() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- Tarjetas KPI ---
        JPanel panelKpis = new JPanel(new GridLayout(1, 3, 20, 0));
        panelKpis.setOpaque(false);
        panelKpis.add(tarjetaKpi("Total de libros en inventario"));
        panelKpis.add(tarjetaKpi("Total de ventas realizadas"));
        panelKpis.add(tarjetaKpi("Ingresos Totales"));
        panel.add(panelKpis, BorderLayout.NORTH);

        // --- Sección Ventas Recientes ---
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setOpaque(false);

        JLabel lblVentasRecientes = new JLabel("Ventas Recientes");
        lblVentasRecientes.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblVentasRecientes.setForeground(new Color(44, 62, 80));
        panelInferior.add(lblVentasRecientes, BorderLayout.NORTH);

        // Tabla de ventas recientes
        String[] columnas = {"ID", "Fecha", "Cliente", "Total", "Estado"};
        modeloVentasRecientes = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // La tabla es de solo lectura
            }
        };
        JTable tabla = new JTable(modeloVentasRecientes);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        JScrollPane scrollPane = new JScrollPane(tabla);
        panelInferior.add(scrollPane, BorderLayout.CENTER);

        panel.add(panelInferior, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Crea una tarjeta KPI: fondo gris claro, texto centrado, valor grande.
     * El JLabel del valor se guarda en un atributo para poder actualizarlo
     * desde actualizarDashboard() sin recorrer el árbol de componentes.
     */
    private JPanel tarjetaKpi(String titulo) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(new Color(236, 240, 241));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitulo.setForeground(new Color(100, 100, 100));

        // JLabel del valor: se asigna al atributo correspondiente según el título
        JLabel lblValor = new JLabel("0", SwingConstants.CENTER);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValor.setForeground(new Color(44, 62, 80));

        // Asigna el JLabel al atributo de instancia para actualización posterior
        if (titulo.contains("libros")) {
            this.lblTotalLibros = lblValor;
        } else if (titulo.contains("ventas")) {
            this.lblTotalVentas = lblValor;
        } else if (titulo.contains("Ingresos")) {
            this.lblIngresosTotales = lblValor;
        }

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);
        return tarjeta;
    }

    // =========================== MÉTODOS PÚBLICOS ===========================

    /**
     * Actualiza todos los KPIs y la tabla de ventas recientes con los datos
     * del DashboardKpiDto. Este método es llamado por la capa Evento cada
     * vez que el usuario navega al dashboard o refresca los datos.
     */
    public void actualizarDashboard(DashboardKpiDto kpi) {
        if (kpi == null) {
            return;
        }

        // Actualiza los 3 valores numéricos de las tarjetas
        lblTotalLibros.setText(String.valueOf(kpi.getTotalLibrosInventario()));
        lblTotalVentas.setText(String.valueOf(kpi.getTotalVentasRealizadas()));

        // Formatea los ingresos con separador de miles y 2 decimales (COP)
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        lblIngresosTotales.setText(formatoMoneda.format(kpi.getIngresosTotales()));

        // Actualiza la tabla de ventas recientes
        actualizarTablaVentasRecientes(kpi.getVentasRecientes());
    }

    /** Refresca solo la tabla de ventas recientes (usado por actualizarDashboard). */
    private void actualizarTablaVentasRecientes(List<VentaResumenDto> ventas) {
        modeloVentasRecientes.setRowCount(0);
        if (ventas == null) {
            return;
        }
        for (VentaResumenDto v : ventas) {
            modeloVentasRecientes.addRow(new Object[]{
                    v.getIdVenta(),
                    v.getFecha(),
                    v.getNombreCliente(),
                    String.format("%.2f", v.getTotal()),
                    v.getEstado()
            });
        }
    }

    /**
     * Muestra una vista específica del área de contenido (dashboard o reportes).
     * Este método es llamado por la capa Evento cuando el usuario pulsa un
     * botón del sidebar.
     */
    public void mostrarVista(String cardName) {
        contenidoLayout.show(contenidoCentral, cardName);
    }

    /** Expone el panel de reportes para que Evento pueda refrescarlo. */
    public PanelReportes getPanelReportes() {
        return panelReportes;
    }

    /**Expone el panel de gestión de carritos para que Evento pueda refrescarlo. */
    public PanelGestionCarritos getPanelGestionCarritos() {
        return panelGestionCarritos;
    }

    /** Devuelve el nombre de la tarjeta del dashboard (para mostrarla por defecto). */
    public String getCardDashboard() {
        return CARD_DASHBOARD;
    }

    /**Devuelve el nombre de la tarjeta de gestión de carritos. */
    public String getCardCarritos() {
        return CARD_CARTOS;
    }

    /** Devuelve el nombre de la tarjeta de reportes. */
    public String getCardReportes() {
        return CARD_REPORTES;
    }
}
