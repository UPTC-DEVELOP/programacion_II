import co.edu.uptc.controller.*;
import co.edu.uptc.model.*;
import co.edu.uptc.service.*;
import co.edu.uptc.view.*;

public class Phase6Smoke {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");
        PanelTienda tienda = new PanelTienda();
        new TiendaController(tienda, null, null, null);
        PanelCarrito carrito = new PanelCarrito();
        if (tienda.getBtnAgregar().getActionListeners().length == 0) throw new AssertionError("evento carrito");
        if (tienda.getCmbLibros().getItemListeners().length == 0) throw new AssertionError("evento combo");
        if (!Comandos.CERRAR_SESION.equals("CERRAR_SESION")) throw new AssertionError("comandos");
        if (carrito.getBtnFinalizar().getActionCommand() == null) throw new AssertionError("finalizar");
        System.out.println("PHASE6_CONTROLLERS_WIRED_OK");
    }
}
