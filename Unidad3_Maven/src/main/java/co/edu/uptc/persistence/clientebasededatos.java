package co.edu.uptc.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import co.edu.uptc.exception.PersistenciaException;
import co.edu.uptc.model.*;
public class clientebasededatos implements UsuarioRepository {

@Override
public List<Usuario> listar() throws PersistenciaException {
	List<Usuario> clientes = new ArrayList<>();
	String sql = "SELECT * FROM clientes";
	try (Connection conexion = Conexion.obtenerConexion()) {
		if (conexion == null) {
			throw new SQLException("No hay conexión con MariaDB");
		}
		try (PreparedStatement consulta = conexion.prepareStatement(sql);
			     ResultSet resultado = consulta.executeQuery()) {
			while (resultado.next()) {
				String correo = resultado.getString("correo_electronico");
				String nombre = resultado.getString("nombre_completo");
				String direccion = resultado.getString("direccion_envio");
				String telefono = resultado.getString("telefono");
				String tipo = resultado.getString("tipo_cliente");
				String contrasena = resultado.getString("contrasena_hash");
				
				
				

				

				Cliente cliente;

				if ("PREMIUM".equalsIgnoreCase(tipo)) {

				    cliente = new ClientePremium(nombre, correo, contrasena, direccion, telefono);

				} else {

				    cliente = new ClienteRegular(nombre, correo, contrasena, direccion, telefono);

				}

				clientes.add(cliente);
	        }

	    }

	} catch (SQLException e) {

	    throw new PersistenciaException(
	        "Error al consultar clientes: " + e.getMessage(), e);

	}

	return clientes;

	}

@Override
public Optional<Usuario> buscarPorCorreo(String correo) throws PersistenciaException {

    String sql = "SELECT * FROM clientes WHERE correo_electronico = ?";

    try (Connection conexion = Conexion.obtenerConexion();
         PreparedStatement consulta = conexion.prepareStatement(sql)) {

        consulta.setString(1, correo);

        try (ResultSet resultado = consulta.executeQuery()) {

            if (resultado.next()) {

                String nombre = resultado.getString("nombre_completo");
                String contrasena = resultado.getString("contrasena_hash");
                String direccion = resultado.getString("direccion_envio");
                String telefono = resultado.getString("telefono");
                String tipo = resultado.getString("tipo_cliente");

                Cliente cliente;

                if ("PREMIUM".equalsIgnoreCase(tipo)) {
                    cliente = new ClientePremium(nombre, correo, contrasena, direccion, telefono);
                } else {
                    cliente = new ClienteRegular(nombre, correo, contrasena, direccion, telefono);
                }

                return Optional.of(cliente);
            }
        }

    } catch (SQLException e) {
        throw new PersistenciaException(
            "Error al buscar cliente: " + e.getMessage(), e);
    }

    return Optional.empty();

    }

@Override
public void guardar(Usuario usuario) throws PersistenciaException {
	if (usuario == null) {
	    throw new PersistenciaException("El cliente no puede ser nulo.");
	}
	if (usuario.getNombreCompleto() == null || usuario.getNombreCompleto().isBlank()) {
	    throw new PersistenciaException("El nombre del cliente es obligatorio.");
	}
	if (usuario.getNombreCompleto().trim().length() < 3) {
	    throw new PersistenciaException("El nombre debe tener al menos 3 caracteres.");
	}

	if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
	    throw new PersistenciaException("El correo electrónico es obligatorio.");
	}
	if (!usuario.getCorreo().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
	    throw new PersistenciaException("El correo electrónico no tiene un formato válido.");
	}
	if (usuario.getContrasenaHash() == null || usuario.getContrasenaHash().isBlank()) {
	    throw new PersistenciaException("La contraseña es obligatoria.");
	}
	if (!(usuario instanceof Cliente)) {
	    throw new PersistenciaException("Solo se pueden guardar clientes.");
	}

	Cliente cliente = (Cliente) usuario;
	if (cliente.getTelefono() == null || cliente.getTelefono().isBlank()) {
	    throw new PersistenciaException("El teléfono es obligatorio.");
	}
	if (!cliente.getTelefono().matches("\\d+")) {
	    throw new PersistenciaException("El teléfono solo debe contener números.");
	}

	if (cliente.getDireccionEnvio() == null || cliente.getDireccionEnvio().isBlank()) {
	    throw new PersistenciaException("La dirección de envío es obligatoria.");
	}
	if (cliente.getDireccionEnvio().trim().length() < 5) {
	    throw new PersistenciaException("La dirección debe tener al menos 5 caracteres.");
	}
	
	Optional<Usuario> existente = buscarPorCorreo(cliente.getCorreo());

	if (existente.isPresent()) {
	    throw new PersistenciaException(
	        "Ya existe un cliente registrado con ese correo electrónico."
	    );
	}
    String sql = "INSERT INTO clientes "
            + "(correo_electronico, nombre_completo, direccion_envio, telefono, tipo_cliente, contrasena_hash) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conexion = Conexion.obtenerConexion();
         PreparedStatement consulta = conexion.prepareStatement(sql)) {

        consulta.setString(1, cliente.getCorreo());
        consulta.setString(2, cliente.getNombreCompleto());
        consulta.setString(3, cliente.getDireccionEnvio());
        consulta.setString(4, cliente.getTelefono());

        String tipo = (cliente instanceof ClientePremium) ? "PREMIUM" : "REGULAR";

        consulta.setString(5, tipo);
        consulta.setString(6, cliente.getContrasenaHash());

        consulta.executeUpdate();

    } catch (SQLException e) {

        throw new PersistenciaException(
                "Error al guardar cliente: " + e.getMessage(), e);
    }
}

}    