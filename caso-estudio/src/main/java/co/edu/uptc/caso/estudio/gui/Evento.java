package co.edu.uptc.caso.estudio.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Principio S (SOLID) - Responsabilidad Única: maneja exclusivamente el despacho de acciones de los botones.
public class Evento implements ActionListener {

    // Constantes de Usuario
    public static final String REGISTRAR_USUARIO = "REGISTRAR_USUARIO";
    public static final String INICIAR_SESION = "INICIAR_SESION";
    public static final String ACTUALIZAR_USUARIO = "ACTUALIZAR_USUARIO";
    public static final String ELIMINAR_USUARIO = "ELIMINAR_USUARIO";
    public static final String OLVIDO_PASSWORD = "OLVIDO_PASSWORD";
    public static final String CERRAR_SESION = "CERRAR_SESION";

    // Constantes de Libros
    public static final String BUSCAR_LIBRO = "BUSCAR_LIBRO";
    public static final String MOSTRAR_TODOS_LIBROS = "MOSTRAR_TODOS_LIBROS";
    public static final String AGREGAR_LIBRO = "AGREGAR_LIBRO";
    public static final String EDITAR_LIBRO = "EDITAR_LIBRO";
    public static final String ELIMINAR_LIBRO = "ELIMINAR_LIBRO";
    public static final String AGREGAR_AL_CARRITO = "AGREGAR_AL_CARRITO";

    // Constantes de Carrito y Compra
    public static final String ACTUALIZAR_CANTIDAD_CARRITO = "ACTUALIZAR_CANTIDAD_CARRITO";
    public static final String ELIMINAR_DEL_CARRITO = "ELIMINAR_DEL_CARRITO";
    public static final String FINALIZAR_COMPRA = "FINALIZAR_COMPRA";

    // Constantes de Historial
    public static final String ACTUALIZAR_HISTORIAL = PanelHistorial.ACTUALIZAR_HISTORIAL;

    private final VentanaPrincipal ventana;

    public Evento(VentanaPrincipal ventana) {
        this.ventana = ventana;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (ventana == null || e == null) {
            return;
        }
        String comando = e.getActionCommand();
        if (comando == null) {
            return;
        }

        if (despacharUsuario(comando)) {
            return;
        }
        if (despacharLibros(comando)) {
            return;
        }
        if (despacharCarritoCompra(comando)) {
            return;
        }
        despacharHistorial(comando);
    }

    private boolean despacharUsuario(String comando) {
        switch (comando) {
            case REGISTRAR_USUARIO:
                ventana.registrarUsuario();
                return true;
            case INICIAR_SESION:
                ventana.iniciarSesion();
                return true;
            case ACTUALIZAR_USUARIO:
                ventana.actualizarDatosUsuario();
                return true;
            case ELIMINAR_USUARIO:
                ventana.eliminarCuentaUsuario();
                return true;
            case OLVIDO_PASSWORD:
                ventana.mostrarOlvidoContrasena();
                return true;
            case CERRAR_SESION:
                ventana.cerrarSesion();
                return true;
            default:
                return false;
        }
    }

    private boolean despacharLibros(String comando) {
        switch (comando) {
            case BUSCAR_LIBRO:
                ventana.buscarLibros();
                return true;
            case MOSTRAR_TODOS_LIBROS:
                ventana.mostrarTodosLosLibros();
                return true;
            case AGREGAR_LIBRO:
                ventana.abrirDialogoLibro(null);
                return true;
            case EDITAR_LIBRO:
                ventana.editarLibroSeleccionado();
                return true;
            case ELIMINAR_LIBRO:
                ventana.eliminarLibroSeleccionado();
                return true;
            case AGREGAR_AL_CARRITO:
                ventana.agregarLibroAlCarrito();
                return true;
            default:
                return false;
        }
    }

    private boolean despacharCarritoCompra(String comando) {
        switch (comando) {
            case ACTUALIZAR_CANTIDAD_CARRITO:
                ventana.actualizarCantidadCarrito();
                return true;
            case ELIMINAR_DEL_CARRITO:
                ventana.eliminarDelCarrito();
                return true;
            case FINALIZAR_COMPRA:
                ventana.realizarCompra();
                return true;
            default:
                return false;
        }
    }

    private boolean despacharHistorial(String comando) {
        switch (comando) {
            case ACTUALIZAR_HISTORIAL:
                ventana.refrescarHistorial();
                return true;
            default:
                return false;
        }
    }
}