package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un billet de cinéma lié à une réservation.
 *
 * Contient les identifiants nécessaires pour retrouver :
 * - la réservation associée,
 * - le tarif appliqué,
 * - le siège réservé,
 * - la séance concernée.
 *
 */
public class Billet implements Serializable {

    // Identifiant unique du billet.
    private int idBillet;

    // Identifiant de la réservation à laquelle ce billet appartient.
    private int idReservation;

    // Identifiant du tarif appliqué (plein, étudiant, etc.). 
    private int idTarif;

    // Identifiant du siège réservé. 
    private int idSiege;

    // Identifiant de la séance concernée.
    private int idSeance;

    public Billet() {}

    /**
     * Constructeur principal.
     *
     * @param idBillet identifiant du billet
     * @param idReservation identifiant de la réservation
     * @param idTarif identifiant du tarif appliqué
     * @param idSiege identifiant du siège réservé
     * @param idSeance identifiant de la séance concernée
     */
    public Billet(int idBillet, int idReservation, int idTarif, int idSiege, int idSeance) {
        this.idBillet = idBillet;
        this.idReservation = idReservation;
        this.idTarif = idTarif;
        this.idSiege = idSiege;
        this.idSeance = idSeance;
    }
    
    // --- Getters / Setters ---

    public int getId() {
        return idBillet;
    }

    public void setId(int idBillet) {
        this.idBillet = idBillet;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdTarif() {
        return idTarif;
    }

    public void setIdTarif(int idTarif) {
        this.idTarif = idTarif;
    }

    public int getIdSiege() {
        return idSiege;
    }

    public void setIdSiege(int idSiege) {
        this.idSiege = idSiege;
    }

    public int getIdSeance() {
        return idSeance;
    }

    public void setIdSeance(int idSeance) {
        this.idSeance = idSeance;
    }
}
