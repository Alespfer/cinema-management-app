package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Représente une réservation effectuée par un client.
 * Une réservation est un "panier" qui contient un ou plusieurs billets.
 * Elle lie un client à un ensemble de billets pour une date donnée.
 * 
 * Implémente Serializable pour la sauvegarde.
 * 
 */
public class Reservation implements Serializable {
    
    private int idReservation;
    private LocalDateTime dateReservation;
    
    // Clé étrangère vers le client qui a fait la réservation
    private int idClient;

    public Reservation() {}

    /**
     * Constructeur pour créer une nouvelle réservation.
     * @param idReservation L'ID unique de la réservation.
     * @param dateReservation La date et l'heure à laquelle la réservation a été faite.
     * @param idClient L'ID du client concerné.
     */
    public Reservation(int idReservation, LocalDateTime dateReservation, int idClient) {
        this.idReservation = idReservation;
        this.dateReservation = dateReservation;
        this.idClient = idClient;
    }

    // --- Getters and Setters ---

    public int getId() { 
        return idReservation; 
    }
    public void setId(int idReservation) { 
        this.idReservation = idReservation; 
    }

    public LocalDateTime getDateReservation() { 
        return dateReservation; 
    }
    public void setDateReservation(LocalDateTime dateReservation) { 
        this.dateReservation = dateReservation; 
    }

    public int getIdClient() { 
        return idClient; 
    }
    public void setIdClient(int idClient) { 
        this.idClient = idClient; 
    }
}