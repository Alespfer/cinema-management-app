// ========================================================================
// FICHIER : CinemaServiceImpl.java
// ========================================================================
package com.mycompany.cinema.service.impl;

import com.mycompany.cinema.*;
import com.mycompany.cinema.dao.*;
import com.mycompany.cinema.dao.impl.*;
import com.mycompany.cinema.service.AdminService;
import com.mycompany.cinema.service.ClientService;
import com.mycompany.cinema.util.IdManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implémentation unique et centrale de toute la logique métier de
 * l'application. Cette classe reçoit les demandes de l'interface graphique,
 * interagit avec les différentes classes DAO (Couche d'Accès aux Données) pour
 * manipuler les objets, applique les règles de gestion (ex: un siège ne peut
 * être vendu deux fois), et retourne le résultat à la Vue.
 */
public class CinemaServiceImpl implements ClientService, AdminService {

    // --- Déclaration de toutes les dépendances envers la couche DAO ---
    // Toute l'application travaille sur une seule et même "source de vérité".
    private final FilmDAO filmDAO = new FilmDAOImpl();
    private final SeanceDAO seanceDAO = new SeanceDAOImpl();
    private final SalleDAO salleDAO = new SalleDAOImpl();
    private final SiegeDAO siegeDAO = new SiegeDAOImpl();
    private final BilletDAO billetDAO = new BilletDAOImpl();
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final ClientDAO clientDAO = new ClientDAOImpl();
    private final TarifDAO tarifDAO = new TarifDAOImpl();
    private final PersonnelDAO personnelDAO = new PersonnelDAOImpl();
    private final VenteSnackDAO venteSnackDAO = new VenteSnackDAOImpl();
    private final AffectationSeanceDAO affectationSeanceDAO = new AffectationSeanceDAOImpl();
    private final PlanningDAO planningDAO = new PlanningDAOImpl();
    private final LigneVenteDAO ligneVenteDAO = new LigneVenteDAOImpl(); // Nom uniformisé
    private final RoleDAO roleDAO = new RoleDAOImpl();
    private final GenreDAO genreDAO = new GenreDAOImpl();
    private final ProduitSnackDAO produitSnackDAO = new ProduitSnackDAOImpl();
    private final EvaluationClientDAO evaluationClientDAO = new EvaluationClientDAOImpl();
    private final CaisseDAO caisseDAO = new CaisseDAOImpl();

    // Constante métier définissant le temps de battement (nettoyage, pub) entre deux séances.
    private static final int TEMPS_BATTEMENT_MINUTES = 30;

    /**
     * Force le rechargement des données de toutes les sources.
     */
    @Override
    public void rechargerTouteLaBase() {
        filmDAO.rechargerDonnees();
        seanceDAO.rechargerDonnees();
        salleDAO.rechargerDonnees();
        siegeDAO.rechargerDonnees();
        billetDAO.rechargerDonnees();
        reservationDAO.rechargerDonnees();
        clientDAO.rechargerDonnees();
        tarifDAO.rechargerDonnees();
        personnelDAO.rechargerDonnees();
        venteSnackDAO.rechargerDonnees();
        affectationSeanceDAO.rechargerDonnees();
        planningDAO.rechargerDonnees();
        ligneVenteDAO.rechargerDonnees();
        roleDAO.rechargerDonnees();
        genreDAO.rechargerDonnees();
        produitSnackDAO.rechargerDonnees();
        evaluationClientDAO.rechargerDonnees();
        caisseDAO.rechargerDonnees();
    }

    // ========================================================================
    // --- SECTION COMMUNE (CLIENT & ADMIN) ---
    // ========================================================================
    /**
     * Récupère la liste de tous les films du catalogue.
     *
     * @return Une liste d'objets Film.
     */
    public List<Film> trouverFilmsAffiche() {
        return filmDAO.trouverTousLesFilms();
    }

    /**
     * Récupère les informations détaillées d'un film grâce à son identifiant.
     *
     * @param idFilm L'identifiant unique du film.
     * @return L'objet Film correspondant, ou `null` si introuvable.
     */
    public Film trouverDetailsFilm(int idFilm) {
        return filmDAO.trouverFilmParId(idFilm);
    }

    /**
     * Recherche des films dont le titre contient un mot-clé, de manière
     * insensible à la casse.
     *
     * @param motCle Le texte à rechercher dans les titres.
     * @return Une liste de films correspondants.
     */
    public List<Film> rechercherFilmsParTitre(String motCle) {
        return filmDAO.rechercherFilmsParTitre(motCle);
    }

    /**
     * Calcule la note moyenne des spectateurs pour un film donné.
     *
     * @param idFilm L'identifiant du film.
     * @return La note moyenne (double), ou 0.0 si aucune évaluation n'existe.
     */
    public double calculerNoteMoyenneSpectateurs(int idFilm) {
        List<EvaluationClient> evaluations = evaluationClientDAO.trouverEvaluationsParIdFilm(idFilm);
        if (evaluations.isEmpty()) {
            return 0.0;
        }
        double sommeDesNotes = 0;
        for (EvaluationClient evaluation : evaluations) {
            sommeDesNotes = sommeDesNotes + evaluation.getNote();
        }
        return sommeDesNotes / evaluations.size();
    }

    // ========================================================================
    // --- SECTION CLIENT ---
    // ========================================================================
    //--- Gestion du compte client ---
    /**
     * Crée un nouveau compte client après validation des informations.
     *
     * @param nom Le nom complet du client.
     * @param email L'adresse e-mail.
     * @param motDePasse Le mot de passe choisi.
     * @return Le nouvel objet Client créé et sauvegardé.
     * @throws Exception Si les données sont invalides ou si l'email est déjà
     * utilisé.
     */
    @Override
    public Client creerCompteClient(String nom, String email, String motDePasse) throws Exception {
        // --- Validation des paramètres d'entrée ---
        if (nom == null || nom.trim().isEmpty()) {
            throw new Exception("Le nom du client est obligatoire.");
        }
        if (email == null || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new Exception("L'adresse email est invalide.");
        }
        this.validerRobustesseMotDePasse(motDePasse);
        // --- Fin de la validation ---

        if (clientDAO.trouverClientParEmail(email) != null) {
            throw new Exception("Un compte avec cet email existe déjà.");
        }

        Client nouveauClient = new Client(IdManager.obtenirProchainIdClient(), nom, email, motDePasse, LocalDate.now());
        clientDAO.ajouterClient(nouveauClient);
        return nouveauClient;
    }

    /**
     * Tente d'authentifier un client avec son email et son mot de passe.
     *
     * @param email L'email du client.
     * @param motDePasse Le mot de passe.
     * @return L'objet Client si l'authentification réussit, sinon `null`.
     */
    @Override
    public Client authentifierClient(String email, String motDePasse) {
        for (Client client : clientDAO.trouverTousLesClients()) {
            if (client.getEmail().equalsIgnoreCase(email) && client.getMotDePasse().equals(motDePasse)) {
                return client;
            }
        }
        return null;
    }

    /**
     * Met à jour les informations d'un compte client existant.
     *
     * @param client L'objet Client avec les données mises à jour.
     * @throws Exception Si les données sont invalides ou si le nouvel email est
     * déjà pris par un autre utilisateur.
     */
    @Override
    public void modifierCompteClient(Client client) throws Exception {
        validerClient(client);
        Client clientExistant = clientDAO.trouverClientParEmail(client.getEmail());
        if (clientExistant != null && clientExistant.getId() != client.getId()) {
            throw new Exception("Un autre compte utilise déjà cet email.");
        }
        clientDAO.mettreAJourClient(client);
    }

    /**
     * Supprime un compte client et toutes ses réservations associées.
     *
     * @param idClient L'identifiant du client à supprimer.
     * @throws Exception Si une erreur survient lors de l'annulation des
     * réservations.
     */
    @Override
    public void supprimerCompteClient(int idClient) throws Exception {
        for (Reservation reservation : reservationDAO.trouverReservationsParIdClient(idClient)) {
            annulerReservation(reservation.getId());
        }
        clientDAO.supprimerClientParId(idClient);
    }

    /**
     * Modifie le mot de passe d'un client.
     *
     * @param clientId L'ID du client.
     * @param nouveauMotDePasse Le nouveau mot de passe.
     * @throws Exception si le client n'est pas trouvé.
     */
    @Override
    public void changerMotDePasseClient(int clientId, String nouveauMotDePasse) throws Exception {
        this.validerRobustesseMotDePasse(nouveauMotDePasse);
        Client client = clientDAO.trouverClientParId(clientId);
        if (client == null) {
            throw new Exception("Client non trouvé.");
        }
        client.setMotDePasse(nouveauMotDePasse);
        clientDAO.mettreAJourClient(client);
    }

    /**
     * Récupère un client par son identifiant.
     *
     * @param clientId L'ID du client.
     * @return L'objet Client, ou `null`.
     */
    @Override
    public Client trouverClientParId(int clientId) {
        return clientDAO.trouverClientParId(clientId);
    }

    /**
     * Récupère un client par son adresse email.
     *
     * @param email L'email du client.
     * @return L'objet Client, ou `null`.
     */
    @Override
    public Client trouverClientParEmail(String email) {
        List<Client> tousLesClients = clientDAO.trouverTousLesClients();
        for (Client client : tousLesClients) {
            if (client.getEmail().equalsIgnoreCase(email)) {
                return client;
            }
        }
        return null;
    }

    // --- Consultation et recherche (client)
    /**
     * Récupère toutes les séances pour un film donné à une date spécifique.
     *
     * @param idFilm L'identifiant du film.
     * @param date La date souhaitée.
     * @return Une liste de séances correspondantes.
     */
    public List<Seance> trouverSeancesPourFilmEtDate(int idFilm, LocalDate date) {
        List<Seance> seancesCorrespondantes = new ArrayList<>();
        for (Seance seance : seanceDAO.trouverSeancesParDate(date)) {
            if (seance.getIdFilm() == idFilm) {
                seancesCorrespondantes.add(seance);
            }
        }
        trierSeancesParDate(seancesCorrespondantes);
        return seancesCorrespondantes;
    }

    /**
     * Trie une liste de séances par ordre chronologique (de la plus proche à la
     * plus lointaine)
     *
     * @param seances La liste de Seance à trier.
     */
    private void trierSeancesParDate(List<Seance> seances) {
        // On ne fait rien si la liste est vide ou n'a qu'un seul élément
        if (seances == null || seances.size() <= 1) {
            return;
        }

        int n = seances.size();

        for (int i = 0; i < n - 1; i++) {

            int indexMin = i;

            for (int j = i + 1; j < n; j++) {

                if (seances.get(j).getDateHeureDebut().isBefore(seances.get(indexMin).getDateHeureDebut())) {
                    indexMin = j;
                }
            }
            Seance temp = seances.get(indexMin);
            seances.set(indexMin, seances.get(i));
            seances.set(i, temp);
        }
    }

    /**
     * Filtre les séances en fonction d'une date, d'un film et/ou d'une salle.
     *
     * @param date La date souhaitée (peut être `null`).
     * @param filmId L'ID du film (peut être `null`).
     * @param salleId L'ID de la salle (peut être `null`).
     * @return Une liste de séances correspondant aux critères.
     */
    @Override
    public List<Seance> trouverSeancesFiltrees(LocalDate date, Integer filmId, Integer salleId) {
        List<Seance> resultat = new ArrayList<>();
        List<Seance> seances;

        if (date != null) {
            seances = seanceDAO.trouverSeancesParDate(date);
        } else {
            seances = seanceDAO.trouverToutesLesSeances();
        }

        for (Seance s : seances) {
            boolean filmOk;
            boolean salleOk;

            if (filmId == null) {
                filmOk = true;
            } else {
                if (s.getIdFilm() == filmId) {
                    filmOk = true;
                } else {
                    filmOk = false;
                }
            }

            if (salleId == null) {
                salleOk = true;
            } else {
                if (s.getIdSalle() == salleId) {
                    salleOk = true;
                } else {
                    salleOk = false;
                }
            }

            if (filmOk && salleOk) {
                resultat.add(s);
            }
        }
        trierSeancesParDate(resultat);
        return resultat;
    }

    /**
     * Recherche des séances en combinant plusieurs filtres : date, genre du
     * film, et mot-clé dans le titre.
     *
     * @param date La date de recherche (peut être `null`).
     * @param genreId L'ID du genre (peut être `null`).
     * @param titreKeyword Le mot-clé à trouver dans le titre (peut être
     * `null`).
     * @return Une liste de séances correspondant à tous les critères fournis.
     */
    @Override
    public List<Seance> rechercherSeances(LocalDate date, Integer genreId, String titreKeyword) {
        List<Seance> resultatFinal = new ArrayList<>();
        for (Seance seance : seanceDAO.trouverToutesLesSeances()) {
            boolean correspond = true;
            if (date != null && !seance.getDateHeureDebut().toLocalDate().isEqual(date)) {
                correspond = false;
            }
            if (correspond) {
                Film film = filmDAO.trouverFilmParId(seance.getIdFilm());
                if (film != null) {
                    if (titreKeyword != null && !titreKeyword.trim().isEmpty() && !film.getTitre().toLowerCase().contains(titreKeyword.toLowerCase())) {
                        correspond = false;
                    }
                    if (correspond && genreId != null) {
                        boolean genreTrouve = false;
                        for (Genre g : film.getGenres()) {
                            if (g.getId() == genreId) {
                                genreTrouve = true;
                                break;
                            }
                        }
                        if (!genreTrouve) {
                            correspond = false;
                        }
                    }
                } else {
                    correspond = false;
                }
            }
            if (correspond) {
                resultatFinal.add(seance);
            }
        }
        trierSeancesParDate(resultatFinal);
        return resultatFinal;
    }

    /**
     * Calcule et retourne la liste des sièges encore disponibles pour une
     * séance.
     *
     * @param seanceId L'identifiant de la séance.
     * @return Une liste des objets Siege disponibles.
     */
    public List<Siege> trouverSiegesDisponibles(int seanceId) {
        Seance seance = seanceDAO.trouverSeanceParId(seanceId);
        if (seance == null) {
            return new ArrayList<>();
        }
        List<Siege> tousLesSieges = siegeDAO.trouverSiegesParIdSalle(seance.getIdSalle());
        List<Billet> billetsVendus = billetDAO.trouverBilletsParIdSeance(seanceId);

        Set<Integer> idsSiegesOccupes = new HashSet<>();
        for (Billet billet : billetsVendus) {
            idsSiegesOccupes.add(billet.getIdSiege());
        }

        List<Siege> siegesDisponibles = new ArrayList<>();
        for (Siege siege : tousLesSieges) {
            if (!idsSiegesOccupes.contains(siege.getId())) {
                siegesDisponibles.add(siege);
            }
        }
        return siegesDisponibles;
    }

    /**
     * Récupère la liste de tous les sièges d'une salle donnée.
     *
     * @param salleId L'identifiant de la salle.
     * @return Une liste d'objets Siege.
     */
    @Override
    public List<Siege> trouverSiegesPourSalle(int salleId) {
        return siegeDAO.trouverSiegesParIdSalle(salleId);
    }

    /**
     * Récupère un tarif par son identifiant.
     *
     * @param tarifId L'ID du tarif.
     * @return L'objet Tarif, ou `null`.
     */
    @Override
    public Tarif trouverTarifParId(int tarifId) {
        return tarifDAO.trouverTarifParId(tarifId);
    }

    /**
     * Récupère la liste de tous les tarifs disponibles.
     *
     * @return Une liste d'objets Tarif.
     */
    @Override
    public List<Tarif> trouverTousLesTarifs() {
        return tarifDAO.trouverTousLesTarifs();
    }

    /**
     * Récupère la liste de tous les genres de films.
     *
     * @return Une liste d'objets Genre.
     */
    @Override
    public List<Genre> trouverTousLesGenres() {
        return genreDAO.trouverTousLesGenres();
    }

    /**
     * Récupère la liste de toutes les salles du cinéma.
     *
     * @return Une liste d'objets Salle.
     */
    @Override
    public List<Salle> trouverToutesLesSalles() {
        return salleDAO.trouverToutesLesSalles();
    }

    /**
     * Récupère la liste de tous les produits de snacking.
     *
     * @return Une liste d'objets ProduitSnack.
     */
    @Override
    public List<ProduitSnack> trouverTousLesProduits() {
        return produitSnackDAO.trouverTousLesProduits();
    }

    // --- Processus de réservation et achat ---
    /**
     * Crée une réservation pour des billets de cinéma sans inclure de snacks.
     *
     * @param idClient L'identifiant du client.
     * @param idSeance L'identifiant de la séance.
     * @param idsSieges La liste des identifiants des sièges à réserver.
     * @param idTarif L'identifiant du tarif à appliquer.
     * @return L'objet Reservation créé.
     * @throws Exception Si un des identifiants est invalide ou un siège n'est
     * plus disponible.
     */
    @Override
    public Reservation effectuerReservation(int idClient, int idSeance, List<Integer> idsSieges, int idTarif) throws Exception {
        if (clientDAO.trouverClientParId(idClient) == null) {
            throw new Exception("Client non trouvé.");
        }
        if (tarifDAO.trouverTarifParId(idTarif) == null) {
            throw new Exception("Tarif non trouvé.");
        }
        if (seanceDAO.trouverSeanceParId(idSeance) == null) {
            throw new Exception("Séance non trouvée.");
        }

        List<Siege> siegesDisponibles = trouverSiegesDisponibles(idSeance);
        List<Integer> idsSiegesDisponibles = new ArrayList<>();
        for (Siege siege : siegesDisponibles) {
            idsSiegesDisponibles.add(siege.getId());
        }
        for (Integer idSiege : idsSieges) {
            if (!idsSiegesDisponibles.contains(idSiege)) {
                throw new Exception("Le siège " + idSiege + " n'est plus disponible.");
            }
        }

        int idNouvelleReservation = IdManager.obtenirProchainIdReservation();
        Reservation reservation = new Reservation(idNouvelleReservation, LocalDateTime.now(), idClient);
        reservationDAO.ajouterReservation(reservation);

        for (Integer idSiege : idsSieges) {
            Billet billet = new Billet(IdManager.obtenirProchainIdBillet(), idNouvelleReservation, idTarif, idSiege, idSeance);
            billetDAO.ajouterBillet(billet);
        }
        return reservation;
    }

    /**
     * Crée et enregistre une commande complète incluant billets et snacks.
     *
     * @param clientId L'identifiant du client.
     * @param seanceId L'identifiant de la séance.
     * @param siegeIds La liste des identifiants des sièges.
     * @param tarifId L'identifiant du tarif.
     * @param panierSnacks La liste des produits de snacking.
     * @return L'objet Reservation finalisé.
     * @throws Exception en cas d'erreur durant le processus (siège
     * indisponible, stock insuffisant, etc.).
     */
    @Override
    public Reservation finaliserCommandeComplete(int clientId, int seanceId, List<Integer> siegeIds, int tarifId, List<LignePanier> panierSnacks) throws Exception {
        // Étape 1 : La réservation des billets est créée.
        Reservation reservation = effectuerReservation(clientId, seanceId, siegeIds, tarifId);

        // Étape 2 : Si des snacks sont commandés, on les traite.
        if (panierSnacks != null && !panierSnacks.isEmpty()) {

            // On utilise l'ID 0 (Système) et la Caisse 0 (Canal Web), et on passe le clientId.
            VenteSnack venteSnack = enregistrerVenteSnackPourClient(0, 0, panierSnacks, clientId);

            // Étape 3 : On lie la vente de snacks à la réservation de billets.
            venteSnack.setIdReservation(reservation.getId());

            // Étape 4 : On sauvegarde cette liaison dans la base de données.
            venteSnackDAO.mettreAJourVenteSnack(venteSnack);
        }

        return reservation;
    }

    /**
     * Annule une réservation existante, ce qui supprime la réservation et tous
     * les billets associés.
     *
     * @param reservationId L'identifiant de la réservation à annuler.
     * @throws Exception si la réservation n'est pas trouvée.
     */
    @Override
    public void annulerReservation(int reservationId) throws Exception {
        if (reservationDAO.trouverReservationParId(reservationId) == null) {
            throw new Exception("Réservation non trouvée.");
        }
        billetDAO.supprimerBilletsParIdReservation(reservationId);
        reservationDAO.supprimerReservationParId(reservationId);
    }

    /**
     * Récupère l'historique de toutes les réservations d'un client.
     *
     * @param clientId L'identifiant du client.
     * @return Une liste d'objets Reservation.
     */
    @Override
    public List<Reservation> trouverHistoriqueReservationsClient(int clientId) {
        return reservationDAO.trouverReservationsParIdClient(clientId);
    }

    /**
     * Récupère tous les billets associés à une réservation.
     *
     * @param reservationId L'ID de la réservation.
     * @return Une liste d'objets Billet.
     */
    @Override
    public List<Billet> trouverBilletsParIdReservation(int reservationId) {
        return billetDAO.trouverBilletsParIdReservation(reservationId);
    }

    /**
     * Retrouve une vente de snacks associée à une réservation de billets.
     *
     * @param reservationId L'identifiant de la réservation.
     * @return La VenteSnack correspondante, ou `null` si aucune n'est liée.
     */
    @Override
    public VenteSnack trouverVenteSnackReservation(int reservationId) {
        // Le service délègue simplement l'appel au DAO compétent.
        return venteSnackDAO.trouverVenteParIdReservation(reservationId);
    }

    /**
     * Récupère les lignes de détail (produits, quantités) pour une vente de
     * snacks donnée.
     *
     * @param venteId L'identifiant de la vente.
     * @return Une liste des lignes de la vente.
     */
    @Override
    public List<LigneVente> trouverLignesParIdVente(int venteId) {
        // Le service délègue l'appel au DAO qui gère les lignes de vente.
        return ligneVenteDAO.trouverLignesParIdVente(venteId);
    }

    // --- Processus de réservation et achat ---
    /**
     * Ajoute une nouvelle évaluation (note et commentaire) d'un client pour un
     * film.
     *
     * @param evaluation L'objet EvaluationClient à enregistrer.
     * @throws Exception si le client a déjà évalué ce film.
     */
    @Override
    public void ajouterEvaluation(EvaluationClient evaluation) throws Exception {
        if (aDejaEvalue(evaluation.getIdClient(), evaluation.getIdFilm())) {
            throw new Exception("Vous avez déjà noté ce film.");
        }
        evaluationClientDAO.ajouterEvaluation(evaluation);
    }

    public void modifierEvaluation(EvaluationClient evaluation) throws Exception {
        // --- Étape 1 : Validation des données ---
        if (evaluation == null) {
            throw new Exception("L'objet d'évaluation ne peut pas être null.");
        }

        int note = evaluation.getNote();
        if (note < 1 || note > 5) {
            throw new Exception("La note doit être comprise entre 1 et 5.");
        }

        // On vérifie que l'évaluation à modifier existe bien.
        // C'est une sécurité pour éviter de créer une évaluation par erreur.
        EvaluationClient evaluationExistante = evaluationClientDAO.trouverEvaluationParClientEtFilm(evaluation.getIdClient(), evaluation.getIdFilm());
        if (evaluationExistante == null) {
            throw new Exception("Impossible de modifier une évaluation qui n'existe pas.");
        }
        evaluationClientDAO.mettreAJourEvaluation(evaluation);
    }

    /**
     * Vérifie si un client a déjà posté un avis pour un film donné.
     *
     * @param clientId L'identifiant du client.
     * @param filmId L'identifiant du film.
     * @return `true` si une évaluation existe, sinon `false`.
     */
    @Override
    public boolean aDejaEvalue(int clientId, int filmId) {
        return evaluationClientDAO.trouverEvaluationParClientEtFilm(clientId, filmId) != null;
    }

    /**
     * Récupère l'évaluation spécifique d'un client pour un film.
     *
     * @param clientId L'identifiant du client.
     * @param filmId L'identifiant du film.
     * @return L'objet EvaluationClient, ou `null` si aucune n'existe.
     */
    @Override
    public EvaluationClient trouverEvaluation(int clientId, int filmId) {
        return evaluationClientDAO.trouverEvaluationParClientEtFilm(clientId, filmId);
    }

    /**
     * Récupère toutes les évaluations postées pour un film.
     *
     * @param filmId L'identifiant du film.
     * @return Une liste d'évaluations.
     */
    @Override
    public List<EvaluationClient> trouverEvaluationsParFilmId(int filmId) {
        return evaluationClientDAO.trouverEvaluationsParIdFilm(filmId);
    }

    // ========================================================================
    // --- SECTION ADMIN ---
    // ========================================================================
    /**
     * Met à jour le stock d'un produit de snack.
     *
     * @param produitId L'identifiant du produit.
     * @param quantiteAjustement La quantité à ajouter (positive) ou à retirer
     * (négative).
     * @throws Exception Si le produit n'existe pas ou si le stock devient
     * négatif.
     */
    @Override
    public void ajusterStockProduit(int produitId, int quantiteAjustement) throws Exception {
        // 1. On récupère l'objet ProduitSnack à partir de son ID.
        ProduitSnack produit = produitSnackDAO.trouverProduitParId(produitId);
        if (produit == null) {
            throw new Exception("Le produit avec l'ID " + produitId + " n'a pas été trouvé.");
        }

        // 2. Calcul du nouveau stock
        int stockActuel = produit.getStock();
        int nouveauStock = stockActuel + quantiteAjustement;

        // 3. On valide que le stock ne devient pas négatif.
        if (nouveauStock < 0) {
            throw new Exception("Opération impossible : le stock ne peut pas devenir négatif.");
        }

        // 4. Mise à jour et sauvegarde de l'objet
        produit.setStock(nouveauStock);
        produitSnackDAO.mettreAJourProduit(produit);
    }

    /**
     * Récupère tous les billets vendus pour une séance.
     *
     * @param seanceId L'identifiant de la séance.
     * @return Une liste d'objets Billet.
     */
    @Override
    public List<Billet> trouverBilletsPourSeance(int seanceId) {
        return billetDAO.trouverBilletsParIdSeance(seanceId);
    }

    public VenteSnack enregistrerVenteSnackPourClient(int idPersonnel, int idCaisse, List<LignePanier> panier, Integer idClient) throws Exception {
        int venteId = IdManager.obtenirProchainIdVenteSnack();
        VenteSnack nouvelleVente = new VenteSnack(venteId, LocalDateTime.now(), idPersonnel, idCaisse, idClient);

        venteSnackDAO.ajouterVenteSnack(nouvelleVente);

        for (LignePanier ligne : panier) {
            ProduitSnack produit = ligne.getProduit();
            int quantite = ligne.getQuantite();

            if (produit.getStock() < quantite) {
                throw new Exception("Stock insuffisant pour " + produit.getNomProduit() + ". La partie snack de la commande est annulée.");
            }

            LigneVente ligneDeVente = new LigneVente(venteId, produit.getId(), quantite, produit.getPrixVente());
            ligneVenteDAO.ajouterLigneVente(ligneDeVente);

            produit.setStock(produit.getStock() - quantite);
            produitSnackDAO.mettreAJourProduit(produit);
        }
        return nouvelleVente;
    }

    /**
     * Récupère une séance par son identifiant.
     *
     * @param seanceId L'ID de la séance.
     * @return L'objet Seance, ou `null`.
     */
    @Override
    public Seance trouverSeanceParId(int seanceId) {
        return seanceDAO.trouverSeanceParId(seanceId);
    }

    // ========================================================================
    // --- SECTION ADMIN ---
    // ========================================================================
    // ---  Authentification et gestion du personnel ---
    /**
     * Tente d'authentifier un membre du personnel. Évite la connexion des
     * utilisateurs désactivés.
     *
     * @param email L'email du membre du personnel.
     * @param motDePasse Le mot de passe.
     * @return L'objet Personnel si l'authentification réussit, sinon `null`.
     */
    @Override
    public Personnel authentifierPersonnel(String email, String motDePasse) {
        for (Personnel p : personnelDAO.trouverToutLePersonnel()) {
            if (p.getEmail().equalsIgnoreCase(email) && p.getMotDePasse().equals(motDePasse)) {

                if (p.getEstActif()) {
                    return p;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Ajoute un nouveau membre au personnel.
     *
     * @param p Le membre du personnel à ajouter.
     */
    @Override
    public void ajouterPersonnel(Personnel p) {
        personnelDAO.ajouterPersonnel(p);
    }

    /**
     * Met à jour les informations d'un membre du personnel.
     *
     * @param p Le membre du personnel avec les données mises à jour.
     */
    @Override
    public void modifierPersonnel(Personnel p) {
        personnelDAO.mettreAJourPersonnel(p);
    }

    @Override
    public void desactiverPersonnel(int personnelId) throws Exception {
        // Sécurité : on interdit la désactivation de l'utilisateur Système
        if (personnelId == 0) {
            throw new Exception("L'utilisateur Système ne peut pas être désactivé.");
        }

        // 1. On récupère l'objet Personnel à partir de son ID.
        Personnel personnelADesactiver = personnelDAO.trouverPersonnelParId(personnelId);

        if (personnelADesactiver == null) {
            throw new Exception("Impossible de trouver l'employé avec l'ID " + personnelId);
        }

        // 2. On change son état.
        personnelADesactiver.setEstActif(false);

        // 3. On demande au DAO de sauvegarder l'objet mis à jour.
        // La méthode mettreAJourPersonnel existe déjà !
        personnelDAO.mettreAJourPersonnel(personnelADesactiver);
    }

    // Implémentation de la nouvelle méthode de réactivation
    @Override
    public void reactiverPersonnel(int personnelId) throws Exception {
        Personnel personnel = personnelDAO.trouverPersonnelParId(personnelId);
        if (personnel == null) {
            throw new Exception("Membre du personnel non trouvé.");
        }
        personnel.setEstActif(true); // On change simplement le statut
        personnelDAO.mettreAJourPersonnel(personnel); // Et on sauvegarde
    }

    /**
     * Supprime un membre du personnel.
     *
     * @param pId L'ID du membre du personnel à supprimer.
     */
    @Override
    public void supprimerPersonnel(int pId) {
        personnelDAO.supprimerPersonnelParId(pId);
    }

    /**
     * Récupère la liste de tous les membres actifs du personnel.
     *
     * @return Une liste d'objets Personnel.
     */
    @Override
    public List<Personnel> trouverToutLePersonnel(boolean inclureInactifs) {
        List<Personnel> toutLePersonnel = personnelDAO.trouverToutLePersonnel();

        if (inclureInactifs) {
            return toutLePersonnel; // Retourne tout le monde si demandé
        }

        // Sinon, on filtre comme avant
        List<Personnel> personnelActif = new ArrayList<>();
        for (Personnel p : toutLePersonnel) {
            if (p.getEstActif()) {
                personnelActif.add(p);
            }
        }
        return personnelActif;
    }

    /**
     * Récupère un membre du personnel par son adresse email.
     *
     * @param email L'email du membre du personnel.
     * @return L'objet Personnel, ou `null`.
     */
    @Override
    public Personnel trouverPersonnelParEmail(String email) {
        for (Personnel p : personnelDAO.trouverToutLePersonnel()) {
            if (p.getEmail().equalsIgnoreCase(email)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Récupère un membre du personnel par son identifiant.
     *
     * @param pId L'ID du membre du personnel.
     * @return L'objet Personnel, ou `null`.
     */
    @Override
    public Personnel trouverPersonnelParId(int pId) {
        return personnelDAO.trouverPersonnelParId(pId);
    }

    /**
     * Modifie le mot de passe d'un membre du personnel.
     *
     * @param personnelId L'ID du membre du personnel.
     * @param nouveauMotDePasse Le nouveau mot de passe.
     * @throws Exception si le membre du personnel n'est pas trouvé.
     */
    @Override
    public void changerMotDePassePersonnel(int personnelId, String nouveauMotDePasse) throws Exception {
        this.validerRobustesseMotDePasse(nouveauMotDePasse);
        Personnel personnel = personnelDAO.trouverPersonnelParId(personnelId);
        if (personnel == null) {
            throw new Exception("Membre du personnel non trouvé.");
        }
        personnel.setMotDePasse(nouveauMotDePasse);
        personnelDAO.mettreAJourPersonnel(personnel);
    }

    /**
     * Récupère la liste de tous les rôles du personnel.
     *
     * @return Une liste d'objets Role.
     */
    @Override
    public List<Role> trouverTousLesRoles() {
        return roleDAO.trouverTousLesRoles();
    }

    @Override
    public Role trouverRoleParId(int idRole) {
        return roleDAO.trouverRoleParId(idRole);
    }

    // --- Gestion du palnning ---
    /**
     * Crée un nouveau créneau de travail dans le planning.
     *
     * @param pId L'ID du membre du personnel concerné.
     * @param d La date et l'heure de début.
     * @param f La date et l'heure de fin.
     * @param poste La description du poste.
     * @return L'objet Planning créé.
     * @throws Exception Si une erreur de persistance survient.
     */
    @Override
    public Planning creerPlanning(int pId, LocalDateTime d, LocalDateTime f, String poste) throws Exception {
        Planning p = new Planning(IdManager.obtenirProchainIdPlanning(), d, f, poste, pId);
        planningDAO.ajouterPlanning(p);
        return p;
    }

    /**
     * Valide les données d'un créneau de planning (cohérence des dates, poste
     * non vide) avant de le mettre à jour dans la couche DAO.
     */
    @Override
    public void modifierPlanning(Planning planning) throws Exception {
        if (planning == null) {
            throw new Exception("L'objet Planning ne peut pas être null.");
        }
        if (planning.getPosteOccupe() == null || planning.getPosteOccupe().trim().isEmpty()) {
            throw new Exception("Le poste ne peut pas être vide.");
        }
        if (planning.getDateHeureFinService().isBefore(planning.getDateHeureDebutService())) {
            throw new Exception("La date de fin ne peut pas être antérieure à la date de début.");
        }

        planningDAO.mettreAJourPlanning(planning);
    }

    /**
     * Demande à la couche DAO de supprimer le créneau.
     */
    @Override
    public void supprimerPlanning(int planningId) throws Exception {
        planningDAO.supprimerPlanningParId(planningId);
    }

    /**
     * Récupère tous les créneaux de travail d'un membre du personnel.
     *
     * @param pId L'ID du membre du personnel.
     * @return Une liste de ses plannings.
     */
    @Override
    public List<Planning> trouverPlanningPourPersonnel(int idPersonnel) {
        List<Planning> planningsPourPersonnel = planningDAO.trouverPlanningsParIdPersonnel(idPersonnel);

        return trierPlanningsParDate(planningsPourPersonnel);
    }

    /**
     * Trie tous les plannings par date de début croissante selon un algorithme
     * de tri par sélection.
     */
    @Override
    public List<Planning> trierPlanningsParDate(List<Planning> plannings) {
        int taille = plannings.size();
        for (int i = 0; i < taille - 1; i++) {
            int indexDuMinimum = i;
            for (int j = i + 1; j < taille; j++) {
                LocalDateTime dateCourante = plannings.get(j).getDateHeureDebutService();
                LocalDateTime dateMinimum = plannings.get(indexDuMinimum).getDateHeureDebutService();
                if (dateCourante.isBefore(dateMinimum)) {
                    indexDuMinimum = j;
                }
            }
            Planning planningMinimum = plannings.get(indexDuMinimum);
            plannings.set(indexDuMinimum, plannings.get(i));
            plannings.set(i, planningMinimum);
        }
        return plannings;
    }

    // --- Gestion du catalogue (films et genres) ---
    /**
     * Ajoute un nouveau film au catalogue.
     *
     * @param film Le film à ajouter.
     * @throws Exception Si les données du film sont invalides.
     */
    @Override
    public void ajouterFilm(Film film) throws Exception {
        validerFilm(film);
        filmDAO.ajouterFilm(film);
    }

    /**
     * Met à jour les informations d'un film existant.
     *
     * @param film Le film avec les données mises à jour.
     * @throws Exception Si les données du film sont invalides.
     */
    @Override
    public void mettreAJourFilm(Film film) throws Exception {
        // On valide également l'objet AVANT de le mettre à jour.
        validerFilm(film);

        // Si la validation passe, on procède à la mise à jour.
        filmDAO.mettreAJourFilm(film);
    }

    /**
     * Supprime un film du catalogue.
     *
     * @param filmId L'ID du film à supprimer.
     * @throws Exception Si le film est encore programmé dans une séance.
     */
    @Override
    public void supprimerFilm(int filmId) throws Exception {
        // On vérifie si le film n'est pas encore lié à une séance.
        for (Seance seance : seanceDAO.trouverToutesLesSeances()) {
            if (seance.getIdFilm() == filmId) {
                // Si on trouve une séance, on lève une exception et on arrête tout.
                throw new Exception("Impossible de supprimer ce film car il est encore programmé pour la séance du "
                        + seance.getDateHeureDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm")) + ".");
            }
        }

        // Si la boucle se termine sans trouver de séance, le film peut être supprimé.
        filmDAO.supprimerFilmParId(filmId);
    }

    /**
     * Ajoute un nouveau genre.
     *
     * @param genre Le genre à ajouter.
     */
    @Override
    public void ajouterGenre(Genre genre) {
        genreDAO.ajouterGenre(genre);
    }

    /**
     * Met à jour un genre existant.
     *
     * @param genre Le genre avec les données mises à jour.
     */
    @Override
    public void modifierGenre(Genre genre) {
        genreDAO.mettreAJourGenre(genre);
    }

    /**
     * Supprime un genre.
     *
     * @param genreId L'ID du genre à supprimer.
     * @throws Exception Si le genre est encore associé à des films.
     */
    @Override
    public void supprimerGenre(int genreId) throws Exception {
        List<String> filmsAssocies = new ArrayList<>();
        for (Film f : filmDAO.trouverTousLesFilms()) {
            for (Genre g : f.getGenres()) {
                if (g.getId() == genreId) {
                    filmsAssocies.add(f.getTitre());
                }
            }
        }
        if (!filmsAssocies.isEmpty()) {
            throw new Exception("Impossible de supprimer. Genre utilisé par : " + String.join(", ", filmsAssocies));
        }
        genreDAO.supprimerGenreParId(genreId);
    }

    // --- Gestion de l'infrastructure (salles et tarifs) ---
    /**
     * Ajoute une nouvelle salle et génère automatiquement son plan de sièges.
     *
     * @param salle L'objet Salle à créer.
     * @param nbRangees Le nombre de rangées de la salle.
     * @param nbSiegesParRangee Le nombre de sièges par rangée.
     * @throws Exception Si les données sont invalides, si la capacité ne
     * correspond pas au plan, ou si le nom de la salle est déjà utilisé.
     */
    @Override
    public void ajouterSalleAvecPlan(Salle salle, int nbRangees, int nbSiegesParRangee) throws Exception {
        validerSalle(salle);

        if (nbRangees <= 0 || nbSiegesParRangee <= 0) {
            throw new Exception("Le nombre de rangées et de sièges par rangée doit être positif.");
        }

        if (salle.getCapacite() != nbRangees * nbSiegesParRangee) {
            throw new Exception("La capacité de la salle ne correspond pas au plan (rangées * sièges).");
        }

        for (Salle s : salleDAO.trouverToutesLesSalles()) {
            if (s.getNumero().equalsIgnoreCase(salle.getNumero())) {
                throw new Exception("Une salle avec le numéro '" + salle.getNumero() + "' existe déjà.");
            }
        }

        // 1. Sauvegarde de la salle pour obtenir son ID
        salleDAO.ajouterSalle(salle);

        // 2. Génération des sièges 
        System.out.println("Création d'un plan de " + nbRangees + "x" + nbSiegesParRangee + " sièges pour la salle ID " + salle.getId());

        for (int r = 1; r <= nbRangees; r++) {
            for (int s = 1; s <= nbSiegesParRangee; s++) {
                int siegeId = IdManager.obtenirProchainIdSiege();
                Siege nouveauSiege = new Siege(siegeId, r, s, salle.getId());
                siegeDAO.ajouterSiege(nouveauSiege);
            }
        }

        System.out.println("Création de " + (nbRangees * nbSiegesParRangee) + " sièges terminée.");
    }

    /**
     * Supprime une salle du cinéma.
     *
     * @param salleId L'ID de la salle à supprimer.
     * @throws Exception Si la salle est encore utilisée dans une séance.
     */
    @Override
    public void supprimerSalle(int salleId) throws Exception {
        // On vérifie si la salle n'est pas encore utilisée dans une séance.
        for (Seance seance : seanceDAO.trouverToutesLesSeances()) {
            if (seance.getIdSalle() == salleId) {
                // Si une séance utilise cette salle, on bloque la suppression.
                throw new Exception("Impossible de supprimer cette salle car elle est encore utilisée pour une séance le "
                        + seance.getDateHeureDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm")) + ".");
            }
        }

        salleDAO.supprimerSalleParId(salleId);
    }

    /**
     * Ajoute un nouveau tarif.
     *
     * @param tarif Le tarif à ajouter.
     * @throws Exception Si les données sont invalides ou si le libellé existe
     * déjà.
     */
    @Override
    public void ajouterTarif(Tarif tarif) throws Exception {
        validerTarif(tarif);

        for (Tarif t : tarifDAO.trouverTousLesTarifs()) {
            if (t.getLibelle().equalsIgnoreCase(tarif.getLibelle())) {
                throw new Exception("Un tarif avec le libellé '" + tarif.getLibelle() + "' existe déjà.");
            }
        }
        tarifDAO.ajouterTarif(tarif);
    }

    /**
     * Met à jour un tarif existant.
     *
     * @param tarif Le tarif avec les données mises à jour.
     * @throws Exception Si les données sont invalides ou si le libellé est déjà
     * utilisé par un autre tarif.
     */
    @Override
    public void modifierTarif(Tarif tarif) throws Exception {
        validerTarif(tarif);

        for (Tarif t : tarifDAO.trouverTousLesTarifs()) {
            if (t.getId() != tarif.getId() && t.getLibelle().equalsIgnoreCase(tarif.getLibelle())) {
                throw new Exception("Un autre tarif avec le libellé '" + tarif.getLibelle() + "' existe déjà.");
            }
        }
        tarifDAO.mettreAJourTarif(tarif);
    }

    /**
     * Supprime un tarif du système.
     *
     * @param tarifId L'ID du tarif à supprimer.
     */
    @Override
    public void supprimerTarif(int tarifId) {
        tarifDAO.supprimerTarifParId(tarifId);
    }

    // --- Gestion de la programmation (séances) ---
    /**
     * Ajoute une nouvelle séance à la programmation après avoir vérifié les
     * conflits d'horaire.
     *
     * @param seance La séance à ajouter.
     * @throws Exception Si la séance est dans le passé ou crée un conflit avec
     * une autre séance.
     */
    @Override
    public void ajouterSeance(Seance seance) throws Exception {

        // 1. On récupère le film pour connaître sa durée.
        Film film = filmDAO.trouverFilmParId(seance.getIdFilm());
        if (film == null) {
            throw new Exception("Le film associé à cette séance n'existe pas.");
        }

        // 2. On calcule l'heure de début de la nouvelle séance.
        LocalDateTime debutNouvelleSeance = seance.getDateHeureDebut();

        // 3. On valide que la séance n'est pas programmée dans le passé.
        if (debutNouvelleSeance.isBefore(LocalDateTime.now())) {
            throw new Exception("Impossible de planifier une séance dans le passé.");
        }

        // 4. On calcule l'heure de fin d'occupation de la salle (incluant le temps de battement).
        LocalDateTime finNouvelleSeance = debutNouvelleSeance.plusMinutes(film.getDureeMinutes() + TEMPS_BATTEMENT_MINUTES);

        // 5. On récupère toutes les séances existantes pour la même salle pour comparaison.
        List<Seance> seancesExistantesDansLaSalle = new ArrayList<>();
        for (Seance s : seanceDAO.trouverToutesLesSeances()) {
            if (s.getIdSalle() == seance.getIdSalle()) {
                seancesExistantesDansLaSalle.add(s);
            }
        }

        // 6. On parcourt chaque séance existante pour détecter un conflit d'horaire.
        for (Seance seanceExistante : seancesExistantesDansLaSalle) {
            Film filmExistant = filmDAO.trouverFilmParId(seanceExistante.getIdFilm());
            if (filmExistant != null) {
                LocalDateTime debutExistant = seanceExistante.getDateHeureDebut();
                // On calcule la fin d'occupation de la séance existante en incluant aussi le battement.
                LocalDateTime finExistant = debutExistant.plusMinutes(filmExistant.getDureeMinutes() + TEMPS_BATTEMENT_MINUTES);

                // La condition de conflit : si la période de la nouvelle séance chevauche celle d'une séance existante.
                if (debutNouvelleSeance.isBefore(finExistant) && finNouvelleSeance.isAfter(debutExistant)) {
                    throw new Exception("Conflit d'horaire : La salle " + seance.getIdSalle()
                            + " est déjà occupée par le film '" + filmExistant.getTitre()
                            + "' (temps de battement inclus) entre "
                            + debutExistant.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                            + " et "
                            + finExistant.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) + ".");
                }
            }
        }

        // Si aucune exception n'a été levée, on peut ajouter la séance en toute sécurité.
        seanceDAO.ajouterSeance(seance);
    }

    /**
     * Modifie une séance existante après avoir vérifié les conflits d'horaire.
     *
     * @param seance La séance avec les données mises à jour.
     * @throws Exception Si un conflit d'horaire est détecté.
     */
    @Override
    public void modifierSeance(Seance seance) throws Exception {
        Film film = filmDAO.trouverFilmParId(seance.getIdFilm());
        if (film == null) {
            throw new Exception("Film non trouvé.");
        }

        LocalDateTime debutNouvelleSeance = seance.getDateHeureDebut();
        LocalDateTime finNouvelleSeance = debutNouvelleSeance.plusMinutes(film.getDureeMinutes());

        for (Seance seanceExistante : seanceDAO.trouverToutesLesSeances()) {
            if (seanceExistante.getId() != seance.getId() && seanceExistante.getIdSalle() == seance.getIdSalle()) {
                Film filmExistant = filmDAO.trouverFilmParId(seanceExistante.getIdFilm());
                if (filmExistant != null) {
                    LocalDateTime debutExistant = seanceExistante.getDateHeureDebut();
                    LocalDateTime finExistant = debutExistant.plusMinutes(filmExistant.getDureeMinutes());
                    if (debutNouvelleSeance.isBefore(finExistant) && finNouvelleSeance.isAfter(debutExistant)) {
                        throw new Exception("Conflit d'horaire avec le film '" + filmExistant.getTitre() + "' dans la même salle.");
                    }
                }
            }
        }
        seanceDAO.mettreAJourSeance(seance);
    }

    /**
     * Supprime une séance de la programmation.
     *
     * @param seanceId L'ID de la séance à supprimer.
     * @throws Exception Si des billets ont déjà été vendus pour cette séance.
     */
    @Override
    public void supprimerSeance(int seanceId) throws Exception {
        List<Billet> billetsPourLaSeance = billetDAO.trouverBilletsParIdSeance(seanceId);
        if (!billetsPourLaSeance.isEmpty()) {
            throw new Exception("Impossible de supprimer cette séance car " + billetsPourLaSeance.size() + " billet(s) ont déjà été vendus.");
        }

        // Si aucun billet n'a été vendu, on peut supprimer la séance.
        seanceDAO.supprimerSeanceParId(seanceId);
    }

    /**
     * Récupère la liste de toutes les séances programmées.
     *
     * @return Une liste d'objets Seance.
     */
    @Override
    public List<Seance> trouverToutesLesSeances() {
        return seanceDAO.trouverToutesLesSeances();
    }

    // --- Gestion du point de vente (produits et ventes) ---
    /**
     * Ajoute un nouveau produit de snack au catalogue.
     *
     * @param produit Le produit à ajouter.
     * @throws Exception Si les données sont invalides ou si le nom du produit
     * existe déjà.
     */
    @Override
    public void ajouterProduitSnack(ProduitSnack produit) throws Exception {
        validerProduitSnack(produit);

        for (ProduitSnack p : produitSnackDAO.trouverTousLesProduits()) {
            if (p.getNomProduit().equalsIgnoreCase(produit.getNomProduit())) {
                throw new Exception("Un produit avec le nom '" + produit.getNomProduit() + "' existe déjà.");
            }
        }
        produitSnackDAO.ajouterProduit(produit);
    }

    /**
     * Met à jour un produit de snack.
     *
     * @param produit Le produit avec les données mises à jour.
     * @throws Exception Si les données sont invalides ou si le nom est déjà
     * utilisé par un autre produit.
     */
    @Override
    public void modifierProduitSnack(ProduitSnack produit) throws Exception {
        validerProduitSnack(produit);

        for (ProduitSnack p : produitSnackDAO.trouverTousLesProduits()) {
            if (p.getId() != produit.getId() && p.getNomProduit().equalsIgnoreCase(produit.getNomProduit())) {
                throw new Exception("Un autre produit avec le nom '" + produit.getNomProduit() + "' existe déjà.");
            }
        }
        produitSnackDAO.mettreAJourProduit(produit);
    }

    /**
     * Supprime un produit de snack.
     *
     * @param produitId L'ID du produit à supprimer.
     * @throws Exception Si le produit est lié à des ventes existantes.
     */
    @Override
    public void supprimerProduitSnack(int produitId) throws Exception {
        List<LigneVente> toutesLesLignes = ligneVenteDAO.trouverToutesLesLignesVente();
        for (LigneVente ligne : toutesLesLignes) {
            if (ligne.getIdProduit() == produitId) {
                throw new IllegalStateException("Impossible de supprimer le produit ID " + produitId + " car il est lié à des ventes existantes.");
            }
        }
        produitSnackDAO.supprimerProduitParId(produitId);
    }

    /**
     * Enregistre une vente de snacks au comptoir pour un client non identifié.
     *
     * @param idPersonnel L'ID de l'employé qui réalise la vente.
     * @param idCaisse L'ID de la caisse utilisée.
     * @param panier La liste des produits et quantités vendus.
     * @return L'objet VenteSnack créé.
     * @throws Exception Si le stock d'un produit est insuffisant.
     */
    @Override
    public VenteSnack enregistrerVenteSnack(int idPersonnel, int idCaisse, List<LignePanier> panier) throws Exception {
        int venteId = IdManager.obtenirProchainIdVenteSnack();
        VenteSnack nouvelleVente = new VenteSnack(venteId, LocalDateTime.now(), idPersonnel, idCaisse, null);

        venteSnackDAO.ajouterVenteSnack(nouvelleVente);

        for (LignePanier ligne : panier) {
            ProduitSnack produit = ligne.getProduit();
            int quantite = ligne.getQuantite();

            if (produit.getStock() < quantite) {
                throw new Exception("Stock insuffisant pour " + produit.getNomProduit() + ". Veuillez entrer une quantité valide.");
            }

            LigneVente ligneDeVente = new LigneVente(venteId, produit.getId(), quantite, produit.getPrixVente());
            ligneVenteDAO.ajouterLigneVente(ligneDeVente);

            produit.setStock(produit.getStock() - quantite);
            produitSnackDAO.mettreAJourProduit(produit);
        }
        return nouvelleVente;
    }

    // --- Reporting ---
    /**
     * Récupère tous les billets vendus pour un film, toutes séances confondues.
     *
     * @param filmId L'ID du film.
     * @return Une liste d'objets Billet.
     */
    @Override
    public List<Billet> trouverBilletsPourFilm(int filmId) {
        List<Billet> billets = new ArrayList<>();
        List<Seance> seancesDuFilm = seanceDAO.trouverSeancesParIdFilm(filmId);
        for (Seance s : seancesDuFilm) {
            List<Billet> billetsDeLaSeance = billetDAO.trouverBilletsParIdSeance(s.getId());
            for (Billet b : billetsDeLaSeance) {
                billets.add(b);
            }
        }
        return billets;
    }

    /**
     * Calcule le chiffre d'affaires total généré par les billets d'un film.
     *
     * @param filmId L'ID du film.
     * @return Le chiffre d'affaires total.
     */
    @Override
    public double calculerChiffreAffairesPourFilm(int filmId) {
        double total = 0;
        for (Billet b : trouverBilletsPourFilm(filmId)) {
            Tarif t = tarifDAO.trouverTarifParId(b.getIdTarif());
            if (t != null) {
                total += t.getPrix();
            }
        }
        return total;
    }

    /**
     * Calcule le chiffre d'affaires des billets pour une journée donnée.
     *
     * @param date La date souhaitée.
     * @return Le chiffre d'affaires total pour ce jour.
     */
    @Override
    public double calculerChiffreAffairesPourJour(LocalDate date) {
        double total = 0;
        for (Seance s : seanceDAO.trouverSeancesParDate(date)) {
            total += calculerChiffreAffairesReservationsPourSeance(s.getId());
        }
        return total;
    }

    /**
     * Calcule le chiffre d'affaires des billets pour une séance spécifique.
     *
     * @param seanceId L'ID de la séance.
     * @return Le chiffre d'affaires de la séance.
     */
    @Override
    public double calculerChiffreAffairesReservationsPourSeance(int seanceId) {
        double total = 0;
        for (Billet b : billetDAO.trouverBilletsParIdSeance(seanceId)) {
            Tarif t = tarifDAO.trouverTarifParId(b.getIdTarif());
            if (t != null) {
                total += t.getPrix();
            }
        }
        return total;
    }

    /**
     * Récupère la liste de toutes les réservations.
     *
     * @return Une liste de toutes les réservations.
     */
    @Override
    public List<Reservation> trouverToutesLesReservations() {
        return reservationDAO.trouverToutesLesReservations();
    }

    /**
     * Récupère toutes les ventes de snacks pour une date donnée.
     *
     * @param date La date souhaitée.
     * @return Une liste des ventes de snacks.
     */
    @Override
    public List<VenteSnack> trouverVentesSnackParDate(LocalDate date) {
        return venteSnackDAO.trouverVentesParDate(date);
    }

    /**
     * Calcule le chiffre d'affaires des snacks pour une journée.
     *
     * @param date La date souhaitée.
     * @return Le chiffre d'affaires total des snacks pour ce jour.
     */
    @Override
    public double calculerChiffreAffairesSnackPourJour(LocalDate date) {
        double total = 0;
        for (VenteSnack v : venteSnackDAO.trouverVentesParDate(date)) {
            total += calculerTotalPourVenteSnack(v);
        }
        return total;
    }

    /**
     * Récupère la liste de toutes les ventes de snacks.
     *
     * @return Une liste de toutes les ventes.
     */
    @Override
    public List<VenteSnack> trouverToutesLesVentesSnack() {
        return venteSnackDAO.trouverToutesLesVentesSnack();
    }

    /**
     * Calcule le montant total d'un ticket de caisse de snacks.
     *
     * @param vente La vente de snack concernée.
     * @return Le montant total de la vente.
     */
    @Override
    public double calculerTotalPourVenteSnack(VenteSnack vente) {
        double total = 0;
        for (LigneVente ligne : ligneVenteDAO.trouverLignesParIdVente(vente.getIdVente())) {
            total += ligne.getQuantite() * ligne.getPrixUnitaire();
        }
        return total;
    }

    /**
     * Récupère une caisse par son identifiant.
     *
     * @param caisseId L'ID de la caisse.
     * @return L'objet Caisse, ou `null`.
     */
    @Override
    public Caisse trouverCaisseParId(int caisseId) {
        return caisseDAO.trouverCaisseParId(caisseId);
    }

    // --- Methodes de validation ---
    /**
     * Méthode privée pour valider l'intégrité d'un objet Film. Vérifie que tous
     * les champs obligatoires sont remplis.
     *
     * @param film L'objet Film à valider.
     * @throws Exception si une des règles métier n'est pas respectée.
     */
    private void validerFilm(Film film) throws Exception {
        if (film == null) {
            throw new Exception("L'objet Film ne peut pas être null.");
        }
        if (film.getTitre() == null || film.getTitre().trim().isEmpty()) {
            throw new Exception("Le titre du film est obligatoire.");
        }
        if (film.getSynopsis() == null || film.getSynopsis().trim().isEmpty()) {
            throw new Exception("Le synopsis est obligatoire.");
        }
        if (film.getDureeMinutes() <= 0) {
            throw new Exception("La durée du film doit être un nombre positif de minutes.");
        }
        if (film.getClassification() == null || film.getClassification().trim().isEmpty()) {
            throw new Exception("La classification est obligatoire.");
        }
        if (film.getUrlAffiche() == null || film.getUrlAffiche().trim().isEmpty()) {
            throw new Exception("L'URL de l'affiche est obligatoire.");
        }
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            throw new Exception("Le film doit avoir au moins un genre.");
        }
        // La note de presse (notePresse) n'est pas vérifiée car elle est optionnelle.
    }

    /**
     * Valide l'intégrité d'un objet Client.
     *
     * @param client L'objet Client à valider.
     * @throws Exception si une règle de validation n'est pas respectée.
     */
    private void validerClient(Client client) throws Exception {
        if (client.getNom() == null || client.getNom().trim().isEmpty()) {
            throw new Exception("Le nom du client est obligatoire.");
        }
        if (client.getEmail() == null || !client.getEmail().contains("@")) {
            throw new Exception("L'adresse email est invalide.");
        }
        if (client.getMotDePasse() == null || client.getMotDePasse().length() < 4) {
            throw new Exception("Le mot de passe doit contenir au moins 4 caractères.");
        }
    }

    /**
     * Valide l'intégrité d'un objet ProduitSnack.
     *
     * @param produit L'objet ProduitSnack à valider.
     * @throws Exception si une règle de validation n'est pas respectée.
     */
    private void validerProduitSnack(ProduitSnack produit) throws Exception {
        if (produit.getNomProduit() == null || produit.getNomProduit().trim().isEmpty()) {
            throw new Exception("Le nom du produit est obligatoire.");
        }
        if (produit.getPrixVente() < 0) {
            throw new Exception("Le prix du produit ne peut pas être négatif.");
        }
        if (produit.getStock() < 0) {
            throw new Exception("Le stock du produit ne peut pas être négatif.");
        }
    }

    /**
     * Valide l'intégrité d'un objet Salle.
     *
     * @param salle L'objet Salle à valider.
     * @throws Exception si une règle de validation n'est pas respectée.
     */
    private void validerSalle(Salle salle) throws Exception {
        if (salle.getNumero() == null || salle.getNumero().trim().isEmpty()) {
            throw new Exception("Le numéro/nom de la salle est obligatoire.");
        }
        if (salle.getCapacite() <= 0) {
            throw new Exception("La capacité de la salle doit être positive.");
        }
    }

    /**
     * Valide l'intégrité d'un objet Tarif.
     *
     * @param tarif L'objet Tarif à valider.
     * @throws Exception si une règle de validation n'est pas respectée.
     */
    private void validerTarif(Tarif tarif) throws Exception {
        if (tarif.getLibelle() == null || tarif.getLibelle().trim().isEmpty()) {
            throw new Exception("Le libellé du tarif est obligatoire.");
        }
        if (tarif.getPrix() < 0) {
            throw new Exception("Le prix du tarif ne peut pas être négatif.");
        }
    }

    /**
     * Méthode privée et centralisée pour valider la robustesse d'un mot de
     * passe. Lève une exception avec un message spécifique pour la première
     * règle non respectée.
     *
     * @param motDePasse Le mot de passe à valider.
     * @throws Exception Si le mot de passe est invalide.
     */
    private void validerRobustesseMotDePasse(String motDePasse) throws Exception {
        if (motDePasse == null) {
            throw new Exception("Le mot de passe ne peut pas être vide.");
        }

        // Règle 1 : Vérifier la longueur
        if (motDePasse.length() < 8) {
            throw new Exception("Le mot de passe doit contenir au moins 8 caractères.");
        }

        // Règle 2 : Doit contenir au moins une minuscule
        if (!motDePasse.matches(".*[a-z].*")) {
            throw new Exception("Le mot de passe doit contenir au moins une lettre minuscule.");
        }

        // Règle 3 : Doit contenir au moins une majuscule
        if (!motDePasse.matches(".*[A-Z].*")) {
            throw new Exception("Le mot de passe doit contenir au moins une lettre majuscule.");
        }

        // Règle 4 : Doit contenir au moins un chiffre
        if (!motDePasse.matches(".*[0-9].*")) {
            throw new Exception("Le mot de passe doit contenir au moins un chiffre.");
        }

        // (Optionnel) Règle 5 : Ne doit pas contenir d'espaces
        if (motDePasse.matches(".*\\s.*")) {
            throw new Exception("Le mot de passe ne doit pas contenir d'espaces.");
        }
    }

    public String reinitialiserMotDePasse(String email) throws Exception {
        Client client = clientDAO.trouverClientParEmail(email);
        if (client == null) {
            throw new Exception("Aucun compte client trouvé avec cet e-mail.");
        }
        String nouveauMdp = "temp" + System.currentTimeMillis() % 10000; // génération simple
        client.setMotDePasse(nouveauMdp);
        clientDAO.mettreAJourClient(client);
        return nouveauMdp;
    }

    public String reinitialiserMotDePassePersonnel(String nomUtilisateur) throws Exception {
        for (Personnel p : personnelDAO.trouverToutLePersonnel()) {
            if (p.getNom().equalsIgnoreCase(nomUtilisateur)) {
                String nouveauMdp = "temp" + System.currentTimeMillis() % 10000;
                p.setMotDePasse(nouveauMdp);
                personnelDAO.mettreAJourPersonnel(p);
                return nouveauMdp;
            }
        }
        throw new Exception("Aucun compte personnel trouvé avec ce nom d'utilisateur.");
    }

}
