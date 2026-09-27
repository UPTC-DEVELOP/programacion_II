package co.uptc.edu.gui.libro;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import co.uptc.edu.libro.modelo.Libro;
import co.uptc.edu.libro.negocio.Libreria;

public class VentanaPrincipal extends JFrame {
	
private PanelLogin pLogin;
private PanelPadreLibro pCentral;
private DialogoCentralLibro dialogoLibro;
private Evento evento;
private Libreria libreria;

public VentanaPrincipal() {
    setSize(800, 600);
    setTitle("Tienda Librería");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new CardLayout()); // Usar CardLayout es más limpio para cambiar vistas

    libreria = new Libreria();
    evento = new Evento(this);
    
    pLogin = new PanelLogin(evento);
    pCentral = new PanelPadreLibro(evento);

    add(pLogin, "LOGIN");
    add(pCentral, "CENTRAL");
}

public void loguear() {
    String usuario = pLogin.getUsuario();
    String contrasenia = pLogin.getContrasenia();

    // Validación básica (Idealmente delegada al negocio)
    if ("a".equals(usuario) && "1".equals(contrasenia)) {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "CENTRAL");
        refrescarTabla();
    } else {
        JOptionPane.showMessageDialog(this, "Usuario o contraseña inválidos");
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear el libro: Verifique los datos numéricos.");
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
            dialogoLibro.setVisible(true); // Se abre el diálogo de forma modal
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningún libro con el ISBN: " + isbn);
        }
    }
}

// 2. Método exclusivo que se ejecuta al hacer clic en el botón "Actualizar" de adentro del diálogo
public void guardarActualizacionLibro() {
    if (dialogoLibro instanceof DialogoEditarLibro) {
        DialogoEditarLibro editarDialogo = (DialogoEditarLibro) dialogoLibro;
        try {
     
            Libro libroActualizado = editarDialogo.capturarDatos();
            
            // Realiza la actualización en la capa de negocio usando el ISBN
            libreria.actualizarLibro(libroActualizado.getIsbn(), libroActualizado);
            
            cerrarDialogoLibro();
            refrescarTabla();
            JOptionPane.showMessageDialog(this, "¡Libro actualizado exitosamente!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: Verifique que los campos numéricos sean correctos.");
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
        var libro = libreria.buscarLibro(isbn);
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

public PanelPadreLibro getPanellibros() { return pCentral; }
public Libreria getLibreria() { return libreria; }

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new VentanaPrincipal().setVisible(true);
    });
}
}