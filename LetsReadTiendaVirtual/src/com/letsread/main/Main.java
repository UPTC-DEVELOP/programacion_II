package com.letsread.main;

import com.letsread.gui.VentanaPrincipal;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // E
    	
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}