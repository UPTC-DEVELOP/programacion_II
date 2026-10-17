package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.modelo.dto.IngresoClienteDto;
import co.edu.uptc.modelo.dto.ItemCarritoDto;

/**
 * PANEL DE GESTIÓN DE CARRITOS DEL ADMINISTRADOR  (paquete: gui)
 * ---------------------------------------------------------------------------
 * Permite al administrador ver TODOS los carritos activos de los clientes
 * (botón "Manage Carts" en el sidebar del Admin Dashboard).
 *
 * Estructura:
 *  - Norte: tabla resumen de carritos (Cliente, Tipo, Items, Subtotal, Total).
 *  - Centro: tabla de DETALLE de los ítems del carrito seleccionado.
 *  - Sur: botón "Eliminar carrito" (acción destructiva con confirmación).
 *
 * PRINCIPIO SOLID APLICADO:
 *  - SRP: este panel SOLO muestra la interfaz de gestión de carritos y notifica
 *    eventos; la lógica de eliminación vive en Evento + CarritoServiceImpl.
 *  - DIP: depende de ActionListener y recibe DTOs (IngresoClienteDto,
 *    ItemCarritoDto), nunca entidades del dominio.
 *  - OCP: para agregar una nueva acción (p. ej. "Exportar carrito") basta con
 *    un botón nuevo + un comando, sin modificar los existentes.
 */
public class PanelGestionCarritos extends JPanel {

    private static final long serialVersionUID = 1L;

    // Comandos de acción
    public static final String CMD_ELIMINAR_CARRITO = "ADMIN_ELIMINAR_CARRITO";
    public static final String CMD_ACTUALIZAR = "ADMIN_ACTUALIZAR_CARRITOS";

    // Tabla resumen de carritos
    private JTable tablaCarritos;
    private DefaultTableModel modeloCarritos;
    private List<IngresoClienteDto> carritosMostrados = new ArrayList<>();

    // Tabla de detalle del carrito seleccionado
    private JTable tablaDetalle;
    private DefaultTableModel modeloDetalle;

    private final ActionListener manejadorEventos;

    public PanelGestionCarritos(ActionListener manejadorEventos) {
        this.manejadorEventos = manejadorEventos;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(236, 240, 241));

        // --- Norte: título + tabla resumen ---
        JPanel panelNorte = new JPanel(new BorderLayout(8, 8));
        panelNorte.setOpaque(false);
        JLabel lblTitulo = new JLabel("Gestión de Carritos de Compras");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(44, 62, 80));
        panelNorte.add(lblTitulo, BorderLayout.NORTH);

        String[] columnasResumen = {"ID Cliente", "Cliente", "Tipo", "Items", "Subtotal", "Total Estimado"};
        modeloCarritos = new DefaultTableModel(columnasResumen, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCarritos = new JTable(modeloCarritos);
        tablaCarritos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCarritos.setRowHeight(26);
        // Al seleccionar una fila se carga el detalle (evento de selección de tabla)
        tablaCarritos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDetalleSeleccionado();
            }
        });
        panelNorte.add(new JScrollPane(tablaCarritos), BorderLayout.CENTER);
        add(panelNorte, BorderLayout.NORTH);

        // --- Centro: detalle de ítems del carrito seleccionado ---
        JPanel panelDetalle = new JPanel(new BorderLayout(5, 5));
        panelDetalle.setOpaque(false);
        JLabel lblDetalle = new JLabel("Detalle del carrito seleccionado");
        lblDetalle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelDetalle.add(lblDetalle, BorderLayout.NORTH);

        String[] columnasDetalle = {"ISBN", "Título", "Cantidad", "Precio Unit.", "Subtotal", "IVA"};
        modeloDetalle = new DefaultTableModel(columnasDetalle, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaDetalle = new JTable(modeloDetalle);
        tablaDetalle.setRowHeight(24);
        panelDetalle.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);
        add(panelDetalle, BorderLayout.CENTER);

        // --- Sur: botones de acción ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.setOpaque(false);
        panelBotones.add(boton("Eliminar carrito", CMD_ELIMINAR_CARRITO));
        panelBotones.add(boton("Actualizar lista", CMD_ACTUALIZAR));
        add(panelBotones, BorderLayout.SOUTH);
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

    /** Actualiza la tabla resumen con la lista de carritos activos. */
    public void actualizarCarritos(List<IngresoClienteDto> carritos) {
        modeloCarritos.setRowCount(0);
        modeloDetalle.setRowCount(0); // Se limpia el detalle al refrescar el resumen
        carritosMostrados = (carritos == null) ? new ArrayList<>() : new ArrayList<>(carritos);
        for (IngresoClienteDto c : carritosMostrados) {
            modeloCarritos.addRow(new Object[]{
                    c.getIdCliente(),
                    c.getNombreCliente(),
                    c.getTipoCliente(),
                    c.getCantidadItems(),
                    String.format("%.2f", c.getSubtotalEstimado()),
                    String.format("%.2f", c.getTotalEstimado())
            });
        }
    }

    /** Devuelve el idCliente del carrito seleccionado en la tabla resumen. */
    public Integer getClienteSeleccionado() {
        int fila = tablaCarritos.getSelectedRow();
        if (fila == -1 || fila >= carritosMostrados.size()) {
            return null;
        }
        return carritosMostrados.get(fila).getIdCliente();
    }

    /** Carga el detalle de ítems del carrito seleccionado (llamado por el listener de selección). */
    private void cargarDetalleSeleccionado() {
        // El detalle se llena desde Evento vía actualizarDetalle(...); aquí solo se limpia
        // al cambiar de selección para evitar mostrar datos obsoletos.
        modeloDetalle.setRowCount(0);
    }

    /** Actualiza la tabla de detalle con los ítems de UN carrito específico. */
    public void actualizarDetalle(List<ItemCarritoDto> items) {
        modeloDetalle.setRowCount(0);
        if (items == null) {
            return;
        }
        for (ItemCarritoDto item : items) {
            modeloDetalle.addRow(new Object[]{
                    item.getIsbn(),
                    item.getTitulo(),
                    item.getCantidad(),
                    String.format("%.2f", item.getPrecioUnitario()),
                    String.format("%.2f", item.getSubtotal()),
                    String.format("%.2f", item.getIva())
            });
        }
    }
}
