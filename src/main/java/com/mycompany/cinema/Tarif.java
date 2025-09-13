package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un tarif appliqué à un billet de cinéma.
 */
public class Tarif implements Serializable {

    // Identifiant unique du tarif.
    private int idTarif;

    // Nom du tarif (ex: "Plein Tarif", "Étudiant").
    private String libelle;

    // Prix du tarif
    private double prix;

    
    public Tarif() {
    }

    /**
     * Constructeur principal.
     *
     * @param idTarif identifiant unique
     * @param libelle libellé du tarif
     * @param prix prix associé
     */
    public Tarif(int idTarif, String libelle, double prix) {
        this.idTarif = idTarif;
        this.libelle = libelle;
        this.prix = prix;
    }
    
    // --- Getters / Setters ---

    public int getId() {
        return idTarif;
    }

    public void setId(int idTarif) {
        this.idTarif = idTarif;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}
