package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente une catégorie de prix pour un billet de cinéma.
 * Chaque tarif a un nom (ex: "Tarif Étudiant") et un prix associé en euros.
 * 
 * Dans l'interface client `SiegePanel`, vous afficherez une liste d'objets `Tarif`
 * dans une JComboBox (un menu déroulant). Cela permettra à l'utilisateur de choisir
 * le tarif qui s'applique à sa réservation (ex: il est étudiant, il vient avec un enfant).
 * L'attribut `prix` sera ensuite utilisé pour calculer le coût total de la réservation.
 * 
 * Dans la partie administration, `GestionTarifsPanel` permettra de créer,
 * modifier ou supprimer ces tarifs.
 */
public class Tarif implements Serializable {
    
    private int idTarif;
    private String libelle; // Le nom du tarif (ex: "Plein Tarif").
    private double prix;    // Le prix en euros.

    /**
     * Constructeur vide (nécessité technique).
     */
    public Tarif() {}

    /**
     * Crée un nouveau type de tarif.
     * @param idTarif L'ID unique du tarif.
     * @param libelle Le nom du tarif.
     * @param prix Le prix correspondant.
     */
    public Tarif(int idTarif, String libelle, double prix) {
        this.idTarif = idTarif;
        this.libelle = libelle;
        this.prix = prix;
    }

    // --- ACCESSEURS (Getters and Setters) ---

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