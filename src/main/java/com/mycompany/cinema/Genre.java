/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;

import java.io.Serializable;

public class Genre implements Serializable {
    private int idGenre;
    private String libelle;

    public Genre() {}

    public Genre(int idGenre, String libelle) {
        this.idGenre = idGenre;
        this.libelle = libelle;
    }
    
    // Getters and Setters
    public int getId() { return idGenre; }
    public void setId(int idGenre) { this.idGenre = idGenre; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
}