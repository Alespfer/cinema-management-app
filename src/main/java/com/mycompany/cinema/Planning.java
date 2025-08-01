package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Représente un créneau de travail assigné à un employé.
 * C'est l'équivalent d'une ligne dans un tableau de service, indiquant
 * qui travaille, de quelle heure à quelle heure, et à quel poste.
 * 
 * Cette classe sera exclusivement utilisée par la partie administration de
 * l'interface graphique, probablement dans un panneau de gestion des plannings
 * pour afficher les horaires de chaque employé sous forme de calendrier ou de liste.
 * L'interface client n'interagira jamais avec cet objet.
 */
public class Planning implements Serializable {
    
    private int idPlanning;                 // Le numéro unique de ce créneau horaire.
    private LocalDateTime dateHeureDebutService; // Date et heure de début du service.
    private LocalDateTime dateHeureFinService;   // Date et heure de fin du service.
    private String posteOccupe;             // Le poste (ex: "Accueil", "Vente Snacking").
    
    // Fait le lien avec l'employé concerné.
    private int idPersonnel;

    /**
     * Constructeur vide (nécessité technique pour la sauvegarde).
     */
    public Planning() {}

    /**
     * Crée un nouveau créneau de planning pour un employé.
     * @param idPlanning L'ID unique du créneau.
     * @param debut Le moment où le service commence.
     * @param fin Le moment où le service se termine.
     * @param poste Le poste de travail.
     * @param idPersonnel L'ID de l'employé.
     */
    public Planning(int idPlanning, LocalDateTime debut, LocalDateTime fin, String poste, int idPersonnel) {
        this.idPlanning = idPlanning;
        this.dateHeureDebutService = debut;
        this.dateHeureFinService = fin;
        this.posteOccupe = poste;
        this.idPersonnel = idPersonnel;
    }

    // --- ACCESSEURS (Getters & Setters) ---

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