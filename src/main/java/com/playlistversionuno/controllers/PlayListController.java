/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.playlistversionuno.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.playlistversionuno.modelos.Cancion;
import com.playlistversionuno.modelos.DatabaseConnection;
import com.playlistversionuno.modelos.DoublyLinkedList;

/**
 *
 * @author yacog
 */
public class PlayListController {
    private DatabaseConnection connection = new DatabaseConnection();
    public int Create (Cancion cancion){
        int result = 0;
        String req = "INSERT INTO cancion (nombrecancion, nombrefoto) VALUES (?, ?);";
        try{
            if(this.connection.connect()){
                PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
                statement.setString(1, cancion.getTitulo());
                statement.setString(2, cancion.getFoto());
                result = statement.executeUpdate();
            }
            else{
                System.out.println("no conecta");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        finally{
            if(this.connection != null){
                connection.desconectar();
            }
        }
        return result; 
    }
    //gonna try and see if this works cause it would be cool
    public DoublyLinkedList<Cancion> Read(){
        DoublyLinkedList<Cancion> canciones = new DoublyLinkedList<>();// forgot why i needed this () need to ask tomorrow 
        String req = "SELECT * FROM cancion;";
        try{
            this.connection.connect();
            PreparedStatement statement = this.connection.getConnection().prepareStatement(req);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int idcancion = resultSet.getInt("idcancion");
                String nombre = resultSet.getString("nombrecancion"); //vuelve a como era antes
                String foto = resultSet.getString("nombrefoto");
                canciones.addLast(new Cancion(idcancion, nombre, foto));
                
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        finally {
            if(this.connection != null){
                this.connection.desconectar();
            }
            
        }
        return canciones;
    }


    public int delete(int idCancion) {
        String req = "DELETE FROM cancion WHERE idcancion = ?";
        try {
            if (this.connection.connect()) {
                try (PreparedStatement stmt = this.connection.getConnection().prepareStatement(req)) {
                    stmt.setInt(1, idCancion);
                    return stmt.executeUpdate();
                }
            } else {
                throw new RuntimeException("No se pudo conectar a la BD");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en BD: " + e.getMessage());
        } finally {
            if (this.connection != null) {
                this.connection.desconectar();
            }
        }
    }

}
