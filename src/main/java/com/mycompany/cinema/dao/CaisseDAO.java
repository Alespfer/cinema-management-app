// ========================================================================
// CaisseDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Caisse;
import java.util.List;

/**
 * Définit le contrat pour la gestion des caisses enregistreuses (points de vente).
 * NB : Comme pour les affectations des séances, les fonctionnalités ont été développées mais non priorisées 
 * dans le rendu final
 */
public interface CaisseDAO {

    // Enregistre une nouvelle caisse.
    void ajouterCaisse(Caisse caisse);
    
    // Recherche une caisse par son identifiant.
    Caisse trouverCaisseParId(int id);

    // Retrouve la liste de toutes les caisses.
    List<Caisse> trouverToutesLesCaisses();
    
    // Met à jour les informations d'une caisse.

    void mettreAJourCaisse(Caisse caisse);
    
    // Supprime une caisse à partir de son identifiant.

    void supprimerCaisseParId(int id);
    
    // Force le rechargement des données depuis la source.

    void rechargerDonnees();
}