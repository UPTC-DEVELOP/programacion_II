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

- `app/04_login_llenado.png`
- `app/14_registro_llenado.png`
- `app/15_registro_exitoso.png`
- `app/18_clientes_listado.png`
- `app/19_clientes_confirmar_eliminar.png`
- `app/21_clientes_despues.png`
- `app/36_mi_cuenta_datos.png`
- `app/57_mi_cuenta_guardado_aviso.png`
- `crud/PI-CLI-01_crear_formulario.png`
- `crud/PI-CLI-02_listar.png`
- `crud/PI-CLI-03_actualizar.png`
- `crud/PI-CLI-04_eliminar_antes.png`
- `crud/PI-CLI-05_eliminar_despues.png`
