package co.edu.uptc.negocio.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entidad de dominio que representa un libro en el catálogo de la tienda.
 * 
 * Principios aplicados:
 * - Encapsulamiento: todos los atributos son privados, acceso vía getters/setters
 * - Invariante de clase: el ISBN nunca es null, el stock nunca es negativo
 * - Responsabilidad única: esta clase SOLO representa el libro, no lo persiste
 *   ni lo valida 
 * 
 * @author Oscar Clavijo
 * @version 1.0 - Septiembre 2026
 */

public class Libro {

	//Atributos
	
	 	private String isbn;
	    private String titulo;
	    private List<String> autores;       // Lista porque un libro puede tener varios autores
	    private int anioPublicacion;
	    private Categoria categoria;
	    private String editorial;
	    private int numPaginas;
	    private double precioVenta;         // Precio con IVA incluido
	    private int stock;
	    private Formato formato;
	    private double promedioCalificacion;
	    private int totalResenas;
	    
	 //Constructor Principal
	    /**
	     * Constructor completo. Se usa cuando se registra un libro nuevo (RF01).
	     * 
	     * @param isbn            Código ISBN-13 único
	     * @param titulo          Título del libro
	     * @param autores         Lista de nombres de autores
	     * @param anioPublicacion Año de publicación (1401..año actual)
	     * @param categoria       Género/categoría del libro
	     * @param editorial       Nombre de la editorial
	     * @param numPaginas      Número de páginas
	     * @param precioVenta     Precio con IVA incluido
	     * @param stock           Cantidad en inventario
	     * @param formato         Físico o Digital
	     */
	  public Libro(String isbn, String titulo, List<String> autores,
                int anioPublicacion, Categoria categoria, String editorial,
                int numPaginas, double precioVenta, int stock, Formato formato) {
       this.isbn = isbn;
       this.titulo = titulo;
       // Copia defensiva: evita que modifiquen la lista externa
       this.autores = new ArrayList<>(autores);
       this.anioPublicacion = anioPublicacion;
       this.categoria = categoria;
       this.editorial = editorial;
       this.numPaginas = numPaginas;
       this.precioVenta = precioVenta;
       this.stock = stock;
       this.formato = formato;
       this.promedioCalificacion = 0.0;
       this.totalResenas = 0;
   }
	  
	  //Constructor vacio ( nesto servira para la persistencia)
	  public Libro() {
	        this.autores = new ArrayList<>();
	    }
	  
	  //Getters y setters
	  
	  public String getIsbn() {
	        return isbn;
	    }

	    /**
	     * El ISBN NO debe  modificarse después de creado.
	     * Es la clave primaria del libro (RF02: "Restricción de edición para el ISBN").
	     * Por eso NO existe un setter para ISBN.
	     */

	    public String getTitulo() {
	        return titulo;
	    }

	    public void setTitulo(String titulo) {
	        this.titulo = titulo;
	    }

	    /**
	     * Devuelve una copia inmodificable de la lista de autores.
	     * Esto previene que código externo modifique la lista interna
	     * sin pasar por el método adecuado (principio de encapsulamiento).
	     */
	    public List<String> getAutores() {
	        return Collections.unmodifiableList(autores);
	    }

	    public void setAutores(List<String> autores) {
	        this.autores = new ArrayList<>(autores);
	    }

	    public int getAnioPublicacion() {
	        return anioPublicacion;
	    }

	    public void setAnioPublicacion(int anioPublicacion) {
	        this.anioPublicacion = anioPublicacion;
	    }

	    public Categoria getCategoria() {
	        return categoria;
	    }

	    public void setCategoria(Categoria categoria) {
	        this.categoria = categoria;
	    }

	    public String getEditorial() {
	        return editorial;
	    }

	    public void setEditorial(String editorial) {
	        this.editorial = editorial;
	    }

	    public int getNumPaginas() {
	        return numPaginas;
	    }

	    public void setNumPaginas(int numPaginas) {
	        this.numPaginas = numPaginas;
	    }

