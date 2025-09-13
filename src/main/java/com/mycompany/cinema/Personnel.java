package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un membre du personnel du cinéma. Contient ses informations
 * personnelles et le rôle associé.
 */
public class Personnel implements Serializable {

    // Identifiant unique du membre du personnel.
    private int idPersonnel;

    // Nom du membre du personnel.
    private String nom;

    // Prénom du membre du personnel.
    private String prenom;

    // Adresse email (sert d'identifiant de connexion).
    private String email;

    // Mot de passe du compte.
    private String motDePasse;

    // Variable booléenne pour indiquer si l'employé est actif 
    private boolean estActif;

    // Identifiant du rôle associé (ex: administrateur, vendeur...).
    private int idRole;

    public Personnel() {
    }

    /**
     * Constructeur principal.
     *
     * @param idPersonnel identifiant unique
     * @param nom nom du personnel
     * @param prenom prénom du personnel
     * @param email adresse email
     * @param motDePasse mot de passe
     * @param idRole identifiant du rôle
     */
    public Personnel(int idPersonnel, String nom, String prenom, String email, String motDePasse, int idRole) {
        this.idPersonnel = idPersonnel;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.idRole = idRole;
        this.estActif = true; // Par défaut, l'employé est initialisé en tant qu'actif
    }

    // --- Getters / Setters ---
    public int getId() {
        return idPersonnel;
    }

    public void setId(int idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public boolean getEstActif() {
        return estActif;
    }

    public void setEstActif(boolean estActif) {
        this.estActif = estActif;
    }

}
