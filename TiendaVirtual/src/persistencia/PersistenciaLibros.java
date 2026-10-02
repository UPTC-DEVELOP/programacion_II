package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import modelo.Libro;

public class PersistenciaLibros {
    private static final String CARPETA = "datos";
    private static final String ARCHIVO = CARPETA + File.separator + "libros.txt";

    public static ArrayList<Libro> cargarLibros() throws IOException {
        prepararArchivo();
        ArrayList<Libro> libros = new ArrayList<Libro>();

        try (BufferedReader lector = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length == 4) {
                    try {
                        int precio = Integer.parseInt(datos[3]);
                        libros.add(new Libro(datos[0], datos[1], datos[2], precio));
                    } catch (NumberFormatException e) {
                        System.err.println("Se ignoró una línea de libros con precio no válido.");
                    }
                }
            }
        }

        return libros;
    }

    public static void guardarTodos(ArrayList<Libro> libros) throws IOException {
        prepararArchivo();

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Libro libro : libros) {
                escritor.write(convertirEnLinea(libro));
                escritor.newLine();
            }
        }
    }

    private static String convertirEnLinea(Libro libro) {
        return limpiar(libro.getTitulo()) + ";"
                + limpiar(libro.getAutor()) + ";"
                + limpiar(libro.getFormato()) + ";"
                + libro.getPrecio();
    }

    private static String limpiar(String texto) {
        return texto.replace(";", ",").replace("\n", " ").replace("\r", " ");
    }

    private static void prepararArchivo() throws IOException {
        File carpeta = new File(CARPETA);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            archivo.createNewFile();
        }
    }
}
