package com.tiendavirtual;

import javax.swing.SwingUtilities;
import com.tiendavirtual.gui.VentaFrame;

public class MainVentas {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new VentaFrame().setVisible(true);
        });

    }
}