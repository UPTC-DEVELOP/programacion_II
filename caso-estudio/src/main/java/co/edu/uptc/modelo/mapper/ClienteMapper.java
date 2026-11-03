package co.edu.uptc.modelo.mapper;

import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.ClientePremium;
import co.edu.uptc.modelo.ClienteRegular;
import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;
import java.sql.Timestamp;

/**
 * Convierte entre la entidad Cliente (capa de persistencia/negocio) y el
 * ClienteDto (capa de presentación).
 */
public class ClienteMapper {

    private ClienteMapper() { }

    // Convierte DTO -> Entidad (para guardar en persistencia)
    public static Cliente toEntity(ClienteDto dto) {
        if (dto == null) return null;

        Timestamp fechaRegistro = new Timestamp(System.currentTimeMillis());
        TipoCliente tipo = (dto.getTipoCliente() == null) ? TipoCliente.REGULAR : dto.getTipoCliente();
        Rol rol = (dto.getRol() == null) ? Rol.CLIENTE : dto.getRol();
        String contrasena = (dto.getContrasenia() == null) ? "" : dto.getContrasenia();

        if (tipo == TipoCliente.PREMIUM) {
            return new ClientePremium(
                    dto.getPrimerNombre(), dto.getOtrosNombres(), dto.getPrimerApellido(),
                    dto.getOtrosApellidos(), dto.getTipoIdentificacion(), dto.getIdentificacion(),
                    dto.getCorreoElectronico(), dto.getCelular(), dto.getDireccion(),
                    dto.getIdCliente(), tipo, contrasena, fechaRegistro, 0, rol);
        }
        return new ClienteRegular(
                dto.getPrimerNombre(), dto.getOtrosNombres(), dto.getPrimerApellido(),
                dto.getOtrosApellidos(), dto.getTipoIdentificacion(), dto.getIdentificacion(),
                dto.getCorreoElectronico(), dto.getCelular(), dto.getDireccion(),
                dto.getIdCliente(), tipo, contrasena, fechaRegistro, 0, rol);
    }

    // Convierte Entidad -> DTO (para mostrar en la GUI)
    public static ClienteDto toDto(Cliente entity) {
        if (entity == null) return null;
        ClienteDto dto = new ClienteDto();
        dto.setIdCliente(entity.getIdCliente());
        dto.setPrimerNombre(entity.getPrimerNombre());
        dto.setOtrosNombres(entity.getOtrosNombres());
        dto.setPrimerApellido(entity.getPrimerApellido());
        dto.setOtrosApellidos(entity.getOtrosApellidos());
        dto.setTipoIdentificacion(entity.getTipoIdentificacion());
        dto.setIdentificacion(entity.getIdentificacion());
        dto.setCorreoElectronico(entity.getCorreoElectronico());
        dto.setCelular(entity.getCelular());
        dto.setDireccion(entity.getDireccion());
        dto.setTipoCliente(entity.getTipoCliente());
        dto.setContrasenia(entity.getContrasenia());
        dto.setRol(entity.getRol());
        return dto;
    }
}
