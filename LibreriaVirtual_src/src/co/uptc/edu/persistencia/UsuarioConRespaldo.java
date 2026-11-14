package co.uptc.edu.persistencia;

import java.util.List;

import co.uptc.edu.interfaces.IGestionUsuario;
import co.uptc.edu.model.Usuario;


//Guarda los usuarios en la persistencia principal (la base de datos) y mantiene una copia de respaldo en el archivo JSON después de cada cambio.
//Las consultas siempre se hacen sobre la persistencia principal.
 
public class UsuarioConRespaldo implements IGestionUsuario {

    private final IGestionUsuario principal;
    private final JsonUsuario respaldo;

    public UsuarioConRespaldo(IGestionUsuario principal, JsonUsuario respaldo) {
        this.principal = principal;
        this.respaldo = respaldo;
        respaldar();
    }

    @Override
    public void guardar(Usuario usuario) {
        principal.guardar(usuario);
        respaldar();
    }

    @Override
    public void actualizar(String correoAnterior, Usuario usuario) {
        principal.actualizar(correoAnterior, usuario);
        respaldar();
    }

    @Override
    public void eliminar(String correo) {
        principal.eliminar(correo);
        respaldar();
    }

    @Override
    public Usuario buscar(String correo) {
        return principal.buscar(correo);
    }

    @Override
    public List<Usuario> listar() {
        return principal.listar();
    }

    private void respaldar() {
        respaldo.reemplazarTodos(principal.listar());
    }
}
