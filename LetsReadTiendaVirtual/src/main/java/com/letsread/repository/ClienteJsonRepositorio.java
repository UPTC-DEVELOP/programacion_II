package com.letsread.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.letsread.model.Cliente;
import com.letsread.model.ClienteFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Guarda los clientes en un archivo JSON usando Gson. */
public class ClienteJsonRepositorio implements ClienteRepositorio {

    /** Objeto plano que Gson sabe leer/escribir (Cliente es abstracta). */
    private static class ClienteDto {
        String tipo;
        String nombreCompleto;
        String correo;
        String direccion;
        String telefono;
        String clave;
    }

    private static final Type TIPO_LISTA = new TypeToken<List<ClienteDto>>() { }.getType();

    private final Path archivo;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ClienteJsonRepositorio(Path archivo) {
        this.archivo = archivo;
    }

    @Override
    public void guardar(Cliente cliente) {
        List<Cliente> todos = listar();
        for (Cliente c : todos) {
            if (c.getCorreo().equalsIgnoreCase(cliente.getCorreo())) {
                throw new IllegalArgumentException("Ya existe un cliente con el correo " + cliente.getCorreo());
            }
        }
        todos.add(cliente);
        escribir(todos);
    }

    @Override
    public Optional<Cliente> buscarPorCorreo(String correo) {
        for (Cliente c : listar()) {
            if (c.getCorreo().equalsIgnoreCase(correo)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Cliente> listar() {
        if (!Files.exists(archivo)) {
            return new ArrayList<>();
        }
        try (Reader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            List<ClienteDto> dtos = gson.fromJson(reader, TIPO_LISTA);
            List<Cliente> clientes = new ArrayList<>();
            if (dtos != null) {
                for (ClienteDto d : dtos) {
                    clientes.add(ClienteFactory.crear(d.tipo, d.nombreCompleto, d.correo,
                            d.direccion, d.telefono, d.clave));
                }
            }
            return clientes;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer " + archivo + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Cliente cliente) {
        List<Cliente> todos = listar();
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getCorreo().equalsIgnoreCase(cliente.getCorreo())) {
                todos.set(i, cliente);
                escribir(todos);
                return;
            }
        }
        throw new IllegalArgumentException("No existe un cliente con el correo " + cliente.getCorreo());
    }

    @Override
    public boolean eliminar(String correo) {
        List<Cliente> todos = listar();
        boolean eliminado = todos.removeIf(c -> c.getCorreo().equalsIgnoreCase(correo));
        if (eliminado) {
            escribir(todos);
        }
        return eliminado;
    }

    private void escribir(List<Cliente> clientes) {
        List<ClienteDto> dtos = new ArrayList<>();
        for (Cliente c : clientes) {
            ClienteDto d = new ClienteDto();
            d.tipo = c.getTipo();
            d.nombreCompleto = c.getNombreCompleto();
            d.correo = c.getCorreo();
            d.direccion = c.getDireccion();
            d.telefono = c.getTelefono();
            d.clave = c.getClave();
            dtos.add(d);
        }
        try (Writer writer = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            gson.toJson(dtos, TIPO_LISTA, writer);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo escribir " + archivo + ": " + e.getMessage(), e);
        }
    }
}