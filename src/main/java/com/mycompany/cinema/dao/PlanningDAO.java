// Fichier : PlanningDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Planning;
import java.util.List;

/**
 * Définit le contrat pour la gestion des plannings de travail du personnel.
 * 
 * Ce contrat sera utilisé par un futur panneau d'administration de gestion
 * des plannings pour créer et afficher les horaires de service de chaque employé.
 */
public interface PlanningDAO {
    void addPlanning(Planning planning);
    List<Planning> getPlanningsByPersonnelId(int personnelId);
    List<Planning> getAllPlannings(); // Nécessaire pour l'IdManager
    void rechargerDonnees();
}