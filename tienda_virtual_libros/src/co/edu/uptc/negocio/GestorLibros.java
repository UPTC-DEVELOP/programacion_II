package co.edu.uptc.negocio;

import co.edu.uptc.negocio.modelo.Categoria;
import co.edu.uptc.negocio.modelo.Formato;
import co.edu.uptc.negocio.modelo.Libro;
import co.edu.uptc.negocio.validacion.ValidadorDatos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase responsable de gestionar el catálogo de libros (CRUD).
 * 
 * Principios aplicados:
 * - Single Responsibility: Solo gestiona libros, no clientes ni compras
 * - Encapsulamiento: La lista de libros es privada, acceso controlado
 * - Separation of Concerns: La validación está en ValidadorDatos, no aquí
 * 
 * Esta clase trabaja en memoria (ArrayList). En etapas posteriores
 * se conectará con la capa de persistencia (JSON/BD).
 * 
 * @author Oscar Clavijo
 * @version 1.0 - Septiembre 2026
 */
public class GestorLibros {

    // Lista que almacena todos los libros del catálogo
    private List<Libro> catalogo;

    /**
     * Constructor que inicializa el catálogo vacío.
     * En producción, aquí se cargaria los datos desde persistencia.
     */
    public GestorLibros() {
        this.catalogo = new ArrayList<>();
    }

    //  RF01: Registrar Libro 

    /**
     * Registra un nuevo libro en el catálogo.
     * 
     * @param isbn            ISBN único del libro
     * @param titulo          Título del libro
     * @param autores         Lista de autores
     * @param anioPublicacion Año de publicación
     * @param categoria       Categoría/género
     * @param editorial       Nombre de la editorial
     * @param numPaginas      Número de páginas
     * @param precioVenta     Precio con IVA incluido
     * @param stock           Cantidad en inventario
     * @param formato         Físico o Digital
     * @return Mensaje de éxito o error
     * 
     * Flujo:
     * 1. Validar todos los campos (delega a ValidadorDatos)
     * 2. Verificar que el ISBN no exista ya
     * 3. Crear el objeto Libro
     * 4. Agregarlo al catálogo
     */
    
    public String registrarLibro(String isbn, String titulo, List<String> autores,
                                  int anioPublicacion, Categoria categoria, String editorial,
                                  int numPaginas, double precioVenta, int stock, Formato formato) {
        
        // Paso 1: Validar campos
        String errorValidacion = ValidadorDatos.validarLibroCompleto(
            isbn, titulo, autores, anioPublicacion, categoria, 
            editorial, numPaginas, precioVenta, stock, formato
        );
        
        if (errorValidacion != null) {
            return "Error de validación: " + errorValidacion;
        }

        // Paso 2: Verificar unicidad del ISBN
        if (existeLibro(isbn)) {
            return "Error: Ya existe un libro con el ISBN " + isbn;
        }

        // Paso 3: Crear y agregar el libro
        Libro nuevoLibro = new Libro(isbn, titulo, autores, anioPublicacion,
                                      categoria, editorial, numPaginas, 
                                      precioVenta, stock, formato);
        
        catalogo.add(nuevoLibro);
        
        return "Libro registrado exitosamente: " + titulo;
    }

    //  RF02: Actualizar Libro 

    /**
     * Actualiza la información de un libro existente.
     * 
     * @param isbn           ISBN del libro a actualizar (no se puede cambiar)
     * @param nuevoTitulo    Nuevo título (null para no cambiar)
     * @param nuevoPrecio    Nuevo precio (null para no cambiar)
     * @param nuevoStock     Nuevo stock (null para no cambiar)
     * @param nuevaEditorial Nueva editorial (null para no cambiar)
     * @return Mensaje de éxito o error
     * 
     * Nota: El ISBN no se puede modificar porque es la clave primaria.
     */
    public String actualizarLibro(String isbn, String nuevoTitulo, 
                                   Double nuevoPrecio, Integer nuevoStock,
                                   String nuevaEditorial) {
        
        // Buscar el libro
        Libro libro = buscarPorISBN(isbn);
        
        if (libro == null) {
            return "Error: No existe un libro con el ISBN " + isbn;
        }

        // Validar nuevos valores si se proporcionan
        if (nuevoPrecio != null && !ValidadorDatos.validarPrecioVenta(nuevoPrecio)) {
            return "Error: El precio debe ser mayor a 0 y menor o igual a 999,999.99";
        }
        
        if (nuevoStock != null && !ValidadorDatos.validarStock(nuevoStock)) {
            return "Error: El stock no puede ser negativo";
        }

        // Actualizar el libro
        libro.actualizar(nuevoTitulo, nuevoPrecio, nuevoStock, nuevaEditorial);
        
        return "Libro actualizado exitosamente: " + libro.getTitulo();
    }

