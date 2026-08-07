# Explicación breve del desarrollo

El trabajo consistió en implementar el formulario de facturación del Sistema de Ventas en Java Swing, partiendo de la estructura del proyecto dado en clase.

## Qué se hizo

- **Formulario de facturación** (`FrmFactura`): captura del cliente (nombre y NIT), fecha automática, número de factura generado automáticamente (FAC-0001, FAC-0002, ...), tabla de productos con cantidad, precio y subtotal, total de la venta calculado al vuelo y botones Agregar / Eliminar / Guardar. También se agregó un botón Imprimir con vista previa del ticket.
- **Catálogos**: productos (código, nombre, precio) y clientes (NIT, nombre, dirección, teléfono) con mantenimiento completo (agregar, editar, eliminar).
- **Búsqueda con autocompletado**: en la factura se escribe el código/NIT o nombre y el sistema sugiere coincidencias del catálogo, autollenando precio y datos del cliente.
- **Persistencia**: los datos se guardan en archivos CSV en la carpeta `datos/`, por lo que no se pierden al cerrar el programa.
- **Validaciones**: NIT de 8 a 13 dígitos, números de factura únicos, sin productos repetidos en una misma factura.

## Cómo está organizado

El proyecto sigue el patrón MVC en cuatro paquetes: `vista` (las ventanas), `controlador` (la lógica intermedia), `dao` (acceso a datos en CSV) y `modelo` (las clases Factura, Producto y Cliente). La factura contiene sus detalles como composición: los detalles se crean y eliminan dentro de la misma factura, y el total se recalcula automáticamente cada vez que se agrega o quita un producto.

## Dificultades encontradas

- Entender cómo enlazar la factura con los catálogos: al principio se usaron combos, pero con varios productos era incómodo, así que se reemplazaron por un buscador con autocompletado.
- El número de factura: hubo que guardar un contador aparte para que el correlativo siguiera en orden aunque se eliminara la última factura, y corregir un error donde se "quemaban" números al abrir y cerrar el formulario sin guardar.
- La persistencia en CSV: los datos con puntos y comas necesitaban escaparse para no romper el archivo al leerlos de nuevo.
- Ajustar la compilación del proyecto al JDK instalado (la configuración inicial pedía una versión de Java más nueva de la disponible).
