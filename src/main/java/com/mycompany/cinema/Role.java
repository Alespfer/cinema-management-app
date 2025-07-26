/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;

import java.io.Serializable;

public class Role implements Serializable {
    private int idRole;
    private String libelle;

    public Role() {}

    public Role(int idRole, String libelle) {
        this.idRole = idRole;
        this.libelle = libelle;
    }

    // Getters and Setters
    public int getId() { 
        return idRole; 
    }
    public void setId(int idRole) { 
        this.idRole = idRole; 
    }
    public String getLibelle() { 
        return libelle; 
    }
    public void setLibelle(String libelle) { 
        this.libelle = libelle; 
    }
}