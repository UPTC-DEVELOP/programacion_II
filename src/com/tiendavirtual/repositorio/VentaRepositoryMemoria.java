package com.tiendavirtual.repositorio;

import java.util.ArrayList;
import java.util.List;

import com.tiendavirtual.modelo.Venta;

public class VentaRepositoryMemoria implements VentaRepository {

    private List<Venta> ventas = new ArrayList<>();

    @Override
    public void guardarVenta(Venta venta) {
        ventas.add(venta);
    }

    @Override
    public Venta buscarPorId(String id) {
        for (Venta venta : ventas) {
            if (venta.getId().equals(id)) {
                return venta;
            }
        }
        return null;
    }

    @Override
    public List<Venta> obtenerTodas() {
        return new ArrayList<>(ventas);
    }

    @Override
    public void eliminarVenta(String id) {
        ventas.removeIf(venta -> venta.getId().equals(id));
    }
}