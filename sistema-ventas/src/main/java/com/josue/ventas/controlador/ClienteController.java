/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.josue.ventas.controlador;

import com.josue.ventas.dao.ClienteDAO;
import com.josue.ventas.dao.ClienteDAOSQLite;
import com.josue.ventas.modelo.Cliente;
import java.util.List;

/**
 *
 * @author josue zetino
 */
public class ClienteController {

    ClienteDAO dao;

    public ClienteController() {
        dao = ClienteDAOSQLite.getInstancia();
    }

    public void Guardar(Cliente cliente) {
        dao.guardar(cliente);
    }

    public List<Cliente> GetClientes() {
        return dao.listar();
    }

    public void Actualizar(Cliente cliente) {
        dao.actualizar(cliente);
    }

    public void Eliminar(int id) {
        dao.eliminar(id);
    }

    public boolean ExisteNit(String nit) {
        return dao.existeNit(nit);
    }
}
