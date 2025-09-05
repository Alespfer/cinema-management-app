// Fichier : SeanceDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Seance;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Définit le contrat pour la gestion des séances (projections de films).
 * 
 * C'est un contrat central pour l'interface client. Il est utilisé partout
 * où l'on doit afficher des horaires :
 * - `getSeancesByDate`, `getSeancesByFilmId` sont la base du système de filtres
 *   du `ProgrammationPanel`.
 */
public interface SeanceDAO {
    void addSeance(Seance seance);
    Seance getSeanceById(int id);
    List<Seance> getAllSeances();
    List<Seance> getSeancesByFilmId(int filmId);
    List<Seance> getSeancesByDate(LocalDate date);
    void updateSeance(Seance seance);
    void deleteSeance(int id);
    void rechargerDonnees();
}