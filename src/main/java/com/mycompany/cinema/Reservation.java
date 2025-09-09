package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Représente une réservation effectuée par un client.
 */
public class Reservation implements Serializable {

    // Identifiant unique de la réservation.
    private int idReservation;

    // Date et heure de la réservation.
    private LocalDateTime dateReservation;

    // Identifiant du client ayant effectué la réservation.
    private int idClient;

    public Reservation() {
    }

    public Reservation(int idReservation, LocalDateTime dateReservation, int idClient) {
        this.idReservation = idReservation;
        this.dateReservation = dateReservation;
        this.idClient = idClient;
    }
    
    // --- Getters / Setters ---

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
