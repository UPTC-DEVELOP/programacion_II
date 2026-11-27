package co.edu.uptc.tiendalibros.interfaces;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Cliente;
import co.edu.uptc.tiendalibros.modelo.Empleado;
import co.edu.uptc.tiendalibros.modelo.Usuario;

/// Representa el repositorio de datos que almacena las cuentas de usuario, con sus roles
/// y permisos.
///
/// Este repositorio es particular porque una cuenta de usuario no se guarda ni se elimina
/// por sí sola.
/// 
/// Una cuenta de usuario nace junto a un cliente o a un empleado.
/// 
/// Una cuenta de usuario se elimina cuando se elimina el cliente o el empleado respectivo.
public interface IRepositorioUsuario extends IRepositorioSoloConsultas<Usuario> {
  
  /// Busca la cuenta de usuario con el correo electrónico especificado.
  ///
  /// La búsqueda no distingue mayúsculas de minúsculas.
  /// 
  /// La cuenta se devuelve completa: con sus roles, sus permisos, su nombre visible y el ID
  /// de su cliente si lo tiene.
  ///
  /// @param correoElectronico Correo electrónico de la cuenta.
  /// @return La cuenta encontrada. O un valor vacío si no existe, o si el correo es nulo.
  Optional<Usuario> buscarPorCorreo(String correoElectronico);
  
  /// Registra un cliente nuevo: guarda su cuenta de usuario, sus roles y sus datos de
  /// cliente en una sola transacción.
  /// 
  /// Si algún paso falla, no se guarda nada.
  ///
  /// El correo electrónico del cliente que se guarda es el de la cuenta de usuario.
  ///
  /// @param usuario Cuenta de usuario nueva sin ID.
  /// @param cliente Datos del cliente nuevo sin ID.
  /// @return La cuenta guardada, completa y con el ID del cliente.
  /// @throws IllegalArgumentException Si algún argumento es nulo o ya tiene ID.
  Usuario registrarCliente(Usuario usuario, Cliente cliente);
  
  /// Crea un empleado nuevo: guarda su cuenta de usuario, sus roles y sus datos de empleado
  /// en una sola transacción.
  /// 
  /// Si algún paso falla, no se guarda nada.
  ///
  /// @param usuario  Cuenta de usuario nueva sin ID.
  /// @param empleado Datos del empleado nuevo sin ID.
  /// @return El empleado guardado, con su ID y el ID de su cuenta.
  /// @throws IllegalArgumentException Si algún argumento es nulo, o si ya tiene ID.
  Empleado crearEmpleado(Usuario usuario, Empleado empleado);
  
  /// Elimina el empleado con el ID especificado.
  /// 
  /// El repositorio también eliminará la cuenta de usuario del empleado y sus roles.
  ///
  /// @param idEmpleado ID del empleado, pero no el ID de su cuenta de usuario.
  /// @return `true` si el empleado se eliminó. `false` si no se encontró.
  /// @throws IllegalArgumentException Si el ID es nulo.
  boolean eliminarEmpleado(Long idEmpleado);
}