    //  RF03: Eliminar Libro 

    /**
     * Elimina un libro del catálogo.
     * 
     * @param isbn ISBN del libro a eliminar
     * @return Mensaje de éxito o error
     * 
     * Restricción: Solo se puede eliminar si no tiene ventas asociadas.
     * Esta validación requiere acceso al módulo de compras, que se implementará
     * en etapas posteriores. Por ahora, solo verificamos que exista.
     */
    public String eliminarLibro(String isbn) {
        
        Libro libro = buscarPorISBN(isbn);
        
        if (libro == null) {
            return "Error: No existe un libro con el ISBN " + isbn;
        }

        // TODO: Validar que no tenga ventas asociadas (requiere integración con módulo de compras)
        // Por ahora, se permite la eliminación
        
        catalogo.remove(libro);
        
        return "Libro eliminado exitosamente: " + libro.getTitulo();
    }

    //  RF04: Listar Catálogo

    /**
     * Lista todos los libros del catálogo, ordenados alfabéticamente por título.
     * 
     * @return Lista de libros ordenada
     */
    public List<Libro> listarTodosLosLibros() {
        // Crear copia para no exponer la lista interna
        List<Libro> copia = new ArrayList<>(catalogo);
        
        // Ordenar alfabéticamente por título
        Collections.sort(copia, Comparator.comparing(Libro::getTitulo));
        
        return copia;
    }

    /**
     * Lista libros con filtros opcionales.
     * 
     * @param categoria     Filtrar por categoría (null para no filtrar)
     * @param autor         Filtrar por autor (búsqueda parcial, null para no filtrar)
     * @param titulo        Filtrar por título (búsqueda parcial, null para no filtrar)
     * @param precioMin     Precio mínimo (null para no filtrar)
     * @param precioMax     Precio máximo (null para no filtrar)
     * @return Lista de libros que cumplen los filtros
     */
    public List<Libro> listarLibrosConFiltros(Categoria categoria, String autor,
                                               String titulo, Double precioMin,
                                               Double precioMax) {
        
        return catalogo.stream()
            // Filtro por categoría
            .filter(libro -> categoria == null || libro.getCategoria() == categoria)
            // Filtro por autor (búsqueda parcial en la lista de autores)
            .filter(libro -> autor == null || autor.isEmpty() || 
                    libro.getAutores().stream()
                         .anyMatch(a -> a.toLowerCase().contains(autor.toLowerCase())))
            // Filtro por título (búsqueda parcial)
            .filter(libro -> titulo == null || titulo.isEmpty() || 
                    libro.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
            // Filtro por rango de precio
            .filter(libro -> precioMin == null || libro.getPrecioVenta() >= precioMin)
            .filter(libro -> precioMax == null || libro.getPrecioVenta() <= precioMax)
            // Ordenar alfabéticamente
            .sorted(Comparator.comparing(Libro::getTitulo))
            .collect(Collectors.toList());
    }

    //  Métodos Auxiliares 

    /**
     * Busca un libro por su ISBN.
     * 
     * @param isbn ISBN a buscar
     * @return El libro encontrado, o null si no existe
     */
    public Libro buscarPorISBN(String isbn) {
        return catalogo.stream()
            .filter(libro -> libro.getIsbn().equals(isbn))
            .findFirst()
            .orElse(null);
    }

    /**
     * Verifica si existe un libro con el ISBN dado.
     * 
     * @param isbn ISBN a verificar
     * @return true si existe, false si no
     */
    public boolean existeLibro(String isbn) {
        return buscarPorISBN(isbn) != null;
    }

    /**
     * Obtiene el total de libros en el catálogo.
     * Útil para el Dashboard del administrador.
     */
    
    public int getTotalLibros() {
        return catalogo.size();
    }

    /**
     * Obtiene el total de unidades en inventario.
     * Útil para el Dashboard del administrador.
     */
    
    public int getTotalUnidadesInventario() {
        return catalogo.stream()
            .mapToInt(Libro::getStock)
            .sum();
    }

    /**
     * Obtiene el valor total del inventario (precio × stock de cada libro).
     * Útil para reportes financieros.
     */
    
    public double getValorTotalInventario() {
        return catalogo.stream()
            .mapToDouble(libro -> libro.getPrecioVenta() * libro.getStock())
            .sum();
    }
}