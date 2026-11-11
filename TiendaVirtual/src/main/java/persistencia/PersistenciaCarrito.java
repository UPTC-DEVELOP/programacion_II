package persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import modelo.ItemCarrito;

/** Formato JSON limitado y versionado. No interpreta JSON arbitrario. */
public final class PersistenciaCarrito {
    private final Path archivo = Paths.get("datos", "carrito.json");

    public static final class Registro {
        public final String titulo, autor, formato;
        public final int precio, cantidad;
        public Registro(String t, String a, String f, int p, int c) {
            titulo=t; autor=a; formato=f; precio=p; cantidad=c;
        }
    }

    public void guardar(List<ItemCarrito> items) throws IOException {
        Files.createDirectories(archivo.getParent());
        StringBuilder json = new StringBuilder("{\n  \"version\": 1,\n  \"items\": [\n");
        for (int i=0; i<items.size(); i++) {
            ItemCarrito item=items.get(i);
            modelo.Libro l=item.getLibro();
            if (i>0) json.append(",\n");
            json.append("    {\"titulo\":").append(comillas(l.getTitulo()))
                .append(",\"autor\":").append(comillas(l.getAutor()))
                .append(",\"formato\":").append(comillas(l.getFormato()))
                .append(",\"precio\":").append(l.getPrecio())
                .append(",\"cantidad\":").append(item.getCantidad()).append('}');
        }
        json.append("\n  ]\n}\n");
        Path temporal = Files.createTempFile(archivo.getParent(), "carrito-", ".tmp");
        try {
            Files.write(temporal,json.toString().getBytes(StandardCharsets.UTF_8));
            try { Files.move(temporal,archivo,StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.ATOMIC_MOVE); }
            catch (java.nio.file.AtomicMoveNotSupportedException ex) {
                Files.move(temporal,archivo,StandardCopyOption.REPLACE_EXISTING);
            }
        } finally { Files.deleteIfExists(temporal); }
    }

    private static String comillas(String s) {
        StringBuilder b=new StringBuilder("\"");
        for (char c: s.toCharArray()) {
            switch(c) {
                case '\\': b.append("\\\\"); break;
                case '\"': b.append("\\\""); break;
                case '\n': b.append("\\n"); break;
                case '\r': b.append("\\r"); break;
                case '\t': b.append("\\t"); break;
                default: if (c<32) b.append(String.format("\\u%04x",(int)c)); else b.append(c);
            }
        }
        return b.append('\"').toString();
    }

    public List<Registro> leer() throws IOException {
        ArrayList<Registro> resultado=new ArrayList<>();
        if (!Files.exists(archivo)) return resultado;
        String texto=new String(Files.readAllBytes(archivo),StandardCharsets.UTF_8);
        // Analizador de esquema estricto, compatible con el escritor anterior.
        try {
            Cursor c=new Cursor(texto);
            c.literal("{"); c.stringField("version"); c.literal(":");
            if (c.number()!=1) throw new IllegalArgumentException("Versión no soportada");
            c.literal(","); c.stringField("items"); c.literal(":"); c.literal("[");
            if (!c.nextIs(']')) {
                do {
                    c.literal("{"); c.stringField("titulo"); c.literal(":"); String t=c.string();
                    c.literal(","); c.stringField("autor"); c.literal(":"); String a=c.string();
                    c.literal(","); c.stringField("formato"); c.literal(":"); String f=c.string();
                    c.literal(","); c.stringField("precio"); c.literal(":"); int p=c.number();
                    c.literal(","); c.stringField("cantidad"); c.literal(":"); int q=c.number();
                    c.literal("}"); if(p<0 || q<=0) throw new IllegalArgumentException();
                    resultado.add(new Registro(t,a,f,p,q));
                    if (!c.nextIs(',')) break;
                    c.literal(",");
                } while(true);
            }
            c.literal("]"); c.literal("}"); c.end();
            return resultado;
        } catch (RuntimeException ex) { throw new IOException("Archivo carrito.json inválido: "+ex.getMessage(),ex); }
    }

    private static final class Cursor {
        final String s; int i;
        Cursor(String s){this.s=s;}
        void ws(){while(i<s.length() && Character.isWhitespace(s.charAt(i)))i++;}
        boolean nextIs(char c){ws();return i<s.length() && s.charAt(i)==c;}
        void literal(String x){ws();if(!s.startsWith(x,i))throw new IllegalArgumentException("Se esperaba "+x);i+=x.length();}
        void stringField(String x){if(!string().equals(x))throw new IllegalArgumentException("Campo inesperado");}
        int number(){ws();int a=i;if(i<s.length()&&s.charAt(i)=='-')i++;while(i<s.length()&&Character.isDigit(s.charAt(i)))i++;if(i==a)throw new IllegalArgumentException("Número inválido");return Integer.parseInt(s.substring(a,i));}
        String string(){
            ws();literal("\"");StringBuilder b=new StringBuilder();
            while(i<s.length()){
                char ch=s.charAt(i++);if(ch=='\"')return b.toString();
                if(ch=='\\'){
                    if(i>=s.length())break;char e=s.charAt(i++);
                    switch(e){case '\"': b.append('\"');break;case '\\':b.append('\\');break;
                      case '/':b.append('/');break;case 'n':b.append('\n');break;
                      case 'r':b.append('\r');break;case 't':b.append('\t');break;
                      case 'u':if(i+4>s.length())throw new IllegalArgumentException("Unicode inválido");
                        b.append((char)Integer.parseInt(s.substring(i,i+4),16));i+=4;break;
                      default:throw new IllegalArgumentException("Escape inválido");}
                }else{if(ch<32)throw new IllegalArgumentException("Carácter de control");b.append(ch);}
            }throw new IllegalArgumentException("Cadena incompleta");
        }
        void end(){ws();if(i!=s.length())throw new IllegalArgumentException("Contenido adicional");}
    }
}
