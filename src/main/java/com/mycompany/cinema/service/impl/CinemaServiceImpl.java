package com.mycompany.cinema.service.impl;

// Importation de toutes les classes nécessaires : le modèle, les contrats DAO, et les utilitaires.
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * C'est LE CERVEAU de l'application. Cette classe unique implémente TOUTES les
 * règles de gestion et les fonctionnalités, que ce soit pour les clients ou
 * pour les administrateurs.
 *
 * Pour le développeur de l'interface graphique : ce fichier est votre UNIQUE
 * POINT DE CONTACT avec la logique de l'application. Tous vos panneaux,
 * fenêtres et dialogues n'appelleront que des méthodes de cette classe (via les
 * interfaces ClientService ou AdminService).
 *
 * Elle agit comme une "façade" : elle vous offre des méthodes simples (ex:
 * "réserver un billet") et se charge de toute la complexité en coulisses
 * (vérifier la disponibilité, créer les objets, les sauvegarder, etc.).
 */
public class CinemaServiceImpl implements ClientService, AdminService {

    // --- DAO INSTANCES ---
    // Le Service crée ici tous les "magasiniers" (DAO) dont il aura besoin pour travailler.
    // Notez que les variables sont de type INTERFACE (ex: FilmDAO), pas de type implémentation (FilmDAOImpl).
    // C'est le secret du découplage : le Service ne dépend que du "contrat", pas de la manière dont il est rempli.
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
    private final ComporteDAO comporteDAO = new ComporteDAOImpl();
    private final RoleDAO roleDAO = new RoleDAOImpl();
    private final GenreDAO genreDAO = new GenreDAOImpl();
    private final ProduitSnackDAO produitSnackDAO = new ProduitSnackDAOImpl();
    private final EvaluationClientDAO evaluationClientDAO = new EvaluationClientDAOImpl();
    private final CaisseDAO caisseDAO = new CaisseDAOImpl();

    /**
     * Méthode utilitaire pour forcer toutes les couches de données à se
     * rafraîchir. Utile pour garantir que l'interface affiche les données les
     * plus à jour après une modification majeure (ex: un admin ajoute un film,
     * un autre panneau doit le voir).
     */
    @Override
    public void rechargerTouteLaBase() {
        // Demande à chaque DAO de relire son fichier.
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
        comporteDAO.rechargerDonnees();
        roleDAO.rechargerDonnees();
        genreDAO.rechargerDonnees();
        produitSnackDAO.rechargerDonnees();
        evaluationClientDAO.rechargerDonnees();
        caisseDAO.rechargerDonnees();
    }
    // =========================================================================
    // SECTION COMMUNE (Partagée entre Clients et Admins via CinemaService)
    // =========================================================================

    /**
     * Récupère la liste de tous les films à l'affiche. Utilisé par le
     * ProgrammationPanel pour l'affichage initial.
     */
    @Override
    public List<Film> getFilmsAffiche() {
        return filmDAO.getAllFilms();
    }

    /**
     * Récupère toutes les informations d'un film spécifique par son ID. Utilisé
     * par le FilmDetailPanel.
     */
    @Override
    public Film getFilmDetails(int filmId) {
        return filmDAO.getFilmById(filmId).orElse(null);
    }

    /**
     * Recherche des films dont le titre contient un certain mot-clé. Utilisé
     * par la barre de recherche du ProgrammationPanel.
     */
    @Override
    public List<Film> findFilmsByTitre(String keyword) {
        return filmDAO.findFilmsByTitre(keyword);
    }

