/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Planning implements Serializable {
    private int idPlanning;
    private LocalDateTime dateHeureDebutService;
    private LocalDateTime dateHeureFinService;
    private String posteOccupe;
    private int idPersonnel; // Clé étrangère

    public Planning() {}

    public Planning(int idPlanning, LocalDateTime debut, LocalDateTime fin, String poste, int idPersonnel) {
        this.idPlanning = idPlanning;
        this.dateHeureDebutService = debut;
        this.dateHeureFinService = fin;
        this.posteOccupe = poste;
        this.idPersonnel = idPersonnel;
    }

    // --- Getters ---
    public int getId() {
        return idPlanning;
    }

    public LocalDateTime getDateHeureDebutService() {
        return dateHeureDebutService;
    }

    public LocalDateTime getDateHeureFinService() {
        return dateHeureFinService;
    }

    public String getPosteOccupe() {
        return posteOccupe;
    }

    public int getIdPersonnel() {
        return idPersonnel;
    }

    // --- Setters ---
    public void setId(int idPlanning) {
        this.idPlanning = idPlanning;
    }

    public void setDateHeureDebutService(LocalDateTime dateHeureDebutService) {
        this.dateHeureDebutService = dateHeureDebutService;
    }

    public void setDateHeureFinService(LocalDateTime dateHeureFinService) {
        this.dateHeureFinService = dateHeureFinService;
    }

    public void setPosteOccupe(String posteOccupe) {
        this.posteOccupe = posteOccupe;
    }

    public void setIdPersonnel(int idPersonnel) {
        this.idPersonnel = idPersonnel;
    }
    
}