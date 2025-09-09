package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un siège physique dans une salle.
 */
public class Siege implements Serializable {

    // Identifiant unique du siège.
    private int idSiege;

    // Numéro de rangée.
    private int numeroRangee;

    // Numéro du siège dans la rangée.
    private int numeroSiege;

    // Identifiant de la salle à laquelle appartient ce siège.
    private int idSalle;

   
    public Siege() {
    }

    /**
     * Constructeur principal.
     *
     * @param idSiege identifiant unique du siège
     * @param numeroRangee rangée du siège
     * @param numeroSiege numéro dans la rangée
     * @param idSalle identifiant de la salle
     */
    public Siege(int idSiege, int numeroRangee, int numeroSiege, int idSalle) {
        this.idSiege = idSiege;
        this.numeroRangee = numeroRangee;
        this.numeroSiege = numeroSiege;
        this.idSalle = idSalle;
    }
    
    
    // --- Getters / Setters ---


    public int getId() {
        return idSiege;
    }

    public void setId(int idSiege) {
        this.idSiege = idSiege;
    }

    public int getNumeroRangee() {
        return numeroRangee;
    }

    public void setNumeroRangee(int numeroRangee) {
        this.numeroRangee = numeroRangee;
    }

    public int getNumeroSiege() {
        return numeroSiege;
    }

    public void setNumeroSiege(int numeroSiege) {
        this.numeroSiege = numeroSiege;
    }

    public int getIdSalle() {
        return idSalle;
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }
}
