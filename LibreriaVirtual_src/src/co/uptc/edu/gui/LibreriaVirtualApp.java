package co.uptc.edu.gui;

import co.uptc.edu.pantallas.*;
import co.uptc.edu.model.Tienda;
import co.uptc.edu.pantallas.PantallaInventario;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;


public class LibreriaVirtualApp extends JFrame implements Navegador {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final Map<Vista, JPanel> pantallas = new EnumMap<>(Vista.class);
    private final Tienda tienda = new Tienda();

    public LibreriaVirtualApp() {
        setTitle("Sistemas de Gestión - Librería Virtual 'BiblioTech'");
        setSize(1000, 680);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tienda.cargarDatosSimulados();

        registrar(Vista.PRESENTACION, new PantallaPresentacion(this));
        registrar(Vista.LOGIN, new PantallaLogin(tienda, this));
        registrar(Vista.USUARIO_HOME, new PantallaUsuario(tienda, this));
        registrar(Vista.CATALOGO, new PantallaCatalogo(tienda, this));
        registrar(Vista.CARRITO, new PantallaCarrito(tienda, this));
        registrar(Vista.ADMIN_HOME, new PantallaAdmin(this));
        registrar(Vista.INVENTARIO, new PantallaInventario(tienda, this));
        registrar(Vista.REGISTRO, new PantallaRegistro(tienda, this));
        registrar(Vista.PERFIL, new PantallaPerfil(tienda, this));
        registrar(Vista.GESTION_USUARIOS, new PantallaGestionUsuarios(tienda, this));

        add(mainPanel);
        irA(Vista.PRESENTACION);
    }

    private void registrar(Vista vista, JPanel pantalla) {
        pantallas.put(vista, pantalla);
        mainPanel.add(pantalla, vista.name());
    }

    @Override
    public void irA(Vista vista) {
        JPanel pantalla = pantallas.get(vista);
        if (pantalla instanceof Refrescable) {
            ((Refrescable) pantalla).refrescar();
        }
        cardLayout.show(mainPanel, vista.name());
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new LibreriaVirtualApp().setVisible(true));
    }
}
