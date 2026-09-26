package co.uptc.edu.negocio;

import java.util.ArrayList;
import java.util.List;

public class Categoria {

	private int idCategoria;
    private String nombre;
    private String descripcion;

    public Categoria() {}

    public Categoria(int idCategoria, String nombre, String descripcion) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Método del diagrama de negocio
    public List<Categoria> listar() {
        List<Categoria> lista = new ArrayList<>();
        lista.add(new Categoria(1, "Ficción", "Novelas y obras literarias"));
        lista.add(new Categoria(2, "Tecnología", "Libros de programación y sistemas"));
        lista.add(new Categoria(3, "Historia", "Documentales y contexto histórico"));
        return lista;
    }

    // Getters y Setters
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
	
}
