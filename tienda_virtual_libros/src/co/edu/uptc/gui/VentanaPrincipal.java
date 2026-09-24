package co.edu.uptc.gui;

import co.edu.uptc.negocio.GestorLibros;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana principal de la aplicación.
 * 
 * Arquitectura:
 * - Usa CardLayout para cambiar entre paneles sin crear nuevas ventanas
 * - Sidebar izquierdo con botones de navegación
 * - Panel central que muestra el contenido según la opción seleccionada
 * 
 * Principio aplicado: Single Responsibility
 * Esta clase SOLO maneja la navegación, no la lógica de negocio.
 * 
 * @author Oscar Clavijo
 * @version 1.0 - Septiembre 2026
 */
public class VentanaPrincipal extends JFrame {

    // Panel que contiene todos los sub-paneles 
    private JPanel panelContenido;
    
    // Layout tipo tarjeta para cambiar entre vistas
    private CardLayout cardLayout;
    
    // Gestor de libros (compartido entre todos los paneles)
    private GestorLibros gestorLibros;

    //  Constructor
    
    public VentanaPrincipal() {
        // Configuración básica de la ventana
        setTitle("Tienda Virtual de Libros - Administrador");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        // Inicializar el gestor de libros (en memoria por ahora)
        this.gestorLibros = new GestorLibros();

        // Crear la estructura de la ventana
        inicializarComponentes();
    }

    /**
     * Crea y organiza todos los componentes de la ventana.
     */
    private void inicializarComponentes() {
        // Layout principal: BorderLayout (sidebar a la izquierda, contenido al centro)
        setLayout(new BorderLayout());

        //  Sidebar izquierdo
        JPanel sidebar = crearSidebar();
        add(sidebar, BorderLayout.WEST);

        //  Panel central con CardLayout
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        // Agregar los diferentes paneles al CardLayout
        panelContenido.add(crearPanelDashboard(), "Dashboard");
        panelContenido.add(crearPanelRegistro(), "Registro");
        panelContenido.add(crearPanelReportes(), "Reportes");

        add(panelContenido, BorderLayout.CENTER);

        // Mostrar el dashboard por defecto
        cardLayout.show(panelContenido, "Dashboard");
    }

    /**
     * Crea el sidebar con los botones de navegación.
     */
    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(4, 1, 10, 10)); // 4 filas, 1 columna
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.setBackground(new Color(240, 240, 240));

        // Botones de navegación
        JButton btnDashboard = crearBotonSidebar("Dashboard");
        JButton btnRegistro = crearBotonSidebar("Registro Libros");
        JButton btnReportes = crearBotonSidebar("Reportes");
        JButton btnSalir = crearBotonSidebar("Salir");

        // Eventos de los botones
        btnDashboard.addActionListener(e -> cardLayout.show(panelContenido, "Dashboard"));
        btnRegistro.addActionListener(e -> cardLayout.show(panelContenido, "Registro"));
        btnReportes.addActionListener(e -> cardLayout.show(panelContenido, "Reportes"));
        btnSalir.addActionListener(e -> System.exit(0));

        sidebar.add(btnDashboard);
        sidebar.add(btnRegistro);
        sidebar.add(btnReportes);
        sidebar.add(btnSalir);

        return sidebar;
    }

    /**
     * Crea un botón estilizado para el sidebar.
     */
    private JButton crearBotonSidebar(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(180, 50));
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        return boton;
    }

    // Fábricas de paneles 

    private JPanel crearPanelDashboard() {
        return new PanelDashboard(gestorLibros);
    }

    private JPanel crearPanelRegistro() {
        return new PanelAdministradorRegistro(gestorLibros);
    }

    private JPanel crearPanelReportes() {
        return new PanelAdministradorReportes();
    }

    //  Método principal para probar la aplicación 
    public static void main(String[] args) {
        // Ejecutar en el hilo de eventos de Swing (thread-safe)
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}