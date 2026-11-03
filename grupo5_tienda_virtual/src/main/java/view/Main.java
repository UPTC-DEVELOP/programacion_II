package view;

import javax.swing.SwingUtilities;

import controller.ControladorCliente;
import controller.ControladorLibro;
import model.Cliente;
import model.ClientePremium;
import model.FormatoLibro;
import model.Libro;
import persistencia.IRepositorio;
import persistencia.RepositorioClientesBD;
import persistencia.RepositorioClientesJson;
import persistencia.RepositorioLibrosBD;
import persistencia.RepositorioLibrosJson;
import service.IServicioClientes;
import service.IServicioLibros;

/**
 * Punto de entrada de la aplicación.
 * Es el único lugar que conoce las IMPLEMENTACIONES concretas: ensambla la
 * aplicación en capas (persistencia -> lógica de negocio -> interfaz gráfica)
 * y le pasa a las ventanas solo las interfaces (SOLID).
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                // CAPA 1: PERSISTENCIA
                // Los datos se guardan en base de datos si se arranca con
                // -Dtienda.bd=true; si no, en archivo JSON.
            	
                boolean usarBD = Boolean.getBoolean("tienda.bd");

                IRepositorio<Cliente> repositorioClientes = usarBD
                        ? new RepositorioClientesBD()
                        : new RepositorioClientesJson();

                IRepositorio<Libro> repositorioLibros = usarBD
                        ? new RepositorioLibrosBD()
                        : new RepositorioLibrosJson();

                // CAPA 2: LÓGICA DE NEGOCIO (solo se usa la interfaz)
                
                IServicioClientes controladorClientes = new ControladorCliente(repositorioClientes);
                IServicioLibros controladorLibros = new ControladorLibro(repositorioLibros);

                cargarDatosDePrueba(controladorClientes);
                cargarLibrosDePrueba(controladorLibros);

                // CAPA 3: INTERFAZ GRÁFICA
                new VentanaLogin(controladorClientes, controladorLibros).setVisible(true);
            }
        });
    }

    // CARGA CLIENTES DE PRUEBA (solo si el JSON está vacío)
    
    private static void cargarDatosDePrueba(IServicioClientes controlador) {
        if (controlador.cantidad() > 0) {
            return;
        }
        Cliente cliente = new ClientePremium("Juan Perez", "juan@uptc.edu.co", "1234");
        cliente.setDireccion("Cra 11 #15-20, Tunja");
        cliente.setTelefono("311 555 2233");
        controlador.registrar(cliente);
    }

    // CARGA LIBROS DE PRUEBA (solo si el JSON está vacío)
    
    private static void cargarLibrosDePrueba(IServicioLibros controladorLibro) {
        if (controladorLibro.listar().size() > 0) {
            return;
        }

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
