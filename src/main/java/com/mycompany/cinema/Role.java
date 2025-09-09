package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un rôle attribué à un membre du personnel.
 */
public class Role implements Serializable {

    // Identifiant unique du rôle.
    private int idRole;

    // Libellé du rôle (ex: "Administrateur", "Vendeur").
    private String libelle;

    public Role() {
    }

    public Role(int idRole, String libelle) {
        this.idRole = idRole;
        this.libelle = libelle;
    }
    
    
    // --- Getters / Setters ---


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
