package com.mycompany.cinema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un film projeté au cinéma.
 * Contient toutes les informations descriptives d'un film.
 * 
 * Implémente Serializable pour la sauvegarde.
 * 
 */
public class Film implements Serializable {
    
    private int idFilm;
    private String titre;
    private String synopsis;
    private int dureeMinutes;
    private String classification;
    private String urlAffiche; // Le nom du fichier image (ex: "dune.jpg")
    
    // Pour la relation Many-to-Many avec Genre, on inclut directement la liste des genres.
    // C'est une façon plus "objet" de représenter ce lien que d'avoir une classe Film_Genre.
    private List<Genre> genres;

    public Film() {
        this.genres = new ArrayList<>();
    }

    public Film(int idFilm, String titre, String synopsis, int dureeMinutes, String classification, String urlAffiche) {
        this.idFilm = idFilm;
        this.titre = titre;
        this.synopsis = synopsis;
        this.dureeMinutes = dureeMinutes;
        this.classification = classification;
        this.urlAffiche = urlAffiche;
        this.genres = new ArrayList<>(); // Toujours initialiser la liste pour éviter les NullPointerException
    }

    // --- Getters and Setters ---

    public int getId() { 
        return idFilm; 
    }
    public void setId(int idFilm) { 
        this.idFilm = idFilm; 
    }

    public String getTitre() { 
        return titre; 
    }
    public void setTitre(String titre) { 
        this.titre = titre; 
    }

    public String getSynopsis() { 
        return synopsis; 
    }
    public void setSynopsis(String synopsis) { 
        this.synopsis = synopsis; 
    }

    public int getDureeMinutes() { 
        return dureeMinutes; 
    }
    public void setDureeMinutes(int dureeMinutes) { 
        this.dureeMinutes = dureeMinutes; 
    }

    public String getClassification() { 
        return classification; 
    }
    public void setClassification(String classification) { 
        this.classification = classification; 
    }

    public String getUrlAffiche() { 
        return urlAffiche; 
    }
    public void setUrlAffiche(String urlAffiche) { 
        this.urlAffiche = urlAffiche; 
    }

    public List<Genre> getGenres() { 
        return genres; 
    }
    public void setGenres(List<Genre> genres) { 
        this.genres = genres; 
    }
}