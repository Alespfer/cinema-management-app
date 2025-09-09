package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente l’affectation d’un membre du personnel à une séance donnée.
 * 
 * Utilisée principalement en back-office pour gérer le planning et les rôles
 * lors des projections.
 */
public class AffectationSeance implements Serializable {
    
    // Identifiant unique de la séance concernée.
    private int idSeance;

    // Identifiant unique du membre du personnel affecté. 
    private int idPersonnel;

    // Constructeur par défaut requis pour la sérialisation.
    public AffectationSeance() {}

    /**
     * Constructeur principal.
     *
     * @param idSeance identifiant de la séance
     * @param idPersonnel identifiant du personnel assigné
     */
    public AffectationSeance(int idSeance, int idPersonnel) {
        this.idSeance = idSeance;
        this.idPersonnel = idPersonnel;
    }
    
    // --- Getters / Setters ---

    public int getIdSeance() {
        return idSeance;
    }

    public void setIdSeance(int idSeance) {
        this.idSeance = idSeance;
    }

    public int getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(int idPersonnel) {
        this.idPersonnel = idPersonnel;
    }
}
