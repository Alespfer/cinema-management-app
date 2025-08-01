package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un point de vente physique ou une caisse enregistreuse.
 * Sert principalement à localiser où une vente de snack a été effectuée.
 * 
 * L'interface graphique client n'utilisera pas cette classe. Elle sera en revanche
 * essentielle pour le "PointDeVenteFrame" (l'interface du vendeur) et pour les
 * rapports de ventes dans le panneau d'administration.
 */
public class Caisse implements Serializable {
    
    private int idCaisse;       // Le numéro unique de la caisse.
    private String nom;         // Un nom facile à reconnaître (ex: "Comptoir Principal").
    private String emplacement; // Une description de sa position (ex: "Hall d'entrée").

    /**
     * Constructeur vide (nécessité technique).
     */
    public Caisse() {}

    /**
     * Crée une nouvelle caisse.
     * @param idCaisse L'identifiant unique.
     * @param nom Son nom.
     * @param emplacement Sa localisation.
     */
    public Caisse(int idCaisse, String nom, String emplacement) {
        this.idCaisse = idCaisse;
        this.nom = nom;
        this.emplacement = emplacement;
    }

    // --- ACCESSEURS (Getters & Setters) ---

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