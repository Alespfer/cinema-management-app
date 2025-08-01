package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Pensez à cette classe comme à une simple "note de service".
 * Son unique but est de lier deux informations : un membre du personnel et une séance.
 * Elle répond à la question : "Qui est de service pour la projection de Dune à 20h ?".
 * 
 * L'interface graphique n'aura probablement jamais besoin de créer ou de modifier
 * directement un objet de ce type. C'est le panneau d'administration qui s'en chargera
 * en coulisses. Vous pourriez cependant l'utiliser pour afficher le nom de l'employé
 * assigné sur une vue de détail d'une séance dans le back-office.
 */
public class AffectationSeance implements Serializable {
    
    // L'identifiant numérique de la séance concernée.
    private int idSeance;
    // L'identifiant numérique de l'employé assigné à cette séance.
    private int idPersonnel;

    /**
     * Constructeur vide, une nécessité technique pour que la sauvegarde et
     * le chargement des données fonctionnent correctement.
     */
    public AffectationSeance() {}

    /**
     * Crée le lien entre une séance et un employé.
     * @param idSeance Le numéro de la séance.
     * @param idPersonnel Le numéro de l'employé.
     */
    public AffectationSeance(int idSeance, int idPersonnel) {
        this.idSeance = idSeance;
        this.idPersonnel = idPersonnel;
    }
    
    // --- ACCESSEURS (Getters & Setters) ---
    // Ces méthodes permettent de lire ou de modifier les identifiants en toute sécurité.

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