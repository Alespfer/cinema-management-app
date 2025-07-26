/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;
import java.io.Serializable;

public class Siege implements Serializable {
    private int idSiege;
    private int numeroRangee;
    private int numeroSiege;
    private int idSalle; // Clé étrangère

    public Siege() {}

    public Siege(int idSiege, int numeroRangee, int numeroSiege, int idSalle) {
        this.idSiege = idSiege;
        this.numeroRangee = numeroRangee;
        this.numeroSiege = numeroSiege;
        this.idSalle = idSalle;
    }

    // Getters and Setters
    public int getId() { return idSiege; }
    public void setId(int idSiege) { this.idSiege = idSiege; }
    public int getNumeroRangee() { return numeroRangee; }
    public void setNumeroRangee(int numeroRangee) { this.numeroRangee = numeroRangee; }
    public int getNumeroSiege() { return numeroSiege; }
    public void setNumeroSiege(int numeroSiege) { this.numeroSiege = numeroSiege; }
    public int getIdSalle() { return idSalle; }
    public void setIdSalle(int idSalle) { this.idSalle = idSalle; }
}