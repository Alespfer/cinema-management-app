package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Cet objet représente l'avis qu'un client a laissé sur un film.
 * Il contient la note (sur 5), le commentaire écrit, et la date de l'avis.
 * 
 * Pour l'interface graphique, vous créerez un objet de ce type lorsque l'utilisateur
 * validera le formulaire dans la fenêtre `EvaluationDialog`. Les informations (note,
 * commentaire) proviendront des composants de cette fenêtre.
 * 
 * Vous afficherez également une liste de ces objets dans le `FilmDetailPanel` pour
 * montrer aux autres utilisateurs les avis déjà postés.
 */
public class EvaluationClient implements Serializable {

    // --- Clés de liaison ---
    private int idClient; // Qui a posté l'avis ?
    private int idFilm;   // Pour quel film ?

    // --- Contenu de l'avis ---
    private int note;           // La note donnée (par exemple, de 1 à 5).
    private String commentaire; // Le texte de l'avis.
    private LocalDateTime dateEvaluation; // Quand l'avis a-t-il été posté ?

    /**
     * Constructeur vide (nécessité technique).
     */
    public EvaluationClient() {}

    /**
     * Crée un nouvel avis.
     * @param idClient L'ID du client.
     * @param idFilm L'ID du film.
     * @param note La note attribuée.
     * @param commentaire Le commentaire laissé.
     * @param dateEvaluation La date et l'heure de la soumission.
     */
    public EvaluationClient(int idClient, int idFilm, int note, String commentaire, LocalDateTime dateEvaluation) {
        this.idClient = idClient;
        this.idFilm = idFilm;
        this.note = note;
        this.commentaire = commentaire;
        this.dateEvaluation = dateEvaluation;
    }

    // --- ACCESSEURS (Getters and Setters) ---
    public int getIdClient() { return idClient; }
    public void setIdClient(int idClient) { this.idClient = idClient; }
    public int getIdFilm() { return idFilm; }
    public void setIdFilm(int idFilm) { this.idFilm = idFilm; }
    public int getNote() { return note; }
    public void setNote(int note) { this.note = note; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public LocalDateTime getDateEvaluation() { return dateEvaluation; }
    public void setDateEvaluation(LocalDateTime dateEvaluation) { this.dateEvaluation = dateEvaluation; }
}