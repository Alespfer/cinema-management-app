/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;


import java.io.Serializable;

public class Personnel implements Serializable {
    private int idPersonnel;
    private String nom;
    private String prenom;
    private String motDePasse;
    private int idRole; // Clé étrangère

    public Personnel() {}

    public Personnel(int idPersonnel, String nom, String prenom, String motDePasse, int idRole) {
        this.idPersonnel = idPersonnel;
        this.nom = nom;
        this.prenom = prenom;
        this.motDePasse = motDePasse;
        this.idRole = idRole;
    }

    // --- Getters ---
    public int getId() { return idPersonnel; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getMotDePasse() { return motDePasse; }
    public int getIdRole() { return idRole; }

    // --- Setters ---
    public void setId(int idPersonnel) { this.idPersonnel = idPersonnel; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public void setIdRole(int idRole) { this.idRole = idRole; }
}