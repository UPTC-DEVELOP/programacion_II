package com.letsread.model;

/** Reconstruye el tipo de cliente correcto a partir del texto guardado ("Regular" / "Premium"). */
public final class ClienteFactory {
    private ClienteFactory() { }

    public static Cliente crear(String tipo, String nombre, String correo,
                                String direccion, String telefono, String clave) {
        if ("Premium".equalsIgnoreCase(tipo)) {
            return new ClientePremium(nombre, correo, direccion, telefono, clave);
        }
        return new ClienteRegular(nombre, correo, direccion, telefono, clave);
    }
}