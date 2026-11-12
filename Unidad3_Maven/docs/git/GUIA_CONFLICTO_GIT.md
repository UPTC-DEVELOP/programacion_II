# Evidencia del Módulo A — Conflicto en `Producto.java`

Los archivos de esta carpeta muestran el proceso solicitado:

1. `Producto_base.java`: versión inicial con precio base.
2. `Producto_estudiante_A.java`: agrega `porcentajeDescuento`.
3. `Producto_estudiante_B.java`: agrega `impuestoIVA` sobre las mismas líneas.
4. `Producto_conflicto.java`: representa los marcadores que Git muestra al fusionar.
5. `Producto_resuelto.java`: conserva descuento e IVA y aplica ambas fórmulas.

## Secuencia de comandos

```bash
git init
git add .gitignore src docs README.md
git commit -m "chore: inicia tienda virtual de libros"
git branch -M main
git remote add origin URL_DEL_REPOSITORIO
git push -u origin main

git checkout -b feature/gestion-productos
git push -u origin feature/gestion-productos
```

El estudiante A edita `Producto.java` para agregar descuento:

```bash
git add src/co/edu/uptc/model/Producto.java
git commit -m "feat(producto): agrega porcentajeDescuento"
git push
```

El estudiante B parte de la versión anterior, modifica las mismas líneas para agregar IVA y realiza su commit:

```bash
git add src/co/edu/uptc/model/Producto.java
git commit -m "feat(producto): agrega impuestoIVA"
git pull origin feature/gestion-productos
```

Git marca el conflicto con `<<<<<<<`, `=======` y `>>>>>>>`. Se conservan los dos atributos y se resuelve el cálculo:

```java
double precioConDescuento = precioBase * (1 - porcentajeDescuento / 100.0);
return precioConDescuento * (1 + impuestoIVA / 100.0);
```

Después de revisar el archivo:

```bash
git add src/co/edu/uptc/model/Producto.java
git commit -m "merge: resuelve conflicto en Producto.java (descuento + IVA)"
git push origin feature/gestion-productos

git checkout main
git merge feature/gestion-productos
git push origin main
```

## Plan de commits del proyecto

```text
feat(model): crea entidades y polimorfismo de productos y clientes
feat(util): agrega validadores, rutas, moneda y seguridad
feat(persistencia): agrega repositorios JSON y registro TXT
feat(service): implementa catálogo, autenticación, carrito y compras
feat(view): crea ventanas y paneles Swing
feat(controller): conecta eventos con servicios
feat(resenas): agrega reseñas de libros comprados
feat(descuento): agrega política de cliente frecuente
```
