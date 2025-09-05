package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.EvaluationClient;
import com.mycompany.cinema.dao.EvaluationClientDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation concrète pour la gestion des avis clients dans le fichier "evaluations_client.dat".
 * 
 * Pour le développeur de l'interface graphique : cette classe est le moteur derrière la
 * section "Avis des spectateurs" de vos panneaux.
 * - Quand un utilisateur clique sur "Valider" dans la fenêtre `EvaluationDialog`, c'est la
 *   méthode `addEvaluation` qui sera appelée en coulisses.
 * - Pour afficher les avis sur la page d'un film, vous utiliserez `getEvaluationsByFilmId`.
 * - Pour savoir s'il faut activer ou désactiver le bouton "Donner une note", vous utiliserez
 *   `getEvaluationByClientAndFilm` pour vérifier si un avis existe déjà.
 */
public class EvaluationClientDAOImpl extends GenericDAOImpl<EvaluationClient> implements EvaluationClientDAO {

    public EvaluationClientDAOImpl() {
        super("evaluations_client.dat");
    }

    @Override
    public void addEvaluation(EvaluationClient evaluation) {
        this.data.add(evaluation);
        saveToFile();
    }
    
    
     // --- NOUVELLE MÉTHODE À IMPLÉMENTER ---
    public void updateEvaluation(EvaluationClient updatedEvaluation) {
        // On parcourt la liste pour trouver l'évaluation à remplacer.
        for (int i = 0; i < this.data.size(); i++) {
            EvaluationClient eval = this.data.get(i);
            // La clé unique d'une évaluation est la combinaison client + film.
            if (eval.getIdClient() == updatedEvaluation.getIdClient() && eval.getIdFilm() == updatedEvaluation.getIdFilm()) {
                // On remplace l'ancien objet par le nouveau à la même position.
                this.data.set(i, updatedEvaluation);
                // On sauvegarde immédiatement les changements.
                saveToFile();
                // La mission est accomplie, on peut quitter la méthode.
                return;
            }
        }
    }

    @Override
    public List<EvaluationClient> getEvaluationsByFilmId(int filmId) {
        List<EvaluationClient> resultat = new ArrayList<>();
        // On parcourt tous les avis pour ne retourner que ceux concernant un film spécifique.
        for (EvaluationClient eval : this.data) {
            if (eval.getIdFilm() == filmId) {
                resultat.add(eval);
            }
        }
        return resultat;
    }

    @Override
    public Optional<EvaluationClient> getEvaluationByClientAndFilm(int clientId, int filmId) {
        // On cherche l'unique avis posté par un client pour un film donné.
        for (EvaluationClient eval : this.data) {
            if (eval.getIdClient() == clientId && eval.getIdFilm() == filmId) {
                // Si on le trouve, on le retourne dans une "boîte" Optional.
                return Optional.of(eval);
            }
        }
        // Sinon, on retourne une "boîte vide".
        return Optional.empty();
    }

    @Override
    public List<EvaluationClient> getAllEvaluations() {
        return new ArrayList<>(this.data);
    }
}