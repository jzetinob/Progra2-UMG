/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.josue.ventas.vista;

import javax.swing.JOptionPane;

/**
 *
 * @author josue zetino
 */
public class FrmPrincipal extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmPrincipal.class.getName());

    FrmFactura facturaVentana;
    FrmListaFacturas listaVentana;

    public FrmPrincipal() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void abrirFactura() {
        if (facturaVentana == null || !facturaVentana.isDisplayable()) {
            facturaVentana = new FrmFactura();
            facturaVentana.setLocationRelativeTo(this);
        }
        facturaVentana.setVisible(true);
        facturaVentana.toFront();
    }

    private void abrirListaFacturas() {
        if (listaVentana == null || !listaVentana.isDisplayable()) {
            listaVentana = new FrmListaFacturas();
            listaVentana.setLocationRelativeTo(this);
        }
        listaVentana.setVisible(true);
        listaVentana.toFront();
    }

    private void limpiarFactura() {
        if (facturaVentana != null && facturaVentana.isDisplayable()) {
            facturaVentana.limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna factura abierta.");
        }
    }

    private void salir() {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea salir del sistema?", "Salir",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void acercaDe() {
        JOptionPane.showMessageDialog(this,
                "Sistema de Ventas\nVersión 1.0\n\nJosue Zetino\n",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnArchivo = new javax.swing.JMenu();
        miNuevaFactura = new javax.swing.JMenuItem();
        miVerFacturas = new javax.swing.JMenuItem();
        miSalir = new javax.swing.JMenuItem();
        mnEdicion = new javax.swing.JMenu();
        miLimpiar = new javax.swing.JMenuItem();
        mnAyuda = new javax.swing.JMenu();
        miAcercaDe = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Ventas");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36));
        jLabel1.setText("SISTEMA DE VENTAS");

        jLabel2.setText("Use el menú Archivo para crear una nueva factura");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addContainerGap(75, Short.MAX_VALUE))
        );

        mnArchivo.setText("Archivo");
        mnArchivo.setMnemonic('A');

        miNuevaFactura.setText("Nueva Factura");
        miNuevaFactura.setMnemonic('N');
        miNuevaFactura.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        miNuevaFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miNuevaFacturaActionPerformed(evt);
            }
        });
        mnArchivo.add(miNuevaFactura);

        miVerFacturas.setText("Ver Facturas");
        miVerFacturas.setMnemonic('V');
        miVerFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miVerFacturasActionPerformed(evt);
            }
        });
        mnArchivo.add(miVerFacturas);

        mnArchivo.addSeparator();

        miSalir.setText("Salir");
        miSalir.setMnemonic('S');
        miSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        miSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSalirActionPerformed(evt);
            }
        });
        mnArchivo.add(miSalir);

        jMenuBar1.add(mnArchivo);

        mnEdicion.setText("Edición");
        mnEdicion.setMnemonic('E');

        miLimpiar.setText("Limpiar Formulario");
        miLimpiar.setMnemonic('L');
        miLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLimpiarActionPerformed(evt);
            }
        });
        mnEdicion.add(miLimpiar);

        jMenuBar1.add(mnEdicion);

        mnAyuda.setText("Ayuda");
        mnAyuda.setMnemonic('y');

        miAcercaDe.setText("Acerca de");
        miAcercaDe.setMnemonic('c');
        miAcercaDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miAcercaDeActionPerformed(evt);
            }
        });
        mnAyuda.add(miAcercaDe);

        jMenuBar1.add(mnAyuda);

        setJMenuBar(jMenuBar1);

        pack();
        setMinimumSize(new java.awt.Dimension(420, 240));
    }// </editor-fold>//GEN-END:initComponents

    private void miNuevaFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miNuevaFacturaActionPerformed
        abrirFactura();
    }//GEN-LAST:event_miNuevaFacturaActionPerformed

    private void miSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSalirActionPerformed
        salir();
    }//GEN-LAST:event_miSalirActionPerformed

    private void miVerFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miVerFacturasActionPerformed
        abrirListaFacturas();
    }//GEN-LAST:event_miVerFacturasActionPerformed

    private void miLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miLimpiarActionPerformed
        limpiarFactura();
    }//GEN-LAST:event_miLimpiarActionPerformed

    private void miAcercaDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miAcercaDeActionPerformed
        acercaDe();
    }//GEN-LAST:event_miAcercaDeActionPerformed

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

        java.awt.EventQueue.invokeLater(() -> new FrmPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu mnArchivo;
    private javax.swing.JMenu mnAyuda;
    private javax.swing.JMenu mnEdicion;
    private javax.swing.JMenuItem miAcercaDe;
    private javax.swing.JMenuItem miLimpiar;
    private javax.swing.JMenuItem miNuevaFactura;
    private javax.swing.JMenuItem miSalir;
    private javax.swing.JMenuItem miVerFacturas;
    // End of variables declaration//GEN-END:variables
}
