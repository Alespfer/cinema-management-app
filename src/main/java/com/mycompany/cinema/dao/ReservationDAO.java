// Fichier : ReservationDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Reservation;
import java.util.List;
import java.util.Optional;

/**
 * Définit le contrat pour la gestion des commandes (réservations) des clients.
 * 
 * L'interface graphique client s'appuie fortement sur ce contrat, notamment
 * le panneau `HistoriqueReservationsPanel` qui utilise `getReservationsByClientId`
 * pour afficher toutes les commandes passées par l'utilisateur connecté.
 */
public interface ReservationDAO {
    void addReservation(Reservation reservation);
    Reservation getReservationById(int id);
    List<Reservation> getAllReservations();
    List<Reservation> getReservationsByClientId(int clientID);
    void deleteReservation(int id);
    void rechargerDonnees();
}