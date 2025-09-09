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
 * l'application. Cette classe est le "chef d'orchestre" : elle reçoit les
 * demandes de l'interface graphique (la Vue), interagit avec les différentes
 * classes DAO (la Couche d'Accès aux Données) pour manipuler les objets,
 * applique les règles de gestion (ex: un siège ne peut être vendu deux fois),
 * et retourne le résultat à la Vue.
 *
 * En implémentant à la fois ClientService et AdminService, elle expose des
 * fonctionnalités distinctes selon le type d'utilisateur, tout en partageant
 * une base de données commune.
 */
public class CinemaServiceImpl implements ClientService, AdminService {

    // --- Déclaration de toutes les dépendances envers la couche DAO ---
    // Chaque DAO est instancié une seule fois et conservé dans un attribut final.
    // Cela garantit que toute l'application travaille sur une seule et même "source de vérité".
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
     * Force le rechargement des données de toutes les sources. Utile pour
     * rafraîchir l'état de l'application sans la redémarrer.
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

    public List<Film> trouverFilmsAffiche() {
        return filmDAO.trouverTousLesFilms();
    }

    public Film trouverDetailsFilm(int idFilm) {
        return filmDAO.trouverFilmParId(idFilm);
    }

    public List<Film> rechercherFilmsParTitre(String motCle) {
        return filmDAO.rechercherFilmsParTitre(motCle);
    }

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
    @Override
    public Client creerCompteClient(String nom, String email, String motDePasse) throws Exception {
        // --- Validation des paramètres d'entrée ---
        if (nom == null || nom.trim().isEmpty()) {
            throw new Exception("Le nom du client est obligatoire.");
        }
        if (email == null || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new Exception("L'adresse email est invalide.");
        }
        if (motDePasse == null || motDePasse.length() < 4) {
            throw new Exception("Le mot de passe doit contenir au moins 4 caractères.");
        }
        // --- Fin de la validation ---

        if (clientDAO.trouverClientParEmail(email) != null) {
            throw new Exception("Un compte avec cet email existe déjà.");
        }

        Client nouveauClient = new Client(IdManager.obtenirProchainIdClient(), nom, email, motDePasse, LocalDate.now());
        clientDAO.ajouterClient(nouveauClient);
        return nouveauClient;
    }

    @Override
    public Client authentifierClient(String email, String motDePasse) {
        for (Client client : clientDAO.trouverTousLesClients()) {
            if (client.getEmail().equalsIgnoreCase(email) && client.getMotDePasse().equals(motDePasse)) {
                return client;
            }
        }
        return null;
    }

    @Override
    public void modifierCompteClient(Client client) throws Exception {
        validerClient(client);
        Client clientExistant = clientDAO.trouverClientParEmail(client.getEmail());
        if (clientExistant != null && clientExistant.getId() != client.getId()) {
            throw new Exception("Un autre compte utilise déjà cet email.");
        }
        clientDAO.mettreAJourClient(client);
    }

    @Override
    public void supprimerCompteClient(int idClient) throws Exception {
        for (Reservation reservation : reservationDAO.trouverReservationsParIdClient(idClient)) {
            annulerReservation(reservation.getId());
        }
        clientDAO.supprimerClientParId(idClient);
    }

    public List<Seance> trouverSeancesPourFilmEtDate(int idFilm, LocalDate date) {
        List<Seance> seancesCorrespondantes = new ArrayList<>();
        for (Seance seance : seanceDAO.trouverSeancesParDate(date)) {
            if (seance.getIdFilm() == idFilm) {
                seancesCorrespondantes.add(seance);
            }
        }
        return seancesCorrespondantes;
    }

    public List<Siege> trouverSiegesDisponibles(int seanceId) {
        Seance seance = seanceDAO.trouverSeanceParId(seanceId);
        if (seance == null) {
            return new ArrayList<>();
        }
        List<Siege> tousLesSieges = siegeDAO.trouverSiegesParIdSalle(seance.getIdSalle());
        List<Billet> billetsVendus = billetDAO.trouverBilletsParIdSeance(seanceId);

        // 1. On crée un ENSEMBLE (HashSet) des sièges occupés.
        // L'ajout est très rapide.
        Set<Integer> idsSiegesOccupes = new HashSet<>();
        for (Billet billet : billetsVendus) {
            idsSiegesOccupes.add(billet.getIdSiege());
        }

        List<Siege> siegesDisponibles = new ArrayList<>();
        // 2. Pour CHAQUE siège de la salle...
        for (Siege siege : tousLesSieges) {
            // 3. ... on vérifie INSTANTANÉMENT s'il est dans l'ensemble.
            // Cette opération est maintenant extrêmement performante.
            if (!idsSiegesOccupes.contains(siege.getId())) {
                siegesDisponibles.add(siege);
            }
        }
        return siegesDisponibles;
    }

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

    @Override
    public void ajusterStockProduit(int produitId, int quantiteAjustement) throws Exception {
        // 1. Récupérer l'objet ProduitSnack à partir de son ID.
        ProduitSnack produit = produitSnackDAO.trouverProduitParId(produitId);
        if (produit == null) {
            throw new Exception("Le produit avec l'ID " + produitId + " n'a pas été trouvé.");
        }

        // 2. Calculer le nouveau stock.
        int stockActuel = produit.getStock();
        int nouveauStock = stockActuel + quantiteAjustement;

        // 3. Valider que le stock ne devient pas négatif.
        if (nouveauStock < 0) {
            throw new Exception("Opération impossible : le stock ne peut pas devenir négatif.");
        }

        // 4. Mettre à jour l'objet et le sauvegarder.
        produit.setStock(nouveauStock);
        produitSnackDAO.mettreAJourProduit(produit);
    }

    // --- DEBUT DE L'AJOUT ---
    @Override
    public VenteSnack trouverVenteSnackReservation(int reservationId) {
        // Le service délègue simplement l'appel au DAO compétent.
        return venteSnackDAO.trouverVenteParIdReservation(reservationId);
    }

    @Override
    public List<LigneVente> trouverLignesParIdVente(int venteId) {
        // Le service délègue l'appel au DAO qui gère les lignes de vente.
        return ligneVenteDAO.trouverLignesParIdVente(venteId);
    }

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

        return resultat;
    }

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
        return resultatFinal;
    }

    @Override
    public List<Siege> trouverSiegesPourSalle(int salleId) {
        return siegeDAO.trouverSiegesParIdSalle(salleId);
    }

    @Override
    public List<Billet> trouverBilletsPourSeance(int seanceId) {
        return billetDAO.trouverBilletsParIdSeance(seanceId);
    }

    @Override
    public void annulerReservation(int reservationId) throws Exception {
        if (reservationDAO.trouverReservationParId(reservationId) == null) {
            throw new Exception("Réservation non trouvée.");
        }
        billetDAO.supprimerBilletsParIdReservation(reservationId);
        reservationDAO.supprimerReservationParId(reservationId);
    }

    @Override
    public List<Reservation> trouverHistoriqueReservationsClient(int clientId) {
        return reservationDAO.trouverReservationsParIdClient(clientId);
    }

    @Override
    public void ajouterEvaluation(EvaluationClient evaluation) throws Exception {
        if (aDejaEvalue(evaluation.getIdClient(), evaluation.getIdFilm())) {
            throw new Exception("Vous avez déjà noté ce film.");
        }
        evaluationClientDAO.ajouterEvaluation(evaluation);
    }

    public void modifierEvaluation(EvaluationClient evaluation) throws Exception {
        evaluationClientDAO.mettreAJourEvaluation(evaluation);
    }

    @Override
    public boolean aDejaEvalue(int clientId, int filmId) {
        return evaluationClientDAO.trouverEvaluationParClientEtFilm(clientId, filmId) != null;
    }

    @Override
    public EvaluationClient trouverEvaluation(int clientId, int filmId) {
        return evaluationClientDAO.trouverEvaluationParClientEtFilm(clientId, filmId);
    }

    @Override
    public List<EvaluationClient> trouverEvaluationsParFilmId(int filmId) {
        return evaluationClientDAO.trouverEvaluationsParIdFilm(filmId);
    }

    public VenteSnack enregistrerVenteSnackPourClient(int idPersonnel, int idCaisse, List<LignePanier> panier, Integer idClient) throws Exception {
        int venteId = IdManager.obtenirProchainIdVenteSnack();
        // La différence est ici : on passe l'idClient au constructeur
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

    @Override
    public Reservation finaliserCommandeComplete(int clientId, int seanceId, List<Integer> siegeIds, int tarifId, List<LignePanier> panierSnacks) throws Exception {
        // Étape 1 : La réservation des billets est créée.
        Reservation reservation = effectuerReservation(clientId, seanceId, siegeIds, tarifId);

        // Étape 2 : Si des snacks sont commandés, on les traite.
        if (panierSnacks != null && !panierSnacks.isEmpty()) {

            // CORRECTION : Appel à la nouvelle méthode dédiée aux clients en ligne.
            // On utilise l'ID 0 (Système) et la Caisse 0 (Canal Web), et on passe le clientId.
            VenteSnack venteSnack = enregistrerVenteSnackPourClient(0, 0, panierSnacks, clientId);

            // Étape 3 : On lie la vente de snacks à la réservation de billets.
            venteSnack.setIdReservation(reservation.getId());

            // Étape 4 : On sauvegarde cette liaison dans la base de données.
            venteSnackDAO.mettreAJourVenteSnack(venteSnack);
        }

        return reservation;
    }

    @Override
    public List<Tarif> trouverTousLesTarifs() {
        return tarifDAO.trouverTousLesTarifs();
    }

    @Override
    public Tarif trouverTarifParId(int tarifId) {
        // Le service délègue simplement l'appel au DAO compétent.
        return tarifDAO.trouverTarifParId(tarifId);
    }

    // Dans le fichier CinemaServiceImpl.java
    @Override
    public Client trouverClientParEmail(String email) {
        // 1. Récupérer la liste de TOUS les clients via le DAO.
        //    Le service n'a pas les données lui-même, il les demande à la couche d'accès aux données.
        List<Client> tousLesClients = clientDAO.trouverTousLesClients();

        // 2. Parcourir chaque client dans la liste.
        //    On utilise une boucle "for-each", comme vu dans le cours sur les Collections (p. 214).
        for (Client client : tousLesClients) {

            // 3. Comparer l'e-mail du client courant avec celui recherché.
            //    On utilise equalsIgnoreCase pour que la recherche ne soit pas sensible à la casse (ex: "Test@a.com" sera égal à "test@a.com").
            //    C'est une bonne pratique pour les identifiants de type e-mail.
            if (client.getEmail().equalsIgnoreCase(email)) {

                // 4. Si on trouve une correspondance, on retourne immédiatement cet objet Client.
                //    La recherche s'arrête ici.
                return client;
            }
        }

        // 5. Si la boucle se termine sans avoir trouvé de client, cela signifie qu'aucun compte ne correspond.
        //    On retourne 'null' pour signaler que le client n'a pas été trouvé.
        return null;
    }

    @Override
    public List<Salle> trouverToutesLesSalles() {
        return salleDAO.trouverToutesLesSalles();
    }

    @Override
    public Client trouverClientParId(int clientId) {
        return clientDAO.trouverClientParId(clientId);
    }

    @Override
    public List<Billet> trouverBilletsParIdReservation(int reservationId) {
        return billetDAO.trouverBilletsParIdReservation(reservationId);
    }

    @Override
    public Seance trouverSeanceParId(int seanceId) {
        return seanceDAO.trouverSeanceParId(seanceId);
    }

    @Override
    public List<ProduitSnack> trouverTousLesProduits() {
        return produitSnackDAO.trouverTousLesProduits();
    }

    // --- SECTION ADMIN ---
    @Override
    public Personnel authentifierPersonnel(String email, String motDePasse) {
        for (Personnel p : personnelDAO.trouverToutLePersonnel()) {
            // On compare maintenant l'email, en ignorant la casse (bonne pratique)
            if (p.getEmail().equalsIgnoreCase(email) && p.getMotDePasse().equals(motDePasse)) {
                return p;
            }
        }
        return null;
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

    /**
     *
     * @param film
     * @throws Exception
     */
    @Override
    public void ajouterFilm(Film film) throws Exception {
        // On valide l'objet Film AVANT de l'ajouter.
        // Si le film n'est pas valide, une exception est lancée et l'exécution s'arrête ici.
        validerFilm(film);

        // Si la validation passe, on procède à l'ajout.
        filmDAO.ajouterFilm(film);
    }

    @Override
    public void mettreAJourFilm(Film film) throws Exception {
        // On valide également l'objet AVANT de le mettre à jour.
        validerFilm(film);

        // Si la validation passe, on procède à la mise à jour.
        filmDAO.mettreAJourFilm(film);
    }

    @Override
    public void supprimerFilm(int filmId) throws Exception {
        // --- VÉRIFICATION DE LA LOGIQUE MÉTIER ---
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

    @Override
    public void ajouterSeance(Seance seance) throws Exception {
        // --- VÉRIFICATION DE LA LOGIQUE MÉTIER ---

        // 1. Récupérer le film pour connaître sa durée.
        Film film = filmDAO.trouverFilmParId(seance.getIdFilm());
        if (film == null) {
            throw new Exception("Le film associé à cette séance n'existe pas.");
        }

        // 2. Calculer l'heure de début de la NOUVELLE séance.
        LocalDateTime debutNouvelleSeance = seance.getDateHeureDebut();

        // 3. Valider que la séance n'est pas programmée dans le passé.
        if (debutNouvelleSeance.isBefore(LocalDateTime.now())) {
            throw new Exception("Impossible de planifier une séance dans le passé.");
        }

        // 4. Calculer l'heure de fin d'occupation de la salle (incluant le temps de battement).
        LocalDateTime finNouvelleSeance = debutNouvelleSeance.plusMinutes(film.getDureeMinutes() + TEMPS_BATTEMENT_MINUTES);

        // 5. Récupérer toutes les séances existantes pour la même salle pour comparaison.
        List<Seance> seancesExistantesDansLaSalle = new ArrayList<>();
        for (Seance s : seanceDAO.trouverToutesLesSeances()) {
            if (s.getIdSalle() == seance.getIdSalle()) {
                seancesExistantesDansLaSalle.add(s);
            }
        }

        // 6. Parcourir chaque séance existante pour détecter un conflit d'horaire.
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

        // --- FIN DE LA VÉRIFICATION ---
        // Si aucune exception n'a été levée, on peut ajouter la séance en toute sécurité.
        seanceDAO.ajouterSeance(seance);
    }

    @Override
    public void modifierSeance(Seance seance) throws Exception {
        // --- VÉRIFICATION DE LA LOGIQUE MÉTIER (copiée de addSeance) ---
        Film film = filmDAO.trouverFilmParId(seance.getIdFilm());
        if (film == null) {
            throw new Exception("Film non trouvé.");
        }

        LocalDateTime debutNouvelleSeance = seance.getDateHeureDebut();
        LocalDateTime finNouvelleSeance = debutNouvelleSeance.plusMinutes(film.getDureeMinutes());

        for (Seance seanceExistante : seanceDAO.trouverToutesLesSeances()) {
            // CORRECTION CRUCIALE : On ne compare pas une séance avec elle-même !
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

    @Override
    public void supprimerSeance(int seanceId) throws Exception {
        // --- VÉRIFICATION DE LA LOGIQUE MÉTIER ---
        // On vérifie si des billets ont déjà été vendus pour cette séance.
        List<Billet> billetsPourLaSeance = billetDAO.trouverBilletsParIdSeance(seanceId);
        if (!billetsPourLaSeance.isEmpty()) {
            // S'il y a au moins un billet, on lève une exception.
            throw new Exception("Impossible de supprimer cette séance car " + billetsPourLaSeance.size() + " billet(s) ont déjà été vendus.");
        }

        // --- FIN DE LA VÉRIFICATION ---
        // Si aucun billet n'a été vendu, on peut supprimer la séance.
        seanceDAO.supprimerSeanceParId(seanceId);
    }

    @Override
    public List<Seance> trouverToutesLesSeances() {
        return seanceDAO.trouverToutesLesSeances();
    }

    @Override
    public void modifierSalle(Salle salle) throws Exception {
        validerSalle(salle);

        // Règle d'unicité pour la modification
        for (Salle s : salleDAO.trouverToutesLesSalles()) {
            if (s.getId() != salle.getId() && s.getNumero().equalsIgnoreCase(salle.getNumero())) {
                throw new Exception("Une autre salle avec le numéro '" + salle.getNumero() + "' existe déjà.");
            }
        }
        salleDAO.mettreAJourSalle(salle);
    }

    @Override
    public void supprimerSalle(int salleId) throws Exception {
        // --- VÉRIFICATION DE LA LOGIQUE MÉTIER ---
        // On vérifie si la salle n'est pas encore utilisée dans une séance.
        for (Seance seance : seanceDAO.trouverToutesLesSeances()) {
            if (seance.getIdSalle() == salleId) {
                // Si une séance utilise cette salle, on bloque la suppression.
                throw new Exception("Impossible de supprimer cette salle car elle est encore utilisée pour une séance le "
                        + seance.getDateHeureDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm")) + ".");
            }
        }

        // --- FIN DE LA VÉRIFICATION ---
        salleDAO.supprimerSalleParId(salleId);
    }

    @Override
    public void ajouterTarif(Tarif tarif) throws Exception {
        validerTarif(tarif); // <<<--- APPEL DE VALIDATION AJOUTÉ

        // Règle d'unicité
        for (Tarif t : tarifDAO.trouverTousLesTarifs()) {
            if (t.getLibelle().equalsIgnoreCase(tarif.getLibelle())) {
                throw new Exception("Un tarif avec le libellé '" + tarif.getLibelle() + "' existe déjà.");
            }
        }
        tarifDAO.ajouterTarif(tarif);
    }

    @Override
    public void modifierTarif(Tarif tarif) throws Exception {
        validerTarif(tarif);

        // Règle d'unicité pour la modification
        for (Tarif t : tarifDAO.trouverTousLesTarifs()) {
            if (t.getId() != tarif.getId() && t.getLibelle().equalsIgnoreCase(tarif.getLibelle())) {
                throw new Exception("Un autre tarif avec le libellé '" + tarif.getLibelle() + "' existe déjà.");
            }
        }
        tarifDAO.mettreAJourTarif(tarif);
    }

    @Override
    public void supprimerTarif(int tarifId) {
        tarifDAO.supprimerTarifParId(tarifId);
    }

    @Override
    public List<Role> trouverTousLesRoles() {
        return roleDAO.trouverTousLesRoles();
    }

    @Override
    public void ajouterPersonnel(Personnel p) {
        personnelDAO.ajouterPersonnel(p);
    }

    @Override
    public void modifierPersonnel(Personnel p) {
        personnelDAO.mettreAJourPersonnel(p);
    }

    @Override
    public void supprimerPersonnel(int pId) {
        personnelDAO.supprimerPersonnelParId(pId);
    }

    @Override
    public List<Personnel> trouverToutLePersonnel() {
        return personnelDAO.trouverToutLePersonnel();
    }

    /**
    @Override
    public void affecterPersonnelASeance(int pId, int sId) throws Exception {
        affectationSeanceDAO.ajouterAffectation(new AffectationSeance(sId, pId));
    }

    @Override
    public void desaffecterPersonnelDeSeance(int pId, int sId) throws Exception {
        affectationSeanceDAO.supprimerAffectation(sId, pId);
    }
    * */

    @Override
    public Planning creerPlanning(int pId, LocalDateTime d, LocalDateTime f, String poste) throws Exception {
        Planning p = new Planning(IdManager.obtenirProchainIdPlanning(), d, f, poste, pId);
        planningDAO.ajouterPlanning(p);
        return p;
    }

    @Override
    public List<Planning> trouverPlanningPourPersonnel(int pId) {
        return planningDAO.trouverPlanningsParIdPersonnel(pId);
    }

    @Override
    public void ajouterProduitSnack(ProduitSnack produit) throws Exception {
        validerProduitSnack(produit);

        // Règle d'unicité (optionnelle mais recommandée)
        for (ProduitSnack p : produitSnackDAO.trouverTousLesProduits()) {
            if (p.getNomProduit().equalsIgnoreCase(produit.getNomProduit())) {
                throw new Exception("Un produit avec le nom '" + produit.getNomProduit() + "' existe déjà.");
            }
        }
        produitSnackDAO.ajouterProduit(produit);
    }

    @Override
    public void modifierProduitSnack(ProduitSnack produit) throws Exception {
        validerProduitSnack(produit);

        // Règle d'unicité pour la modification
        for (ProduitSnack p : produitSnackDAO.trouverTousLesProduits()) {
            if (p.getId() != produit.getId() && p.getNomProduit().equalsIgnoreCase(produit.getNomProduit())) {
                throw new Exception("Un autre produit avec le nom '" + produit.getNomProduit() + "' existe déjà.");
            }
        }
        produitSnackDAO.mettreAJourProduit(produit);
    }

    @Override
    public void supprimerProduitSnack(int produitId) throws Exception {
        List<LigneVente> toutesLesLignes = ligneVenteDAO.trouverToutesLesLignesVente();
        // La boucle est plus simple et exprime plus clairement l'intention :
        // "Pour chaque ligne dans toutes les lignes de vente..."
        for (LigneVente ligne : toutesLesLignes) {
            if (ligne.getIdProduit() == produitId) {
                throw new IllegalStateException("Impossible de supprimer le produit ID " + produitId + " car il est lié à des ventes existantes.");
            }
        }
        produitSnackDAO.supprimerProduitParId(produitId);
    }

    // CORRECTION : Version de enregistrerVenteSnack acceptant List<LignePanier>
    @Override
    public VenteSnack enregistrerVenteSnack(int idPersonnel, int idCaisse, List<LignePanier> panier) throws Exception {
        int venteId = IdManager.obtenirProchainIdVenteSnack();
        // Vente pour client anonyme, donc idClient est null
        VenteSnack nouvelleVente = new VenteSnack(venteId, LocalDateTime.now(), idPersonnel, idCaisse, null);

        // On sauvegarde d'abord la vente elle-même
        venteSnackDAO.ajouterVenteSnack(nouvelleVente);

        // On parcourt ensuite chaque ligne du panier
        for (LignePanier ligne : panier) {
            ProduitSnack produit = ligne.getProduit();
            int quantite = ligne.getQuantite();

            // Vérification cruciale du stock
            if (produit.getStock() < quantite) {
                // Si le stock est insuffisant, on annule toute la transaction
                throw new Exception("Stock insuffisant pour " + produit.getNomProduit() + ". Vente annulée.");
            }

            // On crée la ligne de détail de la vente
            LigneVente ligneDeVente = new LigneVente(venteId, produit.getId(), quantite, produit.getPrixVente());
            ligneVenteDAO.ajouterLigneVente(ligneDeVente);

            // On met à jour le stock du produit
            produit.setStock(produit.getStock() - quantite);
            produitSnackDAO.mettreAJourProduit(produit);
        }
        return nouvelleVente;
    }

    // --- SECTION REPORTING ---
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

    @Override
    public double calculerChiffreAffairesPourJour(LocalDate date) {
        double total = 0;
        for (Seance s : seanceDAO.trouverSeancesParDate(date)) {
            total += calculerChiffreAffairesReservationsPourSeance(s.getId());
        }
        return total;
    }

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

    @Override
    public List<Reservation> trouverToutesLesReservations() {
        return reservationDAO.trouverToutesLesReservations();
    }

    @Override
    public List<VenteSnack> trouverVentesSnackParDate(LocalDate date) {
        return venteSnackDAO.trouverVentesParDate(date);
    }

    @Override
    public double calculerChiffreAffairesSnackPourJour(LocalDate date) {
        double total = 0;
        for (VenteSnack v : venteSnackDAO.trouverVentesParDate(date)) {
            total += calculerTotalPourVenteSnack(v);
        }
        return total;
    }

    @Override
    public List<VenteSnack> trouverToutesLesVentesSnack() {
        return venteSnackDAO.trouverToutesLesVentesSnack();
    }

    @Override
    public double calculerTotalPourVenteSnack(VenteSnack vente) {
        double total = 0;
        for (LigneVente ligne : ligneVenteDAO.trouverLignesParIdVente(vente.getIdVente())) {
            total += ligne.getQuantite() * ligne.getPrixUnitaire();
        }
        return total;
    }

    @Override
    public Caisse trouverCaisseParId(int caisseId) {
        return caisseDAO.trouverCaisseParId(caisseId);
    }

    @Override
    public Personnel trouverPersonnelParId(int pId) {
        return personnelDAO.trouverPersonnelParId(pId);
    }

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

        // 2. Génération des sièges sur la base des paramètres fournis
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

    // --- Gestion des Genres ---
    @Override
    public List<Genre> trouverTousLesGenres() {
        return genreDAO.trouverTousLesGenres();
    }

    @Override
    public void ajouterGenre(Genre genre) {
        genreDAO.ajouterGenre(genre);
    }

    @Override
    public void modifierGenre(Genre genre) {
        genreDAO.mettreAJourGenre(genre);
    }

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

    // AJOUTEZ CES MÉTHODES DE VALIDATION DANS CinemaServiceImpl
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

    private void validerSalle(Salle salle) throws Exception {
        if (salle.getNumero() == null || salle.getNumero().trim().isEmpty()) {
            throw new Exception("Le numéro/nom de la salle est obligatoire.");
        }
        if (salle.getCapacite() <= 0) {
            throw new Exception("La capacité de la salle doit être positive.");
        }
    }

    private void validerTarif(Tarif tarif) throws Exception {
        if (tarif.getLibelle() == null || tarif.getLibelle().trim().isEmpty()) {
            throw new Exception("Le libellé du tarif est obligatoire.");
        }
        if (tarif.getPrix() < 0) {
            throw new Exception("Le prix du tarif ne peut pas être négatif.");
        }
    }

    // Dans CinemaServiceImpl.java
    @Override
    public void changerMotDePasseClient(int clientId, String nouveauMotDePasse) throws Exception {
        Client client = clientDAO.trouverClientParId(clientId);
        if (client == null) {
            throw new Exception("Client non trouvé.");
        }
        client.setMotDePasse(nouveauMotDePasse);
        clientDAO.mettreAJourClient(client);
    }

    @Override
    public Personnel trouverPersonnelParEmail(String email) {
        for (Personnel p : personnelDAO.trouverToutLePersonnel()) {
            if (p.getEmail().equalsIgnoreCase(email)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void changerMotDePassePersonnel(int personnelId, String nouveauMotDePasse) throws Exception {
        Personnel personnel = personnelDAO.trouverPersonnelParId(personnelId);
        if (personnel == null) {
            throw new Exception("Membre du personnel non trouvé.");
        }
        personnel.setMotDePasse(nouveauMotDePasse);
        personnelDAO.mettreAJourPersonnel(personnel);
    }

  
  
}
