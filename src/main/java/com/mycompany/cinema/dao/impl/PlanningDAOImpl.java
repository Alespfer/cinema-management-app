// ========================================================================
// FICHIER : PlanningDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Planning;
import com.mycompany.cinema.dao.PlanningDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation pour la gestion des plannings de travail. Interagit avec le
 * fichier "plannings.dat".
 *
 * Cette classe est utilisée par le back-office pour créer et afficher les
 * horaires de service de chaque employé.
 */
public class PlanningDAOImpl extends GenericDAOImpl<Planning> implements PlanningDAO {

    public PlanningDAOImpl() {
        super("plannings.dat");
    }

    /**
     * Ajoute un nouveau créneau de travail au planning.
     *
     * @param planning Le créneau à enregistrer.
     */
    @Override
    public void ajouterPlanning(Planning planning) {
        this.data.add(planning);
        sauvegarderDansFichier();
    }

    /**
     * Recherche tous les créneaux de planning pour un membre du personnel
     * spécifique.
     *
     * @param idPersonnel L'identifiant de l'employé.
     * @return Une liste de ses créneaux de travail.
     */
    @Override
    public List<Planning> trouverPlanningsParIdPersonnel(int idPersonnel) {
        List<Planning> planningsTrouves = new ArrayList<>();
        for (Planning planning : this.data) {
            if (planning.getIdPersonnel() == idPersonnel) {
                planningsTrouves.add(planning);
            }
        }
        return planningsTrouves;
    }

    /**
     * Retourne la totalité des créneaux de planning enregistrés.
     *
     * @return Une copie de la liste de tous les plannings.
     */
    @Override
    public List<Planning> trouverTousLesPlannings() {
        return new ArrayList<>(this.data);
    }
}
