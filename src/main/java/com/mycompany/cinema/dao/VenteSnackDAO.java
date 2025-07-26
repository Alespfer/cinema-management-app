package com.mycompany.cinema.dao;

import com.mycompany.cinema.VenteSnack;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Contrat pour la gestion de la persistance des Ventes de Snack.
 */
public interface VenteSnackDAO {
    void addVenteSnack(VenteSnack vente);
    Optional<VenteSnack> getVenteSnackById(int id);
    List<VenteSnack> getAllVentesSnack();
    List<VenteSnack> getVentesByDate(LocalDate date);
}