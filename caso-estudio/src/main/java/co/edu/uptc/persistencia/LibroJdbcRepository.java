package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.interfaces.IVentaRepository;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.dto.FiltroLibroDto;
import co.edu.uptc.modelo.enums.Categoria;
import co.edu.uptc.modelo.enums.Formato;
import co.edu.uptc.persistencia.conexion.ConexionDB;
/**
 * IMPLEMENTA ILibroRepositorio Y GESTIONA LA PERSISTENCIA DE LIBROS EN POSTGRESQL
 *  @author: Equipo de desarrollo grupo 7 UPTC
 * 
*/
/*modificacion:Brayan Panqueva*/
public class LibroJdbcRepository implements ILibroRepositorio {

    private final IVentaRepository ventas;

    public LibroJdbcRepository() {
        this(null);
    }

    public LibroJdbcRepository(IVentaRepository ventas) {
        this.ventas = ventas;
    }
    private static final String SELECT = "SELECT isbn,titulo,autores,anio_publicacion,categoria,editorial,numero_paginas,precio_venta,porcentaje_iva,cantidad_inventario,formato FROM books";

    @Override
    public void guardar(Libro l) {
        String q = "INSERT INTO books(isbn,titulo,autores,anio_publicacion,categoria,editorial,numero_paginas,precio_venta,porcentaje_iva,cantidad_inventario,formato) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement(q)) {
            bindAll(p, l, 1);
            p.executeUpdate();
        } catch (SQLException e) {
            throw error(e, "registrar el libro");
        }
    }

    @Override
    public void actualizar(Libro l) {
        String q = "UPDATE books SET titulo=?,autores=?,anio_publicacion=?,categoria=?,editorial=?,numero_paginas=?,precio_venta=?,porcentaje_iva=?,cantidad_inventario=?,formato=? WHERE isbn=?";
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement(q)) {
            int i = 1;
            i = bindData(p, l, i);
            p.setString(i, l.getIsbn());
            p.executeUpdate();
        } catch (SQLException e) {
            throw error(e, "actualizar el libro");
        }
    }

    private int bindAll(PreparedStatement p, Libro l, int i) throws SQLException {
        p.setString(i++, l.getIsbn());
        return bindData(p, l, i);
    }

    private int bindData(PreparedStatement p, Libro l, int i) throws SQLException {
        p.setString(i++, l.getTitulo());
        p.setString(i++, String.join(", ", l.getAutores() == null ? List.of() : l.getAutores()));
        p.setInt(i++, l.getAnioPublicacion());
        p.setString(i++, l.getCategoria() == null ? null : l.getCategoria().name());
        p.setString(i++, l.getEditorial());
        p.setInt(i++, l.getNumPaginas());
        p.setDouble(i++, l.getPrecioVenta());
        p.setInt(i++, l.getFormato() == Formato.DIGITAL ? 19 : 5);
        p.setInt(i++, l.getStock());
        p.setString(i++, l.getFormato() == null ? null : l.getFormato().getEtiqueta());
        return i;
    }

    @Override
    public void eliminar(String isbn) {
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement("DELETE FROM books WHERE isbn=?")) {
            p.setString(1, isbn);
            p.executeUpdate();
        } catch (SQLException e) {
            throw error(e, "eliminar el libro");
        }
    }

    @Override
    public boolean existePorIsbn(String isbn) {
        return consultarPorIsbn(isbn) != null;
    }

    @Override
    public Libro consultarPorIsbn(String isbn) {
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement(SELECT + " WHERE isbn=?")) {
            p.setString(1, isbn);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        } catch (SQLException e) {
            throw error(e, "consultar el libro");
        }
    }

    @Override
    public List<Libro> listarTodos() {
        return listar(SELECT + " ORDER BY lower(titulo)");
    }

    @Override
    public List<Libro> listarConFiltro(FiltroLibroDto f) {
        if (f == null) {
            return listarTodos();
        
        }return listarTodos().stream().filter(l -> coincide(l, f)).collect(Collectors.toList());
    }

    private List<Libro> listar(String sql) {
        List<Libro> out = new ArrayList<>();
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement(sql); ResultSet r = p.executeQuery()) {
            while (r.next()) {
                out.add(map(r));
            
            }return out;
        } catch (SQLException e) {
            throw error(e, "consultar los libros");
        }
    }

    private boolean coincide(Libro l, FiltroLibroDto f) {
        String t = f.getTitulo(), a = f.getAutor();
        return (t == null || t.isBlank() || l.getTitulo().toLowerCase(Locale.ROOT).contains(t.trim().toLowerCase(Locale.ROOT))) && (a == null || a.isBlank() || l.getAutores().stream().anyMatch(x -> x.toLowerCase(Locale.ROOT).contains(a.trim().toLowerCase(Locale.ROOT)))) && (f.getCategoria() == null || f.getCategoria() == l.getCategoria()) && (f.getPrecioMin() == null || l.getPrecioVenta() >= f.getPrecioMin()) && (f.getPrecioMax() == null || l.getPrecioVenta() <= f.getPrecioMax());
    }

    @Override
    public boolean tieneVentas(String isbn) {
        if (ventas != null) {
            return ventas.listarTodas().stream().anyMatch(v -> v.getIsbns() != null && v.getIsbns().contains(isbn));
        
        }try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement("SELECT EXISTS(SELECT 1 FROM sale_detais WHERE isbn_libro=?)")) {
            p.setString(1, isbn);
            try (ResultSet r = p.executeQuery()) {
                r.next();
                return r.getBoolean(1);
            }
        } catch (SQLException e) {
            throw error(e, "verificar ventas del libro");
        }
    }

    private Libro map(ResultSet r) throws SQLException {
        Libro l = new Libro();
        l.setIsbn(r.getString("isbn"));
        l.setTitulo(r.getString("titulo"));
        l.setAutores(new ArrayList<>());
        String a = r.getString("autores");
        if (a != null) {
            for (String x : a.split(",")) {
                if (!x.isBlank()) {
                    l.getAutores().add(x.trim());
        
                }
            }
        }l.setAnioPublicacion(r.getInt("anio_publicacion"));
        l.setCategoria(categoria(r.getString("categoria")));
        l.setEditorial(r.getString("editorial"));
        l.setNumPaginas(r.getInt("numero_paginas"));
        l.setPrecioVenta(r.getDouble("precio_venta"));
        l.setStock(r.getInt("cantidad_inventario"));
        l.setFormato(formato(r.getString("formato")));
        return l;
    }

    private Categoria categoria(String v) {
        if (v == null) {
            return null;
        
        }for (Categoria c : Categoria.values()) {
            if (c.name().equalsIgnoreCase(v) || c.getEtiqueta().equalsIgnoreCase(v)) {
                return c;
        
            }
        }return Categoria.OTROS;
    }

    private Formato formato(String v) {
        if (v == null) {
            return null;
        
        }for (Formato f : Formato.values()) {
            if (f.name().equalsIgnoreCase(v) || f.getEtiqueta().equalsIgnoreCase(v)) {
                return f;
        
            }
        }return null;
    }

    private PersistenciaException error(SQLException e, String op) {
        return new PersistenciaException("No se pudo " + op + " en PostgreSQL: " + e.getMessage(), e);
    }
}
