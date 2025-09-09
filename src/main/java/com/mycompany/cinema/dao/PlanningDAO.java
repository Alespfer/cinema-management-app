// ========================================================================
// FICHIER : PlanningDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Planning;
import java.util.List;

/**
 * Définit le contrat pour la gestion des plannings de travail du personnel.
 */
public interface PlanningDAO {

    void ajouterPlanning(Planning planning);
    List<Planning> trouverPlanningsParIdPersonnel(int idPersonnel);
    List<Planning> trouverTousLesPlannings();
    void rechargerDonnees();
}