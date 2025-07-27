package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente une catégorie de film (ex: "Action", "Drame", "SF").
 * Un film peut être associé à plusieurs genres.
 * 
 * Implémente Serializable pour la sauvegarde.
 * 
 */
public class Genre implements Serializable {
    
    private int idGenre;
    private String libelle;

    public Genre() {}

    /**
     * Constructeur pour créer un nouveau genre.
     * @param idGenre L'identifiant unique du genre.
     * @param libelle Le nom du genre (ex: "Science-Fiction").
     */
    public Genre(int idGenre, String libelle) {
        this.idGenre = idGenre;
        this.libelle = libelle;
    }
    
    // --- Getters and Setters ---

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