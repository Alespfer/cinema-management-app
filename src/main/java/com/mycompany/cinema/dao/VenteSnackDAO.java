// ========================================================================
// FICHIER : VenteSnackDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.VenteSnack;
import java.time.LocalDate;
import java.util.List;

/**
 * Définit le contrat pour la gestion des tickets de caisse de snacks.
 */
public interface VenteSnackDAO {

    void ajouterVenteSnack(VenteSnack vente);
    VenteSnack trouverVenteSnackParId(int id);
    VenteSnack trouverVenteParIdReservation(int idReservation);
    List<VenteSnack> trouverToutesLesVentesSnack();
    List<VenteSnack> trouverVentesParDate(LocalDate date);
    void mettreAJourVenteSnack(VenteSnack vente); 
    void rechargerDonnees();
}