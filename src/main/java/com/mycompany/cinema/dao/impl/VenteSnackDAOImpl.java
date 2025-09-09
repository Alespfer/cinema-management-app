// ========================================================================
// FICHIER : VenteSnackDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.VenteSnack;
import com.mycompany.cinema.dao.VenteSnackDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation concrète pour la gestion des tickets de caisse de snacks.
 * Interagit avec le fichier "ventes_snack.dat".
 *
 * Cette classe est principalement utilisée pour récupérer l'historique des ventes 
 * et calculer les chiffres d'affaires.
 */
public class VenteSnackDAOImpl extends GenericDAOImpl<VenteSnack> implements VenteSnackDAO {

    public VenteSnackDAOImpl() {
        super("ventes_snack.dat");
    }

    /**
     * Ajoute une nouvelle vente de snack.
     * @param vente L'objet VenteSnack à enregistrer.
     */
    @Override
    public void ajouterVenteSnack(VenteSnack vente) {
        this.data.add(vente);
        sauvegarderDansFichier();
    }

    /**
     * Recherche une vente de snack par son identifiant.
     * @param id L'identifiant de la vente.
     * @return L'objet VenteSnack, ou `null` si non trouvé.
     */
    @Override
    public VenteSnack trouverVenteSnackParId(int id) {
        for (VenteSnack vente : this.data) {
            if (vente.getIdVente() == id) {
                return vente;
            }
        }
        return null;
    }


    /**
     * Recherche la vente de snack associée à une réservation de billets spécifique.
     * @param idReservation L'identifiant de la réservation.
     * @return L'objet VenteSnack correspondant, ou `null` si aucune vente n'est liée.
     */
    @Override
    public VenteSnack trouverVenteParIdReservation(int idReservation) {
        for (VenteSnack vente : this.data) {
            if (vente.getIdReservation() != null && vente.getIdReservation() == idReservation) {
                return vente; 
            }
        }
        return null;
    }

     /**
     * Retourne la liste de toutes les ventes de snacks.
     * @return Une copie de la liste de toutes les ventes.
     */
    @Override
    public List<VenteSnack> trouverToutesLesVentesSnack() {
        return new ArrayList<>(this.data);
    }

    /**
     * Met à jour les informations d'une vente de snack.
     * Utile par exemple pour lier une vente de snack à une réservation après coup.
     * @param venteMiseAJour L'objet VenteSnack avec les nouvelles données.
     */
    @Override
    public void mettreAJourVenteSnack(VenteSnack venteMiseAJour) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getIdVente() == venteMiseAJour.getIdVente()) {
                this.data.set(i, venteMiseAJour);
                sauvegarderDansFichier();
                return;
            }
        }
    }

    /**
     * Recherche toutes les ventes de snacks pour un jour donné.
     * @param date La date pour laquelle rechercher les ventes.
     * @return Une liste des ventes pour cette date.
     */
    @Override
    public List<VenteSnack> trouverVentesParDate(LocalDate date) {
        List<VenteSnack> ventesTrouvees = new ArrayList<>();
        for (VenteSnack vente : this.data) {
            if (vente.getDateVente().toLocalDate().isEqual(date)) {
                ventesTrouvees.add(vente);
            }
        }
        return ventesTrouvees;
    }
}
