package co.edu.uptc.persistencia;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.interfaces.ICarritoRepository;
import co.edu.uptc.modelo.CarritoCompras;
import co.edu.uptc.modelo.dto.CarritoSessionDto;
import co.edu.uptc.modelo.mapper.CarritoMapper;
import co.edu.uptc.negocio.CalculadoraDescuento;

/**
 * Implementación del repositorio de carritos con persistencia en JSON.
 *
 * <p>Administra el archivo data/carritos.json, lo crea con la estructura vacía
 * cuando no existe y conserva un carrito por cliente. La clase cumple el
 * contrato ICarritoRepository, por lo que puede sustituir a
 * CarritoRepositoryEnMemoria sin modificar la capa de negocio (LSP y DIP).</p>
 *
 * <p>Los métodos públicos están sincronizados para evitar escrituras
 * concurrentes sobre el mismo archivo. Cada operación realiza una lectura
 * completa, aplica el cambio y reescribe el archivo de forma inmediata.</p>
 *
 * <p>Estructura del archivo:</p>
 * <pre>
 * {
 *   "carritos": [
 *     {
 *       "idCarrito": 1001,
 *       "idCliente": 1001,
 *       "tipoCliente": "PREMIUM",
 *       "items": [ ... ],
 *       "subtotalGeneral": 0.0,
 *       "totalImpuestos": 0.0,
 *       "descuentoAplicado": 0.0,
 *       "totalFinal": 0.0
 *     }
 *   ]
 * }
 * </pre>
 */
public class CarritoJsonRepository implements ICarritoRepository {

    private static final String RUTA_POR_DEFECTO = "data/carritos.json";
    private static final String CLAVE_CARRITOS = "carritos";

    private static final Type TIPO_LISTA_CARRITOS =
            new TypeToken<List<CarritoSessionDto>>() { }.getType();

    private final Gson gson;
    private final Path rutaArchivo;

    /**
     * Construye el repositorio con la ruta data/carritos.json usada por la
     * aplicación real.
     */
    public CarritoJsonRepository() {
        this(RUTA_POR_DEFECTO);
    }

    /**
     * Construye el repositorio con una ruta personalizada. Este constructor se
     * utiliza en las pruebas para trabajar con directorios temporales.
     *
     * @param rutaArchivo ruta del archivo JSON que se desea administrar
     */
    public CarritoJsonRepository(String rutaArchivo) {
        this.rutaArchivo = Paths.get(rutaArchivo);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        inicializarArchivo();
    }

    /**
     * Crea la carpeta y el archivo JSON con la estructura vacía cuando no
     * existen. Si el archivo existe pero está vacío, lo reemplaza por una lista
     * de carritos vacía para mantener un JSON válido.
     */
    private void inicializarArchivo() {
        try {
            Path carpeta = rutaArchivo.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            if (!Files.exists(rutaArchivo) || Files.size(rutaArchivo) == 0) {
                escribir(new ArrayList<>());
            }
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo inicializar el archivo de carritos: " + rutaArchivo, e);
        }
    }

    @Override
    public synchronized void guardarCarrito(CarritoCompras carrito) {
        if (carrito == null) {
            return;
        }

        List<CarritoSessionDto> carritos = leer();
        CarritoSessionDto nuevo = construirDto(carrito);
        boolean reemplazado = false;

        for (int i = 0; i < carritos.size(); i++) {
            if (carritos.get(i).getIdCliente() == carrito.getIdCliente()) {
                carritos.set(i, nuevo);
                reemplazado = true;
                break;
            }
        }

        if (!reemplazado) {
            carritos.add(nuevo);
        }
        escribir(carritos);
    }

    @Override
    public synchronized CarritoCompras obtenerCarritoPorCliente(int idCliente) {
        for (CarritoSessionDto carrito : leer()) {
            if (carrito.getIdCliente() == idCliente) {
                return CarritoMapper.toEntity(carrito);
            }
        }
        return null;
    }

    @Override
    public synchronized void eliminarCarrito(int idCliente) {
        List<CarritoSessionDto> carritos = leer();
        boolean eliminado = carritos.removeIf(carrito -> carrito.getIdCliente() == idCliente);
        if (eliminado) {
            escribir(carritos);
        }
    }

    @Override
    public synchronized List<CarritoCompras> listarTodos() {
        List<CarritoCompras> resultado = new ArrayList<>();
        for (CarritoSessionDto carrito : leer()) {
            CarritoCompras entidad = CarritoMapper.toEntity(carrito);
            if (entidad != null) {
                resultado.add(entidad);
            }
        }
        return resultado;
    }

    /**
     * Convierte el carrito a su DTO de persistencia y completa los totales que
     * se guardan como instantánea. El descuento reutiliza la estrategia del
     * dominio para no duplicar el porcentaje Premium.
     */
    private CarritoSessionDto construirDto(CarritoCompras carrito) {
        CarritoSessionDto dto = CarritoMapper.toDto(carrito);
        double subtotal = carrito.calcularSubtotalGeneral();
        double impuestos = carrito.calcularTotalImpuestos();
        double descuento = CalculadoraDescuento
                .porTipoCliente(carrito.getTipoCliente())
                .calcularDescuento(subtotal);
        double total = subtotal + impuestos - descuento;

        dto.setSubtotalGeneral(redondear(subtotal));
        dto.setTotalImpuestos(redondear(impuestos));
        dto.setDescuentoAplicado(redondear(descuento));
        dto.setTotalFinal(redondear(total));
        return dto;
    }

    /**
     * Lee el archivo y devuelve la lista de carritos. Cuando el archivo no
     * existe o no contiene la clave "carritos", devuelve una lista vacía.
     */
    private List<CarritoSessionDto> leer() {
        try {
            if (!Files.exists(rutaArchivo)) {
                return new ArrayList<>();
            }

            String contenido = Files.readString(rutaArchivo, StandardCharsets.UTF_8);
            if (contenido == null || contenido.trim().isEmpty()) {
                return new ArrayList<>();
            }

            JsonElement raiz = JsonParser.parseString(contenido);
            if (!raiz.isJsonObject()) {
                return new ArrayList<>();
            }

            JsonObject objeto = raiz.getAsJsonObject();
            JsonElement arreglo = objeto.get(CLAVE_CARRITOS);
            if (arreglo == null || !arreglo.isJsonArray()) {
                return new ArrayList<>();
            }

            List<CarritoSessionDto> carritos = gson.fromJson(arreglo, TIPO_LISTA_CARRITOS);
            return (carritos == null) ? new ArrayList<>() : carritos;
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo leer el archivo de carritos: " + rutaArchivo, e);
        } catch (JsonParseException e) {
            throw new PersistenciaException(
                    "El archivo de carritos contiene un JSON inválido: " + rutaArchivo, e);
        }
    }

    /**
     * Escribe la lista completa de carritos dentro de la clave "carritos".
     */
    private void escribir(List<CarritoSessionDto> carritos) {
        try {
            Path carpeta = rutaArchivo.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }

            JsonObject raiz = new JsonObject();
            raiz.add(CLAVE_CARRITOS, gson.toJsonTree(carritos, TIPO_LISTA_CARRITOS));

            try (Writer writer = Files.newBufferedWriter(rutaArchivo, StandardCharsets.UTF_8)) {
                gson.toJson(raiz, writer);
            }
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo escribir el archivo de carritos: " + rutaArchivo, e);
        }
    }

    /** Redondea un valor monetario a dos decimales. */
    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
