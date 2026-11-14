package co.uptc.edu.config;

import co.uptc.edu.interfaces.IGestionCarrito;
import co.uptc.edu.interfaces.IGestionCompra;
import co.uptc.edu.interfaces.IGestionLibro;
import co.uptc.edu.model.Tienda;
import co.uptc.edu.persistencia.ConexionBD;
import co.uptc.edu.persistencia.JdbcCarrito;
import co.uptc.edu.persistencia.JdbcCompra;
import co.uptc.edu.persistencia.JdbcLibro;
import co.uptc.edu.persistencia.LocalCarrito;
import co.uptc.edu.persistencia.LocalCompra;
import co.uptc.edu.persistencia.LocalLibro;

public class TiendaConfig {

    private final Tienda tienda;

    public TiendaConfig() {
        UsuarioConfig usuarios = new UsuarioConfig();
        IGestionLibro iLibro = new LocalLibro();
        IGestionCompra iCompra = new LocalCompra();
        IGestionCarrito iCarrito = new LocalCarrito();
        if (ConexionBD.disponible()) {
            try {
                JdbcLibro jdbcLibro = new JdbcLibro();
                jdbcLibro.listar();
                iLibro = jdbcLibro;
                iCompra = new JdbcCompra();
                iCarrito = new JdbcCarrito();
            } catch (IllegalStateException e) {
                System.err.println(e.getMessage() + " Verifique que existan las tablas (sql/libreria_virtual.sql).");
            }
        }
        tienda = new Tienda(usuarios.getGestionUsuarios(), iLibro, iCompra, iCarrito);
    }

    public Tienda getTienda() {
        return tienda;
    }
}
