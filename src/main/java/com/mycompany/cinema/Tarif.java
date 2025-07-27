package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un type de tarif pour un billet de cinéma (ex: "Plein Tarif").
 * Chaque tarif a un libellé descriptif et un prix.
 * 
 * Implémente Serializable pour la sauvegarde.
 * 
 */
public class Tarif implements Serializable {
    
    private int idTarif;
    private String libelle;
    private double prix;

    public Tarif() {}

    /**
     * Constructeur pour créer un nouveau tarif.
     * @param idTarif L'ID unique du tarif.
     * @param libelle Le nom du tarif (ex: "Tarif Étudiant").
     * @param prix Le prix en euros correspondant.
     */
    public Tarif(int idTarif, String libelle, double prix) {
        this.idTarif = idTarif;
        this.libelle = libelle;
        this.prix = prix;
    }

    // --- Getters and Setters ---

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