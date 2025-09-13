package com.mycompany.cinema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Représente un film avec ses données principales : titre, synopsis, durée,
 * classification, affiche, note, etc.
 * Sert de référence pour l’affichage dans la programmation, le détail d’un film
 * ou les formulaires d’administration.
 */
public class Film implements Serializable {

    // Identifiant unique du film.
    private int idFilm;

    // Titre du film.
    private String titre;

    // Synopsis.
    private String synopsis;

    // Durée en minutes.
    private int dureeMinutes;

    // Classification (ex. “Tous publics”, “-12 ans”, etc.).
    private String classification;

    // Nom de fichier ou URL de l’affiche.
    private String imageAffiche;

    // Note presse agrégée.
    private double notePresse;

    // Liste contenant les différents Genres associés au film.
    private List<Genre> genres;

    
    public Film() {
        this.genres = new ArrayList<Genre>();
    }

    /**
     * Constructeur principal.
     */
    public Film(int idFilm, String titre, String synopsis, int dureeMinutes, String classification, String urlAffiche, double notePresse) {
        this.idFilm = idFilm;
        this.titre = titre;
        this.synopsis = synopsis;
        this.dureeMinutes = dureeMinutes;
        this.classification = classification;
        this.imageAffiche = urlAffiche;
        this.notePresse = notePresse;
        this.genres = new ArrayList<Genre>();
    }

    // --- Getters / Setters ---
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
        return imageAffiche;
    }

    public void setUrlAffiche(String imageAffiche) {
        this.imageAffiche = imageAffiche;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public double getNotePresse() {
        return notePresse;
    }

    public void setNotePresse(double notePresse) {
        this.notePresse = notePresse;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Film film = (Film) obj;
        return idFilm == film.idFilm;
    }

    /**
     * Hash basé sur l’identifiant, conforme à equals.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idFilm);
    }
}
