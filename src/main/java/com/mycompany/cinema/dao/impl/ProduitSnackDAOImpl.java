package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.ProduitSnack;
import com.mycompany.cinema.dao.ProduitSnackDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DAO pour la gestion des produits disponibles au snack.
public class ProduitSnackDAOImpl extends GenericDAOImpl<ProduitSnack> implements ProduitSnackDAO {

    // Initialise le DAO avec le fichier des produits snack.
    public ProduitSnackDAOImpl() {
        super("produits_snack.dat");
    }

    // Ajoute un produit à la liste.
    @Override
    public void addProduit(ProduitSnack produit) {
        this.data.add(produit);
        saveToFile();
    }

    // Recherche un produit par son identifiant.
    @Override
    public Optional<ProduitSnack> getProduitById(int id) {
        for (ProduitSnack p : this.data) {
            if (p.getId() == id) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    // Retourne tous les produits enregistrés.
    @Override
    public List<ProduitSnack> getAllProduits() {
        return new ArrayList<>(this.data);
    }

    // Met à jour les informations d’un produit existant.
    @Override
    public void updateProduit(ProduitSnack updatedProduit) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == updatedProduit.getId()) {
                this.data.set(i, updatedProduit);
                saveToFile();
                return;
            }
        }
    }
}
