package persistencia;

import java.util.ArrayList;
import java.util.List;

import model.Libro;

/**
 * Repositorio de libros en archivo de texto plano (datos/libros.txt).
 */
public class RepositorioLibrosArchivoPlano implements IRepositorio<Libro> {

    private static final String RUTA_POR_DEFECTO = "datos/libros.txt";
    private static final String SEPARADOR = "\t";

    private final String rutaArchivo;
    private final List<Libro> libros;

    public RepositorioLibrosArchivoPlano() {
        this(RUTA_POR_DEFECTO);
    }

    public RepositorioLibrosArchivoPlano(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.libros = new ArrayList<Libro>();
        cargar();
    }

    @Override
    public List<Libro> listar() {
        return libros;
    }

    @Override
    public void agregar(Libro elemento) {
        if (elemento != null) {
            libros.add(elemento);
        }
    }

    @Override
    public boolean eliminar(Libro elemento) {
        if (elemento == null) {
            return false;
        }
        return libros.remove(elemento);
    }

    @Override
    public void cargar() {
        // Implementacion en C13
    }

    @Override
    public void guardar() {
        // Implementacion en C14
    }
}
