package com.mycompany.finaljavainspi;
public class ItemVenta {
    private String nombreProducto;
    private int cantidad;

    public ItemVenta(String nombreProducto, int cantidad) {
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }
}