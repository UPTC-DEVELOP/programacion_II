package co.edu.uptc.modelo.enums;


/**
 * ENUMERACIÓN Formato  (paquete: modelo)
 * ---------------------------------------------------------------------------
 * Un libro puede ser FISICO o DIGITAL (RF01).
 *
 * El caso de estudio dice que el precio de venta incluye IVA y que los
 * porcentajes posibles son 19 y 5. RF01 fija: "si el formato es DIGITAL el
 * precio debe tener IVA del 19%".
 *
 * SUPUESTO (confirmar con el Product Owner/docente): para FISICO se usa 5%.
 *
 * Aplicamos POLIMORFISMO ligero: en vez de escribir "if (formato == DIGITAL)"
 * por todo el código, cada constante conoce su propio porcentaje de IVA.
 * Si mañana cambia la regla, solo se modifica este enum (principio OCP/SRP).
 */
public enum Formato {

    FISICO("Físico", 5),
    DIGITAL("Digital", 19);

    private final String etiqueta;
    /** Porcentaje de IVA ya incluido en el precio de venta. */
    private final int porcentajeIva;

    Formato(String etiqueta, int porcentajeIva) {
        this.etiqueta = etiqueta;
        this.porcentajeIva = porcentajeIva;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public int getPorcentajeIva() {
        return porcentajeIva;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
