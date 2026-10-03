package co.edu.uptc.gui.eventos.admin;


import java.util.List;

import co.edu.uptc.gui.eventos.admin.Pantalla;
import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.modelo.dto.VentaResumenDto;


/**
 * INTERFAZ IVistaAdmin  (paquete: gui.eventos.admin)
 * ---------------------------------------------------------------------------
 * CONTRATO de la capa de PRESENTACIÓN visto desde el controlador.
 * El controlador le "pide cosas" a la vista (mostrar tabla, mostrar mensaje,
 * pedir confirmación) sin saber que detrás hay Swing. Así:
 *   - DIP: el controlador depende de una abstracción, no de JFrame/JPanel.
 *   - Se puede probar el controlador con una vista falsa (sin ventanas).
 */
public interface IVistaAdmin {

    // ---- Navegación ----
    void mostrarPantalla(Pantalla pantalla);

    // ---- Gestión de libros ----
    void mostrarLibros(List<LibroDto> libros);
    String getTextoBusqueda();
    /** ISBN de la fila sobre la que el usuario pulsó Editar/Eliminar. */
    String getIsbnSeleccionado();
    /**
     * Abre el formulario de libro.
     * @param precargado  datos con que se llena el formulario (null = vacío)
     * @param modoEdicion true: el ISBN queda bloqueado (RF02: el ISBN no se edita)
     * @return los datos digitados o null si el usuario canceló.
     */
    LibroDto solicitarDatosLibro(LibroDto precargado, boolean modoEdicion);

    // ---- Dashboard / Reportes ----
    void mostrarResumen(int totalLibros, int totalVentas, double ingresos,
                        List<VentaResumenDto> recientes);
    String getFechaInicial();
    String getFechaFinal();
    void mostrarReporte(List<VentaResumenDto> ventas, int totalLibros,
                        int librosVendidos, double ganancias);

    // ---- Mensajes ----
    void mostrarInformacion(String mensaje);
    void mostrarError(String mensaje);
    boolean confirmar(String mensaje);
}