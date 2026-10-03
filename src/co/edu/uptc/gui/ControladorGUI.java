package co.edu.uptc.gui;

import co.edu.uptc.negocio.CatalogoService;
import co.edu.uptc.negocio.FormatoLibro;
import co.edu.uptc.negocio.Libro;
import co.edu.uptc.negocio.LibroFactory;
import co.edu.uptc.negocio.PersistenciaException;
import co.edu.uptc.negocio.ValidacionException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ControladorGUI implements ActionListener, ListSelectionListener {

    private final VentanaPrincipal vista;
    private final VentanaLibro ventanaLibro;
    private final PanelDetalleLibro panelDetalle;
    private final PanelCatalogo panelCatalogo;
    private final CatalogoService servicio;

    public ControladorGUI(VentanaPrincipal vista, VentanaLibro ventanaLibro,
                          CatalogoService servicio) {
        this.vista = vista;
        this.ventanaLibro = ventanaLibro;
        this.panelDetalle = ventanaLibro.getPanelDetalle();
        this.panelCatalogo = ventanaLibro.getPanelCatalogo();
        this.servicio = servicio;
        registrarEventos();
    }

    private void registrarEventos() {
        vista.agregarListenerMenu(this);
        panelDetalle.agregarListener(this);
        panelCatalogo.agregarListener(this);
        panelCatalogo.agregarSeleccionListener(this);
    }

    public void iniciar() {
        cargarCatalogo();
        vista.setVisible(true);
        vista.mostrarVentanaInterna(ventanaLibro);
    }

    // ===================== Manejo de eventos =====================

    public void actionPerformed(ActionEvent e) {
        try {
            switch (e.getActionCommand()) {
                case Comandos.NUEVO:
                    limpiarFormulario();
                    panelDetalle.enfocarIsbn();
                    break;
                case Comandos.LIMPIAR:
                    limpiarFormulario();
                    break;
                case Comandos.GUARDAR:
                    registrarLibro();
                    break;
                case Comandos.ACTUALIZAR:
                    actualizarLibro();
                    break;
                case Comandos.ELIMINAR:
                    eliminarLibro();
                    break;
                case Comandos.FORMATO_CAMBIO:
                    panelDetalle.actualizarCampoPaginas();
                    break;
                case Comandos.BUSCAR:
                    buscarLibros();
                    break;
                case Comandos.MOSTRAR_TODOS:
                    panelCatalogo.limpiarBusqueda();
                    cargarCatalogo();
                    break;
                case Comandos.MENU_GESTIONAR_LIBROS:
                    vista.mostrarVentanaInterna(ventanaLibro);
                    break;
                case Comandos.MENU_PENDIENTE:
                    UtilidadesGUI.mostrarInformacion(vista,
                            "Este módulo se implementará en una siguiente fase del proyecto.");
                    break;
                case Comandos.MENU_ACERCA:
                    UtilidadesGUI.mostrarInformacion(vista,
                            "Tienda Virtual de Libros - UPTC\nMódulo: Gestión del Catálogo de Libros\n"
                                    + "Programación II - Versión 1.0.0");
                    break;
                case Comandos.MENU_SALIR:
                    if (UtilidadesGUI.confirmar(vista, "¿Desea salir de la aplicación?")) {
                        System.exit(0);
                    }
                    break;
                default:
                    break;
            }
        } catch (ValidacionException ex) {
            UtilidadesGUI.mostrarAdvertencia(vista, ex.getMessage());
        } catch (PersistenciaException ex) {
            // Confiabilidad: error de archivos informado sin detener la aplicación
            UtilidadesGUI.mostrarError(vista, ex.getMessage());
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        String isbn = panelCatalogo.getIsbnSeleccionado();
        if (isbn == null) {
            return;
        }
        servicio.buscarPorIsbn(isbn).ifPresent(libro -> {
            panelDetalle.cargarLibro(libro);
            panelDetalle.setModoEdicion(true);
        });
    }

    // ===================== Casos de uso =====================

    private void registrarLibro() throws ValidacionException {
        Libro libro = construirLibroDesdeFormulario();
        servicio.registrarLibro(libro);
        cargarCatalogo();
        limpiarFormulario();
        UtilidadesGUI.mostrarInformacion(vista, "Libro registrado correctamente.");
    }

    private void actualizarLibro() throws ValidacionException {
        Libro libro = construirLibroDesdeFormulario();
        servicio.actualizarLibro(libro);
        cargarCatalogo();
        limpiarFormulario();
        UtilidadesGUI.mostrarInformacion(vista, "Libro actualizado correctamente.");
    }

    private void eliminarLibro() throws ValidacionException {
        String isbn = panelDetalle.getIsbn();
        if (isbn.isEmpty()) {
            throw new ValidacionException("Seleccione en la tabla el libro que desea eliminar.");
        }
        if (!UtilidadesGUI.confirmar(vista, "¿Desea eliminar el libro con ISBN " + isbn + "?")) {
            return;
        }
        // El servicio impide eliminar libros con ventas asociadas
        servicio.eliminarLibro(isbn);
        cargarCatalogo();
        limpiarFormulario();
        UtilidadesGUI.mostrarInformacion(vista, "Libro eliminado correctamente.");
    }

    private void buscarLibros() throws ValidacionException {
        String criterio = panelCatalogo.getCriterioBusqueda();
        List<Libro> resultado = servicio.buscarLibros(criterio);
        panelCatalogo.mostrarLibros(resultado);
        // mensaje informativo si no hay coincidencias
        if (resultado.isEmpty()) {
            UtilidadesGUI.mostrarInformacion(vista,
                    "No se encontraron libros que coincidan con \"" + criterio + "\".");
        }
    }

    private void cargarCatalogo() {
        panelCatalogo.mostrarLibros(servicio.listarLibros());
    }

    private void limpiarFormulario() {
        panelDetalle.limpiar();
        panelCatalogo.limpiarSeleccion();
    }

    // ===================== Conversión y validación de formulario =====================

    private Libro construirLibroDesdeFormulario() throws ValidacionException {
        validarObligatorios();

        FormatoLibro formato = panelDetalle.getFormatoSeleccionado();

        // VALIDACIÓN de tipos: solo números enteros / decimales positivos
        int anio = parseEntero(panelDetalle.getAnioPublicacion(), "Año de publicación");
        double precio = parseDecimal(panelDetalle.getPrecioBase(), "Precio base");
        int cantidad = parseEntero(panelDetalle.getCantidadDisponible(), "Cantidad disponible");

        // VALIDACIÓN: páginas opcionales, solo físico y mayor a 0 si se informan
        int paginas = 0;
        String textoPaginas = panelDetalle.getNumeroPaginas();
        if (formato == FormatoLibro.FISICO && !textoPaginas.isEmpty()) {
            paginas = parseEntero(textoPaginas, "Número de páginas");
            if (paginas == 0) {
                throw new ValidacionException("El número de páginas, si se informa, debe ser mayor a 0.");
            }
        }

        return LibroFactory.crear(formato,
                panelDetalle.getIsbn(), panelDetalle.getTitulo(), panelDetalle.getAutor(),
                anio, panelDetalle.getCategoria(), panelDetalle.getEditorial(),
                paginas, precio, cantidad, 0.0);
    }

    private void validarObligatorios() throws ValidacionException {
        // VALIDACIÓN: ningún campo obligatorio puede quedar vacío
        List<String> faltantes = new ArrayList<>();
        if (panelDetalle.getIsbn().isEmpty()) faltantes.add("ISBN");
        if (panelDetalle.getTitulo().isEmpty()) faltantes.add("Título");
        if (panelDetalle.getAutor().isEmpty()) faltantes.add("Autor(es)");
        if (panelDetalle.getAnioPublicacion().isEmpty()) faltantes.add("Año de publicación");
        if (panelDetalle.getCategoria().isEmpty()) faltantes.add("Categoría");
        if (panelDetalle.getEditorial().isEmpty()) faltantes.add("Editorial");
        if (panelDetalle.getPrecioBase().isEmpty()) faltantes.add("Precio base");
        if (panelDetalle.getCantidadDisponible().isEmpty()) faltantes.add("Cantidad disponible");
        if (!faltantes.isEmpty()) {
            throw new ValidacionException(
                    "Debe diligenciar los campos obligatorios: " + String.join(", ", faltantes) + ".");
        }
    }

    private int parseEntero(String texto, String campo) throws ValidacionException {
        // VALIDACIÓN de tipo: no se aceptan letras ni negativos
        if (!texto.matches("\\d+")) {
            throw new ValidacionException(
                    "El campo \"" + campo + "\" solo admite números enteros positivos.");
        }
        if (texto.length() > 9) {
            throw new ValidacionException("El valor del campo \"" + campo + "\" es demasiado grande.");
        }
        return Integer.parseInt(texto);
    }

    private double parseDecimal(String texto, String campo) throws ValidacionException {
        // VALIDACIÓN de tipo: no se aceptan letras ni negativos
        if (!texto.matches("\\d+(\\.\\d+)?")) {
            throw new ValidacionException("El campo \"" + campo
                    + "\" solo admite valores numéricos positivos (use punto como separador decimal).");
        }
        if (texto.length() > 13) {
            throw new ValidacionException("El valor del campo \"" + campo + "\" es demasiado grande.");
        }
        return Double.parseDouble(texto);
    }
}
