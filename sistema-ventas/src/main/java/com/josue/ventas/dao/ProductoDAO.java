/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.josue.ventas.dao;

import com.josue.ventas.modelo.Producto;
import java.util.List;

/**
 *
 * @author josue zetino
 */
public interface ProductoDAO {

    void guardar(Producto producto);

    List<Producto> listar();

    void actualizar(Producto producto);

    void eliminar(int id);

    boolean existeCodigo(String codigo);
}
