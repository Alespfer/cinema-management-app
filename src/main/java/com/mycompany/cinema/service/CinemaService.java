package com.mycompany.cinema.service;

import com.mycompany.cinema.Film;
import java.util.List;

/**
 * Interface commune pour toutes les fonctionnalités de consultation des films.
 */
public interface CinemaService {

    /** Retourne la liste des films actuellement à l’affiche. */
    List<Film> getFilmsAffiche();

    /** Donne les détails complets d’un film à partir de son identifiant. */
    Film getFilmDetails(int filmId);

    /** Recherche des films contenant un mot-clé dans leur titre.
     * La recherche n’est pas sensible à la casse.
     */
    List<Film> findFilmsByTitre(String keyword);
    
    
    /**
     * Calcule la note moyenne des spectateurs pour un film donné.
     * @param filmId L'ID du film.
     * @return La note moyenne, ou 0.0 si aucune note n'existe.
     */
    double getNoteMoyenneSpectateurs(int filmId);
}
