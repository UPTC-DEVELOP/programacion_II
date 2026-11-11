package com.tiendavirtual.controlador;

import java.util.List;

import com.tiendavirtual.modelo.Venta;
import com.tiendavirtual.negocio.VentaService;

public class VentaController {

    private VentaService servicio;

    public VentaController(VentaService servicio) {
        this.servicio = servicio;
    }

    public void registrar(Venta venta) {
        servicio.registrar(venta);
    }

    public Venta consultar(String id) {
        return servicio.consultar(id);
    }

    public void actualizar(Venta venta) {
        servicio.actualizar(venta);
    }

    public void eliminar(String id) {
        servicio.eliminar(id);
    }

    public List<Venta> listar() {
        return servicio.listar();
    }
}