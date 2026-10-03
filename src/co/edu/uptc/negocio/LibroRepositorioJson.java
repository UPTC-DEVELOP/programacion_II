package co.edu.uptc.negocio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LibroRepositorioJson implements LibroRepositorio {

    private final Path archivo;
    private final Map<String, Libro> libros = new LinkedHashMap<>();

    public LibroRepositorioJson(Path archivo) {
        this.archivo = archivo;
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
            if (Files.exists(archivo)) {
                cargar();
            } else {
                cargarDatosSemilla();
                persistir();
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo preparar el archivo del catálogo.", e);
        }
    }

 
    public List<Libro> obtenerTodos() {
        return new ArrayList<>(libros.values());
    }

    public Optional<Libro> buscarPorIsbn(String isbn) {
        return Optional.ofNullable(libros.get(isbn));
    }

    public boolean existe(String isbn) {
        return libros.containsKey(isbn);
    }

    public void guardar(Libro libro) {
        libros.put(libro.getIsbn(), libro);
        persistir(); // Disponibilidad de datos: se actualiza en cada operación crítica
    }
    public boolean eliminar(String isbn) {
        boolean eliminado = libros.remove(isbn) != null;
        if (eliminado) {
            persistir();
        }
        return eliminado;
    }

    // ===================== Utilidades internas =====================

    private void cargar() throws IOException {
        String contenido = new String(Files.readAllBytes(archivo), StandardCharsets.UTF_8);
        try {
            for (Map<String, String> m : JsonUtil.desdeJson(contenido)) {
                Libro libro = aLibro(m);
                libros.put(libro.getIsbn(), libro);
            }
        } catch (RuntimeException e) {
       
            throw new PersistenciaException("El archivo libros.json tiene un formato inválido.", e);
        }
    }

    private void persistir() {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Libro libro : libros.values()) {
            lista.add(aMapa(libro));
        }
        try {
            Files.write(archivo, JsonUtil.aJson(lista).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo guardar el catálogo en disco.", e);
        }
    }

    private Libro aLibro(Map<String, String> m) {
        return LibroFactory.crear(
                FormatoLibro.valueOf(m.get("formato")),
                m.get("isbn"),
                m.get("titulo"),
                m.get("autor"),
                Integer.parseInt(m.get("anioPublicacion")),
                m.get("categoria"),
                m.get("editorial"),
                Integer.parseInt(m.get("numeroPaginas")),
                Double.parseDouble(m.get("precioBase")),
                Integer.parseInt(m.get("cantidadDisponible")),
               
                m.containsKey("porcentajeDescuento")
                        ? Double.parseDouble(m.get("porcentajeDescuento")) : 0.0);
    }

    private Map<String, Object> aMapa(Libro l) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("isbn", l.getIsbn());
        m.put("titulo", l.getTitulo());
        m.put("autor", l.getAutor());
        m.put("anioPublicacion", l.getAnioPublicacion());
        m.put("categoria", l.getCategoria());
        m.put("editorial", l.getEditorial());
        m.put("numeroPaginas", l.getNumeroPaginas());
        m.put("precioBase", l.getPrecioBase());
        m.put("cantidadDisponible", l.getCantidadDisponible());
        m.put("porcentajeDescuento", l.getPorcentajeDescuento());
        m.put("formato", l.getFormato().name());
        return m;
    }
    
    private void cargarDatosSemilla() {
        agregarSemilla(LibroFactory.crear(FormatoLibro.FISICO, "9789586600001",
                "Cien años de soledad", "Gabriel García Márquez", 1967, "Novela",
                "Editorial Sudamericana", 471, 50000, 150, 10));
        agregarSemilla(LibroFactory.crear(FormatoLibro.FISICO, "9788437604947",
                "Don Quijote de la Mancha", "Miguel de Cervantes", 1605, "Clásico",
                "Cátedra", 1200, 45000, 85, 0));
        agregarSemilla(LibroFactory.crear(FormatoLibro.DIGITAL, "9780132350884",
                "Clean Code", "Robert C. Martin", 2008, "Tecnología",
                "Prentice Hall", 0, 110000, 45, 15));
        agregarSemilla(LibroFactory.crear(FormatoLibro.FISICO, "9789589585141",
                "La María", "Jorge Isaacs", 1867, "Romántica",
                "Editorial Norma", 320, 35000, 210, 5));
    }

    private void agregarSemilla(Libro libro) {
        libros.put(libro.getIsbn(), libro);
    }
}
