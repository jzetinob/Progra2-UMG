/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.josue.ventas.dao;

import com.josue.ventas.modelo.Cliente;
import java.util.List;

/**
 *
 * @author josue zetino
 */
public interface ClienteDAO {

    void guardar(Cliente cliente);

    List<Cliente> listar();

    void actualizar(Cliente cliente);

    void eliminar(int id);

    boolean existeNit(String nit);
}
