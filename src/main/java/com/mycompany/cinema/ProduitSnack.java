package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un produit vendu au comptoir de snacks.
 */
public class ProduitSnack implements Serializable {

    //Identifiant unique du produit.
    private int idProduit;

    // Nom du produit (ex: "Popcorn Salé Grand").
    private String nomProduit;

    // Description du produit.
    private String description;

    // Prix de vente unitaire.
    private double prixVente;

    // Stock disponible.
    private int stock;

    public ProduitSnack() {
    }

    public ProduitSnack(int id, String nom, String desc, double prix, int stock) {
        this.idProduit = id;
        this.nomProduit = nom;
        this.description = desc;
        this.prixVente = prix;
        this.stock = stock;
    }
    
    // --- Getters / Setters ---

    public int getId() {
        return idProduit;
    }

    public void setId(int idProduit) {
        this.idProduit = idProduit;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(double prixVente) {
        this.prixVente = prixVente;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
