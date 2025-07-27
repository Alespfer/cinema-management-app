package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.AffectationSeance;
import com.mycompany.cinema.dao.AffectationSeanceDAO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Cette classe gère les opérations sur les affectations de personnel aux séances.
// Elle hérite du comportement générique de lecture/écriture de fichiers.
public class AffectationSeanceDAOImpl extends GenericDAOImpl<AffectationSeance> implements AffectationSeanceDAO {

    // Constructeur : précise le fichier dans lequel les données sont sauvegardées
    public AffectationSeanceDAOImpl() {
        super("affectations_seances.dat");
    }

    @Override
    public void addAffectation(AffectationSeance affectation) {
        // Ajout direct dans la liste interne
        this.data.add(affectation);
        saveToFile(); // Sauvegarde après modification
    }

    @Override
    public List<AffectationSeance> getAffectationsBySeanceId(int seanceId) {
        // On prépare une nouvelle liste vide
        List<AffectationSeance> resultat = new ArrayList<>();
        // Parcours de toutes les affectations
        for (AffectationSeance aff : this.data) {
            // On vérifie l'identifiant de séance
            if (aff.getIdSeance() == seanceId) {
                resultat.add(aff);
            }
        }
        return resultat;
    }

    @Override
    public List<AffectationSeance> getAffectationsByPersonnelId(int personnelId) {
        List<AffectationSeance> resultat = new ArrayList<>();
        // Même logique que la méthode précédente, mais on filtre sur l'identifiant du personnel
        for (AffectationSeance aff : this.data) {
            if (aff.getIdPersonnel() == personnelId) {
                resultat.add(aff);
            }
        }
        return resultat;
    }

    @Override
    public void deleteAffectation(int seanceId, int personnelId) {
        // Utilisation d'un itérateur pour suppression sécurisée pendant l'itération
        Iterator<AffectationSeance> iterator = this.data.iterator();
        boolean changed = false;
        while (iterator.hasNext()) {
            AffectationSeance aff = iterator.next();
            // Suppression si les deux identifiants correspondent
            if (aff.getIdSeance() == seanceId && aff.getIdPersonnel() == personnelId) {
                iterator.remove(); // Ne jamais faire this.data.remove(...) en boucle
                changed = true;
                break; // Une seule affectation à supprimer
            }
        }
        // Sauvegarde uniquement si une modification a eu lieu
        if (changed) {
            saveToFile();
        }
    }
}
