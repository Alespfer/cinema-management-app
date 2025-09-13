// ========================================================================
// SiegeDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Siege;
import com.mycompany.cinema.dao.SiegeDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation pour la gestion des sièges.
 * Interagit avec le fichier "sieges.dat".
 */
public class SiegeDAOImpl extends GenericDAOImpl<Siege> implements SiegeDAO {

    public SiegeDAOImpl() {
        super("sieges.dat");
    }

    /**
     * Ajoute un nouveau siège à la source de données.
     * Principalement utilisé lors de la création initiale d'une salle et de son plan.
     * @param siege L'objet Siege à enregistrer.
     */
    @Override
    public void ajouterSiege(Siege siege) {
        this.data.add(siege);
        sauvegarderDansFichier();
    }

    /**
     * Recherche et retourne tous les sièges appartenant à une salle spécifique.
     * @param idSalle L'identifiant de la salle.
     * @return Une liste des sièges de cette salle.
     */
    @Override
    public List<Siege> trouverSiegesParIdSalle(int idSalle) {
        List<Siege> siegesTrouves = new ArrayList<>();
        for (Siege siege : this.data) {
            if (siege.getIdSalle() == idSalle) {
                siegesTrouves.add(siege);
            }
        }
        return siegesTrouves;
    }

    /**
     * Retourne la liste de tous les sièges de tout le cinéma.
     * @return Une copie de la liste de tous les sièges.
     */
    @Override
    public List<Siege> trouverTousLesSieges() {
        return new ArrayList<>(this.data);
    }
}