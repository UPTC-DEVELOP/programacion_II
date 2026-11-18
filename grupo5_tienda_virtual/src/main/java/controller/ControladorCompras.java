package controller;

import java.util.ArrayList;
import java.util.List;

import model.Compra;
import persistencia.IRepositorio;
import persistencia.PersistenciaException;
import service.IServicioCompras;

public class ControladorCompras implements IServicioCompras {

    private final IRepositorio<Compra> repositorio;

    public ControladorCompras(IRepositorio<Compra> repositorio) {
        this.repositorio = repositorio;
        this.repositorio.cargar();
    }

    @Override
    public boolean registrarCompra(Compra compra) {
        if (compra == null
                || compra.getCorreoCliente() == null
                || compra.getCorreoCliente().trim().isEmpty()
                || compra.getItems() == null
                || compra.getItems().isEmpty()
                || compra.getTotal() < 0) {
            return false;
        }
        try {
            repositorio.agregar(compra);
            repositorio.guardar();
            return true;
        } catch (PersistenciaException e) {
            compra.setId(0);
            revertir();
            return false;
        }
    }

    @Override
    public List<Compra> listarCompras() {
        return new ArrayList<Compra>(repositorio.listar());
    }

    @Override
    public List<Compra> listarPorCliente(String correo) {
        List<Compra> resultado = new ArrayList<Compra>();
        if (correo == null || correo.trim().isEmpty()) {
            return resultado;
        }
        for (Compra compra : repositorio.listar()) {
            if (correo.equalsIgnoreCase(compra.getCorreoCliente())) {
                resultado.add(compra);
            }
        }
        return resultado;
    }

    @Override
    public int cantidad() {
        return repositorio.listar().size();
    }

    private void revertir() {
        try {
            repositorio.cargar();
        } catch (PersistenciaException e) {
            System.err.println("No se pudo recargar el estado de compras: " + e.getMessage());
        }
    }
}
