# Checklist de pruebas de persistencia

**Proyecto:** Tienda Virtual de Libros — Grupo 5  
**Fecha:** 19/11/2026

## 1. Matriz 3x3

| Módulo | Archivo plano | JSON | Base de datos |
|---|---|---|---|
| Clientes | [ ] | [ ] | [ ] |
| Libros | [ ] | [ ] | [ ] |
| Compras | [ ] | [ ] | [ ] |

Para cada combinación se debe probar:

- [ ] Registrar o crear.
- [ ] Listar.
- [ ] Actualizar.
- [ ] Eliminar.
- [ ] Cerrar la aplicación.
- [ ] Volver a abrir.
- [ ] Confirmar que los datos siguen ahí.

## 2. Evidencia por combinación

| Módulo | Persistencia | Archivo o base | Estado |
|---|---|---|---|
| Clientes | Archivo plano | `datos/clientes.txt` | Probado: carga, guardado, Premium/Regular y línea inválida |
| Clientes | JSON | `datos/clientes.json` | Pendiente de repetir |
| Clientes | BD | `datos/tienda.db` | Pendiente de repetir |
| Libros | Archivo plano | `datos/libros.txt` | Probado con un libro en C38 |
| Libros | JSON | `datos/libros.json` | Pendiente de repetir |
| Libros | BD | `datos/tienda.db` | Pendiente de repetir |
| Compras | Archivo plano | `datos/compras.txt` | Probado: guardar, cargar, dos items e IDs duplicados |
| Compras | JSON | `datos/compras.json` | Pendiente de repetir |
| Compras | BD | `datos/tienda.db` | Es el valor por defecto actual |

## 3. Prueba de cambio de una línea

1. Abrir `config/ProjectConfig.java`.
2. En `compras()` cambiar `comprasBD()` por `comprasJson()`.
3. Ejecutar `mvn clean compile`.
4. Hacer una compra.
5. Verificar `datos/compras.json`.
6. Cerrar y abrir la aplicación; el historial debe cargar.
7. Volver a `comprasBD()` y verificar `datos/tienda.db`.
8. Confirmar que no se tocó ninguna ventana ni controlador.

## 4. Prueba de independencia de la GUI

- [ ] `model` no importa Swing.
- [ ] `persistencia` no importa Swing.
- [ ] `service` no importa Swing.
- [ ] `controller` no importa Swing.
- [ ] La compra se puede finalizar desde `ControladorCompras` sin abrir la ventana.

## 5. Compilación

```bash
mvn clean compile
```

Resultado esperado: `BUILD SUCCESS`.
