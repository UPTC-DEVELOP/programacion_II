# Tienda Virtual de Libros

Aplicación de escritorio desarrollada en Java 17, Swing y programación orientada a objetos para la asignatura Programación II de la UPTC.

## Arquitectura

El proyecto usa MVC modificado con separación por responsabilidades:

```text
co.edu.uptc/
├── model/         Entidades, herencia y polimorfismo
├── exception/     Excepciones propias del dominio
├── persistence/   Interfaces de repositorios
│   ├── json/      Implementaciones de almacenamiento JSON
│   └── txt/       Registro de operaciones
├── service/       Reglas de negocio y casos de uso
├── controller/    Eventos Swing y coordinación
├── view/          Ventanas, paneles y diálogos
└── util/          Validación, hash, rutas, fechas y moneda
```

`Main.java` es la única raíz de composición. Allí se crean las implementaciones concretas y se inyectan mediante constructores.

## Funcionalidades

- CRUD de libros para el administrador.
- Validación de ISBN, precio, stock, IVA y descuentos.
- Registro e inicio de sesión de clientes.
- Clientes Regular y Premium.
- Contraseñas protegidas con SHA-256.
- Carrito persistente por correo electrónico.
- Compra desde carrito y compra rápida.
- Cálculo separado de subtotal, IVA y total bruto.
- Descuento Premium del 10 %.
- Descuento adicional del 5 % desde el cuarto pedido.
- Actualización automática del inventario.
- Cancelación de pedidos con reposición de stock.
- Historial de compras.
- Recibos TXT.
- Registro de operaciones TXT.
- Reseñas de libros comprados.
- Eventos Swing mediante `ActionListener`, `ItemListener` y `ListSelectionListener`.

## Reglas principales

El precio base se considera antes de IVA:

```text
precioConDescuento = precioBase * (1 - descuentoProducto / 100)
subtotalLinea = precioConDescuento * cantidad
impuestoLinea = subtotalLinea * IVA / 100
totalBruto = subtotal + impuestos
descuentoCliente = cliente.calcularDescuento(totalBruto)
total = totalBruto - descuentos
```

Ejemplo oficial:

```text
2 × Cien años de soledad a $50.000
Subtotal: $100.000
IVA 19 %: $19.000
Total bruto: $119.000
Descuento Premium: $11.900
Total a pagar: $107.100
```

## Usuarios semilla

| Rol | Correo | Contraseña |
|---|---|---|
| Administrador | `admin@libreria.com` | `Admin123` |
| Cliente Regular | `ana@correo.com` | `Ana12345` |
| Cliente Premium | `carlos@correo.com` | `Carlos123` |

Los archivos JSON comienzan como `[]`. Al iniciar, los repositorios crean los datos semilla si están vacíos.

## Ejecución desde PowerShell

Desde la raíz del proyecto:

```powershell
Remove-Item bin -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory bin
$files = Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName
javac -encoding UTF-8 -d bin $files
java -cp bin co.edu.uptc.Main
```

También se puede importar como proyecto Java en Eclipse usando `.project` y `.classpath`.

## Persistencia

```text
data/libros.json
data/clientes.json
data/pedidos.json
data/carritos.json
data/resenas.json
data/registro_operaciones.txt
data/recibos/
```

La serialización JSON está aislada en `persistence.json.ArchivoJson`. El entorno usado para construir esta entrega no tenía disponible Gson 2.10.1 ni pudo descargarlo por TLS, por eso el adaptador actual no depende de librerías externas. Si se agrega `lib/gson-2.10.1.jar` en Eclipse, solo se reemplaza esta subcapa; el modelo y los servicios no cambian.

## SOLID

- **S:** cada servicio, repositorio, vista y controlador tiene una responsabilidad definida.
- **O:** nuevos formatos, tipos de cliente, políticas y repositorios se agregan mediante herencia o interfaces.
- **L:** libros y clientes concretos se usan mediante sus abstracciones.
- **I:** los repositorios son interfaces pequeñas y específicas.
- **D:** los servicios dependen de contratos; `Main` inyecta las implementaciones.

## Evidencia del módulo Git

La carpeta `docs/git` contiene las versiones de `Producto.java`:

- Base.
- Estudiante A con descuento.
- Estudiante B con IVA.
- Archivo con conflicto.
- Resolución final.
- Guía de comandos Git.

## Pruebas ejecutadas

```text
PHASE1_MODEL_OK
PHASE2_UTILS_OK
PHASE3_PERSISTENCE_OK
PHASE4_SERVICES_OK
PHASE5_VIEW_OK
PHASE6_CONTROLLERS_WIRED_OK
PHASE8_REVIEW_FREQUENT_OK
```

El detalle de la revisión final está en `docs/FASE_7_VALIDACION.md`.

## Manual de aprendizaje

El manual completo para estudiar Java, POO, SOLID, arquitectura, Swing, persistencia, pruebas y reconstrucción de la aplicación está en:

`docs/MANUAL_DEFINITIVO_APRENDIZAJE.md`
