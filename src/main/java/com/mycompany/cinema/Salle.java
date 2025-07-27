package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente une salle de projection physique dans le cinéma.
 * Chaque salle a un numéro et une capacité maximale.
 * 
 * Implémente Serializable pour la sauvegarde.
 * 
 */
public class Salle implements Serializable {
    
    private int idSalle;
    private String numero;
    private int capacite;

    public Salle() {}

    /**
     * Constructeur pour créer une nouvelle salle.
     * @param idSalle L'ID unique de la salle.
     * @param numero Le nom ou numéro de la salle (ex: "Salle 1", "Salle IMAX").
     * @param capacite Le nombre total de sièges dans la salle.
     */
    public Salle(int idSalle, String numero, int capacite) {
        this.idSalle = idSalle;
        this.numero = numero;
        this.capacite = capacite;
    }

    // --- Getters and Setters ---

    public int getId() { 
        return idSalle; 
    }
    public void setId(int idSalle) { 
        this.idSalle = idSalle; 
    }

    public String getNumero() { 
        return numero; 
    }
    public void setNumero(String numero) { 
        this.numero = numero; 
    }

    public int getCapacite() { 
        return capacite; 
    }
    public void setCapacite(int capacite) { 
        this.capacite = capacite; 
    }
}