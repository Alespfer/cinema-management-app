// Fichier : src/main/java/com/mycompany/cinema/dao/ProduitSnackDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.ProduitSnack;
import java.util.List;
import java.util.Optional;

/**
 * CONTRAT pour la gestion de la persistance des Produits Snack.
 * Ceci est une INTERFACE. Elle ne fait que définir les méthodes obligatoires.
 */
public interface ProduitSnackDAO {

    /**
     * Ajoute un nouveau produit à la source de données.
     */
    void addProduit(ProduitSnack produit);

    /**
     * Récupère un produit par son identifiant.
     * @return un Optional contenant le produit s'il est trouvé, sinon un Optional vide.
     */
    Optional<ProduitSnack> getProduitById(int id);

    /**
     * Récupère la liste de tous les produits.
     */
    List<ProduitSnack> getAllProduits();

    /**
     * Met à jour les informations d'un produit existant (notamment le stock).
     */
    void updateProduit(ProduitSnack produit);
}