// Oscar

package co.edu.uptc.negocio.modelo;

public enum Categoria {

	CIENCIA_FICCION("Ciencia Ficción"),
    HISTORIA("Historia"),
    TECNOLOGIA("Tecnología"),
    ROMANCE("Romance"),
    FANTASIA("Fantasía"),
    TERROR("Terror"),
    BIOGRAFIA("Biografía"),
    INFANTIL("Infantil"),
    ACADEMICO("Académico"),
    OTROS("Otros");
	
	// Campo que almacena el nombre legible para mostrar en la GUI
    private final String nombreMostrar;

    /**
     * Constructor privado (obligatorio en enums).
     * Asocia cada constante con su representación en pantalla.
     */
    Categoria(String nombreMostrar) {
        this.nombreMostrar = nombreMostrar;
    }

    /**
     * Getter del nombre legible.
     * Se usa para poblar el JComboBox del formulario de registro.
     */
    public String getNombreMostrar() {
        return nombreMostrar;
    }

    /**
     * Override de toString para que al hacer System.out.println(categoria)
     * se muestre el nombre legible y no el nombre de la constante.
     */
    @Override
    public String toString() {
        return nombreMostrar;
    }
}
