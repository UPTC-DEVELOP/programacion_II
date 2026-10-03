package controlador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import excepciones.ExcepcionValidacion;
import modelo.RepositorioUsuarios;
import modelo.Usuario;
import vista.VistaCliente;
public class ControladorCliente implements ActionListener {
    private VistaCliente vista;
    private RepositorioUsuarios repositorio;
    public ControladorCliente(VistaCliente vista,
            RepositorioUsuarios repositorio) {
        this.vista = vista;
        this.repositorio = repositorio;
        vista.getBotonBuscar().addActionListener(this);
        vista.getBotonActualizar().addActionListener(this);
        vista.getBotonEliminar().addActionListener(this);
        vista.getBotonListar().addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == vista.getBotonBuscar()) {
            buscarUsuario();
        } else if (evento.getSource() == vista.getBotonActualizar()) {
            actualizarUsuario();
        } else if (evento.getSource() == vista.getBotonEliminar()) {
            eliminarUsuario();
        } else if (evento.getSource() == vista.getBotonListar()) {
            listarUsuarios();
        }
    }
    private void buscarUsuario() {
        Usuario usuario = repositorio.buscarUsuario(
                vista.getCorreo());
        if (usuario == null) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Usuario no encontrado.");
            return;
        }
        vista.setNombre(usuario.getNombre());
        vista.setDireccion(usuario.getDireccion());
        vista.setTelefono(usuario.getTelefono());
        vista.setTipoCliente(usuario.getTipoCliente());
    }
    private void actualizarUsuario() {
        try {
            Usuario usuarioActualizado =
                    new Usuario(
                            vista.getNombre(),
                            vista.getCorreo(),
                            vista.getDireccion(),
                            vista.getTelefono(),
                            vista.getTipoCliente(),
                            "Temporal123");
            repositorio.actualizarUsuario(
                    usuarioActualizado);
            JOptionPane.showMessageDialog(
                    vista,
                    "Usuario actualizado correctamente.");
        } catch (ExcepcionValidacion e) {
            JOptionPane.showMessageDialog(
                    vista,
                    e.getMessage());
        }
    }
    private void eliminarUsuario() {
        try {
            int opcion = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Desea eliminar este usuario?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                repositorio.eliminarUsuario(
                        vista.getCorreo());
                JOptionPane.showMessageDialog(
                        vista,
                        "Usuario eliminado correctamente.");
            }
        } catch (ExcepcionValidacion e) {
            JOptionPane.showMessageDialog(
                    vista,
                    e.getMessage());
        }
    }
    private void listarUsuarios() {
        StringBuilder listado = new StringBuilder();
        for (Usuario usuario :
                repositorio.listarUsuarios()) {
            listado.append("Nombre: ")
                    .append(usuario.getNombre())
                    .append("\nCorreo: ")
                    .append(usuario.getCorreo())
                    .append("\nTipo Cliente: ")
                    .append(usuario.getTipoCliente())
                    .append("\n\n");
        }
        if (listado.length() == 0) {
            listado.append(
                    "No hay usuarios registrados.");
        }
        JOptionPane.showMessageDialog(
                vista,
                listado.toString(),
                "Listado de Usuarios",
                JOptionPane.INFORMATION_MESSAGE);
    }
}