package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un genre cinématographique (ex. Action, Drame, Science-Fiction).
 */
public class Genre implements Serializable {

    // Identifiant unique du genre.
    private int idGenre;

    // Libellé du genre.
    private String libelle;

    public Genre() {
    }

    /**
     * Constructeur principal.
     *
     * @param idGenre identifiant du genre
     * @param libelle libellé du genre
     */
    public Genre(int idGenre, String libelle) {
        this.idGenre = idGenre;
        this.libelle = libelle;
    }

    // --- Getters / Setters ---
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
