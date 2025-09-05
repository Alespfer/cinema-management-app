// Fichier : ProduitSnackDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.ProduitSnack;
import java.util.List;
import java.util.Optional;

/**
 * Définit le contrat pour la gestion du catalogue des produits de snacking.
 * 
 * Ce contrat est utilisé à la fois par l'interface client et l'interface admin :
 * - `getAllProduits` est appelé par `SnackSelectionPanel` pour afficher la liste des
 *   articles que le client peut acheter.
 * - Toutes les méthodes CRUD (add, get, getAll, update) sont utilisées par le
 *   panneau `GestionProduitsSnackPanel` de l'administrateur.
 */
public interface ProduitSnackDAO {
    void addProduit(ProduitSnack produit);
    ProduitSnack getProduitById(int id);
    List<ProduitSnack> getAllProduits();
    void updateProduit(ProduitSnack produit);
    /**
     * Supprime un produit de la source de données à partir de son identifiant.
     */
    void deleteProduit(int id);
    void rechargerDonnees();
}