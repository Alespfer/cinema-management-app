package com.mycompany.cinema.service;

import com.mycompany.cinema.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface des fonctionnalités administrateur.
 * Hérite des méthodes de consultation commune via CinemaService.
 */
public interface AdminService extends CinemaService {

    // --- Authentification ---

    /**
     * Authentifie un membre du personnel via nom d'utilisateur et mot de passe.
     * @return Un Optional contenant le Personnel si authentifié, sinon vide.
     */
    Optional<Personnel> authentifierPersonnel(String nomUtilisateur, String motDePasse);

    // =========================================================================
    // SECTION GESTION (CRUD - Create, Read, Update, Delete)
    // =========================================================================

    // --- Gestion des Films ---

    /** Ajoute un nouveau film au catalogue. */
    void ajouterFilm(Film film);

    /** Met à jour les informations d’un film existant. */
    void mettreAJourFilm(Film film);

    /** Supprime un film à partir de son ID.
     * @throws Exception si le film est encore référencé dans une séance.
     */
    void supprimerFilm(int filmId) throws Exception;

    // --- Gestion des Séances ---

    /** Ajoute une nouvelle séance.
     * @throws Exception si les paramètres sont incohérents.
     */
    void ajouterSeance(Seance seance) throws Exception;

    /** Modifie une séance existante.
     * @throws Exception en cas de problème de données.
     */
    void modifierSeance(Seance seance) throws Exception;

    /** Supprime une séance existante.
     * @throws Exception si la séance est encore liée à des réservations.
     */
    void supprimerSeance(int seanceId) throws Exception;

    /** Retourne toutes les séances enregistrées. */
    List<Seance> getAllSeances();

    // --- Gestion des Salles ---

    /** Ajoute une nouvelle salle de projection. */
    void ajouterSalle(Salle salle);

    /** Modifie les données d’une salle existante. */
    void modifierSalle(Salle salle);

    /** Supprime une salle.
     * @throws Exception si la salle est utilisée dans des séances.
     */
    void supprimerSalle(int salleId) throws Exception;

    /** Retourne toutes les salles existantes. */
    List<Salle> getAllSalles();

    // --- Gestion des Tarifs ---

    /** Ajoute un nouveau tarif.
     * @throws Exception si un tarif du même type existe déjà.
     */
    void ajouterTarif(Tarif tarif) throws Exception;

    /** Met à jour un tarif existant.
     * @throws Exception en cas d’erreur sur les données.
     */
    void modifierTarif(Tarif tarif) throws Exception;

    /** Supprime un tarif existant. */
    void supprimerTarif(int tarifId);

    /** Retourne tous les tarifs existants. */
    List<Tarif> getAllTarifs();

    // --- Gestion du Personnel et Planning ---

    /** Ajoute un membre du personnel. */
    void ajouterPersonnel(Personnel personnel);

    /** Met à jour les informations du personnel. */
    void modifierPersonnel(Personnel personnel);

    /** Supprime un membre du personnel. */
    void supprimerPersonnel(int personnelId);

    /** Affecte un membre du personnel à une séance.
     * @throws Exception si le personnel est déjà affecté ailleurs.
     */
    void affecterPersonnelASeance(int personnelId, int seanceId) throws Exception;

    /** Supprime une affectation entre un personnel et une séance.
     * @throws Exception si l’affectation n’existe pas.
     */
    void desaffecterPersonnelDeSeance(int personnelId, int seanceId) throws Exception;

    /** Crée un créneau de planning pour un membre du personnel.
     * @throws Exception si chevauchement de créneaux.
     */
    Planning creerPlanning(int personnelId, LocalDateTime debut, LocalDateTime fin, String poste) throws Exception;

    /** Retourne le planning d’un membre du personnel. */
    List<Planning> getPlanningPourPersonnel(int personnelId);

    /** Retourne tous les membres du personnel. */
    List<Personnel> getAllPersonnel();

    /** Retourne tous les rôles possibles pour le personnel. */
    List<Role> getAllRoles();

    // =========================================================================
    // SECTION REPORTING (Suivi des Ventes)
    // =========================================================================

    // --- Ventes Cinéma ---

    /** Retourne tous les billets associés à un film. */
    List<Billet> getBilletsPourFilm(int filmId);

    /** Calcule le chiffre d’affaires généré par un film. */
    double calculerChiffreAffairesPourFilm(int filmId);

    /** Calcule le chiffre d’affaires total pour un jour donné. */
    double calculerChiffreAffairesPourJour(LocalDate date);

    /** Calcule les recettes issues des réservations pour une séance. */
    double calculerChiffreAffairesReservationsPourSeance(int seanceId);

    /** Retourne toutes les réservations enregistrées. */
    List<Reservation> getAllReservations();

    // --- Ventes Snacking ---

    /** Retourne les ventes snack pour une journée. */
    List<VenteSnack> getVentesSnackParJour(LocalDate date);

    /** Calcule les ventes snack pour un jour donné. */
    double calculerChiffreAffairesSnackPourJour(LocalDate date);

    /** Retourne toutes les ventes snack enregistrées. */
    List<VenteSnack> getAllVentesSnack();
}
