/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Film implements Serializable {
    private int idFilm;
    private String titre;
    private String synopsis;
    private int dureeMinutes;
    private String classification;
    private String urlAffiche;
    private List<Genre> genres; // La relation Many-to-Many est matérialisée ici

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
        this.genres = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() { return idFilm; }
    public void setId(int idFilm) { this.idFilm = idFilm; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }
    public int getDureeMinutes() { return dureeMinutes; }
    public void setDureeMinutes(int dureeMinutes) { this.dureeMinutes = dureeMinutes; }
    public String getClassification() { return classification; }
    public void setClassification(String classification) { this.classification = classification; }
    public String getUrlAffiche() { return urlAffiche; }
    public void setUrlAffiche(String urlAffiche) { this.urlAffiche = urlAffiche; }
    public List<Genre> getGenres() { return genres; }
    public void setGenres(List<Genre> genres) { this.genres = genres; }
}