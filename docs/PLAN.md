# Plan: Completar el Sistema de Ventas

**Fecha:** 2026-08-01
**Proyecto:** Sistema de Ventas (
**Repo:** https://github.com/jzetinob/sistema-ventas
**Documentación completa:** [README.md](../README.md) · [ARQUITECTURA.md](ARQUITECTURA.md)

## Estado actual

| Fase | Estado | Commit |
|---|---|---|
| 1 — Persistencia CSV | ✅ Completada | `98b6107` |
| 2 — Lista de facturas completa | ✅ Completada | `0ff5b04` |
| 3 — Catálogos | ✅ Completada | `397e539` |
| 4 — Combos en la factura | ✅ Completada | `f0c5f20` |
| 5 — Impresión con vista previa | ✅ Completada | `956c633` |
| 6 — Validaciones | ✅ Completada | `b0690ec` |
| 7 — Pruebas y entrega | ⏳ En curso | — |

Este archivo es el **historial de decisiones** (el "por qué" de cada cosa). La descripción técnica de cómo está implementado vive en [ARQUITECTURA.md](ARQUITECTURA.md).

## Cómo saber si una fase está terminada

Regla de cierre de fase (se cumple cuando el commit de la fase existe):

1. **Compila** — el proyecto compila sin errores.
2. **Se probó** — el flujo correspondiente se probó (en NetBeans o con prueba automatizada).
3. **Estado actualizado** — la tabla de estado de este archivo tiene la fase en "✅ Completada".
4. **Commit hecho** — el historial de git tiene el commit de la fase (todos se nombran con "Fase N: ...").

Si la fase aparece "✅ Completada" en la tabla y su commit está en `git log`, **no se vuelve a ejecutar**: ya está implementada. Los fixes posteriores a una fase terminada van en commits separados y se anotan en "Correcciones posteriores al plan".

Para verificar rápido:

```
git status                     # debe estar limpio si todo lo planeado está subido
git log --oneline              # muestra los commits de cada fase
```

## Contexto
El objetivo era un formulario de facturación con menú principal (Archivo, Edición, Ayuda en español). El software ya tiene: ventana principal con menú, factura funcional (cliente, fecha, número, tabla de productos, total, Agregar/Eliminar/Guardar) y lista de facturas registradas. Se decidió completarlo para que sea un sistema de ventas completo, con persistencia de datos.

## Decisiones tomadas ()
1. **Persistencia en CSV / texto plano** (sin librerías) como puente hasta SQLite. Cuando se necesite base de datos, solo se crea un DAO nuevo (`FacturaDAOSQLite`) y se cambia una línea en el controlador; la interfaz `FacturaDAO` ya lo permite.
2. **Catálogos integrados con la factura mediante combos** (JComboBox): el producto se elige del catálogo y el precio se autollena; el cliente se autollena con su NIT.
3. **Impresión con vista previa**: se dibuja el ticket con Graphics2D y se imprime con el diálogo estándar de Windows (`java.awt.print.Printable`), sin dependencias externas.
4. Todo sigue el patrón **MVC** (modelo, dao, controlador, vista) usado en el proyecto y las relaciones de asociación, agregación y composición se conservan.

## Detalle de implementación importante
`FacturaDetalle` es una clase anidada package-private (composición). El DAO (paquete `dao`) y las vistas (paquete `vista`) no pueden iterar sus campos directamente. Por eso `Factura` ganará un método público `getDetallesFilas()` que devuelve `Object[][]` (producto, cantidad, precio, subtotal), manteniendo el encapsulamiento.

## Fases

### Fase 1 — Persistencia CSV
- `dao/CsvUtil.java`: escapar/parsear campos separados con `;` (evita romper si un campo trae `;` o salto de línea).
- `dao/FacturaDAOCsv.java` (patrón Singleton): carga las CSV al arrancar; en cada guardar/eliminar reescribe:
  - `datos/facturas.csv` → `id;numeroFactura;nit;cliente;fecha;total`
  - `datos/detalles.csv` → `idFactura;producto;cantidad;precio;subtotal`
  - `datos/contador.txt` → próximo id y próximo correlativo (para que el número de factura no se repita aunque se elimine la última).
- `FacturaDAO.java`: se agregan `obtenerSiguienteNumeroFactura()` y `eliminarConDetalles(int id)` a la interfaz.
- `FacturaController.java`: usa `FacturaDAOCsv`.
- `FrmFactura.java`: el número de factura se autollena (`FAC-0001`, `FAC-0002`, ...) y queda solo lectura.
- `.gitignore`: se ignora la carpeta `datos/` (los datos no se suben al repo).

### Fase 2 — Lista de facturas completa
- `FrmListaFacturas.java`: botones **Eliminar** (con confirmación, llama `EliminarConDetalles`), **Ver Detalle** y **Actualizar**.
- Nueva `vista/FrmDetalleFactura.java`: muestra datos del cliente y la tabla de productos de la factura seleccionada (usa `getDetallesFilas()`).

### Fase 3 — Catálogos (Productos y Clientes)
- `modelo/Producto.java` (id, código, nombre, precio) y `modelo/Cliente.java` (id, nit, nombre, dirección, teléfono).
- DAOs CSV + controllers con CRUD completo (Guardar, Listar, Actualizar, Eliminar). Validación de código/NIT únicos.
- `vista/FrmProductos.java` y `vista/FrmClientes.java`: formularios tipo CRUD con tabla.
- `FrmPrincipal.java`: nuevo menú **Catálogos** → Productos / Clientes.

### Fase 4 — Combos en la factura
- `FrmFactura.java`: `txtProducto` se convierte en `cmbProducto` (muestra "código - nombre"; al elegir autollena el precio). `cmbCliente` autollena NIT + nombre. Los campos siguen siendo editables para entrada manual.

### Fase 5 — Impresión con vista previa
- `vista/TicketFactura.java`: implementa `java.awt.print.Printable`; dibuja el ticket (cliente, fecha, número, productos, total).
- `vista/FrmVistaPreviaFactura.java`: diálogo que pinta el ticket (mismo painter) + botón **Imprimir**.
- Botón **Imprimir** en `FrmFactura`; el ticket también se puede imprimir desde el detalle de factura.

### Fase 6 — Validaciones
- NIT con formato guatemalteco (8-13 dígitos, guiones opcionales).
- Número de factura duplicado.
- Producto duplicado en la misma factura.
- Código de producto y NIT de cliente duplicados en catálogos.

### Fase 7 — Pruebas y entrega
- Compilar después de cada fase (`javac` manual, sin Maven local) y probar el flujo completo en NetBeans.
- Commits incrementales y push a `origin/main`.
- Capturas de cada pantalla y PDF final con explicación (asociación, agregación, composición, singleton, persistencia CSV, impresión).

## Notas para el futuro (SQLite)
- Agregar dependencia `org.xerial:sqlite-jdbc` al `pom.xml`.
- Crear `FacturaDAOSQLite` (y análogos para Producto/Cliente) que implementen las mismas interfaces.
- Cambiar la instancia usada en los controllers (1 línea por controller).
- El resto del código no se toca.

## Correcciones posteriores al plan
- **Commit `53338aa`**: se corrigió el orden de inicialización del singleton CSV (la instancia se creaba antes que las constantes de rutas, provocando NPE) y el correlativo de factura ahora se consume al pre-fill (evita números repetidos).
- La persistencia se verificó con una prueba automatizada: guardar → simular reinicio (nueva JVM) → los datos se recuperan y el correlativo continúa (FAC-0002).
- **Buscador inteligente (commit `14b523b`)**: la decisión 2 (combos) se reemplazó por el componente reutilizable `CampoBusqueda` (autocompletado): se escribe y filtra por código/NIT o nombre, con flechas + Enter o clic para elegir. Motivo: con catálogos grandes, un desplegable no es productivo. Si algún día hay millones de registros, la búsqueda real debe pasar a SQLite con `LIKE`.
