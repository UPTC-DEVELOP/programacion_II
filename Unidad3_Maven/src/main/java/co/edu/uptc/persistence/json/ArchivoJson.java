package co.edu.uptc.persistence.json;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.edu.uptc.exception.PersistenciaException;

// Este adaptador mantiene el formato JSON en persistence.json. Si se incorpora Gson,
// solo esta clase y los repositorios JSON necesitan cambiar; el resto del proyecto no.
public final class ArchivoJson {
    private ArchivoJson() { }

    public static List<Map<String, String>> leer(Path archivo) {
        try {
            preparar(archivo);
            String texto = new String(Files.readAllBytes(archivo), StandardCharsets.UTF_8).trim();
            if (texto.isEmpty() || texto.equals("[]")) {
				return new ArrayList<>();
			}
            return parsearObjetos(texto);
        } catch (IOException | RuntimeException e) {
            throw new PersistenciaException("El archivo " + archivo.getFileName() + " tiene un formato inválido.", e);
        }
    }

    public static void escribir(Path archivo, List<Map<String, String>> objetos) {
        try {
            preparar(archivo);
            StringBuilder texto = new StringBuilder("[\n");
            for (int i = 0; i < objetos.size(); i++) {
                texto.append("  {");
                Map<String, String> objeto = objetos.get(i);
                int campo = 0;
                for (Map.Entry<String, String> entrada : objeto.entrySet()) {
                    if (campo++ > 0) {
						texto.append(",");
					}
                    texto.append("\"" ).append(escapar(entrada.getKey())).append("\":\"")
                            .append(escapar(entrada.getValue())).append("\"");
                }
                texto.append("}");
                if (i < objetos.size() - 1) {
					texto.append(",");
				}
                texto.append("\n");
            }
            texto.append("]");
            Files.write(archivo, texto.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo guardar " + archivo.getFileName() + ".", e);
        }
    }

    private static void preparar(Path archivo) throws IOException {
        if (archivo.getParent() != null) {
			Files.createDirectories(archivo.getParent());
		}
        if (!Files.exists(archivo)) {
			Files.write(archivo, "[]".getBytes(StandardCharsets.UTF_8));
		}
    }

    private static List<Map<String, String>> parsearObjetos(String texto) {
        List<Map<String, String>> resultado = new ArrayList<>();
        int inicio = texto.indexOf('{');
        while (inicio >= 0) {
            int fin = buscarCierre(texto, inicio);
            if (fin < 0) {
				throw new IllegalArgumentException("Objeto JSON incompleto");
			}
            resultado.add(parsearObjeto(texto.substring(inicio + 1, fin)));
            inicio = texto.indexOf('{', fin + 1);
        }
        return resultado;
    }

    private static int buscarCierre(String texto, int inicio) {
        boolean comillas = false;
        for (int i = inicio; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '"' && (i == 0 || texto.charAt(i - 1) != '\\')) {
				comillas = !comillas;
			}
            if (c == '}' && !comillas) {
				return i;
			}
        }
        return -1;
    }

    private static Map<String, String> parsearObjeto(String contenido) {
        Map<String, String> objeto = new LinkedHashMap<>();
        List<String> campos = separar(contenido, ',');
        for (String campo : campos) {
            List<String> partes = separar(campo, ':');
            if (partes.size() != 2) {
				continue;
			}
            objeto.put(desescapar(quitarComillas(partes.get(0).trim())), desescapar(quitarComillas(partes.get(1).trim())));
        }
        return objeto;
    }

    private static List<String> separar(String texto, char separador) {
        List<String> partes = new ArrayList<>();
        boolean comillas = false; int anterior = 0;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '"' && (i == 0 || texto.charAt(i - 1) != '\\')) {
				comillas = !comillas;
			}
            if (c == separador && !comillas) { partes.add(texto.substring(anterior, i)); anterior = i + 1; }
        }
        partes.add(texto.substring(anterior));
        return partes;
    }

    private static String quitarComillas(String texto) { return texto.length() >= 2 && texto.startsWith("\"") && texto.endsWith("\"") ? texto.substring(1, texto.length() - 1) : texto; }
    private static String escapar(String texto) { return texto == null ? "" : texto.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n"); }
    private static String desescapar(String texto) { return texto.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\"); }
}
