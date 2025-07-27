package com.mycompany.cinema.util;

/**
 * Interface fonctionnelle pour extraire un identifiant entier depuis un objet.
 * Utilisée pour remplacer les expressions lambda interdites dans le cours.
 *
 * @param <T> Le type de l'objet à partir duquel extraire l'ID.
 */
public interface IdExtractor<T> {
    int extractId(T object);
}
