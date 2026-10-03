package co.edu.uptc.persistencia;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.interfaces.IAdministradorRepositorio;
import co.edu.uptc.modelo.Administrador;

/**
 * CLASE AdministradorRepositorioMemoria  (paquete: persistencia)  implements IAdministradorRepositorio
 * ---------------------------------------------------------------------------
 * Almacenamiento TEMPORAL de administradores en una lista en RAM. Solo guarda
 * y recupera: no valida nada (eso es responsabilidad de la capa de negocio).
 * Arranca con un administrador de prueba para poder ingresar al sistema.
 */
public class AdministradorRepositorioMemoria implements IAdministradorRepositorio {

    private final List<Administrador> administradores = new ArrayList<>();

    public AdministradorRepositorioMemoria() {
        administradores.add(new Administrador("Admin", "", "Tienda", "", "CC", "1000000000",
                "admin@uptc.edu.co", "", "", "Admin123*"));
    }

    @Override
    public void guardar(Administrador administrador) {
        administradores.add(administrador);
    }

    @Override
    public List<Administrador> listar() {
        return new ArrayList<>(administradores);   // copia: nadie altera la lista interna
    }

    //El correo no distingue mayusculas/minusculas
    @Override
    public Administrador buscarPorCorreo(String correo) {
        for (Administrador a : administradores) {
            if (a.getCorreoElectronico().equalsIgnoreCase(correo)) {
                return a;
            }
        }
        return null;
    }
}
