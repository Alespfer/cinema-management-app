package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Planning;
import com.mycompany.cinema.dao.PlanningDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation concrète pour la gestion des plannings de travail du personnel.
 * S'occupe de la lecture et de l'écriture dans le fichier "plannings.dat".
 * 
 * Pour le développeur de l'interface graphique : cette classe fournit les données
 * pour le panneau d'administration qui gérera les horaires des employés.
 * Quand l'administrateur voudra voir l'emploi du temps de "Jean Dupont", c'est la
 * méthode `getPlanningsByPersonnelId` qui sera utilisée en arrière-plan pour
 * récupérer et afficher ses créneaux de travail.
 */
public class PlanningDAOImpl extends GenericDAOImpl<Planning> implements PlanningDAO {

    public PlanningDAOImpl() {
        super("plannings.dat");
    }

    @Override
    public void addPlanning(Planning planning) {
        this.data.add(planning);
        saveToFile();
    }

    @Override
    public List<Planning> getPlanningsByPersonnelId(int personnelId) {
        List<Planning> planningsDuPersonnel = new ArrayList<>();
        // On parcourt tous les créneaux de planning...
        for (Planning planning : this.data) {
            // ...et on retourne uniquement ceux qui appartiennent à l'employé demandé.
            if (planning.getIdPersonnel() == personnelId) {
                planningsDuPersonnel.add(planning);
            }
        }
        return planningsDuPersonnel;
    }

    @Override
    public List<Planning> getAllPlannings() {
        return new ArrayList<>(this.data);
    }
}