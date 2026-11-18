package view;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import config.ProjectConfig;
import controller.ControladorCliente;
import controller.ControladorCompras;
import controller.ControladorLibro;
import model.Cliente;
import model.ClientePremium;
import model.FormatoLibro;
import model.Libro;
import persistencia.PersistenciaException;
import service.IServicioClientes;
import service.IServicioCompras;
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
                try {
                    // CAPA 1 y 2: persistencia y lógica de negocio
                	
                    IServicioClientes controladorClientes =
                            new ControladorCliente(ProjectConfig.clientes());
                    IServicioLibros controladorLibros =
                            new ControladorLibro(ProjectConfig.libros());
                    IServicioCompras controladorCompras =
                            new ControladorCompras(ProjectConfig.compras());

                    cargarDatosDePrueba(controladorClientes);
                    cargarLibrosDePrueba(controladorLibros);

                    // CAPA 3: INTERFAZ GRÁFICA
                    new VentanaLogin(controladorClientes, controladorLibros, controladorCompras)
                            .setVisible(true);
                } catch (PersistenciaException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "No se pudo inicializar el almacenamiento:\n" + e.getMessage(),
                            "Error de persistencia", JOptionPane.ERROR_MESSAGE);
                }
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
        if (!controlador.registrar(cliente)) {
            throw new PersistenciaException("No se pudo guardar el cliente de prueba");
        }
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

        if (!controladorLibro.registrar(libro1)
                || !controladorLibro.registrar(libro2)
                || !controladorLibro.registrar(libro3)) {
            throw new PersistenciaException("No se pudieron guardar los libros de prueba");
        }
    }
}
