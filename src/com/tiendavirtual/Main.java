package com.tiendavirtual;

import com.tiendavirtual.controlador.LibroController;
import com.tiendavirtual.gui.LibroFrame;
import com.tiendavirtual.negocio.LibroService;
import com.tiendavirtual.repositorio.LibroRepositoryMySQL;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        LibroController c = new LibroController(
            new LibroService(new LibroRepositoryMySQL())
        );

        SwingUtilities.invokeLater(
            () -> new LibroFrame(c).setVisible(true)
        );
    }
}