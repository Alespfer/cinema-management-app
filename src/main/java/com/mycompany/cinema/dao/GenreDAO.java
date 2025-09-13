// ========================================================================
// GenreDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Genre;
import java.util.List;

/**
 * Définit le contrat pour la gestion des catégories de films (Action, Drame, etc.).
 * Utilisé pour peupler les menus de filtres et pour la gestion du catalogue.
 */
public interface GenreDAO {

    void ajouterGenre(Genre genre);
    Genre trouverGenreParId(int id);
    List<Genre> trouverTousLesGenres();
    void mettreAJourGenre(Genre genre);
    void supprimerGenreParId(int id);    
    void rechargerDonnees();
}