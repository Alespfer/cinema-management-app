// ========================================================================
// FICHIER : SalleDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Salle;
import java.util.List;

/**
 * Définit le contrat pour la gestion des salles de projection.
 */
public interface SalleDAO {

    void ajouterSalle(Salle salle);
    Salle trouverSalleParId(int id);
    List<Salle> trouverToutesLesSalles();
    void mettreAJourSalle(Salle salle);
    void supprimerSalleParId(int id);
    void rechargerDonnees();
}