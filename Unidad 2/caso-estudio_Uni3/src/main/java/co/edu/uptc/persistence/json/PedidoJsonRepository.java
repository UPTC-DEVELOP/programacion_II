package co.edu.uptc.persistence.json;

import co.edu.uptc.model.LineaPedido;
import co.edu.uptc.model.MetodoPago;
import co.edu.uptc.model.Pedido;
import co.edu.uptc.persistence.PedidoRepository;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// PedidoJsonRepository guarda una fila por línea y reconstruye el pedido al leer.
public class PedidoJsonRepository implements PedidoRepository {
    private final Path archivo;
    private final List<Pedido> pedidos = new ArrayList<Pedido>();
    public PedidoJsonRepository(Path archivo) { this.archivo = archivo; cargar(); }
    @Override public List<Pedido> listar() { return new ArrayList<Pedido>(pedidos); }
    @Override public List<Pedido> listarPorCliente(String correo) { List<Pedido> r=new ArrayList<Pedido>(); for(Pedido p:pedidos) if(p.getCorreoCliente().equalsIgnoreCase(correo)) r.add(p); return r; }
    @Override public long siguienteId() { long mayor=0; for(Pedido p:pedidos) if(p.getId()>mayor) mayor=p.getId(); return mayor+1; }
    @Override public void guardar(Pedido pedido) { pedidos.add(pedido); guardarTodos(); }
    @Override public void eliminar(long id) { for(int i=pedidos.size()-1;i>=0;i--) if(pedidos.get(i).getId()==id) pedidos.remove(i); guardarTodos(); }
    private void cargar() {
        Map<Long,List<Map<String,String>>> grupos = new LinkedHashMap<Long,List<Map<String,String>>>();
        for(Map<String,String> m:ArchivoJson.leer(archivo)){ long id=Long.parseLong(m.get("id")); if(!grupos.containsKey(id)) grupos.put(id,new ArrayList<Map<String,String>>()); grupos.get(id).add(m); }
        for(Map.Entry<Long,List<Map<String,String>>> e:grupos.entrySet()){ Map<String,String> h=e.getValue().get(0); ArrayList<LineaPedido> lineas=new ArrayList<LineaPedido>(); for(Map<String,String> m:e.getValue()) lineas.add(new LineaPedido(m.get("isbn"),m.get("titulo"),decimal(m,"precio"),decimal(m,"descuento"),decimal(m,"iva"),entero(m,"cantidad"))); pedidos.add(new Pedido(e.getKey(),h.get("fecha"),h.get("correo"),lineas,decimal(h,"subtotal"),decimal(h,"impuestos"),decimal(h,"descuentoCliente"),decimal(h,"total"),MetodoPago.valueOf(h.get("metodo")))); }
    }
    private void guardarTodos(){ List<Map<String,String>> filas=new ArrayList<Map<String,String>>(); for(Pedido p:pedidos) for(LineaPedido l:p.getLineas()){ Map<String,String> m=new LinkedHashMap<String,String>(); m.put("id",String.valueOf(p.getId()));m.put("fecha",p.getFecha());m.put("correo",p.getCorreoCliente());m.put("subtotal",String.valueOf(p.getSubtotal()));m.put("impuestos",String.valueOf(p.getImpuestos()));m.put("descuentoCliente",String.valueOf(p.getDescuento()));m.put("total",String.valueOf(p.getTotal()));m.put("metodo",p.getMetodoPago().name());m.put("isbn",l.getIsbn());m.put("titulo",l.getTitulo());m.put("precio",String.valueOf(l.getPrecioBase()));m.put("descuento",String.valueOf(l.getPorcentajeDescuento()));m.put("iva",String.valueOf(l.getImpuestoIVA()));m.put("cantidad",String.valueOf(l.getCantidad()));filas.add(m);} ArchivoJson.escribir(archivo,filas); }
    private static int entero(Map<String,String>m,String k){return Integer.parseInt(m.get(k));}
    private static double decimal(Map<String,String>m,String k){return Double.parseDouble(m.get(k));}
}
