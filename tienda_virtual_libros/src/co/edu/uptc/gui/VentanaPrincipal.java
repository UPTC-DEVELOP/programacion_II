package co.edu.uptc.gui;

//import co.edu.uptc.negocio.DatosPrueba;
import co.edu.uptc.negocio.ControladorLogin;
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
@SuppressWarnings("serial")
public class VentanaPrincipal extends JFrame {

    private JPanel panelContenido;
    private CardLayout cardLayout;
    private GestorLibros gestorLibros;

    /*
     * Modificaciones por: Brayan Javier Panqueva Pelayo
     * Atributo para gestionar la ventana emergente modal (JDialog)
     * que desplegará el formulario de autenticación antes de cargar
     * el panel principal del Administrador.
     */
    private JDialog dialogoLogin;

    public VentanaPrincipal() {
        setTitle("Tienda Virtual de Libros - Administrador");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.gestorLibros = new GestorLibros();
      //  DatosPrueba.cargarDatosPrueba(gestorLibros);

        /*
         * Modificaciones por: Brayan Javier Panqueva Pelayo
         * Se despliega el login emergente en el constructor de la ventana.
         * Se difiere la carga de componentes de administración hasta que 
         * el inicio de sesión se complete exitosamente.
         */
        mostrarLoginEmergente();
    }

    /*
     * Modificaciones por: Brayan Javier Panqueva Pelayo
     * Método que configura y visualiza el PanelLogin como ventana emergente (JDialog).
     * Conecta la vista con ControladorLogin y, en caso de cerrar la ventana sin autenticarse,
     * finaliza la ejecución del sistema.
     */
    private void mostrarLoginEmergente() {
        dialogoLogin = new JDialog(this, "Autenticación de Usuario", true);
        dialogoLogin.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Si el usuario da clic en la X para cerrar la ventana emergente, finaliza la app
        dialogoLogin.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        // Instancia de la vista del login
        PanelLogin panelLogin = new PanelLogin();

        // Conexión con el ControladorLogin ubicado en el paquete negocio
        new ControladorLogin(panelLogin, () -> {
            dialogoLogin.dispose();
            inicializarComponentes();
        });

        dialogoLogin.setContentPane(panelLogin);
        dialogoLogin.setSize(400, 450);
        dialogoLogin.setResizable(false);
        dialogoLogin.setLocationRelativeTo(this);
        dialogoLogin.setVisible(true);
    }

    @SuppressWarnings("unused")
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

        /*
         * Modificaciones por: Brayan Javier Panqueva Pelayo
         * Revalida y redibuja la ventana principal para forzar la representación gráfica 
         * adecuada del layout tras la destrucción del diálogo modal.
         */
        revalidate();
        repaint();
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