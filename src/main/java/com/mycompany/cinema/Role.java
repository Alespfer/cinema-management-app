package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente le rôle ou le poste d'un membre du personnel (ex: "Administrateur").
 * Permet de gérer les droits et les permissions.
 * 
 * Implémente Serializable pour la sauvegarde.
 * 
 */
public class Role implements Serializable {
    
    private int idRole;
    private String libelle;

    public Role() {}

    /**
     * Constructeur pour créer un nouveau rôle.
     * @param idRole L'ID unique du rôle.
     * @param libelle Le nom du rôle (ex: "Vendeur", "Projectionniste").
     */
    public Role(int idRole, String libelle) {
        this.idRole = idRole;
        this.libelle = libelle;
    }

    // --- Getters and Setters ---

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