/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;

import java.io.Serializable;

public class Tarif implements Serializable {
    private int idTarif;
    private String libelle;
    private double prix;

    public Tarif() {}

    public Tarif(int idTarif, String libelle, double prix) {
        this.idTarif = idTarif;
        this.libelle = libelle;
        this.prix = prix;
    }

    // Getters and Setters
    public int getId() { 
        return idTarif; 
    }
    public void setId(int idTarif) { 
        this.idTarif = idTarif; 
    }
    public String getLibelle() { 
        return libelle; 
    }
    public void setLibelle(String libelle) { 
        this.libelle = libelle; 
    }
    public double getPrix() { 
        return prix; 
    }
    public void setPrix(double prix) { 
        this.prix = prix; 
    }
}