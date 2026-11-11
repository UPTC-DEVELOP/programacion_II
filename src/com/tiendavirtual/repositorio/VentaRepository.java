package com.tiendavirtual.repositorio;

import java.util.List;
import com.tiendavirtual.modelo.Venta;

public interface VentaRepository {

    void guardarVenta(Venta venta);

    Venta buscarPorId(String id);

    List<Venta> obtenerTodas();

    void eliminarVenta(String id);
}