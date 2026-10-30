package persistencia;

import java.lang.reflect.Type;
import java.time.LocalDate;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Convierte java.time.LocalDate a texto ISO (yyyy-MM-dd) en el JSON.
 *
 * Sin este adaptador Gson usaria reflexion sobre java.time y Java 17+ lo
 * bloquea (el modulo java.base no permite el acceso), lanzando
 * InaccessibleObjectException al guardar.
 */
public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    @Override
    public JsonElement serialize(LocalDate fecha, Type tipoOrigen, JsonSerializationContext contexto) {
        return new JsonPrimitive(fecha.toString());
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type tipoDestino, JsonDeserializationContext contexto)
            throws JsonParseException {
        return LocalDate.parse(json.getAsString());
    }
}
