package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Planning;
import com.mycompany.cinema.dao.PlanningDAO;
import java.util.ArrayList;
import java.util.List;

public class PlanningDAOImpl extends GenericDAOImpl<Planning> implements PlanningDAO {

    public PlanningDAOImpl() {
        super("plannings.dat");
    }

    @Override
    public void addPlanning(Planning planning) {
        this.data.add(planning);
        saveToFile();
    }

    /**
     * Récupère tous les créneaux de planning pour un membre du personnel donné.
     * @param personnelId L'ID du membre du personnel.
     * @return Une liste de ses plannings.
     */
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

    @Override
    public List<Planning> getAllPlannings() {
        return new ArrayList<>(this.data);
    }
}