	    public double getPrecioVenta() {
	        return precioVenta;
	    }

	    public void setPrecioVenta(double precioVenta) {
	        this.precioVenta = precioVenta;
	    }

	    public int getStock() {
	        return stock;
	    }

	    public void setStock(int stock) {
	        this.stock = stock;
	    }

	    public Formato getFormato() {
	        return formato;
	    }

	    public void setFormato(Formato formato) {
	        this.formato = formato;
	    }

	    public double getPromedioCalificacion() {
	        return promedioCalificacion;
	    }

	    public int getTotalResenas() {
	        return totalResenas;
	    }
	    
	    
	    //Metodos de comportamiento 
	    /**
	     * Reduce el stock del libro cuando se realiza una venta.
	     * 
	     * @param cantidad unidades a descontar
	     * @return true si se pudo disminuir, false si no hay stock suficiente
	     * 
	     * Este método encapsula la regla de negocio:
	     * "Si un cliente intenta comprar más unidades de las disponibles,
	     *  el sistema debe mostrar un mensaje de error."
	     */
	    public boolean disminuirStock(int cantidad) {
	        if (cantidad <= 0) {
	            return false;
	        }
	        if (this.stock < cantidad) {
	            return false; // No hay suficiente stock
	        }
	        this.stock -= cantidad;
	        return true;
	    }
	    
	    /**
	     * Actualiza los datos del libro con nuevos valores (RF02).
	     * Se usa cuando el administrador edita un libro existente.
	     * 
	     * @param nuevoTitulo      nuevo título (puede ser null para no cambiar)
	     * @param nuevoPrecio      nuevo precio (puede ser null para no cambiar)
	     * @param nuevoStock       nuevo stock (puede ser null para no cambiar)
	     * @param nuevaEditorial   nueva editorial (puede ser null para no cambiar)
	     */
	    public void actualizar(String nuevoTitulo, Double nuevoPrecio,
                Integer nuevoStock, String nuevaEditorial) {
			if (nuevoTitulo != null && !nuevoTitulo.trim().isEmpty()) {
			 this.titulo = nuevoTitulo;
			}
			if (nuevoPrecio != null && nuevoPrecio > 0) {
			 this.precioVenta = nuevoPrecio;
			}
			if (nuevoStock != null && nuevoStock >= 0) {
			 this.stock = nuevoStock;
			}
			if (nuevaEditorial != null && !nuevaEditorial.trim().isEmpty()) {
			 this.editorial = nuevaEditorial;
			}			
}
	   
	    /**
	     * Calcula el precio base del libro SIN IVA.
	     * Útil para mostrar desglose en facturas y reportes.
	     * 
	     * Fórmula: precioBase = precioVenta / (1 + tasaIVA)
	     */
	    public double getPrecioBase() {
	        return this.precioVenta / (1 + this.formato.getTasaIVA());
	    }

	    /**
	     * Calcula el monto de IVA contenido en el precio de venta.
	     */
	    public double getMontoIVA() {
	        return this.precioVenta - getPrecioBase();
	    }

	    //  Métodos heredados de Object 

	    /**
	     * Dos libros son iguales si tienen el mismo ISBN.
	     * El ISBN es la identidad del libro (clave primaria).
	     */
	    
	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj) return true;
	        if (obj == null || getClass() != obj.getClass()) return false;
	        Libro libro = (Libro) obj;
	        return isbn != null && isbn.equals(libro.isbn);
	    }

	    @Override
	    public int hashCode() {
	        return isbn != null ? isbn.hashCode() : 0;
	    }
	    
	    /**
	     * Representación legible del libro. Útil para debugging y logs.
	     */
	    
	    @Override
	    public String toString() {
	        return String.format("Libro [ISBN=%s, título=%s, autores=%s, año=%d, " +
	                "categoría=%s, editorial=%s, páginas=%d, precio=%.2f, " +
	                "stock=%d, formato=%s]",
	                isbn, titulo, autores, anioPublicacion, categoria,
	                editorial, numPaginas, precioVenta, stock, formato);
	    }    
}



