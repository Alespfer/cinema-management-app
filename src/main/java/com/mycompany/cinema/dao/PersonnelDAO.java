// Fichier : PersonnelDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Personnel;
import java.util.List;
import java.util.Optional;

/**
 * Définit le contrat pour la gestion des données des employés du cinéma.
 * 
 * Ce contrat est la base de toute la partie administrative de l'interface graphique.
 * - `getAllPersonnel` est utilisé pour la connexion (`LoginFrame`) et pour lister les employés
 *   dans `GestionPersonnelPanel`.
 * - Les autres méthodes (add, update, delete) sont les actions principales
 *   du panneau `GestionPersonnelPanel`.
 */
public interface PersonnelDAO {
    void addPersonnel(Personnel personnel);
    Personnel getPersonnelById(int id);
    List<Personnel> getAllPersonnel();
    void updatePersonnel(Personnel personnel);
    void deletePersonnel(int id);
    void rechargerDonnees();
}