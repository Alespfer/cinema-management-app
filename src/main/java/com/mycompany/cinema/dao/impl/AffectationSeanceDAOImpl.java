// ========================================================================
// AffectationSeanceDAOImpl.java
// ========================================================================


package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.AffectationSeance;
import com.mycompany.cinema.dao.AffectationSeanceDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de l'interface AffectationSeanceDAO.
 *
 */
public class AffectationSeanceDAOImpl extends GenericDAOImpl<AffectationSeance> implements AffectationSeanceDAO {

    public AffectationSeanceDAOImpl() {
        super("affectations_seances.dat");
    }

    
    /**
     * Ajoute une nouvelle affectation à la source de données.
     * @param affectation L'objet AffectationSeance à ajouter, contenant les identifiants
     *                    de la séance et du membre du personnel.
     */
    public void ajouterAffectation(AffectationSeance affectation) {
        this.data.add(affectation);
        sauvegarderDansFichier();
    }

    /**
     * Recherche et retourne toutes les affectations liées à un identifiant de séance spécifique.
     *
     * @param idSeance L'identifiant numérique de la séance pour laquelle on recherche les affectations.
     * @return Une List<AffectationSeance> contenant toutes les correspondances.
     *         Retourne une liste vide si aucune affectation n'est trouvée pour cette séance.
     */
    @Override
    public List<AffectationSeance> trouverAffectationsParIdSeance(int idSeance) {
        List<AffectationSeance> affectationsTrouvees = new ArrayList<>();
        // Parcours exhaustif de la liste en mémoire.
        for (AffectationSeance affectation : this.data) {
            // Comparaison de l'identifiant pour filtrer les résultats.
            if (affectation.getIdSeance() == idSeance) {
                affectationsTrouvees.add(affectation);
            }
        }
        return affectationsTrouvees;
    }

    /**
     * Recherche et retourne toutes les affectations d'un membre du personnel donné.
     *
     * @param idPersonnel L'identifiant numérique du membre du personnel.
     * @return Une List<AffectationSeance> contenant toutes les séances assignées à cet employé.
     *         Retourne une liste vide si l'employé n'a aucune affectation.
     */
    @Override
    public List<AffectationSeance> trouverAffectationsParIdPersonnel(int idPersonnel) {
        List<AffectationSeance> affectationsTrouvees = new ArrayList<>();
        for (AffectationSeance affectation : this.data) {
            if (affectation.getIdPersonnel() == idPersonnel) {
                affectationsTrouvees.add(affectation);
            }
        }
        return affectationsTrouvees;
    }

    /**
     * Supprime une affectation spécifique, identifiée par la combinaison
     * d'un identifiant de séance et d'un identifiant de personnel.
     *
     * @param idSeance L'identifiant de la séance.
     * @param idPersonnel L'identifiant du membre du personnel.
     */
    @Override
    public void supprimerAffectation(int idSeance, int idPersonnel) {
        int indexASupprimer = -1; 

        for (int i = 0; i < this.data.size(); i++) {
            AffectationSeance affectationCourante = this.data.get(i);
            if (affectationCourante.getIdSeance() == idSeance && affectationCourante.getIdPersonnel() == idPersonnel) {
                indexASupprimer = i; 
                break; 
            }
        }

        if (indexASupprimer != -1) {
            this.data.remove(indexASupprimer); 
            sauvegarderDansFichier(); 
        }
    }

    
}