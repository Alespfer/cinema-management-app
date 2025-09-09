// ========================================================================
// FICHIER : FilmDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Film;
import java.util.List;

/**
 * Définit le contrat pour la gestion du catalogue de films.
 */
public interface FilmDAO {

    void ajouterFilm(Film film);
    Film trouverFilmParId(int id);
    List<Film> rechercherFilmsParTitre(String motCle); 
    List<Film> trouverTousLesFilms();
    void mettreAJourFilm(Film film);
    void supprimerFilmParId(int id);
    void rechargerDonnees();
}