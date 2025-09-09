package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Représente une séance de projection (film + salle + horaire).
 */
public class Seance implements Serializable {

    // Identifiant unique de la séance.
    private int idSeance;

    // Date et heure de début de la projection.
    private LocalDateTime dateHeureDebut;

    // Identifiant du film projeté.
    private int idFilm;

    // Identifiant de la salle utilisée.
    private int idSalle;

    
    public Seance() {
    }

    /**
     * Constructeur principal.
     *
     * @param idSeance identifiant unique de la séance
     * @param dateHeureDebut date et heure de début
     * @param idSalle identifiant de la salle
     * @param idFilm identifiant du film
     */
    public Seance(int idSeance, LocalDateTime dateHeureDebut, int idSalle, int idFilm) {
        this.idSeance = idSeance;
        this.dateHeureDebut = dateHeureDebut;
        this.idSalle = idSalle;
        this.idFilm = idFilm;
    }
    
    // --- Getters / Setters ---

    public int getId() {
        return idSeance;
    }

    public void setId(int idSeance) {
        this.idSeance = idSeance;
    }

    public LocalDateTime getDateHeureDebut() {
        return dateHeureDebut;
    }

    public void setDateHeureDebut(LocalDateTime dateHeureDebut) {
        this.dateHeureDebut = dateHeureDebut;
    }

    public int getIdSalle() {
        return idSalle;
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }
}
