/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.playlistversionuno.modelos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author yacog
 */
public class DatabaseConnection {
    private Connection connection;
    
    public boolean connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyectoPlaylist", "root", "Pedochino1978");
            return true;
            
    
        } catch (ClassNotFoundException | SQLException e){
        return false;
                }
    

    }
    public void desconectar(){
      
      try{
          connection.close();
      }catch (SQLException ex){
          System.out.println(ex.getMessage());
      }
  }
  public Connection getConnection(){
      return connection;
  }
    
}
