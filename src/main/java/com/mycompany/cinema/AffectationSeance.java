package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Classe représentant la table de liaison entre un membre du personnel et une séance.
 * Elle sert à définir quel employé est assigné à quelle projection.
 * 
 * J'ai utilisé cette classe pour matérialiser la relation "Many-to-Many" du MLD
 * entre PERSONNEL et SEANCE.
 * 
 * Elle doit implémenter 'Serializable' pour que je puisse la sauvegarder dans un fichier
 * avec le GenericDAOImpl, comme on l'a vu dans le chapitre sur les IO Fichiers.
 * 
 */
public class AffectationSeance implements Serializable {
    
    // Ces attributs sont des clés étrangères, elles correspondent aux ID
    // des objets Seance et Personnel qu'on veut lier.
    private int idSeance;
    private int idPersonnel;

    /**
     * Constructeur par défaut, nécessaire pour certaines opérations de sérialisation.
     */
    public AffectationSeance() {}

    /**
     * Constructeur principal pour créer une nouvelle affectation.
     * @param idSeance L'identifiant de la séance concernée.
     * @param idPersonnel L'identifiant du membre du personnel assigné.
     */
    public AffectationSeance(int idSeance, int idPersonnel) {
        this.idSeance = idSeance;
        this.idPersonnel = idPersonnel;
    }
    
    // --- Getters ---
    // Méthodes pour lire les valeurs des attributs depuis l'extérieur.
    // C'est le principe d'encapsulation vu en cours.

    public int getIdSeance() {
        return idSeance;
    }

    public int getIdPersonnel() {
        return idPersonnel;
    }

    // --- Setters ---
    // Méthodes pour modifier les valeurs des attributs.

    public void setIdSeance(int idSeance) {
        this.idSeance = idSeance;
    }

    public void setIdPersonnel(int idPersonnel) {
        this.idPersonnel = idPersonnel;
    }
}