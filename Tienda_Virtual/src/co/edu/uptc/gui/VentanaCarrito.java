package co.edu.uptc.gui;

import co.edu.uptc.controller.TiendaController;
import javax.swing.JFrame;

public class VentanaCarrito extends JFrame {
    private TiendaController controller;
    private PanelCarritoCompras panelCarrito;

    public VentanaCarrito(TiendaController controller) {
        this.controller = controller;

        setTitle("Carrito de Compras - Tienda Virtual");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // Cerrar no debe terminar la aplicación
        setLocationRelativeTo(null);

        panelCarrito = new PanelCarritoCompras(new EventosCarrito(this));
        add(panelCarrito);

        refrescar();
    }

    // Recarga el catálogo (combo) y el contenido del carrito (tabla y totales)
    public void refrescar() {
        panelCarrito.cargarLibros(controller.listarLibros());
        refrescarCarrito();
    }

    public void refrescarCarrito() {
        panelCarrito.cargarCarrito(controller.obtenerCarrito());
    }

    public TiendaController getController() {
        return controller;
    }

    public PanelCarritoCompras getPanelCarrito() {
        return panelCarrito;
    }
}