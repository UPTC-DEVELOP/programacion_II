package co.edu.uptc.gui;

import java.awt.CardLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class VentanaPrincipal extends JFrame {

    private static final String VISTA_LOGIN = "LOGIN";
    private static final String VISTA_CENTRAL = "CENTRAL";

    private final PanelLogin pLogin;
    private final PanelPadreLibro pLibros;
    private final PanelPadreCliente pClientes;
    private final PanelCarrito panelCarrito;
    private final PanelPedido panelPedido;

    public VentanaPrincipal(ActionListener evento) {
        setSize(800, 600);
        setTitle("Tienda Libreria");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new CardLayout()); // Usar CardLayout es más limpio para cambiar vistas

        pLogin = new PanelLogin(evento);
        pLibros = new PanelPadreLibro(evento);
        pClientes = new PanelPadreCliente(evento);

        panelCarrito = new PanelCarrito();
        panelPedido = new PanelPedido();
        panelCarrito.setPanelPedido(panelPedido);

        JTabbedPane pestanias = new JTabbedPane();
        pestanias.addTab("Libros", pLibros);
        pestanias.addTab("Clientes", pClientes);
        pestanias.addTab("Carrito", panelCarrito);
        pestanias.addTab("Pedidos", panelPedido);

        add(pLogin, VISTA_LOGIN);
        add(pestanias, VISTA_CENTRAL);
    }

    public void mostrarVistaCentral() {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), VISTA_CENTRAL);
    }

    public PanelLogin getPanelLogin() {
        return pLogin;
    }

    public PanelPadreLibro getPanelLibros() {
        return pLibros;
    }

    public PanelPadreCliente getPanelClientes() {
        return pClientes;
    }
}
