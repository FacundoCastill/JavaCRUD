package com.mycompany.finaljavainspi;
import javax.swing.*;
import java.awt.*;

public class PanelAgregarProducto extends JPanel {
    private JTextField nombreField;
    private JTextField precioField;
    private JTextField stockField;

    public PanelAgregarProducto() {
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Nombre del producto:"));
        nombreField = new JTextField();
        add(nombreField);

        add(new JLabel("Precio del producto:"));
        precioField = new JTextField();
        add(precioField);

        add(new JLabel("Stock del producto:"));
        stockField = new JTextField();
        add(stockField);
    }

    public String getNombre() {
        return nombreField.getText();
    }

    public String getPrecio() {
        return precioField.getText();
    }

    public String getStock() {
        return stockField.getText();
    }
}