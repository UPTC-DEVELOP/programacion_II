// Principio D (SOLID): conecta los servicios de negocio con los repositorios MySQL y JSON.
package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.persistence.ConexionMySQL;
import co.edu.uptc.caso.estudio.persistence.IRepositorioCarrito;
import co.edu.uptc.caso.estudio.persistence.IRepositorioCompra;
import co.edu.uptc.caso.estudio.persistence.IRepositorioLibros;
import co.edu.uptc.caso.estudio.persistence.IRepositorioUsuario;
import co.edu.uptc.caso.estudio.persistence.InicializadorBaseDatos;
import co.edu.uptc.caso.estudio.persistence.RegistradorOperacionesTxt;
import co.edu.uptc.caso.estudio.persistence.RepositorioCarritoJson;
import co.edu.uptc.caso.estudio.persistence.RepositorioCarritoMySQL;
import co.edu.uptc.caso.estudio.persistence.RepositorioCompraJson;
import co.edu.uptc.caso.estudio.persistence.RepositorioCompraMySQL;
import co.edu.uptc.caso.estudio.persistence.RepositorioLibrosJson;
import co.edu.uptc.caso.estudio.persistence.RepositorioLibrosMySQL;
import co.edu.uptc.caso.estudio.persistence.RepositorioUsuarioJson;
import co.edu.uptc.caso.estudio.persistence.RepositorioUsuarioMySQL;
import java.sql.Connection;

public class TiendaConfig {

    private final IGestionLibros gestionLibros;
    private final IGestionUsuarios gestionUsuarios;
    private final IGestionCompras gestionCompras;

    public TiendaConfig() {
        inicializarInfraestructura();

        RegistradorOperacionesTxt registradorTxt = new RegistradorOperacionesTxt();

        IRepositorioLibros repoLibrosMySQL = new RepositorioLibrosMySQL();
        IRepositorioLibros repoLibrosJson = new RepositorioLibrosJson();

        IRepositorioUsuario repoUsuarioMySQL = new RepositorioUsuarioMySQL();
        IRepositorioUsuario repoUsuarioJson = new RepositorioUsuarioJson();

        IRepositorioCarrito repoCarritoMySQL = new RepositorioCarritoMySQL(repoLibrosMySQL);
        IRepositorioCarrito repoCarritoJson = new RepositorioCarritoJson();

        IRepositorioCompra repoCompraMySQL = new RepositorioCompraMySQL();
        IRepositorioCompra repoCompraJson = new RepositorioCompraJson();

        this.gestionLibros = crearGestionLibros(repoLibrosMySQL, repoLibrosJson, registradorTxt);
        this.gestionUsuarios = crearGestionUsuarios(
                repoUsuarioMySQL,
                repoUsuarioJson,
                repoCarritoJson,
                registradorTxt);
        this.gestionCompras = crearGestionCompras(
                repoCarritoMySQL,
                repoCarritoJson,
                repoCompraMySQL,
                repoCompraJson,
                this.gestionUsuarios,
                this.gestionLibros,
                registradorTxt);
    }

    // Intenta inicializar tablas en MySQL al arrancar; si falla, el sistema opera con JSON
    private void inicializarInfraestructura() {
        try (Connection conn = ConexionMySQL.obtenerConexion()) {
            InicializadorBaseDatos.inicializarTablas(conn);
        } catch (Exception e) {
            System.err.println("Aviso: No se pudo inicializar MySQL al arrancar (operando con respaldo JSON): "
                    + e.getMessage());
        }
    }

    // Construye el servicio de negocio de libros con sus repositorios y registrador
    private IGestionLibros crearGestionLibros(IRepositorioLibros repoLibrosMySQL,
                                              IRepositorioLibros repoLibrosJson,
                                              RegistradorOperacionesTxt registradorTxt) {
        return new GestionLibrosImpl(repoLibrosMySQL, repoLibrosJson, registradorTxt);
    }

    // Construye el servicio de negocio de usuarios con persistencia y limpieza de carrito JSON
    private IGestionUsuarios crearGestionUsuarios(IRepositorioUsuario repoUsuarioMySQL,
                                                IRepositorioUsuario repoUsuarioJson,
                                                IRepositorioCarrito repoCarritoJson,
                                                RegistradorOperacionesTxt registradorTxt) {
        return new GestionUsuariosImpl(repoUsuarioMySQL, repoUsuarioJson, repoCarritoJson, registradorTxt);
    }

    // Construye el servicio de negocio de compras integrando carrito, compra, usuarios y libros
    private IGestionCompras crearGestionCompras(IRepositorioCarrito repoCarritoMySQL,
                                              IRepositorioCarrito repoCarritoJson,
                                              IRepositorioCompra repoCompraMySQL,
                                              IRepositorioCompra repoCompraJson,
                                              IGestionUsuarios usuarios,
                                              IGestionLibros libros,
                                              RegistradorOperacionesTxt registradorTxt) {
        return new GestionComprasImpl(
                repoCarritoMySQL,
                repoCarritoJson,
                repoCompraMySQL,
                repoCompraJson,
                usuarios,
                libros,
                registradorTxt);
    }

    public IGestionLibros getGestionLibros() {
        return gestionLibros;
    }

    public IGestionUsuarios getGestionUsuarios() {
        return gestionUsuarios;
    }

    public IGestionCompras getGestionCompras() {
        return gestionCompras;
    }
}