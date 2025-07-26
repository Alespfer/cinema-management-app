/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;

// package com.mycompany.cinema; // Garde ton package

import java.io.Serializable;

public class Caisse implements Serializable {
    private int idCaisse;
    private String nom;
    private String emplacement;

    public Caisse() {}

    public Caisse(int idCaisse, String nom, String emplacement) {
        this.idCaisse = idCaisse;
        this.nom = nom;
        this.emplacement = emplacement;
    }

    // --- Getters ---
    public int getId() {
        return idCaisse;
    }

    public String getNom() {
        return nom;
    }

    public String getEmplacement() {
        return emplacement;
    }

    // --- Setters ---
    public void setId(int idCaisse) {
        this.idCaisse = idCaisse;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }
}