// ========================================================================
// GenericDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

/**
 * Contrat de base pour tous les gestionnaires de données (DAO).
 * Règle pour s'assurer que chaque DAO possède une méthode pour se rafraîchir 
 * à partir de sa source de données (les fichiers .dat).
 * @param <T> Le type de l'objet (ex: Film) que le DAO gère.
 */
public interface GenericDAO<T> {
    
    /** Force le rechargement des données depuis le disque. */
    void rechargerDonnees();
}