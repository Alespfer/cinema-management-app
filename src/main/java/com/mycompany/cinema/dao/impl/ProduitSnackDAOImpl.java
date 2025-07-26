package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.ProduitSnack;
import com.mycompany.cinema.dao.ProduitSnackDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProduitSnackDAOImpl extends GenericDAOImpl<ProduitSnack> implements ProduitSnackDAO {

    public ProduitSnackDAOImpl() { super("produits_snack.dat"); }

    @Override
    public void addProduit(ProduitSnack produit) {
        this.data.add(produit);
        saveToFile();
    }

    @Override
    public Optional<ProduitSnack> getProduitById(int id) {
        for (ProduitSnack p : this.data) {
            if (p.getId() == id) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ProduitSnack> getAllProduits() {
        return new ArrayList<>(this.data);
    }

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