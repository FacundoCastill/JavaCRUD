package com.mycompany.finaljavainspi;
import javax.swing.*;
import java.awt.*;

public class PanelActualizarProducto extends JPanel {
    private JTextField nombreField;
    private JTextField nuevoPrecioField;
    private JTextField nuevoStockField;

    public PanelActualizarProducto() {
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Nombre del producto a actualizar:"));
        nombreField = new JTextField();
        add(nombreField);

        add(new JLabel("Nuevo precio del producto:"));
        nuevoPrecioField = new JTextField();
        add(nuevoPrecioField);

        add(new JLabel("Nuevo stock del producto:"));
        nuevoStockField = new JTextField();
        add(nuevoStockField);
    }

    public String getNombre() {
        return nombreField.getText();
    }

    public String getNuevoPrecio() {
        return nuevoPrecioField.getText();
    }

    public String getNuevoStock() {
        return nuevoStockField.getText();
    }
}