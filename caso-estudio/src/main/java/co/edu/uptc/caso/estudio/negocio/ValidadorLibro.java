package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Libro;
import java.time.Year;

// Valida los campos obligatorios, rangos y unicidad de ISBN de un libro
public class ValidadorLibro {

    private static final int ANIO_MINIMO = 1450;
    private static final double IVA_MINIMO = 0.0;
    private static final double IVA_MAXIMO = 100.0;

    private final GestionLibrosImpl gestionLibros;

    public ValidadorLibro(GestionLibrosImpl gestionLibros) {
        this.gestionLibros = gestionLibros;
    }

    public String validar(Libro l, boolean esEdicion) {
        if (l == null) {
            return "El libro no puede ser nulo.";
        }
        if (esVacio(l.getIsbn())) {
            return "El ISBN es obligatorio.";
        }
        if (esVacio(l.getTitulo())) {
            return "El título no puede estar vacío.";
        }
        if (esVacio(l.getAutor())) {
            return "El autor no puede estar vacío.";
        }
        if (esVacio(l.getCategoria())) {
            return "Debe seleccionar una categoría.";
        }
        if (l.getFormato() == null) {
            return "Debe seleccionar un formato.";
        }

        int anioActual = Year.now().getValue();
        if (l.getAnio() < ANIO_MINIMO || l.getAnio() > anioActual) {
            return "Año inválido (entre " + ANIO_MINIMO + " y " + anioActual + ").";
        }
        if (l.getPaginas() <= 0) {
            return "El número de páginas debe ser mayor a 0.";
        }
        if (l.getPrecioVenta() <= 0) {
            return "El precio de venta debe ser mayor a $0.";
        }
        if (l.getStock() < 0) {
            return "El stock no puede ser negativo.";
        }
        if (l.getPorcentajeIva() < IVA_MINIMO || l.getPorcentajeIva() > IVA_MAXIMO) {
            return "El IVA debe estar entre 0% y 100%.";
        }

        if (!esEdicion && gestionLibros != null && gestionLibros.buscarLibroPorIsbn(l.getIsbn()) != null) {
            return "Ya existe un libro con el ISBN: " + l.getIsbn();
        }
        return null;
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}