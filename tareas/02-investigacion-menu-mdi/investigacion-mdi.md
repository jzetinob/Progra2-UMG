# Investigación: Formulario contenedor (Menú MDI)

**Tarea:** Investigación: Formulario contenedor (Menú MDI) — 2 puntos
**Tecnología:** Java Swing
**Proyecto aplicado:** Sistema de Ventas (este repositorio)

## 1. ¿Qué es un formulario contenedor (MDI)?

**MDI** (Multiple Document Interface) es una arquitectura de interfaz de escritorio donde una **ventana principal** (contenedor o "frame padre") aloja dentro de sí varias **ventanas hijas**, en lugar de abrirlas como ventanas independientes del sistema operativo. Las ventanas hijas se mantienen confinadas al área del contenedor: se pueden mover, minimizar, maximizar y cerrar, pero nunca salen de la ventana principal.

Es el modelo clásico de programas como el viejo Office (Excel con varios libros abiertos), los editores antiguos o los IDE (NetBeans, Eclipse): una sola barra de menú controla todas las ventanas abiertas.

En Java Swing, el contenedor MDI se arma con tres piezas:

| Pieza | Rol |
|---|---|
| `JFrame` (padre) | La ventana principal con su `JMenuBar` |
| `JDesktopPane` | El "escritorio" donde viven las ventanas hijas |
| `JInternalFrame` | Cada ventana hija |

## 2. ¿Qué es un JDesktopPane?

`JDesktopPane` es un contenedor especializado (hereda de `JLayeredPane`) diseñado para alojar ventanas internas. Gestiona sus posiciones en **capas** y delega el manejo de maximizar/minimizar a un `DesktopManager`. En la práctica funciona como el escritorio de Windows: las ventanas internas se dibujan encima del desktop y se superponen entre sí.

En este proyecto, el `JDesktopPane` es el contenido del `FrmPrincipal`:

```java
jDesktopPane = new javax.swing.JDesktopPane();
jDesktopPane.setBackground(new java.awt.Color(204, 204, 204));

javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
getContentPane().setLayout(layout);
layout.setHorizontalGroup(
    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addComponent(jDesktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
);
layout.setVerticalGroup(
    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addComponent(jDesktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
);
```

(Fragmento real de `FrmPrincipal.java`, método `initComponents()`.)

## 3. ¿Qué es un JInternalFrame?

`JInternalFrame` es el equivalente *interno* de un `JFrame`: tiene barra de título, botones de cerrar/minimizar/maximizar y puede contener cualquier componente, pero **no es una ventana del sistema operativo** — solo existe dentro de un `JDesktopPane`. Algunas de sus propiedades típicas son:

- `setClosable(true)` — permite cerrar la ventana interna.
- `setIconifiable(true)` — permite minimizarla (queda como icono abajo del escritorio).
- `setMaximizable(true)` — permite maximizarla.
- `setResizable(true)` — permite redimensionarla.
- `setTitle(...)` — texto de la barra de título.

En este proyecto todos los formularios de la aplicación (factura, catálogos, lista de facturas y detalle) pasaron de `JFrame` a `JInternalFrame`. Ejemplo real de `FrmProductos.java`:

```java
public class FrmProductos extends javax.swing.JInternalFrame {

    public FrmProductos() {
        initComponents();
        controller = new ProductoController();
        configurarTabla();
        refrescarTabla();
        // ...
    }
```

y en su `initComponents()`:

```java
setClosable(true);
setIconifiable(true);
setMaximizable(true);
setResizable(true);
setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
setTitle("Catálogo de Productos");
```

Un detalle importante: los `JInternalFrame` no tienen método `setLocationRelativeTo(null)` ni pueden mostrarse con `setVisible(true)` fuera de un escritorio; por eso se eliminaron los `main()` de los formularios hijos y el centrado se hace manualmente respecto al `JDesktopPane`.

## 4. ¿Cómo funciona un JMenuBar?

`JMenuBar` es la barra de menús horizontal que se coloca en la parte superior de un `JFrame` (el sistema lo agrega al *root pane* con `setJMenuBar`). Dentro de ella van `JMenu` (Archivo, Catálogos, ...) y dentro de cada menú, `JMenuItem` (Nueva Factura, Salir, ...). Los ítems lanzan `ActionEvent` que la ventana principal atiende.

En la arquitectura MDI, **la barra de menú pertenece al contenedor** (no a cada ventana hija), por lo que un solo menú controla todas las ventanas abiertas. Ejemplo real de `FrmPrincipal.java` — se agrega el menú "Ventana" con las operaciones típicas MDI:

