package com.mycompany.cinema;

import java.io.Serializable;
import java.util.Objects;

/**
 * Représente une salle de projection du cinéma.
 */
public class Salle implements Serializable {

    //  Identifiant unique de la salle.
    private int idSalle;

    // Nom ou numéro de la salle (ex: "Salle 1", "Salle IMAX").
    private String numero;

    // Nombre total de sièges disponibles.
    private int capacite;

    public Salle() {
    }

    /**
     * Constructeur principal.
     *
     * @param idSalle identifiant unique de la salle
     * @param numero nom ou numéro attribué à la salle
     * @param capacite nombre total de sièges
     */
    public Salle(int idSalle, String numero, int capacite) {
        this.idSalle = idSalle;
        this.numero = numero;
        this.capacite = capacite;
    }

    // --- Getters / Setters ---
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

    // Rédéfinition de la méthode equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Salle salle = (Salle) obj;
        return idSalle == salle.idSalle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSalle);
    }
}
