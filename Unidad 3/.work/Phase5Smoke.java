import co.edu.uptc.view.*;

public class Phase5Smoke {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");
        PanelTienda tienda = new PanelTienda();
        PanelCarrito carrito = new PanelCarrito();
        PanelHistorial historial = new PanelHistorial();
        new PanelCatalogoAdmin();
        new PanelPerfil();
        new PanelResenas();
        if (!"REGISTRAR_PEDIDO".equals(tienda.getBtnRegistrar().getActionCommand())) throw new AssertionError("comando tienda");
        if (!"FINALIZAR_COMPRA".equals(carrito.getBtnFinalizar().getActionCommand())) throw new AssertionError("comando carrito");
        if (!"CANCELAR_PEDIDO".equals(historial.getBtnCancelar().getActionCommand())) throw new AssertionError("comando historial");
        System.out.println("PHASE5_VIEW_OK");
    }
}
