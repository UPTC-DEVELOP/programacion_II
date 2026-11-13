package co.edu.uptc.persistence.json;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import co.edu.uptc.model.Administrador;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.ClientePremium;
import co.edu.uptc.model.ClienteRegular;
import co.edu.uptc.model.Rol;
import co.edu.uptc.model.Usuario;
import co.edu.uptc.persistence.UsuarioRepository;
import co.edu.uptc.util.SeguridadHash;

// UsuarioJsonRepository reconstruye cada usuario usando el campo tipoUsuario.
public class UsuarioJsonRepository implements UsuarioRepository {
    private final Path archivo;
    private final List<Usuario> usuarios = new ArrayList<>();

    public UsuarioJsonRepository(Path archivo) {
        this.archivo = archivo; cargar();
        if (usuarios.isEmpty()) { semillas(); guardarTodos(); }
    }
    @Override public List<Usuario> listar() { return new ArrayList<>(usuarios); }
    @Override public Optional<Usuario> buscarPorCorreo(String correo) { for (Usuario u : usuarios) {
		if (u.getCorreo().equalsIgnoreCase(correo)) {
			return Optional.of(u);
		}
	} return Optional.empty(); }
    @Override public void guardar(Usuario usuario) { eliminarDeMemoria(usuario.getCorreo()); usuarios.add(usuario); guardarTodos(); }

    private void cargar() {
        for (Map<String, String> m : ArchivoJson.leer(archivo)) {
            Rol rol = Rol.valueOf(m.get("rol"));
            if (rol == Rol.ADMINISTRADOR) {
				usuarios.add(new Administrador(m.get("nombre"), m.get("correo"), m.get("hash")));
			} else if ("PREMIUM".equals(m.get("tipoUsuario"))) {
				usuarios.add(new ClientePremium(m.get("nombre"), m.get("correo"), m.get("hash"), m.get("direccion"), m.get("telefono")));
			} else {
				usuarios.add(new ClienteRegular(m.get("nombre"), m.get("correo"), m.get("hash"), m.get("direccion"), m.get("telefono")));
			}
        }
    }
    private void guardarTodos() {
        List<Map<String, String>> filas = new ArrayList<>();
        for (Usuario u : usuarios) {
            Map<String, String> m = new LinkedHashMap<>();
            m.put("nombre", u.getNombreCompleto()); m.put("correo", u.getCorreo()); m.put("hash", u.getContrasenaHash()); m.put("rol", u.getRol().name());
            if (u instanceof Cliente) { Cliente c = (Cliente) u; m.put("tipoUsuario", c instanceof ClientePremium ? "PREMIUM" : "REGULAR"); m.put("direccion", c.getDireccionEnvio()); m.put("telefono", c.getTelefono()); } else {
				m.put("tipoUsuario", "ADMINISTRADOR");
			}
            filas.add(m);
        }
        ArchivoJson.escribir(archivo, filas);
    }
    private void semillas() {
        usuarios.add(new Administrador("Administrador", "admin@libreria.com", SeguridadHash.hash("Admin123")));
        usuarios.add(new ClienteRegular("Ana Pérez", "ana@correo.com", SeguridadHash.hash("Ana12345"), "Calle 1 # 2-3", "3001234567"));
        usuarios.add(new ClientePremium("Carlos Gómez", "carlos@correo.com", SeguridadHash.hash("Carlos123"), "Carrera 4 # 5-6", "3107654321"));
    }
    private void eliminarDeMemoria(String correo) { for (int i = usuarios.size() - 1; i >= 0; i--) {
		if (usuarios.get(i).getCorreo().equalsIgnoreCase(correo)) {
			usuarios.remove(i);
		}
	} }
}
