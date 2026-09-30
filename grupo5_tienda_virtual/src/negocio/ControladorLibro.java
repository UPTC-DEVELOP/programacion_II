package negocio;

import java.util.ArrayList;
import java.util.List;

/// CONTROLADOR DE NEGOCIO Y OPERACIONES EN MEMORIA ///

public class ControladorLibro {

    //  ATRIBUTO QUE ALMACENA LA COLECCION DE LIBROS EN MEMORIA  //
	
    private List libros;

    //  INICIO DE LA LISTA DE LIBROS  //
    
    public ControladorLibro() {
        this.libros = new ArrayList();
    }

    //   REGISTRO DE UN NUEVO LIBRO (RF01)  //
    
    public boolean registrar(Libro libro) {
           	
        if (libro == null || libro.getIsbn() == null || buscar(libro.getIsbn()) != null) {
            return false;
        }
        return libros.add(libro);
    }

    //  LISTADO COMPLETO DE LIBROS (RF04) // 
    
    public List listar() {
        return libros;
    }

    //   BUSCAR UN LIBRO POR SU CODIGO ISBN (RF09)  //
    
    public Libro buscar(String isbn) {
        if (isbn == null) return null;
        for (Object obj : libros) {
            Libro libro = (Libro) obj;
            if (isbn.equalsIgnoreCase(libro.getIsbn())) {
                return libro;
            }
        }
        return null;
    }

    //  BUSCAR LIBROS POR  COINCIDENCIA EN ISBN, EL TITULO O AUTOR (RF09)  //
    
    public List buscarPorCriterio(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return libros;
        }
        List resultados = new ArrayList();
        String filtro = criterio.toLowerCase();
        for (Object obj : libros) {
            Libro libro = (Libro) obj;
            boolean coincideIsbn = libro.getIsbn() != null && libro.getIsbn().toLowerCase().contains(filtro);
            boolean coincideTitulo = libro.getTitulo() != null && libro.getTitulo().toLowerCase().contains(filtro);
            boolean coincideAutor = libro.getAutor() != null && libro.getAutor().toLowerCase().contains(filtro);

            if (coincideIsbn || coincideTitulo || coincideAutor) {
                resultados.add(libro);
            }
        }
        return resultados;
    }

    //  ACTUALIZAR LOS DATOS DE UN LIBRO EXISTENTE (RF02)  //
    
    public boolean actualizar(String isbn, Libro datos) {
        Libro actual = buscar(isbn);
        if (actual == null || datos == null) return false;
        actual.setTitulo(datos.getTitulo());
        actual.setAutor(datos.getAutor());
        actual.setAnioPublicacion(datos.getAnioPublicacion());
        actual.setCategoria(datos.getCategoria());
        actual.setEditorial(datos.getEditorial());
        actual.setNumeroPaginas(datos.getNumeroPaginas());
        actual.setPrecio(datos.getPrecio());
        actual.setPorcentajeIva(datos.getPorcentajeIva());
        actual.setCantidadDisponible(datos.getCantidadDisponible());
        actual.setFormato(datos.getFormato());
        return true;
    }

    //   ELIMINAR UN LIBRO DE LA LISTA POR ISBN (RF03)  //
    
    public boolean eliminar(String isbn) {
        Libro actual = buscar(isbn);
        if (actual == null) return false;
        return libros.remove(actual);
    }

    //   VALIDAR LA DISPONIBILIDAD DE STOCK (RF17)  //
    
    public boolean validarDisponibilidad(String isbn, int cantidadSolicitada) {
       
        Libro libro = buscar(isbn);
        if (libro != null) {
            return libro.getCantidadDisponible() >= cantidadSolicitada;
        }
        return false;
    }

    //  ACTUALIZAR EL INVENTARIO TRAS UNA VENTA (RF18)  //
    
    public boolean actualizarInventario(String isbn, int cantidadVendida) {
            Libro libro = buscar(isbn);
        if (libro != null && libro.getCantidadDisponible() >= cantidadVendida) {
            int nuevoStock = libro.getCantidadDisponible() - cantidadVendida;
            libro.setCantidadDisponible(nuevoStock);
            return true;
        }
        return false;
    }
}