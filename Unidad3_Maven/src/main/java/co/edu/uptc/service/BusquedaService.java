package co.edu.uptc.service;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.model.Libro;

// BusquedaService mantiene la búsqueda fuera de los paneles Swing.
public class BusquedaService {
    private final CatalogoService catalogo;
    public BusquedaService(CatalogoService catalogo) { this.catalogo = catalogo; }
    public List<Libro> buscarPorTexto(String texto) {
        List<Libro> resultado = new ArrayList<>(); String criterio = texto == null ? "" : texto.toLowerCase();
        for (Libro libro : catalogo.listar()) {
			if (libro.getTitulo().toLowerCase().contains(criterio) || libro.getIsbn().contains(criterio)) {
				resultado.add(libro);
			}
		}
        return resultado;
    }
}
