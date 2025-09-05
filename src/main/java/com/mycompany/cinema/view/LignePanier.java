package com.mycompany.cinema.view;

import com.mycompany.cinema.ProduitSnack;

/**
 * Classe de support simple pour représenter une ligne du panier de snacks.
 * Contient un produit et la quantité choisie. Cette structure remplace 
 * l'utilisation d'une Map pour rester conforme à la Doctrine.
 */
public class LignePanier {
    public ProduitSnack produit;
    public int quantite;

    public LignePanier(ProduitSnack p, int q) {
        this.produit = p;
        this.quantite = q;
    }
}