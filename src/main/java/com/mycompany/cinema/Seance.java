package com.mycompany.cinema;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.Serializable;
import java.time.LocalDateTime;

public class Seance implements Serializable {
    private int idSeance;
    private LocalDateTime dateHeureDebut;
    private int idSalle; // Clé étrangère
    private int idFilm;  // Clé étrangère

    public Seance() {}

    public Seance(int idSeance, LocalDateTime dateHeureDebut, int idSalle, int idFilm) {
        this.idSeance = idSeance;
        this.dateHeureDebut = dateHeureDebut;
        this.idSalle = idSalle;
        this.idFilm = idFilm;
    }

    // Getters and Setters
    public int getId() { return idSeance; }
    public void setId(int idSeance) { this.idSeance = idSeance; }
    public LocalDateTime getDateHeureDebut() { return dateHeureDebut; }
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) { this.dateHeureDebut = dateHeureDebut; }
    public int getIdSalle() { return idSalle; }
    public void setIdSalle(int idSalle) { this.idSalle = idSalle; }
    public int getIdFilm() { return idFilm; }
    public void setIdFilm(int idFilm) { this.idFilm = idFilm; }
}