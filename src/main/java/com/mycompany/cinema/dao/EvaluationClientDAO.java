package com.mycompany.cinema.dao;

import com.mycompany.cinema.EvaluationClient;
import java.util.List;
import java.util.Optional;

public interface EvaluationClientDAO {
    void addEvaluation(EvaluationClient evaluation);
    List<EvaluationClient> getEvaluationsByFilmId(int filmId);
    Optional<EvaluationClient> getEvaluationByClientAndFilm(int clientId, int filmId);
    List<EvaluationClient> getAllEvaluations();
    
    void rechargerDonnees();

}