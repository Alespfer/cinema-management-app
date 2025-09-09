// ========================================================================
// FICHIER : LigneVenteDAO.java 
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.LigneVente;
import java.util.List;

/**
 * Définit le contrat pour la gestion des lignes de détail d'une vente de snacks.
 */
public interface LigneVenteDAO {
    
    // Enregistre une nouvelle ligne de vente.
    void ajouterLigneVente(LigneVente ligneVente);

    // Retrouve toutes les lignes de vente d'un ticket de caisse donné.
    List<LigneVente> trouverLignesParIdVente(int idVente);

    // Retrouve la totalité des lignes de vente enregistrées.
    List<LigneVente> trouverToutesLesLignesVente();
    
    // Force le rechargement des données depuis la source.
    void rechargerDonnees();
}