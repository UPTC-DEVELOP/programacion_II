package controlador;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import excepciones.ExcepcionValidacion;
import modelo.Libro;
import modelo.RepositorioLibros;
import modelo.RepositorioCarrito;
import modelo.RepositorioUsuarios;
import modelo.RepositorioProductos;
import vista.VistaCliente;
import vista.VistaCarrito;
import vista.VistaInicio;
import vista.VistaLogin;
import vista.VistaEvaluacionParte2;
public class ControladorInicio implements ActionListener {
    private VistaInicio vista;
    private VistaLogin login;
    private RepositorioLibros repositorio;
    private RepositorioCarrito repositorioCarrito;
    public ControladorInicio(VistaInicio vista, VistaLogin login,
            RepositorioLibros repositorio) {
        this.vista = vista;
        this.login = login;
        this.repositorio = repositorio;
        this.repositorioCarrito = new RepositorioCarrito();
        vista.getBotonAnadirLibro().addActionListener(this);
        vista.getBotonQuitarLibro().addActionListener(this);
        vista.getBotonGestionarClientes().addActionListener(this);
        vista.getBotonCarrito().addActionListener(this);
        vista.getBotonEvaluacionParte2().addActionListener(this);
        vista.getBotonCerrarSesion().addActionListener(this);
        actualizarTabla();
    }
    @Override
    public void actionPerformed(ActionEvent evento) {
        Object origen = evento.getSource();
        if (origen == vista.getBotonAnadirLibro()) {
            anadirLibro();
        } else if (origen == vista.getBotonQuitarLibro()) {
            quitarLibro();
        } else if (origen == vista.getBotonGestionarClientes()) {
            abrirGestionClientes();
        } else if (origen == vista.getBotonCarrito()) {
            abrirCarrito();
        } else if (origen == vista.getBotonEvaluacionParte2()) {
            abrirEvaluacionParte2();
        } else if (origen == vista.getBotonCerrarSesion()) {
            cerrarSesion();
        }
    }
    private void abrirCarrito() {
        VistaCarrito vistaCarrito = new VistaCarrito();
        new ControladorCarrito(vistaCarrito, repositorioCarrito, repositorio.getLibros());
        vistaCarrito.setVisible(true);
    }

    private void abrirEvaluacionParte2() {
        VistaEvaluacionParte2 vistaEvaluacion = new VistaEvaluacionParte2();
        RepositorioProductos repositorioProductos = new RepositorioProductos();
        new ControladorEvaluacionParte2(vistaEvaluacion, repositorioProductos);
        vistaEvaluacion.setVisible(true);
    }

    private void abrirGestionClientes() {
        VistaCliente vistaCliente = new VistaCliente();
        RepositorioUsuarios repositorioUsuarios =
                new RepositorioUsuarios();
        new ControladorCliente(
                vistaCliente,
                repositorioUsuarios);
        vistaCliente.setVisible(true);
    }
    private void anadirLibro() {
        JTextField campoTitulo = new JTextField(20);
        JTextField campoAutor = new JTextField(20);
        JComboBox<String> comboFormato =
                new JComboBox<String>(
                        new String[] { "Físico", "Digital" });
        JTextField campoPrecio = new JTextField(20);
        JPanel formulario =
                new JPanel(new GridLayout(4, 2, 8, 8));
        formulario.add(new JLabel("Título:"));
        formulario.add(campoTitulo);
        formulario.add(new JLabel("Autor:"));
        formulario.add(campoAutor);
        formulario.add(new JLabel("Formato:"));
        formulario.add(comboFormato);
        formulario.add(new JLabel("Precio:"));
        formulario.add(campoPrecio);
        int opcion = JOptionPane.showConfirmDialog(
                vista,
                formulario,
                "Añadir libro",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }
        String titulo = campoTitulo.getText().trim();
        String autor = campoAutor.getText().trim();
        String formato = comboFormato.getSelectedItem().toString();
        String textoPrecio = campoPrecio.getText().trim();
        if (!textoPrecio.matches("^[0-9]+$")) {
            JOptionPane.showMessageDialog(
                    vista,
                    "El precio debe contener solo números.",
                    "Precio no válido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int precio = Integer.parseInt(textoPrecio);
            repositorio.anadirLibro(
                    new Libro(
                            titulo,
                            autor,
                            formato,
                            precio));
            actualizarTabla();
            JOptionPane.showMessageDialog(
                    vista,
                    "Libro añadido y guardado en libros.txt.",
                    "Catálogo",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    vista,
                    "El precio ingresado es demasiado grande.",
                    "Precio no válido",
                    JOptionPane.WARNING_MESSAGE);
        } catch (ExcepcionValidacion e) {
            JOptionPane.showMessageDialog(
                    vista,
                    e.getMessage(),
                    "Revise los datos",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    private void quitarLibro() {
        int fila = vista.getFilaSeleccionada();
        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Seleccione primero el libro que desea quitar.",
                    "Seleccione un libro",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(
                vista,
                "¿Desea quitar el libro seleccionado?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            try {
                repositorio.quitarLibro(fila);
                actualizarTabla();
            } catch (ExcepcionValidacion e) {
                JOptionPane.showMessageDialog(
                        vista,
                        e.getMessage(),
                        "No fue posible quitar el libro",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    private void actualizarTabla() {
        vista.limpiarLibros();
        for (Libro libro : repositorio.getLibros()) {
            vista.mostrarLibro(libro);
        }
    }
    private void cerrarSesion() {
        vista.dispose();
        login.limpiarContrasena();
        login.setVisible(true);
    }
}