package co.edu.uptc.negocio;

public enum FormatoLibro {

    FISICO("Físico"),
    DIGITAL("Digital");

    private final String etiqueta;

    FormatoLibro(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
    public String toString() {
        return etiqueta;
    }
}
