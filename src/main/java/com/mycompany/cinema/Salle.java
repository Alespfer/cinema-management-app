/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;

import java.io.Serializable;

public class Salle implements Serializable {
    private int idSalle;
    private String numero;
    private int capacite;

    public Salle() {}

    public Salle(int idSalle, String numero, int capacite) {
        this.idSalle = idSalle;
        this.numero = numero;
        this.capacite = capacite;
    }

    // Getters and Setters
    public int getId() { return idSalle; }
    public void setId(int idSalle) { this.idSalle = idSalle; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
}