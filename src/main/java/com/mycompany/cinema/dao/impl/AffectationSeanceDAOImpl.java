// ========================================================================
// FICHIER : AffectationSeanceDAOImpl.java
// ========================================================================


package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.AffectationSeance;
import com.mycompany.cinema.dao.AffectationSeanceDAO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implémentation de l'interface AffectationSeanceDAO.
 * Cette classe assure la gestion de la persistance (lecture et écriture) des objets
 * AffectationSeance dans le fichier "affectations_seances.dat". Elle représente la couche
 * la plus basse de l'application, responsable de l'interaction directe avec la source de données.
 *
 * L'interface graphique n'interagira jamais directement avec cette classe, passant systématiquement 
 * par la couche de service (AdminService) qui orchestre la logique métier.
 */
public class AffectationSeanceDAOImpl extends GenericDAOImpl<AffectationSeance> implements AffectationSeanceDAO {

    // Constructeur. Il indique à la classe parente le nom du fichier à utiliser pour la persistance.
    public AffectationSeanceDAOImpl() {
        super("affectations_seances.dat");
    }

    
    /**
     * Ajoute une nouvelle affectation à la source de données.
     * 
     * L'opération se déroule en deux temps : d'abord une modification de la liste
     * en mémoire, puis la sauvegarde de cette liste mise à jour dans le fichier.
     *
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
        // La logique est identique à la recherche par séance, mais le critère de filtre change.
        for (AffectationSeance affectation : this.data) {
            if (affectation.getIdPersonnel() == idPersonnel) {
                affectationsTrouvees.add(affectation);
            }
        }
        return affectationsTrouvees;
    }

    /**
     * Supprime une affectation spécifique, identifiée par la combinaison unique
     * d'un identifiant de séance et d'un identifiant de personnel.
     * La méthode utilise une boucle 'for' classique pour identifier l'index de l'élément
     * à supprimer, puis effectue la suppression en dehors de la boucle pour garantir la sécurité.
     *
     * @param idSeance L'identifiant de la séance.
     * @param idPersonnel L'identifiant du membre du personnel.
     */
    @Override
    public void supprimerAffectation(int idSeance, int idPersonnel) {
        int indexASupprimer = -1; // Initialisation à une valeur invalide.

        // Étape 1 : Parcourir la liste pour trouver l'index de l'affectation à supprimer.
        for (int i = 0; i < this.data.size(); i++) {
            AffectationSeance affectationCourante = this.data.get(i);
            // Si on trouve la correspondance exacte (même séance ET même employé), on stocke son index pour suppression.
            if (affectationCourante.getIdSeance() == idSeance && affectationCourante.getIdPersonnel() == idPersonnel) {
                indexASupprimer = i; 
                break; 
            }
        }

        // Étape 2 : Si un index a été trouvé, on peut procéder à la suppression.
        if (indexASupprimer != -1) {
            this.data.remove(indexASupprimer); 
            sauvegarderDansFichier(); // On sauvegarde uniquement si une modification a eu lieu.
        }
    }

   

    
}