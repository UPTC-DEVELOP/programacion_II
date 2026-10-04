package co.edu.uptc.gui;

import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.dto.CredencialDto;
import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.negocio.GestionCliente;
import co.edu.uptc.negocio.GestionLibro;
import co.edu.uptc.negocio.GestionSeguridad;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la tienda virtual de libros.
 * Utiliza CardLayout para alternar entre:
 *  - LOGIN        : pantalla de autenticación
 *  - DASHBOARD    : panel del administrador (CRUD libros y clientes)
 *  - GESTION_LIBROS / GESTION_CLIENTES : CRUD del administrador
 *  - PANEL_CLIENTE : panel del cliente (catálogo y gestión de su perfil)
 */
public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    // Nombres de las "tarjetas" del CardLayout
    public static final String PANEL_LOGIN = "LOGIN";
    public static final String PANEL_ADMIN = "DASHBOARD";
    public static final String PANEL_LIBROS = "GESTION_LIBROS";
    public static final String PANEL_CLIENTES = "GESTION_CLIENTES";
    public static final String PANEL_CLIENTE = "PANEL_CLIENTE";

    private CardLayout cardLayout;
    private JPanel panelContenedor;

    private PanelLogin panelLogin;
    private PanelDashboard panelDashboard;
    private PanelGestionLibros panelGestionLibros;
    private PanelGestionClientes panelGestionClientes;
    private PanelCliente panelCliente;

    public VentanaPrincipal(GestionSeguridad gestionSeguridad, GestionCliente gestionCliente,
                            GestionLibro gestionLibro) {
        inicializarComponentes(gestionSeguridad, gestionCliente, gestionLibro);
        configurarVentana();
    }

    private void inicializarComponentes(GestionSeguridad seguridad, GestionCliente gc, GestionLibro gl) {
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        // El controlador solo necesita la ventana: los paneles se crean después
        // y se leen a través de los getters en el momento de cada evento.
        Evento evento = new Evento(this, seguridad, gc, gl);

        panelLogin = new PanelLogin(evento);
        panelDashboard = new PanelDashboard(evento);
        panelGestionLibros = new PanelGestionLibros(evento);
        panelGestionClientes = new PanelGestionClientes(evento);
        panelCliente = new PanelCliente(evento);

        panelContenedor.add(panelLogin, PANEL_LOGIN);
        panelContenedor.add(panelDashboard, PANEL_ADMIN);
        panelContenedor.add(panelGestionLibros, PANEL_LIBROS);
        panelContenedor.add(panelGestionClientes, PANEL_CLIENTES);
        panelContenedor.add(panelCliente, PANEL_CLIENTE);

        add(panelContenedor);
    }

    private void configurarVentana() {
        setTitle("Tienda Virtual de Libros - UPTC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
    }

    public void cambiarPanel(String nombrePanel) {
        cardLayout.show(panelContenedor, nombrePanel);
    }

    public void mostrarPanelLogin() {
        cambiarPanel(PANEL_LOGIN);
    }

    public PanelLogin getPanelLogin() { return panelLogin; }
    public PanelDashboard getPanelDashboard() { return panelDashboard; }
    public PanelGestionClientes getPanelGestionClientes() { return panelGestionClientes; }
    public PanelGestionLibros getPanelGestionLibros() { return panelGestionLibros; }
    public PanelCliente getPanelCliente() { return panelCliente; }

    public CredencialDto obtenerCredenciales() {
        return panelLogin != null ? panelLogin.getCredencial() : null;
    }

    public LibroDto obtenerDatosLibro() {
        return panelGestionLibros != null ? panelGestionLibros.getLibroSeleccionado() : null;
    }

    public ClienteDto obtenerDatosCliente() {
        return panelGestionClientes != null ? panelGestionClientes.getClienteSeleccionado() : null;
    }
}
