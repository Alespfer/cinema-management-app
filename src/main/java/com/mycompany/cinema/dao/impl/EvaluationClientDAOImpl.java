package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.EvaluationClient;
import com.mycompany.cinema.dao.EvaluationClientDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EvaluationClientDAOImpl extends GenericDAOImpl<EvaluationClient> implements EvaluationClientDAO {

    public EvaluationClientDAOImpl() {
        super("evaluations_client.dat");
    }

    @Override
    public void addEvaluation(EvaluationClient evaluation) {
        this.data.add(evaluation);
        saveToFile();
    }

    @Override
    public List<EvaluationClient> getEvaluationsByFilmId(int filmId) {
        List<EvaluationClient> resultat = new ArrayList<>();
        for (EvaluationClient eval : this.data) {
            if (eval.getIdFilm() == filmId) {
                resultat.add(eval);
            }
        }
        return resultat;
    }

    @Override
    public Optional<EvaluationClient> getEvaluationByClientAndFilm(int clientId, int filmId) {
        for (EvaluationClient eval : this.data) {
            if (eval.getIdClient() == clientId && eval.getIdFilm() == filmId) {
                return Optional.of(eval);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<EvaluationClient> getAllEvaluations() {
        return new ArrayList<>(this.data);
    }
}