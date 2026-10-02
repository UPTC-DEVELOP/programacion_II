package co.edu.uptc;

import javax.swing.SwingUtilities;

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
        // Swing debe ejecutarse en el hilo de eventos (EDT).
        SwingUtilities.invokeLater(AppLibros::iniciar);
    }

    private static void iniciar() {
        // 1) ALMACENAMIENTO TEMPORAL EN MEMORIA (tipado por interfaz => fácil de reemplazar)
        ILibroRepositorio repositorio = new LibroRepositorioMemoria();
        IConsultaVentas ventas = new ConsultaVentasMemoria();
        IAuditoria auditoria = new AuditoriaMemoria();

        // 2) CAPA DE NEGOCIO (recibe las dependencias por constructor)
        IValidadorLibro validador = new ValidadorLibro();
        IGestionLibro gestionLibro = new GestionLibro(repositorio, ventas, auditoria, validador);
        IGestionReporte gestionReporte = new GestionReporte(repositorio, ventas);

        // 3) CAPA DE PRESENTACIÓN: la ventana y el controlador que la gobierna
        VentanaPrincipalAdmin ventana = new VentanaPrincipalAdmin();
        ControladorAdmin controlador = new ControladorAdmin(ventana, gestionLibro, gestionReporte);

        // 4) Se cierra el ciclo: la ventana notifica sus eventos al controlador
        ventana.registrarEscuchador(controlador);

        controlador.iniciar();
        ventana.setVisible(true);
    }
}
