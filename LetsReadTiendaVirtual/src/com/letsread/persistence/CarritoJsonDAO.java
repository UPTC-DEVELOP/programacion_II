package com.letsread.persistence;

import com.letsread.model.ElementoCarrito;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CarritoJsonDAO {
    private static final String RUTA_JSON = "carrito_letsread.json";

    public static void guardarCarrito(List<ElementoCarrito> items) {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < items.size(); i++) {
            ElementoCarrito item = items.get(i);
            json.append("  {\n")
                .append("    \"isbn\": \"").append(item.getLibro().getIsbn()).append("\",\n")
                .append("    \"titulo\": \"").append(item.getLibro().getTitulo()).append("\",\n")
                .append("    \"cantidad\": ").append(item.getCantidad()).append(",\n")
                .append("    \"subtotal\": ").append(item.getSubtotal()).append("\n")
                .append("  }").append(i < items.size() - 1 ? ",\n" : "\n");
        }
        json.append("]");

        try (FileWriter writer = new FileWriter(RUTA_JSON)) {
            writer.write(json.toString());
            ArchivoTxtDAO.registrarOperacion("Carrito guardado en " + RUTA_JSON);
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo JSON: " + e.getMessage());
        }
    }
}