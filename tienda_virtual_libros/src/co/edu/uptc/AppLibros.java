package co.edu.uptc;

import java.awt.CardLayout;
import java.util.function.Consumer;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import co.edu.uptc.gui.EventoLogin;
import co.edu.uptc.gui.PanelLogin;
import co.edu.uptc.gui.PanelRegistro;
import co.edu.uptc.gui.admin.VentanaPrincipalAdmin;
import co.edu.uptc.gui.eventos.admin.ControladorAdmin;
import co.edu.uptc.gui.interfaz.admin.IAuditoria;
import co.edu.uptc.gui.interfaz.admin.IConsultaVentas;
import co.edu.uptc.gui.interfaz.admin.IGestionLibro;
import co.edu.uptc.gui.interfaz.admin.IGestionReporte;
import co.edu.uptc.gui.interfaz.admin.ILibroRepositorio;
import co.edu.uptc.gui.interfaz.admin.IValidadorLibro;
import co.edu.uptc.negocio.admin.GestionLibro;
import co.edu.uptc.negocio.admin.GestionReporte;
import co.edu.uptc.negocio.admin.ValidadorLibro;
import co.edu.uptc.negocio.admin.memoria.AuditoriaMemoria;
import co.edu.uptc.negocio.admin.memoria.ConsultaVentasMemoria;
import co.edu.uptc.negocio.admin.memoria.LibroRepositorioMemoria;

/**
 * CLASE AppLibros  (paquete raíz)  -  PUNTO DE ENTRADA (main)
 * ---------------------------------------------------------------------------
 * Es la "RAÍZ DE COMPOSICIÓN": el ÚNICO lugar donde se hacen los "new" de las
 * clases concretas y se conectan las capas entre sí (inyección de dependencias).
 * Todo lo demás depende de interfaces. Si mañana se cambia JSON por JDBC, solo
 * se modifica UNA línea de esta clase (principio OCP/DIP).
 *
 * PERSISTENCIA: por indicación de la guía (unidad 2) todavía NO hay archivos ni
 * base de datos. Se usan implementaciones EN MEMORIA de las interfaces. Para
 * activar la persistencia más adelante basta cambiar estas 3 líneas por
 * LibroRepositorioJson / ConsultaVentasJson / AuditoriaTxt.
 *
 * Orden de armado (de abajo hacia arriba, igual que las capas):
 *   datos en memoria -> negocio -> vista -> controlador -> conexión de eventos
 */

public class AppLibros {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppLibros::mostrarPantallaAcceso);
    }

    private static void mostrarPantallaAcceso() {
        final JDialog dialogoAcceso = new JDialog((JFrame) null, "Acceso al Sistema - Tienda de Libros", true);
        dialogoAcceso.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogoAcceso.setSize(520, 520);
        dialogoAcceso.setLocationRelativeTo(null);
        dialogoAcceso.setResizable(false);

        CardLayout cardLayout = new CardLayout();
        JPanel panelContenedor = new JPanel(cardLayout);

        PanelLogin panelLogin = new PanelLogin();
        PanelRegistro panelRegistro = new PanelRegistro();

        panelContenedor.add(panelLogin, "VISTA_LOGIN");
        panelContenedor.add(panelRegistro, "VISTA_REGISTRO");

        // Recibe el rol autorizado tras validar login
        Consumer<String> alAutenticar = (String rol) -> {
            dialogoAcceso.dispose();

            if ("Administrador".equalsIgnoreCase(rol)) {
                iniciarModoAdmin();
            } else {
                iniciarModoCliente();
            }
        };

        new EventoLogin(
            panelLogin, 
            panelRegistro, 
            cardLayout, 
            panelContenedor, 
            alAutenticar
        );

        dialogoAcceso.setContentPane(panelContenedor);
        dialogoAcceso.setVisible(true);
    }

    private static void iniciarModoAdmin() {
        // 1) ALMACENAMIENTO TEMPORAL EN MEMORIA
        ILibroRepositorio repositorio = new LibroRepositorioMemoria();
        IConsultaVentas ventas = new ConsultaVentasMemoria();
        IAuditoria auditoria = new AuditoriaMemoria();

        // 2) CAPA DE NEGOCIO
        IValidadorLibro validador = new ValidadorLibro();
        IGestionLibro gestionLibro = new GestionLibro(repositorio, ventas, auditoria, validador);
        IGestionReporte gestionReporte = new GestionReporte(repositorio, ventas);

        // 3) CAPA DE PRESENTACIÓN ADMIN
        VentanaPrincipalAdmin ventana = new VentanaPrincipalAdmin();
        ControladorAdmin controlador = new ControladorAdmin(ventana, gestionLibro, gestionReporte);

        // 4) CONEXIÓN Y ARRANQUE
        ventana.registrarEscuchador(controlador);
        controlador.iniciar();
        ventana.setVisible(true);
    }
//JOptionPane de prueba para el módulo de cliente, se puede reemplazar por la ventana principal del cliente cuando esté disponible.
    private static void iniciarModoCliente() {
        JOptionPane.showMessageDialog(
            null, 
            "Bienvenido al Módulo de Cliente.\nCargando catálogo de libros...", 
            "Módulo Cliente", 
            JOptionPane.INFORMATION_MESSAGE
        );
        // Descomentar al conectar la vista cliente
        // VentanaPrincipalCliente ventanaCliente = new VentanaPrincipalCliente();
        // ventanaCliente.setVisible(true);
    }
}