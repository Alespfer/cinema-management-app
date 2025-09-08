package com.mycompany.cinema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * C'est l'un des objets les plus importants pour l'interface graphique. Il
 * contient toutes les informations à afficher sur un film : son titre, son
 * affiche, son résumé, etc.
 *
 * Vous utiliserez un objet `Film` pour peupler : - Les lignes du tableau dans
 * `ProgrammationPanel`. - L'intégralité du panneau de détails
 * `FilmDetailPanel`. - Les formulaires de gestion dans `GestionFilmsPanel` pour
 * l'administrateur.
 *
 * Notez la présence de la `List<Genre> genres`. C'est une liste d'objets
 * `Genre` directement incluse dans le film, ce qui facilite l'affichage des
 * catégories (Action, Aventure...) associées au film.
 */
public class Film implements Serializable {

    private int idFilm;
    private String titre;
    private String synopsis;
    private int dureeMinutes;
    private String classification; // Ex: "Tous publics", "-12 ans"
    private String urlAffiche;     // Le nom du fichier de l'affiche (ex: "dune.jpg")
    private double notePresse;     // La note donnée par les critiques professionnels.

    // Un film peut avoir plusieurs genres (ex: "Science-Fiction" ET "Aventure").
    // On stocke donc une liste d'objets Genre directement ici.
    private List<Genre> genres;

    /**
     * Constructeur vide. Important : on initialise la liste des genres pour
     * éviter les erreurs de type `NullPointerException`.
     */
    public Film() {
        this.genres = new ArrayList<>();
    }

    /**
     * Crée un objet Film complet.
     */
    public Film(int idFilm, String titre, String synopsis, int dureeMinutes, String classification, String urlAffiche, double notePresse) {
        this.idFilm = idFilm;
        this.titre = titre;
        this.synopsis = synopsis;
        this.dureeMinutes = dureeMinutes;
        this.classification = classification;
        this.urlAffiche = urlAffiche;
        this.notePresse = notePresse;
        this.genres = new ArrayList<>(); // Toujours initialiser les listes !
    }

    // --- ACCESSEURS (Getters and Setters) ---
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
     * Calcule le hash code basé sur l'identifiant du film. Cette méthode est
     * essentielle pour garantir le bon fonctionnement du film dans les
     * collections de type HashSet ou comme clé de HashMap. Elle respecte le
     * contrat : si deux films sont égaux, ils ont le même hash code.
     */
    @Override
    public int hashCode() {
        // La méthode statique Objects.hash est une manière simple et robuste
        // de générer un hash code à partir d'un ou plusieurs champs.
        // (cf. doctrine p. 182)
        return Objects.hash(idFilm);
    }
}
