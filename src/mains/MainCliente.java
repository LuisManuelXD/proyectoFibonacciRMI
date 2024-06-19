package mains;//import vista.VistaCliente;

import vista.VistaCliente;

import javax.swing.*;

public class MainCliente {
    static String HOST = "192.168.1.9";
//    static String HOST = "25.9.115.198";
//    static String HOST = "192.168.1.242";
    static int PUERTO = 3000;
    
    public static void main(String[] args) {
        try {
            String nombre = JOptionPane.showInputDialog("Ingresa tu nombre:");
            VistaCliente vistaCliente = new VistaCliente(nombre, HOST, PUERTO);
            vistaCliente.setTitle("Proyecto Fibonacci RMI - Cliente " + nombre);
            vistaCliente.setSize(700, 400);
            vistaCliente.setLocationRelativeTo(null);
            vistaCliente.setLayout(null);
            vistaCliente.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}