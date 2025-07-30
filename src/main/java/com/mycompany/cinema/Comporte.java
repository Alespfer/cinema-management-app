package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente une ligne d'une vente de snack. C'est une table de liaison du MLD
 * entre VENTE_SNACK et PRODUIT_SNACK.
 * 
 * Elle contient des informations propres à la transaction, comme la quantité
 * vendue et le prix unitaire au moment de la vente (important, car le prix
 * du produit peut changer plus tard).
 * 
 * Implémente Serializable pour la sauvegarde.
 * 
 */
public class Comporte implements Serializable {
    
    // Clés étrangères vers VenteSnack et ProduitSnack
    private int idVente;
    private int idProduit;
    
    // Attributs spécifiques à cette ligne de vente
    private int quantite;
    private double prixUnitaire;

    public Comporte() {}

    /**
     * Constructeur pour créer une ligne de vente.
     * @param idVente L'ID de la vente à laquelle cette ligne appartient.
     * @param idProduit L'ID du produit vendu.
     * @param quantite Le nombre d'unités vendues.
     * @param prixUnitaire Le prix du produit au moment de la vente.
     */
    public Comporte(int idVente, int idProduit, int quantite, double prixUnitaire) {
        this.idVente = idVente;
        this.idProduit = idProduit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    // --- Getters & Setters ---

    public int getIdVente() { 
        return idVente; 
    }
    public void setIdVente(int idVente) { 
        this.idVente = idVente; 
    }

    public int getIdProduit() { 
        return idProduit; 
    }
    public void setIdProduit(int idProduit) { 
        this.idProduit = idProduit; 
    }

    public int getQuantite() { 
        return quantite; 
    }
    public void setQuantite(int quantite) { 
        this.quantite = quantite; 
    }

    public double getPrixUnitaire() { 
        return prixUnitaire; 
    }
    public void setPrixUnitaire(double prixUnitaire) { 
        this.prixUnitaire = prixUnitaire; 
    }
    
    //Ceci est un test
}