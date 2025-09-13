// ========================================================================
// PersonnelDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Personnel;
import java.util.List;

/**
 * Définit le contrat pour la gestion des données des employés du cinéma.
 */
public interface PersonnelDAO {

    void ajouterPersonnel(Personnel personnel);
    Personnel trouverPersonnelParId(int id);
    List<Personnel> trouverToutLePersonnel();
    void mettreAJourPersonnel(Personnel personnel);
    void supprimerPersonnelParId(int id);
    void rechargerDonnees();
}