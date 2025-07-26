package com.mycompany.cinema.service;

import com.mycompany.cinema.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Contrat de service global pour l'application Cinéma.
 * C'est le point d'entrée unique pour toute la logique métier.
 * L'interface graphique (ou toute autre forme de client) ne communiquera
 * qu'avec cette interface.
 */
public interface CinemaService {

    // =========================================================================
    // SECTION UTILISATEUR
    // =========================================================================

    // --- Compte Client ---
    Client creerCompteClient(String nom, String email, String motDePasse) throws Exception;
    Optional<Client> authentifierClient(String email, String motDePasse);
    
    // --- Consultation ---
    List<Film> getFilmsAffiche();
    Film getFilmDetails(int filmId);
    List<Seance> getSeancesPourFilmEtDate(int filmId, LocalDate date);
    List<Siege> getSiegesPourSalle(int salleId);
    List<Billet> getBilletsPourSeance(int seanceId); // Pour connaître les sièges occupés
    List<Siege> getSiegesDisponibles(int seanceId);

    // --- Transactionnel ---
    Reservation effectuerReservation(int clientId, int seanceId, List<Integer> siegeIds, int tarifId) throws Exception;
    void annulerReservation(int reservationId) throws Exception;
    List<Reservation> getHistoriqueReservationsClient(int clientId);


    // =========================================================================
    // SECTION ADMINISTRATEUR
    // =========================================================================

    // --- Gestion des Films ---
    void ajouterFilm(Film film);
    void mettreAJourFilm(Film film);
    void supprimerFilm(int filmId) throws Exception;

    // --- Gestion des Séances ---
    void ajouterSeance(Seance seance);
    void modifierSeance(Seance seance);
    void supprimerSeance(int seanceId) throws Exception;

    // --- Gestion des Salles ---
    void ajouterSalle(Salle salle);
    void modifierSalle(Salle salle);
    void supprimerSalle(int salleId) throws Exception;

    // --- Gestion des Tarifs ---
    void ajouterTarif(Tarif tarif);
    void modifierTarif(Tarif tarif);
    void supprimerTarif(int tarifId);

    // --- Gestion du Personnel ---
    void ajouterPersonnel(Personnel personnel);
    void modifierPersonnel(Personnel personnel);
    void supprimerPersonnel(int personnelId);

    // --- Suivi des Ventes ---
    List<VenteSnack> getVentesSnackParJour(LocalDate date);
    double calculerChiffreAffairesReservationsPourSeance(int seanceId);
}