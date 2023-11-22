package com.mycompany.finaljavainspi;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class VentanaPrincipal {

    public static void createAndShowGUI() {
        crearBaseDeDatos();

        JFrame ventana = new JFrame("Ventas");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(800, 500);
        ventana.setResizable(false);
        ventana.setLayout(null);

        JButton botonAgregar = new JButton("Agregar Producto Nuevo");
        botonAgregar.setBounds(0, 0, 200, 127);
        botonAgregar.addActionListener(e -> Acciones.agregarProducto());
        ventana.add(botonAgregar);

        JButton botonActualizar = new JButton("Actualizar Producto");
        botonActualizar.setBounds(0, 127, 200, 127);
        botonActualizar.addActionListener(e -> Acciones.actualizarProducto());
        ventana.add(botonActualizar);

        JButton botonVerLista = new JButton("Ver Lista de Productos");
        botonVerLista.setBounds(0, 254, 200, 127);
        botonVerLista.addActionListener(e -> Acciones.verListaProductos());
        ventana.add(botonVerLista);

        JButton botonBorrar = new JButton("Borrar Producto");
        botonBorrar.setBounds(0, 381, 200, 127);
        botonBorrar.addActionListener(e -> Acciones.borrarProducto());
        ventana.add(botonBorrar);

        JButton botonVender = new JButton("Vender");
        botonVender.setBounds(400, 381, 200, 50);
        botonVender.addActionListener(e -> Acciones.venderProductos());
        ventana.add(botonVender);

         JLabel labelImagen = new JLabel();
        labelImagen.setBounds(200, 0, 600, 381);

        ImageIcon icono = new ImageIcon("C:/Users/Oh yeah/Documents/NetBeansProjects/finaljavainspi/src/main/java/com/mycompany/finaljavainspi/logo.png");
        Image imagen = icono.getImage().getScaledInstance(labelImagen.getWidth(), labelImagen.getHeight(), Image.SCALE_SMOOTH);
        labelImagen.setIcon(new ImageIcon(imagen));

        ventana.add(labelImagen);

        ventana.setVisible(true);
    }

    private static void crearBaseDeDatos() {
        String url = "jdbc:sqlite:finalprog.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS productos (id INTEGER PRIMARY KEY, nombre TEXT, precio REAL, stock INTEGER)");
            statement.close();

        } catch (SQLException e) {
            System.err.println("Error al crear la base de datos: " + e.getMessage());
        }
    }
}
