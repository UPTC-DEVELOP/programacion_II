package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.DatosTransferencia;
import co.edu.uptc.caso.estudio.model.enums.MetodoPago;
import co.edu.uptc.caso.estudio.negocio.dto.PagoDto;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// Valida los datos de tarjeta o transferencia y construye el objeto DatosTransferencia correspondiente
public class ValidadorPago {

    private static final String REGEX_TARJETA = "\\d{16}";
    private static final String REGEX_CVV = "\\d{3,4}";
    private static final String REGEX_EXPIRACION = "(0[1-9]|1[0-2])/\\d{2}";
    private static final String REGEX_NEQUI = "3\\d{9}";
    private static final DateTimeFormatter FORMATO_EXPIRACION = DateTimeFormatter.ofPattern("MM/yy");

    public String validar(PagoDto pago) {
        if (pago == null) {
            return "Datos de pago inválidos.";
        }
        if (MetodoPago.TARJETA.getEtiqueta().equalsIgnoreCase(pago.getMetodo())) {
            return validarTarjeta(pago);
        }
        if (MetodoPago.TRANSFERENCIA.getEtiqueta().equalsIgnoreCase(pago.getMetodo())) {
            return validarTransferencia(pago);
        }
        return "Método de pago no válido.";
    }

    public DatosTransferencia construirDatosTransferencia(PagoDto pago) {
        if (pago == null) {
            return null;
        }
        if (MetodoPago.TARJETA.getEtiqueta().equalsIgnoreCase(pago.getMetodo())) {
            return new DatosTransferencia(
                    pago.getBanco(),
                    pago.getNumeroTarjeta(),
                    pago.getCvv(),
                    pago.getExpiracion(),
                    null);
        }
        if (MetodoPago.TRANSFERENCIA.getEtiqueta().equalsIgnoreCase(pago.getMetodo())) {
            return new DatosTransferencia(
                    DatosTransferencia.NEQUI,
                    null,
                    null,
                    null,
                    pago.getNumeroNequi());
        }
        return null;
    }

    private String validarTarjeta(PagoDto pago) {
        if (esVacio(pago.getBanco())) {
            return "Seleccione un banco.";
        }
        if (pago.getNumeroTarjeta() == null || !pago.getNumeroTarjeta().matches(REGEX_TARJETA)) {
            return "Número de tarjeta inválido (16 dígitos).";
        }
        if (pago.getCvv() == null || !pago.getCvv().matches(REGEX_CVV)) {
            return "CVV inválido (3 o 4 dígitos).";
        }
        if (pago.getExpiracion() == null || !pago.getExpiracion().matches(REGEX_EXPIRACION)) {
            return "Fecha de expiración inválida (MM/AA).";
        }

        YearMonth vencimiento = parsearExpiracion(pago.getExpiracion());
        if (vencimiento == null) {
            return "Fecha de expiración inválida (MM/AA).";
        }
        if (vencimiento.isBefore(YearMonth.now())) {
            return "La tarjeta se encuentra vencida.";
        }
        return null;
    }

    private YearMonth parsearExpiracion(String expiracion) {
        try {
            return YearMonth.parse(expiracion, FORMATO_EXPIRACION);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private String validarTransferencia(PagoDto pago) {
        if (pago.getNumeroNequi() == null || !pago.getNumeroNequi().matches(REGEX_NEQUI)) {
            return "Número de Nequi inválido (10 dígitos empezando por 3).";
        }
        return null;
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}