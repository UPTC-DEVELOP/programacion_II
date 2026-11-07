package main.java.co.edu.uptc.model;

import java.util.LinkedHashMap;
import java.util.Map;

/** Departamentos de Colombia con algunas de sus ciudades principales. */
public final class UbicacionesColombia {
    private static final Map<String, String[]> DATOS = new LinkedHashMap<>();

    static {
        DATOS.put("Amazonas", new String[]{"Leticia", "Puerto Nariño"});
        DATOS.put("Antioquia", new String[]{"Medellín", "Apartadó", "Bello", "Envigado", "Itagüí", "Turbo", "Rionegro"});
        DATOS.put("Arauca", new String[]{"Arauca", "Saravena", "Tame"});
        DATOS.put("Atlántico", new String[]{"Barranquilla", "Soledad", "Malambo"});
        DATOS.put("Bogotá D.C.", new String[]{"Bogotá"});
        DATOS.put("Bolívar", new String[]{"Cartagena", "Magangué", "Turbaco"});
        DATOS.put("Boyacá", new String[]{"Tunja", "Duitama", "Sogamoso", "Chiquinquirá", "Paipa", "Puerto Boyaca"});
        DATOS.put("Caldas", new String[]{"Manizales", "La Dorada", "Chinchiná"});
        DATOS.put("Caquetá", new String[]{"Florencia", "San Vicente del Caguán"});
        DATOS.put("Casanare", new String[]{"Yopal", "Aguazul", "Villanueva"});
        DATOS.put("Cauca", new String[]{"Popayán", "Santander de Quilichao", "Puerto Tejada"});
        DATOS.put("Cesar", new String[]{"Valledupar", "Aguachica", "Bosconia"});
        DATOS.put("Chocó", new String[]{"Quibdó", "Istmina", "Tadó"});
        DATOS.put("Córdoba", new String[]{"Montería", "Lorica", "Sahagún"});
        DATOS.put("Cundinamarca", new String[]{"Soacha", "Fusagasugá", "Zipaquirá", "Girardot", "Chía"});
        DATOS.put("Guainía", new String[]{"Inírida"});
        DATOS.put("Guaviare", new String[]{"San José del Guaviare"});
        DATOS.put("Huila", new String[]{"Neiva", "Pitalito", "Garzón"});
        DATOS.put("La Guajira", new String[]{"Riohacha", "Maicao", "Uribia"});
        DATOS.put("Magdalena", new String[]{"Santa Marta", "Ciénaga", "Fundación"});
        DATOS.put("Meta", new String[]{"Villavicencio", "Acacías", "Granada"});
        DATOS.put("Nariño", new String[]{"Pasto", "Tumaco", "Ipiales"});
        DATOS.put("Norte de Santander", new String[]{"Cúcuta", "Ocaña", "Pamplona"});
        DATOS.put("Putumayo", new String[]{"Mocoa", "Puerto Asís", "Orito"});
        DATOS.put("Quindío", new String[]{"Armenia", "Calarcá", "Montenegro"});
        DATOS.put("Risaralda", new String[]{"Pereira", "Dosquebradas", "Santa Rosa de Cabal"});
        DATOS.put("San Andrés y Providencia", new String[]{"San Andrés", "Providencia"});
        DATOS.put("Santander", new String[]{"Bucaramanga", "Floridablanca", "Barrancabermeja", "San Gil"});
        DATOS.put("Sucre", new String[]{"Sincelejo", "Corozal", "Sampués"});
        DATOS.put("Tolima", new String[]{"Ibagué", "Espinal", "Honda"});
        DATOS.put("Valle del Cauca", new String[]{"Cali", "Palmira", "Buenaventura", "Tuluá", "Cartago"});
        DATOS.put("Vaupés", new String[]{"Mitú"});
        DATOS.put("Vichada", new String[]{"Puerto Carreño", "La Primavera"});
    }

    private UbicacionesColombia() {}

    public static String[] departamentos() {
        return DATOS.keySet().toArray(new String[0]);
    }

    public static String[] ciudadesDe(String departamento) {
        String[] ciudades = DATOS.get(departamento);
        return ciudades != null ? ciudades : new String[0];
    }
}