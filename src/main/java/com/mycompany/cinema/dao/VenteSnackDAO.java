// Fichier : VenteSnackDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.VenteSnack;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Définit le contrat pour la gestion des tickets de caisse de snacks.
 * 
 * Ce contrat est principalement utilisé par le `ReportingPanel` de l'admin
 * pour afficher l'historique des ventes (`getAllVentesSnack`) et pour
 * filtrer les ventes par jour (`getVentesByDate`).
 */
public interface VenteSnackDAO {
    void addVenteSnack(VenteSnack vente);
    VenteSnack getVenteSnackById(int id);
    List<VenteSnack> getAllVentesSnack();
    List<VenteSnack> getVentesByDate(LocalDate date);
    VenteSnack getVenteByReservationId(int reservationId);
    void updateVenteSnack(VenteSnack vente); // <-- AJOUTEZ CETTE LIGNE
    void rechargerDonnees();


}