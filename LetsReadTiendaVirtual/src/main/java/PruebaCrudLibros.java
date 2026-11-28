import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import com.letsread.model.Libro;
import com.letsread.persistence.LibroBdRepositorio;
import com.letsread.persistence.LibroJsonRepositorio;
import com.letsread.persistence.LibroRepositorio;

/**
 * Prueba el CRUD de libros con las dos implementaciones de LibroRepositorio:
 * JSON y base de datos. Se detiene despues de cada paso para tomar capturas.
 */
public class PruebaCrudLibros {

    private static final String ISBN = "978-0000000002";
    private static final Path ARCHIVO_JSON = Path.of("libros_prueba.json");
    private static final Scanner TECLADO = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        Files.deleteIfExists(ARCHIVO_JSON);
        probar("JSON", new LibroJsonRepositorio(ARCHIVO_JSON.toString()), true);
        probar("BASE DE DATOS", new LibroBdRepositorio(), false);
        System.out.println("\nPruebas terminadas.");
    }

    private static void probar(String nombre, LibroRepositorio repo, boolean esJson) throws Exception {
        System.out.println("\n===== PRUEBA CRUD DE LIBROS - " + nombre + " =====");
        repo.eliminar(ISBN); // limpieza por si quedo algo de una prueba anterior

        Libro libro = new Libro(ISBN, "Libro de Prueba", "Autor Prueba", 2024, "Prueba",
                "Editorial Prueba", 100, 25000, 19.0, 5, "Físico");

        // 1. Registrar
        repo.guardar(libro);
        mostrar("1. REGISTRAR", repo, esJson);
        pausar();

        // 2. Actualizar
        libro.setPrecioVenta(30000);
        repo.actualizar(libro);
        mostrar("2. ACTUALIZAR (precio 25000 -> 30000)", repo, esJson);
        pausar();

        // 3. Eliminar
        repo.eliminar(ISBN);
        mostrar("3. ELIMINAR", repo, esJson);
        pausar();
    }

    private static void mostrar(String paso, LibroRepositorio repo, boolean esJson) throws Exception {
        System.out.println("\n--- " + paso + " ---");
        Libro l = repo.buscarPorIsbn(ISBN);
        if (l == null) {
            System.out.println("El libro " + ISBN + " NO existe.");
        } else {
            System.out.println("Existe: " + l.getIsbn() + " | " + l.getTitulo()
                    + " | precio " + l.getPrecioVenta() + " | stock " + l.getCantidadInventario());
        }
        if (esJson) {
            System.out.println("Contenido de " + ARCHIVO_JSON + ":");
            System.out.println(Files.readString(ARCHIVO_JSON));
        }
    }

    private static void pausar() {
        System.out.println("\n>> Toma la captura y presiona Enter en esta consola para continuar...");
        TECLADO.nextLine();
    }
}