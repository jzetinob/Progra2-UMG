/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.josue.ventas.vista;

import com.josue.ventas.controlador.FacturaController;
import com.josue.ventas.modelo.Factura;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author josue zetino
 */
public class FrmListaFacturas extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmListaFacturas.class.getName());

    FacturaController controller;
    DefaultTableModel modeloTabla;

    public FrmListaFacturas() {
        initComponents();
        controller = new FacturaController();
        configurarTabla();
        cargarFacturas();
        setLocationRelativeTo(null);
    }

    private void configurarTabla() {
        String[] columnas = {"No.", "NIT", "Cliente", "Fecha", "Total"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTableFacturas.setModel(modeloTabla);
    }

    private void cargarFacturas() {
        modeloTabla.setRowCount(0);
        List<Factura> facturas = controller.GetFacturas();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Factura factura : facturas) {
            Object[] fila = {
                factura.getId(),
                factura.getNit(),
                factura.getCliente(),
                factura.getFecha() != null ? sdf.format(factura.getFecha()) : "",
                String.format("%.2f", factura.getTotal())
            };
            modeloTabla.addRow(fila);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableFacturas = new javax.swing.JTable();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Facturas Registradas");

        jTableFacturas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "NIT", "Cliente", "Fecha", "Total"
            }
        ));
        jScrollPane1.setViewportView(jTableFacturas);

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCerrar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCerrar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new FrmListaFacturas().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableFacturas;
    // End of variables declaration//GEN-END:variables
}
