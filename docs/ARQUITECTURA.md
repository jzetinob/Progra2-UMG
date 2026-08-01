# Arquitectura del Sistema de Ventas

## Patrón de diseño: MVC (Modelo-Vista-Controlador)

El proyecto se divide en 4 paquetes bajo `com.josue.ventas`:

| Paquete | Responsabilidad | Clases |
|---|---|---|
| `modelo` | Datos de negocio (entidades) | `Factura` (+ clase anidada `FacturaDetalle`), `Producto`, `Cliente` |
| `dao` | Acceso a datos (persistencia) | `FacturaDAO`, `FacturaDAOCsv`, `ProductoDAO`, `ProductoDAOCsv`, `ClienteDAO`, `ClienteDAOCsv`, `CsvUtil` |
| `controlador` | Lógica entre vista y DAO | `FacturaController`, `ProductoController`, `ClienteController` |
| `vista` | Interfaces gráficas (JFrames) | `FrmPrincipal`, `FrmFactura`, `FrmListaFacturas`, `FrmDetalleFactura`, `FrmProductos`, `FrmClientes`, `FrmVistaPreviaFactura`, `TicketFactura`, `CampoBusqueda` |

Flujo de datos:

```
Vista (JFrame)
   │ 1. el usuario interactúa
   ▼
Controlador  ──►  DAO (interfaz)
                     │ 2. implementación actual: CSV (singleton)
                     ▼
                 Archivos en datos/
```

## Relaciones entre objetos

### Asociación
Cada vista conoce a su controlador, y cada controlador conoce a su DAO:

```
FrmFactura ──usa──► FacturaController ──usa──► FacturaDAO
FrmProductos ──usa──► ProductoController ──usa──► ProductoDAO
```

Ambos pueden existir por separado: son "amigos" que se pasan mensajes. Es la relación más débil.

### Agregación ("tiene un" / parte-todo débil)
`Factura` contiene una `List<FacturaDetalle>`. Los detalles pueden existir conceptualmente fuera de la factura y se agregan con `agregarDetalle(...)`.

### Composición ("es parte de" / parte-todo fuerte)
`FacturaDetalle` es una **clase anidada** dentro de `Factura`, sin modificador de acceso (solo la propia Factura puede manipular los detalles). El ciclo de vida del detalle depende 100 % de la Factura:

- Se crean solo con `Factura.agregarDetalle(String producto, int cantidad, double precio)`.
- Se eliminan con `Factura.eliminarDetalle(int index)`.
- Si la factura desaparece, los detalles desaparecen con ella.

La Factura expone los datos de sus detalles mediante `getDetallesFilas()` (devuelve `Object[][]`), así las otras capas no necesitan tocar la clase interna: encapsulamiento + composición.

### Patrón Singleton
Cada DAO CSV expone una única instancia compartida con `getInstancia()` y constructor privado:

```java
private static final FacturaDAOCsv instancia = new FacturaDAOCsv();
private FacturaDAOCsv() { cargar(); }
public static FacturaDAOCsv getInstancia() { return instancia; }
```

Gracias a esto, todas las ventanas del programa ven los mismos datos (un `FrmFactura` guarda y `FrmListaFacturas` ve lo guardado).

> Detalle importante: la constante `instancia` se declara DESPUÉS de las constantes de rutas de archivos. Si se declara antes, el constructor se ejecuta cuando las rutas aún son `null` (error de orden de inicialización estática que se corrigió más adelante en el historial).

## Persistencia en CSV

Carpeta `datos/` (creada automáticamente en el directorio de trabajo, ignorada por git):

| Archivo | Formato (separador `;`) | Ejemplo |
|---|---|---|
| `facturas.csv` | `id;numero;nit;cliente;fecha;total` | `1;FAC-0001;123456789;Juan Perez;2026-08-01 16:29:07;22.5` |
| `detalles.csv` | `idFactura;producto;cantidad;precio;subtotal` | `1;Manzana;3;2.5;7.5` |
| `productos.csv` | `id;codigo;nombre;precio` | `1;P01;Manzana;2.5` |
| `clientes.csv` | `id;nit;nombre;direccion;telefono` | `1;123456789;Juan Perez;Zona 1;5555-5555` |
| `contador.txt` | próximo correlativo de factura | `2` |

Mecánica:
- Al arrancar, el DAO carga todos los archivos a memoria.
- En cada guardar/eliminar/actualizar reescribe los archivos completos (datos pequeños, reescritura total = simplicidad).
- `CsvUtil` escapa con comillas los campos que contienen `;`, comillas o saltos de línea, y parsea las líneas respetando las comillas.

## Número de factura automático

- `FacturaDAO.obtenerSiguienteNumeroFactura()` devuelve el siguiente correlativo (`FAC-%04d`) y **lo consume** (incrementa el contador), para que dos facturas abiertas no repitan número.
- `FrmFactura` lo muestra en un campo solo lectura y lo renueva al limpiar el formulario.
- El contador se persiste en `contador.txt`, así el número no se repite ni después de reiniciar (ni si se elimina la última factura).

## Impresión

- `TicketFactura` implementa `java.awt.print.Printable` y dibuja el ticket (encabezado, cliente, NIT, fecha, número, productos, total, pie) con `Graphics2D`.
- `FrmVistaPreviaFactura` es un diálogo modal que pinta el ticket con el mismo método `pintar(...)` y ofrece el botón **Imprimir**, que abre el diálogo estándar del sistema (`PrinterJob.printDialog()`).
- Sin librerías externas: todo es JDK puro.
- Se puede imprimir desde `FrmFactura` (factura en curso) y desde `FrmDetalleFactura` (factura guardada).

## Ventana principal

`FrmPrincipal` (título "Sistema de Ventas") es el punto de entrada (`SistemaVentas.main`):

- **Archivo**: Nueva Factura (Ctrl+N), Ver Facturas, Salir (Ctrl+Q, con confirmación).
- **Catálogos**: Productos, Clientes.
- **Edición**: Limpiar Formulario (limpia la factura abierta).
- **Ayuda**: Acerca de.

Las ventanas secundarias se abren centradas respecto a la principal y se reutilizan si ya están abiertas (`isDisplayable()`).

## Buscador con autocompletado

`CampoBusqueda` es un componente reutilizable (JTextField + popup + JList) que filtra a medida que se escribe (contiene, sin distinguir mayúsculas):

- Flechas ↑/↓ para moverse, Enter para elegir, Escape para cerrar el popup.
- Al elegir un cliente, se autollenan NIT y nombre; al elegir un producto, se autollena el precio.
- Se usa en `FrmFactura` para cliente y producto; funciona aunque el catálogo sea grande.

## Futuro: SQLite

Las interfaces (`FacturaDAO`, `ProductoDAO`, `ClienteDAO`) permiten cambiar la persistencia sin tocar vistas ni controladores:

1. Agregar `org.xerial:sqlite-jdbc` al `pom.xml`.
2. Crear `FacturaDAOSQLite` (y análogos) que implementen las mismas interfaces.
3. Cambiar la línea `dao = ...getInstancia()` en cada controller.
