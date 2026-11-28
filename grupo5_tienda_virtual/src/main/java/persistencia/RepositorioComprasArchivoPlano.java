package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Compra;
import model.ItemCompra;
import model.Libro;

public class RepositorioComprasArchivoPlano implements IRepositorio<Compra> {

    private static final String RUTA_POR_DEFECTO = "datos/compras.txt";

    private final Path archivo;
    private final List<Compra> compras;

    public RepositorioComprasArchivoPlano() {
        this(RUTA_POR_DEFECTO);
    }

    public RepositorioComprasArchivoPlano(String ruta) {
        this.archivo = Path.of(ruta);
        this.compras = new ArrayList<Compra>();
    }

    @Override
    public List<Compra> listar() {
        return compras;
    }

    @Override
    public void agregar(Compra compra) {
        if (compra.getId() == 0) {
            compra.setId(siguienteId());
        }
        compras.add(compra);
    }

    @Override
    public boolean eliminar(Compra compra) {
        return compras.remove(compra);
    }

    @Override
    public void insertar(Compra compra) {
        if (compra == null) {
            return;
        }
        List<Compra> todas = leerTodas();
        if (compra.getId() == 0) {
            compra.setId(siguienteId(todas));
        }
        for (Compra existente : todas) {
            if (existente.getId() == compra.getId()) {
                throw new PersistenciaException(
                        "Id de compra duplicado: " + compra.getId());
            }
        }
        try {
            Path carpeta = archivo.getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            try (BufferedWriter escritor = Files.newBufferedWriter(archivo,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                escribirCompra(escritor, compra);
            }
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo insertar la compra en " + archivo.toAbsolutePath(), e);
        }
    }

    @Override
    public void actualizar(Compra compra) {
        if (compra == null) {
            return;
        }
        List<Compra> todas = leerTodas();
        boolean encontrada = false;
        for (int i = 0; i < todas.size(); i++) {
            if (todas.get(i).getId() == compra.getId()) {
                todas.set(i, compra);
                encontrada = true;
                break;
            }
        }
        if (encontrada) {
            escribirTodas(todas);
        }
    }

    @Override
    public boolean eliminarPersistente(Compra compra) {
        if (compra == null) {
            return false;
        }
        List<Compra> todas = leerTodas();
        boolean eliminada = todas.removeIf(c -> c.getId() == compra.getId());
        if (eliminada) {
            escribirTodas(todas);
        }
        return eliminada;
    }

    @Override
    public void cargar() {
        compras.clear();
        compras.addAll(leerTodas());
    }

    @Override
    public List<Compra> listarUltimos(int limite) {
        if (limite <= 0) {
            return new ArrayList<Compra>();
        }
        List<Compra> todas = leerTodas();
        todas.sort(Comparator.comparingLong(Compra::getId).reversed());
        int hasta = Math.min(limite, todas.size());
        return new ArrayList<Compra>(todas.subList(0, hasta));
    }

    @Override
    public List<Compra> listarPagina(int inicio, int cantidad) {
        if (inicio < 0 || cantidad <= 0) {
            return new ArrayList<Compra>();
        }
        List<Compra> todas = leerTodas();
        todas.sort(Comparator.comparingLong(Compra::getId));
        int desde = Math.min(inicio, todas.size());
        int hasta = Math.min(todas.size(), desde + cantidad);
        return new ArrayList<Compra>(todas.subList(desde, hasta));
    }

    @Override
    public int contar() {
        return leerTodas().size();
    }

    @Override
    public List<Compra> buscar(String criterio) {
        List<Compra> resultado = new ArrayList<Compra>();
        if (criterio == null || criterio.isBlank()) {
            return resultado;
        }
        for (Compra compra : leerTodas()) {
            if (criterio.equalsIgnoreCase(compra.getCorreoCliente())) {
                resultado.add(compra);
            }
        }
        resultado.sort(Comparator.comparingLong(Compra::getId).reversed());
        if (resultado.size() > 10) {
            return new ArrayList<Compra>(resultado.subList(0, 10));
        }
        return resultado;
    }

    private List<Compra> leerTodas() {
        List<Compra> leidas = new ArrayList<Compra>();
        if (!Files.exists(archivo)) {
            return leidas;
        }

        Map<Long, Compra> comprasPorId = new LinkedHashMap<Long, Compra>();

        try (BufferedReader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            int numeroLinea = 0;

            while ((linea = lector.readLine()) != null) {
                numeroLinea++;
                if (linea.isBlank()) {
                    continue;
                }

                String[] partes = linea.split("\t", -1);

                try {
                    if ("C".equals(partes[0])) {
                        if (partes.length != 6) {
                            throw new IllegalArgumentException("La cabecera debe tener 6 campos");
                        }
                        Compra compra = new Compra();
                        compra.setId(Long.parseLong(partes[1]));
                        if (!partes[2].isBlank()) {
                            compra.setFecha(LocalDate.parse(partes[2]));
                        }
                        compra.setMetodoPago(partes[3]);
                        compra.setCorreoCliente(partes[4]);
                        compra.setTotal(Double.parseDouble(partes[5]));
                        if (comprasPorId.containsKey(compra.getId())) {
                            throw new IllegalArgumentException(
                                    "Compra duplicada con id " + compra.getId());
                        }
                        comprasPorId.put(compra.getId(), compra);
                    } else if ("I".equals(partes[0])) {
                        if (partes.length != 7) {
                            throw new IllegalArgumentException("El detalle debe tener 7 campos");
                        }
                        long idCompra = Long.parseLong(partes[1]);
                        Compra compra = comprasPorId.get(idCompra);
                        if (compra == null) {
                            throw new IllegalArgumentException("No existe la compra " + idCompra);
                        }
                        Libro libro = new Libro();
                        libro.setIsbn(partes[2]);
                        libro.setTitulo(partes[3]);
                        libro.setCantidadDisponible(0);
                        libro.setPrecio(Double.parseDouble(partes[5]));
                        libro.setPorcentajeIva(Double.parseDouble(partes[6]));
                        ItemCompra item = new ItemCompra(libro, Integer.parseInt(partes[4]));
                        compra.agregarItem(item);
                    } else {
                        throw new IllegalArgumentException("Tipo de linea desconocido: " + partes[0]);
                    }
                } catch (IllegalArgumentException | DateTimeParseException e) {
                    throw new PersistenciaException(
                            "Linea " + numeroLinea + " invalida en " + archivo.toAbsolutePath(), e);
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo leer " + archivo.toAbsolutePath(), e);
        }

        leidas.addAll(comprasPorId.values());
        return leidas;
    }

    private long siguienteId() {
        return siguienteId(compras);
    }

    private long siguienteId(List<Compra> lista) {
        long maximo = 0;
        for (Compra compra : lista) {
            maximo = Math.max(maximo, compra.getId());
        }
        return maximo + 1;
    }

    @Override
    public void guardar() {
        escribirTodas(compras);
    }

    private void escribirTodas(List<Compra> lista) {
        try {
            Path carpeta = archivo.getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }

            Set<Long> ids = new HashSet<Long>();
            for (Compra compra : lista) {
                if (compra == null) {
                    continue;
                }
                if (compra.getId() == 0) {
                    compra.setId(siguienteId());
                }
                if (!ids.add(compra.getId())) {
                    throw new PersistenciaException(
                            "Id de compra duplicado: " + compra.getId());
                }
            }

            try (BufferedWriter escritor = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
                for (Compra compra : lista) {
                    if (compra != null) {
                        escribirCompra(escritor, compra);
                    }
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo escribir " + archivo.toAbsolutePath(), e);
        }
    }

    private void escribirCompra(BufferedWriter escritor, Compra compra) throws IOException {
        escribirLinea(escritor,
                "C",
                Long.toString(compra.getId()),
                compra.getFecha() != null ? compra.getFecha().toString() : "",
                limpiarCampo(compra.getMetodoPago()),
                limpiarCampo(compra.getCorreoCliente()),
                Double.toString(compra.getTotal()));

        if (compra.getItems() == null) {
            return;
        }

        for (ItemCompra item : compra.getItems()) {
            if (item == null || item.getLibro() == null) {
                continue;
            }
            Libro libro = item.getLibro();
            escribirLinea(escritor,
                    "I",
                    Long.toString(compra.getId()),
                    limpiarCampo(libro.getIsbn()),
                    limpiarCampo(libro.getTitulo()),
                    Integer.toString(item.getCantidad()),
                    Double.toString(libro.getPrecio()),
                    Double.toString(libro.getPorcentajeIva()));
        }
    }

    private void escribirLinea(BufferedWriter escritor, String... campos) throws IOException {
        escritor.write(String.join("\t", campos));
        escritor.newLine();
    }

    private String limpiarCampo(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace("\t", " ").replace("\r", " ").replace("\n", " ");
    }

    public Path getArchivo() {
        return archivo;
    }
}
