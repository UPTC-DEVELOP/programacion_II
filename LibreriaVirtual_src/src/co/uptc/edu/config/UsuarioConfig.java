package co.uptc.edu.config;

import co.uptc.edu.interfaces.IGestionUsuario;
import co.uptc.edu.model.GestionUsuarios;
import co.uptc.edu.model.Usuario;
import co.uptc.edu.persistencia.ConexionBD;
import co.uptc.edu.persistencia.JdbcUsuario;
import co.uptc.edu.persistencia.JsonUsuario;
import co.uptc.edu.persistencia.UsuarioConRespaldo;

public class UsuarioConfig {

    public static final String RUTA_USUARIOS_JSON = "data/usuarios.json";

    private final IGestionUsuario iUsuario;
    private final GestionUsuarios gestionUsuarios;

    public UsuarioConfig() {
        iUsuario = crearPersistencia();
        gestionUsuarios = new GestionUsuarios(iUsuario);
    }

    public GestionUsuarios getGestionUsuarios() {
        return gestionUsuarios;
    }

    public IGestionUsuario getiUsuario() {
        return iUsuario;
    }

    private static IGestionUsuario crearPersistencia() {
        JsonUsuario json = new JsonUsuario(RUTA_USUARIOS_JSON);
        if (ConexionBD.disponible()) {
            try {
                JdbcUsuario jdbc = new JdbcUsuario();
                if (jdbc.listar().isEmpty()) {
                    for (Usuario u : json.listar()) {
                        jdbc.guardar(u);
                    }
                }
                return new UsuarioConRespaldo(jdbc, json);
            } catch (IllegalStateException e) {
                System.err.println(e.getMessage() + " Verifique que exista la tabla usuarios (sql/libreria_virtual.sql).");
            }
        }
        System.err.println("Aviso: sin base de datos, los usuarios se guardan en " + RUTA_USUARIOS_JSON + ".");
        return json;
    }
}
