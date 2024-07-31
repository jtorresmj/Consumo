/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.consumo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author jtorr
 */
public class Conexion {
    
    public Connection conexionDB;
    
    public String url=url="localhost;databaseName=conexion;user=root;password=1234;";
    //public String jdbc= "https://mvnrepository.com/artifact/com.mysql/mysql-connector-j";

    public void abrirConexion() {
        try {
            //Class.forName(jdbc);
            conexionDB = DriverManager.getConnection(url);
            JOptionPane.showMessageDialog(null, "Conexion exitosa...!", "Estado de conexion",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            System.out.println("Error al conectar a la base de datos " + e.getMessage());
        }
    }

    public void cerrarConexion() {
        try {
            conexionDB.close();
        } catch (SQLException ex) {
            System.out.println("Error al conectar a la base de datos " + ex.getMessage());
        }

    }
}


