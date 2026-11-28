package com.letsread.main;

import com.letsread.gui.VentanaPrincipal;
import com.letsread.persistence.LibroJsonRepositorio;
import com.letsread.persistence.LibroRepositorio;
import com.letsread.repository.ClienteJsonRepositorio;
import com.letsread.repository.ClienteRepositorio;
import com.letsread.service.CatalogoInicial;
import com.letsread.service.ClienteService;
import com.letsread.service.LibroService;

import javax.swing.SwingUtilities;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        // Aquí se decide la implementación (inyección de dependencias):
        // para usar MariaDB basta con cambiar por: new ClienteBdRepositorio() y new LibroBdRepositorio()
        ClienteRepositorio repositorio = new ClienteJsonRepositorio(Path.of("clientes_letsread.json"));
        ClienteService clienteService = new ClienteService(repositorio);

        LibroRepositorio libroRepositorio = new LibroJsonRepositorio();
        LibroService libroService = new LibroService(libroRepositorio);
        libroService.sembrarSiVacio(CatalogoInicial.obtener());

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(clienteService, libroService);
            ventana.setVisible(true);
        });
    }
}