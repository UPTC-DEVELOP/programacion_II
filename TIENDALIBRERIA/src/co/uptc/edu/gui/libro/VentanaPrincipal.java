package co.uptc.edu.gui.libro;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import co.edu.uptc.gui.DialogoCentralCliente;
import co.edu.uptc.gui.DialogoCrearCliente;
import co.edu.uptc.gui.DialogoEditarCliente;
import co.edu.uptc.gui.PanelCarrito;
import co.edu.uptc.gui.PanelPadreCliente;
import co.edu.uptc.gui.PanelPedido;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.negocio.ClienteConfig;
import co.edu.uptc.negocio.GestionCliente;
import co.uptc.edu.libro.modelo.Libro;
import co.uptc.edu.libro.negocio.Libreria;
import co.uptc.edu.libro.negocio.LibreriaException;

public class VentanaPrincipal extends JFrame {
	
private PanelLogin pLogin;
private PanelPadreLibro pCentral;
private DialogoCentralLibro dialogoLibro;
private DialogoCentralCliente dialogoCliente;
private Evento evento;
private Libreria libreria;
private PanelPadreCliente pClientes;
private GestionCliente gestionCliente;
private PanelCarrito panelCarrito;
private PanelPedido panelPedido;

public VentanaPrincipal() {
    setSize(800, 600);
    setTitle("Tienda Libreria");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new CardLayout()); // Usar CardLayout es más limpio para cambiar vistas

    libreria = new Libreria();
    evento = new Evento(this);
    
    pLogin = new PanelLogin(evento);
    pCentral = new PanelPadreLibro(evento);

    gestionCliente = ClienteConfig.getInstancia().getGestionCliente();
    pClientes = new PanelPadreCliente(evento);
    
    panelCarrito = new PanelCarrito();
    panelPedido = new PanelPedido();
    panelCarrito.setPanelPedido(panelPedido);

    JTabbedPane pestanias = new JTabbedPane();
    pestanias.addTab("Libros", pCentral);
    pestanias.addTab("Clientes", pClientes);
    pestanias.addTab("Carrito", panelCarrito);
    pestanias.addTab("Pedidos", panelPedido);

    add(pLogin, "LOGIN");
    add(pestanias, "CENTRAL");

}

public void loguear() {
    String usuario = pLogin.getUsuario();
    String contrasenia = pLogin.getContrasenia();

    // Validación básica (Idealmente delegada al negocio)
    if ("a".equals(usuario) && "1".equals(contrasenia)) {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "CENTRAL");
        refrescarTabla();
        refrescarTablaClientes();
    } else {
        JOptionPane.showMessageDialog(this, "Usuario o contraseña invalidos");
    }
}

public void lanzarDialogoLibro() {
    dialogoLibro = new DialogoCrearLibro(evento, "Crear Libro", true);
    dialogoLibro.setVisible(true);
}

public void cerrarDialogoLibro() {
    if (dialogoLibro != null) {
        dialogoLibro.dispose();
        dialogoLibro = null;
    }
}

public void crearLibro() {
    if (dialogoLibro instanceof DialogoCrearLibro) {
        DialogoCrearLibro crear = (DialogoCrearLibro) dialogoLibro;
        try {
            libreria.agregarLibro(crear.capturarDatos());
            cerrarDialogoLibro();
            refrescarTabla();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al crear el libro: Verifique los datos numéricos.");
        } catch (IllegalArgumentException | LibreriaException e) {
            JOptionPane.showMessageDialog(this, "Error al crear el libro: " + e.getMessage());
        }
    }
}

public void eliminarLibro() {
    String isbn = pCentral.getLibroSeleccionadoIsbn();
    if (isbn != null) {
        libreria.eliminarLibro(isbn);
        refrescarTabla();
    } else {
        JOptionPane.showMessageDialog(this, "Seleccione un libro para eliminar");
    }
}

public void actualizarLibro() {
    String isbn = JOptionPane.showInputDialog(this, "Ingrese el ISBN del libro que desea actualizar:");
    
    if (isbn != null && !isbn.trim().isEmpty()) {
        Libro libroEncontrado = libreria.buscarLibro(isbn.trim());
        
        if (libroEncontrado != null) {
            DialogoEditarLibro editarDialogo = new DialogoEditarLibro(evento, "Actualizar Libro", false);
            editarDialogo.cargarDatosLibro(libroEncontrado);
            
            dialogoLibro = editarDialogo;
            dialogoLibro.setVisible(true); // Se abre el dialogo de forma modal
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningun libro con el ISBN: " + isbn);
        }
    }
}

