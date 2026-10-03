package co.edu.uptc.gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.function.Consumer;

import javax.swing.JPanel;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.interfaces.IGestionAcceso;
import co.edu.uptc.modelo.Administrador;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.ClientePremium;
import co.edu.uptc.modelo.ClienteRegular;
import co.edu.uptc.modelo.Rol;
import co.edu.uptc.modelo.TipoCliente;

/**
 * Controlador de login y registro con restricción estricta de roles
 * (Cliente / Administrador).
 *
 * Solo traduce lo que hay en la vista y muestra el resultado: las reglas
 * (credenciales, roles, duplicados, formatos) viven en la capa de negocio,
 * a la que se accede por el contrato IGestionAcceso (SRP / DIP).
 *
 * @author Brayan Javier Panqueva Pelayo
 * @version 1.1 - Octubre 2026
 */
public class EventoLogin implements ActionListener {

    private static final String TIPO_CLIENTE_VIP = "cliente VIP";
    private static final String TIPO_ADMINISTRADOR = "Administrador";
    private static final String TIPO_IDENTIFICACION_POR_DEFECTO = "CC";

    private final PanelLogin vistaLogin;
    private final PanelRegistro vistaRegistro;
    private final CardLayout cardLayout;
    private final JPanel panelContenedor;
    private final IGestionAcceso gestionAcceso;
    private final Consumer<Rol> accionExito;

    public EventoLogin(PanelLogin vistaLogin, PanelRegistro vistaRegistro, CardLayout cardLayout,
            JPanel panelContenedor, IGestionAcceso gestionAcceso, Consumer<Rol> accionExito) {
        this.vistaLogin = vistaLogin;
        this.vistaRegistro = vistaRegistro;
        this.cardLayout = cardLayout;
        this.panelContenedor = panelContenedor;
        this.gestionAcceso = gestionAcceso;
        this.accionExito = accionExito;

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
        Rol perfil = vistaLogin.esPerfilAdmin() ? Rol.ADMINISTRADOR : Rol.CLIENTE;
        try {
            Rol rol = gestionAcceso.autenticar(vistaLogin.getCorreo(), vistaLogin.getClave(), perfil);
            vistaLogin.mostrarEstado("Autenticación exitosa");
            if (accionExito != null) {
                accionExito.accept(rol);
            }
        } catch (ReglaNegocioException ex) {
            vistaLogin.limpiarClave();
            vistaLogin.mostrarEstado(ex.getMessage());
        }
    }

    private void procesarRegistro() {
        String tipoUsuario = vistaRegistro.getTipoUsuario();
        try {
            if (TIPO_ADMINISTRADOR.equals(tipoUsuario)) {
                gestionAcceso.registrarAdministrador(crearAdministrador());
            } else {
                gestionAcceso.registrarCliente(crearCliente(TIPO_CLIENTE_VIP.equals(tipoUsuario)));
            }
        } catch (ReglaNegocioException ex) {
            vistaRegistro.mostrarEstado(ex.getMessage());
            return;
        }

        vistaRegistro.limpiarCampos();
        cardLayout.show(panelContenedor, "VISTA_LOGIN");
        vistaLogin.mostrarEstado("¡Registrado como " + tipoUsuario + "! Puede ingresar.");
    }

    private Cliente crearCliente(boolean esVip) {
        String[] nombre = separarNombre(vistaRegistro.getNombreUsuario());
        if (esVip) {
            return new ClientePremium(nombre[0], nombre[1], nombre[2], nombre[3],
                    TIPO_IDENTIFICACION_POR_DEFECTO, vistaRegistro.getIdentificacion(), vistaRegistro.getCorreo(),
                    vistaRegistro.getCelular(), vistaRegistro.getDireccion(), 0, TipoCliente.PREMIUM,
                    vistaRegistro.getContrasenia(), null, 0);
        }
        return new ClienteRegular(nombre[0], nombre[1], nombre[2], nombre[3],
                TIPO_IDENTIFICACION_POR_DEFECTO, vistaRegistro.getIdentificacion(), vistaRegistro.getCorreo(),
                vistaRegistro.getCelular(), vistaRegistro.getDireccion(), 0, TipoCliente.REGULAR,
                vistaRegistro.getContrasenia(), null, 0);
    }

    private Administrador crearAdministrador() {
        String[] nombre = separarNombre(vistaRegistro.getNombreUsuario());
        return new Administrador(nombre[0], nombre[1], nombre[2], nombre[3],
                TIPO_IDENTIFICACION_POR_DEFECTO, vistaRegistro.getIdentificacion(), vistaRegistro.getCorreo(),
                vistaRegistro.getCelular(), vistaRegistro.getDireccion(), vistaRegistro.getContrasenia());
    }

    /**
     * El formulario tiene un solo campo de nombre. Se reparte así:
     * "Ana Ruiz" -> nombre + apellido; "Ana Ruiz Gil" -> nombre + 2 apellidos;
     * "Ana María Ruiz Gil" -> los dos últimos son apellidos y el resto nombres.
     * @return {primerNombre, otrosNombres, primerApellido, otrosApellidos}
     */
    private static String[] separarNombre(String completo) {
        String[] partes = completo.trim().isEmpty() ? new String[0] : completo.trim().split("\\s+");
        switch (partes.length) {
            case 0:  return new String[] { "", "", "", "" };
            case 1:  return new String[] { partes[0], "", "", "" };
            case 2:  return new String[] { partes[0], "", partes[1], "" };
            case 3:  return new String[] { partes[0], "", partes[1], partes[2] };
            default:
                int n = partes.length;
                String otrosNombres = String.join(" ", Arrays.copyOfRange(partes, 1, n - 2));
                return new String[] { partes[0], otrosNombres, partes[n - 2], partes[n - 1] };
        }
    }
}
