package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Représente un client de l’application (compte utilisateur). Contient les
 * informations d’authentification et de profil.
 */
public class Client implements Serializable {

    // Identifiant unique du client.
    private int idClient;

    // Nom du client
    private String nom;

    // Adresse e-mail (sert d’identifiant de connexion).
    private String email;

    // Mot de passe du compte.
    private String motDePasse;

    // Date de création du compte.
    private LocalDate dateCreation;

    // Constructeur par défaut requis pour la sérialisation.
    public Client() {
    }

    /**
     * Constructeur principal.
     *
     * @param idClient identifiant
     * @param nom nom du client
     * @param email e-mail du client
     * @param motDePasse mot de passe
     * @param dateCreation date de création du compte
     */
    public Client(int idClient, String nom, String email, String motDePasse, LocalDate dateCreation) {
        this.idClient = idClient;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.dateCreation = dateCreation;
    }

    // --- Getters / Setters ---
    public int getId() {
        return idClient;
    }

    public void setId(int idClient) {
        this.idClient = idClient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }
}
