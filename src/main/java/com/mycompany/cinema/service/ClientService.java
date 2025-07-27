package com.mycompany.cinema.service;

import com.mycompany.cinema.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface regroupant toutes les fonctionnalités accessibles aux clients.
 */
public interface ClientService extends CinemaService {

    // --- Compte Client ---

    /** Crée un compte client avec les informations fournies.
     * @throws Exception si les données sont invalides ou l’email déjà pris.
     */
    Client creerCompteClient(String nom, String email, String motDePasse) throws Exception;

    /** Authentifie un client à partir de son email et mot de passe.
     * @return un Optional contenant le client si trouvé, vide sinon.
     */
    Optional<Client> authentifierClient(String email, String motDePasse);

    /** Modifie les informations d’un compte client.
     * @throws Exception si l’opération échoue (ex : conflit).
     */
    void modifierCompteClient(Client client) throws Exception;

    /** Supprime un compte client.
     * @throws Exception si des réservations sont encore liées.
     */
    void supprimerCompteClient(int clientId) throws Exception;

    // --- Consultation ---

    /** Retourne les séances proposées pour un film donné à une date précise. */
    List<Seance> getSeancesPourFilmEtDate(int filmId, LocalDate date);

    /** Retourne les séances selon des critères multiples (tous optionnels).
     * @param filmId identifiant du film ou null
     * @param salleId identifiant de la salle ou null
     */
    List<Seance> findSeancesFiltrees(LocalDate date, Integer filmId, Integer salleId);

    /** Retourne tous les sièges d’une salle donnée. */
    List<Siege> getSiegesPourSalle(int salleId);

    /** Retourne les billets déjà réservés pour une séance. */
    List<Billet> getBilletsPourSeance(int seanceId);

    /** Retourne les sièges disponibles pour une séance. */
    List<Siege> getSiegesDisponibles(int seanceId);
    
    /** Retourne la liste de tous les tarifs disponibles (Plein, Étudiant, etc.). */
    List<Tarif> getAllTarifs();

     /** Retourne la liste de tous les genres disponibles. */
    List<Genre> getAllGenres();
    
    List<Salle> getAllSalles();

    /**
     * Recherche les séances selon des critères combinés. Les paramètres null sont ignorés.
     * @param date La date souhaitée (peut être null).
     * @param genreId L'ID du genre souhaité (peut être null).
     * @param titreKeyword Un mot-clé à rechercher dans le titre du film (peut être null).
     * @return Une liste de séances correspondant aux filtres.
     */
    List<Seance> rechercherSeances(LocalDate date, Integer genreId, String titreKeyword);


    // --- Transactionnel ---

    /** Effectue une réservation pour un client.
     * @throws Exception si certains sièges sont déjà réservés.
     */
    Reservation effectuerReservation(int clientId, int seanceId, List<Integer> siegeIds, int tarifId) throws Exception;

    /** Annule une réservation existante.
     * @throws Exception si la réservation n’existe pas.
     */
    void annulerReservation(int reservationId) throws Exception;

    /** Retourne l’historique des réservations du client. */
    List<Reservation> getHistoriqueReservationsClient(int clientId);


 
}
