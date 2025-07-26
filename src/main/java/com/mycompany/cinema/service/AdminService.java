package com.mycompany.cinema.service;

import com.mycompany.cinema.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AdminService extends CinemaService { // Hérite des méthodes communes (getFilmsAffiche, getFilmDetails, findFilmsByTitre)

    // --- Authentification ---
    Optional<Personnel> authentifierPersonnel(String nomUtilisateur, String motDePasse);

    // =========================================================================
    // SECTION GESTION (CRUD - Create, Read, Update, Delete)
    // =========================================================================

    // --- Gestion des Films ---
    void ajouterFilm(Film film);
    void mettreAJourFilm(Film film);
    void supprimerFilm(int filmId) throws Exception;

    // --- Gestion des Séances ---
    void ajouterSeance(Seance seance) throws Exception; // throws Exception ajouté
    void modifierSeance(Seance seance) throws Exception; // throws Exception ajouté
    void supprimerSeance(int seanceId) throws Exception;
    List<Seance> getAllSeances(); // Utile pour les vues admin

    // --- Gestion des Salles ---
    void ajouterSalle(Salle salle);
    void modifierSalle(Salle salle);
    void supprimerSalle(int salleId) throws Exception;
    List<Salle> getAllSalles(); // Utile pour les vues admin

    // --- Gestion des Tarifs ---
    void ajouterTarif(Tarif tarif) throws Exception; // throws Exception ajouté
    void modifierTarif(Tarif tarif) throws Exception; // throws Exception ajouté
    void supprimerTarif(int tarifId);
    List<Tarif> getAllTarifs(); // Utile pour les vues admin

    // --- Gestion du Personnel et Planning ---
    void ajouterPersonnel(Personnel personnel);
    void modifierPersonnel(Personnel personnel);
    void supprimerPersonnel(int personnelId);
    void affecterPersonnelASeance(int personnelId, int seanceId) throws Exception;
    void desaffecterPersonnelDeSeance(int personnelId, int seanceId) throws Exception;
    Planning creerPlanning(int personnelId, LocalDateTime debut, LocalDateTime fin, String poste) throws Exception;
    List<Planning> getPlanningPourPersonnel(int personnelId);
    List<Personnel> getAllPersonnel(); // Utile pour les vues admin
    List<Role> getAllRoles(); 

    // =========================================================================
    // SECTION REPORTING (Suivi des Ventes)
    // =========================================================================

    // --- Ventes Cinéma ---
    List<Billet> getBilletsPourFilm(int filmId);
    double calculerChiffreAffairesPourFilm(int filmId);
    double calculerChiffreAffairesPourJour(LocalDate date);
    double calculerChiffreAffairesReservationsPourSeance(int seanceId);
    List<Reservation> getAllReservations(); // MÉTHODE AJOUTÉE

    // --- Ventes Snacking ---
    List<VenteSnack> getVentesSnackParJour(LocalDate date);
    double calculerChiffreAffairesSnackPourJour(LocalDate date);
    List<VenteSnack> getAllVentesSnack(); // MÉTHODE AJOUTÉE

}