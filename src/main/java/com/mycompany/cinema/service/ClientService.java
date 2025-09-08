package com.mycompany.cinema.service;

import com.mycompany.cinema.*;
import com.mycompany.cinema.LignePanier;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Interface regroupant toutes les fonctionnalités accessibles aux clients.
 */
public interface ClientService extends CinemaService {

    // --- Compte Client ---
    /**
     * Crée un compte client avec les informations fournies.
     *
     * @throws Exception si les données sont invalides ou l’email déjà pris.
     */
    Client creerCompteClient(String nom, String email, String motDePasse) throws Exception;

    /**
     * Authentifie un client à partir de son email et mot de passe.
     *
     * @return un Optional contenant le client si trouvé, vide sinon.
     */
    Client authentifierClient(String email, String motDePasse);

    Client getClientByEmail(String email);

    /**
     * Modifie les informations d’un compte client.
     *
     * @throws Exception si l’opération échoue (ex : conflit).
     */
    void modifierCompteClient(Client client) throws Exception;

    /**
     * Supprime un compte client.
     *
     * @throws Exception si des réservations sont encore liées.
     */
    void supprimerCompteClient(int clientId) throws Exception;

    // --- Consultation ---
    /**
     * Retourne les séances proposées pour un film donné à une date précise.
     */
    List<Seance> getSeancesPourFilmEtDate(int filmId, LocalDate date);

    /**
     * Retourne les séances selon des critères multiples (tous optionnels).
     *
     * @param filmId identifiant du film ou null
     * @param salleId identifiant de la salle ou null
     */
    List<Seance> findSeancesFiltrees(LocalDate date, Integer filmId, Integer salleId);

    /**
     * Retourne tous les sièges d’une salle donnée.
     */
    List<Siege> getSiegesPourSalle(int salleId);

    /**
     * Retourne les billets déjà réservés pour une séance.
     */
    List<Billet> getBilletsPourSeance(int seanceId);

    /**
     * Retourne les sièges disponibles pour une séance.
     */
    List<Siege> getSiegesDisponibles(int seanceId);

    /**
     * Retourne la liste de tous les tarifs disponibles (Plein, Étudiant, etc.).
     */
    List<Tarif> getAllTarifs();

    /**
     * Retourne la liste de tous les genres disponibles.
     */
    List<Genre> getAllGenres();

    List<Salle> getAllSalles();

    /**
     * Recherche les séances selon des critères combinés. Les paramètres null
     * sont ignorés.
     *
     * @param date La date souhaitée (peut être null).
     * @param genreId L'ID du genre souhaité (peut être null).
     * @param titreKeyword Un mot-clé à rechercher dans le titre du film (peut
     * être null).
     * @return Une liste de séances correspondant aux filtres.
     */
    List<Seance> rechercherSeances(LocalDate date, Integer genreId, String titreKeyword);

    /**
     * Récupère un client par son identifiant. Nécessaire pour l'affichage des
     * détails dans les rapports ou l'historique.
     *
     * @param clientId L'ID du client.
     * @return Un Optional contenant le client.
     */
    Client getClientById(int clientId);

    /**
     * Récupère tous les billets associés à un numéro de réservation spécifique.
     * Nécessaire pour l'historique des réservations.
     *
     * @param reservationId L'ID de la réservation.
     * @return Une liste de billets.
     */
    List<Billet> getBilletsByReservationId(int reservationId);

    /**
     * Récupère une séance par son identifiant. Utile pour obtenir les détails
     * d'une séance à partir d'un billet.
     *
     * @param seanceId L'ID de la séance.
     * @return Un Optional contenant la séance.
     */
    Seance getSeanceById(int seanceId);

    List<ProduitSnack> getAllProduitsSnack();

    // --- Transactionnel ---
    /**
     * Effectue une réservation pour un client.
     *
     * @throws Exception si certains sièges sont déjà réservés.
     */
    Reservation effectuerReservation(int clientId, int seanceId, List<Integer> siegeIds, int tarifId) throws Exception;

    /**
     * Annule une réservation existante.
     *
     * @throws Exception si la réservation n’existe pas.
     */
    void annulerReservation(int reservationId) throws Exception;

    /**
     * Retourne l’historique des réservations du client.
     */
    List<Reservation> getHistoriqueReservationsClient(int clientId);

    // --- DEBUT DE L'AJOUT ---
    /**
     * Récupère la vente de snacks associée à une réservation.
     *
     * @param reservationId L'ID de la réservation.
     * @return La VenteSnack ou null si inexistante.
     */
    VenteSnack getVenteSnackForReservation(int reservationId);

    /**
     * Récupère les lignes de détail (produit, quantité) pour une vente de
     * snack.
     *
     * @param venteId L'ID de la vente de snack.
     * @return Une liste d'objets Comporte.
     */
    List<Comporte> getLignesVenteForVente(int venteId);
    // --- FIN DE L'AJOUT ---

    void changerMotDePasseClient(int clientId, String nouveauMotDePasse) throws Exception;

    /**
     * Enregistre une nouvelle évaluation pour un film par le client connecté.
     *
     * @param evaluation L'objet EvaluationClient à sauvegarder.
     * @throws Exception si le client a déjà noté ce film.
     */
    void ajouterEvaluation(EvaluationClient evaluation) throws Exception;

    /**
     * Vérifie si un client a déjà évalué un film spécifique.
     *
     * @param clientId L'ID du client.
     * @param filmId L'ID du film.
     * @return true si une évaluation existe, false sinon.
     */
    boolean aDejaEvalue(int clientId, int filmId);

    /**
     * Récupère toutes les évaluations postées pour un film spécifique.
     *
     * @param filmId L'ID du film.
     * @return Une liste d'objets EvaluationClient.
     */
    List<EvaluationClient> getEvaluationsByFilmId(int filmId);

    EvaluationClient getEvaluation(int clientId, int filmId);

    /**
     * Finalise une commande complète incluant billets et snacks.
     *
     * @param clientId L'ID du client.
     * @param seanceId L'ID de la séance.
     * @param siegeIds La liste des IDs des sièges.
     * @param tarifId L'ID du tarif appliqué.
     * @param panierSnacks Le panier de snacks (Produit -> Quantité).
     * @return La réservation créée.
     * @throws Exception en cas d'erreur (siège non dispo, stock
     * insuffisant...).
     */
    Reservation finaliserCommandeComplete(int clientId, int seanceId, List<Integer> siegeIds, int tarifId, List<LignePanier> panierSnacks) throws Exception;

    /**
     * Force le service à recharger toutes ses données depuis la source de
     * persistance. C'est la méthode à appeler pour garantir la fraîcheur des
     * données.
     */
    void rechargerTouteLaBase();

}
