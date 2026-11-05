package co.edu.uptc.tienda.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.edu.uptc.tienda.conexion.Conexion;
import co.edu.uptc.tienda.interfaces.IGestionLibro;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.modelo.LibroFisico;
import co.edu.uptc.tienda.modelo.enums.TipoLibro;

public class BaseDeDatosLibro implements IGestionLibro {

	@Override
	public void agregar(Libro libro) {
		Conexion conn = new Conexion();
		conn.conexion();

		String sentenciaSQL = "INSERT INTO libros VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = conn.getConnection().prepareStatement(sentenciaSQL)) {

			pstmt.setInt(1, 0);
			pstmt.setString(2, libro.getIsbn());
			pstmt.setString(3, libro.getTitulo());

			List<String> listaAutores = libro.getAutores();
			String autoresUnidos = String.join(", ", listaAutores);

			pstmt.setString(4, autoresUnidos);
			pstmt.setString(5, libro.getFechaPublicacion());
			pstmt.setString(6, libro.getGenero());
			pstmt.setString(7, libro.getEditorial());
			pstmt.setInt(8, libro.getNumeroPaginas());
			pstmt.setDouble(9, libro.getPrecioBase());
			pstmt.setInt(10, libro.getStockDisponible());
			pstmt.setString(11, libro.getTipoLibro().name());
			pstmt.setDouble(12, libro.getIVA());
			pstmt.setDouble(13, libro.getPrecioFinal());

			pstmt.executeUpdate();
			pstmt.close();
			conn.desconectar();

			conn = null;

		} catch (Exception e) {
			System.out.println("Error con la Base de Datos");
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

				if (rs.getString("tipo_libro").equals("FISICO")) {
					Libro libro = new LibroFisico();

					libro.setIdLibro(rs.getInt("id_libro"));
					libro.setIsbn(rs.getString("isbn"));
					libro.setTitulo(rs.getString("titulo"));

					String autoresTexto = rs.getString("autores");

					List<String> listaAutores = new ArrayList<>();
					if (autoresTexto != null && !autoresTexto.isEmpty()) {

						listaAutores = new ArrayList<>(Arrays.asList(autoresTexto.split(", ")));
					}

					libro.setAutores(listaAutores);
					libro.setFechaPublicacion(rs.getString("fecha_publicacion"));
					libro.setGenero(rs.getString("genero"));
					libro.setEditorial(rs.getString("editorial"));
					libro.setNumeroPaginas(rs.getInt("numero_paginas"));
					libro.setPrecioBase(rs.getDouble("precio_Base"));
					libro.setStockDisponible(rs.getInt("stock_disponible"));
					libro.setTipoLibro(TipoLibro.FISICO);
					libro.setPrecioFinal(rs.getDouble("precio_final"));

					libros.add(libro);

				} else if (rs.getString("tipo_libro").equals("DIGITAL")) {
					Libro libro = new LibroFisico();

					libro.setIdLibro(rs.getInt("id_libro"));
					libro.setIsbn(rs.getString("isbn"));
					libro.setTitulo(rs.getString("titulo"));

					String autoresTexto = rs.getString("autores");

					List<String> listaAutores = new ArrayList<>();
					if (autoresTexto != null && !autoresTexto.isEmpty()) {

						listaAutores = new ArrayList<>(Arrays.asList(autoresTexto.split(", ")));
					}

					libro.setAutores(listaAutores);
					libro.setFechaPublicacion(rs.getString("fecha_publicacion"));
					libro.setGenero(rs.getString("genero"));
					libro.setEditorial(rs.getString("editorial"));
					libro.setNumeroPaginas(rs.getInt("numero_paginas"));
					libro.setPrecioBase(rs.getDouble("precio_Base"));
					libro.setStockDisponible(rs.getInt("stock_disponible"));
					libro.setTipoLibro(TipoLibro.DIGITAL);
					libro.setPrecioFinal(rs.getDouble("precio_final"));

					libros.add(libro);
				}

			}

		} catch (Exception e) {
			System.err.println("Error al obtener los valores de la Base de Datos");
		}

		return libros;
	}

	@Override
	public Libro buscar(String titulo) {
		Libro libro = null;

		Conexion conn = new Conexion();
		conn.conexion();

		String sentenciaSQL = "SELECT * FROM libros WHERE titulo = ?";

		try (PreparedStatement pstmt = conn.getConnection().prepareStatement(sentenciaSQL)) {

			pstmt.setString(1, titulo);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				if (rs.getString("tipo_libro").equals("FISICO")) {
					libro = new LibroFisico();

					libro.setIdLibro(rs.getInt("id_libro"));
					libro.setIsbn(rs.getString("isbn"));
					libro.setTitulo(rs.getString("titulo"));

					String autoresTexto = rs.getString("autores");

					List<String> listaAutores = new ArrayList<>();
					if (autoresTexto != null && !autoresTexto.isEmpty()) {

						listaAutores = new ArrayList<>(Arrays.asList(autoresTexto.split(", ")));
					}

					libro.setAutores(listaAutores);
					libro.setFechaPublicacion(rs.getString("fecha_publicacion"));
					libro.setGenero(rs.getString("genero"));
					libro.setEditorial(rs.getString("editorial"));
					libro.setNumeroPaginas(rs.getInt("numero_paginas"));
					libro.setPrecioBase(rs.getDouble("precio_Base"));
					libro.setStockDisponible(rs.getInt("stock_disponible"));
					libro.setTipoLibro(TipoLibro.FISICO);
					libro.setPrecioFinal(rs.getDouble("precio_final"));

				} else if (rs.getString("tipo_libro").equals("DIGITAL")) {
					libro = new LibroFisico();

					libro.setIdLibro(rs.getInt("id_libro"));
					libro.setIsbn(rs.getString("isbn"));
					libro.setTitulo(rs.getString("titulo"));

					String autoresTexto = rs.getString("autores");

					List<String> listaAutores = new ArrayList<>();
					if (autoresTexto != null && !autoresTexto.isEmpty()) {

						listaAutores = new ArrayList<>(Arrays.asList(autoresTexto.split(", ")));
					}

					libro.setAutores(listaAutores);
					libro.setFechaPublicacion(rs.getString("fecha_publicacion"));
					libro.setGenero(rs.getString("genero"));
					libro.setEditorial(rs.getString("editorial"));
					libro.setNumeroPaginas(rs.getInt("numero_paginas"));
					libro.setPrecioBase(rs.getDouble("precio_Base"));
					libro.setStockDisponible(rs.getInt("stock_disponible"));
					libro.setTipoLibro(TipoLibro.DIGITAL);
					libro.setPrecioFinal(rs.getDouble("precio_final"));

				}

			}

		} catch (Exception e) {
			System.out.println("Error al consultar los datos");
		}

		return libro;
	}

	@Override
	public boolean actualizar(Libro libro) {
		Conexion conn = new Conexion();
		conn.conexion();

		String sentenciaSQL = "UPDATE libros  SET titulo = ?, autores = ?,  fecha_publicacion = ?, genero = ?, editorial = ?, numero_paginas = ?, precio_base = ?, stock_disponible = ?, tipo_libro = ?, precio_final = ? WHERE id_libro = ?";

		try (PreparedStatement pstmt = conn.getConnection().prepareStatement(sentenciaSQL)){
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return false;
	}

	@Override
	public boolean eliminar(String titulo) {
		// TODO Auto-generated method stub
		return false;
	}

}
