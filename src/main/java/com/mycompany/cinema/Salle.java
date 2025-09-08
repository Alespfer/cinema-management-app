package com.mycompany.cinema;

import java.io.Serializable;
import java.util.Objects;

/**
 * Représente une salle de projection physique dans le cinéma.
 *
 * Cet objet est utilisé à plusieurs endroits dans l'interface graphique : -
 * Pour afficher le nom de la salle dans les détails d'une séance (ex: "Salle
 * 5"). - Dans `SiegePanel`, l'ID de la salle est crucial pour savoir quel plan
 * de sièges il faut dessiner. - Dans la partie administration, pour gérer la
 * liste des salles disponibles dans `GestionSallesPanel`.
 */
public class Salle implements Serializable {

    private int idSalle;
    private String numero;   // Le nom ou numéro de la salle (ex: "Salle 1", "Salle IMAX").
    private int capacite; // Le nombre total de sièges dans cette salle.

    /**
     * Constructeur vide (nécessité technique).
     */
    public Salle() {
    }

    /**
     * Crée une nouvelle salle.
     *
     * @param idSalle L'ID unique de la salle.
     * @param numero Le nom de la salle.
     * @param capacite Le nombre total de sièges.
     */
    public Salle(int idSalle, String numero, int capacite) {
        this.idSalle = idSalle;
        this.numero = numero;
        this.capacite = capacite;
    }

    // --- ACCESSEURS (Getters and Setters) ---
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

    /**
     * Calcule le hash code basé sur l'identifiant de la salle. Cette méthode
     * est essentielle pour respecter le contrat entre equals() et hashCode().
     */
    @Override
    public int hashCode() {
        // On utilise la méthode utilitaire Objects.hash pour garantir une implémentation
        // correcte et performante, comme vu à la page 182 de la doctrine.
        return Objects.hash(idSalle);
    }
}
