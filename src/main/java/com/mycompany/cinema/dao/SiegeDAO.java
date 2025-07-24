package com.mycompany.cinema.dao;

import com.mycompany.cinema.Siege;
import java.util.List;

/**
 * Contrat pour la gestion de la persistance des Sièges.
 * La principale utilité est de récupérer les sièges d'une salle spécifique.
 */
public interface SiegeDAO {
    List<Siege> getSiegesBySalleId(int salleId);
    List<Siege> getAllSieges();
    // Le CRUD individuel sur les sièges est rarement nécessaire, mais l'interface le permet.
    void addSiege(Siege siege);
}