package com.mycompany.finaljavainspi;
import javax.swing.*;

public class VentasApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal.createAndShowGUI();
        });
    }
}