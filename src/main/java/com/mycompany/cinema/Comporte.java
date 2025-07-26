/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;import java.io.Serializable;

public class Comporte implements Serializable {
    private int idVente;
    private int idProduit;
    private int quantite;
    private double prixUnitaire;

    public Comporte() {}

    public Comporte(int idVente, int idProduit, int quantite, double prixUnitaire) {
        this.idVente = idVente;
        this.idProduit = idProduit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    // --- Getters ---
    public int getIdVente() {
        return idVente;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public int getQuantite() {
        return quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    // --- Setters ---
    public void setIdVente(int idVente) {
        this.idVente = idVente;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
}
