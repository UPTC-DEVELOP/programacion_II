package persistencia;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        cargar();
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
    public void cargar() {
        compras.clear();
        if (!Files.exists(archivo)) {
            return;
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

        compras.addAll(comprasPorId.values());
    }

    private long siguienteId() {
        long maximo = 0;
        for (Compra compra : compras) {
            maximo = Math.max(maximo, compra.getId());
        }
        return maximo + 1;
    }

    @Override
    public void guardar() {
    }

    public Path getArchivo() {
        return archivo;
    }
}
