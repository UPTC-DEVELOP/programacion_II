package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.dto.ItemCarritoDto;
import co.edu.uptc.modelo.dto.TotalesCarritoDto;

/**
 * PANEL DEL CARRITO DE COMPRAS DEL CLIENTE  (paquete: gui)
 * ---------------------------------------------------------------------------
 * Implementa la interfaz de USUARIO del carrito con las pestañas del mockup:
 *  - "Mi carrito": tabla de ítems + botones CRUD (+ / - / Eliminar / Vaciar)
 *    + panel de totales (RF13: subtotal, IVA, descuento Premium, total final).
 *
 * RF CUBIERTOS:
 *  - RF10 (Agregar): el "Agregar al carrito" se ejecuta desde PanelCliente
 *    (pestaña Catálogo) y delega en ICarritoService.agregarLibro().
 *  - RF11 (Gestionar Cantidad): botones "+1" y "-1" sobre el ítem seleccionado.
 *    Si la cantidad llega a 0, se SUGIERE eliminar (JOptionPane de opción).
 *  - RF12 (Eliminar): botón "Eliminar ítem" con CONFIRMACIÓN previa (YES_NO).
 *  - RF13 (Totales): el panel inferior muestra los 4 valores calculados por
 *    ICarritoService.calcularTotales() (subtotal, IVA, descuento, total).
 *
 * PRINCIPIO SOLID APLICADO:
 *  - SRP: este panel SOLO muestra el carrito y notifica eventos; toda la
 *    lógica de validación y cálculo vive en CarritoServiceImpl (capa negocio).
 *  - DIP: depende de ActionListener (abstracción) y recibe DTOs (ItemCarritoDto,
 *    TotalesCarritoDto), nunca entidades del dominio.
 *  - OCP: para agregar una nueva acción al carrito basta con un botón nuevo
 *    + un comando, sin modificar los existentes.
 *
 * ¿Por que esta separado de PanelCliente?
 *  - SRP: PanelCliente se encarga del catálogo y del perfil; el carrito tiene
 *    su propio ciclo de vida y sus propias reglas (RF10-RF13). Mezclarlos en
 *    una sola clase haría el código difícil de mantener.
 *  - Reutilización: PanelCarrito se inyecta como pestaña dentro de
 *    PanelCliente (composición, no herencia).
 */
public class PanelCarrito extends JPanel {

    private static final long serialVersionUID = 1L;

    // --- Comandos de acción (RF10-RF13) ---
    public static final String CMD_AUMENTAR = "CARRITO_AUMENTAR";
    public static final String CMD_DISMINUIR = "CARRITO_DISMINUIR";
    public static final String CMD_ELIMINAR = "CARRITO_ELIMINAR";
    public static final String CMD_VACIAR = "CARRITO_VACIAR";
    public static final String CMD_ACTUALIZAR_TOTALES = "CARRITO_ACTUALIZAR";

    // --- Componentes de la interfaz ---
    private JTable tablaItems;
    private DefaultTableModel modeloTabla;
    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblDescuento;
    private JLabel lblTotalFinal;
    private JLabel lblEstadoCliente; // Muestra "Premium" o "Regular" (contexto RF13)

    // Copia de los DTOs mostrados (permite mapear fila -> ISBN)
    private List<ItemCarritoDto> itemsMostrados = new ArrayList<>();

    private ClienteDto clienteActual;
    private final ActionListener manejadorEventos;

    /**
     * Constructor del panel.
     * @param manejadorEventos Objeto que escuchará los eventos (clase Evento)
     */
    public PanelCarrito(ActionListener manejadorEventos) {
        this.manejadorEventos = manejadorEventos;
        construir();
    }

    // =========================== CONSTRUCCIÓN ===========================

