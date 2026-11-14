package config;

import model.Cliente;
import model.Compra;
import model.Libro;
import persistencia.IRepositorio;
import persistencia.RepositorioClientesJson;
import persistencia.RepositorioComprasJson;
import persistencia.RepositorioLibrosJson;

public final class ProjectConfig {

    private ProjectConfig() {
    }

    public static IRepositorio<Cliente> clientes() {
        return new RepositorioClientesJson();
    }

    public static IRepositorio<Libro> libros() {
        return new RepositorioLibrosJson();
    }

    public static IRepositorio<Compra> compras() {
        return new RepositorioComprasJson();
    }
}
