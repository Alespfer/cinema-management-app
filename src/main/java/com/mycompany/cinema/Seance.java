package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Représente une projection d'un film, à une date et une heure données,
 * dans une salle spécifique. C'est l'événement principal que les clients réservent.
 * 
 * Implémente Serializable pour la sauvegarde.
 * 
 * @author [Mets ton nom ici]
 */
public class Seance implements Serializable {
    
    private int idSeance;
    private LocalDateTime dateHeureDebut;
    
    // Clés étrangères vers le film projeté et la salle de projection
    private int idFilm;
    private int idSalle;

    public Seance() {}

    /**
     * Constructeur pour créer une nouvelle séance.
     * @param idSeance L'ID unique de la séance.
     * @param dateHeureDebut La date et l'heure précises du début du film.
     * @param idSalle L'ID de la salle où le film est projeté.
     * @param idFilm L'ID du film concerné.
     */
    public Seance(int idSeance, LocalDateTime dateHeureDebut, int idSalle, int idFilm) {
        this.idSeance = idSeance;
        this.dateHeureDebut = dateHeureDebut;
        this.idSalle = idSalle;
        this.idFilm = idFilm;
    }

    // --- Getters and Setters ---

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