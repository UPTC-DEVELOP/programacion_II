package config;

import model.Cliente;
import model.Compra;
import model.Libro;
import persistencia.IRepositorio;
import persistencia.RepositorioClientesBD;
import persistencia.RepositorioClientesJson;
import persistencia.RepositorioComprasJson;
import persistencia.RepositorioLibrosJson;

public final class ProjectConfig {

    private ProjectConfig() {
    }

    public static IRepositorio<Cliente> clientes() {
        return clientesJson();
    }

    public static IRepositorio<Cliente> clientesJson() {
        return new RepositorioClientesJson();
    }

    public static IRepositorio<Cliente> clientesBD() {
        return new RepositorioClientesBD();
    }

    public static IRepositorio<Libro> libros() {
        return new RepositorioLibrosJson();
    }

    public static IRepositorio<Compra> compras() {
        return new RepositorioComprasJson();
    }
}
