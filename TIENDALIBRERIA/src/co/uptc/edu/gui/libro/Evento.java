package co.uptc.edu.gui.libro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class Evento implements ActionListener {

    // Constantes de comandos generales
    public static final String CANCELAR = "Cancelar";
    public static final String LOGIN = "Login";

    // Constantes CRUD para Libros
    public static final String ELIMINAR_LIBRO = "Eliminar_Libro";
    public static final String VER_LIBRO = "Ver_Libro";
    public static final String ACTUALIZAR_LIBRO = "Actualizar_Libro";
    public static final String CREAR_LIBRO = "Crear_Libro";
    public static final String LIMPIAR_LIBRO = "Limpiar_Libro";
    public static final String BUSCAR_LIBRO = "Buscar_Libro";

    // Constantes para guardar/editar desde diálogos
    public static final String GUARDAR_LIBRO = "Guardar_Libro";
    public static final String EDITAR_LIBRO = "Editar_Libro";
    public static final String CANCELAR_CREACION_LIBRO = "Cancelar_Creacion_Libro";
	public static final String CANCELAR_LIBRO = "Cancelar_Libro";
	public static final String GUARDAR_ACTUALIZACION = "Guardar_Actualizacion";

    // Constantes CRUD para Clientes
    public static final String CREAR_CLIENTE = "Crear_Cliente";
    public static final String ACTUALIZAR_CLIENTE = "Actualizar_Cliente";
    public static final String ELIMINAR_CLIENTE = "Eliminar_Cliente";
    public static final String VER_CLIENTE = "Ver_Cliente";
    public static final String BUSCAR_CLIENTE = "Buscar_Cliente";
    public static final String LIMPIAR_CLIENTE = "Limpiar_Cliente";

    // Constantes para guardar/editar clientes desde dialogos
    public static final String GUARDAR_CLIENTE = "Guardar_Cliente";
    public static final String CANCELAR_CREACION_CLIENTE = "Cancelar_Creacion_Cliente";
    public static final String GUARDAR_ACTUALIZACION_CLIENTE = "Guardar_Actualizacion_Cliente";
    public static final String CANCELAR_CLIENTE = "Cancelar_Cliente";
	
    private final VentanaPrincipal ventana;

    public Evento(VentanaPrincipal v) {
        this.ventana = v;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando == null) {
            return;
        }

        switch (comando) {
            case CANCELAR:
                JOptionPane.showMessageDialog(null, "Operación cancelada");
                break;
            case LOGIN:
                ventana.loguear();
                break;
            case CREAR_LIBRO:
                ventana.lanzarDialogoLibro();
                break;
            case CANCELAR_CREACION_LIBRO:
            case CANCELAR_LIBRO:
                ventana.cerrarDialogoLibro();
                break;
            case GUARDAR_LIBRO:
                ventana.crearLibro();
                break;
            case ELIMINAR_LIBRO:
                ventana.eliminarLibro();
                break;
            case ACTUALIZAR_LIBRO:
                ventana.actualizarLibro();
                break;
            case VER_LIBRO:
                ventana.verLibro();
                break;
            case BUSCAR_LIBRO:
                ventana.buscarLibro();
                break;
            case LIMPIAR_LIBRO:
                ventana.limpiarTablaLibros();
                break;
            case GUARDAR_ACTUALIZACION:
                ventana.guardarActualizacionLibro();
                break;
            case CREAR_CLIENTE:
                ventana.lanzarDialogoCliente();
                break;
            case CANCELAR_CREACION_CLIENTE:
            case CANCELAR_CLIENTE:
                ventana.cerrarDialogoCliente();
                break;
            case GUARDAR_CLIENTE:
                ventana.crearCliente();
                break;
            case ACTUALIZAR_CLIENTE:
                ventana.actualizarCliente();
                break;
            case GUARDAR_ACTUALIZACION_CLIENTE:
                ventana.guardarActualizacionCliente();
                break;
            case ELIMINAR_CLIENTE:
                ventana.eliminarCliente();
                break;
            case VER_CLIENTE:
                ventana.verCliente();
                break;
            case BUSCAR_CLIENTE:
                ventana.buscarCliente();
                break;
            case LIMPIAR_CLIENTE:
                ventana.limpiarTablaClientes();
                break;
            default:
                // Comando no reconocido
                break;
        }
    }
}