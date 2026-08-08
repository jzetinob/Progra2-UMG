/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.josue.ventas.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Migra los registros guardados en CSV (tareas anteriores) a la base de datos
 * SQLite. Solo se ejecuta la primera vez: si la base esta vacia y existen los
 * archivos CSV, los importa para no perder la informacion.
 *
 * @author josue zetino
 */
public class MigradorDatos {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MigradorDatos.class.getName());

    private static final String DIRECTORIO = "datos";
    private static final File ARCHIVO_CLIENTES = new File(DIRECTORIO + File.separator + "clientes.csv");
    private static final File ARCHIVO_PRODUCTOS = new File(DIRECTORIO + File.separator + "productos.csv");
    private static final File ARCHIVO_FACTURAS = new File(DIRECTORIO + File.separator + "facturas.csv");
    private static final File ARCHIVO_DETALLES = new File(DIRECTORIO + File.separator + "detalles.csv");

    private static final MigradorDatos instancia = new MigradorDatos();

    private MigradorDatos() {
    }

    public static MigradorDatos instancia() {
        return instancia;
    }

    public synchronized void migrarSiVacio(Connection conexion) {
        if (conexion == null || tablaTieneDatos(conexion, "clientes")) {
            return;
        }
        migrar(conexion);
    }

    private boolean tablaTieneDatos(Connection conexion, String tabla) {
        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tabla)) {
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException ex) {
            return false;
        }
    }

    private void migrar(Connection conexion) {
        try {
            conexion.setAutoCommit(false);
            migrarClientes(conexion);
            migrarProductos(conexion);
            migrarFacturas(conexion);
            migrarDetalles(conexion);
            conexion.commit();
            logger.info("Datos de CSV migrados a la base de datos SQLite");
        } catch (SQLException ex) {
            try {
                conexion.rollback();
            } catch (SQLException rollbackEx) {
                logger.log(java.util.logging.Level.WARNING, "No se pudo revertir la migracion", rollbackEx);
            }
            logger.log(java.util.logging.Level.WARNING, "No se pudo migrar los datos CSV", ex);
        } finally {
            try {
                conexion.setAutoCommit(true);
            } catch (SQLException ex) {
                logger.log(java.util.logging.Level.WARNING, "No se pudo restaurar autocommit", ex);
            }
        }
    }

    private void migrarClientes(Connection conexion) throws SQLException {
        if (!ARCHIVO_CLIENTES.exists()) {
            return;
        }
        String sql = "INSERT INTO clientes (id, nit, nombre, direccion, telefono) VALUES (?, ?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_CLIENTES)); PreparedStatement ps = conexion.prepareStatement(sql)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] c = CsvUtil.dividirLinea(linea);
                ps.setInt(1, Integer.parseInt(c[0]));
                ps.setString(2, c[1]);
                ps.setString(3, c[2]);
                ps.setString(4, c[3]);
                ps.setString(5, c[4]);
                ps.executeUpdate();
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.WARNING, "No se pudo leer clientes.csv", ex);
        }
    }

    private void migrarProductos(Connection conexion) throws SQLException {
        if (!ARCHIVO_PRODUCTOS.exists()) {
            return;
        }
        String sql = "INSERT INTO productos (id, codigo, nombre, precio) VALUES (?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_PRODUCTOS)); PreparedStatement ps = conexion.prepareStatement(sql)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] c = CsvUtil.dividirLinea(linea);
                ps.setInt(1, Integer.parseInt(c[0]));
                ps.setString(2, c[1]);
                ps.setString(3, c[2]);
                ps.setDouble(4, Double.parseDouble(c[3]));
                ps.executeUpdate();
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.WARNING, "No se pudo leer productos.csv", ex);
        }
    }

    private void migrarFacturas(Connection conexion) throws SQLException {
        if (!ARCHIVO_FACTURAS.exists()) {
            return;
        }
        String sql = "INSERT INTO facturas (id, numero_factura, nit, cliente, fecha, total) VALUES (?, ?, ?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_FACTURAS)); PreparedStatement ps = conexion.prepareStatement(sql)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] c = CsvUtil.dividirLinea(linea);
                ps.setInt(1, Integer.parseInt(c[0]));
                ps.setString(2, c[1]);
                ps.setString(3, c[2]);
                ps.setString(4, c[3]);
                ps.setString(5, c[4]);
                ps.setDouble(6, Double.parseDouble(c[5]));
                ps.executeUpdate();
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.WARNING, "No se pudo leer facturas.csv", ex);
        }
    }

    private void migrarDetalles(Connection conexion) throws SQLException {
        if (!ARCHIVO_DETALLES.exists()) {
            return;
        }
        String sql = "INSERT INTO factura_detalles (factura_id, producto, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_DETALLES)); PreparedStatement ps = conexion.prepareStatement(sql)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] c = CsvUtil.dividirLinea(linea);
                ps.setInt(1, Integer.parseInt(c[0]));
                ps.setString(2, c[1]);
                ps.setInt(3, Integer.parseInt(c[2]));
                ps.setDouble(4, Double.parseDouble(c[3]));
                ps.setDouble(5, Double.parseDouble(c[4]));
                ps.executeUpdate();
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.WARNING, "No se pudo leer detalles.csv", ex);
        }
    }
}
