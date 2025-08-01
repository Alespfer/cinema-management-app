package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Cet objet représente un client enregistré dans notre système.
 * Il contient toutes ses informations de compte.
 * 
 * Du point de vue de l'interface, vous recevrez un objet de ce type juste après
 * une connexion réussie. Vous pourrez alors utiliser ses méthodes "get" pour,
 * par exemple, afficher un message de bienvenue personnalisé : "Bonjour, " + client.getNom().
 * 
 * Les informations de cet objet seront affichées et modifiées dans le panneau
 * "Mes Informations" de l'espace client.
 */
public class Client implements Serializable {
    
    private int idClient;           // Le numéro unique du client.
    private String nom;             // Son nom complet.
    private String email;           // Son email, qui sert aussi d'identifiant de connexion.
    private String motDePasse;      // Son mot de passe.
    private LocalDate dateCreation; // La date à laquelle il a créé son compte.

    /**
     * Constructeur vide (nécessité technique).
     */
    public Client() {}

    /**
     * Crée un nouvel objet Client avec toutes ses informations.
     */
    public Client(int idClient, String nom, String email, String motDePasse, LocalDate dateCreation) {
        this.idClient = idClient;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.dateCreation = dateCreation;
    }

    // --- ACCESSEURS (Getters & Setters) ---
    
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