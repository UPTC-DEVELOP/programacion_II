package util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.UIManager;





public class Estilos {

	public static final Color AZUL_MARINO = new Color(18, 49, 80);
    public static final Color AZUL_VIVO = new Color(47, 128, 237);
    public static final Color TURQUESA = new Color(49, 168, 166);
    public static final Color CELESTE = new Color(225, 241, 252);
    public static final Color FONDO = new Color(242, 248, 252);
    public static final Color TEXTO = new Color(31, 43, 56);

    public static void aplicarNimbus() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el estilo Nimbus.");
        }
    }

    public static void campoTexto(JTextField campo) {
        campo.setBackground(Color.WHITE);
        campo.setForeground(TEXTO);
        campo.setCaretColor(AZUL_MARINO);
        campo.setSelectionColor(CELESTE);
        campo.setSelectedTextColor(AZUL_MARINO);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(165, 190, 211)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        campo.setOpaque(true);
    }

    public static void combo(JComboBox<String> combo) {
        combo.setBackground(Color.WHITE);
        combo.setForeground(TEXTO);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    public static void botonPrincipal(JButton boton) {
        boton.setBackground(AZUL_VIVO);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        boton.setOpaque(true);
    }

    public static void botonSecundario(JButton boton) {
        boton.setBackground(TURQUESA);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        boton.setOpaque(true);
    }

    public static void botonClaro(JButton boton) {
        boton.setBackground(CELESTE);
        boton.setForeground(Color.BLACK);
        boton.setFont(new Font("SansSerif", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AZUL_VIVO),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)));
        boton.setOpaque(true);
    }
}
