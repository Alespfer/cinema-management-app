package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un fauteuil physique et unique dans une salle de cinéma.
 * Chaque siège est défini par sa position (numéro de rangée et de siège)
 * et est rattaché à une salle spécifique.
 * 
 * L'interface graphique `SiegePanel` est le principal utilisateur de cette classe.
 * Vous y afficherez une grille de boutons cliquables, où chaque bouton représentera
 * un objet `Siege`. Vous utiliserez les propriétés de l'objet pour :
 * - Le positionner correctement dans la grille (`getNumeroRangee`, `getNumeroSiege`).
 * - Savoir s'il est disponible, occupé ou sélectionné par l'utilisateur.
 * - Le stocker dans une liste lorsque le client clique dessus pour le réserver.
 */
public class Siege implements Serializable {
    
    private int idSiege;        // Le numéro unique du fauteuil dans tout le cinéma.
    private int numeroRangee;   // Le numéro de la rangée (ex: 8).
    private int numeroSiege;    // Le numéro du siège dans cette rangée (ex: 12).
    
    // Fait le lien avec la salle dans laquelle se trouve ce siège.
    private int idSalle;

    /**
     * Constructeur vide (nécessité technique).
     */
    public Siege() {}

    /**
     * Crée un nouveau siège.
     * @param idSiege L'ID unique du siège.
     * @param numeroRangee Le numéro de sa rangée.
     * @param numeroSiege Le numéro de sa place dans la rangée.
     * @param idSalle L'ID de la salle à laquelle il appartient.
     */
    public Siege(int idSiege, int numeroRangee, int numeroSiege, int idSalle) {
        this.idSiege = idSiege;
        this.numeroRangee = numeroRangee;
        this.numeroSiege = numeroSiege;
        this.idSalle = idSalle;
    }

    // --- ACCESSEURS (Getters and Setters) ---

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