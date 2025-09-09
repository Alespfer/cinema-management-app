package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Représente un créneau de travail assigné à un employé.
 */
public class Planning implements Serializable {

    // Identifiant unique du créneau.
    private int idPlanning;

    // Date et heure de début du service.
    private LocalDateTime dateHeureDebutService;

    // Date et heure de fin du service.
    private LocalDateTime dateHeureFinService;

    // Poste occupé (ex: "Accueil", "Vente Snacking").
    private String posteOccupe;

    // Identifiant de l'employé concerné.
    private int idPersonnel;

    public Planning() {
    }

    // Constructeur principal
    public Planning(int idPlanning, LocalDateTime debut, LocalDateTime fin, String poste, int idPersonnel) {
        this.idPlanning = idPlanning;
        this.dateHeureDebutService = debut;
        this.dateHeureFinService = fin;
        this.posteOccupe = poste;
        this.idPersonnel = idPersonnel;
    }
    
    
    // --- Getters / Setters ---

    public int getId() {
        return idPlanning;
    }

    public void setId(int idPlanning) {
        this.idPlanning = idPlanning;
    }

    public LocalDateTime getDateHeureDebutService() {
        return dateHeureDebutService;
    }

    public void setDateHeureDebutService(LocalDateTime dateHeureDebutService) {
        this.dateHeureDebutService = dateHeureDebutService;
    }

    public LocalDateTime getDateHeureFinService() {
        return dateHeureFinService;
    }

    public void setDateHeureFinService(LocalDateTime dateHeureFinService) {
        this.dateHeureFinService = dateHeureFinService;
    }

    public String getPosteOccupe() {
        return posteOccupe;
    }

    public void setPosteOccupe(String posteOccupe) {
        this.posteOccupe = posteOccupe;
    }

    public int getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(int idPersonnel) {
        this.idPersonnel = idPersonnel;
    }
}
