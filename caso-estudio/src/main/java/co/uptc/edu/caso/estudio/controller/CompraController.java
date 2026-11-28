package co.uptc.edu.caso.estudio.controller;

import java.util.List;
import co.uptc.edu.caso.estudio.negocio.Compra;
import co.uptc.edu.caso.estudio.service.CompraService;

public class CompraController {

    private CompraService service;

    public CompraController(CompraService service) {
        this.service = service;
    }

    public String registrarCompra(Compra compra, String metodoPago) {
        return service.registrarCompra(compra, metodoPago);
    }

    public List<Compra> listar() {
        return service.listar();
    }

    public List<Compra> listarJson() {
        return service.listarJson();
    }

    public Compra buscarPorId(int idCompra) {
        return service.buscarPorId(idCompra);
    }

    public boolean actualizarEstado(int idCompra, String nuevoEstado) {
        return service.actualizarEstado(idCompra, nuevoEstado);
    }

    public boolean eliminar(int idCompra) {
        return service.eliminar(idCompra);
    }
}