package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Campo de texto con placeholder (hint) personalizado.
 *
 * @author Grupo 7 - UPTC
 */
public class CampoConHint extends JTextField {

    private static final long serialVersionUID = 1L;

    private String hint;

    public CampoConHint(String hint, int columns) {
        super(columns);
        this.hint = hint;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getText().isEmpty() && !hasFocus()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(Color.GRAY);
            g2.drawString(hint, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
            g2.dispose();
        }
    }
}