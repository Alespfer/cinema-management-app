/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDate;

public class Client implements Serializable {
    private int idClient;
    private String nom;
    private String email;
    private String motDePasse;
    private LocalDate dateCreation;

    public Client() {}

    public Client(int idClient, String nom, String email, String motDePasse, LocalDate dateCreation) {
        this.idClient = idClient;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.dateCreation = dateCreation;
    }

    // Getters and Setters
    public int getId() { return idClient; }
    public void setId(int idClient) { this.idClient = idClient; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }
}