/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistremadegestion;




/**
 *
 * @author artur
 */
public class Sistremadegestion {

    public static void main(String[] args) {
     if (!Verificador.verificarLicencia()) {
            System.out.println("Este software no está activado para esta máquina.");
            return;
        }

        Formulario ventana = new Formulario();
        ventana.show();
    }
}
