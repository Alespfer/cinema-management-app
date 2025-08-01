package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Représente un événement de projection unique : un film spécifique,
 * dans une salle donnée, à une date et une heure précises.
 * C'est l'entité centrale que les clients choisissent et réservent.
 * 
 * Dans l'interface graphique, vous afficherez des listes d'objets `Seance`.
 * - Dans `ProgrammationPanel`, chaque ligne du tableau correspond à une séance.
 * - Dans `FilmDetailPanel`, vous afficherez les horaires (qui sont des séances) pour un film.
 * - Lorsqu'un client clique sur un horaire, vous stockerez l'objet `Seance` sélectionné
 *   pour le passer à l'étape suivante, le `SiegePanel`, qui en a besoin pour savoir
 *   quels sièges afficher et lesquels sont déjà occupés.
 */
public class Seance implements Serializable {
    
    private int idSeance;
    private LocalDateTime dateHeureDebut; // Date et heure exactes du début de la projection.
    
    // --- Clés de liaison ---
    private int idFilm;  // Quel film est projeté ?
    private int idSalle; // Dans quelle salle ?

    /**
     * Constructeur vide (nécessité technique pour la sauvegarde).
     */
    public Seance() {}

    /**
     * Crée une nouvelle séance de projection.
     * @param idSeance L'ID unique de la séance.
     * @param dateHeureDebut Le moment précis du début du film.
     * @param idSalle L'ID de la salle où a lieu la projection.
     * @param idFilm L'ID du film projeté.
     */
    public Seance(int idSeance, LocalDateTime dateHeureDebut, int idSalle, int idFilm) {
        this.idSeance = idSeance;
        this.dateHeureDebut = dateHeureDebut;
        this.idSalle = idSalle;
        this.idFilm = idFilm;
    }

    // --- ACCESSEURS (Getters and Setters) ---

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