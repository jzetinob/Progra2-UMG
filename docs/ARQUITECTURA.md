# Arquitectura del Sistema de Ventas

## Patrón de diseño: MVC (Modelo-Vista-Controlador)

El proyecto se divide en 4 paquetes bajo `com.josue.ventas`:

| Paquete | Responsabilidad | Clases |
|---|---|---|
| `modelo` | Datos de negocio (entidades) | `Factura` (+ clase anidada `FacturaDetalle`), `Producto`, `Cliente` |
| `dao` | Acceso a datos (persistencia) | `ConexionBD`, `MigradorDatos`, `FacturaDAO`, `FacturaDAOSQLite`, `ProductoDAO`, `ProductoDAOSQLite`, `ClienteDAO`, `ClienteDAOSQLite` (quedan como referencia: `FacturaDAOCsv`, `ProductoDAOCsv`, `ClienteDAOCsv`, `CsvUtil`) |
| `controlador` | Lógica entre vista y DAO | `FacturaController`, `ProductoController`, `ClienteController` |
| `vista` | Interfaces gráficas (JFrames) | `FrmPrincipal`, `FrmFactura`, `FrmListaFacturas`, `FrmDetalleFactura`, `FrmProductos`, `FrmClientes`, `FrmVistaPreviaFactura`, `TicketFactura`, `CampoBusqueda` |

Flujo de datos:

```
Vista (JFrame)
   │ 1. el usuario interactúa
   ▼
Controlador  ──►  DAO (interfaz)
                     │ 2. implementación actual: SQLite con JDBC (singleton)
                     ▼
                 Base de datos datos/sistema_ventas.db
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
Cada DAO expone una única instancia compartida con `getInstancia()` y constructor privado:

```java
private static final FacturaDAOSQLite instancia = new FacturaDAOSQLite();
private FacturaDAOSQLite() { }
public static FacturaDAOSQLite getInstancia() { return instancia; }
```

`ConexionBD` también es singleton: abre una sola conexión JDBC al iniciar la aplicación y todos los DAOs la reutilizan (`ConexionBD.getInstancia().getConnection()`).

Gracias a esto, todas las ventanas del programa ven los mismos datos (un `FrmFactura` guarda y `FrmListaFacturas` ve lo guardado).

## Persistencia en SQLite (desde la Fase 9)

La base de datos es un archivo `datos/sistema_ventas.db` (carpeta `datos/` creada automáticamente en el directorio de trabajo, ignorada por git). Esquema:

| Tabla | Columnas | Restricciones |
|---|---|---|
| `clientes` | id, nit, nombre, direccion, telefono | PK id autoincremental, UNIQUE nit |
| `productos` | id, codigo, nombre, precio | PK id autoincremental, UNIQUE codigo |
| `facturas` | id, numero_factura, nit, cliente, fecha, total | PK id autoincremental, UNIQUE numero_factura |
| `factura_detalles` | id, factura_id, producto, cantidad, precio, subtotal | FK factura_id → facturas ON DELETE CASCADE |

Mecánica:
- `ConexionBD` carga el driver (`org.sqlite.JDBC`), abre la conexión y ejecuta `CREATE TABLE IF NOT EXISTS` al arrancar.
- `MigradorDatos` importa los CSV de las fases anteriores a la BD (una sola vez, si la BD está vacía).
- Los DAOs SQLite usan `PreparedStatement` (protege contra inyección SQL) y `RETURN_GENERATED_KEYS` para recuperar el id generado.
- Guardar/actualizar factura corre en una **transacción**: factura + detalles se insertan juntos, con `commit`/`rollback`.

> **Histórico (reemplazado):** en las fases 1-8 la persistencia era en CSV (`facturas.csv`, `detalles.csv`, `productos.csv`, `clientes.csv`, `contador.txt`). Los DAO CSV quedan en el código como referencia y fueron la base de las mismas interfaces DAO.

## Número de factura automático

- `FacturaDAO.obtenerSiguienteNumeroFactura()` devuelve el siguiente correlativo (`FAC-%04d`) consultando el máximo FAC-XXXX en la tabla `facturas` (sin contador externo).
- `FrmFactura` lo muestra en un campo solo lectura y lo renueva al limpiar el formulario.
- Como el número se calcula de los registros reales, no se repite después de reiniciar ni si se elimina la última factura.

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

## Cómo se implementó SQLite

Las interfaces (`FacturaDAO`, `ProductoDAO`, `ClienteDAO`) permitieron cambiar la persistencia sin tocar vistas ni controladores:

1. Se agregó `org.xerial:sqlite-jdbc` al `pom.xml`.
2. Se crearon `FacturaDAOSQLite`, `ProductoDAOSQLite` y `ClienteDAOSQLite` implementando las mismas interfaces.
3. Se cambió la línea `dao = ...getInstancia()` en cada controller.
