# Mini Tutorial: Cómo usar el Sistema de Ventas

El orden correcto es: **primero el catálogo, después la factura** (la factura usa combos que se llenan del catálogo).

## 1. Arrancar

Corre el proyecto en NetBeans (o `FrmPrincipal.java` → Run File). Se abre la ventana **"Sistema de Ventas"**.

## 2. Crear productos (obligatorio antes de facturar)

- Menú **Catálogos → Productos**.
- Escribe: Código (p. ej. `P01`), Nombre (`Manzana`), Precio (`2.50`).
- Clic en **Guardar**. Repite para 2-3 productos.

## 3. Crear clientes

- Menú **Catálogos → Clientes**.
- Escribe: NIT (8-13 dígitos, guiones opcionales: `123456789`) y Nombre.
- Clic en **Guardar**.

## 4. Crear la factura

- Menú **Archivo → Nueva Factura**:
  - En el combo **Cliente del catálogo** elige el cliente → autollena NIT y nombre (aún puedes editarlos).
  - El **número de factura** ya viene solo (`FAC-0001`, no editable).
  - En **Agregar Producto**: elige el producto del combo → el **precio se autollena** → escribe la **Cantidad** → clic en **Agregar**. El total se actualiza solo.
  - Clic en **Guardar**.

## 5. Ver lo guardado

- Menú **Archivo → Ver Facturas**: ahí aparecen todas las facturas.
- Con una fila seleccionada puedes:
  - **Ver Detalle** → productos y datos de esa factura (también puedes imprimirla desde ahí).
  - **Imprimir** → vista previa del ticket, luego el diálogo de impresión de Windows.
  - **Eliminar** → pide confirmación.
  - **Actualizar** → recarga la lista (útil si guardaste facturas con la lista abierta).

## 6. Cerrar y volver a abrir

Los datos **no se pierden**: se guardan en archivos CSV dentro de la carpeta `datos/` del proyecto (se pueden abrir en Excel).

## Pistas

- Si el combo de producto está vacío, todavía no hay productos en el catálogo. El orden siempre es: **catálogo → factura**.
- El NIT debe tener entre 8 y 13 dígitos; los guiones son opcionales.
- No se puede agregar dos veces el mismo producto a una factura.
