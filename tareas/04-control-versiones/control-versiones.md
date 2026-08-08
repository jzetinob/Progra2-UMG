# Publicación y Control de Versiones del Proyecto

**Tarea 4 — Programación II**
**Estudiante:** Josue Zetino
**Repositorio público:** https://github.com/jzetinob/sistema-ventas

---

## 1. Objetivo

Utilizar Git y GitHub como herramienta de control de versiones para documentar el desarrollo del Sistema de Ventas durante el semestre, manteniendo un único repositorio con todo el avance registrado.

## 2. Repositorio

- **Enlace:** https://github.com/jzetinob/sistema-ventas
- **Rama principal:** `main`
- **Historial:** 35 commits con mensajes descriptivos que reflejan cada avance del proyecto.
- **Visibilidad:** pública (accesible para el docente sin necesidad de cuenta).

## 3. Contenido del repositorio

| Elemento | Descripción |
|---|---|
| `sistema-ventas/` | Código fuente completo del proyecto (Java Swing + Maven) |
| `sistema-ventas/pom.xml` | Estructura del proyecto Maven de NetBeans + dependencia del driver JDBC de SQLite |
| `sistema-ventas/nbactions.xml` | Configuración de ejecución de NetBeans |
| `README.md` | Índice maestro del proyecto: entregas, requisitos, cómo ejecutar y arquitectura |
| `docs/` | Documentación: PLAN.md (historial de decisiones), ARQUITECTURA.md, mini-tutorial |
| `tareas/` | Entregables del semestre (documentos, presentaciones y PDF de cada tarea) |
| `.gitignore` | Excluye archivos que no deben subirse (`datos/`, `capturas/`, `target/`, `build/`) |

> La base de datos (`datos/`) y las capturas de pantalla (`capturas/`) están excluidas del repositorio a propósito: son archivos locales de trabajo que no forman parte del código entregado.

## 4. Historial de commits

Los 35 commits del repositorio documentan el avance por fases (los más representativos):

| Fase | Commit (resumen) |
|---|---|
| Base | `Initial commit`, `Proyecto base del sistema de ventas` |
| Formulario de facturación | `Add FrmFactura.java - Sistema de Facturación con Java Swing`, `Aplicar composicion Factura-FacturaDetalle`, `Agregar ventana principal con menu` |
| Tarea 1 — Persistencia | `Fase 1: persistencia CSV`, `Fase 2: lista de facturas`, `Fase 3: catalogos`, `Fase 4: combos`, `Fase 5: impresion`, `Fase 6: validaciones` |
| Mejoras | `Buscador con autocompletado`, `Mejoras: correlativo sin quemar, formato de precios, logging` |
| Tarea 2 — MDI | `MDI: convertir la app a formulario contenedor con JDesktopPane y JInternalFrame` |
| Tarea 3 — Base de datos | `Fase 9: integrar base de datos SQLite con JDBC` |
| Entregables | `Entregable final tarea 1/2/3: presentacion y PDF` |

Cada tarea tiene sus commits de **implementación** y de **entregable final**, lo que evidencia el flujo completo: programar → documentar → publicar.

## 5. Capturas solicitadas

> (se insertan aquí las capturas del historial de commits y del contenido del repositorio en GitHub)

## 6. Trabajo realizado en esta etapa

Esta etapa se dedicó a consolidar el control de versiones del proyecto:

1. **Un único repositorio** para todo el semestre, con la rama `main` como línea principal de desarrollo.
2. **Commits frecuentes y descriptivos**: cada funcionalidad se registró por separado (fases 1 a 9) junto con sus correcciones y documentación.
3. **Organización del repositorio**: estructura de carpetas por tarea (`tareas/NN-nombre/`), índice en el `README.md` y documentación técnica en `docs/`.
4. **Exclusión de archivos innecesarios** con `.gitignore` (base de datos local, capturas, binarios de compilación).
5. **Publicación**: todos los avances se subieron al repositorio público en GitHub, verificando el estado con `git status` antes de cada push.

## 7. Funcionalidades implementadas (resumen del semestre)

- **Formulario de facturación** (tarea 1): cliente, NIT, fecha automática, número de factura automático (FAC-0001...), tabla de productos con subtotales y total, imprimir con vista previa y validaciones.
- **Formulario contenedor MDI** (tarea 2): menú principal con escritorio de ventanas internas (`JDesktopPane` + `JInternalFrame`), cascada y mosaico.
- **Integración con base de datos** (tarea 3): SQLite con JDBC, 4 tablas relacionales, persistencia permanente verificada al cerrar y reabrir la aplicación.

## 8. Conclusiones

- Git y GitHub permitieron mantener un historial completo y verificable de todo el desarrollo del semestre.
- Los commits con mensajes descriptivos hacen que cualquier persona pueda entender la evolución del proyecto.
- El repositorio público es el medio de entrega: el docente puede clonarlo, compilarlo y revisar el historial.
