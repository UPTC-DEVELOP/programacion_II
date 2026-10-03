package co.edu.uptc.gui;

import co.edu.uptc.negocio.CatalogoService;
import co.edu.uptc.negocio.Libro;
import co.edu.uptc.negocio.PersistenciaException;
import co.edu.uptc.negocio.ValidacionException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private void buscarLibros() throws ValidacionException {
        String criterio = panelCatalogo.getCriterioBusqueda();
        List<Libro> resultado = servicio.buscarLibros(criterio);
        panelCatalogo.mostrarLibros(resultado);
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
}
