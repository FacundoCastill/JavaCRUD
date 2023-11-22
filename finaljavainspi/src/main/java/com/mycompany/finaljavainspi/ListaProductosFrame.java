package com.mycompany.finaljavainspi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ListaProductosFrame extends JFrame {

    public ListaProductosFrame() {
        super("Lista de Productos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Nombre");
        tableModel.addColumn("Precio");
        tableModel.addColumn("Stock");

        try (Connection conn = DriverManager.getConnection(Acciones.DB_URL)) {
            String query = "SELECT nombre, precio, stock FROM productos ORDER BY nombre";
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] rowData = {
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    };
                    tableModel.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener la lista de productos.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}