package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.modelo.Administrador;

/**
 * INTERFAZ IAdministradorRepositorio  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * CONTRATO de PERSISTENCIA de administradores. La capa de negocio
 * (GestionAcceso) solo conoce esta interfaz. Hoy la implementa
 * AdministradorRepositorioMemoria; para usar archivos o JDBC basta crear otra
 * clase que la implemente y cambiar UNA línea en AppLibros (OCP / DIP).
 */
public interface IAdministradorRepositorio {

    void guardar(Administrador administrador);

    List<Administrador> listar();

    /** @return el administrador o null si no existe (sin distinguir mayúsculas). */
    Administrador buscarPorCorreo(String correo);
}
