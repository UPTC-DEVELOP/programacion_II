package co.uptc.edu.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

/** Colores y componentes reutilizables para mantener un estilo uniforme. */
public final class EstiloUI {

    public static final Color PRIMARIO = new Color(207, 105, 85);
    public static final Color INDIGO = new Color(79, 70, 229);
    public static final Color FONDO_PRESENTACION = new Color(245, 247, 250);
    public static final Color FONDO_LOGIN = new Color(241, 245, 249);
    public static final Color FONDO_MENU = new Color(248, 250, 252);
    public static final Color TEXTO_SUBTITULO = new Color(71, 85, 105);
    public static final Color TEXTO_INFO = new Color(100, 116, 139);
    public static final Color PRECIO = new Color(16, 185, 129);

    private EstiloUI() {}

    public static NumberFormat formatoMoneda() {
        return NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
    }

    public static JPanel crearHeaderSuperior(String titulo) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARIO);
        header.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setForeground(Color.WHITE);

        header.add(lbl, BorderLayout.WEST);
        return header;
    }

    public static JButton crearBotonEstilizado(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(Color.ORANGE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    public static JButton crearCardBoton(String titulo, String subtitulo, Color accentColor) {
        JButton btn = new JButton("<html><center><font size='5'><b>" + titulo + "</b></font><br><br><font color='#64748B'>"
                + subtitulo + "</font></center></html>");
        btn.setBackground(Color.WHITE);
        btn.setForeground(accentColor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return btn;
    }
}
