package co.edu.uptc.model;

// Indica si el libro se entrega físicamente o como archivo digital.
public enum FormatoLibro {
    FISICO("Físico"), DIGITAL("Digital");
    private final String texto;
    FormatoLibro(String texto) { this.texto = texto; }
    public String getTexto() { return texto; }
    @Override public String toString() { return texto; }
}
