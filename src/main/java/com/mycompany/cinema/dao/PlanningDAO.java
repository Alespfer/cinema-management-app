package com.mycompany.cinema.dao;

import com.mycompany.cinema.Planning;
import java.util.List;

/**
 * Contrat pour la gestion de la persistance des Plannings.
 */
public interface PlanningDAO {
    
    void addPlanning(Planning planning);

    List<Planning> getPlanningsByPersonnelId(int personnelId);

    List<Planning> getAllPlannings(); // Nécessaire pour IdManager
}