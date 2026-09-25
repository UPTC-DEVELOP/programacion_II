package co.edu.uptc.gui;

import co.edu.uptc.negocio.GestorLibros;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel del Dashboard administrativo.
 * 
 * Muestra estadísticas generales:
 * - Total de libros en inventario
 * - Total de ventas realizadas (pendiente de integración)
 * - Ingresos totales (pendiente de integración)
 * - Tabla de ventas recientes (pendiente de integración)
 * 
 * @author Oscar Clavijo
 * @version 1.0 - Septiembre 2026
 */
@SuppressWarnings("serial")
public class PanelDashboard extends JPanel {

    private GestorLibros gestorLibros;

    public PanelDashboard(GestorLibros gestorLibros) {
        this.gestorLibros = gestorLibros;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //  Panel superior con 3 tarjetas de estadísticas 
        JPanel panelStats = new JPanel(new GridLayout(1, 3, 20, 0));
        
        panelStats.add(crearTarjetaStat("Total de libros en inventario", 
                String.valueOf(gestorLibros.getTotalLibros())));
        panelStats.add(crearTarjetaStat("Total de ventas realizadas", "0")); // TODO: integrar con módulo de ventas
        panelStats.add(crearTarjetaStat("Ingresos Totales", "$0")); // TODO: integrar con módulo de ventas

        add(panelStats, BorderLayout.NORTH);

        //  Panel inferior con tabla de ventas recientes 
        JPanel panelVentas = new JPanel(new BorderLayout());
        panelVentas.setBorder(BorderFactory.createTitledBorder("Ventas Recientes"));

        String[] columnas = {"ID", "Fecha", "Cliente", "Total", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tablaVentas = new JTable(modelo);
        
        // TODO: Poblar con datos reales cuando esté listo el módulo de ventas
        panelVentas.add(new JScrollPane(tablaVentas), BorderLayout.CENTER);

        add(panelVentas, BorderLayout.CENTER);
    }

    /**
     * Crea una tarjeta visual para mostrar una estadística.
     */
    
    private JPanel crearTarjetaStat(String titulo, String valor) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        tarjeta.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 24));
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjeta.add(Box.createVerticalGlue());
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(lblValor);
        tarjeta.add(Box.createVerticalGlue());

        return tarjeta;
    }
}