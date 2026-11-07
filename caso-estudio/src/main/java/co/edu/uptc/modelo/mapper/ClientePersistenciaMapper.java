package co.edu.uptc.modelo.mapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.ClientePremium;
import co.edu.uptc.modelo.ClienteRegular;
import co.edu.uptc.modelo.dto.ClientePersistenciaDto;
import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;

/**
 * Convierte entre la entidad Cliente y su DTO de persistencia JSON.
 *
 * <p>Al leer del archivo crea la subclase correcta según el tipo:
 * PREMIUM → ClientePremium, cualquier otro → ClienteRegular.</p>
 */
public final class ClientePersistenciaMapper {

    private ClientePersistenciaMapper() {
    }

    /**
     * Convierte una entidad Cliente en el registro que se guarda en JSON.
     *
     * @param cliente Entidad a convertir
     * @return DTO de persistencia, o null si el cliente es null
     */
    public static ClientePersistenciaDto toDto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        ClientePersistenciaDto dto = new ClientePersistenciaDto();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setPrimerNombre(cliente.getPrimerNombre());
        dto.setOtrosNombres(cliente.getOtrosNombres());
        dto.setPrimerApellido(cliente.getPrimerApellido());
        dto.setOtrosApellidos(cliente.getOtrosApellidos());
        dto.setTipoIdentificacion(cliente.getTipoIdentificacion());
        dto.setIdentificacion(cliente.getIdentificacion());
        dto.setCorreoElectronico(cliente.getCorreoElectronico());
        dto.setCelular(cliente.getCelular());
        dto.setDireccion(cliente.getDireccion());
        dto.setTipoCliente(cliente.getTipoCliente());
        dto.setRol(cliente.getRol());
        dto.setContrasenia(cliente.getContrasenia());
        dto.setFechaRegistro(cliente.getFechaRegistro() == null
                ? null : cliente.getFechaRegistro().toLocalDateTime().toString());
        dto.setIntentosFallidos(cliente.getIntentosFallidos());
        return dto;
    }

    /**
     * Convierte un registro del JSON en la entidad Cliente correspondiente.
     *
     * @param dto Registro leído del archivo
     * @return ClientePremium o ClienteRegular, o null si el dto es null
     */
    public static Cliente toEntity(ClientePersistenciaDto dto) {
        if (dto == null) {
            return null;
        }
        TipoCliente tipo = (dto.getTipoCliente() == null) ? TipoCliente.REGULAR : dto.getTipoCliente();
        Rol rol = (dto.getRol() == null) ? Rol.CLIENTE : dto.getRol();
        Timestamp fecha = convertirFecha(dto.getFechaRegistro());

        if (tipo == TipoCliente.PREMIUM) {
            return new ClientePremium(
                    dto.getPrimerNombre(), dto.getOtrosNombres(), dto.getPrimerApellido(),
                    dto.getOtrosApellidos(), dto.getTipoIdentificacion(), dto.getIdentificacion(),
                    dto.getCorreoElectronico(), dto.getCelular(), dto.getDireccion(),
                    dto.getIdCliente(), tipo, dto.getContrasenia(), fecha,
                    dto.getIntentosFallidos(), rol);
        }
        return new ClienteRegular(
                dto.getPrimerNombre(), dto.getOtrosNombres(), dto.getPrimerApellido(),
                dto.getOtrosApellidos(), dto.getTipoIdentificacion(), dto.getIdentificacion(),
                dto.getCorreoElectronico(), dto.getCelular(), dto.getDireccion(),
                dto.getIdCliente(), tipo, dto.getContrasenia(), fecha,
                dto.getIntentosFallidos(), rol);
    }

    /** Convierte la fecha ISO-8601 del archivo en Timestamp (null si no es válida). */
    private static Timestamp convertirFecha(String fechaIso) {
        if (fechaIso == null || fechaIso.trim().isEmpty()) {
            return null;
        }
        try {
            return Timestamp.valueOf(LocalDateTime.parse(fechaIso.trim()));
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
