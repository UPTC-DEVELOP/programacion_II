package co.edu.uptc.tienda.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.edu.uptc.tienda.conexion.Conexion;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.modelo.enums.TipoLibro;
import co.edu.uptc.tienda.negocio.IRepositorioLibro;

/**
 * Repositorio de libros en la Base de Datos MySQL (tabla libros).
 */
public class BaseDeDatosLibro implements IRepositorioLibro {

	private static final String SEPARADOR_AUTORES = ", ";

	@Override
	public void guardar(Libro libro) {
		Conexion conn = new Conexion();
		conn.conexion();

		String sentenciaSQL = "INSERT INTO libros VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = conn.getConnection().prepareStatement(sentenciaSQL)) {

			pstmt.setInt(1, 0);
			pstmt.setString(2, libro.getIsbn());
			pstmt.setString(3, libro.getTitulo());
			pstmt.setString(4, String.join(SEPARADOR_AUTORES, libro.getAutores()));
			pstmt.setString(5, libro.getFechaPublicacion());
			pstmt.setString(6, libro.getGenero());
			pstmt.setString(7, libro.getEditorial());
			pstmt.setInt(8, libro.getNumeroPaginas());
			pstmt.setDouble(9, libro.getPrecioBase());
			pstmt.setInt(10, libro.getStockDisponible());
			pstmt.setString(11, libro.getTipoLibro().name());
			pstmt.setDouble(12, Libro.IVA);
			pstmt.setDouble(13, libro.getPrecioFinal());

			pstmt.executeUpdate();

		} catch (Exception e) {
			System.err.println("Error con la Base de Datos -> " + e.getMessage());
		} finally {
			conn.desconectar();
		}
	}

	@Override
	public List<Libro> listar() {
		List<Libro> libros = new ArrayList<>();
		Conexion conn = new Conexion();
		conn.conexion();

		String sentenciaSQL = "SELECT * FROM libros";

		try (PreparedStatement pstmt = conn.getConnection().prepareStatement(sentenciaSQL);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				libros.add(mapearLibro(rs));
			}

			pstmt.close();
			conn.desconectar();

			conn = null;

		} catch (Exception e) {
			System.err.println("Error al obtener los valores de la Base de Datos -> " + e.getMessage());
		} finally {
			conn.desconectar();
		}

		return libros;
	}

	@Override
	public Libro buscarPorIsbn(String isbn) {
		return buscarPorColumna("isbn", isbn);
	}

	@Override
	public Libro buscarPorTitulo(String titulo) {
		return buscarPorColumna("titulo", titulo);
	}

	// TODO: completar la sentencia UPDATE (identificar el registro por isbn)
	@Override
	public boolean actualizar(Libro libro) {
		return false;
	}

	// TODO: implementar DELETE FROM libros WHERE isbn = ?
	@Override
	public boolean eliminar(String isbn) {
		return false;
	}

	// La columna nunca proviene del usuario, solo de las constantes de esta clase
	private Libro buscarPorColumna(String columna, String valor) {
		Libro libro = null;
		Conexion conn = new Conexion();
		conn.conexion();

		String sentenciaSQL = "SELECT * FROM libros WHERE " + columna + " = ?";

		try (PreparedStatement pstmt = conn.getConnection().prepareStatement(sentenciaSQL)) {

			pstmt.setString(1, valor);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					libro = mapearLibro(rs);
				}
			}

			pstmt.close();
			conn.desconectar();

			conn = null;

		} catch (Exception e) {
			System.err.println("Error al consultar los datos -> " + e.getMessage());
		} finally {
			conn.desconectar();
		}

		return libro;
	}

	private Libro mapearLibro(ResultSet rs) throws SQLException {
		TipoLibro tipoLibro = TipoLibro.valueOf(rs.getString("tipo_libro"));

<<<<<<< HEAD
		String sentenciaSQL = "UPDATE libros  SET titulo = ?, autores = ?,  fecha_publicacion = ?, genero = ?, editorial = ?, numero_paginas = ?, precio_base = ?, stock_disponible = ?, tipo_libro = ?, precio_final = ? WHERE id_libro = ?";

		try (PreparedStatement pstmt = conn.getConnection().prepareStatement(sentenciaSQL)) {

			pstmt.setString(1, libro.getTitulo());

			List<String> listaAutores = libro.getAutores();
			String autoresUnidos = String.join(", ", listaAutores);

			pstmt.setString(2, autoresUnidos);
			pstmt.setString(3, libro.getFechaPublicacion());
			pstmt.setString(4, libro.getGenero());
			pstmt.setString(5, libro.getEditorial());
			pstmt.setInt(6, libro.getNumeroPaginas());
			pstmt.setDouble(7, libro.getPrecioBase());
			pstmt.setInt(8, libro.getStockDisponible());
			pstmt.setString(9, libro.getTipoLibro().name());
			pstmt.setDouble(10, libro.getPrecioFinal());
			pstmt.setLong(11, libro.getIdLibro());

			pstmt.executeUpdate();
			pstmt.close();
			conn.desconectar();

			conn = null;

		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage() + "\nCódigo: " + e.getErrorCode());
			return false;
		}

		return true;
	}
=======
		List<String> listaAutores = new ArrayList<>();
		String autoresTexto = rs.getString("autores");
		if (autoresTexto != null && !autoresTexto.isEmpty()) {
			listaAutores = new ArrayList<>(Arrays.asList(autoresTexto.split(SEPARADOR_AUTORES)));
		}
>>>>>>> 6b1e013dcf11c77de612949100c05583cb4efa93

		Libro libro = tipoLibro.crear(rs.getString("isbn"), rs.getString("titulo"), listaAutores,
				rs.getString("fecha_publicacion"), rs.getString("genero"), rs.getString("editorial"),
				rs.getInt("numero_paginas"), rs.getDouble("precio_Base"), rs.getInt("stock_disponible"));
		libro.setIdLibro(rs.getLong("id_libro"));

		return libro;
	}

}
