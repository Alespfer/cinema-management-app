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
    // === Section Gestion Snacking                       ===
    // =========================================================================
    
    /** Retourne la liste de tous les produits de snacking. */
    List<ProduitSnack> getAllProduitsSnack();

    /** Ajoute un nouveau produit de snacking au catalogue. */
    void ajouterProduitSnack(ProduitSnack produit) throws Exception;

    /** Met à jour les informations d'un produit de snacking existant. */
    void modifierProduitSnack(ProduitSnack produit) throws Exception;

    /** Supprime un produit de snacking du catalogue. */
    void supprimerProduitSnack(int produitId) throws Exception;


    
    
    /**
     * Enregistre une nouvelle vente de snacks dans le système.
     * @param idPersonnel L'ID de l'employé qui réalise la vente.
     * @param idCaisse L'ID de la caisse utilisée.
     * @param panier Une map contenant les ProduitsSnack et leur quantité vendue.
     * @return L'objet VenteSnack qui a été créé.
     * @throws Exception en cas de stock insuffisant ou autre erreur métier.
     */
    VenteSnack enregistrerVenteSnack(int idPersonnel, int idCaisse, java.util.Map<ProduitSnack, Integer> panier) throws Exception;
    
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
    
     /**
     * Calcule le montant total pour une seule vente de snack.
     * Nécessaire pour l'affichage du détail dans le tableau du ReportingPanel.
     * @param vente L'objet VenteSnack concerné.
     * @return Le montant total de cette transaction.
     */
    double calculerTotalPourVenteSnack(VenteSnack vente);

    
    // --- Méthodes de support pour les vues Admin ---

    
     /**
     * Récupère un client par son identifiant.
     * Nécessaire pour l'affichage des détails dans les rapports ou l'historique.
     * @param clientId L'ID du client.
     * @return Un Optional contenant le client.
     */
    Optional<Client> getClientById(int clientId);

    /**
     * Récupère tous les billets associés à un numéro de réservation spécifique.
     * Nécessaire pour l'historique des réservations.
     * @param reservationId L'ID de la réservation.
     * @return Une liste de billets.
     */
    List<Billet> getBilletsByReservationId(int reservationId);

    /**
     * Récupère une séance par son identifiant.
     * Utile pour obtenir les détails d'une séance à partir d'un billet.
     * @param seanceId L'ID de la séance.
     * @return Un Optional contenant la séance.
     */
    Optional<Seance> getSeanceById(int seanceId);

}
