
package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioCategoria;
import co.edu.uptc.tiendalibros.modelo.Categoria;
import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;

public class RepositorioCategoriaJDBC implements IRepositorioCategoria {

    private final ConexionBaseDatos conexionBaseDatos;

    public RepositorioCategoriaJDBC(ConexionBaseDatos conexionBaseDatos) {
        this.conexionBaseDatos = conexionBaseDatos;
    }

    @Override
    public Categoria guardar(Categoria categoria) {

        if (categoria == null) {
            return null;
        }

        String sqlInsertar = "INSERT INTO categoria "
                + "(nombre, descripcion, codigo) VALUES (?, ?, ?)";

        String sqlActualizar = "UPDATE categoria SET "
                + "nombre = ?, descripcion = ?, codigo = ? "
                + "WHERE id_categoria = ?";

        try (Connection conexion = conexionBaseDatos.abrir()) {

            if (categoria.getId() == null) {

                try (PreparedStatement comando = conexion.prepareStatement(
                        sqlInsertar, Statement.RETURN_GENERATED_KEYS)) {

                    comando.setString(1, categoria.getNombre());
                    comando.setString(2, categoria.getDescripcion());
                    comando.setString(3, categoria.getCodigo());

                    comando.executeUpdate();

                    try (ResultSet claves = comando.getGeneratedKeys()) {
                        if (claves.next()) {
                            return new Categoria(
                                    claves.getLong(1),
                                    categoria.getNombre(),
                                    categoria.getDescripcion(),
                                    categoria.getCodigo()
                            );
                        }
                    }
                }

            } else {

                try (PreparedStatement comando = conexion.prepareStatement(sqlActualizar)) {

                    comando.setString(1, categoria.getNombre());
                    comando.setString(2, categoria.getDescripcion());
                    comando.setString(3, categoria.getCodigo());
                    comando.setLong(4, categoria.getId());

                    int filas = comando.executeUpdate();

                    if (filas == 0) {
                        throw new IllegalArgumentException(
                                "No se encontró la categoría con ID: " + categoria.getId()
                        );
                    }

                    return categoria;
                }
            }

        } catch (SQLException excepcion) {
            throw new ErrorTecnicoSistemaException(
                    "Error al guardar la categoría en la base de datos.", excepcion
            );
        }

        return null;
    }

    @Override
    public boolean eliminar(Long id) {

        if (id == null) {
            return false;
        }

        String sql = "DELETE FROM categoria WHERE id_categoria = ?";

        try (Connection conexion = conexionBaseDatos.abrir();
             PreparedStatement comando = conexion.prepareStatement(sql)) {

            comando.setLong(1, id);

            return comando.executeUpdate() > 0;

        } catch (SQLException excepcion) {
            throw new ErrorTecnicoSistemaException(
                    "Error al eliminar la categoría.", excepcion
            );
        }
    }

    @Override
    public List<Categoria> buscarPorId(Long id) {

        List<Categoria> categorias = new ArrayList<>();

        if (id == null) {
            return categorias;
        }

        String sql = "SELECT id_categoria, nombre, descripcion, codigo "
                + "FROM categoria WHERE id_categoria = ?";

        try (Connection conexion = conexionBaseDatos.abrir();
             PreparedStatement comando = conexion.prepareStatement(sql)) {

            comando.setLong(1, id);

            try (ResultSet resultado = comando.executeQuery()) {

                while (resultado.next()) {
                    categorias.add(convertirFila(resultado));
                }
            }

        } catch (SQLException excepcion) {
            throw new ErrorTecnicoSistemaException(
                    "Error al buscar la categoría.", excepcion
            );
        }

        return categorias;
    }

    @Override
    public List<Categoria> listar() {

        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT id_categoria, nombre, descripcion, codigo "
                + "FROM categoria ORDER BY id_categoria";

        try (Connection conexion = conexionBaseDatos.abrir();
             PreparedStatement comando = conexion.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                categorias.add(convertirFila(resultado));
            }

        } catch (SQLException excepcion) {
            throw new ErrorTecnicoSistemaException(
                    "Error al listar las categorías.", excepcion
            );
        }

        return categorias;
    }

    private Categoria convertirFila(ResultSet fila) throws SQLException {

        return new Categoria(
                fila.getLong("id_categoria"),
                fila.getString("nombre"),
                fila.getString("descripcion"),
                fila.getString("codigo")
        );
    }
}
