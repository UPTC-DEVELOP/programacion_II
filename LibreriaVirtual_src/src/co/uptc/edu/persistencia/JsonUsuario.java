package co.uptc.edu.persistencia;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import co.uptc.edu.interfaces.IGestionUsuario;
import co.uptc.edu.model.Rol;
import co.uptc.edu.model.Usuario;

/**
 * Persistencia de usuarios en un archivo JSON.
 * El archivo se lee una vez al crear el objeto y se reescribe completo en cada cambio.
 * La contraseña nunca se escribe: solo su hash y su sal, codificados en Base64.
 */
public class JsonUsuario implements IGestionUsuario {

    private final Path archivo;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();

    public JsonUsuario(String ruta) {
        this.archivo = Paths.get(ruta);
        cargar();
    }

    @Override
    public void guardar(Usuario usuario) {
        usuarios.put(usuario.getCorreo(), usuario);
        escribir();
    }

    @Override
    public void actualizar(String correoAnterior, Usuario usuario) {
        if (!usuario.getCorreo().equals(correoAnterior)) {
            usuarios.remove(correoAnterior);
        }
        usuarios.put(usuario.getCorreo(), usuario);
        escribir();
    }

    @Override
    public void eliminar(String correo) {
        usuarios.remove(correo);
        escribir();
    }

    @Override
    public Usuario buscar(String correo) {
        return usuarios.get(correo);
    }

    @Override
    public List<Usuario> listar() {
        return new ArrayList<>(usuarios.values());
    }

    /** Deja en el archivo exactamente los usuarios recibidos (se usa para el respaldo). */
    public void reemplazarTodos(List<Usuario> nuevos) {
        usuarios.clear();
        for (Usuario u : nuevos) {
            usuarios.put(u.getCorreo(), u);
        }
        escribir();
    }

    // ---------- Lectura y escritura del archivo ----------
    private void cargar() {
        if (!Files.exists(archivo)) {
            return;
        }
        try (Reader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            RegistroUsuario[] registros = gson.fromJson(lector, RegistroUsuario[].class);
            if (registros == null) {
                return;
            }
            for (RegistroUsuario r : registros) {
                Usuario u = r.aUsuario();
                usuarios.put(u.getCorreo(), u);
            }
        } catch (IOException | JsonParseException | IllegalArgumentException | NullPointerException e) {
            throw new IllegalStateException("No se pudo leer el archivo de usuarios " + archivo + ".", e);
        }
    }

    private void escribir() {
        List<RegistroUsuario> registros = new ArrayList<>();
        for (Usuario u : usuarios.values()) {
            registros.add(new RegistroUsuario(u));
        }
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
            try (Writer escritor = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
                gson.toJson(registros, escritor);
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo de usuarios " + archivo + ".", e);
        }
    }

    /** Forma en que se escribe cada usuario dentro del archivo JSON. */
    private static class RegistroUsuario {
        String nombre;
        String apellido;
        String correo;
        String direccion;
        String telefono;
        String tipoUsuario;
        String salt;
        String hashContrasena;

        RegistroUsuario(Usuario u) {
            this.nombre = u.getNombre();
            this.apellido = u.getApellido();
            this.correo = u.getCorreo();
            this.direccion = u.getDireccion();
            this.telefono = u.getTelefono();
            this.tipoUsuario = u.getTipoUsuario().name();
            this.salt = Base64.getEncoder().encodeToString(u.getSalt());
            this.hashContrasena = Base64.getEncoder().encodeToString(u.getHashContrasena());
        }

        Usuario aUsuario() {
            return Usuario.reconstruir(nombre, apellido, correo, direccion, telefono,
                    Rol.valueOf(tipoUsuario),
                    Base64.getDecoder().decode(salt), Base64.getDecoder().decode(hashContrasena));
        }
    }
}
