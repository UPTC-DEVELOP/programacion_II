package controller;

import java.util.List;

import model.Compra;
import model.ItemCompra;
import persistencia.IRepositorio;
import service.IConsultaVentas;

public class ConsultaVentas implements IConsultaVentas {

    private static final int TAMANO_PAGINA = 100;

    private final IRepositorio<Compra> repositorio;

    public ConsultaVentas(IRepositorio<Compra> repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public boolean tieneVentas(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return false;
        }
        int total = repositorio.contar();
        for (int inicio = 0; inicio < total; inicio += TAMANO_PAGINA) {
            List<Compra> pagina = repositorio.listarPagina(inicio, TAMANO_PAGINA);
            for (Compra compra : pagina) {
                if (contieneLibro(compra, isbn)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean contieneLibro(Compra compra, String isbn) {
        if (compra == null || compra.getItems() == null) {
            return false;
        }
        for (ItemCompra item : compra.getItems()) {
            if (item != null && item.getLibro() != null
                    && isbn.equalsIgnoreCase(item.getLibro().getIsbn())) {
                return true;
            }
        }
        return false;
    }
}
