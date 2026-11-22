package co.edu.uptc.gui;

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
        switch (comando) {
            // Usuario
            case REGISTRAR_USUARIO -> ventana.registrarUsuario();
            case INICIAR_SESION -> ventana.iniciarSesion();
            case ACTUALIZAR_USUARIO -> ventana.actualizarDatosUsuario();
            case ELIMINAR_USUARIO -> ventana.eliminarCuentaUsuario();
            case OLVIDO_PASSWORD -> ventana.mostrarOlvidoContrasena();
            case CERRAR_SESION -> ventana.cerrarSesion();

            // Libros
            case BUSCAR_LIBRO -> ventana.buscarLibros();
            case MOSTRAR_TODOS_LIBROS -> ventana.mostrarTodosLosLibros();
            case AGREGAR_LIBRO -> ventana.abrirDialogoLibro(null);
            case EDITAR_LIBRO -> ventana.editarLibroSeleccionado();
            case ELIMINAR_LIBRO -> ventana.eliminarLibroSeleccionado();
            case AGREGAR_AL_CARRITO -> ventana.agregarLibroAlCarrito();

            // Carrito
            case ACTUALIZAR_CANTIDAD_CARRITO -> ventana.actualizarCantidadCarrito();
            case ELIMINAR_DEL_CARRITO -> ventana.eliminarDelCarrito();
            case FINALIZAR_COMPRA -> ventana.realizarCompra();
        }
    }
}
