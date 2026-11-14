package co.edu.uptc.gui;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelBotones extends JPanel {

    private static final long serialVersionUID = 1L;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    public PanelBotones() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(0, 3, 6, 6));
        setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        btnNuevo = crearBoton("Nuevo", Comandos.NUEVO);
        btnGuardar = crearBoton("Guardar", Comandos.GUARDAR);
        btnActualizar = crearBoton("Actualizar", Comandos.ACTUALIZAR);
        btnEliminar = crearBoton("Eliminar", Comandos.ELIMINAR);
        btnLimpiar = crearBoton("Limpiar", Comandos.LIMPIAR);

        add(btnNuevo);
        add(btnGuardar);
        add(btnActualizar);
        add(btnEliminar);
        add(btnLimpiar);

        setModoEdicion(false);
    }

    private JButton crearBoton(String texto, String comando) {
        JButton boton = new JButton(texto);
        boton.setActionCommand(comando); // identifica el origen en actionPerformed
        return boton;
    }

    public void agregarListener(ActionListener listener) {
        btnNuevo.addActionListener(listener);
        btnGuardar.addActionListener(listener);
        btnActualizar.addActionListener(listener);
        btnEliminar.addActionListener(listener);
        btnLimpiar.addActionListener(listener);
    }

    public void setModoEdicion(boolean edicion) {
        btnGuardar.setEnabled(!edicion);
        btnActualizar.setEnabled(edicion);
        btnEliminar.setEnabled(edicion);
    }
}

