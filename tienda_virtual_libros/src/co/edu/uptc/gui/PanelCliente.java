package co.edu.uptc.gui;

import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.dto.LibroDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * PANEL DEL CLIENTE (CRUD del cliente sobre su propia información).
 *
 * CREATE : desde la pantalla de login ("Registrarse").
 * READ   : pestaña "Mis datos" y catálogo de libros.
 * UPDATE : botón "Editar mis datos" (abre DialogoCliente).
 * DELETE : botón "Eliminar mi cuenta" (regresa al login).
 *
 * PRINCIPIO SOLID:
 * - SRP: Solo muestra la interfaz y notifica eventos.
 * - DIP: Depende de ActionListener, no de la capa de negocio.
 *
 * @author Grupo 7 - UPTC
 * @version 1.0
 */
public class PanelCliente extends JPanel {

    private static final long serialVersionUID = 1L;

    // Comandos de acción
    public static final String CMD_EDITAR_PERFIL = "EDITAR_MI_PERFIL";
    public static final String CMD_ELIMINAR_CUENTA = "ELIMINAR_MI_CUENTA";
    public static final String CMD_CERRAR_SESION = "CERRAR_SESION";
    public static final String CMD_BUSCAR_CATALOGO = "BUSCAR_CATALOGO";

    private JLabel lblBienvenida;
    private JTextArea areaDatos;
    private JTextField txtBusqueda;
    private JTable tablaCatalogo;
    private DefaultTableModel modeloTabla;
    private JButton btnEditarPerfil;
    private JButton btnEliminarCuenta;
    private JButton btnCerrarSesion;
    private JButton btnBuscarCatalogo;

    private List<LibroDto> librosMostrados = new ArrayList<>();
    private ClienteDto clienteActual;

    private final ActionListener manejadorEventos;

    public PanelCliente(ActionListener manejadorEventos) {
        this.manejadorEventos = manejadorEventos;
        inicializarComponentes();
        configurarLayout();
        registrarEventos();
    }

    private void inicializarComponentes() {
        lblBienvenida = new JLabel("Bienvenido", SwingConstants.LEFT);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBienvenida.setForeground(new Color(44, 62, 80));

        txtBusqueda = new CampoConHint("Buscar por título o autor", 22);

        String[] columnas = {"ISBN", "Título", "Autor", "Año", "Categoría", "Precio", "Stock"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCatalogo = new JTable(modeloTabla);
        tablaCatalogo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        areaDatos = new JTextArea();
        areaDatos.setEditable(false);
        areaDatos.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaDatos.setLineWrap(true);
        areaDatos.setWrapStyleWord(true);
        areaDatos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(236, 240, 241));

        // --- Norte: bienvenida + acciones del CRUD del cliente ---
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setOpaque(false);
        panelSuperior.add(lblBienvenida, BorderLayout.WEST);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAcciones.setOpaque(false);
        btnEditarPerfil = boton("Editar mis datos", CMD_EDITAR_PERFIL);
        btnEliminarCuenta = boton("Eliminar mi cuenta", CMD_ELIMINAR_CUENTA);
        btnCerrarSesion = boton("Cerrar sesión", CMD_CERRAR_SESION);
        panelAcciones.add(btnEditarPerfil);
        panelAcciones.add(btnEliminarCuenta);
        panelAcciones.add(btnCerrarSesion);
        panelSuperior.add(panelAcciones, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // --- Centro: pestañas ---
        JTabbedPane pestanias = new JTabbedPane();

        JPanel panelCatalogo = new JPanel(new BorderLayout(8, 8));
        JPanel barraBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barraBusqueda.add(new JLabel("Catálogo:"));
        barraBusqueda.add(txtBusqueda);
        btnBuscarCatalogo = boton("Buscar", CMD_BUSCAR_CATALOGO);
        barraBusqueda.add(btnBuscarCatalogo);
        panelCatalogo.add(barraBusqueda, BorderLayout.NORTH);
        panelCatalogo.add(new JScrollPane(tablaCatalogo), BorderLayout.CENTER);

        JScrollPane scrollDatos = new JScrollPane(areaDatos);
        scrollDatos.setBorder(BorderFactory.createEmptyBorder());

        pestanias.addTab("Catálogo de libros", panelCatalogo);
        pestanias.addTab("Mis datos", scrollDatos);
        add(pestanias, BorderLayout.CENTER);
    }

    private JButton boton(String texto, String comando) {
        JButton boton = new JButton(texto);
        boton.setActionCommand(comando);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void registrarEventos() {
        btnEditarPerfil.addActionListener(manejadorEventos);
        btnEliminarCuenta.addActionListener(manejadorEventos);
        btnCerrarSesion.addActionListener(manejadorEventos);
        btnBuscarCatalogo.addActionListener(manejadorEventos);
    }

    // ------------------------- Datos del cliente -------------------------

    /** Asigna el cliente autenticado y refresca la pestaña "Mis datos". */
    public void setCliente(ClienteDto cliente) {
        this.clienteActual = cliente;
        lblBienvenida.setText(cliente == null
                ? "Bienvenido"
                : "Bienvenido, " + cliente.getNombreCompleto());
        pintarDatos();
    }

    public ClienteDto getCliente() {
        return clienteActual;
    }

    private void pintarDatos() {
        if (clienteActual == null) {
            areaDatos.setText("");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("--------- MIS DATOS ---------\n\n");
        sb.append("ID              : ").append(clienteActual.getIdCliente()).append("\n");
        sb.append("Nombre completo : ").append(clienteActual.getNombreCompleto()).append("\n");
        sb.append("Identificación  : ").append(nulo(clienteActual.getTipoIdentificacion()))
          .append(" ").append(nulo(clienteActual.getIdentificacion())).append("\n");
        sb.append("Correo          : ").append(nulo(clienteActual.getCorreoElectronico())).append("\n");
        sb.append("Celular         : ").append(nulo(clienteActual.getCelular())).append("\n");
        sb.append("Dirección       : ").append(nulo(clienteActual.getDireccion())).append("\n");
        sb.append("Tipo de cliente : ").append(clienteActual.getTipoCliente() == null
                ? "-" : clienteActual.getTipoCliente().getNombreMostrar()).append("\n");
        sb.append("Rol             : ").append(clienteActual.getRol() == null
                ? "-" : clienteActual.getRol().getNombreMostrar()).append("\n");
        areaDatos.setText(sb.toString());
    }

    private String nulo(String valor) {
        return valor == null ? "" : valor;
    }

    // ------------------------- Catálogo -------------------------

    public String getTextoBusqueda() {
        return txtBusqueda.getText().trim();
    }

    public LibroDto getLibroSeleccionado() {
        int fila = tablaCatalogo.getSelectedRow();
        if (fila == -1 || fila >= librosMostrados.size()) {
            return null;
        }
        return librosMostrados.get(fila);
    }

    public void setLibros(List<LibroDto> libros) {
        modeloTabla.setRowCount(0);
        librosMostrados = (libros == null) ? new ArrayList<>() : new ArrayList<>(libros);
        for (LibroDto libro : librosMostrados) {
            String autores = (libro.getAutores() == null) ? "" : String.join(", ", libro.getAutores());
            modeloTabla.addRow(new Object[]{
                    libro.getIsbn(),
                    libro.getTitulo(),
                    autores,
                    libro.getAnioPublicacion(),
                    libro.getCategoria() == null ? "" : libro.getCategoria().getEtiqueta(),
                    libro.getPrecioVenta(),
                    libro.getStock()
            });
        }
    }
}
