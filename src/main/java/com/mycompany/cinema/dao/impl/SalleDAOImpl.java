// ========================================================================
// SalleDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Salle;
import com.mycompany.cinema.dao.SalleDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation pour la gestion de la persistance des objets Salle.
 * Interagit avec le fichier "salles.dat".
 *
 */
public class SalleDAOImpl extends GenericDAOImpl<Salle> implements SalleDAO {

    public SalleDAOImpl() {
        super("salles.dat");
    }
    
    
    /**
     * Ajoute une nouvelle salle au système.
     * @param salle L'objet Salle à enregistrer.
     */
    @Override
    public void ajouterSalle(Salle salle) {
        this.data.add(salle);
        sauvegarderDansFichier();
    }

    /**
     * Recherche une salle par son identifiant.
     * @param id L'identifiant de la salle à trouver.
     * @return L'objet Salle correspondant, ou `null` si non trouvé.
     */
    @Override
    public Salle trouverSalleParId(int id) {
        for (Salle salle : this.data) {
            if (salle.getId() == id) {
                return salle;
            }
        }
        return null;
    }

    /**
     * Retourne la liste de toutes les salles enregistrées.
     * @return Une copie de la liste des salles.
     */
    @Override
    public List<Salle> trouverToutesLesSalles() {
        return new ArrayList<>(this.data);
    }

    /**
     * Supprime une salle de la source de données à partir de son identifiant.
     * @param id L'identifiant de la salle à supprimer.
     */
    @Override
    public void supprimerSalleParId(int id) {
        int indexASupprimer = -1;
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == id) {
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
