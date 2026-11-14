package co.edu.uptc.persistence.json;

import co.edu.uptc.exception.PersistenciaException;
import co.edu.uptc.model.Categoria;
import co.edu.uptc.model.FormatoLibro;
import co.edu.uptc.model.Libro;
import co.edu.uptc.model.LibroFactory;
import co.edu.uptc.persistence.LibroRepository;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// LibroJsonRepository carga el catálogo en memoria y lo reescribe después de cada cambio.
public class LibroJsonRepository implements LibroRepository {
    private final Path archivo;
    private final List<Libro> libros = new ArrayList<Libro>();

    public LibroJsonRepository(Path archivo) {
        this.archivo = archivo;
        cargar();
        if (libros.isEmpty()) { cargarSemillas(); guardarTodos(); }
    }
    @Override public List<Libro> listar() { return new ArrayList<Libro>(libros); }
    @Override public Optional<Libro> buscarPorIsbn(String isbn) { for (Libro l : libros) if (l.getIsbn().equals(isbn)) return Optional.of(l); return Optional.empty(); }
    @Override public boolean existe(String isbn) { return buscarPorIsbn(isbn).isPresent(); }
    @Override public void guardar(Libro libro) { eliminarDeMemoria(libro.getIsbn()); libros.add(libro); guardarTodos(); }
    @Override public void eliminar(String isbn) { eliminarDeMemoria(isbn); guardarTodos(); }

    private void cargar() {
        for (Map<String, String> m : ArchivoJson.leer(archivo)) {
            ArrayList<String> autores = new ArrayList<String>();
            String lista = m.get("autores"); if (lista != null && !lista.isEmpty()) for (String a : lista.split(";")) autores.add(a);
            libros.add(LibroFactory.crear(FormatoLibro.valueOf(m.get("formato")), m.get("isbn"), m.get("titulo"), autores,
                    entero(m, "anio"), Categoria.valueOf(m.get("categoria")), m.get("editorial"), entero(m, "paginas"),
                    decimal(m, "precio"), entero(m, "stock"), decimal(m, "descuento"), decimal(m, "iva")));
        }
    }
    private void guardarTodos() {
        List<Map<String, String>> filas = new ArrayList<Map<String, String>>();
        for (Libro l : libros) {
            Map<String, String> m = new LinkedHashMap<String, String>();
            m.put("isbn", l.getIsbn()); m.put("titulo", l.getTitulo()); m.put("autores", juntar(l.getAutores()));
            m.put("anio", String.valueOf(l.getAnioPublicacion())); m.put("categoria", l.getCategoria().name());
            m.put("editorial", l.getEditorial()); m.put("paginas", String.valueOf(l.getNumeroPaginas()));
            m.put("precio", String.valueOf(l.getPrecioBase())); m.put("stock", String.valueOf(l.getStock()));
            m.put("descuento", String.valueOf(l.getPorcentajeDescuento())); m.put("iva", String.valueOf(l.getImpuestoIVA()));
            m.put("formato", l.getFormato().name()); filas.add(m);
        }
        ArchivoJson.escribir(archivo, filas);
    }
    private void cargarSemillas() {
        libros.add(crear("978-958-00-0001-1", "Cien años de soledad", Categoria.LITERATURA, 50000, 10, 19, FormatoLibro.FISICO));
        libros.add(crear("978-958-00-0002-8", "El amor en los tiempos del cólera", Categoria.LITERATURA, 45000, 8, 5, FormatoLibro.FISICO));
        libros.add(crear("978-958-00-0003-5", "Crónica de una muerte anunciada", Categoria.LITERATURA, 28000, 2, 5, FormatoLibro.FISICO));
        libros.add(crear("978-958-00-0004-2", "Clean Code", Categoria.TECNOLOGIA, 120000, 5, 19, FormatoLibro.FISICO));
        libros.add(crear("978-958-00-0005-9", "Dune", Categoria.CIENCIA_FICCION, 65000, 6, 19, FormatoLibro.DIGITAL));
        libros.add(crear("978-958-00-0006-6", "Sapiens", Categoria.HISTORIA, 80000, 12, 19, FormatoLibro.FISICO));
    }
    private Libro crear(String isbn, String titulo, Categoria categoria, double precio, int stock, double iva, FormatoLibro formato) {
        ArrayList<String> autores = new ArrayList<String>(); autores.add("Autor de ejemplo");
        return LibroFactory.crear(formato, isbn, titulo, autores, 2020, categoria, "Editorial de ejemplo", 300, precio, stock, 0, iva);
    }
    private void eliminarDeMemoria(String isbn) { for (int i = libros.size() - 1; i >= 0; i--) if (libros.get(i).getIsbn().equals(isbn)) libros.remove(i); }
    private static int entero(Map<String, String> m, String clave) { return Integer.parseInt(m.get(clave)); }
    private static double decimal(Map<String, String> m, String clave) { return Double.parseDouble(m.get(clave)); }
    private static String juntar(List<String> valores) { StringBuilder s = new StringBuilder(); for (String v : valores) { if (s.length() > 0) s.append(';'); s.append(v); } return s.toString(); }
}
