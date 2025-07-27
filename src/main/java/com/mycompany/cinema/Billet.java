package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Classe représentant un billet de cinéma.
 * Un billet est l'entité qui connecte une réservation, une séance, un siège et un tarif.
 * C'est le cœur du système de réservation.
 * 
 * Comme les autres classes du modèle, elle doit être 'Serializable' pour être sauvegardée.
 * 
 */
public class Billet implements Serializable {
    
    // La clé primaire du billet.
    private int idBillet;
    
    // Les clés étrangères qui lient le billet aux autres objets.
    private int idReservation;
    private int idTarif;
    private int idSiege;
    private int idSeance;

    /**
     * Constructeur vide.
     */
    public Billet() {}

    /**
     * Constructeur pour créer un billet complet avec toutes ses informations.
     */
    public Billet(int idBillet, int idReservation, int idTarif, int idSiege, int idSeance) {
        this.idBillet = idBillet;
        this.idReservation = idReservation;
        this.idTarif = idTarif;
        this.idSiege = idSiege;
        this.idSeance = idSeance;
    }

    // --- Getters and Setters ---

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