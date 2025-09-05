// Fichier : TarifDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Tarif;
import java.util.List;
import java.util.Optional;

/**
 * Définit le contrat pour la gestion des tarifs (Plein, Étudiant...).
 * 
 * L'interface client (`SiegePanel`) utilise `getAllTarifs` pour peupler le menu
 * déroulant de sélection du tarif. L'interface admin (`GestionTarifsPanel`)
 * utilise toutes les méthodes CRUD pour gérer le catalogue de tarifs.
 */
public interface TarifDAO {
    void addTarif(Tarif tarif);
    Tarif getTarifById(int id);
    List<Tarif> getAllTarifs();
    void updateTarif(Tarif tarif);
    void deleteTarif(int id);
    void rechargerDonnees();
}
