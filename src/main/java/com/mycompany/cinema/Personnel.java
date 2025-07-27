package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un membre du personnel du cinéma.
 * Chaque membre du personnel a un rôle (Administrateur, Vendeur, etc.)
 * qui détermine ses droits et fonctions.
 * 
 * Implémente Serializable pour la sauvegarde.
 * 
 */
public class Personnel implements Serializable {
    
    private int idPersonnel;
    private String nom;
    private String prenom;
    private String motDePasse;
    
    // Clé étrangère vers l'objet Role
    private int idRole;

    public Personnel() {}

    /**
     * Constructeur pour créer un nouvel employé.
     */
    public Personnel(int idPersonnel, String nom, String prenom, String motDePasse, int idRole) {
        this.idPersonnel = idPersonnel;
        this.nom = nom;
        this.prenom = prenom;
        this.motDePasse = motDePasse;
        this.idRole = idRole;
    }

    // --- Getters ---

    public int getId() { 
        return idPersonnel; 
    }
    public String getNom() { 
        return nom; 
    }
    public String getPrenom() { 
        return prenom; 
    }
    public String getMotDePasse() { 
        return motDePasse; 
    }
    public int getIdRole() { 
        return idRole;
    }

    // --- Setters ---

    public void setId(int idPersonnel) { 
        this.idPersonnel = idPersonnel; 
    }
    public void setNom(String nom) { 
        this.nom = nom; 
    }
    public void setPrenom(String prenom) { 
        this.prenom = prenom; 
    }
    public void setMotDePasse(String motDePasse) { 
        this.motDePasse = motDePasse; 
    }
    public void setIdRole(int idRole) { 
        this.idRole = idRole; 
    }
}