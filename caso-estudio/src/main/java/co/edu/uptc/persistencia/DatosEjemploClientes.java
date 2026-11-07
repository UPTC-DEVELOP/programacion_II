package co.edu.uptc.persistencia;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.ClientePremium;
import co.edu.uptc.modelo.ClienteRegular;
import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;
import co.edu.uptc.negocio.CifradorContrasena;

/**
 * Clientes de ejemplo (semillas) compartidos por LocalCliente y
 * ClienteJsonRepository, para no duplicar los datos.
 *
 * <p>Las contraseñas se guardan cifradas. Para iniciar sesión se usan:
 * admin@libros.com / admin123, cliente@libros.com / cliente123 y
 * maria@libros.com / maria123.</p>
 */
final class DatosEjemploClientes {

    private DatosEjemploClientes() {
    }

    /**
     * Crea los clientes de ejemplo con id 1, 2 y 3.
     *
     * @return Lista nueva de clientes de ejemplo
     */
    static List<Cliente> crear() {
        Timestamp ahora = new Timestamp(System.currentTimeMillis());
        List<Cliente> clientes = new ArrayList<>();

        clientes.add(new ClientePremium(
                "Ana", "Maria", "Gomez", "Restrepo", "CC", "1000000001",
                "admin@libros.com", "3001112233", "Calle 1 # 2-3",
                1, TipoCliente.PREMIUM, CifradorContrasena.hash("admin123"), ahora, 0, Rol.ADMIN));

        clientes.add(new ClienteRegular(
                "Luis", "Andres", "Martinez", "Diaz", "CC", "1000000002",
                "cliente@libros.com", "3004445566", "Carrera 5 # 6-7",
                2, TipoCliente.REGULAR, CifradorContrasena.hash("cliente123"), ahora, 0, Rol.CLIENTE));

        clientes.add(new ClientePremium(
                "Maria", "Elena", "Sanchez", "Ortiz", "CC", "1000000003",
                "maria@libros.com", "3007778899", "Avenida 8 # 9-10",
                3, TipoCliente.PREMIUM, CifradorContrasena.hash("maria123"), ahora, 0, Rol.CLIENTE));

        return clientes;
    }
}
