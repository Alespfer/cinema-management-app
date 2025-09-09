package com.mycompany.cinema;

import com.mycompany.cinema.ProduitSnack;

/**
 * Représente une ligne du panier de snacks. Associe un produit à une quantité
 * sélectionnée par le client.
 */
public class LignePanier {

    private ProduitSnack produit;
    private int quantite;

    public LignePanier(ProduitSnack p, int q) {
        this.produit = p;
        this.quantite = q;
    }
    
    
    // --- Getters / Setters ---


    public ProduitSnack getProduit() {
        return produit;
    }

    public void setProduit(ProduitSnack produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        if (quantite >= 0) {
            this.quantite = quantite;
        }
    }
}
