package co.edu.uptc.negocio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JsonUtil {

    private JsonUtil() {
    }

    // ===================== ESCRITURA =====================

    public static String aJson(List<Map<String, Object>> objetos) {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < objetos.size(); i++) {
            Map<String, Object> objeto = objetos.get(i);
            sb.append("  {\n");
            int j = 0;
            for (Map.Entry<String, Object> entrada : objeto.entrySet()) {
                sb.append("    \"").append(escapar(entrada.getKey())).append("\": ");
                Object valor = entrada.getValue();
                if (valor instanceof Number || valor instanceof Boolean) {
                    sb.append(valor);
                } else {
                    sb.append('"').append(escapar(String.valueOf(valor))).append('"');
                }
                if (++j < objeto.size()) {
                    sb.append(',');
                }
                sb.append('\n');
            }
            sb.append("  }");
            if (i < objetos.size() - 1) {
                sb.append(',');
            }
            sb.append('\n');
        }
        return sb.append("]").toString();
    }

    private static String escapar(String texto) {
        StringBuilder sb = new StringBuilder();
        for (char c : texto.toCharArray()) {
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    // ===================== LECTURA =====================

    public static List<Map<String, String>> desdeJson(String json) {
        List<Map<String, String>> lista = new ArrayList<>();
        if (json == null || json.trim().isEmpty()) {
            return lista;
        }
        int[] pos = {0};
        try {
            saltar(json, pos);
            esperar(json, pos, '[');
            saltar(json, pos);
            if (json.charAt(pos[0]) == ']') {
                return lista;
            }
            while (true) {
                lista.add(leerObjeto(json, pos));
                saltar(json, pos);
                char c = json.charAt(pos[0]++);
                if (c == ']') {
                    break;
                }
                if (c != ',') {
                    throw new IllegalArgumentException("JSON inválido en la posición " + pos[0]);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("JSON incompleto", e);
        }
        return lista;
    }

    private static Map<String, String> leerObjeto(String j, int[] p) {
        Map<String, String> mapa = new LinkedHashMap<>();
        saltar(j, p);
        esperar(j, p, '{');
        saltar(j, p);
        if (j.charAt(p[0]) == '}') {
            p[0]++;
            return mapa;
        }
        while (true) {
            saltar(j, p);
            String clave = leerCadena(j, p);
            saltar(j, p);
            esperar(j, p, ':');
            saltar(j, p);
            String valor;
            if (j.charAt(p[0]) == '"') {
                valor = leerCadena(j, p);
            } else {
                int inicio = p[0];
                while (p[0] < j.length() && ",}".indexOf(j.charAt(p[0])) < 0) {
                    p[0]++;
                }
                valor = j.substring(inicio, p[0]).trim();
            }
            mapa.put(clave, valor);
            saltar(j, p);
            char c = j.charAt(p[0]++);
            if (c == '}') {
                break;
            }
            if (c != ',') {
                throw new IllegalArgumentException("JSON inválido en la posición " + p[0]);
            }
        }
        return mapa;
    }

    private static String leerCadena(String j, int[] p) {
        esperar(j, p, '"');
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = j.charAt(p[0]++);
            if (c == '"') {
                break;
            }
            if (c == '\\') {
                char n = j.charAt(p[0]++);
                switch (n) {
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    case 'u':
                        sb.append((char) Integer.parseInt(j.substring(p[0], p[0] + 4), 16));
                        p[0] += 4;
                        break;
                    default: sb.append(n); // cubre \" \\ y \/
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static void saltar(String j, int[] p) {
        while (p[0] < j.length() && Character.isWhitespace(j.charAt(p[0]))) {
            p[0]++;
        }
    }

    private static void esperar(String j, int[] p, char esperado) {
        if (j.charAt(p[0]) != esperado) {
            throw new IllegalArgumentException(
                    "Se esperaba '" + esperado + "' en la posición " + p[0]);
        }
        p[0]++;
    }
}
