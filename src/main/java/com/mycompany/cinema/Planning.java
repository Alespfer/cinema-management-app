package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Représente un créneau de travail pour un membre du personnel.
 * Permet de définir qui travaille, quand, et à quel poste.
 * 
 * Implémente Serializable pour la sauvegarde.
 * 
 */
public class Planning implements Serializable {
    
    private int idPlanning;
    private LocalDateTime dateHeureDebutService;
    private LocalDateTime dateHeureFinService;
    private String posteOccupe;
    
    // Clé étrangère vers le membre du personnel concerné
    private int idPersonnel;

    public Planning() {}

    /**
     * Constructeur pour créer un nouveau créneau de planning.
     * @param idPlanning L'ID unique du créneau.
     * @param debut La date et l'heure de début du service.
     * @param fin La date et l'heure de fin du service.
     * @param poste Le poste occupé (ex: "Vente Snacking", "Accueil").
     * @param idPersonnel L'ID de l'employé concerné.
     */
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