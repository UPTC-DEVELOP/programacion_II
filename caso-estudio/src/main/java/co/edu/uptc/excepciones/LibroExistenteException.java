package co.edu.uptc.excepciones;

/**
 * EXCEPCIÓN LibroExistenteException  (paquete: excepciones)
 * ---------------------------------------------------------------------------
 * Se lanza cuando se intenta registrar un libro cuyo ISBN ya está presente en
 * el catálogo (RF01: "validar ISBN único").
 *
 * Hereda de ReglaNegocioException para mantener la convención del proyecto:
 * toda violación de negocio es una excepción checked que obliga a la capa que
 * llama (controlador/GUI) a manejarla y mostrar un mensaje al usuario.
 *
 * Aplica: SRP (Single Responsibility Principle) - representa una única
 * condición de error de negocio.
 */
public class LibroExistenteException extends ReglaNegocioException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepción con un mensaje listo para mostrar al usuario.
     *
     * @param mensaje Descripción legible del error
     */
    public LibroExistenteException(String mensaje) {
        super(mensaje);
    }
}
