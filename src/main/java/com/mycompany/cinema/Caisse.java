package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Classe représentant une caisse enregistreuse ou un point de vente.
 * Utilisée principalement pour les ventes de snacks.
 * 
 * Implémente 'Serializable' pour la persistance des données.
 * 
 */
public class Caisse implements Serializable {
    
    private int idCaisse;
    private String nom;
    private String emplacement;

    /**
     * Constructeur par défaut.
     */
    public Caisse() {}

    /**
     * Constructeur principal pour définir une nouvelle caisse.
     * @param idCaisse L'identifiant unique de la caisse.
     * @param nom Un nom descriptif (ex: "Comptoir Principal").
     * @param emplacement Une description de sa localisation (ex: "Hall d'entrée").
     */
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