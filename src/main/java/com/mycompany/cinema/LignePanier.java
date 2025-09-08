package com.mycompany.cinema;

import com.mycompany.cinema.ProduitSnack;

/**
 * Classe de support simple pour représenter une ligne du panier de snacks.
 * Contient un produit et la quantité choisie. Cette structure remplace 
 * l'utilisation d'une Map pour rester conforme à la Doctrine.
 */
public class LignePanier {
    // Les attributs sont maintenant privés, protégeant l'état interne de l'objet.
    private ProduitSnack produit;
    private int quantite;

    public LignePanier(ProduitSnack p, int q) {
        this.produit = p;
        this.quantite = q;
    }
    
    // --- ACCESSEURS (Getters & Setters) ---
    // On fournit un accès contrôlé aux données.
    
    public ProduitSnack getProduit() {
        return produit;
    }

    public void setProduit(ProduitSnack produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    /**
     * Modifie la quantité. On pourrait ajouter une validation ici
     * pour empêcher les quantités négatives.
     * @param quantite la nouvelle quantité (doit être positive).
     */
    public void setQuantite(int quantite) {
        if (quantite >= 0) { // Ajout d'une protection logique grâce à l'encapsulation
            this.quantite = quantite;
        }
    }
}