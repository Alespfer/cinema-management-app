package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente une catégorie de film, comme "Action", "Drame", ou "Science-Fiction".
 * 
 * Dans l'interface graphique, vous utiliserez principalement une liste d'objets `Genre`
 * pour peupler les menus déroulants de filtres (par exemple dans `ProgrammationPanel`).
 * Vous pourrez aussi afficher les libellés des genres sur la page de détail d'un film.
 */
public class Genre implements Serializable {
    
    private int idGenre;    // Le numéro unique du genre.
    private String libelle; // Le nom du genre (ex: "Aventure").

    /**
     * Constructeur vide (nécessité technique).
     */
    public Genre() {}

    /**
     * Crée une nouvelle catégorie de film.
     * @param idGenre L'identifiant unique.
     * @param libelle Le nom de la catégorie.
     */
    public Genre(int idGenre, String libelle) {
        this.idGenre = idGenre;
        this.libelle = libelle;
    }
    
    // --- ACCESSEURS (Getters and Setters) ---

    public int getId() { 
        return idGenre; 
    }
    public void setId(int idGenre) { 
        this.idGenre = idGenre; 
    }
    
    public String getLibelle() { 
        return libelle; 
    }
    public void setLibelle(String libelle) { 
        this.libelle = libelle; 
    }
}