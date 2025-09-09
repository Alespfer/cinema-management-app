// ========================================================================
// FICHIER : GenericDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

/**
 * Contrat de base pour TOUS les gestionnaires de données (DAO).
 * C'est une règle technique qui assure que chaque DAO possède une méthode
 * pour se rafraîchir à partir de sa source de données (les fichiers .dat).
 * @param <T> Le type de l'objet (ex: Film) que le DAO gère.
 */
public interface GenericDAO<T> {
    
    /** Force le rechargement des données depuis le disque. */
    void rechargerDonnees();
}