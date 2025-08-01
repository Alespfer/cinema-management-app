// Fichier : FilmDAO.java
package com.mycompany.cinema.dao;
import com.mycompany.cinema.Film;
import java.util.List;
import java.util.Optional;

/**
 * Définit le contrat pour la gestion du catalogue de films.
 * C'est l'un des contrats les plus utilisés par l'interface graphique.
 * 
 * - `getAllFilms` et `findFilmsByTitre` sont la base du `ProgrammationPanel`.
 * - `getFilmById` est essentiel pour le `FilmDetailPanel`.
 * - `addFilm`, `updateFilm`, `deleteFilm` sont les opérations du `GestionFilmsPanel` de l'admin.
 */
public interface FilmDAO {
    void addFilm(Film film);
    Optional<Film> getFilmById(int id);
    List<Film> getAllFilms();
    void updateFilm(Film film);
    void deleteFilm(int id);
    List<Film> findFilmsByTitre(String keyword); 
    void rechargerDonnees();
}
