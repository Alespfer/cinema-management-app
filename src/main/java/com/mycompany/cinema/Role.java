package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente le poste ou le niveau de permission d'un membre du personnel.
 * Exemples : "Administrateur", "Vendeur", "Projectionniste".
 * 
 * Cette classe est fondamentale pour la sécurité et la logique de l'interface
 * du personnel.
 * 
 * Dans `LoginFrame`, après une connexion réussie d'un employé, vous récupérerez
 * son objet `Role` pour décider quelle fenêtre principale ouvrir :
 * - Si le `libelle` est "Vendeur", vous ouvrez `PointDeVenteFrame`.
 * - Pour tout autre rôle ("Administrateur"), vous ouvrez `AdminMainFrame`.
 * 
 * Dans le panneau `GestionPersonnelPanel`, vous utiliserez une liste de `Role`s
 * pour peupler la JComboBox qui permet d'assigner un rôle à un employé.
 */
public class Role implements Serializable {
    
    private int idRole;
    private String libelle; // Le nom du rôle, ex: "Administrateur"

    /**
     * Constructeur vide (nécessité technique).
     */
    public Role() {}

    /**
     * Crée un nouveau rôle dans le système.
     * @param idRole L'ID unique du rôle.
     * @param libelle Le nom du rôle.
     */
    public Role(int idRole, String libelle) {
        this.idRole = idRole;
        this.libelle = libelle;
    }

    // --- ACCESSEURS (Getters and Setters) ---

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