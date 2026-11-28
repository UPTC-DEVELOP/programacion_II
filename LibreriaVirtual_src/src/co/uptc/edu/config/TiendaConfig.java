package co.uptc.edu.config;

import co.uptc.edu.controlador.CarritoControlador;
import co.uptc.edu.controlador.LibroControlador;
import co.uptc.edu.controlador.SesionControlador;
import co.uptc.edu.controlador.UsuarioControlador;
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

// Raíz de composición: elige la persistencia, crea el modelo y entrega los controladores a la vista
public class TiendaConfig {

    private final Tienda tienda;
    private final SesionControlador sesionControlador;
    private final UsuarioControlador usuarioControlador;
    private final LibroControlador libroControlador;
    private final CarritoControlador carritoControlador;

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
        sesionControlador = new SesionControlador(tienda);
        usuarioControlador = new UsuarioControlador(usuarios.getGestionUsuarios(), sesionControlador);
        libroControlador = new LibroControlador(tienda, sesionControlador);
        carritoControlador = new CarritoControlador(tienda, sesionControlador);
    }

    public Tienda getTienda() {
        return tienda;
    }

    public SesionControlador getSesionControlador() {
        return sesionControlador;
    }

    public UsuarioControlador getUsuarioControlador() {
        return usuarioControlador;
    }

    public LibroControlador getLibroControlador() {
        return libroControlador;
    }

    public CarritoControlador getCarritoControlador() {
        return carritoControlador;
    }
}
