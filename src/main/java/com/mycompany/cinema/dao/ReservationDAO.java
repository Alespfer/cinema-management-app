// ========================================================================
// ReservationDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Reservation;
import java.util.List;

/**
 * Définit le contrat pour la gestion des commandes (réservations) des clients.
 */
public interface ReservationDAO {

    void ajouterReservation(Reservation reservation);
    Reservation trouverReservationParId(int id);
    List<Reservation> trouverToutesLesReservations();
    List<Reservation> trouverReservationsParIdClient(int idClient);
    void supprimerReservationParId(int id);
    void rechargerDonnees();
}