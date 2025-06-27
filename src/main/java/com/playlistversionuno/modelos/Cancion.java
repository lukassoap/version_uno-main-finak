/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.playlistversionuno.modelos;

/**
 *
 * @author yacog
 */
public class Cancion {
    private int codigo;
    private String titulo;
    private String foto;

    public Cancion(int codigo, String titulo, String foto) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.foto = foto;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        // Asegurar que el título no tenga extensión
        this.titulo = titulo.contains(".") ? 
                    titulo.substring(0, titulo.lastIndexOf('.')) : 
                    titulo;
    }

    public String getFoto() {
        return foto;
    }

public void setFoto(String foto) {
    if (foto != null) {
        this.foto = foto.replace(".png.png", ".png").replace(".jpg.jpg", ".jpg");
    }
}
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cancion other = (Cancion) obj;
        return this.codigo == other.codigo && 
            this.titulo.equals(other.titulo) && 
            this.foto.equals(other.foto);
    }

    
        
}
