package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.persistence.*;
import java.sql.Connection;

public class TiendaConfig {

    private final IGestionLibros gestionLibros;
    private final IGestionUsuarios gestionUsuarios;
    private final IGestionCompras gestionCompras;

    public TiendaConfig() {
        inicializarInfraestructura();

        IRepositorioLibros repoLibrosMySQL = new RepositorioLibrosMySQL();
        IRepositorioLibros repoLibrosJson = new RepositorioLibrosJson();

        IRepositorioUsuario repoUsuarioMySQL = new RepositorioUsuarioMySQL();
        IRepositorioUsuario repoUsuarioJson = new RepositorioUsuarioJson();

        IRepositorioCarrito repoCarritoMySQL = new RepositorioCarritoMySQL(repoLibrosMySQL);
        IRepositorioCarrito repoCarritoJson = new RepositorioCarritoJson();

        IRepositorioCompra repoCompraMySQL = new RepositorioCompraMySQL();
        IRepositorioCompra repoCompraJson = new RepositorioCompraJson();

        RegistradorOperacionesTxt registradorTxt = new RegistradorOperacionesTxt();

        this.gestionLibros = new GestionLibrosImpl(repoLibrosMySQL, repoLibrosJson, registradorTxt);
        this.gestionUsuarios = new GestionUsuariosImpl(repoUsuarioMySQL, repoUsuarioJson, repoCarritoJson, registradorTxt);
        this.gestionCompras = new GestionComprasImpl(
                repoCarritoMySQL, repoCarritoJson,
                repoCompraMySQL, repoCompraJson,
                gestionUsuarios, gestionLibros,
                registradorTxt
        );
    }

    private void inicializarInfraestructura() {
        try (Connection conn = ConexionMySQL.obtenerConexion()) {
            InicializadorBaseDatos.inicializarTablas(conn);
        } catch (Exception e) {
            System.err.println("Aviso: No se pudo inicializar MySQL al arrancar (operando con respaldo JSON): " + e.getMessage());
        }
    }

    public IGestionLibros getGestionLibros() { return gestionLibros; }
    public IGestionUsuarios getGestionUsuarios() { return gestionUsuarios; }
    public IGestionCompras getGestionCompras() { return gestionCompras; }
}