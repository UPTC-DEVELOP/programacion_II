package co.uptc.edu.gui;

import co.uptc.edu.config.TiendaConfig;
import co.uptc.edu.controlador.CarritoControlador;
import co.uptc.edu.controlador.LibroControlador;
import co.uptc.edu.controlador.SesionControlador;
import co.uptc.edu.controlador.UsuarioControlador;
import co.uptc.edu.pantallas.*;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;


public class LibreriaVirtualApp extends JFrame implements Navegador {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final Map<Vista, JPanel> pantallas = new EnumMap<>(Vista.class);
    private final TiendaConfig config = new TiendaConfig();

    public LibreriaVirtualApp() {
        setTitle("Sistemas de Gestión - Librería Virtual 'BiblioTech'");
        setSize(1000, 680);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        config.getTienda().cargarDatosSimulados();

        // Las pantallas solo conocen a los controladores, nunca al modelo ni a la persistencia
        SesionControlador sesion = config.getSesionControlador();
        UsuarioControlador usuarios = config.getUsuarioControlador();
        LibroControlador libros = config.getLibroControlador();
        CarritoControlador carrito = config.getCarritoControlador();

        registrar(Vista.PRESENTACION, new PantallaPresentacion(this));
        registrar(Vista.LOGIN, new PantallaLogin(sesion, this));
        registrar(Vista.USUARIO_HOME, new PantallaUsuario(sesion, this));
        registrar(Vista.CATALOGO, new PantallaCatalogo(libros, carrito, this));
        registrar(Vista.CARRITO, new PantallaCarrito(carrito, this));
        registrar(Vista.ADMIN_HOME, new PantallaAdmin(this));
        registrar(Vista.INVENTARIO, new PantallaInventario(libros, this));
        registrar(Vista.REGISTRO, new PantallaRegistro(usuarios, this));
        registrar(Vista.PERFIL, new PantallaPerfil(sesion, usuarios, this));
        registrar(Vista.GESTION_USUARIOS, new PantallaGestionUsuarios(usuarios, sesion, this));

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
