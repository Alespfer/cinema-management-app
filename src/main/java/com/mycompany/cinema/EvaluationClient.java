package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Évaluation déposée par un client pour un film (note et commentaire). Utilisée
 * pour l’affichage des avis et le calcul d’indicateurs.
 */
public class EvaluationClient implements Serializable {

    //Identifiant du client auteur de l’évaluation.
    private int idClient;

    // Identifiant du film évalué.
    private int idFilm;

    // Note attribuée. 
    private int note;

    // Commentaire libre du client.
    private String commentaire;

    // Date de la soumission de l’avis.
    private LocalDateTime dateEvaluation;

    
    public EvaluationClient() {
    }

    /**
     * Constructeur principal.
     *
     * @param idClient identifiant du client
     * @param idFilm identifiant du film
     * @param note note attribuée
     * @param commentaire commentaire associé
     * @param dateEvaluation date/heure de l’évaluation
     */
    public EvaluationClient(int idClient, int idFilm, int note, String commentaire, LocalDateTime dateEvaluation) {
        this.idClient = idClient;
        this.idFilm = idFilm;
        this.note = note;
        this.commentaire = commentaire;
        this.dateEvaluation = dateEvaluation;
    }

    // --- Getters / Setters ---
    
    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public LocalDateTime getDateEvaluation() {
        return dateEvaluation;
    }

    public void setDateEvaluation(LocalDateTime dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }
}
