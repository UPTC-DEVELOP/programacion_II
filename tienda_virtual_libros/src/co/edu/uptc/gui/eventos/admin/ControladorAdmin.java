package co.edu.uptc.gui.eventos.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import co.edu.uptc.gui.interfaz.admin.IGestionLibro;
import co.edu.uptc.gui.interfaz.admin.IGestionReporte;
import co.edu.uptc.gui.interfaz.admin.IVistaAdmin;
import co.edu.uptc.negocio.admin.ReglaNegocioException;
import co.edu.uptc.negocio.admin.dto.FiltroLibroDto;
import co.edu.uptc.negocio.admin.dto.LibroDto;
import co.edu.uptc.negocio.admin.dto.VentaResumenDto;


/**
 * CLASE ControladorAdmin  (paquete: eventos)  implements ActionListener
 * ---------------------------------------------------------------------------
 * PUENTE entre la presentación y el negocio (patrón MVC / controlador):
 *   1. Recibe cada clic (ActionEvent) de la GUI.
 *   2. Lo traduce a un EventoAdmin.
 *   3. Llama a la capa de negocio (IGestionLibro / IGestionReporte).
 *   4. Le ordena a la vista (IVistaAdmin) qué mostrar.
 *
 * SOLID:
 *  - S: solo coordina eventos; no valida (negocio) ni dibuja (gui).
 *  - O: una acción nueva = una constante en EventoAdmin + un caso aquí.
 *  - D: depende de TRES interfaces; no importa ninguna clase de Swing ni de
 *       persistencia. Se podría probar sin abrir ventanas.
 */
public class ControladorAdmin implements ActionListener {

    private final IVistaAdmin vista;
    private final IGestionLibro gestionLibro;
    private final IGestionReporte gestionReporte;

    public ControladorAdmin(IVistaAdmin vista, IGestionLibro gestionLibro, IGestionReporte gestionReporte) {
        this.vista = vista;
        this.gestionLibro = gestionLibro;
        this.gestionReporte = gestionReporte;
    }

    /** Estado inicial: se muestra el Dashboard con datos actualizados. */
    public void iniciar() {
        irADashboard();
    }

    /** Punto único de entrada de TODOS los eventos de la GUI. */
    @Override
    public void actionPerformed(ActionEvent e) {
        EventoAdmin evento = EventoAdmin.valueOf(e.getActionCommand());
        switch (evento) {
            case IR_DASHBOARD:      irADashboard();     break;
            case IR_GESTION_LIBROS: irAGestionLibros(); break;
            case IR_REPORTES:       vista.mostrarPantalla(Pantalla.REPORTES); break;
            case REGISTRAR_LIBRO:   registrarLibro();   break;
            case BUSCAR_LIBRO:      buscarLibros();     break;
            case EDITAR_LIBRO:      editarLibro();      break;
            case ELIMINAR_LIBRO:    eliminarLibro();    break;
            case GENERAR_REPORTE:   generarReporte();   break;
            default: break;
        }
    }

    // ============================ Navegación ============================

    private void irADashboard() {
        vista.mostrarResumen(gestionReporte.totalLibros(), gestionReporte.totalVentas(),
                gestionReporte.ingresosTotales(), gestionReporte.ventasRecientes(5));
        vista.mostrarPantalla(Pantalla.DASHBOARD);
    }

    private void irAGestionLibros() {
        vista.mostrarLibros(gestionLibro.listar(null));      // RF04 sin filtros: todo el catálogo
        vista.mostrarPantalla(Pantalla.GESTION_LIBROS);
    }

    // ============================ RF04 - Listar / buscar ============================

    private void buscarLibros() {
        FiltroLibroDto filtro = new FiltroLibroDto();
        filtro.setTexto(vista.getTextoBusqueda());
        List<LibroDto> resultado = gestionLibro.listar(filtro);
        vista.mostrarLibros(resultado);
        if (resultado.isEmpty()) {
            vista.mostrarInformacion("No se encontraron libros");   // mensaje exigido por RF04
        }
    }

    // ============================ RF01 - Registrar ============================

    private void registrarLibro() {
        LibroDto datos = null;
        while (true) {
            // Se abre el formulario (la primera vez vacío).
            datos = vista.solicitarDatosLibro(datos, false);
            if (datos == null) {
                return;                                  // el usuario canceló
            }
            try {
                gestionLibro.registrar(datos);           // el negocio valida y guarda
                vista.mostrarInformacion("Libro registrado exitosamente.");
                irAGestionLibros();
                return;
            } catch (ReglaNegocioException ex) {
                // Error de negocio: se informa y se REABRE el formulario con lo digitado,
                // así el usuario no pierde los datos.
                vista.mostrarError(ex.getMessage());
            }
        }
    }

    // ============================ RF02 - Actualizar ============================

    private void editarLibro() {
        String isbn = vista.getIsbnSeleccionado();
        if (isbn == null) {
            vista.mostrarError("Seleccione un libro de la tabla.");
            return;
        }
        LibroDto datos = gestionLibro.buscarPorIsbn(isbn);
        if (datos == null) {
            vista.mostrarError("El libro ya no existe en el catálogo.");
            irAGestionLibros();
            return;
        }
        while (true) {
            datos = vista.solicitarDatosLibro(datos, true);   // true = ISBN bloqueado
            if (datos == null) {
                return;
            }
            try {
                gestionLibro.actualizar(datos);
                vista.mostrarInformacion("Libro actualizado exitosamente.");
                irAGestionLibros();
                return;
            } catch (ReglaNegocioException ex) {
                vista.mostrarError(ex.getMessage());
            }
        }
    }

    // ============================ RF03 - Eliminar ============================

    private void eliminarLibro() {
        String isbn = vista.getIsbnSeleccionado();
        if (isbn == null) {
            vista.mostrarError("Seleccione un libro de la tabla.");
            return;
        }
        // Ventana de validación: confirmar ANTES de ejecutar la acción.
        if (!vista.confirmar("¿Está seguro de eliminar el libro con ISBN " + isbn + "?")) {
            return;
        }
        try {
            gestionLibro.eliminar(isbn);
            vista.mostrarInformacion("Libro eliminado correctamente.");
        } catch (ReglaNegocioException ex) {
            vista.mostrarError(ex.getMessage());          // p. ej. "tiene ventas registradas"
        }
        irAGestionLibros();
    }

    // ============================ Reportes ============================

    private void generarReporte() {
        String desde = vista.getFechaInicial();
        String hasta = vista.getFechaFinal();
        try {
            // Validación de formato de fecha (presentación): vacía o aaaa-mm-dd.
            if (!desde.isEmpty()) LocalDate.parse(desde);
            if (!hasta.isEmpty()) LocalDate.parse(hasta);
        } catch (DateTimeParseException ex) {
            vista.mostrarError("Las fechas deben tener el formato aaaa-mm-dd (ej. 2026-10-01).");
            return;
        }
        List<VentaResumenDto> ventas = gestionReporte.ventasEntre(desde, hasta);
        int librosVendidos = 0;
        double ganancias = 0;
        for (VentaResumenDto v : ventas) {
            librosVendidos += v.getIsbns().size();        // cada ISBN listado = 1 ejemplar vendido
            ganancias += v.getTotal();
        }
        vista.mostrarReporte(ventas, gestionReporte.totalLibros(), librosVendidos, ganancias);
    }
}