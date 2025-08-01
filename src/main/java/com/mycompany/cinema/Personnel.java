package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un employé du cinéma (administrateur, vendeur, etc.).
 * 
 * Cette classe est cruciale pour la partie "back-office" de l'application.
 * L'interface graphique utilisera un objet `Personnel` après une connexion réussie
 * en mode "Personnel" pour :
 * 1. Déterminer quelle interface afficher (Panneau Admin complet ou Point de Vente).
 * 2. Afficher le nom de l'employé connecté.
 * 3. Gérer les droits d'accès.
 * 
 * L'interface client n'a pas besoin de connaître cette classe.
 */
public class Personnel implements Serializable {
    
    private int idPersonnel;
    private String nom;
    private String prenom;
    private String motDePasse;
    
    // Fait le lien avec l'objet `Role` pour connaître ses permissions.
    private int idRole;

    /**
     * Constructeur vide (nécessité technique).
     */
    public Personnel() {}

    /**
     * Crée un nouvel employé.
     */
    public Personnel(int idPersonnel, String nom, String prenom, String motDePasse, int idRole) {
        this.idPersonnel = idPersonnel;
        this.nom = nom;
        this.prenom = prenom;
        this.motDePasse = motDePasse;
        this.idRole = idRole;
    }

    // --- ACCESSEURS (Getters) ---

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

    // --- MUTATEURS (Setters) ---

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