    /**
     * Calcule et retourne la note moyenne donnée par les spectateurs pour un
     * film. Cette version utilise une boucle 'for-each' classique pour une
     * conformité maximale avec le cours.
     */
    @Override
    public double getNoteMoyenneSpectateurs(int filmId) {
        // 1. Récupérer la liste des données à traiter, comme vu au chapitre "Collections".
        List<EvaluationClient> evaluations = evaluationClientDAO.getEvaluationsByFilmId(filmId);

        // 2. Si la liste est vide, on retourne 0 pour éviter une division par zéro.
        if (evaluations.isEmpty()) {
            return 0.0;
        }

        // 3. Initialiser une variable pour accumuler la somme.
        double sommeDesNotes = 0;

        // 4. Parcourir la liste avec une boucle "for-each", comme enseigné à la page 214.
        for (EvaluationClient eval : evaluations) {
            sommeDesNotes = sommeDesNotes + eval.getNote(); // ou sommeDesNotes += eval.getNote();
        }

        // 5. Calculer la moyenne manuellement.
        return sommeDesNotes / evaluations.size();
    }

    // =========================================================================
    // SECTION CLIENT (Fonctionnalités disponibles pour les clients)
    // =========================================================================
    /**
     * Gère la création d'un nouveau compte client. Applique la règle métier :
     * un email ne peut pas être utilisé deux fois.
     *
     * @return Le nouvel objet Client créé.
     * @throws Exception Si l'email existe déjà. La Vue devra intercepter cette
     * exception et afficher un message d'erreur.
     */
    @Override
    public Client creerCompteClient(String nom, String email, String motDePasse) throws Exception {
        for (Client c : clientDAO.getAllClients()) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                throw new Exception("Email déjà utilisé.");
            }
        }
        Client nouveauClient = new Client(IdManager.getNextClientId(), nom, email, motDePasse, LocalDate.now());
        clientDAO.addClient(nouveauClient);
        return nouveauClient;
    }

    /**
     * Tente d'authentifier un client avec son email et son mot de passe.
     * Utilise une boucle 'for-each' pour la recherche, conformément aux bases
     * du cours.
     */
    @Override
    public Optional<Client> authentifierClient(String email, String motDePasse) {
        // 1. Récupérer la liste de tous les clients.
        List<Client> tousLesClients = clientDAO.getAllClients();

        // 2. Parcourir la liste pour trouver une correspondance.
        for (Client client : tousLesClients) {
            // La méthode equalsIgnoreCase est une méthode standard de la classe String (vue p.57)
            if (client.getEmail().equalsIgnoreCase(email) && client.getMotDePasse().equals(motDePasse)) {
                // Si trouvé, on retourne un Optional contenant le client et on arrête la recherche.
                return Optional.of(client);
            }
        }

        // 3. Si la boucle se termine sans trouver de client, on retourne un Optional vide.
        return Optional.empty();
    }

    /**
     * Met à jour les informations d'un client (nom, mot de passe). Utilisé par
     * le InfosPersonnellesPanel.
     */
    @Override
    public void modifierCompteClient(Client client) throws Exception {
        clientDAO.updateClient(client);
    }

    /**
     * Supprime un compte client. Règle de gestion : annule d'abord toutes ses
     * réservations existantes.
     */
    @Override
    public void supprimerCompteClient(int clientId) throws Exception {
        for (Reservation r : reservationDAO.getReservationsByClientId(clientId)) {
            annulerReservation(r.getId());
        }
        clientDAO.deleteClient(clientId);
    }

    @Override
    public List<Seance> getSeancesPourFilmEtDate(int filmId, LocalDate date) {
        List<Seance> resultat = new ArrayList<>();
        for (Seance s : seanceDAO.getSeancesByDate(date)) {
            if (s.getIdFilm() == filmId) {
                resultat.add(s);
            }
        }
        return resultat;
    }

    @Override
    public List<Seance> findSeancesFiltrees(LocalDate date, Integer filmId, Integer salleId) {
        List<Seance> resultat = new ArrayList<>();
        List<Seance> seances = (date != null) ? seanceDAO.getSeancesByDate(date) : seanceDAO.getAllSeances();
        for (Seance s : seances) {
            if ((filmId == null || s.getIdFilm() == filmId) && (salleId == null || s.getIdSalle() == salleId)) {
                resultat.add(s);
            }
        }
        return resultat;
    }

    /**
     * Logique de recherche avancée pour le ProgrammationPanel, combinant
     * plusieurs filtres. C'est un bon exemple d'orchestration complexe.
     */
    @Override
    public List<Seance> rechercherSeances(LocalDate date, Integer genreId, String titreKeyword) {
        List<Seance> resultatFinal = new ArrayList<>();
        List<Seance> seancesSource = seanceDAO.getAllSeances();

        for (Seance seance : seancesSource) {
            boolean correspond = true;
            // Filtre par date
            if (date != null && !seance.getDateHeureDebut().toLocalDate().isEqual(date)) {
                correspond = false;
            }

            // Si la date correspond (ou n'est pas un filtre), on vérifie le film
            if (correspond) {
                Optional<Film> filmOpt = filmDAO.getFilmById(seance.getIdFilm());
                if (filmOpt.isPresent()) {
                    Film film = filmOpt.get();
                    // Filtre par mot-clé dans le titre
                    if (titreKeyword != null && !titreKeyword.trim().isEmpty() && !film.getTitre().toLowerCase().contains(titreKeyword.toLowerCase())) {
                        correspond = false;
                    }
                    // Si toujours ok, on filtre par genre
                    if (correspond && genreId != null) {
                        boolean genreTrouve = false;
                        if (film.getGenres() != null) {
                            for (Genre g : film.getGenres()) {
                                if (g.getId() == genreId) {
                                    genreTrouve = true;
                                    break;
                                }
                            }
                        }
                        if (!genreTrouve) {
                            correspond = false;
                        }
                    }
                } else {
                    correspond = false; // Le film associé à la séance n'existe pas
                }
            }
            if (correspond) {
                resultatFinal.add(seance);
            }
        }
        return resultatFinal;
    }

    @Override
    public List<Siege> getSiegesPourSalle(int salleId) {
        return siegeDAO.getSiegesBySalleId(salleId);
    }

    @Override
    public List<Billet> getBilletsPourSeance(int seanceId) {
        return billetDAO.getBilletsBySeanceId(seanceId);
    }

    /**
     * Logique métier pour déterminer les sièges encore disponibles pour une
     * séance. C'est une orchestration clé pour le SiegePanel.
     */
    @Override
    public List<Siege> getSiegesDisponibles(int seanceId) {
        Optional<Seance> seanceOpt = seanceDAO.getSeanceById(seanceId);
        if (seanceOpt.isEmpty()) {
            return new ArrayList<>();
        }

        // 1. On prend tous les sièges de la salle
        List<Siege> tousLesSieges = siegeDAO.getSiegesBySalleId(seanceOpt.get().getIdSalle());
        // 2. On prend tous les billets déjà vendus pour cette séance
        List<Billet> billetsVendus = getBilletsPourSeance(seanceId);
        // 3. On extrait les IDs des sièges occupés
        List<Integer> idsSiegesOccupes = new ArrayList<>();
        for (Billet billet : billetsVendus) {
            idsSiegesOccupes.add(billet.getIdSiege());
        }

        // 4. On construit la liste des sièges disponibles en retirant les sièges occupés
        List<Siege> siegesDisponibles = new ArrayList<>();
        for (Siege siege : tousLesSieges) {
            if (!idsSiegesOccupes.contains(siege.getId())) {
                siegesDisponibles.add(siege);
            }
        }
        return siegesDisponibles;
    }

    /**
     * Gère la transaction de réservation de billets. Applique plusieurs règles
     * de gestion avant de contacter les DAO.
     */
    @Override
    public Reservation effectuerReservation(int clientId, int seanceId, List<Integer> siegeIds, int tarifId) throws Exception {
        // Règle de gestion n°1 : Vérifier que toutes les données de base existent.
        if (clientDAO.getClientById(clientId).isEmpty()) {
            throw new Exception("Client non trouvé.");
        }
        if (tarifDAO.getTarifById(tarifId).isEmpty()) {
            throw new Exception("Tarif non trouvé.");
        }
        if (seanceDAO.getSeanceById(seanceId).isEmpty()) {
            throw new Exception("Séance non trouvée.");
        }

        // Règle de gestion n°2 : Vérifier que les sièges demandés sont bien disponibles.
        List<Siege> siegesDisponibles = getSiegesDisponibles(seanceId);
        List<Integer> siegesDisponiblesIds = new ArrayList<>();
        for (Siege s : siegesDisponibles) {
            siegesDisponiblesIds.add(s.getId());
        }

        for (Integer siegeId : siegeIds) {
            // Si un des sièges demandés n'est plus dans la liste des disponibles, on lance une erreur.
            if (!siegesDisponiblesIds.contains(siegeId)) {
                throw new Exception("Le siège " + siegeId + " n'est plus disponible. La réservation a été annulée.");
            }
        }

        // Si tout est valide, on procède à la création des objets dans la base de données.
        int newReservationId = IdManager.getNextReservationId();
        Reservation reservation = new Reservation(newReservationId, LocalDateTime.now(), clientId);
        reservationDAO.addReservation(reservation);

        for (Integer siegeId : siegeIds) {
            int newBilletId = IdManager.getNextBilletId();
            Billet billet = new Billet(newBilletId, newReservationId, tarifId, siegeId, seanceId);
            billetDAO.addBillet(billet);
        }
        return reservation;
    }

    @Override
    public void annulerReservation(int reservationId) throws Exception {
        if (reservationDAO.getReservationById(reservationId).isEmpty()) {
            throw new Exception("Réservation non trouvée.");
        }
        // Orchestration : on supprime d'abord les "enfants" (billets) avant de supprimer le "parent" (réservation).
        billetDAO.deleteBilletsByReservationId(reservationId);
        reservationDAO.deleteReservation(reservationId);
    }

    @Override
    public List<Reservation> getHistoriqueReservationsClient(int clientId) {
        return reservationDAO.getReservationsByClientId(clientId);
    }

    @Override
    public void ajouterEvaluation(EvaluationClient evaluation) throws Exception {
        // Règle de gestion : un client ne peut évaluer un film qu'une seule fois.
        if (aDejaEvalue(evaluation.getIdClient(), evaluation.getIdFilm())) {
            throw new Exception("Vous avez déjà noté ce film.");
        }
        evaluationClientDAO.addEvaluation(evaluation);
    }

    @Override
    public boolean aDejaEvalue(int clientId, int filmId) {
        return evaluationClientDAO.getEvaluationByClientAndFilm(clientId, filmId).isPresent();
    }

    @Override
    public List<EvaluationClient> getEvaluationsByFilmId(int filmId) {
        return evaluationClientDAO.getEvaluationsByFilmId(filmId);
    }

    // --- Méthodes "passerelle" qui simplifient l'accès aux données pour la Vue ---
    @Override
    public List<Tarif> getAllTarifs() {
        return tarifDAO.getAllTarifs();
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreDAO.getAllGenres();
    }

    @Override
    public List<Salle> getAllSalles() {
        return salleDAO.getAllSalles();
    }

    @Override
    public Optional<Client> getClientById(int clientId) {
        return clientDAO.getClientById(clientId);
    }

    @Override
    public List<Billet> getBilletsByReservationId(int reservationId) {
        return billetDAO.getBilletsByReservationId(reservationId);
    }

    @Override
    public Optional<Seance> getSeanceById(int seanceId) {
        return seanceDAO.getSeanceById(seanceId);
    }

    @Override
    public List<ProduitSnack> getAllProduitsSnack() {
        return produitSnackDAO.getAllProduits();
    }

    // =========================================================================
    // MÉTHODE TRANSACTIONNELLE COMBINÉE - VERSION UNIQUE ET CORRECTE
    // =========================================================================
    /**
     * Gère la transaction complète d'une commande en ligne : billets ET snacks.
     * C'est la méthode la plus complexe car elle doit garantir que soit TOUT
     * réussit, soit TOUT est annulé, pour éviter des incohérences de données
     * (ex: billets réservés mais pas assez de stock pour le popcorn).
     *
     * @param clientId L'ID du client qui passe la commande.
     * @param seanceId L'ID de la séance choisie.
     * @param siegeIds La liste des IDs des sièges sélectionnés.
     * @param tarifId L'ID du tarif appliqué aux billets.
     * @param panierSnacks Le panier de snacks (Produit -> Quantité).
     * @return L'objet Reservation créé si la transaction réussit.
     * @throws Exception Si une étape échoue (siège déjà pris, stock
     * insuffisant...).
     */
    @Override
    public Reservation finaliserCommandeComplete(int clientId, int seanceId, List<Integer> siegeIds, int tarifId, Map<ProduitSnack, Integer> panierSnacks) throws Exception {
        // Étape 1 : On tente de réserver les billets. Cette méthode a ses propres vérifications.
        Reservation reservation = effectuerReservation(clientId, seanceId, siegeIds, tarifId);

        // Étape 2 : Si la réservation des billets a réussi et que le client a ajouté des snacks...
        if (panierSnacks != null && !panierSnacks.isEmpty()) {

            // Règle de gestion : on vérifie les stocks AVANT de toucher aux données.
            for (Map.Entry<ProduitSnack, Integer> entry : panierSnacks.entrySet()) {
                Optional<ProduitSnack> produitOpt = produitSnackDAO.getProduitById(entry.getKey().getId());
                if (produitOpt.isEmpty() || produitOpt.get().getStock() < entry.getValue()) {
                    // SI LE STOCK EST INSUFFISANT : on annule la réservation de billets qu'on vient de faire.
                    // C'est un "rollback" manuel pour garantir que le client ne se retrouve pas avec des billets mais sans ses snacks.
                    annulerReservation(reservation.getId());
                    // On lance une erreur claire que la Vue pourra afficher.
                    throw new Exception("Stock insuffisant pour le produit '" + entry.getKey().getNomProduit() + "'. Commande annulée.");
                }
            }

            // Si les stocks sont suffisants, on procède à l'enregistrement de la vente de snacks.
            int venteId = IdManager.getNextVenteSnackId();
            // On utilise des IDs fictifs (-1) car c'est une vente en ligne, sans employé ni caisse physique.
            VenteSnack vente = new VenteSnack(venteId, LocalDateTime.now(), -1, -1, clientId);
            vente.setIdReservation(reservation.getId()); // On lie cette vente de snack à la réservation de billets.
            venteSnackDAO.addVenteSnack(vente);

            // Pour chaque article dans le panier, on crée une ligne de détail et on met à jour le stock.
            for (Map.Entry<ProduitSnack, Integer> entry : panierSnacks.entrySet()) {
                ProduitSnack produit = entry.getKey();
                Integer quantite = entry.getValue();

                Comporte ligne = new Comporte(venteId, produit.getId(), quantite, produit.getPrixVente());
                comporteDAO.addLigneVente(ligne);

                // On décrémente le stock du produit.
                produit.setStock(produit.getStock() - quantite);
                produitSnackDAO.updateProduit(produit);
            }
        }

        // Si tout s'est bien passé, on retourne l'objet Reservation créé.
        return reservation;
    }

    // =========================================================================
    // SECTION ADMIN (Fonctionnalités réservées aux administrateurs)
    // =========================================================================
    /**
     * Tente d'authentifier un membre du personnel. Utilise une boucle
     * 'for-each' pour la recherche.
     */
    @Override
    public Optional<Personnel> authentifierPersonnel(String nomUtilisateur, String motDePasse) {
        List<Personnel> toutLePersonnel = personnelDAO.getAllPersonnel();

        for (Personnel p : toutLePersonnel) {
            if (p.getNom().equalsIgnoreCase(nomUtilisateur) && p.getMotDePasse().equals(motDePasse)) {
                return Optional.of(p);
            }
        }

        return Optional.empty();
    }

    // --- Les méthodes suivantes sont des "passerelles" directes vers le DAO correspondant. ---
    // --- Leur logique est si simple qu'elles n'ont pas besoin de commentaires individuels. ---
    // --- Elles permettent à la Vue de demander une action au Service, qui la délègue simplement ---
    // --- au bon gestionnaire de données (DAO). ---
    @Override
    public void ajouterFilm(Film film) {
        filmDAO.addFilm(film);
    }

    @Override
    public void mettreAJourFilm(Film film) {
        filmDAO.updateFilm(film);
    }

    @Override
    public void supprimerFilm(int filmId) throws Exception {
        filmDAO.deleteFilm(filmId);
    }

    @Override
    public void ajouterSeance(Seance seance) throws Exception {
        seanceDAO.addSeance(seance);
    }

    @Override
    public void modifierSeance(Seance seance) throws Exception {
        seanceDAO.updateSeance(seance);
    }

    @Override
    public void supprimerSeance(int seanceId) throws Exception {
        seanceDAO.deleteSeance(seanceId);
    }

    @Override
    public List<Seance> getAllSeances() {
        return seanceDAO.getAllSeances();
    }

    @Override
    public void ajouterSalle(Salle salle) {
        salleDAO.addSalle(salle);
    }

    @Override
    public void modifierSalle(Salle salle) {
        salleDAO.updateSalle(salle);
    }

    @Override
    public void supprimerSalle(int salleId) throws Exception {
        salleDAO.deleteSalle(salleId);
    }

    @Override
    public void ajouterTarif(Tarif tarif) throws Exception {
        tarifDAO.addTarif(tarif);
    }

    @Override
    public void modifierTarif(Tarif tarif) throws Exception {
        tarifDAO.updateTarif(tarif);
    }

    @Override
    public void supprimerTarif(int tarifId) {
        tarifDAO.deleteTarif(tarifId);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }

    @Override
    public void ajouterPersonnel(Personnel p) {
        personnelDAO.addPersonnel(p);
    }

    @Override
    public void modifierPersonnel(Personnel p) {
        personnelDAO.updatePersonnel(p);
    }

    @Override
    public void supprimerPersonnel(int pId) {
        personnelDAO.deletePersonnel(pId);
    }

    @Override
    public List<Personnel> getAllPersonnel() {
        return personnelDAO.getAllPersonnel();
    }

    @Override
    public void affecterPersonnelASeance(int pId, int sId) throws Exception {
        affectationSeanceDAO.addAffectation(new AffectationSeance(sId, pId));
    }

    @Override
    public void desaffecterPersonnelDeSeance(int pId, int sId) throws Exception {
        affectationSeanceDAO.deleteAffectation(sId, pId);
    }

    @Override
    public Planning creerPlanning(int pId, LocalDateTime d, LocalDateTime f, String poste) throws Exception {
        Planning p = new Planning(IdManager.getNextPlanningId(), d, f, poste, pId);
        planningDAO.addPlanning(p);
        return p;
    }

    @Override
    public List<Planning> getPlanningPourPersonnel(int pId) {
        return planningDAO.getPlanningsByPersonnelId(pId);
    }

    @Override
    public void ajouterProduitSnack(ProduitSnack p) throws Exception {
        produitSnackDAO.addProduit(p);
    }

    @Override
    public void modifierProduitSnack(ProduitSnack p) throws Exception {
        produitSnackDAO.updateProduit(p);
    }

    /**
     * Supprime un produit de snacking du catalogue. Applique une règle de
     * gestion essentielle : un produit ne peut être supprimé que s'il n'est
     * référencé dans AUCUNE vente passée. Cela garantit l'intégrité des
     * rapports de ventes historiques.
     *
     * @param pId L'ID du produit à supprimer.
     * @throws Exception Si le produit est trouvé dans une ou plusieurs lignes
     * de vente, ou si le produit n'existe pas.
     */
    @Override
    public void supprimerProduitSnack(int pId) throws Exception {
        // --- Étape 1 : Validation de l'existence du produit ---
        if (produitSnackDAO.getProduitById(pId).isEmpty()) {
            throw new Exception("Impossible de supprimer : le produit avec l'ID " + pId + " n'existe pas.");
        }

        // --- Étape 2 : Vérification de l'intégrité des données (Règle de gestion) ---
        // On utilise la nouvelle méthode du DAO pour récupérer toutes les lignes de détail de toutes les ventes.
        List<Comporte> toutesLesLignesDeVente = comporteDAO.getAllLignesVente();

        // On parcourt chaque ligne de vente...
        for (Comporte ligne : toutesLesLignesDeVente) {
            // ...et si l'ID du produit à supprimer est trouvé dans l'une de ces lignes...
            if (ligne.getIdProduit() == pId) {
                // ...cela signifie que le produit a déjà été vendu. On ne peut pas le supprimer.
                // On lève une exception pour arrêter l'opération et informer l'utilisateur.
                throw new Exception("Impossible de supprimer le produit : il est référencé dans des ventes existantes (ex: Vente N°" + ligne.getIdVente() + ").");
            }
        }

        // --- Étape 3 : Suppression effective ---
        // Si la boucle se termine sans avoir levé d'exception, le produit peut être supprimé en toute sécurité.
        // On délègue l'opération de suppression au DAO correspondant.
        produitSnackDAO.deleteProduit(pId);
    }

    /**
     * Gère la transaction complète d'une vente de snacks effectuée au point de
     * vente. Cette méthode applique plusieurs règles de gestion critiques : 1.
     * Vérifie la validité de l'employé, de la caisse et du panier. 2. Vérifie
     * que le stock est suffisant pour TOUS les articles du panier AVANT de
     * commencer la transaction. 3. Si tout est valide, elle crée la vente, crée
     * chaque ligne de détail, et met à jour les stocks.
     *
     * @param pId L'ID du membre du personnel qui effectue la vente.
     * @param cId L'ID de la caisse où la vente a lieu.
     * @param panier Une Map contenant les produits à vendre et leur quantité.
     * @return L'objet VenteSnack créé, contenant l'ID et la date de la
     * transaction.
     * @throws Exception Si une règle de gestion est violée (ex: stock
     * insuffisant), une exception est levée pour annuler toute l'opération.
     */
    @Override
    public VenteSnack enregistrerVenteSnack(int pId, int cId, Map<ProduitSnack, Integer> panier) throws Exception {
        // --- Étape 1 : Validation des entrées ---
        // Règle métier : on ne peut pas enregistrer une vente sans employé, sans caisse, ou sans articles.
        if (personnelDAO.getPersonnelById(pId).isEmpty()) {
            throw new Exception("Personnel non trouvé. Vente annulée.");
        }
        if (caisseDAO.getCaisseById(cId).isEmpty()) {
            throw new Exception("Caisse non trouvée. Vente annulée.");
        }
        if (panier == null || panier.isEmpty()) {
            throw new Exception("Le panier est vide. Vente annulée.");
        }

        // --- Étape 2 : Vérification des stocks (Phase de pré-transaction) ---
        // Règle métier cruciale : on vérifie la disponibilité de TOUS les produits avant de modifier la moindre donnée.
        // Cela évite de se retrouver avec une vente à moitié validée si un seul article manque.
        for (Map.Entry<ProduitSnack, Integer> entry : panier.entrySet()) {
            ProduitSnack produitDansPanier = entry.getKey();
            Integer quantiteDemandee = entry.getValue();

            // On récupère la version la plus à jour du produit depuis la source de données pour avoir le stock réel.
            Optional<ProduitSnack> produitEnStockOpt = produitSnackDAO.getProduitById(produitDansPanier.getId());

            if (produitEnStockOpt.isEmpty()) {
                throw new Exception("Le produit '" + produitDansPanier.getNomProduit() + "' n'existe plus dans le système. Vente annulée.");
            }

            ProduitSnack produitEnStock = produitEnStockOpt.get();
            if (produitEnStock.getStock() < quantiteDemandee) {
                // Si UN SEUL produit n'a pas assez de stock, on annule TOUTE la vente immédiatement.
                throw new Exception("Stock insuffisant pour le produit '" + produitEnStock.getNomProduit() + "'. Vente annulée.");
            }
        }

        // --- Étape 3 : Création de la Vente (Début de la transaction) ---
        // Si toutes les vérifications ci-dessus sont passées, on peut commencer à enregistrer les données.
        int venteId = IdManager.getNextVenteSnackId();
        VenteSnack vente = new VenteSnack(venteId, LocalDateTime.now(), pId, cId);
        venteSnackDAO.addVenteSnack(vente); // On sauvegarde le "ticket de caisse" principal.

        // --- Étape 4 : Création des lignes de détail et mise à jour des stocks ---
        for (Map.Entry<ProduitSnack, Integer> entry : panier.entrySet()) {
            ProduitSnack produit = entry.getKey();
            Integer quantite = entry.getValue();

            // On crée la ligne de détail de la vente (ex: "2x Popcorn à 6.50€").
            Comporte ligneDeVente = new Comporte(venteId, produit.getId(), quantite, produit.getPrixVente());
            comporteDAO.addLigneVente(ligneDeVente);

            // On met à jour le stock de l'objet produit en mémoire.
            produit.setStock(produit.getStock() - quantite);
            // On sauvegarde le produit mis à jour dans le fichier pour que le stock soit correct pour la prochaine vente.
            produitSnackDAO.updateProduit(produit);
        }

        // La transaction est terminée avec succès. On retourne l'objet VenteSnack
        // qui contient toutes les informations sur la vente (ID, date, etc.).
        return vente;
    }
    // --- Méthodes de Reporting pour l'admin ---
    // Ces méthodes contiennent de la logique métier de calcul et d'agrégation de données.

    @Override
    public List<Billet> getBilletsPourFilm(int filmId) {
        List<Billet> billets = new ArrayList<>();
        for (Seance s : seanceDAO.getSeancesByFilmId(filmId)) {
            billets.addAll(billetDAO.getBilletsBySeanceId(s.getId()));
        }
        return billets;
    }

    @Override
    public double calculerChiffreAffairesPourFilm(int filmId) {
        double total = 0;
        for (Billet b : getBilletsPourFilm(filmId)) {
            Optional<Tarif> t = tarifDAO.getTarifById(b.getIdTarif());
            if (t.isPresent()) {
                total += t.get().getPrix();
            }
        }
        return total;
    }

    @Override
    public double calculerChiffreAffairesPourJour(LocalDate date) {
        double total = 0;
        for (Seance s : seanceDAO.getSeancesByDate(date)) {
            total += calculerChiffreAffairesReservationsPourSeance(s.getId());
        }
        return total;
    }

    @Override
    public double calculerChiffreAffairesReservationsPourSeance(int seanceId) {
        double total = 0;
        for (Billet b : billetDAO.getBilletsBySeanceId(seanceId)) {
            Optional<Tarif> t = tarifDAO.getTarifById(b.getIdTarif());
            if (t.isPresent()) {
                total += t.get().getPrix();
            }
        }
        return total;
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }

    @Override
    public List<VenteSnack> getVentesSnackParJour(LocalDate date) {
        return venteSnackDAO.getVentesByDate(date);
    }

    @Override
    public double calculerChiffreAffairesSnackPourJour(LocalDate date) {
        double total = 0;
        for (VenteSnack v : venteSnackDAO.getVentesByDate(date)) {
            total += calculerTotalPourVenteSnack(v);
        }
        return total;
    }

    @Override
    public List<VenteSnack> getAllVentesSnack() {
        return venteSnackDAO.getAllVentesSnack();
    }

    @Override
    public double calculerTotalPourVenteSnack(VenteSnack vente) {
        double total = 0;
        for (Comporte ligne : comporteDAO.getLignesByVenteId(vente.getIdVente())) {
            total += ligne.getQuantite() * ligne.getPrixUnitaire();
        }
        return total;
    }

    @Override
    public Optional<Caisse> getCaisseById(int caisseId) {
        return caisseDAO.getCaisseById(caisseId);
    }

    @Override
    public Optional<Personnel> getPersonnelById(int pId) {
        return personnelDAO.getPersonnelById(pId);
    }
}
