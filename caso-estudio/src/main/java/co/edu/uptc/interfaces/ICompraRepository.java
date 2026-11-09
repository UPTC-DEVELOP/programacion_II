package co.edu.uptc.interfaces;

import co.edu.uptc.modelo.dto.CompraDtos.FacturaDto;
import co.edu.uptc.modelo.dto.CompraDtos.SolicitudCompraDto;
/** 
 * INTERFAZ ICompraRepository QUE DEFINE LOS MÉTODOS PARA GESTIONAR COMPRAS Y FACTURAS
 * @author: Equipo de desarrollo grupo 7 UPTC
 */
public interface ICompraRepository {
    int finalizarCompra(SolicitudCompraDto solicitud);
    FacturaDto consultarFactura(int idVenta);
}
