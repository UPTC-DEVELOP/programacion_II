package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.FormatoLibro;
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
        libros.clear();
        libros.addAll(leerTodos());
    }

    @Override
    public List<Libro> listarUltimos(int limite) {
        List<Libro> todos = leerTodos();
        if (limite <= 0 || todos.isEmpty()) {
            return new ArrayList<Libro>();
        }
        int desde = Math.max(0, todos.size() - limite);
        List<Libro> ultimos = new ArrayList<Libro>(todos.subList(desde, todos.size()));
        Collections.reverse(ultimos);
        return ultimos;
    }

    @Override
    public List<Libro> listarPagina(int inicio, int cantidad) {
        List<Libro> todos = leerTodos();
        int desde = Math.max(0, inicio);
        int hasta = Math.min(todos.size(), desde + Math.max(0, cantidad));
        if (desde >= hasta) {
            return new ArrayList<Libro>();
        }
        return new ArrayList<Libro>(todos.subList(desde, hasta));
    }

    @Override
    public int contar() {
        return leerTodos().size();
    }

    private List<Libro> leerTodos() {
        List<Libro> leidos = new ArrayList<Libro>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return leidos;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            int numeroLinea = 0;
            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                if (linea.trim().isEmpty()) {
                    continue;
                }
                leidos.add(parsearLinea(linea, numeroLinea));
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al leer el archivo de libros: " + rutaArchivo, e);
        }
        return leidos;
    }

    /**
     * Valida y convierte una linea tabulada en un objeto Libro.
     */
    private Libro parsearLinea(String linea, int numeroLinea) {
        String[] partes = linea.split(SEPARADOR, -1);
        if (partes.length < 11) {
            throw new PersistenciaException(
                    "Formato invalido en libros.txt en la linea " + numeroLinea + ": se esperaban 11 columnas.");
        }

        String isbn = partes[0].trim();
        String titulo = partes[1].trim();
        String autor = partes[2].trim();
        if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty()) {
            throw new PersistenciaException(
                    "Datos obligatorios vacios (isbn, titulo o autor) en libros.txt en la linea " + numeroLinea + ".");
        }

        int anioPublicacion;
        int numeroPaginas;
        double precio;
        double porcentajeIva;
        int cantidadDisponible;
        try {
            anioPublicacion = Integer.parseInt(partes[3].trim());
            numeroPaginas = Integer.parseInt(partes[6].trim());
            precio = Double.parseDouble(partes[7].trim());
            porcentajeIva = Double.parseDouble(partes[8].trim());
            cantidadDisponible = Integer.parseInt(partes[9].trim());
        } catch (NumberFormatException e) {
            throw new PersistenciaException(
                    "Valor numerico invalido en libros.txt en la linea " + numeroLinea + ": " + e.getMessage(), e);
        }

        if (numeroPaginas < 0 || precio < 0 || porcentajeIva < 0 || cantidadDisponible < 0) {
            throw new PersistenciaException(
                    "Valores numericos negativos no permitidos en libros.txt en la linea " + numeroLinea + ".");
        }

        FormatoLibro formato;
        try {
            formato = FormatoLibro.valueOf(partes[10].trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new PersistenciaException(
                    "Formato de libro invalido en libros.txt en la linea " + numeroLinea + ": '" + partes[10].trim()
                            + "'.",
                    e);
        }

        return construirLibro(isbn, titulo, autor, anioPublicacion, partes[4].trim(), partes[5].trim(),
                numeroPaginas, precio, porcentajeIva, cantidadDisponible, formato);
    }

    private Libro construirLibro(String isbn, String titulo, String autor, int anioPublicacion, String categoria,
            String editorial, int numeroPaginas, double precio, double porcentajeIva, int cantidadDisponible,
            FormatoLibro formato) {
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setAnioPublicacion(anioPublicacion);
        libro.setCategoria(categoria);
        libro.setEditorial(editorial);
        libro.setNumeroPaginas(numeroPaginas);
        libro.setPrecio(precio);
        libro.setPorcentajeIva(porcentajeIva);
        libro.setCantidadDisponible(cantidadDisponible);
        libro.setFormato(formato);
        return libro;
    }

    @Override
    public void guardar() {
        File archivo = new File(rutaArchivo);
        File carpetaPadre = archivo.getParentFile();
        if (carpetaPadre != null && !carpetaPadre.exists()) {
            carpetaPadre.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
            for (Libro libro : libros) {
                writer.write(formatearLinea(libro));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar el archivo de libros: " + rutaArchivo, e);
        }
    }

    /**
     * Serializa un libro en una linea tabulada compatible con parsearLinea().
     */
    private String formatearLinea(Libro libro) {
        FormatoLibro formato = libro.getFormato() != null ? libro.getFormato() : FormatoLibro.FISICO;
        return String.join(SEPARADOR,
                limpiarCampo(libro.getIsbn()),
                limpiarCampo(libro.getTitulo()),
                limpiarCampo(libro.getAutor()),
                String.valueOf(libro.getAnioPublicacion()),
                limpiarCampo(libro.getCategoria()),
                limpiarCampo(libro.getEditorial()),
                String.valueOf(libro.getNumeroPaginas()),
                String.valueOf(libro.getPrecio()),
                String.valueOf(libro.getPorcentajeIva()),
                String.valueOf(libro.getCantidadDisponible()),
                formato.name());
    }

    private String limpiarCampo(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace("\t", " ").replace("\r", " ").replace("\n", " ");
    }
}