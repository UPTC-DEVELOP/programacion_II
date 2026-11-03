package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.modelo.dto.DashboardKpiDto;
import co.edu.uptc.modelo.dto.VentaResumenDto;

/**
 * INTERFAZ IGestionDashboard  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * CONTRATO de la capa de NEGOCIO del dashboard administrativo.
 * La presentación (PanelDashboard / Evento) depende de ESTA ABSTRACCIÓN,
 * nunca de la implementación concreta GestionDashboard (DIP).
 *
 * 
 *  - SRP: el panel no debe saber cómo se calcula el stock total ni cómo se
 *    ordenan las ventas recientes; eso es lógica de negocio.
 *  - Testeabilidad: al depender de la interfaz, se puede sustituir por un
 *    mock en pruebas futuras sin tocar la GUI.
 *
 * MÉTODOS:
 *  - obtenerKpis: retorna TODOS los indicadores del dashboard en un solo DTO.
 *  - listarVentas: retorna el resumen de todas las ventas (para Reportes).
 */
public interface IGestionDashboard {

    /** Calcula y retorna todos los KPIs del dashboard en una sola llamada. */
    DashboardKpiDto obtenerKpis();

    /** Retorna el resumen de TODAS las ventas, ordenadas de más reciente a más antigua. */
    List<VentaResumenDto> listarVentas();
}
