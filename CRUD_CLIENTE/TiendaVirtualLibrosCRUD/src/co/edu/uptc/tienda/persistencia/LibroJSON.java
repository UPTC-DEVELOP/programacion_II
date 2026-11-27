package co.edu.uptc.tienda.persistencia;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.modelo.LibroFisico;
import co.edu.uptc.tienda.modelo.enums.TipoLibro;
import co.edu.uptc.tienda.negocio.IRepositorioLibro;
import co.edu.uptc.tienda.negocio.NegocioException;

/**
 * Repositorio de libros en un archivo JSON (data/libros.json). Cumple el mismo
 * contrato que BaseDeDatosLibro, por eso el negocio no cambia al usarlo.
 */
public class LibroJSON implements IRepositorioLibro {

	private static final Path RUTA_ARCHIVO = Paths.get("data", "libros.json");

	private static final Type TIPO_LISTA = new TypeToken<List<RegistroLibro>>() {
	}.getType();

	private final Gson gson;

	public LibroJSON() {
		this.gson = new GsonBuilder().setPrettyPrinting().create();
	}

	@Override
	public void guardar(Libro libro) {
		List<Libro> libros = leerArchivo();

		// La factura guarda el id del libro, por eso se asigna aquí (mayor + 1)
		libro.setIdLibro(calcularSiguienteId(libros));
		libros.add(libro);

		escribirArchivo(libros);
	}

	@Override
	public List<Libro> listar() {
		return leerArchivo();
	}

	@Override
	public Libro buscarPorIsbn(String isbn) {
		for (Libro libro : leerArchivo()) {
			if (libro.getIsbn().equals(isbn)) {
				return libro;
			}
		}
		return null;
	}

	@Override
	public Libro buscarPorTitulo(String titulo) {
		for (Libro libro : leerArchivo()) {
			if (libro.getTitulo().equalsIgnoreCase(titulo)) {
				return libro;
			}
		}
		return null;
	}

	@Override
	public boolean actualizar(Libro libroActualizado) {
		List<Libro> libros = leerArchivo();

		for (int i = 0; i < libros.size(); i++) {
			if (libros.get(i).getIsbn().equals(libroActualizado.getIsbn())) {
				libros.set(i, libroActualizado);
				escribirArchivo(libros);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean eliminar(String isbn) {
		List<Libro> libros = leerArchivo();

		boolean eliminado = libros.removeIf(libro -> libro.getIsbn().equals(isbn));
		if (eliminado) {
			escribirArchivo(libros);
		}
		return eliminado;
	}

	private long calcularSiguienteId(List<Libro> libros) {
		long mayorId = 0;
		for (Libro libro : libros) {
			if (libro.getIdLibro() != null) {
				mayorId = Math.max(mayorId, libro.getIdLibro());
			}
		}
		return mayorId + 1;
	}

	private List<Libro> leerArchivo() {
		List<Libro> libros = new ArrayList<>();

		if (!Files.exists(RUTA_ARCHIVO)) {
			return libros;
		}

		try {
			String contenido = Files.readString(RUTA_ARCHIVO, StandardCharsets.UTF_8);
			List<RegistroLibro> registros = gson.fromJson(contenido, TIPO_LISTA);

			if (registros != null) {
				for (RegistroLibro registro : registros) {
					libros.add(registro.aLibro());
				}
			}
			return libros;

		} catch (IOException | JsonParseException | IllegalArgumentException | NullPointerException e) {
			// No se devuelve una lista vacía: al guardar se sobrescribirían los datos
			System.err.println("Error al leer el archivo de libros -> " + e.getMessage());
			throw new NegocioException("No se pudo leer el archivo de libros: " + RUTA_ARCHIVO.toAbsolutePath());
		}
	}

	private void escribirArchivo(List<Libro> libros) {
		List<RegistroLibro> registros = new ArrayList<>();
		for (Libro libro : libros) {
			registros.add(new RegistroLibro(libro));
		}

		try {
			Path carpeta = RUTA_ARCHIVO.getParent();
			if (carpeta != null) {
				Files.createDirectories(carpeta);
			}
			Files.writeString(RUTA_ARCHIVO, gson.toJson(registros, TIPO_LISTA), StandardCharsets.UTF_8);

		} catch (IOException e) {
			System.err.println("Error al escribir el archivo de libros -> " + e.getMessage());
			throw new NegocioException("No se pudo guardar el archivo de libros: " + RUTA_ARCHIVO.toAbsolutePath());
		}
	}

	/**
	 * Forma en que un libro se escribe en el JSON. Libro es abstracta, por eso se
	 * guarda su formato y al leer se reconstruye con {@link TipoLibro}.
	 */
	private static class RegistroLibro {

		private Long idLibro;
		private String tipoLibro;
		private String isbn;
		private String titulo;
		private List<String> autores;
		private String fechaPublicacion;
		private String genero;
		private String editorial;
		private int numeroPaginas;
		private double precioBase;
		private int stockDisponible;
		// Solo para libros físicos
		private String tiempoMaximoEntrega;

		RegistroLibro(Libro libro) {
			this.idLibro = libro.getIdLibro();
			this.tipoLibro = libro.getTipoLibro().name();
			this.isbn = libro.getIsbn();
			this.titulo = libro.getTitulo();
			this.autores = libro.getAutores() != null ? new ArrayList<>(libro.getAutores()) : new ArrayList<>();
			this.fechaPublicacion = libro.getFechaPublicacion();
			this.genero = libro.getGenero();
			this.editorial = libro.getEditorial();
			this.numeroPaginas = libro.getNumeroPaginas();
			this.precioBase = libro.getPrecioBase();
			this.stockDisponible = libro.getStockDisponible();
			if (libro instanceof LibroFisico fisico) {
				this.tiempoMaximoEntrega = fisico.getTiempoMaximoEntrega();
			}
		}

		Libro aLibro() {
			Libro libro = TipoLibro.valueOf(tipoLibro).crear(isbn, titulo,
					autores != null ? new ArrayList<>(autores) : new ArrayList<>(), fechaPublicacion, genero,
					editorial, numeroPaginas, precioBase, stockDisponible);

			libro.setIdLibro(idLibro);
			if (libro instanceof LibroFisico fisico && tiempoMaximoEntrega != null) {
				fisico.setTiempoMaximoEntrega(tiempoMaximoEntrega);
			}
			return libro;
		}
	}
}
