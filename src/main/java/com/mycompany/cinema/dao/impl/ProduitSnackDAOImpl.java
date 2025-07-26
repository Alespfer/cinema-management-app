package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.ProduitSnack;
import com.mycompany.cinema.dao.ProduitSnackDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProduitSnackDAOImpl extends GenericDAOImpl<ProduitSnack> implements ProduitSnackDAO {

    public ProduitSnackDAOImpl() {
        super("produits_snack.dat");
    }

    @Override
    public void addProduit(ProduitSnack produit) {
        this.data.add(produit);
        saveToFile();
    }

    @Override
    public Optional<ProduitSnack> getProduitById(int id) {
        return this.data.stream().filter(p -> p.getId() == id).findFirst();
    }

    @Override
    public List<ProduitSnack> getAllProduits() {
        return new ArrayList<>(this.data);
    }

    @Override
    public void updateProduit(ProduitSnack updatedProduit) {
        getProduitById(updatedProduit.getId()).ifPresent(p -> {
            int index = this.data.indexOf(p);
            this.data.set(index, updatedProduit);
            saveToFile();
        });
    }
}