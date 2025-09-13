// ========================================================================
// FICHIER : ClientDAOImpl.java
// ========================================================================package com.mycompany.cinema.dao.impl;
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Caisse;
import com.mycompany.cinema.dao.CaisseDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation pour la gestion de la persistance des objets Caisse.
 * Elle interagit avec le fichier "caisses.dat".
 *
 */
public class CaisseDAOImpl extends GenericDAOImpl<Caisse> implements CaisseDAO {

    public CaisseDAOImpl() {
        super("caisses.dat");
    }

    /**
     * Ajoute une nouvelle caisse au système.
     *
     * @param caisse L'objet Caisse à enregistrer.
     */
    @Override
    public void ajouterCaisse(Caisse caisse) {
        this.data.add(caisse);
        sauvegarderDansFichier();
    }

    /**
     * Recherche une caisse par son identifiant.
     *
     * @param id L'identifiant unique de la caisse à trouver.
     * @return L'objet Caisse correspondant, ou `null` si aucune caisse n'a cet
     * ID.
     */
    @Override
    public Caisse trouverCaisseParId(int id) {
        for (Caisse caisse : this.data) {
            if (caisse.getId() == id) {
                return caisse;
            }
        }
        return null;
    }

    /**
     * Retourne la liste de toutes les caisses enregistrées.
     *
     * @return Une copie de la liste des caisses
     */
    @Override
    public List<Caisse> trouverToutesLesCaisses() {
        return new ArrayList<>(this.data);
    }

    /**
     * Met à jour les informations d'une caisse existante.
     *
     * @param caisseMiseAJour L'objet Caisse contenant les nouvelles
     * informations. L'ID doit correspondre à celui de la caisse à modifier.
     */
    @Override
    public void mettreAJourCaisse(Caisse caisseMiseAJour) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == caisseMiseAJour.getId()) {
                this.data.set(i, caisseMiseAJour);
                sauvegarderDansFichier();
                return;
            }
        }
    }

    /**
     * Supprime une caisse du système à partir de son identifiant.
     *
     * @param id L'identifiant de la caisse à supprimer.
     */
    @Override
    public void supprimerCaisseParId(int id) {
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

