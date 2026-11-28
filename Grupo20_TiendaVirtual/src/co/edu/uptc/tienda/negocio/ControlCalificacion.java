package co.edu.uptc.tienda.negocio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.excepcion.CompraExcepcion;
import co.edu.uptc.tienda.excepcion.LibroExcepcion;
import co.edu.uptc.tienda.interfaz.IGestionCompra;
import co.edu.uptc.tienda.interfaz.IPersistenciaCalificacion;
import co.edu.uptc.tienda.interfaz.IRegistroOperaciones;
import co.edu.uptc.tienda.modelo.Calificacion;
import co.edu.uptc.tienda.persistencia.CalificacionBD;
import co.edu.uptc.tienda.persistencia.CalificacionJson;
import co.edu.uptc.tienda.persistencia.RegistroOperacionesTxt;

public class ControlCalificacion {

    public static final int LARGO_COMENTARIO = 300;

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ControlLibro controlLibro;
    private final IGestionCompra compras;
    private final List<IPersistenciaCalificacion> persistencias;
    private final IRegistroOperaciones registro;
    private final List<Calificacion> calificaciones = new ArrayList<>();
    private String avisoCarga;

    public ControlCalificacion(ControlLibro controlLibro) {
        this(controlLibro, new GestionCompra(), crearPersistencias(),
                new RegistroOperacionesTxt("datos/operaciones.txt"));
    }

    public ControlCalificacion(ControlLibro controlLibro, IGestionCompra compras,
            List<IPersistenciaCalificacion> persistencias, IRegistroOperaciones registro) {
        this.controlLibro = controlLibro;
        this.compras = compras;
        this.persistencias = persistencias;
        this.registro = registro;
        cargar();
    }

    private static List<IPersistenciaCalificacion> crearPersistencias() {
        List<IPersistenciaCalificacion> persistencias = new ArrayList<>();
        persistencias.add(new CalificacionBD());
        persistencias.add(new CalificacionJson("datos/calificaciones.json"));
        return persistencias;
    }

    private void cargar() {
        List<String> errores = new ArrayList<>();
        for (IPersistenciaCalificacion persistencia : persistencias) {
            try {
                for (Calificacion calificacion : persistencia.leer()) {
                    if (buscar(calificacion.getIsbn(), calificacion.getIdCliente()) == null) {
                        calificaciones.add(calificacion);
                    }
                }
            } catch (LibroExcepcion e) {
                errores.add(e.getMessage());
            }
        }
        if (!errores.isEmpty()) {
            avisoCarga = String.join("\n", errores);
        }
    }

    public String getAvisoCarga() {
        return avisoCarga;
    }

    public void calificar(String isbn, String idCliente, int puntaje, String comentario) throws LibroExcepcion {
        if (controlLibro.buscarLibro(isbn) == null) {
            throw new LibroExcepcion("No existe un libro registrado con ese ISBN.");
        }
        if (idCliente == null || idCliente.trim().isEmpty()) {
            throw new LibroExcepcion("Debe ingresar el ID del cliente que califica.");
        }
        if (puntaje < 1 || puntaje > 5) {
            throw new LibroExcepcion("La calificación debe estar entre 1 y 5.");
        }
        String texto = comentario == null ? "" : comentario.trim();
        if (texto.length() > LARGO_COMENTARIO) {
            throw new LibroExcepcion("El comentario no puede pasar de " + LARGO_COMENTARIO + " caracteres.");
        }
        try {
            if (!compras.clienteComproLibro(idCliente.trim(), isbn)) {
                throw new LibroExcepcion("Solo puede calificar el libro un cliente que lo haya comprado.");
            }
        } catch (CompraExcepcion e) {
            throw new LibroExcepcion("No se pudo revisar si el cliente compró el libro:\n" + e.getMessage());
        }
        Calificacion calificacion = new Calificacion(isbn, idCliente.trim(), puntaje, texto,
                LocalDateTime.now().format(FORMATO));
        Calificacion anterior = buscar(isbn, idCliente.trim());
        if (anterior != null) {
            calificaciones.remove(anterior);
        }
        calificaciones.add(calificacion);
        List<String> errores = new ArrayList<>();
        for (IPersistenciaCalificacion persistencia : persistencias) {
            try {
                persistencia.guardar(calificacion);
            } catch (LibroExcepcion e) {
                errores.add(e.getMessage());
            }
        }
        if (errores.size() == persistencias.size()) {
            calificaciones.remove(calificacion);
            if (anterior != null) {
                calificaciones.add(anterior);
            }
            throw new LibroExcepcion("La calificación no se pudo guardar en ninguna parte:\n"
                    + String.join("\n", errores));
        }
        registro.registrar("Libros", "Calificar libro", "ISBN " + isbn + ", cliente " + idCliente.trim()
                + ", puntaje " + puntaje);
        if (!errores.isEmpty()) {
            throw new LibroExcepcion("La calificación se guardó, pero no en todas partes:\n"
                    + String.join("\n", errores));
        }
    }

    public List<Calificacion> listarPorLibro(String isbn) {
        List<Calificacion> delLibro = new ArrayList<>();
        for (Calificacion calificacion : calificaciones) {
            if (calificacion.getIsbn().equals(isbn)) {
                delLibro.add(calificacion);
            }
        }
        return delLibro;
    }

    public double calcularPromedio(String isbn) {
        List<Calificacion> delLibro = listarPorLibro(isbn);
        if (delLibro.isEmpty()) {
            return 0;
        }
        double suma = 0;
        for (Calificacion calificacion : delLibro) {
            suma += calificacion.getPuntaje();
        }
        return suma / delLibro.size();
    }

    private Calificacion buscar(String isbn, String idCliente) {
        for (Calificacion calificacion : calificaciones) {
            if (calificacion.esDe(isbn, idCliente)) {
                return calificacion;
            }
        }
        return null;
    }
}
