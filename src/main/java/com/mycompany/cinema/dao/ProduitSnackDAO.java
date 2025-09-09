// ========================================================================
// FICHIER : ProduitSnackDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.ProduitSnack;
import java.util.List;

/**
 * Définit le contrat pour la gestion du catalogue des produits de snacking.
 */
public interface ProduitSnackDAO {
    
    void ajouterProduit(ProduitSnack produit);
    ProduitSnack trouverProduitParId(int id);
    List<ProduitSnack> trouverTousLesProduits();
    void mettreAJourProduit(ProduitSnack produit);
    void supprimerProduitParId(int id);
    void rechargerDonnees();
}