/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistremadegestion.models;

/**
 *
 * @author artur
 */
public class Producto {
    private String nombre;
    private double precio;
    private String barCode;
    private String id;
    private int cantidad;
    
   public Producto(String nombre, double precio, String barCode, String id, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.barCode = barCode;
        this.id = id;
        this.cantidad = cantidad;
    }
public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return nombre + " - $" + precio;
    }
}
