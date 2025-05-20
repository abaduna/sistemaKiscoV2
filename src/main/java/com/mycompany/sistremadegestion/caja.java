/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.sistremadegestion;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author artur
 */
public class caja extends javax.swing.JFrame {

    private JDateChooser fechaInicioChooser;
    private JDateChooser fechaFinChooser;
    private JTextField horaInicioField;
    private JTextField horaFinField;
    private JButton submitButton;
    private JTextArea resultadoArea;
    private JTextField txtTotal;
    private JTextField txtTotalTarjeta;
    private JTextField txtTotalEfectivo;
    private JTextField txtTotalTransferencia;

    /**
     * Creates new form caja
     */
    public caja() {
        initComponents();
        setTitle("Consulta de Compras por Fecha y Hora");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        fechaInicioChooser = new JDateChooser();
        fechaInicioChooser.setDateFormatString("yyyy-MM-dd");
        fechaFinChooser = new JDateChooser();
        fechaFinChooser.setDateFormatString("yyyy-MM-dd");

        horaInicioField = new JTextField("00:00:00", 8); // formato HH:mm:ss
        horaFinField = new JTextField("23:59:59", 8);

        submitButton = new JButton("Buscar Compras");
        submitButton.addActionListener(e -> buscarCompras());

        resultadoArea = new JTextArea(15, 40);
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);

        add(new JLabel("Fecha Inicial:"));
        add(fechaInicioChooser);
        add(new JLabel("Hora Inicial (HH:mm:ss):"));
        add(horaInicioField);

        add(new JLabel("Fecha Final:"));
        add(fechaFinChooser);
        add(new JLabel("Hora Final (HH:mm:ss):"));
        add(horaFinField);
        JButton btnNuevaCompra = new JButton("Ingresar alguna compra del local");
        btnNuevaCompra.addActionListener(e -> registrarCompra());
        add(btnNuevaCompra);
        add(submitButton);
        add(scrollPane);
        JPanel panelTotales = new JPanel(new GridLayout(4, 2, 10, 10)); // 3 filas, 2 columnas, con espacio

// Etiqueta y campo para TOTAL GENERAL
        JLabel lblTotal = new JLabel("TOTAL FINAL DE COMPRAS: $");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        txtTotal = new JTextField(10);
        txtTotal.setEditable(false);
        txtTotal.setFont(new Font("Arial", Font.BOLD, 14));
        txtTotal.setForeground(Color.BLUE);

// Etiqueta y campo para TRANSFERENCIA
        JLabel lblTotalTransferencia = new JLabel("TOTAL COMPRAS Transferencia: $");
        lblTotalTransferencia.setFont(new Font("Arial", Font.BOLD, 14));
        txtTotalTransferencia = new JTextField(10);
        txtTotalTransferencia.setEditable(false);
        txtTotalTransferencia.setFont(new Font("Arial", Font.BOLD, 14));
        txtTotalTransferencia.setForeground(new Color(0, 128, 0)); // verde

// Etiqueta y campo para TARJETA
        JLabel lblTotalTarjeta = new JLabel("TOTAL COMPRAS Tarjeta: $");
        lblTotalTarjeta.setFont(new Font("Arial", Font.BOLD, 14));
        txtTotalTarjeta = new JTextField(10);
        txtTotalTarjeta.setEditable(false);
        txtTotalTarjeta.setFont(new Font("Arial", Font.BOLD, 14));
        txtTotalTarjeta.setForeground(new Color(128, 0, 128)); // violeta

// Etiqueta y campo para EFECTIVO
        JLabel lblTotalEfectivo = new JLabel("TOTAL COMPRAS Efectivo: $");
        lblTotalEfectivo.setFont(new Font("Arial", Font.BOLD, 14));
        txtTotalEfectivo = new JTextField(10);
        txtTotalEfectivo.setEditable(false);
        txtTotalEfectivo.setFont(new Font("Arial", Font.BOLD, 14));
        txtTotalEfectivo.setForeground(Color.RED); // rojo

// Añadir al panel
        panelTotales.add(lblTotal);
        panelTotales.add(txtTotal);

        panelTotales.add(lblTotalTransferencia);
        panelTotales.add(txtTotalTransferencia);

        panelTotales.add(lblTotalTarjeta);
        panelTotales.add(txtTotalTarjeta);

// Finalmente lo agregás al contenedor principal (por ejemplo un JFrame o un JPanel mayor)
        add(panelTotales);

        setVisible(true);
    }
