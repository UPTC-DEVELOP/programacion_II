package controller;

import java.util.ArrayList;
import java.util.List;

import model.Compra;
import persistencia.IRepositorio;
import service.IServicioCompras;

/**
 * Logica de negocio de compras.
 *
 * Implementa la interfaz IServicioCompras (la que usan las ventanas) y delega
 * el almacenamiento en un IRepositorio<Compra> que se inyecta por constructor:
 * el controlador NO sabe si las compras viven en JSON o en una base de datos
 * (SOLID: Dependency Inversion + Single Responsibility).
 */
public class ControladorCompras implements IServicioCompras {

    // Repositorio donde se guardan las compras (JSON o BD, lo decide el Main)
    private final IRepositorio<Compra> repositorio;

    // Recibe el repositorio por constructor y carga las compras ya guardadas
    public ControladorCompras(IRepositorio<Compra> repositorio) {
        this.repositorio = repositorio;
        this.repositorio.cargar();
    }

    // REGISTRAR UNA COMPRA
    
    @Override
    public boolean registrarCompra(Compra compra) {
        // No se registra una compra nula o sin items
        if (compra == null || compra.getItems() == null || compra.getItems().isEmpty()) {
            return false;
        }
        repositorio.agregar(compra);
        repositorio.guardar();
        return true;
    }

    // LISTAR EL HISTORIAL DE COMPRAS
    
    @Override
    public List<Compra> listarCompras() {
        return new ArrayList<Compra>(repositorio.listar());
    }
}