// 2. Método exclusivo que se ejecuta al hacer clic en el boton "Actualizar" de adentro del diálogo
public void guardarActualizacionLibro() {
    if (dialogoLibro instanceof DialogoEditarLibro) {
        DialogoEditarLibro editarDialogo = (DialogoEditarLibro) dialogoLibro;
        try {
     
            Libro libroActualizado = editarDialogo.capturarDatos();
            
            // Realiza la actualizacion en la capa de negocio usando el ISBN
            libreria.actualizarLibro(libroActualizado.getIsbn(), libroActualizado);
            
            cerrarDialogoLibro();
            refrescarTabla();
            JOptionPane.showMessageDialog(this, "¡Libro actualizado exitosamente!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: Verifique que los campos numéricos sean correctos.");
        } catch (IllegalArgumentException | LibreriaException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }
}

public void verLibro() {
    String isbn = pCentral.getLibroSeleccionadoIsbn();
    if (isbn != null) {
        var libro = libreria.buscarLibro(isbn);
        if (libro != null) {
            JOptionPane.showMessageDialog(this, libro.toString());
        }
    } else {
        JOptionPane.showMessageDialog(this, "Seleccione un libro para ver");
    }
}

public void buscarLibro() {
    String isbn = JOptionPane.showInputDialog(this, "Ingrese ISBN a buscar:");
    if (isbn != null && !isbn.trim().isEmpty()) {
        var libro = libreria.buscarLibro(isbn.trim());
        if (libro != null) {
            JOptionPane.showMessageDialog(this, libro.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Libro no encontrado.");
        }
    }
}

public void limpiarTablaLibros() {
    pCentral.getModelo().setRowCount(0);
}

public void refrescarTabla() {
    pCentral.poblarTabla(libreria.getListaLibros());
}

// ---------- CRUD de clientes (comandos recibidos desde Evento) ----------
public void lanzarDialogoCliente() {
    dialogoCliente = new DialogoCrearCliente(evento, "Crear Cliente", true);
    dialogoCliente.setVisible(true);
}

public void cerrarDialogoCliente() {
    if (dialogoCliente != null) {
        dialogoCliente.dispose();
        dialogoCliente = null;
    }
}

public void crearCliente() {
    if (dialogoCliente instanceof DialogoCrearCliente) {
        try {
            gestionCliente.guardarCliente(dialogoCliente.capturarDatos());
            cerrarDialogoCliente();
            refrescarTablaClientes();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error al crear el cliente: " + e.getMessage());
        }
    }
}

public void actualizarCliente() {
    String correo = pClientes.getClienteSeleccionadoCorreo();
    if (correo == null) {
        correo = JOptionPane.showInputDialog(this, "Ingrese el correo o la cédula del cliente que desea actualizar:");
    }

    if (correo != null && !correo.trim().isEmpty()) {
        Cliente clienteEncontrado = gestionCliente.buscarCliente(correo);

        if (clienteEncontrado != null) {
            DialogoEditarCliente editarDialogo = new DialogoEditarCliente(evento, "Actualizar Cliente", false);
            editarDialogo.cargarDatosCliente(clienteEncontrado);

            dialogoCliente = editarDialogo;
            dialogoCliente.setVisible(true); // Se abre el dialogo de forma modal
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningún cliente con: " + correo);
        }
    }
}

public void guardarActualizacionCliente() {
    if (dialogoCliente instanceof DialogoEditarCliente) {
        try {
            gestionCliente.actualizarCliente(dialogoCliente.capturarDatos());
            cerrarDialogoCliente();
            refrescarTablaClientes();
            JOptionPane.showMessageDialog(this, "¡Cliente actualizado exitosamente!");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }
}

public void eliminarCliente() {
    String correo = pClientes.getClienteSeleccionadoCorreo();
    if (correo == null) {
        JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar");
        return;
    }

    int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Desea eliminar al cliente con correo " + correo + "?",
            "Eliminar cliente", JOptionPane.YES_NO_OPTION);

    if (respuesta == JOptionPane.YES_OPTION) {
        gestionCliente.eliminarCliente(correo);
        refrescarTablaClientes();
    }
}

public void verCliente() {
    String correo = pClientes.getClienteSeleccionadoCorreo();
    if (correo == null) {
        JOptionPane.showMessageDialog(this, "Seleccione un cliente para ver");
        return;
    }

    Cliente cliente = gestionCliente.buscarCliente(correo);
    if (cliente != null) {
        JOptionPane.showMessageDialog(this, cliente.toString());
    }
}

public void buscarCliente() {
    String criterio = JOptionPane.showInputDialog(this, "Ingrese el correo o la cédula a buscar:");
    if (criterio != null && !criterio.trim().isEmpty()) {
        Cliente cliente = gestionCliente.buscarCliente(criterio);
        if (cliente != null) {
            pClientes.seleccionarClientePorCorreo(cliente.getCorreo());
            JOptionPane.showMessageDialog(this, cliente.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
        }
    }
}

// Restablece la tabla: quita la seleccion y vuelve a cargar los clientes

public void limpiarTablaClientes() {
    refrescarTablaClientes();
}

public void refrescarTablaClientes() {
    pClientes.poblarTabla(gestionCliente.listarClientes());
}

public PanelPadreLibro getPanellibros() { return pCentral; }
public Libreria getLibreria() { return libreria; }

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new VentanaPrincipal().setVisible(true);
    });
}
}