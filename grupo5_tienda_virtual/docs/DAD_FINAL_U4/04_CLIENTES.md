# 4.1 Clientes

**Responsable:** Juan Pablo Barrero  
**Requisitos:** RF05 Registrar cliente, RF06 Iniciar sesión, RF07 Actualizar datos.

## a) Caso de uso

- Actor: Cliente / Administrador.
- Casos: registrar, listar, buscar, actualizar, eliminar e iniciar sesión.

**Evidencia:** pendiente de diagrama específico en el DAD final.

## b) Especificación

- Registrar un cliente con correo único.
- Validar correo con formato válido.
- Validar contraseña de mínimo 4 caracteres.
- Permitir clientes Regular y Premium.
- Actualizar datos personales.
- Eliminar cliente.

## c) Diagrama de clases

Debe incluir:

- `Cliente`, `ClienteRegular`, `ClientePremium`.
- `IRepositorio<Cliente>`.
- `RepositorioClientesArchivoPlano`, `RepositorioClientesJson`, `RepositorioClientesBD`.
- `IServicioClientes`.
- `ControladorCliente`.
- `EventosRegistroCliente`, `EventosClientes`, `EventosMiCuenta`.

**Evidencia:** pendiente de diagrama en el DAD final.

## d) Diagrama de secuencia

Debe mostrar:

```text
Vista → Eventos → IServicioClientes → ControladorCliente → IRepositorio → almacenamiento
```

**Evidencia:** pendiente de diagrama en el DAD final.

## e) Código y commits

- `ControladorCliente`.
- `ProjectConfig.clientes()`.
- Pruebas relacionadas.

**Evidencia:** pendiente de fragmento y enlace en el DAD final.

## f) Pruebas individuales

| ID | Operación | Evidencia | Estado |
|---|---|---|---|
| PI-CLI-01 | Crear cliente | `pruebas/14_CrudCompletoTest_clientesCrudCompleto.txt` | OK |
| PI-CLI-02 | Listar/buscar | `pruebas/14_CrudCompletoTest_clientesCrudCompleto.txt` | OK |
| PI-CLI-03 | Actualizar | `pruebas/14_CrudCompletoTest_clientesCrudCompleto.txt` | OK |
| PI-CLI-04 | Eliminar | `pruebas/14_CrudCompletoTest_clientesCrudCompleto.txt` | OK |
| PI-CLI-05 | Validación correo/contraseña | `pruebas/06_FlujosPersistenciaTest_validaCorreoYContrasenaDeClientes.txt` | OK |
| PI-CLI-06 | Duplicado/login inválido | `pruebas/11_ReglasNegocioTest_rechazaClienteDuplicadoEInicioSesionInvalido.txt` | OK |

## Capturas

- `capturas_eclipse/01_login.png`
- `capturas_eclipse/02_registro_cliente.png`
- `capturas_eclipse/03_gestion_clientes.png`
- `capturas_eclipse/05_mi_cuenta.png`
- `crud/PI-CLI-01_crear_formulario.png`
- `crud/PI-CLI-02_listar.png`
- `crud/PI-CLI-03_actualizar.png`
- `crud/PI-CLI-04_eliminar.png`
