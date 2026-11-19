# Pruebas manuales - Clientes (archivo plano)

**Responsable:** Liz Alejandra Rojas
**Configuracion probada:** `ProjectConfig.clientes()` -> `clientesArchivoPlano()` (valor por defecto)
**Archivo de datos:** `datos/clientes.txt`


## Formato real de `clientes.txt`

Un cliente por linea, campos separados por tabulador:

```text
TIPO <tab> nombre <tab> correo <tab> contrasena <tab> direccion <tab> telefono
```

Ejemplo (cliente de prueba que crea `Main` si el archivo esta vacio):

```text
PREMIUM	Juan Perez	juan@uptc.edu.co	1234	Cra 11 #15-20, Tunja	311 555 2233
```


## Casos de prueba

| ID | Caso | Pasos | Resultado esperado |
|----|------|-------|--------------------|
| CL-01 | Cliente de prueba | Abrir la app con `datos/` vacia y cerrarla. | Se crea `datos/clientes.txt` con la linea `PREMIUM` de Juan Perez. |
| CL-02 | Registrar Regular | Login > **Registrarse**. Llenar nombre, correo, direccion, telefono, contrasena; tipo **Regular**; **Registrarse**. | Mensaje "Cliente registrado correctamente." Linea nueva `REGULAR` en `clientes.txt`. |
| CL-03 | Registrar Premium | Igual que CL-02 con tipo **Premium**. | Linea nueva `PREMIUM` en `clientes.txt`. |
| CL-04 | Correo duplicado | Registrar otra vez un correo ya existente. | Mensaje "Ya existe un cliente con ese correo." No se agrega linea. |
| CL-05 | Iniciar sesion | Entrar con correo y contrasena de CL-02. | Abre la ventana de compras. Con contrasena incorrecta no entra. |
| CL-06 | Listar | Login > **Entrar como Administrador** > **Gestion de Clientes**. | La tabla muestra todos los clientes de `clientes.txt`. |
| CL-07 | Actualizar datos | Iniciar sesion > **Mi Cuenta**. Cambiar nombre, direccion o telefono > **Guardar cambios**. | "Datos actualizados correctamente." El cambio aparece en `clientes.txt`. El correo no es editable. |
| CL-08 | Nombre vacio | En Mi Cuenta borrar el nombre y guardar. | "El nombre completo es obligatorio." No se modifica el archivo. |
| CL-09 | Eliminar | Gestion de Clientes > seleccionar un cliente > **Eliminar** > confirmar. | Desaparece de la tabla y de `clientes.txt`. |
| CL-10 | Persistencia | Cerrar la app y abrirla de nuevo. | Los clientes siguen ahi. |
| CL-11 | Tipo se conserva | Reiniciar y verificar un Premium y un Regular. | El Premium sigue en `PREMIUM` y el Regular en `REGULAR`. Al comprar, el Premium recibe 10 % de descuento. |

## Repetir con JSON y BD

Cambiar solo la linea de `ProjectConfig.clientes()`, borrar `datos/` y repetir CL-01 a CL-11:

| Persistencia | Linea en `ProjectConfig.clientes()` | Donde verificar |
|--------------|-------------------------------------|-----------------|
| Archivo plano | `return clientesArchivoPlano();` | `datos/clientes.txt` |
| JSON | `return clientesJson();` | `datos/clientes.json` |
| Base de datos | `return clientesBD();` | tabla de clientes en `datos/tienda.db` |

Los resultados se anotan en `docs/ACTA_PRUEBAS.md`.
