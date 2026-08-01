/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.josue.ventas.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author josue zetino
 */
public class Factura {
    private int id;
    private Date fecha;
    private String numeroFactura;
    private String cliente;
    private String nit;
    private List<FacturaDetalle> detalles;
    private double total;

    public Factura() {
        this.detalles = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public List<FacturaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<FacturaDetalle> detalles) {
        this.detalles = detalles;
    }

    public double getTotal() {
        return total;
    }

    public Object[][] getDetallesFilas() {
        if (detalles == null || detalles.isEmpty()) {
            return new Object[0][0];
        }
        Object[][] filas = new Object[detalles.size()][4];
        for (int i = 0; i < detalles.size(); i++) {
            FacturaDetalle d = detalles.get(i);
            filas[i][0] = d.getProducto();
            filas[i][1] = d.getCantidad();
            filas[i][2] = d.getPrecio();
            filas[i][3] = d.getSubtotal();
        }
        return filas;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void agregarDetalle(String producto, int cantidad, double precio) {
        FacturaDetalle detalle = new FacturaDetalle(producto, cantidad, precio);
        this.detalles.add(detalle);
        calcularTotal();
    }

    public void eliminarDetalle(int index) {
        if (index >= 0 && index < this.detalles.size()) {
            this.detalles.remove(index);
            calcularTotal();
        }
    }

    private void calcularTotal() {
        double suma = 0;
        for (FacturaDetalle d : this.detalles) {
            suma += d.getSubtotal();
        }
        this.total = suma;
    }

    class FacturaDetalle {
        private String producto;
        private int cantidad;
        private double precio;
        private double subtotal;

        public FacturaDetalle(String producto, int cantidad, double precio) {
            this.producto = producto;
            this.cantidad = cantidad;
            this.precio = precio;
            this.subtotal = cantidad * precio;
        }

        public String getProducto() {
            return producto;
        }

        public void setProducto(String producto) {
            this.producto = producto;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
            this.subtotal = cantidad * precio;
        }

        public double getPrecio() {
            return precio;
        }

        public void setPrecio(double precio) {
            this.precio = precio;
            this.subtotal = cantidad * precio;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(double subtotal) {
            this.subtotal = subtotal;
        }
    }
}