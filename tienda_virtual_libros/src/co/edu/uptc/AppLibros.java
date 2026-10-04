package co.edu.uptc;

import co.edu.uptc.gui.VentanaPrincipal;
import co.edu.uptc.negocio.GestionCliente;
import co.edu.uptc.negocio.GestionLibro;
import co.edu.uptc.negocio.GestionSeguridad;
import co.edu.uptc.negocio.ValidadorDatos;
import co.edu.uptc.persistencia.LocalCliente;
import co.edu.uptc.persistencia.LocalLibro;

/**
 * Punto de entrada de la Tienda Virtual de Libros.
 * Arquitectura multicapa con inyección de dependencias:
 *
 *   Persistencia  -> LocalCliente / LocalLibro (detrás de sus interfaces)
 *   Validación    -> ValidadorDatos
 *   Negocio       -> GestionSeguridad / GestionCliente / GestionLibro
 *   Presentación  -> VentanaPrincipal (Swing)
 */
public class AppLibros {
    public static void main(String[] args) {
        // 1. Capa de Persistencia (se inyecta en la capa de negocio)
        LocalLibro repositorioLibro = new LocalLibro();
        LocalCliente repositorioCliente = new LocalCliente();

        // 2. Capa de Validación
        ValidadorDatos validador = new ValidadorDatos();

        // 3. Capa de Negocio (Inyección de Dependencias)
        GestionSeguridad gestionSeguridad = new GestionSeguridad(repositorioCliente);
        GestionLibro gestionLibro = new GestionLibro(repositorioLibro, validador);
        GestionCliente gestionCliente = new GestionCliente(repositorioCliente, validador);

        // 4. Capa de Presentación (GUI)
        java.awt.EventQueue.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(gestionSeguridad, gestionCliente, gestionLibro);
            ventana.setVisible(true);
        });
    }
}
