// ========================================================================
// FICHIER : EvaluationClientDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.EvaluationClient;
import java.util.List;

/**
 * Définit le contrat pour la gestion des avis laissés par les clients sur les films.
 */
public interface EvaluationClientDAO {
    
    // Enregistre une nouvelle évaluation.

    void ajouterEvaluation(EvaluationClient evaluation);

    // Met à jour une évaluation existante.
    void mettreAJourEvaluation(EvaluationClient evaluation);

    // Retrouve toutes les évaluations pour un film donné.
    List<EvaluationClient> trouverEvaluationsParIdFilm(int idFilm);

    // Retrouve l'évaluation spécifique d'un client pour un film donné.
    EvaluationClient trouverEvaluationParClientEtFilm(int idClient, int idFilm);

    // Retrouve la liste de toutes les évaluations enregistrées.
    List<EvaluationClient> trouverToutesLesEvaluations();

    // Force le rechargement des données depuis la source.
    void rechargerDonnees();
}