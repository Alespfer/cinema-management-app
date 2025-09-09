// ========================================================================
// FICHIER : SeanceDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Seance;
import java.time.LocalDate;
import java.util.List;

/**
 * Définit le contrat pour la gestion des séances (projections de films).
 * C'est un contrat central pour l'interface client.
 */
public interface SeanceDAO {
    
    void ajouterSeance(Seance seance);
    Seance trouverSeanceParId(int id);
    List<Seance> trouverToutesLesSeances();
    List<Seance> trouverSeancesParIdFilm(int idFilm);
    List<Seance> trouverSeancesParDate(LocalDate date);
    void mettreAJourSeance(Seance seance);
    void supprimerSeanceParId(int id);
    void rechargerDonnees();
}