package co.edu.uptc.service;

import co.edu.uptc.exception.CredencialesInvalidasException;
import co.edu.uptc.exception.ValidacionException;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.ClientePremium;
import co.edu.uptc.model.ClienteRegular;
import co.edu.uptc.model.Usuario;
import co.edu.uptc.persistence.RegistroOperaciones;
import co.edu.uptc.persistence.UsuarioRepository;
import co.edu.uptc.util.SeguridadHash;
import co.edu.uptc.util.Validador;

// AutenticacionService aplica las reglas de usuarios y deja el almacenamiento en UsuarioRepository.
public class AutenticacionService {
    private final UsuarioRepository usuarios;
    private final RegistroOperaciones registro;
    public AutenticacionService(UsuarioRepository usuarios, RegistroOperaciones registro) { this.usuarios = usuarios; this.registro = registro; }

    public Cliente registrarCliente(String nombre, String correo, String contrasena, String direccion,
                                    String telefono, boolean premium) {
        Validador.nombre(nombre); Validador.correo(correo); Validador.contrasena(contrasena); Validador.telefono(telefono); Validador.obligatorio(direccion, "dirección");
        String normalizado = correo.trim().toLowerCase();
        if (usuarios.buscarPorCorreo(normalizado).isPresent()) {
			throw new ValidacionException("Ya existe un usuario con ese correo.");
		}
        Cliente cliente = premium ? new ClientePremium(nombre.trim(), normalizado, SeguridadHash.hash(contrasena), direccion.trim(), telefono.trim())
                : new ClienteRegular(nombre.trim(), normalizado, SeguridadHash.hash(contrasena), direccion.trim(), telefono.trim());
        usuarios.guardar(cliente); registro.registrar("INFO", normalizado, "Cliente registrado"); return cliente;
    }
    public Usuario iniciarSesion(String correo, String contrasena) {
        Usuario usuario = usuarios.buscarPorCorreo(correo.trim().toLowerCase()).orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos."));
        if (!usuario.getContrasenaHash().equals(SeguridadHash.hash(contrasena))) {
			throw new CredencialesInvalidasException("Correo o contraseña incorrectos.");
		}
        registro.registrar("INFO", usuario.getCorreo(), "Inicio de sesión"); return usuario;
    }
    public Cliente actualizarCliente(Cliente cliente, String nombre, String direccion, String telefono, String nuevaContrasena) {
        Validador.nombre(nombre); Validador.telefono(telefono); Validador.obligatorio(direccion, "dirección");
        if (nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
			Validador.contrasena(nuevaContrasena);
		}
        cliente.setNombreCompleto(nombre.trim()); cliente.setDireccionEnvio(direccion.trim()); cliente.setTelefono(telefono.trim());
        if (nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
			cliente.setContrasenaHash(SeguridadHash.hash(nuevaContrasena));
		}
        usuarios.guardar(cliente); registro.registrar("INFO", cliente.getCorreo(), "Datos personales actualizados"); return cliente;
    }
}
