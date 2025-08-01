package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un article vendu au comptoir de snacks, comme un popcorn ou une boisson.
 * 
 * Cet objet est crucial pour deux parties de l'interface graphique :
 * 1. Côté Client : Une liste d'objets `ProduitSnack` sera affichée dans le 
 *    `SnackSelectionPanel` pour que le client puisse composer son panier. L'attribut
 *    `stock` est important ici pour savoir si un produit est disponible.
 * 2. Côté Admin : Le `GestionProduitsSnackPanel` utilisera ces objets pour permettre
 *    à l'administrateur de gérer le catalogue (ajouter/modifier/supprimer des produits)
 *    et de mettre à jour les stocks.
 */
public class ProduitSnack implements Serializable {
    
    private int idProduit;
    private String nomProduit;
    private String description;
    private double prixVente;
    private int stock; // La quantité restante de ce produit.

    /**
     * Constructeur vide (nécessité technique).
     */
    public ProduitSnack() {}

    /**
     * Crée un nouveau produit pour le comptoir de snacks.
     * @param id L'ID unique du produit.
     * @param nom Le nom affiché au client (ex: "Popcorn Salé Grand").
     * @param desc Une courte description.
     * @param prix Le prix de vente d'une unité.
     * @param stock La quantité initiale disponible.
     */
    public ProduitSnack(int id, String nom, String desc, double prix, int stock) {
        this.idProduit = id;
        this.nomProduit = nom;
        this.description = desc;
        this.prixVente = prix;
        this.stock = stock;
    }

    // --- ACCESSEURS (Getters and Setters) ---

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