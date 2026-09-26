package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import modelo.Usuario;

public class PersistenciaUsuarios {
    private static final String CARPETA = "datos";
    private static final String ARCHIVO = CARPETA + File.separator + "usuarios.txt";

    public static ArrayList<Usuario> cargarUsuarios() throws IOException {
        prepararArchivo();
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

        try (BufferedReader lector = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length == 6) {
                    usuarios.add(new Usuario(
                            datos[0], datos[1], datos[2],
                            datos[3], datos[4], datos[5]));
                }
            }
        }

        return usuarios;
    }

    public static void guardarUsuario(Usuario usuario) throws IOException {
        prepararArchivo();

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            escritor.write(convertirEnLinea(usuario));
            escritor.newLine();
        }
    }

    public static void guardarTodos(ArrayList<Usuario> usuarios) throws IOException {
        prepararArchivo();

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Usuario usuario : usuarios) {
                escritor.write(convertirEnLinea(usuario));
                escritor.newLine();
            }
        }
    }

    private static String convertirEnLinea(Usuario usuario) {
        return limpiar(usuario.getNombre()) + ";"
                + limpiar(usuario.getCorreo()) + ";"
                + limpiar(usuario.getDireccion()) + ";"
                + limpiar(usuario.getTelefono()) + ";"
                + limpiar(usuario.getTipoCliente()) + ";"
                + limpiar(usuario.getContrasena());
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
