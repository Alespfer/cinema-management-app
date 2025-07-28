package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Représente l'évaluation (note et commentaire) d'un film par un client.
 * La paire (idClient, idFilm) sert de clé primaire composite, garantissant
 * qu'un client ne peut évaluer un même film qu'une seule fois.
 */
public class EvaluationClient implements Serializable {

    private int idClient;
    private int idFilm;
    private int note; // Note sur 5, par exemple.
    private String commentaire;
    private LocalDateTime dateEvaluation;

    public EvaluationClient() {}

    public EvaluationClient(int idClient, int idFilm, int note, String commentaire, LocalDateTime dateEvaluation) {
        this.idClient = idClient;
        this.idFilm = idFilm;
        this.note = note;
        this.commentaire = commentaire;
        this.dateEvaluation = dateEvaluation;
    }

    // --- Getters and Setters ---
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