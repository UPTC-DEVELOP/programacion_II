# Tienda Virtual de Libros

Aplicación de escritorio en Java (Swing) para la gestión de una tienda virtual de libros.

Proyecto académico — Programación II, 2026-II.

## Integrantes — Grupo 5

- Juan Pablo Barrero
- Camilo
- Liz Rojas

## Descripción

Permite administrar el catálogo de libros y los clientes, gestionar un carrito de
compras y registrar las compras con su recibo.

## Arquitectura

Arquitectura en capas:

- `src/gui` — interfaz gráfica (ventanas, paneles, eventos).
- `src/negocio` — lógica y reglas del dominio.
- `src/persistencia` — almacenamiento en archivos.

## Módulos

| Módulo | Responsable | Clases |
|--------|-------------|--------|
| Libros | Camilo | `Libro`, `FormatoLibro` |
| Clientes | Juan Pablo Barrero | `Cliente`, `ClienteRegular`, `ClientePremium` |
| Compras | Liz Rojas | `CarritoCompras`, `Compra`, `ItemCompra`, `MetodoPago` |

## Requisitos

Java 8 o superior.

## Compilación y ejecución

```bat
javac -d bin src\negocio\*.java src\persistencia\*.java src\gui\*.java
java -cp bin gui.Main
```

## Estructura

```
src/gui
src/negocio
src/persistencia
diseno/
prototipos/
```
