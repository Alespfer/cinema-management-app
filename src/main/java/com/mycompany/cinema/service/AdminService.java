/*
 * AdminService.java
 * Définit le contrat pour toutes les fonctionnalités de l'administrateur.
 * Hérite de CinemaService pour les méthodes de consultation communes.
 */
package com.mycompany.cinema.service;

import com.mycompany.cinema.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AdminService extends CinemaService {

    // --- Authentification & Gestion du Personnel ---
    Personnel authentifierPersonnel(String nomUtilisateur, String motDePasse);

    Personnel trouverPersonnelParEmail(String email);

    void changerMotDePassePersonnel(int personnelId, String nouveauMotDePasse) throws Exception;

    void ajouterPersonnel(Personnel personnel);

    void modifierPersonnel(Personnel personnel);

    void supprimerPersonnel(int personnelId);

    List<Personnel> trouverToutLePersonnel();

    List<Role> trouverTousLesRoles();

    // --- Gestion du Planning ---
    Planning creerPlanning(int personnelId, LocalDateTime debut, LocalDateTime fin, String poste) throws Exception;

    List<Planning> trouverPlanningPourPersonnel(int personnelId);

    // void affecterPersonnelASeance(int personnelId, int seanceId) throws Exception;
    // void desaffecterPersonnelDeSeance(int personnelId, int seanceId) throws Exception;
    
    
    // --- Gestion des Films, Genres, Salles, Tarifs ---
    
    void ajouterFilm(Film film) throws Exception;

    void mettreAJourFilm(Film film) throws Exception;

    void supprimerFilm(int filmId) throws Exception;

    void ajouterGenre(Genre genre);

    void modifierGenre(Genre genre);

    void supprimerGenre(int genreId) throws Exception;

    List<Genre> trouverTousLesGenres();

    void ajouterSalleAvecPlan(Salle salle, int nbRangees, int nbSiegesParRangee) throws Exception;

    public void modifierSalle(Salle salle) throws Exception;

    void supprimerSalle(int salleId) throws Exception;

    List<Salle> trouverToutesLesSalles();

    void ajouterTarif(Tarif tarif) throws Exception;

    void modifierTarif(Tarif tarif) throws Exception;

    void supprimerTarif(int tarifId);

    List<Tarif> trouverTousLesTarifs();

    // --- Gestion des Séances ---
    
    void ajouterSeance(Seance seance) throws Exception;

    void modifierSeance(Seance seance) throws Exception;

    void supprimerSeance(int seanceId) throws Exception;

    List<Seance> trouverToutesLesSeances();

    // --- Gestion du Snacking ---
    
    List<ProduitSnack> trouverTousLesProduits();

    void ajouterProduitSnack(ProduitSnack produit) throws Exception;

    void modifierProduitSnack(ProduitSnack produit) throws Exception;

    void supprimerProduitSnack(int produitId) throws Exception;

    VenteSnack enregistrerVenteSnack(int idPersonnel, int idCaisse, List<LignePanier> panier) throws Exception;

    void ajusterStockProduit(int produitId, int quantiteAjustement) throws Exception;

    // --- Rapports de Ventes (Reporting) ---
    
    double calculerChiffreAffairesPourFilm(int filmId);

    double calculerChiffreAffairesPourJour(LocalDate date);

    double calculerChiffreAffairesSnackPourJour(LocalDate date);

    List<VenteSnack> trouverVentesSnackParDate(LocalDate date);

    double calculerChiffreAffairesReservationsPourSeance(int seanceId);

    List<Reservation> trouverToutesLesReservations();

    List<VenteSnack> trouverToutesLesVentesSnack();

    double calculerTotalPourVenteSnack(VenteSnack vente);

    // --- Méthodes de support pour les Vues Admin ---
    
    Client trouverClientParId(int clientId);

    List<Billet> trouverBilletsParIdReservation(int reservationId);

    Seance trouverSeanceParId(int seanceId);

    public Personnel trouverPersonnelParId(int idPersonnel);

    Caisse trouverCaisseParId(int caisseId);

    Tarif trouverTarifParId(int tarifId);

    List<Billet> trouverBilletsPourFilm(int filmId);

    /**
     * Force le service à recharger toutes ses données depuis les fichiers.
     */
    void rechargerTouteLaBase();

}
