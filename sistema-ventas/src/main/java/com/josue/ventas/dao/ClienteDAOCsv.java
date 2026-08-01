/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.josue.ventas.dao;

import com.josue.ventas.modelo.Cliente;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josue zetino
 */
public class ClienteDAOCsv implements ClienteDAO {

    private static final ClienteDAOCsv instancia = new ClienteDAOCsv();

    private static final String DIRECTORIO = "datos";
    private static final String ARCHIVO_CLIENTES = DIRECTORIO + File.separator + "clientes.csv";

    private final List<Cliente> clientes = new ArrayList<>();
    private int siguienteId = 1;

    private ClienteDAOCsv() {
        cargar();
    }

    public static ClienteDAOCsv getInstancia() {
        return instancia;
    }

    private void cargar() {
        File archivo = new File(ARCHIVO_CLIENTES);
        if (!archivo.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] campos = CsvUtil.dividirLinea(linea);
                Cliente c = new Cliente();
                c.setId(Integer.parseInt(campos[0]));
                c.setNit(campos[1]);
                c.setNombre(campos[2]);
                c.setDireccion(campos[3]);
                c.setTelefono(campos[4]);
                clientes.add(c);
                if (c.getId() >= siguienteId) {
                    siguienteId = c.getId() + 1;
                }
            }
        } catch (IOException ex) {
        }
    }

    private void escribir() {
        File dir = new File(DIRECTORIO);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_CLIENTES))) {
            for (Cliente c : clientes) {
                String linea = String.join(";",
                        String.valueOf(c.getId()),
                        CsvUtil.escape(c.getNit()),
                        CsvUtil.escape(c.getNombre()),
                        CsvUtil.escape(c.getDireccion()),
                        CsvUtil.escape(c.getTelefono()));
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException ex) {
        }
    }

    @Override
    public void guardar(Cliente cliente) {
        if (cliente.getId() == 0) {
            cliente.setId(siguienteId++);
        }
        clientes.add(cliente);
        escribir();
    }

    @Override
    public List<Cliente> listar() {
        return clientes;
    }

    @Override
    public void actualizar(Cliente cliente) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == cliente.getId()) {
                clientes.set(i, cliente);
                break;
            }
        }
        escribir();
    }

    @Override
    public void eliminar(int id) {
        clientes.removeIf(cliente -> cliente.getId() == id);
        escribir();
    }

    @Override
    public boolean existeNit(String nit) {
        for (Cliente c : clientes) {
            if (c.getNit().equalsIgnoreCase(nit)) {
                return true;
            }
        }
        return false;
    }
}
