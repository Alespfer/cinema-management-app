package com.mycompany.cinema.dao;

import com.mycompany.cinema.AffectationSeance;
import java.util.List;

/**
 * Définit le "contrat" pour tout ce qui touche à la sauvegarde des affectations du personnel.
 * C'est une liste de règles : toute classe qui gère les affectations DOIT savoir
 * comment ajouter, trouver et supprimer ces liens.
 * 
 * Pour l'interface graphique (côté admin), cela signifie que vous pouvez compter sur
 * l'existence de ces fonctionnalités. Quand vous développerez le panneau de gestion
 * des séances, vous appellerez (via le service) une méthode qui implémente
 * `getAffectationsBySeanceId` pour afficher quel employé est assigné.
 */
public interface AffectationSeanceDAO {

    /** Règle n°1 : On doit pouvoir enregistrer une nouvelle affectation. */
    void addAffectation(AffectationSeance affectation);

    /** Règle n°2 : On doit pouvoir retrouver toutes les affectations pour une séance donnée. */
    List<AffectationSeance> getAffectationsBySeanceId(int seanceId);

    /** Règle n°3 : On doit pouvoir retrouver tous les créneaux de travail d'un employé. */
    List<AffectationSeance> getAffectationsByPersonnelId(int personnelId);

    /** Règle n°4 : On doit pouvoir annuler l'affectation d'un employé à une séance. */
    void deleteAffectation(int seanceId, int personnelId);
    
    /** Règle n°5 : On doit pouvoir forcer le rechargement des données depuis le disque. */
    void rechargerDonnees();
}