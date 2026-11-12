package co.edu.uptc.gui;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuPrincipal extends JMenuBar {

    private static final long serialVersionUID = 1L;

    private final List<JMenuItem> items = new ArrayList<>();

    public MenuPrincipal() {
        initComponents();
    }

    private void initComponents() {
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.add(crearItem("Salir", Comandos.MENU_SALIR));

        JMenu menuCatalogo = new JMenu("Catálogo");
        menuCatalogo.add(crearItem("Gestionar libros", Comandos.MENU_GESTIONAR_LIBROS));

        JMenu menuClientes = new JMenu("Clientes");
        menuClientes.add(crearItem("Gestionar clientes / Iniciar sesión", Comandos.MENU_GESTIONAR_CLIENTES));

        JMenu menuVentas = new JMenu("Ventas");
        menuVentas.add(crearItem("Tienda virtual / Carrito", Comandos.MENU_TIENDA));
        menuVentas.add(crearItem("Registrar pedidos (modo legado)", Comandos.MENU_GESTIONAR_PEDIDOS));

        JMenu menuReportes = new JMenu("Reportes");
        menuReportes.add(crearItem("Bitácora y reportes (próximamente)", Comandos.MENU_PENDIENTE));

        JMenu menuAyuda = new JMenu("Ayuda");
        menuAyuda.add(crearItem("Acerca de", Comandos.MENU_ACERCA));

        add(menuArchivo);
        add(menuCatalogo);
        add(menuClientes);
        add(menuVentas);
        add(menuReportes);
        add(menuAyuda);
    }

    private JMenuItem crearItem(String texto, String comando) {
        JMenuItem item = new JMenuItem(texto);
        item.setActionCommand(comando);
        items.add(item);
        return item;
    }

    public void agregarListener(ActionListener listener) {
        for (JMenuItem item : items) {
            item.addActionListener(listener);
        }
    }
}
