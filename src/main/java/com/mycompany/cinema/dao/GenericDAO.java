package com.mycompany.cinema.dao;

import java.util.List;

/**
 * Contrat de base pour TOUS les gestionnaires de données (DAO).
 * C'est une règle technique qui assure que chaque DAO possède une méthode
 * pour se rafraîchir à partir de sa source de données (les fichiers .dat).
 * 
 * L'interface graphique n'utilisera jamais ce contrat directement. C'est un
 * outil pour garantir la cohérence et la robustesse de l'architecture.
 */
public interface GenericDAO<T> {
    
    /** Force le rechargement des données depuis le disque. */
    void rechargerDonnees();
}