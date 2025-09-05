package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.ProduitSnack;
import com.mycompany.cinema.dao.ProduitSnackDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation concrète pour la gestion du catalogue de snacks dans
 * "produits_snack.dat".
 *
 * Pour le développeur de l'interface graphique : cette classe est fondamentale
 * pour toute la partie "vente de snacks". - Le `SnackSelectionPanel` (côté
 * client) utilise `getAllProduits` pour afficher la liste des articles
 * disponibles à l'achat. - Le `GestionProduitsSnackPanel` (côté admin) utilise
 * toutes les méthodes de cette classe (add, get, update) pour permettre à
 * l'administrateur de gérer le catalogue et les stocks.
 */
public class ProduitSnackDAOImpl extends GenericDAOImpl<ProduitSnack> implements ProduitSnackDAO {

    public ProduitSnackDAOImpl() {
        super("produits_snack.dat");
    }

    @Override
    public void addProduit(ProduitSnack produit) {
        this.data.add(produit);
        saveToFile();
    }

    // Dans ProduitSnackDAOImpl.java
    @Override
    public ProduitSnack getProduitById(int id) {
        for (ProduitSnack p : this.data) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<ProduitSnack> getAllProduits() {
        return new ArrayList<>(this.data);
    }

    @Override
    public void updateProduit(ProduitSnack updatedProduit) {
        // Cherche le produit par son ID et le remplace par la version mise à jour.
        // Essentiel pour la mise à jour des stocks après une vente.
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == updatedProduit.getId()) {
                this.data.set(i, updatedProduit);
                saveToFile();
                return;
            }
        }
    }

    /**
     * Supprime un produit de la liste en mémoire et sauvegarde la modification
     * dans le fichier.
     */
    @Override
    public void deleteProduit(int id) {
        boolean changed = false;
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == id) {
                this.data.remove(i);
                changed = true;
                break; // Le produit est unique, on peut arrêter la boucle.
            }
        }
        if (changed) {
            saveToFile();
        }
    }
}
