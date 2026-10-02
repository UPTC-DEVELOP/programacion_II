package co.edu.uptc.gui.admin;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import co.edu.uptc.gui.eventos.admin.Pantalla;
import co.edu.uptc.gui.interfaz.admin.IVistaAdmin;
import co.edu.uptc.negocio.admin.dto.LibroDto;
import co.edu.uptc.negocio.admin.dto.VentaResumenDto;



/**
 * CLASE VentanaPrincipalAdmin  (paquete: gui)  extends JFrame  implements IVistaAdmin
 * ---------------------------------------------------------------------------
 * VENTANA PRINCIPAL del módulo de administración. Se llama "...Admin" a
 * propósito para NO chocar con la VentanaPrincipal de los compañeros (login,
 * cliente) cuando se unan los módulos en el repositorio.
 *
 * Estructura (BorderLayout):
 *   WEST   -> PanelMenuLateral (4 botones)
 *   CENTER -> panel con CardLayout que alterna: Dashboard / Manage Books / Reportes
 *
 * ¿Por qué implementa IVistaAdmin? Para que el controlador la maneje a través
 * de la interfaz (DIP). Esta clase solo DELEGA en sus paneles: no contiene
 * lógica de negocio (SRP).
 */
public class VentanaPrincipalAdmin extends JFrame implements IVistaAdmin {

    private static final long serialVersionUID = 1L;

    private final CardLayout cartas = new CardLayout();
    private final JPanel contenedor = new JPanel(cartas);

    private final PanelMenuLateral menu = new PanelMenuLateral();
    private final PanelDashboard dashboard = new PanelDashboard();
    private final PanelGestionLibros gestionLibros = new PanelGestionLibros();
    private final PanelReportes reportes = new PanelReportes();

    public VentanaPrincipalAdmin() {
        super("Admin Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 0));
        getContentPane().setBackground(Estilos.GRIS_FONDO);

        // Cada pantalla se registra con el nombre de su constante Pantalla.
        contenedor.add(dashboard, Pantalla.DASHBOARD.name());
        contenedor.add(gestionLibros, Pantalla.GESTION_LIBROS.name());
        contenedor.add(reportes, Pantalla.REPORTES.name());

        add(menu, BorderLayout.WEST);
        add(contenedor, BorderLayout.CENTER);

        setSize(1020, 560);
        setMinimumSize(Estilos.tamano(760, 480));
        setLocationRelativeTo(null);
    }

    /**
     * Conecta el controlador a todos los componentes que disparan eventos.
     * Se hace DESPUÉS de construir la ventana porque el controlador necesita la
     * vista y la vista necesita al controlador (se rompe el ciclo en AppLibros).
     */
    public void registrarEscuchador(ActionListener controlador) {
        menu.registrarEscuchador(controlador);
        gestionLibros.registrarEscuchador(controlador);
        reportes.registrarEscuchador(controlador);
    }

    // ===================== Implementación de IVistaAdmin =====================

    @Override
    public void mostrarPantalla(Pantalla pantalla) {
        cartas.show(contenedor, pantalla.name());
    }

    @Override
    public void mostrarLibros(List<LibroDto> libros) {
        gestionLibros.mostrarLibros(libros);
    }

    @Override
    public String getTextoBusqueda() {
        return gestionLibros.getTextoBusqueda();
    }

    @Override
    public String getIsbnSeleccionado() {
        return gestionLibros.getIsbnSeleccionado();
    }

    @Override
    public LibroDto solicitarDatosLibro(LibroDto precargado, boolean modoEdicion) {
        return new DialogoLibro(this, precargado, modoEdicion).mostrar();
    }

    @Override
    public void mostrarResumen(int totalLibros, int totalVentas, double ingresos,
                               List<VentaResumenDto> recientes) {
        dashboard.mostrarResumen(totalLibros, totalVentas, ingresos, recientes);
    }

    @Override
    public String getFechaInicial() {
        return reportes.getFechaInicial();
    }

    @Override
    public String getFechaFinal() {
        return reportes.getFechaFinal();
    }

    @Override
    public void mostrarReporte(List<VentaResumenDto> ventas, int totalLibros,
                               int librosVendidos, double ganancias) {
        reportes.mostrarReporte(ventas, totalLibros, librosVendidos, ganancias);
    }

    @Override
    public void mostrarInformacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Ventana de validación previa (RF03): el usuario debe confirmar antes de eliminar. */
    @Override
    public boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
