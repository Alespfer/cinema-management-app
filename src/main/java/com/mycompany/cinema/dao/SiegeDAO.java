// ========================================================================
// FICHIER : SiegeDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Siege;
import java.util.List;

/**
 * Définit le contrat pour la gestion des sièges.
 * Sa fonction principale est de lister tous les sièges d'une salle donnée.
 */
public interface SiegeDAO {

    void ajouterSiege(Siege siege);
    List<Siege> trouverSiegesParIdSalle(int idSalle);
    List<Siege> trouverTousLesSieges();
    void rechargerDonnees();
}