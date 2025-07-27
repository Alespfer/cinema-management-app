package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un siège individuel dans une salle de cinéma.
 * Chaque siège est identifié par son numéro de rangée et son numéro de siège
 * et appartient à une salle spécifique.
 * 
 * Implémente Serializable pour la sauvegarde.
 * 
 */
public class Siege implements Serializable {
    
    private int idSiege;
    private int numeroRangee;
    private int numeroSiege;
    
    // Clé étrangère vers la salle à laquelle le siège appartient
    private int idSalle;

    public Siege() {}

    /**
     * Constructeur pour créer un nouveau siège.
     * @param idSiege L'ID unique du siège.
     * @param numeroRangee Le numéro de la rangée (ligne).
     * @param numeroSiege Le numéro du siège dans la rangée.
     * @param idSalle L'ID de la salle où se trouve le siège.
     */
    public Siege(int idSiege, int numeroRangee, int numeroSiege, int idSalle) {
        this.idSiege = idSiege;
        this.numeroRangee = numeroRangee;
        this.numeroSiege = numeroSiege;
        this.idSalle = idSalle;
    }

    // --- Getters and Setters ---

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