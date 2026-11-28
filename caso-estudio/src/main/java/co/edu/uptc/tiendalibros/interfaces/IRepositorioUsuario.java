package co.edu.uptc.tiendalibros.interfaces;

import java.util.Optional;

import co.edu.uptc.tiendalibros.modelo.Cliente;
import co.edu.uptc.tiendalibros.modelo.Rol;
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
  
  /// Cuenta las cuentas de usuario habilitadas que tienen el rol especificado.
  ///
  /// @param rol Rol que se quiere contar.
  /// @return Cantidad de cuentas habilitadas con ese rol.
  /// @throws IllegalArgumentException Si el rol es nulo.
  int contarCuentasHabilitadasConRol(Rol rol);
}