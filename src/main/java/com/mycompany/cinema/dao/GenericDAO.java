package com.mycompany.cinema.dao;

import java.util.List;

/**
 * Interface de base pour tous les DAO, définissant les opérations communes.
 */
public interface GenericDAO<T> {
    /**
     * Force le DAO à relire sa source de données, mettant à jour son état interne.
     */
    void rechargerDonnees();
}