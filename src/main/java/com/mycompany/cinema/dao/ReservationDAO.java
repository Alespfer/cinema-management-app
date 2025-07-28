package com.mycompany.cinema.dao;

import com.mycompany.cinema.Reservation;
import java.util.List;
import java.util.Optional;

/**
 * Contrat pour la gestion de la persistance des Réservations.
 * La recherche par client est une fonctionnalité essentielle.
 */
public interface ReservationDAO {
    void addReservation(Reservation reservation);
    Optional<Reservation> getReservationById(int id);
    List<Reservation> getAllReservations();
    List<Reservation> getReservationsByClientId(int clientID);
    void deleteReservation(int id);
    
    
    void rechargerDonnees();

}