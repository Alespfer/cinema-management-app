/*
 * CinemaService.java
 * Définit le contrat de base pour les fonctionnalités de consultation accessibles à tous.
 * Toute classe de service liée au cinéma devra, au minimum, implémenter ces méthodes.
 */

package com.mycompany.cinema.service;

import com.mycompany.cinema.Film;
import java.util.List;


public interface CinemaService {

    /**
     * Retrouve la liste de tous les films actuellement à l’affiche.
     * @return Une liste d'objets Film.
     */
    List<Film> trouverFilmsAffiche();

    /**
     * Retrouve les informations détaillées d'un film à partir de son identifiant.
     * @param idFilm L'identifiant unique du film.
     * @return L'objet Film correspondant, ou `null` si l'identifiant est introuvable.
     */
    Film trouverDetailsFilm(int idFilm);

    /**
     * Recherche des films dont le titre contient un mot-clé donné.
     * La recherche doit être implémentée de manière insensible à la casse (majuscules/minuscules).
     * @param motCle Le texte à rechercher dans les titres des films.
     * @return Une liste de films correspondant au critère de recherche ; peut être vide.
     */
    List<Film> rechercherFilmsParTitre(String motCle);
    
    
    /**
     * Calcule la note moyenne attribuée par les spectateurs pour un film spécifique.
     * @param idFilm L'identifiant du film.
     * @return La note moyenne sous forme de `double`. Si aucune évaluation n'existe pour ce film,
     *         la méthode doit retourner 0.0.
     */
    double calculerNoteMoyenneSpectateurs(int idFilm);
}
