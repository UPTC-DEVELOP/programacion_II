package co.edu.uptc.negocio;

import co.edu.uptc.modelo.enums.Categoria;

/**
 * CLASE ABSTRACTA CalculadoraImpuesto  (paquete: negocio)
 * ---------------------------------------------------------------------------
 * ESTRATEGIA para calcular el IVA de un libro según su categoría (RF10:
 * "Aplicar IVA (5% o 19% según categoría del libro").
 *
 * ¿POR QUÉ PATRÓN ESTRATEGIA (y NO un if/else en el servicio)?
 * 
 *  - OCP (Open/Closed): si mañana el negocio crea una nueva tasa (p. ej. 0%
 *    para libros de texto), se agrega UNA clase nueva (ImpuestoExento) y se
 *    toca SOLO la fábrica porCategoria(); el servicio CarritoServiceImpl NO
 *    se modifica.
 *  - SRP: la regla "qué tasa aplica a qué categoría" vive en un solo lugar
 *    (esta jerarquía), no entremezclada con las validaciones del carrito.
 *  - Testeabilidad: cada estrategia es una unidad tiny y determinista, fácil
 *    de probar aisladamente.
 *
 * ¿POR QUÉ CLASE ABSTRACTA Y NO INTERFAZ?
 * 
 *  - La consigna pide "clase abstracta o estrategia": aquí la clase abstracta
 *    aporta un punto de extensión común (y el método estático factoría vive
 *    junto al contrato). Si en el futuro las estrategias necesitaran estado o
 *    métodos compartidos, la jerarquía ya está lista.
 *
 * MAPEO DE CATEGORÍAS A TASAS (DECISIÓN DE DISEÑO QUE DEBE VALIDAR NEGOCIOS):
 * 
 *  - TECNOLOGIA y HISTORIA  -> 5%  (tasa reducida: material formativo)
 *  - CIENCIA_FICCION, ROMANCE y OTROS -> 19% (tasa general)
 *  - Como el mapeo está encapsulado en porCategoria(), cambiarlo después es
 *    un cambio de UNA línea aquí, sin tocar el servicio ni el repositorio.
 */
public abstract class CalculadoraImpuesto {

    /** Devuelve la tasa decimal: 0.05 (5%) o 0.19 (19%). */
    public abstract double getTasa();

    /**
     * Calcula el VALOR del impuesto sobre una base imponible.
     * Se redondea a 2 decimales (precisión monetaria que exige el RF13).
     */
    public abstract double calcularImpuesto(double baseImponible);

    /**
     * FÁBRICA ESTÁTICA: devuelve la estrategia de IVA adecuada para la
     * categoría del libro. Centraliza el mapeo enum -> estrategia.
     *
     * @param categoria categoría del libro (nunca null: el catálogo la garantiza)
     * @return la CalculadoraImpuesto correspondiente a esa categoría
     */
    public static CalculadoraImpuesto porCategoria(Categoria categoria) {
        // Tasa reducida (5%) para categorías formativas/educativas.
        if (categoria == Categoria.TECNOLOGIA || categoria == Categoria.HISTORIA) {
            return new ImpuestoTasaReducida();
        }
        // Tasa general (19%) para el resto (incluye OTROS como caso por defecto).
        return new ImpuestoTasaGeneral();
    }
}
