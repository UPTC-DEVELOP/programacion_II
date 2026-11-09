package co.edu.uptc.interfaces;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.dto.CompraDtos.FacturaDto;
/** INTERFAZ IGestionCompra QUE DEFINE LOS MÉTODOS PARA GESTIONAR COMPRAS Y FACTURAS
 *  @author: Equipo de desarrollo grupo 7 UPTC
 */
public interface IGestionCompra {
    FacturaDto finalizarCompra(int idCliente) throws ReglaNegocioException;
    FacturaDto consultarFactura(int idVenta) throws ReglaNegocioException;
}
