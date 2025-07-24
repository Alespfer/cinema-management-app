package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.AffectationSeance;
import com.mycompany.cinema.dao.AffectationSeanceDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AffectationSeanceDAOImpl extends GenericDAOImpl<AffectationSeance> implements AffectationSeanceDAO {

    public AffectationSeanceDAOImpl() {
        super("affectations_seances.dat");
    }

    @Override
    public void addAffectation(AffectationSeance affectation) {
        this.data.add(affectation);
        saveToFile();
    }

    @Override
    public List<AffectationSeance> getAffectationsBySeanceId(int seanceId) {
        // Utilisation d'un stream ici car c'est la façon la plus lisible de filtrer une liste.
        return this.data.stream()
                .filter(aff -> aff.getIdSeance() == seanceId)
                .collect(Collectors.toList());
    }

    @Override
    public List<AffectationSeance> getAffectationsByPersonnelId(int personnelId) {
        return this.data.stream()
                .filter(aff -> aff.getIdPersonnel() == personnelId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAffectation(int seanceId, int personnelId) {
        // La clé de cette entité est composite (seanceId + personnelId).
        if (this.data.removeIf(aff -> aff.getIdSeance() == seanceId && aff.getIdPersonnel() == personnelId)) {
            saveToFile();
        }
    }
}