package co.edu.uptc.gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import javax.swing.JPanel;

/**
 * Controlador de login y registro con restricción estricta de roles 
 * (Cliente / Administrador).
 * 
 * @author Brayan Javier Panqueva Pelayo
 * @version 1.0 - Septiembre 2026
 */
public class EventoLogin implements ActionListener {

    private final PanelLogin vistaLogin;
    private final PanelRegistro vistaRegistro;
    private final CardLayout cardLayout;
    private final JPanel panelContenedor;
    private final Consumer<String> accionExito; // Devuelve "Administrador" o "Cliente"
//constantes de expresiones regulares para validar correo y contraseña
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._\\-#])[A-Za-z\\d@$!%*?&._\\-#]{8,}$";

    // Guardado en memoria: Correo -> Rol ("Administrador" o "Cliente")
    private final Map<String, String> usuariosRoles;

    public EventoLogin(PanelLogin vistaLogin, PanelRegistro vistaRegistro, CardLayout cardLayout, JPanel panelContenedor, Consumer<String> accionExito) {
        this.vistaLogin = vistaLogin;
        this.vistaRegistro = vistaRegistro;
        this.cardLayout = cardLayout;
        this.panelContenedor = panelContenedor;
        this.accionExito = accionExito;

        this.usuariosRoles = new HashMap<>();

        // Usuarios predeterminados de prueba
        this.usuariosRoles.put("admin@uptc.edu.co", "Administrador");
        this.usuariosRoles.put("usuario@uptc.edu.co", "Cliente");

        this.vistaLogin.registrarEvento(this);
        this.vistaRegistro.registrarEvento(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (PanelLogin.INGRESAR.equals(comando)) {
            procesarAutenticacion();
        } else if (PanelLogin.OLVIDO.equals(comando)) {
            vistaLogin.mostrarEstado("Opción de recuperación no disponible.");
        } else if (PanelLogin.REGISTRARSE.equals(comando)) {
            vistaLogin.mostrarEstado("");
            cardLayout.show(panelContenedor, "VISTA_REGISTRO");
        } 
        else if (PanelRegistro.REGISTRAR.equals(comando)) {
            procesarRegistro();
        } else if (PanelRegistro.CANCELAR.equals(comando)) {
            vistaRegistro.limpiarCampos();
            cardLayout.show(panelContenedor, "VISTA_LOGIN");
        }
    }

    private void procesarAutenticacion() {
        String correo = vistaLogin.getCorreo().toLowerCase();
        String clave = vistaLogin.getClave();
        boolean intentaEntrarComoAdmin = vistaLogin.esPerfilAdmin();

        if (!validarCorreo(correo)) {
            vistaLogin.mostrarEstado("Correo inválido revise el formato del correo e intentelo nuevamente.");
            return;
        }

        if (!usuariosRoles.containsKey(correo)) {
            vistaLogin.mostrarEstado("Acceso denegado: El usuario no está registrado.");
            return;
        }

        if (!validarContrasenia(clave)) {
            vistaLogin.mostrarEstado("Clave débil deve tener Mín. 8 caracteres, 1 mayúscula, 1 número y 1 especial.");
            return;
        }

        String rolRegistrado = usuariosRoles.get(correo);

        // CONTROL EXCLUSIVO DE ACCESO POR ROL:
        if (intentaEntrarComoAdmin && !"Administrador".equalsIgnoreCase(rolRegistrado)) {
            vistaLogin.mostrarEstado("Acceso denegado: No tiene permisos de Administrador.");
            return;
        }

        if (!intentaEntrarComoAdmin && "Administrador".equalsIgnoreCase(rolRegistrado)) {
            vistaLogin.mostrarEstado("Su cuenta es Administrador. Seleccione el perfil 'Admin'.");
            return;
        }

        vistaLogin.mostrarEstado("Autenticación exitosa");
        if (accionExito != null) {
            accionExito.accept(rolRegistrado);
        }
    }

    private void procesarRegistro() {
        String correo = vistaRegistro.getCorreo();
        String contrasenia = vistaRegistro.getContrasenia();
        String tipoUsuarioSeleccionado = vistaRegistro.getTipoUsuario();

        if (!validarCorreo(correo)) {
            vistaRegistro.mostrarEstado("Correo inválido. Ejemplo: usuario@dominio.com");
            return;
        }

        if (!validarContrasenia(contrasenia)) {
            vistaRegistro.mostrarEstado("Clave requerida: 8 caracteres, 1 mayúscula, 1 número y 1 especial.");
            return;
        }

        String correoBusqueda = correo.toLowerCase();
        if (usuariosRoles.containsKey(correoBusqueda)) {
            vistaRegistro.mostrarEstado("Este correo ya se encuentra registrado.");
            return;
        }

        // Registrar correo con su respectivo rol
        usuariosRoles.put(correoBusqueda, tipoUsuarioSeleccionado);

        vistaRegistro.limpiarCampos();
        cardLayout.show(panelContenedor, "VISTA_LOGIN");
        vistaLogin.mostrarEstado("¡Registrado como " + tipoUsuarioSeleccionado + "! Puede ingresar.");
    }

    public boolean validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(EMAIL_REGEX, correo.trim());
    }

    public boolean validarContrasenia(String clave) {
        if (clave == null || clave.isEmpty()) {
            return false;
        }
        return Pattern.matches(PASSWORD_REGEX, clave);
    }
}