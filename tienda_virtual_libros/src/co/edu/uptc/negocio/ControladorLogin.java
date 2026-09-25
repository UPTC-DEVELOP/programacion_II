package co.edu.uptc.negocio;

import co.edu.uptc.gui.PanelLogin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

/**
 * Controlador encargado de gestionar las validaciones y los eventos
 * del formulario de inicio de sesión.
 * 
 * @author Brayan Javier Panqueva Pelayo
 * @version 1.0 - Septiembre 2026
 */
public class ControladorLogin implements ActionListener {

    private final PanelLogin vista;
    private final Runnable accionExito;

    /*
     * Expresiones regulares para la validación:
     * - EMAIL: Garantiza que termine en '@gmail.com'
     * - PASSWORD_: Verifica al menos 8 caracteres, 1 mayúscula, 1 número y 1 carácter especial
     */
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@gmail\\.com$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._\\-#])[A-Za-z\\d@$!%*?&._\\-#]{8,}$";

    public ControladorLogin(PanelLogin vista, Runnable accionExito) {
        this.vista = vista;
        this.accionExito = accionExito;
       this.vista.registrarEvento(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (PanelLogin.INGRESAR.equals(comando)) {
            procesarAutenticacion();
        } else if (PanelLogin.OLVIDO.equals(comando)) {
            vista.mostrarEstado("Opción de recuperación no disponible.");
        } else if (PanelLogin.REGISTRARSE.equals(comando)) {
            vista.mostrarEstado("Opción de registro no disponible.");
        }
    }

    /*
     * Método que realiza el flujo de validaciones de credenciales y perfil
     */
    private void procesarAutenticacion() {
        String correo = vista.getCorreo();
        String clave = vista.getClave();

        // Validar correo institucional/gmail
        if (!validarCorreo(correo)) {
            vista.mostrarEstado("Correo inválido (debe terminar en @gmail.com)");
            return;
        }

        //Validar complejidad de contraseña
        if (!validarContrasenia(clave)) {
            vista.mostrarEstado("Clave débil: Mín. 8 caracteres, 1 mayúscula, 1 número y 1 especial");
            return;
        }

        //  Validar perfil seleccionado
        if (!vista.esPerfilAdmin()) {
            vista.mostrarEstado("Acceso denegado: Se requiere perfil Admin");
            return;
        }

        // Si pasa todas las validaciones exitosamente
        vista.mostrarEstado("Autenticación exitosa");
        if (accionExito != null) {
            accionExito.run();
        }
    }

    /*
     * Comprueba si el correo cumple con el dominio @gmail.com
     */
    public boolean validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(EMAIL_REGEX, correo.trim());
    }

    /*
     * Comprueba si la contraseña reúne los requisitos de seguridad:
     * - Mínimo 8 caracteres
     * - Al menos 1 letra mayúscula
     * - Al menos 1 número
     * - Al menos 1 carácter especial (ej. @, $, !, %, *, ?, &, ., _, -, #)
     */
    public boolean validarContrasenia(String clave) {
        if (clave == null || clave.isEmpty()) {
            return false;
        }
        return Pattern.matches(PASSWORD_REGEX, clave);
    }
}