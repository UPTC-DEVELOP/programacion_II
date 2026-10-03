package gui;

import javax.swing.SwingUtilities;

import negocio.ControladorProducto;
import negocio.Producto;

/**
 * Punto de entrada de la aplicacion.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ControladorProducto controlador = new ControladorProducto();
                cargarProductosDePrueba(controlador);
                new VentanaPrincipal().setVisible(true);
            }
        });
    }

    private static void cargarProductosDePrueba(ControladorProducto controlador) {
        controlador.agregar(new Producto("Cuaderno", 3500, 20));
        controlador.agregar(new Producto("Lapicero", 1500, 50));
        controlador.agregar(new Producto("Mochila", 45000, 10));
    }
}
