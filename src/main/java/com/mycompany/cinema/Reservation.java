/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Reservation implements Serializable {
    private int idReservation;
    private LocalDateTime dateReservation;
    private int idClient; // Clé étrangère

    public Reservation() {}

    public Reservation(int idReservation, LocalDateTime dateReservation, int idClient) {
        this.idReservation = idReservation;
        this.dateReservation = dateReservation;
        this.idClient = idClient;
    }

    // Getters and Setters
    public int getId() { return idReservation; }
    public void setId(int idReservation) { this.idReservation = idReservation; }
    public LocalDateTime getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDateTime dateReservation) { this.dateReservation = dateReservation; }
    public int getIdClient() { return idClient; }
    public void setIdClient(int idClient) { this.idClient = idClient; }
}