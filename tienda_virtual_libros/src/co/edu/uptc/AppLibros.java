package co.edu.uptc;

import co.edu.uptc.gui.EventoLogin;
import co.edu.uptc.gui.PanelLogin;
import co.edu.uptc.gui.PanelRegistro;
import co.edu.uptc.gui.admin.VentanaPrincipalAdmin;
import co.edu.uptc.gui.cliente.VentanaPrincipalCliente;
import co.edu.uptc.gui.eventos.admin.ControladorAdmin;
import co.edu.uptc.interfaces.IAdministradorRepositorio;
import co.edu.uptc.interfaces.IAuditoria;
import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.IConsultaVentas;
import co.edu.uptc.interfaces.IGestionAcceso;
import co.edu.uptc.interfaces.IGestionCliente;
import co.edu.uptc.interfaces.IGestionLibro;
import co.edu.uptc.interfaces.IGestionReporte;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.interfaces.IValidadorLibro;
import co.edu.uptc.modelo.Rol;
import co.edu.uptc.negocio.acceso.GestionAcceso;
import co.edu.uptc.negocio.admin.GestionLibro;
import co.edu.uptc.negocio.admin.GestionReporte;
import co.edu.uptc.negocio.admin.ValidadorLibro;
import co.edu.uptc.negocio.cliente.GestionCliente;
import co.edu.uptc.persistencia.AdministradorRepositorioMemoria;
import co.edu.uptc.persistencia.AuditoriaMemoria;
import co.edu.uptc.persistencia.ConsultaVentasMemoria;
import co.edu.uptc.persistencia.LibroRepositorioMemoria;
import co.edu.uptc.persistencia.LocalCliente;
import java.awt.CardLayout;
import java.util.function.Consumer;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
 * activar la persistencia más adelante basta cambiar estas líneas por
 * LibroRepositorioJson / ConsultaVentasJson / AuditoriaTxt / ClienteRepositorioJdbc...
 *
 * Orden de armado (de abajo hacia arriba, igual que las capas):
 *   datos en memoria -> negocio -> vista -> controlador -> conexión de eventos
 */

public class AppLibros {

    // Usuarios: se crean UNA vez y se comparten entre el login, el módulo admin y el
    // de cliente (y entre sesiones), para que los registros no se pierdan al cerrar sesión.
    private static final IClienteRepositorio persistenciaClientes = new LocalCliente();
    private static final IAdministradorRepositorio persistenciaAdministradores = new AdministradorRepositorioMemoria();
    private static final IGestionCliente gestionCliente = new GestionCliente(persistenciaClientes);
    private static final IGestionAcceso gestionAcceso = new GestionAcceso(gestionCliente, persistenciaAdministradores);

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
        Consumer<Rol> alAutenticar = (Rol rol) -> {
            dialogoAcceso.dispose();

            if (rol == Rol.ADMINISTRADOR) {
                iniciarModoAdmin();
            } else {
                iniciarModoCliente(panelLogin.getCorreo());
            }
        };

        new EventoLogin(
            panelLogin, 
            panelRegistro, 
            cardLayout, 
            panelContenedor,
            gestionAcceso,
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
        VentanaPrincipalAdmin ventana = new VentanaPrincipalAdmin(gestionCliente);
        ControladorAdmin controlador = new ControladorAdmin(ventana, gestionLibro, gestionReporte);

        // 4) CONEXIÓN Y ARRANQUE
        ventana.registrarEscuchador(controlador);
        controlador.iniciar();
        ventana.setVisible(true);
    }

    /** Módulo de cliente: CRUD de clientes. "Cerrar sesión" vuelve al login. */
    private static void iniciarModoCliente(String usuario) {
        new VentanaPrincipalCliente(usuario, gestionCliente, AppLibros::mostrarPantallaAcceso).setVisible(true);
    }
}
