// ========================================================================
// EvaluationClientDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.EvaluationClient;
import com.mycompany.cinema.dao.EvaluationClientDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation pour la gestion des avis clients. S'occupe de la persistance
 * des objets EvaluationClient dans le fichier "evaluations_client.dat".
 *
 */
public class EvaluationClientDAOImpl extends GenericDAOImpl<EvaluationClient> implements EvaluationClientDAO {

    public EvaluationClientDAOImpl() {
        super("evaluations_client.dat");
    }

    /**
     * Ajoute une nouvelle évaluation.
     *
     * @param evaluation L'objet EvaluationClient à sauvegarder.
     */
    @Override
    public void ajouterEvaluation(EvaluationClient evaluation) {
        this.data.add(evaluation);
        sauvegarderDansFichier();
    }

    /**
     * Met à jour une évaluation existante. Une évaluation est identifiée de
     * manière unique par la paire (idClient, idFilm).
     *
     * @param evaluationMiseAJour L'objet EvaluationClient contenant les
     * nouvelles données.
     */
    @Override
    public void mettreAJourEvaluation(EvaluationClient evaluationMiseAJour) {
        for (int i = 0; i < this.data.size(); i++) {
            EvaluationClient evaluationActuelle = this.data.get(i);
            // La clé unique est la combinaison de l'ID du client et de l'ID du film.
            if (evaluationActuelle.getIdClient() == evaluationMiseAJour.getIdClient()
                    && evaluationActuelle.getIdFilm() == evaluationMiseAJour.getIdFilm()) {

                this.data.set(i, evaluationMiseAJour);
                sauvegarderDansFichier();
                return;
            }
        }
    }
    
    /**
     * Récupère toutes les évaluations postées pour un film spécifique.
     * @param idFilm L'identifiant du film.
     * @return Une liste d'évaluations, vide si aucune n'est trouvée.
     */
    @Override
    public List<EvaluationClient> trouverEvaluationsParIdFilm(int idFilm) {
        List<EvaluationClient> evaluationsTrouvees = new ArrayList<>();
        for (EvaluationClient evaluation : this.data) {
            if (evaluation.getIdFilm() == idFilm) {
                evaluationsTrouvees.add(evaluation);
            }
        }
        return evaluationsTrouvees;
    }

    /**
     * Récupère l'évaluation unique d'un client pour un film donné. Elle permet de
     * vérifier si un client a déjà posté un avis pour un film.
     *
     * @param idClient L'identifiant du client.
     * @param idFilm L'identifiant du film.
     * @return L'objet EvaluationClient s'il existe, sinon `null`.
     */
    @Override
    public EvaluationClient trouverEvaluationParClientEtFilm(int idClient, int idFilm) {
        for (EvaluationClient evaluation : this.data) {
            if (evaluation.getIdClient() == idClient && evaluation.getIdFilm() == idFilm) {
                return evaluation;
            }
        }
        return null;
    }

    /**
     * Retourne la liste de toutes les évaluations du système.
     * @return Une copie de la liste pour la sécurité des données.
     */
    @Override
    public List<EvaluationClient> trouverToutesLesEvaluations() {
        return new ArrayList<>(this.data);
    }
}
