package co.edu.uptc.caso.estudio.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

// Crea las tablas requeridas por la tienda virtual si aún no existen en MySQL
public class InicializadorBaseDatos {

    private InicializadorBaseDatos() {
    }

    // Ejecuta las sentencias DDL de creación de tablas de forma idempotente
    public static void inicializarTablas(Connection conn) throws SQLException {
        if (conn == null) {
            return;
        }

        String sqlLibros = "CREATE TABLE IF NOT EXISTS libros ("
                + "isbn VARCHAR(30) PRIMARY KEY,"
                + "titulo VARCHAR(200) NOT NULL,"
                + "autor VARCHAR(150) NOT NULL,"
                + "anio INT NOT NULL,"
                + "categoria VARCHAR(80),"
                + "editorial VARCHAR(100),"
                + "paginas INT NOT NULL,"
                + "precio DOUBLE NOT NULL,"
                + "stock INT NOT NULL,"
                + "formato VARCHAR(30) NOT NULL,"
                + "porcentaje_iva DOUBLE NOT NULL)";

        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id VARCHAR(30) PRIMARY KEY,"
                + "nombre_completo VARCHAR(150) NOT NULL,"
                + "correo VARCHAR(120) NOT NULL UNIQUE,"
                + "direccion_envio VARCHAR(200),"
                + "departamento VARCHAR(80),"
                + "ciudad VARCHAR(80),"
                + "telefono VARCHAR(30),"
                + "tipo_cliente VARCHAR(30) NOT NULL,"
                + "porcentaje_descuento DOUBLE NOT NULL,"
                + "contrasena_hash VARCHAR(255) NOT NULL)";

        String sqlCarrito = "CREATE TABLE IF NOT EXISTS carrito_items ("
                + "cliente_id VARCHAR(30) NOT NULL,"
                + "libro_isbn VARCHAR(30) NOT NULL,"
                + "cantidad INT NOT NULL,"
                + "PRIMARY KEY (cliente_id, libro_isbn),"
                + "FOREIGN KEY (cliente_id) REFERENCES usuarios(id) ON DELETE CASCADE,"
                + "FOREIGN KEY (libro_isbn) REFERENCES libros(isbn) ON DELETE CASCADE)";

        String sqlCompras = "CREATE TABLE IF NOT EXISTS compras ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "cliente_id VARCHAR(30) NOT NULL,"
                + "fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                + "total DOUBLE NOT NULL,"
                + "FOREIGN KEY (cliente_id) REFERENCES usuarios(id))";

        String sqlCompraItems = "CREATE TABLE IF NOT EXISTS compra_items ("
                + "compra_id INT NOT NULL,"
                + "libro_isbn VARCHAR(30) NOT NULL,"
                + "cantidad INT NOT NULL,"
                + "precio_unitario DOUBLE NOT NULL,"
                + "PRIMARY KEY (compra_id, libro_isbn),"
                + "FOREIGN KEY (compra_id) REFERENCES compras(id),"
                + "FOREIGN KEY (libro_isbn) REFERENCES libros(isbn))";

        try (Statement st = conn.createStatement()) {
            st.execute(sqlLibros);
            st.execute(sqlUsuarios);
            st.execute(sqlCarrito);
            st.execute(sqlCompras);
            st.execute(sqlCompraItems);
        }
    }
}