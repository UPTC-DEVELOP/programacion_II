package co.edu.uptc.negocio;

import co.edu.uptc.modelo.enums.TipoCliente;

/**
 * CLASE ABSTRACTA CalculadoraDescuento  (paquete: negocio)
 * ---------------------------------------------------------------------------
 * ESTRATEGIA para calcular el descuento sobre el subtotal del carrito según
 * el TIPO de cliente (RF13: "Descuento 10% si el cliente es PREMIUM").
 *
 * ¿POR QUÉ UNA ESTRATEGIA PROPIA Y NO USAR Cliente.calcularDescuento()?
 *  - La jerarquía Cliente (ClientePremium/ClienteRegular) YA implementa el 10%
 *    vía polimorfismo; sin embargo, ese método recibe un objeto ENTIDAD
 *    Cliente, y la capa de SERVICIO del carrito trabaja con ClienteDto (DTO de
 *    presentación). Construir una entidad Cliente completa SOLO para calcular un
 *    descuento sería innecesario y acoplaría el servicio a los mappers.
 *  - La estrategia recibe el enum TipoCliente (que SÍ trae el DTO) y calcula
 *    el descuento sin construir entidades: menos acoplamiento, más simple de
 *    testear (basta un enum, no un objeto Cliente completo).
 *  - OCP: agregar un nuevo tipo de cliente con su descuento (p. ej. ESTUDIANTE
 *    15%) = agregar UNA clase + UNA línea en la fábrica, sin tocar el servicio.
 *
 * NOTA DE CONSISTENCIA: el porcentaje de DescuentoPremium (10%) DEBE coincidir
 * con el de ClientePremium.calcularDescuento(); ambos representan la MISMA
 * regla de negocio vista desde dos capas distintas.
 */
public abstract class CalculadoraDescuento {

    /** Calcula el valor del descuento sobre el subtotal del carrito. */
    public abstract double calcularDescuento(double subtotal);

    /**
     * FÁBRICA ESTÁTICA: devuelve la estrategia de descuento según el tipo de
     * cliente. Centraliza el mapeo enum -> estrategia.
     *
     * @param tipoCliente tipo de cliente (PREMIUM o REGULAR); puede ser null
     * @return la CalculadoraDescuento correspondiente (null se trata como REGULAR)
     */
    public static CalculadoraDescuento porTipoCliente(TipoCliente tipoCliente) {
        if (tipoCliente == TipoCliente.PREMIUM) {
            return new DescuentoPremium();
        }
        // REGULAR (y cualquier valor null/desconocido) => sin descuento.
        return new DescuentoRegular();
    }
}
