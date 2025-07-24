package com.mycompany.cinema.dao;

import com.mycompany.cinema.Billet;
import java.util.List;

/**
 * Contrat pour la gestion de la persistance des Billets.
 * Les billets sont presque toujours liés à une réservation.
 */
public interface BilletDAO {
    void addBillet(Billet billet);
    List<Billet> getBilletsByReservationId(int reservationId); // La méthode la plus utile
    List<Billet> getAllBillets();
    void deleteBilletsByReservationId(int reservationId);
}