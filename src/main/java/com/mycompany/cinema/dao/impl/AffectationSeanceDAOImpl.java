package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.AffectationSeance;
import com.mycompany.cinema.dao.AffectationSeanceDAO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implémentation concrète pour gérer la sauvegarde et la lecture des affectations du personnel.
 * Cette classe s'occupe de lire et d'écrire dans le fichier "affectations_seances.dat".
 * 
 * Pour le développeur de l'interface graphique : vous n'interagirez JAMAIS directement avec cette classe.
 * Vos panneaux (ex: gestion des séances) appelleront des méthodes du 'AdminService', qui lui,
 * se chargera de faire appel à cette classe pour manipuler les données.
 */
public class AffectationSeanceDAOImpl extends GenericDAOImpl<AffectationSeance> implements AffectationSeanceDAO {

    /**
     * Constructeur. Il indique à la classe parente 'GenericDAOImpl'
     * le nom du fichier à utiliser pour la persistance.
     */
    public AffectationSeanceDAOImpl() {
        super("affectations_seances.dat");
    }

    @Override
    public void addAffectation(AffectationSeance affectation) {
        // Ajoute l'objet 'affectation' à la liste en mémoire...
        this.data.add(affectation);
        // ...puis sauvegarde immédiatement la liste complète dans le fichier.
        saveToFile();
    }

    @Override
    public List<AffectationSeance> getAffectationsBySeanceId(int seanceId) {
        List<AffectationSeance> resultat = new ArrayList<>();
        // On parcourt toutes les affectations en mémoire...
        for (AffectationSeance aff : this.data) {
            // ...et on ajoute à notre liste de résultats uniquement celles qui correspondent à l'ID de la séance recherchée.
            if (aff.getIdSeance() == seanceId) {
                resultat.add(aff);
            }
        }
        return resultat;
    }

    @Override
    public List<AffectationSeance> getAffectationsByPersonnelId(int personnelId) {
        List<AffectationSeance> resultat = new ArrayList<>();
        // Même logique que la méthode précédente, mais en filtrant par l'ID de l'employé.
        for (AffectationSeance aff : this.data) {
            if (aff.getIdPersonnel() == personnelId) {
                resultat.add(aff);
            }
        }
        return resultat;
    }

    @Override
    public void deleteAffectation(int seanceId, int personnelId) {
        // On utilise un 'Iterator' pour parcourir la liste. C'est la seule façon
        // sûre de supprimer un élément d'une collection pendant qu'on la parcourt.
        Iterator<AffectationSeance> iterator = this.data.iterator();
        boolean changed = false;
        while (iterator.hasNext()) {
            AffectationSeance aff = iterator.next();
            // Si on trouve la correspondance exacte (même séance ET même employé)...
            if (aff.getIdSeance() == seanceId && aff.getIdPersonnel() == personnelId) {
                iterator.remove(); // ...on la supprime de la liste en mémoire.
                changed = true;
                break; // On sort de la boucle, car le lien est unique.
            }
        }
        // Pour optimiser, on ne réécrit le fichier que si une suppression a vraiment eu lieu.
        if (changed) {
            saveToFile();
        }
    }
}