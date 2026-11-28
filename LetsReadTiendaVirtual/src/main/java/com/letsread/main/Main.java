package com.letsread.main;

import com.letsread.gui.VentanaPrincipal;
import com.letsread.repository.ClienteJsonRepositorio;
import com.letsread.repository.ClienteRepositorio;
import com.letsread.service.ClienteService;

import javax.swing.SwingUtilities;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        // Aquí se decide la implementación (inyección de dependencias):
        // para usar MariaDB basta con cambiar por: new ClienteBdRepositorio()
        ClienteRepositorio repositorio = new ClienteJsonRepositorio(Path.of("clientes_letsread.json"));
        ClienteService clienteService = new ClienteService(repositorio);

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(clienteService);
            ventana.setVisible(true);
        });
    }
}