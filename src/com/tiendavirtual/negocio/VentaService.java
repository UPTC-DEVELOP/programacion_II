package com.tiendavirtual.negocio;

import java.util.List;

import com.tiendavirtual.modelo.Venta;
import com.tiendavirtual.repositorio.LibroRepository;
import com.tiendavirtual.repositorio.VentaRepository;

public class VentaService {

    private VentaRepository repositorio;
    private LibroRepository libros;

    public VentaService(VentaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public VentaService(VentaRepository repositorio, LibroRepository libros) {
        this.repositorio = repositorio;
        this.libros = libros;
    }

    private void validarLibro(Venta venta) {
        String isbn = venta.getIsbn();

        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "El ISBN del libro es obligatorio.");
        }

        if (libros != null && libros.buscarPorIsbn(isbn.trim()) == null) {
            throw new IllegalArgumentException(
                "No existe un libro con ese ISBN.");
        }
    }

    public void registrar(Venta venta) {

        if (venta == null || venta.getId() == null
                || venta.getId().trim().isEmpty()) {
            throw new IllegalArgumentException(
                "La venta no es válida.");
        }

        if (venta.getMontoTotal() <= 0) {
            throw new IllegalArgumentException(
                "El monto debe ser mayor que cero.");
        }

        if (repositorio.buscarPorId(venta.getId()) != null) {
            throw new IllegalArgumentException(
                "El ID de venta ya existe.");
        }

        validarLibro(venta);
        repositorio.guardarVenta(venta);
    }

    public Venta consultar(String id) {
        return repositorio.buscarPorId(id);
    }

    public List<Venta> listar() {
        return repositorio.obtenerTodas();
    }

    public void eliminar(String id) {

        if (repositorio.buscarPorId(id) == null) {
            throw new IllegalArgumentException(
                "La venta no existe.");
        }

        repositorio.eliminarVenta(id);
    }

    public void actualizar(Venta venta) {

        if (venta == null || venta.getId() == null
                || venta.getId().trim().isEmpty()) {
            throw new IllegalArgumentException(
                "La venta no es válida.");
        }

        if (repositorio.buscarPorId(venta.getId()) == null) {
            throw new IllegalArgumentException(
                "La venta no existe.");
        }

        if (venta.getMontoTotal() <= 0) {
            throw new IllegalArgumentException(
                "El monto debe ser mayor que cero.");
        }

        validarLibro(venta);
        repositorio.actualizarVenta(venta);
    }
}