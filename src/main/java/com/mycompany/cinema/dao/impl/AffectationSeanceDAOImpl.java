package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.AffectationSeance;
import com.mycompany.cinema.dao.AffectationSeanceDAO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AffectationSeanceDAOImpl extends GenericDAOImpl<AffectationSeance> implements AffectationSeanceDAO {

    public AffectationSeanceDAOImpl() { super("affectations_seances.dat"); }

    @Override
    public void addAffectation(AffectationSeance affectation) {
        this.data.add(affectation);
        saveToFile();
    }

    @Override
    public List<AffectationSeance> getAffectationsBySeanceId(int seanceId) {
        List<AffectationSeance> resultat = new ArrayList<>();
        for (AffectationSeance aff : this.data) {
            if (aff.getIdSeance() == seanceId) {
                resultat.add(aff);
            }
        }
        return resultat;
    }

    @Override
    public List<AffectationSeance> getAffectationsByPersonnelId(int personnelId) {
        List<AffectationSeance> resultat = new ArrayList<>();
        for (AffectationSeance aff : this.data) {
            if (aff.getIdPersonnel() == personnelId) {
                resultat.add(aff);
            }
        }
        return resultat;
    }

    @Override
    public void deleteAffectation(int seanceId, int personnelId) {
        Iterator<AffectationSeance> iterator = this.data.iterator();
        boolean changed = false;
        while (iterator.hasNext()) {
            AffectationSeance aff = iterator.next();
            if (aff.getIdSeance() == seanceId && aff.getIdPersonnel() == personnelId) {
                iterator.remove(); // Suppression sécurisée
                changed = true;
                break; // Une seule affectation possible, on peut arrêter
            }
        }
        if (changed) {
            saveToFile();
        }
    }
}