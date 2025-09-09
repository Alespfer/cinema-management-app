package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente une ligne d’une vente de snacks. Contient le produit vendu, la
 * quantité et le prix unitaire au moment de l’achat.
 */
public class LigneVente implements Serializable {

    // Identifiant de la vente
    private int idVente;
    
    // Identifiant du produit
    private int idProduit;
    
    // Quantité du produit 
    private int quantite;
    
    // Prix unitaire appliqué lors de la vente
    private double prixUnitaire;

    public LigneVente() {
    }

    public LigneVente(int idVente, int idProduit, int quantite, double prixUnitaire) {
        this.idVente = idVente;
        this.idProduit = idProduit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    
    // --- Getters / Setters ---

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
}
