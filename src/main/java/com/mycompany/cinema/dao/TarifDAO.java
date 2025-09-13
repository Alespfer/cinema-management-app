// ========================================================================
// TarifDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Tarif;
import java.util.List;

/**
 * Définit le contrat pour la gestion des tarifs disponibles.
 */
public interface TarifDAO {

    void ajouterTarif(Tarif tarif);
    Tarif trouverTarifParId(int id);
    List<Tarif> trouverTousLesTarifs();
    void mettreAJourTarif(Tarif tarif);
    void supprimerTarifParId(int id);
    void rechargerDonnees();
}