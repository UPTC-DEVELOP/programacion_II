package co.edu.uptc.gui.admin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JTextField;

/**
 * CLASE CampoConHint  (paquete: gui)  extends JTextField
 * ---------------------------------------------------------------------------
 * Caja de texto que muestra un texto guía en gris (ej. "Fecha inicial") cuando
 * está vacía, igual que los campos del prototipo. Se logra SOBRESCRIBIENDO
 * paintComponent (herencia + polimorfismo): primero se pinta el campo normal y,
 * si no hay texto, se dibuja encima el hint. NO se escribe nada dentro del
 * campo, así getText() devuelve "" cuando el usuario no digitó nada.
 */
class CampoConHint extends JTextField {

    private static final long serialVersionUID = 1L;
    private final String hint;

    CampoConHint(String hint, int columnas) {
        super(columnas);
        this.hint = hint;
        setFont(Estilos.FUENTE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getText().isEmpty()) {
            Insets in = getInsets();
            g.setColor(Color.GRAY);
            g.setFont(getFont());
            g.drawString(hint, in.left + 2, getHeight() / 2 + g.getFontMetrics().getAscent() / 2 - 1);
        }
    }
}