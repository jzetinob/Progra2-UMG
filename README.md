# Sistema de Ventas

Sistema de facturación de escritorio en Java Swing con patrón MVC — Sistema de facturación de escritorio en **Java Swing** con patrón **MVC**.

## ¿Qué hace?

- **Ventana principal** con menú clásico: Archivo, Catálogos, Edición y Ayuda (en español).
- **Facturación**: cliente, NIT, fecha automática, número de factura automático (FAC-0001, FAC-0002, ...), tabla de productos con subtotales y total, botones Agregar / Eliminar / Guardar / Imprimir.
- **Catálogos**: CRUD completo de productos (código, nombre, precio) y clientes (NIT, nombre, dirección, teléfono), integrados con la factura mediante un **buscador con autocompletado** (filtra por código/NIT o nombre mientras escribes, sin depender del tamaño del catálogo).
- **Persistencia**: los datos se guardan en archivos CSV dentro de la carpeta `datos/` (no se pierden al cerrar el programa).
- **Impresión**: vista previa del ticket y envío a la impresora con el diálogo estándar de Windows (`java.awt.print`, sin librerías externas).
- **Validaciones**: NIT guatemalteco (8-13 dígitos), números de factura únicos, productos sin repetir, código/NIT únicos en catálogos.

## Requisitos

- JDK 25 (el proyecto compila con `maven.compiler.release 26`... si tu NetBeans no lo acepta, bájalo en `pom.xml`).
- NetBeans 22+ con soporte Maven.

## Cómo ejecutar

1. Clona el repositorio y ábrelo en NetBeans (proyecto Maven: `sistema-ventas`).
2. Ejecuta el proyecto (main class: `com.josue.ventas.SistemaVentas`) o corre `FrmPrincipal.java`.
3. Para ver los datos guardados, abre la carpeta `datos/` del directorio de trabajo (los .csv se pueden abrir en Excel).

## Arquitectura (resumen)

Patrón **MVC** en 4 paquetes bajo `com.josue.ventas`:

```
┌───────────────┐      ┌──────────────────┐      ┌────────────────────┐
│  vista        │ ───► │  controlador     │ ───► │  dao               │
│  (JFrames)    │      │  (Controllers)   │      │  (DAO + CSV)       │
└───────────────┘      └──────────────────┘      └────────┬───────────┘
                                                          │
                                              ┌───────────▼───────────┐
                                              │  modelo               │
                                              │  (Factura, Producto…) │
                                              └───────────────────────┘
```

- Relaciones: **asociación** (vista → controlador → dao), **agregación** (Factura contiene una lista de detalles) y **composición** (los detalles se crean y destruyen dentro de la Factura).
- Los DAO son **singleton** y se guardan en **CSV** para poder reemplazarlos por SQLite en el futuro (mismas interfaces).

Documentación detallada en [`docs/`](docs/).

## Historial de desarrollo

| Fase | Qué se hizo | Commit |
|---|---|---|
| — | Formulario de facturación base + menú principal | `61ee3da` |
| — | Lista de facturas registradas | `8d6ee12` |
| 1 | Persistencia CSV + número de factura automático | `98b6107` |
| 2 | Lista completa: eliminar, ver detalle, actualizar | `0ff5b04` |
| 3 | Catálogos de productos y clientes | `397e539` |
| 4 | Combos de catálogo en la factura | `f0c5f20` |
| 5 | Impresión con vista previa | `956c633` |
| 6 | Validaciones | `b0690ec` |
| — | Fix: inicialización del singleton y correlativo | `53338aa` |

## Documentación

- [`docs/mini-tutorial.md`](docs/mini-tutorial.md) — tutorial paso a paso de cómo usar la app.
- [`docs/PLAN.md`](docs/PLAN.md) — plan de desarrollo (decisiones y fases).
- [`docs/ARQUITECTURA.md`](docs/ARQUITECTURA.md) — arquitectura detallada.
- Los `.md` futuros se agregan en `docs/`.

## Futuro: SQLite

Cuando se necesite base de datos: agregar `org.xerial:sqlite-jdbc` al `pom.xml`, crear DAOs que implementen las mismas interfaces y cambiar una línea en cada controller. Más detalles en [`docs/PLAN.md`](docs/PLAN.md).
