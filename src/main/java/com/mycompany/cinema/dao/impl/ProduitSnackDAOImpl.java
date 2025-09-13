// ========================================================================
// ProduitSnackDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.ProduitSnack;
import com.mycompany.cinema.dao.ProduitSnackDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation concrète pour la gestion du catalogue de snacks. Interagit
 * avec le fichier "produits_snack.dat".
 *
 */
public class ProduitSnackDAOImpl extends GenericDAOImpl<ProduitSnack> implements ProduitSnackDAO {

    public ProduitSnackDAOImpl() {
        super("produits_snack.dat");
    }

     /**
     * Ajoute un nouveau produit de snack.
     * @param produit L'objet ProduitSnack à ajouter.
     */
    @Override
    public void ajouterProduit(ProduitSnack produit) {
        this.data.add(produit);
        sauvegarderDansFichier();
    }

    /**
     * Recherche un produit de snack par son identifiant.
     * @param id L'identifiant du produit.
     * @return L'objet ProduitSnack, ou `null` si non trouvé.
     */
    @Override
    public ProduitSnack trouverProduitParId(int id) {
        for (ProduitSnack produit : this.data) {
            if (produit.getId() == id) {
                return produit;
            }
        }
        return null;
    }

    /**
     * Retourne la liste de tous les produits de snack disponibles.
     * @return Une copie de la liste des produits.
     */
    @Override
    public List<ProduitSnack> trouverTousLesProduits() {
        return new ArrayList<>(this.data);
    }

    /**
     * Met à jour les informations d'un produit de snack (typiquement le stock).
     * @param produitMisAJour L'objet ProduitSnack avec les nouvelles données.
     */
    @Override
    public void mettreAJourProduit(ProduitSnack produitMisAJour) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == produitMisAJour.getId()) {
                this.data.set(i, produitMisAJour);
                sauvegarderDansFichier();
                return;
            }
        }
    }

    /**
     * Supprime un produit de snack du catalogue à partir de son identifiant.
     * @param id L'identifiant du produit à supprimer.
     */
    @Override
    public void supprimerProduitParId(int id) {
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
