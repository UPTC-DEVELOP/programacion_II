package co.edu.uptc.caso.estudio;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import co.edu.uptc.controller.Evento;
import co.edu.uptc.gui.DialogoCrearCliente;
import co.edu.uptc.gui.DialogoCrearLibro;
import co.edu.uptc.gui.VentanaPrincipal;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.Libro;
import co.edu.uptc.repository.ClienteJsonRepositorio;
import co.edu.uptc.repository.LibroJsonRepositorio;
import co.edu.uptc.util.Comandos;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Inicialización de los repositorios de datos
            Object libroRepo = instanciarRepositorio(LibroJsonRepositorio.class, "data/libros.json");
            Object clienteRepo = instanciarRepositorio(ClienteJsonRepositorio.class, "data/clientes.json");

            // 2. Creación del controlador central de eventos y ventana principal
            Evento oyenteEvento = new Evento();
            VentanaPrincipal ventana = new VentanaPrincipal(oyenteEvento);

            // 3. Registro de comandos para inicio de sesión y navegación
            oyenteEvento.registrar("LOGIN", () -> ventana.mostrarVistaCentral());
            oyenteEvento.registrar("Login", () -> ventana.mostrarVistaCentral());
            oyenteEvento.registrar("INGRESAR", () -> ventana.mostrarVistaCentral());

            // 4. Registro de comandos para abrir formularios emergentes
            oyenteEvento.registrar("Crear_Libro", () -> abrirFormularioLibro(oyenteEvento, libroRepo, ventana));
            oyenteEvento.registrar("CREAR_LIBRO", () -> abrirFormularioLibro(oyenteEvento, libroRepo, ventana));

            oyenteEvento.registrar("Crear_Cliente", () -> abrirFormularioCliente(oyenteEvento, clienteRepo, ventana));
            oyenteEvento.registrar("CREAR_CLIENTE", () -> abrirFormularioCliente(oyenteEvento, clienteRepo, ventana));

            // 5. Despliegue de la interfaz gráfica
            ventana.setVisible(true);
        });
    }

    // Método para gestionar la creación de libros y sus eventos
    private static void abrirFormularioLibro(Evento oyente, Object repo, VentanaPrincipal ventana) {
        DialogoCrearLibro dLibro = new DialogoCrearLibro(oyente, "Crear Nuevo Libro", true);

        // Registro de la acción del botón Guardar Libro
        oyente.registrar(Comandos.GUARDAR_LIBRO, () -> {
            try {
                Libro nuevoLibro = dLibro.capturarDatos();
                List<Libro> lista = cargarLista(repo);
                lista.add(nuevoLibro);
                guardarLista(repo, lista);

                JOptionPane.showMessageDialog(dLibro, "¡Libro guardado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dLibro.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dLibro, "Error al guardar el libro: " + ex.getMessage(), "Error de entrada", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Registro de la acción del botón Cancelar Libro
        oyente.registrar(Comandos.CANCELAR_CREACION_LIBRO, () -> dLibro.dispose());

        dLibro.setVisible(true);
    }

    // Método para gestionar la creación de clientes y sus eventos
    private static void abrirFormularioCliente(Evento oyente, Object repo, VentanaPrincipal ventana) {
        DialogoCrearCliente dCliente = new DialogoCrearCliente(oyente, "Crear Nuevo Cliente", true);

        // Registro de la acción del botón Guardar Cliente
        oyente.registrar(Comandos.GUARDAR_CLIENTE, () -> {
            try {
                Cliente nuevoCliente = dCliente.capturarDatos();
                List<Cliente> lista = cargarLista(repo);
                lista.add(nuevoCliente);
                guardarLista(repo, lista);

                JOptionPane.showMessageDialog(dCliente, "¡Cliente registrado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dCliente.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dCliente, "Error al guardar el cliente: " + ex.getMessage(), "Error de entrada", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Registro de la acción del botón Cancelar Cliente
        oyente.registrar(Comandos.CANCELAR_CREACION_CLIENTE, () -> dCliente.dispose());

        dCliente.setVisible(true);
    }

    // --- MÉTODOS AUXILIARES Y ADAPTADORES ---

    // Instancia el repositorio adaptándose a constructores con o sin parámetros
    private static Object instanciarRepositorio(Class<?> clazz, String ruta) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e1) {
            try {
                return clazz.getDeclaredConstructor(String.class).newInstance(ruta);
            } catch (Exception e2) {
                return null;
            }
        }
    }

    // Carga la lista de registros desde la persistencia existente
    @SuppressWarnings("unchecked")
    private static <T> List<T> cargarLista(Object repo) {
        if (repo == null) return new ArrayList<>();
        String[] metodos = {"cargar", "obtenerTodos", "getLibros", "getClientes", "getLista", "consultar", "listar", "cargarLibros", "cargarClientes"};
        for (String nombre : metodos) {
            try {
                Method m = repo.getClass().getMethod(nombre);
                Object res = m.invoke(repo);
                if (res instanceof List) {
                    return (List<T>) res;
                }
            } catch (Exception ignored) {}
        }
        return new ArrayList<>();
    }

    // Guarda la lista de registros actualizada en la persistencia existente
    private static void guardarLista(Object repo, List<?> lista) {
        if (repo == null) return;
        for (Method m : repo.getClass().getMethods()) {
            if (m.getParameterCount() == 1 && List.class.isAssignableFrom(m.getParameterTypes()[0])) {
                try {
                    m.invoke(repo, lista);
                    return;
                } catch (Exception ignored) {}
            }
        }
    }
}