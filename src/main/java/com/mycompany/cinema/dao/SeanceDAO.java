package com.mycompany.cinema.dao;

import com.mycompany.cinema.Seance;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Contrat pour la gestion de la persistance des Séances.
 * Inclut des méthodes de recherche spécifiques au métier.
 */
public interface SeanceDAO {
    void addSeance(Seance seance);
    Optional<Seance> getSeanceById(int id);
    List<Seance> getAllSeances();
    List<Seance> getSeancesByFilmId(int filmId);
    List<Seance> getSeancesByDate(LocalDate date);
    void updateSeance(Seance seance);

    void deleteSeance(int id);

    void rechargerDonnees();
}