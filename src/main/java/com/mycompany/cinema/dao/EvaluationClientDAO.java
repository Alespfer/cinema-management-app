// Fichier : EvaluationClientDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.EvaluationClient;
import java.util.List;
import java.util.Optional;

/**
 * Définit le contrat pour la gestion des avis laissés par les clients.
 * 
 * L'interface graphique utilisera ce contrat :
 * - via `addEvaluation` quand un client soumet un avis dans `EvaluationDialog`.
 * - via `getEvaluationsByFilmId` pour afficher la liste des avis dans `FilmDetailPanel`.
 * - via `getEvaluationByClientAndFilm` pour vérifier si le client a déjà voté
 *   et ainsi désactiver le bouton "Donner une note".
 */
public interface EvaluationClientDAO {
    void addEvaluation(EvaluationClient evaluation);
    void updateEvaluation(EvaluationClient evaluation);
    List<EvaluationClient> getEvaluationsByFilmId(int filmId);
    EvaluationClient getEvaluationByClientAndFilm(int clientId, int filmId);
    List<EvaluationClient> getAllEvaluations();
    void rechargerDonnees();
}