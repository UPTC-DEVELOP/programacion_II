package co.edu.uptc.libreria.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.libreria.interfaces.ICarritoPersistencia;
import co.edu.uptc.libreria.interfaces.IGestionLibro;
import co.edu.uptc.libreria.modelo.ItemCarrito;
import co.edu.uptc.libreria.modelo.Libro;

public class CarritoBD implements ICarritoPersistencia {

    private IGestionLibro persistenciaLibro;

    public CarritoBD(IGestionLibro persistenciaLibro) {
        this.persistenciaLibro = persistenciaLibro;
    }

    @Override
    public void guardar(List<ItemCarrito> items) {
        String sqlInsertar = "INSERT INTO carrito (codigo_libro, titulo, autor, cantidad, precio_unitario) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection()) {
            if (con == null) return;

            try (PreparedStatement psDelete = con.prepareStatement("DELETE FROM carrito")) {
                psDelete.executeUpdate();
            }

            try (PreparedStatement psInsert = con.prepareStatement(sqlInsertar)) {
                for (ItemCarrito item : items) {
                    Libro libro = item.getLibro();
                    psInsert.setInt(1, libro.getCodigo());
                    psInsert.setString(2, libro.getTitulo());
                    psInsert.setString(3, libro.getAutor());
                    psInsert.setInt(4, item.getCantidad());
                    psInsert.setDouble(5, item.getPrecioUnitario());
                    psInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar el carrito en BD: " + e.getMessage());
        }
    }

    @Override
    public List<ItemCarrito> cargar() {
        List<ItemCarrito> listaCargada = new ArrayList<>();
        String sqlcarga = "SELECT codigo_libro, titulo, autor, cantidad, precio_unitario FROM carrito";

        try (Connection con = ConexionBD.getConnection()) {
            if (con == null) return listaCargada;

            try (PreparedStatement ps = con.prepareStatement(sqlcarga);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int codigoLibro = rs.getInt("codigo_libro");
                    int cantidad = rs.getInt("cantidad");
                    double precioUnitario = rs.getDouble("precio_unitario");

            
                    Libro libro = buscarLibro(codigoLibro);

                    if (libro == null) {
                        libro = new Libro();
                        libro.setCodigo(codigoLibro);
                        libro.setTitulo(rs.getString("titulo"));
                        libro.setAutor(rs.getString("autor"));
                        libro.setPrecio(precioUnitario);
                    }

                    listaCargada.add(new ItemCarrito(libro, cantidad, precioUnitario));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar el carrito desde BD: " + e.getMessage());
        }
        return listaCargada;
    }
    
    public void actualizar(ItemCarrito item) {
        String sqlAct = "UPDATE carrito SET titulo = ?, autor = ?, cantidad = ?, precio_unitario = ? WHERE codigo_libro = ?";

        try (Connection con = ConexionBD.getConnection()) {
            if (con == null) return;

            try (PreparedStatement psAct = con.prepareStatement(sqlAct)) {
                Libro libro = item.getLibro();
                psAct.setString(1, libro.getTitulo());
                psAct.setString(2, libro.getAutor());
                psAct.setInt(3, item.getCantidad());
                psAct.setDouble(4, item.getPrecioUnitario());
                psAct.setInt(5, libro.getCodigo()); 
                psAct.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el carrito: " + e.getMessage());
        }
    }

    private Libro buscarLibro(int codigo) {
        for (Libro l : persistenciaLibro.listar()) {
            if (l.getCodigo() == codigo) {
                return l;
            }
        }
        return null;
    }
}