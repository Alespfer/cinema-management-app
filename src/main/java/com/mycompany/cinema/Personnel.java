package com.mycompany.cinema;

import java.io.Serializable;

public class Personnel implements Serializable {

    private int idPersonnel;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private int idRole;

    /**
     * Constructeur vide (nécessité technique).
     */
    public Personnel() {
    }

    /**
     * Constructeur principal et valide.
     */
    public Personnel(int idPersonnel, String nom, String prenom, String email, String motDePasse, int idRole) {
        this.idPersonnel = idPersonnel;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.idRole = idRole;
    }

    // --- ACCESSEURS (Getters) ---
    public int getId() {
        return idPersonnel;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public int getIdRole() {
        return idRole;
    }

    // --- MUTATEURS (Setters) ---
    public void setId(int idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }
}