    private void construir() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(236, 240, 241));

        // --- Norte: título + etiqueta del tipo de cliente ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        JLabel lblTitulo = new JLabel("Mi carrito de compras");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(44, 62, 80));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        lblEstadoCliente = new JLabel("");
        lblEstadoCliente.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblEstadoCliente.setForeground(new Color(100, 100, 100));
        panelSuperior.add(lblEstadoCliente, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // --- Centro: tabla de ítems ---
        String[] columnas = {"ISBN", "Título", "Cantidad", "Precio Unit.", "Subtotal", "IVA"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // La tabla es de solo lectura: la edición va por botones
            }
        };
        tablaItems = new JTable(modeloTabla);
        tablaItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaItems.setRowHeight(28);
        add(new JScrollPane(tablaItems), BorderLayout.CENTER);

        // --- Sur: botones de acción + panel de totales (RF13) ---
        JPanel panelSur = new JPanel(new BorderLayout(10, 10));
        panelSur.setOpaque(false);

        // Botones CRUD del carrito (RF11 y RF12)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.setOpaque(false);
        panelBotones.add(boton("Cantidad +1", CMD_AUMENTAR));
        panelBotones.add(boton("Cantidad -1", CMD_DISMINUIR));
        panelBotones.add(boton("Eliminar ítem", CMD_ELIMINAR));
        panelBotones.add(Box.createHorizontalStrut(20));
        panelBotones.add(boton("Vaciar carrito", CMD_VACIAR));
        panelBotones.add(boton("Actualizar totales", CMD_ACTUALIZAR_TOTALES));
        panelSur.add(panelBotones, BorderLayout.NORTH);

        // Panel de totales (RF13)
        JPanel panelTotales = new JPanel(new GridLayout(4, 2, 5, 5));
        panelTotales.setOpaque(false);
        panelTotales.setBorder(BorderFactory.createTitledBorder("Resumen (RF13)"));
        lblSubtotal = crearLabelTotal("Subtotal general:");
        lblIva = crearLabelTotal("Total IVA:");
        lblDescuento = crearLabelTotal("Descuento (Premium 10%):");
        lblTotalFinal = crearLabelTotal("TOTAL FINAL:");
        lblTotalFinal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelTotales.add(new JLabel("Subtotal general:"));
        panelTotales.add(lblSubtotal);
        panelTotales.add(new JLabel("Total IVA:"));
        panelTotales.add(lblIva);
        panelTotales.add(new JLabel("Descuento (Premium 10%):"));
        panelTotales.add(lblDescuento);
        panelTotales.add(new JLabel("TOTAL FINAL:"));
        panelTotales.add(lblTotalFinal);
        panelSur.add(panelTotales, BorderLayout.CENTER);

        add(panelSur, BorderLayout.SOUTH);
    }

    private JLabel crearLabelTotal(String texto) {
        JLabel lbl = new JLabel("$0.00");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        return lbl;
    }

    private JButton boton(String texto, String comando) {
        JButton b = new JButton(texto);
        b.setActionCommand(comando);
        b.addActionListener(manejadorEventos);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    // =========================== MÉTODOS PÚBLICOS ===========================

    /** Asigna el cliente actual y actualiza la etiqueta de tipo (contexto RF13). */
    public void setCliente(ClienteDto cliente) {
        this.clienteActual = cliente;
        if (cliente != null && cliente.getTipoCliente() != null) {
            lblEstadoCliente.setText("Cliente " + cliente.getTipoCliente().getNombreMostrar());
        } else {
            lblEstadoCliente.setText("");
        }
    }

    public ClienteDto getCliente() {
        return clienteActual;
    }

    /** Actualiza la tabla de ítems con los datos del servicio. */
    public void actualizarItems(List<ItemCarritoDto> items) {
        modeloTabla.setRowCount(0);
        itemsMostrados = (items == null) ? new ArrayList<>() : new ArrayList<>(items);
        for (ItemCarritoDto item : itemsMostrados) {
            modeloTabla.addRow(new Object[]{
                    item.getIsbn(),
                    item.getTitulo(),
                    item.getCantidad(),
                    String.format("%.2f", item.getPrecioUnitario()),
                    String.format("%.2f", item.getSubtotal()),
                    String.format("%.2f", item.getIva())
            });
        }
    }

    /** Actualiza el panel de totales (RF13) con los 4 valores del DTO. */
    public void actualizarTotales(TotalesCarritoDto totales) {
        if (totales == null) {
            return;
        }
        NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        lblSubtotal.setText(moneda.format(totales.getSubtotalGeneral()));
        lblIva.setText(moneda.format(totales.getTotalImpuestos()));
        lblDescuento.setText(moneda.format(totales.getDescuento()));
        lblTotalFinal.setText(moneda.format(totales.getTotalFinal()));
    }

    /**
     * Devuelve el ISBN del ítem seleccionado en la tabla, o null si no hay
     * selección. Lo usa la capa Evento para saber sobre qué ítem operar.
     */
    public String getIsbnSeleccionado() {
        int fila = tablaItems.getSelectedRow();
        if (fila == -1 || fila >= itemsMostrados.size()) {
            return null;
        }
        return itemsMostrados.get(fila).getIsbn();
    }

    /** Devuelve la cantidad actual del ítem seleccionado (para el flujo RF11). */
    public int getCantidadSeleccionada() {
        int fila = tablaItems.getSelectedRow();
        if (fila == -1 || fila >= itemsMostrados.size()) {
            return -1;
        }
        return itemsMostrados.get(fila).getCantidad();
    }

    /** Limpia la tabla y los totales (se usa al cerrar sesión o vaciar carrito). */
    public void limpiar() {
        modeloTabla.setRowCount(0);
        itemsMostrados.clear();
        lblSubtotal.setText("$0.00");
        lblIva.setText("$0.00");
        lblDescuento.setText("$0.00");
        lblTotalFinal.setText("$0.00");
    }
}
