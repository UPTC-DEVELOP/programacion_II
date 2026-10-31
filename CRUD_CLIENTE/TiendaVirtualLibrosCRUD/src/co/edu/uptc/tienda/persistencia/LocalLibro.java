package co.edu.uptc.tienda.persistencia;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import co.edu.uptc.tienda.conexion.Conexion;
import co.edu.uptc.tienda.interfaces.IGestionLibro;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.modelo.Libros;

public class LocalLibro implements IGestionLibro {

	private Libros libros;
	private long siguienteIdLibro;

	public LocalLibro() {
		this.libros = new Libros();
		this.siguienteIdLibro = 1L;
	}

	@Override
	public void agregar(Libro libro) {

		Conexion conn = new Conexion();
		conn.conexion();

		String sentenciaSQL = "INSERT INTO libros VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = conn.getConnection().prepareStatement(sentenciaSQL)) {

			libro.setIdLibro(this.siguienteIdLibro);
			this.siguienteIdLibro++;

			pstmt.setLong(1, libro.getIdLibro());
			pstmt.setString(2, libro.getIsbn());
			pstmt.setString(3, libro.getTitulo());

			String autoresTexto = String.join(", ", libro.getAutores());
			pstmt.setString(4, autoresTexto);

			pstmt.setString(5, libro.getFechaPublicacion());
			pstmt.setString(6, libro.getGenero());
			pstmt.setString(7, libro.getEditorial());
			pstmt.setInt(8, libro.getNumeroPaginas());
			pstmt.setDouble(9, libro.getPrecioFinal());
			pstmt.setInt(10, libro.getStockDisponible());
			pstmt.setString(11, libro.getTipoLibro().name());

			pstmt.executeUpdate();

			pstmt.close();
			conn.desconectar();

		} catch (SQLException e) {
			System.err.println("Error en el guardado en la base de Datos: \n");
			e.printStackTrace();
		}

		libros.agregarLibro(libro);
	}

	@Override
	public List<Libro> listar() {
		return libros.listarLibros();
	}

	@Override
	public Libro buscar(String titulo) {
		return libros.buscarLibro(titulo);
	}

	@Override
	public boolean actualizar(Libro libroActualizar) {
		return libros.actualizarLibro(libroActualizar);
	}

	@Override
	public boolean eliminar(String titulo) {
		return libros.eliminarLibro(titulo);
	}
}
