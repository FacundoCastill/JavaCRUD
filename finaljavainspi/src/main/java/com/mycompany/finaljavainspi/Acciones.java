package com.mycompany.finaljavainspi;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
public class Acciones {

    public static final String DB_URL = "jdbc:sqlite:finalprog.db";
    private static List<ItemVenta> carrito = new ArrayList<>();
    
    public static void agregarProducto() {
        PanelAgregarProducto panelAgregarProducto = new PanelAgregarProducto();
        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        int result = JOptionPane.showConfirmDialog(
                null,
                panelAgregarProducto,
                "Ingrese los datos del nuevo producto",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String nombre = panelAgregarProducto.getNombre();
            String precioStr = panelAgregarProducto.getPrecio();
            String stockStr = panelAgregarProducto.getStock();

            if (!nombre.isEmpty() && !precioStr.isEmpty() && !stockStr.isEmpty()) {
                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    String query = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, nombre);
                        pstmt.setDouble(2, Double.parseDouble(precioStr));
                        pstmt.setInt(3, Integer.parseInt(stockStr));
                        pstmt.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(null, "Producto agregado correctamente.");
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al agregar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar valores válidos para agregar un producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public static void actualizarProducto() {
        PanelActualizarProducto panelActualizarProducto = new PanelActualizarProducto();
        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        int result = JOptionPane.showConfirmDialog(
                null,
                panelActualizarProducto,
                "Ingrese los datos para actualizar el producto",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String nombre = panelActualizarProducto.getNombre();
            String nuevoPrecioStr = panelActualizarProducto.getNuevoPrecio();
            String nuevoStockStr = panelActualizarProducto.getNuevoStock();

            if (!nombre.isEmpty() && !nuevoPrecioStr.isEmpty() && !nuevoStockStr.isEmpty()) {
                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    String query = "UPDATE productos SET precio = ?, stock = ? WHERE nombre = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setDouble(1, Double.parseDouble(nuevoPrecioStr));
                        pstmt.setInt(2, Integer.parseInt(nuevoStockStr));
                        pstmt.setString(3, nombre);
                        pstmt.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(null, "Producto actualizado correctamente.");
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al actualizar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar valores válidos para actualizar un producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

        public static void verListaProductos() {
        SwingUtilities.invokeLater(() -> {
            new ListaProductosFrame();
        });
    }

    public static void borrarProducto() {
        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del producto a borrar:");
        if (nombre != null && !nombre.isEmpty()) {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String query = "DELETE FROM productos WHERE nombre = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, nombre);
                    pstmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Producto borrado correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al borrar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Debe ingresar un nombre válido para borrar un producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void venderProductos() {
        boolean cerrarCarrito = false;

        while (!cerrarCarrito) {
            String nombreProducto = JOptionPane.showInputDialog("Ingrese el nombre del producto a vender (o escriba 'cerrar' para finalizar la venta):");

            if (nombreProducto != null && !nombreProducto.isEmpty()) {
                if (nombreProducto.equalsIgnoreCase("cerrar")) {
                    cerrarCarrito = true;
                } else {
                    String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad a vender:");

                    if (cantidadStr != null && !cantidadStr.isEmpty()) {
                        try (Connection conn = DriverManager.getConnection(DB_URL)) {

                            String verificarStockQuery = "SELECT stock FROM productos WHERE nombre = ?";
                            try (PreparedStatement verificarStockStmt = conn.prepareStatement(verificarStockQuery)) {
                                verificarStockStmt.setString(1, nombreProducto);
                                try (ResultSet rs = verificarStockStmt.executeQuery()) {
                                    if (rs.next()) {
                                        int stockActual = rs.getInt("stock");
                                        int cantidadAVender = Integer.parseInt(cantidadStr);

                                        if (cantidadAVender > 0 && cantidadAVender <= stockActual) {

                                            String actualizarStockQuery = "UPDATE productos SET stock = ? WHERE nombre = ?";
                                            try (PreparedStatement actualizarStockStmt = conn.prepareStatement(actualizarStockQuery)) {
                                                actualizarStockStmt.setInt(1, stockActual - cantidadAVender);
                                                actualizarStockStmt.setString(2, nombreProducto);
                                                actualizarStockStmt.executeUpdate();

                                                carrito.add(new ItemVenta(nombreProducto, cantidadAVender));

                                                JOptionPane.showMessageDialog(null, "Producto agregado al carrito.");
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(null, "La cantidad a vender es inválida o excede el stock disponible.", "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "El producto no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error al realizar la venta.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar un nombre de producto válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        cerrarCarrito();
    }

    private static void cerrarCarrito() {
        UIManager.put("OptionPane.yesButtonText", "Si");
        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Desea cerrar el carrito y exportar los datos?");
        if (confirmacion == JOptionPane.YES_OPTION) {
            exportarCarrito();
        }
    }

    private static void exportarCarrito() {
        double total = 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("carrito.txt"))) {
            writer.write("Producto\tCantidad\tPrecio unitario\tTotal\n");

            for (ItemVenta item : carrito) {
                String nombreProducto = item.getNombreProducto();
                int cantidad = item.getCantidad();

                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    String obtenerPrecioQuery = "SELECT precio FROM productos WHERE nombre = ?";
                    try (PreparedStatement obtenerPrecioStmt = conn.prepareStatement(obtenerPrecioQuery)) {
                        obtenerPrecioStmt.setString(1, nombreProducto);
                        try (ResultSet rs = obtenerPrecioStmt.executeQuery()) {
                            if (rs.next()) {
                                double precioUnitario = rs.getDouble("precio");
                                double subtotal = precioUnitario * cantidad;

                                total += subtotal;

                                writer.write(nombreProducto + "\t" + cantidad + "\t" + precioUnitario + "\t" + subtotal + "\n");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al obtener información del producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            writer.write("\nTotal: " + total);

            JOptionPane.showMessageDialog(null, "Carrito cerrado. Datos exportados correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al exportar los datos del carrito.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            carrito.clear();
        }
    }
}