package co.edu.uptc.gui.admin;



import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/*
import co.edu.uptc.libros.eventos.EventoAdmin;
import co.edu.uptc.libros.negocio.dto.VentaResumenDto;
*/
/**
 * CLASE PanelReportes  (paquete: gui)  extends JPanel
 * ---------------------------------------------------------------------------
 * Pantalla "Reportes" del prototipo:
 *   NORTE : "Fecha inicial" | "Fecha final" | botón "Generar Reporte"
 *   CENTRO: tabla con las ventas del rango
 *   SUR   : tres totales (libros, libros vendidos, ganancias)
 * Layouts: BorderLayout + GridBagLayout/BorderLayout internos + GridLayout.
 */
class PanelReportes extends JPanel {

    private static final long serialVersionUID = 1L;

    private final CampoConHint campoDesde = new CampoConHint("Fecha inicial (aaaa-mm-dd)", 18);
    private final CampoConHint campoHasta = new CampoConHint("Fecha final (aaaa-mm-dd)", 18);
    private final JButton botonGenerar =
            Estilos.boton("Generar Reporte", EventoAdmin.GENERAR_REPORTE.name(), null);
    private final DefaultTableModel modelo =
            new DefaultTableModel(new String[] {"Fecha", "ISBN(s) vendidos", "Total"}, 0) {
                private static final long serialVersionUID = 1L;
                @Override
                public boolean isCellEditable(int f, int c) {
                    return false;
                }
            };
    private final JLabel lblTotalLibros = Estilos.etiqueta("Total libros: 0");
    private final JLabel lblVendidos = Estilos.etiqueta("Libros vendidos: 0");
    private final JLabel lblGanancias = Estilos.etiqueta("Ganancias totales: $0");

    PanelReportes() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setBackground(Estilos.GRIS_FONDO);

        // ----- Filtros de fecha -----
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        filtros.setOpaque(false);
        filtros.add(campoDesde);
        filtros.add(campoHasta);
        filtros.add(botonGenerar);
        add(filtros, BorderLayout.NORTH);

        // ----- Tabla -----
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(24);
        tabla.setFont(Estilos.FUENTE);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(Estilos.GRIS_BORDE));
        add(scroll, BorderLayout.CENTER);

        // ----- Totales -----
        JPanel totales = new JPanel(new GridLayout(3, 1, 0, 4));
        totales.setOpaque(false);
        totales.add(lblTotalLibros);
        totales.add(lblVendidos);
        totales.add(lblGanancias);
        add(totales, BorderLayout.SOUTH);
    }

    void registrarEscuchador(ActionListener escuchador) {
        botonGenerar.addActionListener(escuchador);
    }

    String getFechaInicial() { return campoDesde.getText().trim(); }
    String getFechaFinal()   { return campoHasta.getText().trim(); }

    void mostrarReporte(List<VentaResumenDto> ventas, int totalLibros, int librosVendidos, double ganancias) {
        modelo.setRowCount(0);
        NumberFormat moneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"));
        for (VentaResumenDto v : ventas) {
            modelo.addRow(new Object[] {v.getFecha(), String.join(", ", v.getIsbns()), moneda.format(v.getTotal())});
        }
        lblTotalLibros.setText("Total libros: " + totalLibros);
        lblVendidos.setText("Libros vendidos: " + librosVendidos);
        lblGanancias.setText("Ganancias totales: " + moneda.format(ganancias));
    }
}
