package com.mycompany.cinema.service;

import com.mycompany.cinema.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CinemaService {
    // --- Fonctions Côté Utilisateur ---
    List<Film> getFilmsAffiche();
    Film getFilmDetails(int filmId);
    List<Seance> getSeancesPourFilmEtDate(int filmId, LocalDate date);
    List<Siege> getSiegesPourSalle(int salleId);
    List<Billet> getBilletsPourSeance(int seanceId); // Pour connaître les sièges occupés
    Reservation effectuerReservation(int clientId, int seanceId, List<Integer> siegeIds, int tarifId) throws Exception;
    void annulerReservation(int reservationId) throws Exception;
    List<Reservation> getHistoriqueReservationsClient(int clientId);

    // --- Fonctions Côté Admin ---
    void ajouterFilm(Film film);
    void mettreAJourFilm(Film film);
    void supprimerFilm(int filmId) throws Exception; // Logique métier : vérifier qu'aucune séance n'est prévue
    
    // ... Ajouter ici progressivement toutes les autres fonctionnalités métier (gestion du personnel, des stocks, etc.)
}