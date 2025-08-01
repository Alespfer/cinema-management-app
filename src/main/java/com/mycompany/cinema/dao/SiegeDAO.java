// Fichier : SiegeDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Siege;
import java.util.List;

/**
 * Définit le contrat pour la gestion des sièges.
 * Sa fonction la plus importante est de pouvoir lister tous les sièges d'une salle.
 * 
 * L'interface graphique, via `SiegePanel`, dépend entièrement de `getSiegesBySalleId`
 * pour pouvoir construire le plan de la salle et afficher les sièges.
 */
public interface SiegeDAO {
    List<Siege> getSiegesBySalleId(int salleId);
    List<Siege> getAllSieges();
    void addSiege(Siege siege);
    void rechargerDonnees();
}