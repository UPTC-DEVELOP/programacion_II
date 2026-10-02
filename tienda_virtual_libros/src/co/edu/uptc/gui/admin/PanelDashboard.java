package co.edu.uptc.gui.admin;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

//import co.edu.uptc.libros.negocio.dto.VentaResumenDto;

/**
 * CLASE PanelDashboard  (paquete: gui)  extends JPanel
 * ---------------------------------------------------------------------------
 * Pantalla "Admin Dashboard" del prototipo:
 *   - Fila superior con 3 tarjetas: total de libros en inventario, total de
 *     ventas realizadas e ingresos totales  (GridLayout 1x3).
 *   - Debajo: el título "Ventas Recientes" y una caja con las últimas ventas.
 * Layouts usados (la guía exige "distribuciones"): BorderLayout general +
 * GridLayout para las tarjetas.
 * Solo PINTA datos: los números los calcula la capa de negocio.
 */
class PanelDashboard extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JLabel valorLibros = crearValor();
    private final JLabel valorVentas = crearValor();
    private final JLabel valorIngresos = crearValor();
    private final JTextArea areaRecientes = new JTextArea();

    PanelDashboard() {
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setBackground(Estilos.GRIS_FONDO);

        // ----- Tarjetas superiores -----
        JPanel tarjetas = new JPanel(new GridLayout(1, 3, 14, 0));
        tarjetas.setOpaque(false);
        tarjetas.add(crearTarjeta("Total de libros en inventario", valorLibros));
        tarjetas.add(crearTarjeta("Total de ventas realizadas", valorVentas));
        tarjetas.add(crearTarjeta("Ingresos Totales", valorIngresos));
        tarjetas.setPreferredSize(Estilos.tamano(0, 110));
        add(tarjetas, BorderLayout.NORTH);

        // ----- Ventas recientes -----
        JPanel recientes = new JPanel(new BorderLayout(0, 6));
        recientes.setOpaque(false);
        JLabel titulo = new JLabel("Ventas Recientes");
        titulo.setFont(Estilos.FUENTE_TITULO);
        recientes.add(titulo, BorderLayout.NORTH);

        areaRecientes.setEditable(false);
        areaRecientes.setFont(Estilos.FUENTE);
        areaRecientes.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        JScrollPane scroll = new JScrollPane(areaRecientes);
        scroll.setBorder(BorderFactory.createLineBorder(Estilos.GRIS_BORDE));
        recientes.add(scroll, BorderLayout.CENTER);
        add(recientes, BorderLayout.CENTER);
    }

    /** Recibe datos ya calculados y los muestra (el panel no hace cálculos). */
    void mostrarResumen(int totalLibros, int totalVentas, double ingresos, List<VentaResumenDto> recientes) {
        valorLibros.setText(String.valueOf(totalLibros));
        valorVentas.setText(String.valueOf(totalVentas));
        valorIngresos.setText(NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO")).format(ingresos));

        if (recientes.isEmpty()) {
            areaRecientes.setText("Aún no hay ventas registradas.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (VentaResumenDto v : recientes) {
            sb.append(v.getFecha()).append("  |  ").append(v.getIsbns().size())
              .append(" libro(s)  |  ")
              .append(NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO")).format(v.getTotal()))
              .append('\n');
        }
        areaRecientes.setText(sb.toString());
    }

    // ----- Métodos auxiliares de construcción -----

    private static JLabel crearValor() {
        JLabel l = new JLabel("0", SwingConstants.CENTER);
        l.setFont(Estilos.FUENTE_TITULO.deriveFont(20f));
        return l;
    }

    /** Tarjeta = panel con borde, texto descriptivo arriba y valor grande al centro. */
    private static JPanel crearTarjeta(String titulo, JLabel valor) {
        JPanel t = new JPanel(new BorderLayout());
        t.setBackground(Estilos.GRIS_BOTON);
        t.setBorder(BorderFactory.createLineBorder(Estilos.GRIS_BORDE));
        JLabel l = new JLabel(titulo, SwingConstants.CENTER);
        l.setFont(Estilos.FUENTE);
        l.setBorder(BorderFactory.createEmptyBorder(10, 4, 0, 4));
        t.add(l, BorderLayout.NORTH);
        t.add(valor, BorderLayout.CENTER);
        return t;
    }
}
