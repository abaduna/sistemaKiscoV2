/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistremadegestion;

/**
 *
 * @author artur
 */
public class Verificador {

    private static final String CLAVE_PERMITIDA = "303A64E6EBA5"; // 

    public static boolean verificarLicencia() {
        try {
            String claveLocal = Licencia.obtenerMAC();
            return claveLocal.equals(CLAVE_PERMITIDA);
        } catch (Exception e) {
            System.out.println("Error al obtener la MAC.");
            return false;
        }
    }
}