```java
mnVentana.setText("Ventana");
mnVentana.setMnemonic('V');

miCascada.setText("Cascada");
miCascada.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        miCascadaActionPerformed(evt);
    }
});
mnVentana.add(miCascada);
// ... Mosaico, Minimizar todo, Restaurar todo ...

setJMenuBar(jMenuBar1);
```

## 5. ¿Cómo abrir formularios hijos desde un menú?

El patrón es: el `ActionListener` del `JMenuItem` llama a un método que **agrega el `JInternalFrame` al `JDesktopPane`**, lo hace visible y lo selecciona. En `FrmPrincipal` se centralizó en un método reutilizable:

```java
private void abrirFormulario(JInternalFrame frame) {
    if (!frame.isVisible()) {
        jDesktopPane.add(frame);
        frame.setVisible(true);
        Dimension escritorio = jDesktopPane.getSize();
        frame.setLocation(Math.max(0, (escritorio.width - frame.getWidth()) / 2),
                Math.max(0, (escritorio.height - frame.getHeight()) / 2));
    }
    try {
        frame.setSelected(true);
    } catch (java.beans.PropertyVetoException ex) {
        logger.log(java.util.logging.Level.WARNING, "No se pudo seleccionar la ventana hija.", ex);
    }
    frame.toFront();
}

private void abrirFactura() {
    if (facturaVentana == null || !facturaVentana.isDisplayable()) {
        facturaVentana = new FrmFactura();
    }
    abrirFormulario(facturaVentana);
}
```

Cada ítem del menú usa ese método; por ejemplo `Archivo → Nueva Factura`:

```java
private void miNuevaFacturaActionPerformed(java.awt.event.ActionEvent evt) {
    abrirFactura();
}
```

El menú **Ventana** implementa las operaciones clásicas del MDI recorriendo las ventanas del escritorio con `jDesktopPane.getAllFrames()`. Ejemplo real, el mosaico:

```java
private void mosaico() {
    java.util.List<JInternalFrame> visibles = new java.util.ArrayList<>();
    for (JInternalFrame frame : jDesktopPane.getAllFrames()) {
        if (!frame.isIcon()) {
            visibles.add(frame);
        }
    }
    if (visibles.isEmpty()) {
        return;
    }
    int n = visibles.size();
    int columnas = (int) Math.ceil(Math.sqrt(n));
    int filas = (int) Math.ceil((double) n / columnas);
    int ancho = jDesktopPane.getWidth() / columnas;
    int alto = jDesktopPane.getHeight() / filas;
    for (int i = 0; i < n; i++) {
        visibles.get(i).setBounds((i % columnas) * ancho, (i / columnas) * alto, ancho, alto);
    }
}
```

## 6. Ventajas de esta arquitectura en aplicaciones de escritorio

1. **Un solo menú central**: los menús Archivo, Catálogos, Ventana, etc. viven en el contenedor y aplican a todas las ventanas hijas; no se duplica la barra de menú por formulario.
2. **Orden visual**: las ventanas no se pierden detrás de otras aplicaciones del sistema operativo; todo el trabajo del programa ocurre dentro de una sola ventana.
3. **Gestión integrada de ventanas**: el menú Ventana permite organizar (cascada, mosaico, minimizar/restaurar todo) sin depender del sistema operativo.
4. **Menor costo de recursos**: las ventanas internas son componentes ligeros de Swing (no crean ventanas nativas del SO por cada formulario).
5. **Estado compartido**: es natural que el contenedor centralice referencias (p. ej. el `JDesktopPane` compartido entre la lista de facturas y el detalle).
6. **Concentración en la tarea**: al mantener todo dentro del contenedor, el usuario tiene un contexto único (todos los catálogos y la factura a la vista) sin cambiar de ventana del sistema.

## Bibliografía

- Oracle. *How to Use Internal Frames* (Java Swing Tutorial). https://docs.oracle.com/javase/tutorial/uiswing/components/internalframe.html
- Oracle. *How to Use Desktop Panes* (Java Swing Tutorial). https://docs.oracle.com/javase/tutorial/uiswing/components/desktoppane.html
- Oracle. *How to Use Menus* (Java Swing Tutorial). https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html
- Oracle. *JDesktopPane* (API Java SE). https://docs.oracle.com/en/java/javase/25/docs/api/java.desktop/javax/swing/JDesktopPane.html
- Oracle. *JInternalFrame* (API Java SE). https://docs.oracle.com/en/java/javase/25/docs/api/java.desktop/javax/swing/JInternalFrame.html
