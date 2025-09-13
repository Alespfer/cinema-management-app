/*
 * ClientService.java
 * Définit le contrat pour toutes les fonctionnalités accessibles à un client authentifié.
 * Elle hérite de CinemaService pour inclure les méthodes de consultation de base.
 */

package com.mycompany.cinema.service;

import com.mycompany.cinema.*;
import com.mycompany.cinema.LignePanier;
import java.time.LocalDate;
import java.util.List;


public interface ClientService extends CinemaService {

    // --- Gestion du Compte Client ---
    
    
    Client creerCompteClient(String nom, String email, String motDePasse) throws Exception;
    Client authentifierClient(String email, String motDePasse);
    Client trouverClientParEmail(String email);
    void modifierCompteClient(Client client) throws Exception;
    void supprimerCompteClient(int clientId) throws Exception;
    void changerMotDePasseClient(int clientId, String nouveauMotDePasse) throws Exception;

    // --- Consultation des informations ---
    
    
    List<Seance> trouverSeancesPourFilmEtDate(int filmId, LocalDate date);
    List<Seance> trouverSeancesFiltrees(LocalDate date, Integer filmId, Integer salleId);
    List<Siege> trouverSiegesPourSalle(int salleId);
    List<Billet> trouverBilletsPourSeance(int seanceId);
    List<Siege> trouverSiegesDisponibles(int seanceId);
    List<Tarif> trouverTousLesTarifs();
    List<Genre> trouverTousLesGenres();
    List<Salle> trouverToutesLesSalles();
    List<ProduitSnack> trouverTousLesProduits();
    List<Seance> rechercherSeances(LocalDate date, Integer genreId, String titreKeyword);
    VenteSnack trouverVenteSnackReservation(int reservationId);
    List<LigneVente> trouverLignesParIdVente(int venteId);
    Client trouverClientParId(int clientId);
    List<Billet> trouverBilletsParIdReservation(int reservationId);
    Seance trouverSeanceParId(int seanceId);
  
    // --- Processus de réservation et historique ---

    Reservation finaliserCommandeComplete(int clientId, int seanceId, List<Integer> siegeIds, int tarifId, List<LignePanier> panierSnacks) throws Exception;
    void annulerReservation(int reservationId) throws Exception;
    List<Reservation> trouverHistoriqueReservationsClient(int clientId);
    Reservation effectuerReservation(int clientId, int seanceId, List<Integer> siegeIds, int tarifId) throws Exception;

    
    // --- Gestion des évaluations ---

    void ajouterEvaluation(EvaluationClient evaluation) throws Exception;
    void modifierEvaluation(EvaluationClient evaluation) throws Exception; 
    boolean aDejaEvalue(int clientId, int filmId);
    List<EvaluationClient> trouverEvaluationsParFilmId(int filmId);
    EvaluationClient trouverEvaluation(int clientId, int filmId);


    // --- Méthodes de service ---
     /**
     * Force le service à recharger toutes ses données depuis les fichiers.
     */
    void rechargerTouteLaBase();

}
