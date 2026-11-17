
package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioCategoria;
import co.edu.uptc.tiendalibros.modelo.Categoria;

public class RepositorioCategoriaJDBC extends RepositorioJDBCBase<Categoria> implements IRepositorioCategoria {

    public RepositorioCategoriaJDBC(ConexionBaseDatos conexionBaseDatos) {
      super(conexionBaseDatos);
    }
    
    @Override
    protected PreparedStatement crearComandoSQLInsertar(Connection conexion, Categoria categoria)
      throws SQLException {
      
      String sqlInsertar = "INSERT INTO categoria "
        + "(nombre, descripcion, codigo) VALUES (?, ?, ?)";
      
      PreparedStatement comando = conexion
        .prepareStatement(sqlInsertar, Statement.RETURN_GENERATED_KEYS);
      
      comando.setString(1, categoria.getNombre());
      comando.setString(2, categoria.getDescripcion());
      comando.setString(3, categoria.getCodigo());
      
      return comando;
    }
    
    @Override
    protected PreparedStatement crearComandoSQLActualizar(Connection conexion, Categoria categoria)
      throws SQLException {
      
      String sqlActualizar = "UPDATE categoria SET "
        + "nombre = ?, descripcion = ?, codigo = ? "
        + "WHERE id_categoria = ?";
      
      PreparedStatement comando = conexion.prepareStatement(sqlActualizar);
      
      comando.setString(1, categoria.getNombre());
      comando.setString(2, categoria.getDescripcion());
      comando.setString(3, categoria.getCodigo());
      comando.setLong(4, categoria.getId());
      
      return comando;
    }
    
    @Override
    protected PreparedStatement crearComandoSQLEliminar(Connection conexion, Long id) throws SQLException {
      String sql = "DELETE FROM categoria WHERE id_categoria = ?";
      
      PreparedStatement comando = conexion.prepareStatement(sql);
      comando.setLong(1, id);
      
      return comando;
    }
    
    @Override
    protected PreparedStatement crearComandoSQLBuscarPorId(Connection conexion, Long id) throws SQLException {
      String sql = "SELECT id_categoria, nombre, descripcion, codigo FROM categoria WHERE id_categoria = ?";
      PreparedStatement comando = conexion.prepareStatement(sql);
      comando.setLong(1, id);
      
      return comando;
    }

    @Override
    protected PreparedStatement crearComandoSQLListar(Connection conexion) throws SQLException {
      String sql = "SELECT id_categoria, nombre, descripcion, codigo "
        + "FROM categoria ORDER BY id_categoria";
      return conexion.prepareStatement(sql);
    }
    
    @Override
    protected Categoria asignarId(Categoria categoria, Long id) {
      return new Categoria(
        id,
        categoria.getNombre(),
        categoria.getDescripcion(),
        categoria.getCodigo()
      );
    }
    
    @Override
    protected Categoria convertirFila(ResultSet fila) throws SQLException {
      return new Categoria(
        fila.getLong("id_categoria"),
        fila.getString("nombre"),
        fila.getString("descripcion"),
        fila.getString("codigo")
      );
    }
    
}
