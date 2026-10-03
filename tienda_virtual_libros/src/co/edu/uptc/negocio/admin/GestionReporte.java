package co.edu.uptc.negocio.admin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import co.edu.uptc.interfaces.IConsultaVentas;
import co.edu.uptc.interfaces.IGestionReporte;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.modelo.dto.VentaResumenDto;


/**
 * CLASE GestionReporte  (paquete: negocio)  implements IGestionReporte
 * ---------------------------------------------------------------------------
 * Calcula los números del Dashboard y de la pantalla Reportes.
 * Solo LEE: usa el repositorio de libros y la consulta de ventas (DIP).
 */
public class GestionReporte implements IGestionReporte {

    private final ILibroRepositorio repositorio;
    private final IConsultaVentas ventas;

    public GestionReporte(ILibroRepositorio repositorio, IConsultaVentas ventas) {
        this.repositorio = repositorio;
        this.ventas = ventas;
    }

    @Override
    public int totalLibros() {
        return repositorio.listarTodos().size();
    }

    @Override
    public int totalVentas() {
        return ventas.listarVentas().size();
    }

    @Override
    public double ingresosTotales() {
        double suma = 0;
        for (VentaResumenDto v : ventas.listarVentas()) {
            suma += v.getTotal();
        }
        return suma;
    }

    @Override
    public List<VentaResumenDto> ventasRecientes(int cantidad) {
        List<VentaResumenDto> todas = new ArrayList<>(ventas.listarVentas());
        todas.sort(Comparator.comparing(VentaResumenDto::getFecha).reversed());
        return todas.subList(0, Math.min(cantidad, todas.size()));
    }

    /** Las fechas yyyy-MM-dd se comparan como texto porque ese formato ordena bien. */
    @Override
    public List<VentaResumenDto> ventasEntre(String desde, String hasta) {
        List<VentaResumenDto> resultado = new ArrayList<>();
        for (VentaResumenDto v : ventas.listarVentas()) {
            boolean despuesDeDesde = desde == null || desde.isEmpty() || v.getFecha().compareTo(desde) >= 0;
            boolean antesDeHasta = hasta == null || hasta.isEmpty() || v.getFecha().compareTo(hasta) <= 0;
            if (despuesDeDesde && antesDeHasta) {
                resultado.add(v);
            }
        }
        return resultado;
    }
}
