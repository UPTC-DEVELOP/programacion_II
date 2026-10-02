package gui;

import javax.swing.SwingUtilities;

import negocio.Cliente;
import negocio.ClientePremium;
import negocio.ControladorCliente;
import negocio.ControladorLibro;
import negocio.FormatoLibro;
import negocio.Libro;


/**
 * Punto de entrada de la aplicacion. Abre la ventana "Iniciar Sesion".
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

            	// CONTROLADOR DE CLIENTES
                ControladorCliente controlador = new ControladorCliente();
                cargarDatosDePrueba(controlador);

                // CONTROLADOR DE LIBROS
                ControladorLibro controladorLibro = new ControladorLibro();
                cargarLibrosDePrueba(controladorLibro);

                // ABRIR VENTANA DE INICIO DE SESION
                new VentanaLogin(controlador, controladorLibro).setVisible(true);
            }
        });
    }

 // CARGAR CLIENTE DE PRUEBA
    private static void cargarDatosDePrueba(ControladorCliente controlador) {
        Cliente cliente = new ClientePremium("Juan Perez", "juan@uptc.edu.co", "1234");
       
        cliente.setDireccion("Cra 11 #15-20, Tunja");
        cliente.setTelefono("311 555 2233");
        
        controlador.registrar(cliente);
    }
    
 // CARGAR LIBROS DE PRUEBA
    private static void cargarLibrosDePrueba(ControladorLibro controladorLibro) {

        Libro libro1 = new Libro(
                "978-001",
                "Java para principiantes",
                "Carlos Gomez",
                50000,
                19.0,
                10,
                FormatoLibro.FISICO
        );

        Libro libro2 = new Libro(
                "978-002",
                "Programacion Orientada a Objetos",
                "Ana Rodriguez",
                65000,
                19.0,
                8,
                FormatoLibro.FISICO
        );

        Libro libro3 = new Libro(
                "978-003",
                "Fundamentos de Bases de Datos",
                "Luis Martinez",
                40000,
                5.0,
                15,
                FormatoLibro.DIGITAL
        );

        controladorLibro.registrar(libro1);
        controladorLibro.registrar(libro2);
        controladorLibro.registrar(libro3);
    }
    
    
}