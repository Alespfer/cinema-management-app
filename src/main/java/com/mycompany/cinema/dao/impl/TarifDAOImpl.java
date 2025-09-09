// ========================================================================
// FICHIER : TarifDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Tarif;
import com.mycompany.cinema.dao.TarifDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation concrète pour la gestion des tarifs.
 * Interagit avec le fichier "tarifs.dat".
 *
 * Fournit les données pour le menu déroulant de sélection du tarif dans l'interface
 * de réservation, ainsi que pour le panneau de gestion des tarifs de l'administrateur.
 */
public class TarifDAOImpl extends GenericDAOImpl<Tarif> implements TarifDAO {

    public TarifDAOImpl() {
        super("tarifs.dat");
    }

    /**
     * Ajoute un nouveau tarif.
     * @param tarif Le tarif à enregistrer.
     */
    @Override
    public void ajouterTarif(Tarif tarif) {
        this.data.add(tarif);
        sauvegarderDansFichier();
    }

    /**
     * Recherche un tarif par son identifiant unique.
     * @param id L'identifiant du tarif.
     * @return L'objet Tarif correspondant, ou `null` si non trouvé.
     */
    @Override
    public Tarif trouverTarifParId(int id) {
        for (Tarif tarif : this.data) {
            if (tarif.getId() == id) {
                return tarif;
            }
        }
        return null;
    }

    /**
     * Retourne la liste de tous les tarifs disponibles.
     * @return Une copie de la liste des tarifs.
     */
    @Override
    public List<Tarif> trouverTousLesTarifs() {
        return new ArrayList<>(this.data);
    }

    /**
     * Met à jour les informations d'un tarif.
     * @param tarifMisAJour L'objet Tarif avec les nouvelles données.
     */
    @Override
    public void mettreAJourTarif(Tarif tarifMisAJour) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == tarifMisAJour.getId()) {
                this.data.set(i, tarifMisAJour);
                sauvegarderDansFichier();
                return;
            }
        }
    }

    /**
     * Supprime un tarif à partir de son identifiant.
     * @param id L'identifiant du tarif à supprimer.
     */
    @Override
    public void supprimerTarifParId(int id) {
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