package co.edu.uptc.gui.admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * CLASE Estilos  (paquete: gui)
 * ---------------------------------------------------------------------------
 * Centraliza colores, fuentes y la creación de botones para que TODAS las
 * pantallas luzcan igual (principio DRY: no repetir código). Si el docente o
 * el grupo piden otro look, se cambia aquí y se actualiza toda la aplicación.
 */
final class Estilos {

    static final Color GRIS_FONDO   = new Color(245, 245, 245);
    static final Color GRIS_BOTON   = new Color(221, 221, 221);
    static final Color GRIS_BORDE   = new Color(150, 150, 150);
    static final Font  FUENTE       = new Font("SansSerif", Font.PLAIN, 12);
    static final Font  FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 13);

    private Estilos() { }

    /** Botón gris plano como los del prototipo; el comando identifica el EventoAdmin. */
    static JButton boton(String texto, String comando, ActionListener escuchador) {
        JButton b = new JButton(texto);
        b.setActionCommand(comando);
        b.setBackground(GRIS_BOTON);
        b.setFont(FUENTE);
        b.setFocusPainted(false);
        if (escuchador != null) {
            b.addActionListener(escuchador);
        }
        return b;
    }

    static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE);
        return l;
    }

    static Dimension tamano(int ancho, int alto) {
        return new Dimension(ancho, alto);
    }
}
