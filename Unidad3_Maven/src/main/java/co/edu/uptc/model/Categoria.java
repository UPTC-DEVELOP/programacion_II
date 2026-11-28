package co.edu.uptc.model;

// Estas categorías permiten clasificar y buscar libros sin usar textos sueltos.
public enum Categoria {
    CIENCIA_FICCION("Ciencia Ficción"), HISTORIA("Historia"), TECNOLOGIA("Tecnología"),
    LITERATURA("Literatura"), CIENCIA("Ciencia"), OTRA("Otra");
    private final String texto;
    Categoria(String texto) { this.texto = texto; }
    public String getTexto() { return texto; }
    @Override public String toString() { return texto; }
}
