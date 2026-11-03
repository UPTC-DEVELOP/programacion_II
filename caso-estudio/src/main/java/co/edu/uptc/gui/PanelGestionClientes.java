package co.edu.uptc.gui;

import co.edu.uptc.modelo.dto.ClienteDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel para la gestión (CRUD) de clientes.
 *
 * PRINCIPIO SOLID:
 * - SRP: Solo se encarga de mostrar la interfaz y capturar eventos del usuario.
 * - DIP: Depende de la abstracción ActionListener, no de una clase de negocio concreta.
 *
 * @author Grupo 7 - UPTC
 * @version 1.0
 */
public class PanelGestionClientes extends JPanel {

    private static final long serialVersionUID = 1L;

    // Componentes de la interfaz
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnVolver;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    // Copia de los DTOs que se están mostrando (permite mapear la fila -> DTO)
    private List<ClienteDto> clientesMostrados = new ArrayList<>();

    // Listener para manejar los eventos (Clase Evento)
    private final ActionListener manejadorEventos;

    // Comandos de acción (Deben coincidir con los de la clase Evento.java)
    public static final String CMD_BUSCAR = "BUSCAR_CLIENTE";
    public static final String CMD_NUEVO = "NUEVO_CLIENTE";
    public static final String CMD_EDITAR = "EDITAR_CLIENTE";
    public static final String CMD_ELIMINAR = "ELIMINAR_CLIENTE";
    public static final String CMD_VOLVER = "DASHBOARD";

    /**
     * Constructor del panel.
     * @param manejadorEventos Objeto que escuchará los eventos de este panel (Clase Evento).
     */
    public PanelGestionClientes(ActionListener manejadorEventos) {
        this.manejadorEventos = manejadorEventos;
        inicializarComponentes();
        configurarLayout();
        registrarEventos();
    }

    private void inicializarComponentes() {
        // Campos de búsqueda
        txtBusqueda = new CampoConHint("Nombre, correo o identificación", 20);
        btnBuscar = new JButton("Buscar");

        // Botones de acción
        btnNuevo = new JButton("Nuevo Cliente");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnVolver = new JButton("Volver al panel");

        // Configuración de la tabla
        String[] columnas = {"ID", "Nombre", "Identificación", "Correo", "Tipo", "Rol"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evita que el usuario edite celdas directamente en la tabla
            }
        };
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo una fila a la vez
    }

    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Panel Superior (Búsqueda y Acciones) ---
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Buscar por nombre o correo:"));
        panelSuperior.add(txtBusqueda);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(Box.createHorizontalStrut(30)); // Espacio
        panelSuperior.add(btnNuevo);
        panelSuperior.add(btnEditar);
        panelSuperior.add(btnEliminar);
        panelSuperior.add(Box.createHorizontalStrut(30));
        panelSuperior.add(btnVolver);

        add(panelSuperior, BorderLayout.NORTH);

        // --- Panel Central (Tabla) ---
        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Registra los ActionListeners y define los ActionCommands.
     * PRINCIPIO: La GUI no decide qué hacer, solo notifica mediante comandos.
     */
    private void registrarEventos() {
        btnBuscar.setActionCommand(CMD_BUSCAR);
        btnBuscar.addActionListener(manejadorEventos);

        btnNuevo.setActionCommand(CMD_NUEVO);
        btnNuevo.addActionListener(manejadorEventos);

        btnEditar.setActionCommand(CMD_EDITAR);
        btnEditar.addActionListener(manejadorEventos);

        btnEliminar.setActionCommand(CMD_ELIMINAR);
        btnEliminar.addActionListener(manejadorEventos);

        btnVolver.setActionCommand(CMD_VOLVER);
        btnVolver.addActionListener(manejadorEventos);
    }

    // --- MÉTODOS PÚBLICOS PARA OBTENER DATOS (Usados por el Controlador/Evento) ---

    public String getTextoBusqueda() {
        return txtBusqueda.getText().trim();
    }

    /**
     * Devuelve el DTO completo del cliente seleccionado en la tabla.
     * @return DTO del cliente o null si no hay selección.
     */
    public ClienteDto getClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1 || filaSeleccionada >= clientesMostrados.size()) {
            return null; // No hay fila seleccionada
        }
        return clientesMostrados.get(filaSeleccionada);
    }

    /**
     * Actualiza la tabla con una lista de clientes.
     * @param clientes Lista de DTOs a mostrar.
     */
    public void actualizarTabla(List<ClienteDto> clientes) {
        modeloTabla.setRowCount(0); // Limpiar tabla actual
        clientesMostrados = (clientes == null) ? new ArrayList<>() : new ArrayList<>(clientes);

        for (ClienteDto cliente : clientesMostrados) {
            modeloTabla.addRow(new Object[]{
                    cliente.getIdCliente(),
                    cliente.getNombreCompleto(),
                    cliente.getIdentificacion(),
                    cliente.getCorreoElectronico(),
                    cliente.getTipoCliente() == null ? "" : cliente.getTipoCliente().name(),
                    cliente.getRol() == null ? "" : cliente.getRol().name()
            });
        }
    }
}
