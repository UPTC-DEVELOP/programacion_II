package gui;

import javax.swing.SwingUtilities;

import negocio.Cliente;
import negocio.ClientePremium;
import negocio.ControladorCliente;

/**
 * Punto de entrada de la aplicacion. Abre la ventana "Iniciar Sesion".
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ControladorCliente controlador = new ControladorCliente();
                cargarDatosDePrueba(controlador);
                new VentanaLogin(controlador).setVisible(true);
            }
        });
    }

    private static void cargarDatosDePrueba(ControladorCliente controlador) {
        Cliente cliente = new ClientePremium("Juan Perez", "juan@uptc.edu.co", "1234");
        cliente.setDireccion("Cra 11 #15-20, Tunja");
        cliente.setTelefono("311 555 2233");
        controlador.registrar(cliente);
    }
}
