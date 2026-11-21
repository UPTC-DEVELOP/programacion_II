package co.edu.uptc.model;

import java.util.HashMap;
import java.util.Map;

public class UbicacionesColombia {
    private static final Map<String, String[]> DEPARTAMENTOS_CIUDADES = new HashMap<>();

    static {
        DEPARTAMENTOS_CIUDADES.put("Boyacá", new String[]{
            "Tunja", "Sogamoso", "Duitama", "Chiquinquirá", "Paipa", "Moniquirá", "Villa de Leyva"
        });
        DEPARTAMENTOS_CIUDADES.put("Cundinamarca", new String[]{
            "Bogotá", "Soacha", "Chía", "Zipaquirá", "Girardot", "Facatativá", "Fusagasugá"
        });
        DEPARTAMENTOS_CIUDADES.put("Antioquia", new String[]{
            "Medellín", "Bello", "Itagüí", "Envigado", "Rionegro", "Apartadó"
        });
        DEPARTAMENTOS_CIUDADES.put("Valle del Cauca", new String[]{
            "Cali", "Palmira", "Buenaventura", "Tuluá", "Cartago", "Buga"
        });
        DEPARTAMENTOS_CIUDADES.put("Santander", new String[]{
            "Bucaramanga", "Floridablanca", "Girón", "Piedecuesta", "Barrancabermeja", "San Gil"
        });
        DEPARTAMENTOS_CIUDADES.put("Atlántico", new String[]{
            "Barranquilla", "Soledad", "Malambo", "Sabanalarga", "Puerto Colombia"
        });
    }

    public static String[] getDepartamentos() {
        return DEPARTAMENTOS_CIUDADES.keySet().toArray(new String[0]);
    }

    public static String[] getCiudades(String departamento) {
        if (departamento == null || !DEPARTAMENTOS_CIUDADES.containsKey(departamento)) {
            return new String[0];
        }
        return DEPARTAMENTOS_CIUDADES.get(departamento);
    }
}