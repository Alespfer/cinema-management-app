package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente une caisse enregistreuse ou un point de vente physique.
 *
 * Utilisée pour tracer l’origine des ventes (snacking, billetterie, etc.)
 * et alimenter les rapports côté administration.
 */
public class Caisse implements Serializable {

    // Identifiant unique de la caisse. 
    private int idCaisse;

    // Nom attribué à la caisse (ex. "Comptoir Principal").
    private String nom;

    // Emplacement ou localisation de la caisse (ex. "Hall d'entrée"). 
    private String emplacement;

    // Constructeur par défaut requis pour la sérialisation. 
    public Caisse() {}

    /**
     * Constructeur principal.
     *
     * @param idCaisse identifiant unique
     * @param nom nom de la caisse
     * @param emplacement localisation de la caisse
     */
    public Caisse(int idCaisse, String nom, String emplacement) {
        this.idCaisse = idCaisse;
        this.nom = nom;
        this.emplacement = emplacement;
    }
    
    // --- Getters / Setters ---

    public int getId() {
        return idCaisse;
    }

    public void setId(int idCaisse) {
        this.idCaisse = idCaisse;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }
}
