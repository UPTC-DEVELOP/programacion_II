package co.edu.uptc.negocio.modelo;

/**
 * Representa el formato físico del libro.
 * 
 * Importante para el cálculo de IVA según RF01:
 * - DIGITAL → IVA del 19%
 * - FISICO  → IVA del 5%
 * 
 * Esta decisión de negocio se encapsula aquí para no dispersarla
 * en múltiples clases (principio DRY).
 */

public enum Formato {

	FISICO("Físico", 0.05),   // 5% IVA para libros físicos
    DIGITAL("Digital", 0.19); // 19% IVA para libros digitales

    private final String nombreMostrar;
    private final double tasaIVA; // Tasa de IVA asociada al formato

    Formato(String nombreMostrar, double tasaIVA) {
        this.nombreMostrar = nombreMostrar;
        this.tasaIVA = tasaIVA;
    }

    public String getNombreMostrar() {
        return nombreMostrar;
    }

    /**
     * Getter de la tasa de IVA.
     * Permite calcular impuestos sin hardcodear valores en la lógica de negocio.
     */
    public double getTasaIVA() {
        return tasaIVA;
    }

    @Override
    public String toString() {
        return nombreMostrar;
    }
}
