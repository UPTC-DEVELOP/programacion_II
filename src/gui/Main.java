package gui;

import javax.swing.SwingUtilities;

import negocio.ControladorProducto;
import negocio.Producto;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ControladorProducto controlador = new ControladorProducto();
                cargarProductosDePrueba(controlador);
                new VentanaPrincipal(controlador).setVisible(true);
            }
        });
    }

    private static void cargarProductosDePrueba(ControladorProducto controlador) {
        Producto cuaderno = new Producto("Cuaderno", 3500, 20);
        cuaderno.setPorcentajeDescuento(0.10);
        cuaderno.setImpuestoIVA(0.19);
        controlador.agregar(cuaderno);

        Producto lapicero = new Producto("Lapicero", 1500, 50);
        lapicero.setPorcentajeDescuento(0.05);
        lapicero.setImpuestoIVA(0.19);
        controlador.agregar(lapicero);

        Producto mochila = new Producto("Mochila", 45000, 10);
        mochila.setPorcentajeDescuento(0.15);
        mochila.setImpuestoIVA(0.19);
        controlador.agregar(mochila);
    }
}