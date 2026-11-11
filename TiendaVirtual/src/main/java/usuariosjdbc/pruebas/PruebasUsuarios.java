package usuariosjdbc.pruebas;
import usuariosjdbc.repositorio.RepositorioUsuarios;
/** Diagnostico no destructivo: no inserta ni elimina datos. */
public class PruebasUsuarios {
    public static void main(String[] args) {
        try {System.out.println(new RepositorioUsuarios().buscarUsuario("prueba@ejemplo.com") == null ? "Conexion OK; usuario de prueba no encontrado" : "Conexion OK; usuario de prueba encontrado");}
        catch(RuntimeException ex) {System.err.println(ex.getMessage());System.exit(1);}
    }
}
