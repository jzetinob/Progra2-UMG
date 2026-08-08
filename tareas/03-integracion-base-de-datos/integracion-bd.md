# Integración del Proyecto con Base de Datos

**Tarea 3 — Programación II**
**Estudiante:** Josue Zetino
**Repositorio público:** https://github.com/jzetinob/sistema-ventas

---

## 1. Objetivo

Modificar el sistema de ventas (Java Swing, patrón MVC) para reemplazar el almacenamiento en memoria/CSV por una base de datos relacional, de modo que los registros se almacenen de forma permanente mediante JDBC.

## 2. Base de datos seleccionada: SQLite

Se eligió **SQLite** porque es una base de datos relacional ligera que no requiere instalar un servidor: la base es un solo archivo (`datos/sistema_ventas.db`) que el sistema crea automáticamente. El controlador JDBC se agregó como dependencia Maven:

```xml
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.47.1.0</version>
</dependency>
```

## 3. Tablas creadas

Al arrancar, la aplicación ejecuta `CREATE TABLE IF NOT EXISTS` y crea las 4 tablas:

| Tabla | Columnas |
|---|---|
| `clientes` | id (PK, autoincremental), nit (UNIQUE), nombre, direccion, telefono |
| `productos` | id (PK, autoincremental), codigo (UNIQUE), nombre, precio |
| `facturas` | id (PK, autoincremental), numero_factura (UNIQUE), nit, cliente, fecha, total |
| `factura_detalles` | id (PK), factura_id (FK → facturas ON DELETE CASCADE), producto, cantidad, precio, subtotal |

## 4. Cómo se realizó la conexión

La conexión se administra en la clase `ConexionBD` (patrón **singleton**): se carga el driver con `Class.forName("org.sqlite.JDBC")` y se obtiene con `DriverManager`:

```java
Connection conexion = DriverManager.getConnection("jdbc:sqlite:datos/sistema_ventas.db");
```

La conexión se abre una sola vez al iniciar la aplicación y se reutiliza en todas las operaciones.

## 5. Clases y componentes agregados

- **`dao/ConexionBD.java`** — singleton que abre la conexión JDBC, crea las tablas y expone `getConnection()`.
- **`dao/ClienteDAOSQLite.java`** — CRUD de clientes (`guardar`, `listar`, `actualizar`, `eliminar`, `existeNit`) con `PreparedStatement`.
- **`dao/ProductoDAOSQLite.java`** — CRUD de productos (análogo al anterior).
- **`dao/FacturaDAOSQLite.java`** — guarda la factura y sus detalles en una **transacción** (si algo falla, no se guarda nada); calcula el siguiente número FAC-XXXX con `MAX` de la tabla.
- **`dao/MigradorDatos.java`** — migra a la BD los registros que existían en los CSV de las tareas anteriores (solo la primera vez).

Los DAOs SQLite implementan las **mismas interfaces** que los DAO CSV (`ClienteDAO`, `ProductoDAO`, `FacturaDAO`), por lo que solo se cambió una línea en cada controlador:

```java
dao = FacturaDAOSQLite.getInstancia();   // antes: FacturaDAOCsv.getInstancia()
```

## 6. Persistencia de los formularios

- **Facturación** (`FrmFactura`): al guardar, la factura y su detalle se insertan en `facturas` y `factura_detalles`.
- **Catálogos** (`FrmProductos`, `FrmClientes`): cada guardar/actualizar/eliminar ejecuta su SQL contra la BD.
- **Lista de facturas** (`FrmListaFacturas`): lee de la BD con su número, cliente, fecha y total, y carga los detalles al abrir.

## 7. Verificación: los datos permanecen al cerrar y reabrir

La prueba que pide la tarea se realizó de dos maneras:

1. **Desde la aplicación**: se guardaron facturas, productos y clientes; se cerró el programa y se volvió a abrir; los registros aparecieron intactos y la numeración de facturas continuó (FAC-0001, FAC-0002, ...).
2. **Con DB Browser for SQLite**: se abrió el archivo `sistema_ventas.db` y se confirmaron las 4 tablas con sus registros.

## 8. Capturas

> (se insertan aquí las capturas del sistema funcionando y de la base de datos con registros)

## 9. Conclusiones

- La arquitectura DAO permitió cambiar la persistencia sin modificar las vistas ni los controladores (cambio de 1 línea).
- La base de datos garantiza permanencia, unicidad de NIT/código/número de factura e integridad con llaves foráneas.
- Las transacciones evitan facturas incompletas.
