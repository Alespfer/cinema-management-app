/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;

import java.io.Serializable;

public class AffectationSeance implements Serializable {
    private int idSeance;
    private int idPersonnel;

    public AffectationSeance() {}

    public AffectationSeance(int idSeance, int idPersonnel) {
        this.idSeance = idSeance;
        this.idPersonnel = idPersonnel;
    }
    
    // --- Getters ---
    public int getIdSeance() {
        return idSeance;
    }

    public int getIdPersonnel() {
        return idPersonnel;
    }

    // --- Setters ---
    public void setIdSeance(int idSeance) {
        this.idSeance = idSeance;
    }

    public void setIdPersonnel(int idPersonnel) {
        this.idPersonnel = idPersonnel;
    }
    
    
}