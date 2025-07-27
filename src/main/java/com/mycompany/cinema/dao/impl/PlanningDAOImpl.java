package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Planning;
import com.mycompany.cinema.dao.PlanningDAO;
import java.util.ArrayList;
import java.util.List;

// DAO pour la gestion des plannings du personnel.
public class PlanningDAOImpl extends GenericDAOImpl<Planning> implements PlanningDAO {

    // Initialise le DAO avec le fichier associé aux plannings.
    public PlanningDAOImpl() {
        super("plannings.dat");
    }

    // Ajoute un nouveau planning à la liste.
    @Override
    public void addPlanning(Planning planning) {
        this.data.add(planning);
        saveToFile();
    }

    // Retourne les plannings associés à un identifiant de personnel donné.
    @Override
    public List<Planning> getPlanningsByPersonnelId(int personnelId) {
        List<Planning> planningsDuPersonnel = new ArrayList<>();
        for (Planning planning : this.data) {
            if (planning.getIdPersonnel() == personnelId) {
                planningsDuPersonnel.add(planning);
            }
        }
        return planningsDuPersonnel;
    }

    // Retourne la liste complète des plannings enregistrés.
    @Override
    public List<Planning> getAllPlannings() {
        return new ArrayList<>(this.data);
    }
}
