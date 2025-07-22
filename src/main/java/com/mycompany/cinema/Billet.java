/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;

import java.io.Serializable;

public class Billet implements Serializable {
    private int idBillet;
    private int idReservation;
    private int idTarif;
    private int idSiege;
    private int idSeance;

    public Billet() {}

    public Billet(int idBillet, int idReservation, int idTarif, int idSiege, int idSeance) {
        this.idBillet = idBillet;
        this.idReservation = idReservation;
        this.idTarif = idTarif;
        this.idSiege = idSiege;
        this.idSeance = idSeance;
    }

    // Getters and Setters
    public int getId() { return idBillet; }
    public void setId(int idBillet) { this.idBillet = idBillet; }
    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }
    public int getIdTarif() { return idTarif; }
    public void setIdTarif(int idTarif) { this.idTarif = idTarif; }
    public int getIdSiege() { return idSiege; }
    public void setIdSiege(int idSiege) { this.idSiege = idSiege; }
    public int getIdSeance() { return idSeance; }
    public void setIdSeance(int idSeance) { this.idSeance = idSeance; }
}