private void registrarCompra() {
    // 1. Obtener fecha actual
    LocalDateTime now = LocalDateTime.now();
    String fechaActual = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // 2. Preguntar el producto
    String producto = JOptionPane.showInputDialog(this, "Ingrese el nombre del producto:");
    if (producto == null || producto.trim().isEmpty()) {
        return; // cancelado o vacío
    }

    // 3. Preguntar el precio
    String precioStr = JOptionPane.showInputDialog(this, "Ingrese el precio del producto:");
    if (precioStr == null || precioStr.trim().isEmpty()) {
        return; // cancelado
    }

    double precio;
    try {
        precio = -Math.abs(Double.parseDouble(precioStr)); // Precio negativo
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Precio inválido");
        return;
    }

    int idCompraGenerada = -1;

    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:productos.db")) {
        // Insertar en compras
        String sqlCompra = "INSERT INTO compras (fecha, metodo_pago) VALUES (?, ?)";
        PreparedStatement psCompra = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS);
        psCompra.setString(1, fechaActual);
        psCompra.setString(2, "Compras");
        psCompra.executeUpdate();

        // Obtener ID generado
        ResultSet rs = psCompra.getGeneratedKeys();
        if (rs.next()) {
            idCompraGenerada = rs.getInt(1);
        }

        // Insertar en compraProductos con el producto ingresado
        if (idCompraGenerada != -1) {
            String sqlDetalle = "INSERT INTO comprarProductos (compra_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
            PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle);
            psDetalle.setInt(1, idCompraGenerada);
            psDetalle.setString(2, producto); // Lo que ingresó el usuario
            psDetalle.setInt(3, 1);
            psDetalle.setDouble(4, precio);
            psDetalle.executeUpdate();
        }

        JOptionPane.showMessageDialog(this, "Compra registrada correctamente.");
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al registrar la compra: " + e.getMessage());
    }
}


    private void buscarCompras() {
        Date fechaInicio = fechaInicioChooser.getDate();
        Date fechaFin = fechaFinChooser.getDate();
        String horaInicio = horaInicioField.getText().trim();
        String horaFin = horaFinField.getText().trim();

        if (fechaInicio == null || fechaFin == null || horaInicio.isEmpty() || horaFin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa ambas fechas y horas.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaHoraInicio = sdf.format(fechaInicio) + " " + horaInicio;
        String fechaHoraFin = sdf.format(fechaFin) + " " + horaFin;

        resultadoArea.setText("");

        String queryCompras = "SELECT id, fecha, metodo_pago FROM compras WHERE fecha BETWEEN ? AND ? ORDER BY fecha ASC";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:productos.db"); PreparedStatement stmt = conn.prepareStatement(queryCompras)) {

            stmt.setString(1, fechaHoraInicio);
            stmt.setString(2, fechaHoraFin);

            ResultSet rs = stmt.executeQuery();

            double totalFinal = 0.0;
            double totalTarjeta = 0.0;
            double totalEfectivo = 0.0;
            double totalTransferencia = 0.0;

            while (rs.next()) {
                int idCompra = rs.getInt("id");
                String fecha = rs.getString("fecha");
                String metodo = rs.getString("metodo_pago");

                resultadoArea.append("Compra #" + idCompra + " | Fecha: " + fecha + " | Método: " + metodo + "\n");

                double totalCompra = obtenerTotalCompra(conn, idCompra);
                resultadoArea.append("  -> Total de esta compra: $" + String.format("%.2f", totalCompra) + "\n\n");

                totalFinal += totalCompra;

                switch (metodo.toLowerCase()) {
                    case "tarjeta":
                        totalTarjeta += totalCompra;
                        break;
                    case "efectivo":
                        totalEfectivo += totalCompra;
                        break;
                    case "transferencia":
                        totalTransferencia += totalCompra;
                        break;
                }
            }

            txtTotal.setText(String.format("%.2f", totalFinal));
            txtTotalTarjeta.setText(String.format("%.2f", totalTarjeta));
            txtTotalEfectivo.setText(String.format("%.2f", totalEfectivo));
            txtTotalTransferencia.setText(String.format("%.2f", totalTransferencia));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al consultar:\n" + e.getMessage());
        }
    }

    private double obtenerTotalCompra(Connection conn, int idCompra) throws SQLException {
        String query = "SELECT precio_unitario, cantidad FROM comprarProductos WHERE compra_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idCompra);
            ResultSet rs = stmt.executeQuery();

            double total = 0.0;
            while (rs.next()) {
                double precio = rs.getDouble("precio_unitario");
                int cantidad = rs.getInt("cantidad");
                total += precio * cantidad;
            }
            return total;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 533, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(caja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(caja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(caja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(caja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new caja().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
