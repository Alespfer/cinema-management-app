package com.mycompany.cinema.dao;

import com.mycompany.cinema.AffectationSeance;
import java.util.List;

/**
 * Contrat pour la gestion des affectations du personnel aux séances.
 */
public interface AffectationSeanceDAO {
    void addAffectation(AffectationSeance affectation);
    List<AffectationSeance> getAffectationsBySeanceId(int seanceId);
    List<AffectationSeance> getAffectationsByPersonnelId(int personnelId);
    void deleteAffectation(int seanceId, int personnelId);
    
    void rechargerDonnees();

}