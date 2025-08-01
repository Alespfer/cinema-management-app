package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente une seule ligne sur un ticket de caisse de snacks.
 * Par exemple : "2 Popcorns à 6.50€ l'unité".
 * 
 * Son rôle est de lier une vente globale (`VenteSnack`) avec un produit spécifique
 * (`ProduitSnack`) tout en ajoutant des informations propres à cette transaction :
 * la quantité et le prix au moment de l'achat.
 * 
 * L'interface graphique n'interagira pas directement avec cette classe.
 * Elle sera utilisée en coulisses, notamment par le panneau de reporting
 * de l'administrateur pour calculer le total d'une vente ou afficher son détail.
 */
public class Comporte implements Serializable {
    
    // --- Clés de liaison ---
    private int idVente;      // Fait référence au ticket de caisse global.
    private int idProduit;    // Fait référence au produit acheté (ex: Popcorn).
    
    // --- Données de la transaction ---
    private int quantite;         // Combien d'unités de ce produit ont été achetées ?
    private double prixUnitaire;  // Quel était le prix d'une unité à ce moment précis ?

    /**
     * Constructeur vide (nécessité technique pour la sauvegarde).
     */
    public Comporte() {}

    /**
     * Crée une nouvelle ligne de vente.
     * @param idVente L'ID du ticket de caisse parent.
     * @param idProduit L'ID du produit vendu.
     * @param quantite Le nombre d'articles.
     * @param prixUnitaire Le prix d'un article au moment de la vente.
     */
    public Comporte(int idVente, int idProduit, int quantite, double prixUnitaire) {
        this.idVente = idVente;
        this.idProduit = idProduit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    // --- ACCESSEURS (Getters & Setters) ---

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