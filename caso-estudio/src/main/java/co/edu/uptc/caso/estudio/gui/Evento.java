// Principio S (SOLID) - Responsabilidad Única: maneja exclusivamente el despacho de acciones de los botones.

package co.edu.uptc.caso.estudio.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private final VentanaPrincipal ventana;

    public Evento(VentanaPrincipal ventana) {
        this.ventana = ventana;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        if (comando == null) {
            return;
        }
        switch (comando) {
            case REGISTRAR_USUARIO:
                ventana.registrarUsuario();
                break;
            case INICIAR_SESION:
                ventana.iniciarSesion();
                break;
            case ACTUALIZAR_USUARIO:
                ventana.actualizarDatosUsuario();
                break;
            case ELIMINAR_USUARIO:
                ventana.eliminarCuentaUsuario();
                break;
            case OLVIDO_PASSWORD:
                ventana.mostrarOlvidoContrasena();
                break;
            case CERRAR_SESION:
                ventana.cerrarSesion();
                break;
            case BUSCAR_LIBRO:
                ventana.buscarLibros();
                break;
            case MOSTRAR_TODOS_LIBROS:
                ventana.mostrarTodosLosLibros();
                break;
            case AGREGAR_LIBRO:
                ventana.abrirDialogoLibro(null);
                break;
            case EDITAR_LIBRO:
                ventana.editarLibroSeleccionado();
                break;
            case ELIMINAR_LIBRO:
                ventana.eliminarLibroSeleccionado();
                break;
            case AGREGAR_AL_CARRITO:
                ventana.agregarLibroAlCarrito();
                break;
            case ACTUALIZAR_CANTIDAD_CARRITO:
                ventana.actualizarCantidadCarrito();
                break;
            case ELIMINAR_DEL_CARRITO:
                ventana.eliminarDelCarrito();
                break;
            case FINALIZAR_COMPRA:
                ventana.realizarCompra();
                break;
            default:
                break;
        }
    }
}
