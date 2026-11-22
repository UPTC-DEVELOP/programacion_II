package co.edu.uptc.negocio;

import co.edu.uptc.persistence.*;

public class TiendaConfig {
    private final IGestionLibros gestionLibros;
    private final IGestionUsuarios gestionUsuarios;
    private final IGestionCompras gestionCompras;

    public TiendaConfig() {
        IRepositorioLibros repoLibrosMySQL = new RepositorioLibrosMySQL();
        IRepositorioLibros repoLibrosJson = new RepositorioLibrosJson();
        IRepositorioUsuario repoUsuarioMySQL = new RepositorioUsuarioMySQL();
        IRepositorioUsuario repoUsuarioJson = new RepositorioUsuarioJson();
        IRepositorioCarrito repoCarritoMySQL = new RepositorioCarritoMySQL();
        IRepositorioCarrito repoCarritoJson = new RepositorioCarritoJson();
        IRepositorioCompra repoCompraMySQL = new RepositorioCompraMySQL();

        this.gestionLibros = new GestionLibrosImpl(repoLibrosMySQL, repoLibrosJson);
        GestionUsuariosImpl usuariosImpl = new GestionUsuariosImpl(repoUsuarioMySQL, repoUsuarioJson, gestionLibros);
        this.gestionUsuarios = usuariosImpl;
        this.gestionCompras = new GestionComprasImpl(repoCarritoMySQL, repoCarritoJson, repoCompraMySQL, gestionUsuarios, gestionLibros);
        usuariosImpl.setGestionCompras(this.gestionCompras);
    }

    public IGestionLibros getGestionLibros() { return gestionLibros; }
    public IGestionUsuarios getGestionUsuarios() { return gestionUsuarios; }
    public IGestionCompras getGestionCompras() { return gestionCompras; }
}
