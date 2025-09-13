// ========================================================================
// AffectationSeanceDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.AffectationSeance;
import java.util.List;

/**
 * Définit le contrat pour la persistance des données des affectations du personnel aux séances.
 * 
 * NB : Comme expliqué dans notre rapport (P.10), les méthodes de cette classe ont été initialement développées
 * mais non priorisées dans le rendu final. Il serait possible de les incorporer dans une version future. 
 */
public interface AffectationSeanceDAO {

  
    
    // Enregistre une nouvelle association entre un membre du personnel et une séance.
    void ajouterAffectation(AffectationSeance affectation);

    // Recherche toutes les affectations pour une séance donnée.
    List<AffectationSeance> trouverAffectationsParIdSeance(int idSeance);


    // Recherche toutes les affectations pour un membre du personnel donné.
    List<AffectationSeance> trouverAffectationsParIdPersonnel(int idPersonnel);

    // Supprime le lien entre un membre du personnel et une séance.
    void supprimerAffectation(int idSeance, int idPersonnel);
    
    //Force le rechargement des données depuis la source.
    void rechargerDonnees();
}