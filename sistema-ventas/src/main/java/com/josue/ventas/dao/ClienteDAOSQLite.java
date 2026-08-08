/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.josue.ventas.dao;

import com.josue.ventas.modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion de ClienteDAO sobre SQLite usando JDBC.
 * Sustituye al almacenamiento en CSV: cada operacion se ejecuta
 * directamente contra la base de datos.
 *
 * @author josue zetino
 */
public class ClienteDAOSQLite implements ClienteDAO {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ClienteDAOSQLite.class.getName());

    private static final ClienteDAOSQLite instancia = new ClienteDAOSQLite();

    private ClienteDAOSQLite() {
    }

    public static ClienteDAOSQLite getInstancia() {
        return instancia;
    }

    private Connection conexion() {
        return ConexionBD.getInstancia().getConnection();
    }

    @Override
    public void guardar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nit, nombre, direccion, telefono) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNit());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getDireccion());
            ps.setString(4, cliente.getTelefono());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al guardar cliente", ex);
        }
    }

    @Override
    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, nit, nombre, direccion, telefono FROM clientes ORDER BY id";
        try (Statement stmt = conexion().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clientes.add(filaAEntidad(rs));
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al listar clientes", ex);
        }
        return clientes;
    }

    @Override
    public void actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nit = ?, nombre = ?, direccion = ?, telefono = ? WHERE id = ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, cliente.getNit());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getDireccion());
            ps.setString(4, cliente.getTelefono());
            ps.setInt(5, cliente.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al actualizar cliente", ex);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al eliminar cliente", ex);
        }
    }

    @Override
    public boolean existeNit(String nit) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE LOWER(nit) = LOWER(?)";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, nit);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al consultar NIT", ex);
            return false;
        }
    }

    private Cliente filaAEntidad(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setNit(rs.getString("nit"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setTelefono(rs.getString("telefono"));
        return cliente;
    }
}
