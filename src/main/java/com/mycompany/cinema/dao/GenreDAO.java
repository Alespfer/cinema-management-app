// Fichier : GenreDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Genre;
import java.util.List;
import java.util.Optional;

/**
 * Définit le contrat pour la gestion des catégories de films (Action, Drame, etc.).
 * 
 * L'interface graphique utilisera ce contrat (via le service) pour peupler les
 * menus déroulants de filtres dans `ProgrammationPanel` et dans les panneaux d'administration.
 */
public interface GenreDAO {
    void addGenre(Genre genre);
    Optional<Genre> getGenreById(int id);
    List<Genre> getAllGenres();
    void rechargerDonnees();
}