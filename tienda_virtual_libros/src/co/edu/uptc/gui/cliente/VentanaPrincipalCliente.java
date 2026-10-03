package co.edu.uptc.gui.cliente;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import co.edu.uptc.interfaces.IGestionCliente;

/**
 * Ventana que ve el CLIENTE despues de iniciar sesion:
 *   NORTH  -> saludo + boton "Cerrar sesión"
 *   CENTER -> PanelCentral (CRUD de clientes)
 */
public class VentanaPrincipalCliente extends JFrame {

	private static final long serialVersionUID = 1L;

	private PanelCentral panelCentral;

	//Constructor
	public VentanaPrincipalCliente(String usuario, IGestionCliente gestionCliente, Runnable alCerrarSesion) {


	    setTitle("Sistema de Clientes");
	    setSize(900, 600);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //CENTRADA
	    setLocationRelativeTo(null);

	    JPanel barraSesion = new JPanel(new BorderLayout());
	    barraSesion.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
	    barraSesion.add(new JLabel("Bienvenido(a), " + usuario), BorderLayout.WEST);

	    JButton btnCerrarSesion = new JButton("Cerrar sesión");
	    btnCerrarSesion.addActionListener(e -> {
	        dispose();
	        alCerrarSesion.run();
	    });
	    barraSesion.add(btnCerrarSesion, BorderLayout.EAST);

	    panelCentral = new PanelCentral(gestionCliente);

	    add(barraSesion, BorderLayout.NORTH);
	    add(panelCentral, BorderLayout.CENTER);
	}
}
