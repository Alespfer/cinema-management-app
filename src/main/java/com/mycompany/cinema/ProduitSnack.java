package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un article vendu au comptoir de snacks (confiserie).
 * Contient des informations sur le produit et son état de stock.
 * 
 * Implémente Serializable pour la sauvegarde.
 * 
 */
public class ProduitSnack implements Serializable {
    
    private int idProduit;
    private String nomProduit;
    private String description;
    private double prixVente;
    private int stock;

    public ProduitSnack() {}

    /**
     * Constructeur pour créer un nouveau produit.
     * @param id L'ID unique du produit.
     * @param nom Le nom du produit (ex: "Popcorn Salé Grand").
     * @param desc Une brève description.
     * @param prix Le prix de vente unitaire.
     * @param stock La quantité initiale en stock.
     */
    public ProduitSnack(int id, String nom, String desc, double prix, int stock) {
        this.idProduit = id;
        this.nomProduit = nom;
        this.description = desc;
        this.prixVente = prix;
        this.stock = stock;
    }

    // --- Getters and Setters ---

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