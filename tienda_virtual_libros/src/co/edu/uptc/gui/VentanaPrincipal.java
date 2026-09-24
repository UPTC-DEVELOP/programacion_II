package co.edu.uptc.gui;

//import co.edu.uptc.negocio.DatosPrueba;
import co.edu.uptc.negocio.GestorLibros;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación.
 * 
 * Arquitectura:
 * - Sidebar izquierdo con botones de navegación
 * - Panel central con CardLayout para cambiar entre vistas
 * - GestorLibros compartido entre todos los paneles
 * 
 * @author Oscar Clavijo
 * @version 1.0 - Septiembre 2026
 */
public class VentanaPrincipal extends JFrame {

    private JPanel panelContenido;
    private CardLayout cardLayout;
    private GestorLibros gestorLibros;

    public VentanaPrincipal() {
        setTitle("Tienda Virtual de Libros - Administrador");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.gestorLibros = new GestorLibros();
      //  DatosPrueba.cargarDatosPrueba(gestorLibros);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = crearSidebar();
        add(sidebar, BorderLayout.WEST);

        // Panel central con CardLayout 
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        panelContenido.add(crearPanelDashboard(), "Dashboard");
        PanelManageBooks PanelManageBooks = new PanelManageBooks(gestorLibros, this);
        panelContenido.add(crearPanelManageBooks(), "Manage Books");
        panelContenido.add(crearPanelReportes(), "Reportes");

        add(panelContenido, BorderLayout.CENTER);

        cardLayout.show(panelContenido, "Dashboard");
    }

    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(4, 1, 10, 10));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.setBackground(new Color(240, 240, 240));

        JButton btnDashboard = crearBotonSidebar("Dashboard");
        JButton btnManageBooks = crearBotonSidebar("Manage Books");
        JButton btnReportes = crearBotonSidebar("Reportes");
        JButton btnRegistro = crearBotonSidebar("Registro Libros");

        btnDashboard.addActionListener(e -> cardLayout.show(panelContenido, "Dashboard"));
        btnManageBooks.addActionListener(e -> cardLayout.show(panelContenido, "Manage Books"));
        btnReportes.addActionListener(e -> cardLayout.show(panelContenido, "Reportes"));
        btnRegistro.addActionListener(e -> abrirVentanaRegistro());

        sidebar.add(btnDashboard);
        sidebar.add(btnManageBooks);
        sidebar.add(btnReportes);
        sidebar.add(btnRegistro);

        return sidebar;
    }

    private JButton crearBotonSidebar(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(180, 80));
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        return boton;
    }

    private JPanel crearPanelDashboard() {
        return new PanelDashboard(gestorLibros);
    }

    private JPanel crearPanelManageBooks() {
        return new PanelManageBooks(gestorLibros, this);
    }

    private JPanel crearPanelReportes() {
        return new PanelAdministradorReportes();
    }

    /**
     * Abre la ventana de registro de libros.
     */
    private void abrirVentanaRegistro() {
        VentanaRegistroLibros dialogo = new VentanaRegistroLibros(
            this, gestorLibros, (PanelManageBooks) crearPanelManageBooks()
        );
        dialogo.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}