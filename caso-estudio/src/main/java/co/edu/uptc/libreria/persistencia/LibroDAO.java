package co.edu.uptc.libreria.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.libreria.interfaces.IGestionLibro;
import co.edu.uptc.libreria.modelo.Libro;

public class LibroDAO implements IGestionLibro{

	@Override
	public void guardar(Libro libro) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO libros (codigo, titulo, autor, editorial, anio_publicacion, genero, precio, stock) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection con = ConexionBD.getConnection()) {
            if (con == null) return;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, libro.getCodigo());
                ps.setString(2, libro.getTitulo());
                ps.setString(3, libro.getAutor());
                ps.setString(4, libro.getEditorial());
                ps.setString(5, libro.getAnioPublicacion());
                ps.setString(6, libro.getGenero());
                ps.setDouble(7, libro.getPrecio());
                ps.setString(8, libro.getStock());
                
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar: " + e.getMessage());
            return;
        }
	}

    public List<Libro> listar() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT* FROM libros";

        try (Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    libros.add(Construirlibros(rs));
                        
                }
        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
        }
        return libros;
    }

    
    
    private Libro Construirlibros(ResultSet rs){
		// TODO Auto-generated method stub
    	try {
    		Libro libro = new Libro();
    		libro.setCodigo(rs.getInt("codigo"));
    		libro.setTitulo(rs.getString("titulo"));
    		libro.setAutor(rs.getString("autor"));
    		libro.setEditorial(rs.getString("editorial"));
    		libro.setAnioPublicacion(rs.getString("anio_publicacion"));
    		libro.setGenero(rs.getString("genero"));
    		libro.setPrecio(rs.getDouble("precio"));
    		libro.setStock(rs.getString("stock"));
    		
    		return libro;
    	} catch (SQLException e) {
    		System.err.println("Error al construir libro" + e.getMessage());
    		return null;
    	}
		
	}
    
    @Override
	public void  actualizar(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, precio = ? WHERE codigo = ?";

        try (Connection con = ConexionBD.getConnection()) {
            if (con == null) return;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, libro.getTitulo());
                ps.setString(2, libro.getAutor());
                ps.setDouble(3, libro.getPrecio());
                ps.setInt(4, libro.getCodigo());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar: " + e.getMessage());
            return;
        }
    }


	@Override
	public void eliminar(String codigo) {
		// TODO Auto-generated method stub
		
		String sql = "DELETE FROM libros WHERE codigo = ?";

        try (Connection con = ConexionBD.getConnection()) {
            if (con == null) return;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1,Integer.parseInt(codigo));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            return;
        }
		
	}
}