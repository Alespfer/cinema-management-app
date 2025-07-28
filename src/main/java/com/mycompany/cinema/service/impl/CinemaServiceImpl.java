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
import java.util.List;
import java.util.Optional;

public class CinemaServiceImpl implements ClientService, AdminService {

    // Le service instancie tous les DAOs. C'est son arsenal d'outils.
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
    private final RoleDAO roleDAO = new RoleDAOImpl(); // DAO MANQUANT AJOUTÉ
    private final GenreDAO genreDAO = new GenreDAOImpl(); // Assurez-vous qu'il est bien instancie
    private final ProduitSnackDAO produitSnackDAO = new ProduitSnackDAOImpl(); // Assurez-vous qu'il est bien instancié
    private final EvaluationClientDAO evaluationClientDAO = new EvaluationClientDAOImpl();


    // =========================================================================
    // SECTION COMMUNE (implémentation de la base CinemaService)
    // =========================================================================
    
    
    @Override
    public List<Film> getFilmsAffiche() {
        return filmDAO.getAllFilms();
    }
    
    
    @Override
    public Film getFilmDetails(int filmId) {
        return filmDAO.getFilmById(filmId).orElse(null);
    }

    @Override
    public List<Film> findFilmsByTitre(String keyword) {
        return filmDAO.findFilmsByTitre(keyword);
    }
    
     @Override
    public List<Genre> getAllGenres() {
        return genreDAO.getAllGenres();
    }

    @Override
    public List<Seance> rechercherSeances(LocalDate date, Integer genreId, String titreKeyword) {
        List<Seance> resultatFinal = new ArrayList<>();
        List<Seance> seancesSource = seanceDAO.getAllSeances(); // On part de toutes les séances

        for (Seance seance : seancesSource) {
            boolean correspond = true;

            // Filtre 1 : La date (si fournie)
            if (date != null) {
                if (!seance.getDateHeureDebut().toLocalDate().isEqual(date)) {
                    correspond = false;
                }
            }

            // Filtres 2 & 3 : Le titre et le genre (liés au film)
            if (correspond) { // Inutile de vérifier le film si la date ne correspond déjà pas
                Optional<Film> filmOpt = filmDAO.getFilmById(seance.getIdFilm());
                if (filmOpt.isPresent()) {
                    Film film = filmOpt.get();
                    
                    // Filtre par mot-clé dans le titre
                    if (titreKeyword != null && !titreKeyword.trim().isEmpty()) {
                        if (!film.getTitre().toLowerCase().contains(titreKeyword.toLowerCase())) {
                            correspond = false;
                        }
                    }
                    
                    // Filtre par genre
                    if (correspond && genreId != null) {
                        boolean genreTrouve = false;
                        // Attention : la liste des genres peut être null si non initialisée dans le constructeur
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
                    correspond = false; // Le film associé n'existe pas, on ignore la séance
                }
            }
            
            if (correspond) {
                resultatFinal.add(seance);
            }
        }
        return resultatFinal;
    }
       
  
    
    // =========================================================================
    // SECTION CLIENT (implémentation de ClientService)
    // =========================================================================

    /**
     * Crée un compte client en vérifiant qu'aucun autre client n'utilise le même email.
     * @throws Exception si un client avec le même email existe déjà.
     */
    @Override
    public Client creerCompteClient(String nom, String email, String motDePasse) throws Exception {
        for (Client clientExistant : clientDAO.getAllClients()) {
            if (clientExistant.getEmail().equalsIgnoreCase(email)) {
                throw new Exception("Un compte avec cet email existe déjà.");
            }
        }
        int newId = IdManager.getNextClientId();
        Client nouveauClient = new Client(newId, nom, email, motDePasse, LocalDate.now());
        clientDAO.addClient(nouveauClient);
        return nouveauClient;
    }

    /**
     * Vérifie les identifiants du client et retourne un Optional avec le client si trouvé.
     */
    @Override
    public Optional<Client> authentifierClient(String email, String motDePasse) {
        for (Client client : clientDAO.getAllClients()) {
            if (client.getEmail().equalsIgnoreCase(email) && client.getMotDePasse().equals(motDePasse)) {
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }
    
    /**
     * Modifie les informations d'un client existant après vérification de son existence.
     * @throws Exception si le client n'existe pas.
     */

    @Override
    public void modifierCompteClient(Client client) throws Exception {
        if (clientDAO.getClientById(client.getId()).isEmpty()) {
            throw new Exception("Client non trouvé.");
        }
        clientDAO.updateClient(client);
    }

    /**
     * Supprime un compte client ainsi que toutes ses réservations associées.
     * @throws Exception si le client est introuvable.
     */

    @Override
    public void supprimerCompteClient(int clientId) throws Exception {
        if (clientDAO.getClientById(clientId).isEmpty()) {
            throw new Exception("Client non trouvé.");
        }
        List<Reservation> reservationsDuClient = reservationDAO.getReservationsByClientId(clientId);
        for (Reservation reservation : reservationsDuClient) {
            this.annulerReservation(reservation.getId());
        }
        clientDAO.deleteClient(clientId);
    }
    
    
    /**
    * Retourne toutes les séances correspondant à un film donné à une date donnée.
    */
    @Override
    public List<Seance> getSeancesPourFilmEtDate(int filmId, LocalDate date) {
        List<Seance> seancesDuJour = seanceDAO.getSeancesByDate(date);
        List<Seance> seancesResultat = new ArrayList<>();
        for (Seance seance : seancesDuJour) {
            if (seance.getIdFilm() == filmId) {
                seancesResultat.add(seance);
            }
        }
        return seancesResultat;
    }
    
    
    /**
    * Retourne les séances correspondant aux critères fournis (date, film, salle).
    */
     @Override
    public List<Seance> findSeancesFiltrees(LocalDate date, Integer filmId, Integer salleId) {
        List<Seance> resultat = new ArrayList<>();
        List<Seance> seancesInitiales;
        if (date != null) {
            seancesInitiales = seanceDAO.getSeancesByDate(date);
        } else {
            seancesInitiales = seanceDAO.getAllSeances();
        }

        for (Seance seance : seancesInitiales) {
            boolean correspondAuxCriteres = true;
            if (filmId != null && seance.getIdFilm() != filmId) {
                correspondAuxCriteres = false;
            }
            if (correspondAuxCriteres && salleId != null && seance.getIdSalle() != salleId) {
                correspondAuxCriteres = false;
            }
            if (correspondAuxCriteres) {
                resultat.add(seance);
            }
        }
        return resultat;
    }

    // Retourne la liste des sièges associés à une salle donnée
    @Override
    public List<Siege> getSiegesPourSalle(int salleId) {
        return siegeDAO.getSiegesBySalleId(salleId);
    }
    
    // Retourne la liste des billets vendus pour une séance
    @Override
    public List<Billet> getBilletsPourSeance(int seanceId) {
        return billetDAO.getBilletsBySeanceId(seanceId);
    }

    // Retourne les sièges disponibles (non réservés) pour une séance donnée
    @Override
    public List<Siege> getSiegesDisponibles(int seanceId) {
        Optional<Seance> seanceOpt = seanceDAO.getSeanceById(seanceId);
        if (seanceOpt.isEmpty()) {
            return new ArrayList<>();
        }
        Seance seance = seanceOpt.get();
        List<Siege> tousLesSieges = siegeDAO.getSiegesBySalleId(seance.getIdSalle());
        List<Billet> billetsVendus = getBilletsPourSeance(seanceId);
        List<Integer> idsSiegesOccupes = new ArrayList<>();
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
    * Permet à un client de réserver un ou plusieurs sièges pour une séance donnée.
    * Effectue toutes les vérifications nécessaires (disponibilité, cohérence).
    */
    @Override
    public Reservation effectuerReservation(int clientId, int seanceId, List<Integer> siegeIds, int tarifId) throws Exception {
        if (clientDAO.getClientById(clientId).isEmpty()) throw new Exception("Client non trouvé.");
        if (tarifDAO.getTarifById(tarifId).isEmpty()) throw new Exception("Tarif non trouvé.");
        
        Optional<Seance> seanceOpt = seanceDAO.getSeanceById(seanceId);
        if (seanceOpt.isEmpty()) throw new Exception("Séance non trouvée.");
        Seance seance = seanceOpt.get();

        Optional<Salle> salleOpt = salleDAO.getSalleById(seance.getIdSalle());
        if (salleOpt.isEmpty()) throw new Exception("Salle associée à la séance non trouvée.");
        Salle salle = salleOpt.get();
        
        if (siegeIds.size() > salle.getCapacite()) {
            throw new Exception("Le nombre de sièges demandés (" + siegeIds.size() + ") dépasse la capacité de la salle (" + salle.getCapacite() + ").");
        }
        
        List<Siege> siegesDisponibles = getSiegesDisponibles(seanceId);
        List<Integer> siegesDisponiblesIds = new ArrayList<>();
        for (Siege s : siegesDisponibles) {
            siegesDisponiblesIds.add(s.getId());
        }
        for (Integer siegeId : siegeIds) {
            if (!siegesDisponiblesIds.contains(siegeId)) {
                throw new Exception("Le siège " + siegeId + " n'est plus disponible.");
            }
        }

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

    /**
    * Permet d'annuler une réservation (suppression des billets et de la réservation).
    */
    @Override
    public void annulerReservation(int reservationId) throws Exception {
        if (reservationDAO.getReservationById(reservationId).isEmpty()) {
            throw new Exception("Réservation non trouvée.");
        }
        billetDAO.deleteBilletsByReservationId(reservationId);
        reservationDAO.deleteReservation(reservationId);
    }

    /**
    * Retourne l'historique de toutes les réservations effectuées par un client.
    */

    @Override
    public List<Reservation> getHistoriqueReservationsClient(int clientId) {
        return reservationDAO.getReservationsByClientId(clientId);
    }

    // =========================================================================
    // SECTION ADMINISTRATEUR (implémentation de AdminService)
    // =========================================================================
    /**
    * Authentifie un membre du personnel à partir de son nom d'utilisateur et mot de passe.
    * Renvoie un Optional<Personnel> si les identifiants sont valides.
    */
    @Override
    public Optional<Personnel> authentifierPersonnel(String nomUtilisateur, String motDePasse) {
        for (Personnel p : personnelDAO.getAllPersonnel()) {
            if (p.getNom().equalsIgnoreCase(nomUtilisateur) && p.getMotDePasse().equals(motDePasse)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }
    
    
    /**
    * Ajoute un nouveau film à la base.
    * L'ID est généré automatiquement.
    */
    @Override
    public void ajouterFilm(Film film) {
        film.setId(IdManager.getNextFilmId());
        filmDAO.addFilm(film);
    }

    /**
    * Met à jour les informations d'un film existant.
    */
    @Override
    public void mettreAJourFilm(Film film) {
        filmDAO.updateFilm(film);
    }

    /**
    * Supprime un film uniquement s'il n'est pas associé à une séance existante.
    */
    @Override
    public void supprimerFilm(int filmId) throws Exception {
        if (!seanceDAO.getSeancesByFilmId(filmId).isEmpty()) {
            throw new Exception("Impossible de supprimer un film programmé dans une séance.");
        }
        filmDAO.deleteFilm(filmId);
    }

    /**
    * Ajoute une nouvelle séance après vérification des conflits dans la salle.
    */
    @Override
    public void ajouterSeance(Seance seance) throws Exception {
        verifierConflitPlanning(seance);
        seance.setId(IdManager.getNextSeanceId());
        seanceDAO.addSeance(seance);
    }

    /**
    * Met à jour une séance existante après vérification des conflits.
    */
    @Override
    public void modifierSeance(Seance seance) throws Exception {
        verifierConflitPlanning(seance);
        seanceDAO.updateSeance(seance);
    }

    /**
    * Supprime une séance uniquement si aucun billet n'y est associé.
    */
    @Override
    public void supprimerSeance(int seanceId) throws Exception {
        if (!billetDAO.getBilletsBySeanceId(seanceId).isEmpty()) {
            throw new Exception("Impossible de supprimer une séance avec des billets vendus.");
        }
        seanceDAO.deleteSeance(seanceId);
    }

    /**
    * Retourne toutes les séances (utile côté admin).
    */
    @Override
    public List<Seance> getAllSeances() {
        return seanceDAO.getAllSeances();
    }
    
    /**
    * Ajoute une nouvelle salle dans la base avec un ID généré automatiquement.
    */

    @Override
    public void ajouterSalle(Salle salle) {
        salle.setId(IdManager.getNextSalleId());
        salleDAO.addSalle(salle);
    }

    /**
    * Met à jour les informations d'une salle existante.
    */
    @Override
    public void modifierSalle(Salle salle) {
        salleDAO.updateSalle(salle);
    }
    
    
    /**
    * Supprime une salle uniquement si elle n'est pas utilisée dans une séance.
    */
    @Override
    public void supprimerSalle(int salleId) throws Exception {
        boolean isUsed = false;
        for (Seance seance : seanceDAO.getAllSeances()) {
            if (seance.getIdSalle() == salleId) {
                isUsed = true;
                break;
            }
        }
        if (isUsed) {
            throw new Exception("Impossible de supprimer une salle utilisée dans une séance.");
        }
        salleDAO.deleteSalle(salleId);
    }

    /**
     * Retourne la liste de toutes les salles enregistrées.
     */
    @Override
    public List<Salle> getAllSalles() {
        return salleDAO.getAllSalles();
    }
    
    /**
     * Ajoute un nouveau tarif après vérification que le prix est valide (non négatif).
     */
    @Override
    public void ajouterTarif(Tarif tarif) throws Exception {
        if (tarif.getPrix() < 0) {
            throw new Exception("Le prix d'un tarif ne peut pas être négatif.");
        }
        tarif.setId(IdManager.getNextTarifId());
        tarifDAO.addTarif(tarif);
    }

    /**
    * Met à jour un tarif existant, après validation du prix.
    */

    @Override
    public void modifierTarif(Tarif tarif) throws Exception {
        if (tarif.getPrix() < 0) {
            throw new Exception("Le prix d'un tarif ne peut pas être négatif.");
        }
        tarifDAO.updateTarif(tarif);
    }

    /**
    * Supprime un tarif sans contrainte particulière.
    */
    @Override
    public void supprimerTarif(int tarifId) {
        tarifDAO.deleteTarif(tarifId);
    }
    
    /**
    * Retourne tous les tarifs disponibles.
    */
    @Override
    public List<Tarif> getAllTarifs() {
        return tarifDAO.getAllTarifs();
    }
    
    
    /**
    * Retourne la liste de tous les rôles (utile pour les interfaces d'administration).
    */

    @Override
    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }

    /**
    * Ajoute un nouveau membre du personnel avec un ID généré automatiquement.
    */
    @Override
    public void ajouterPersonnel(Personnel personnel) {
        personnel.setId(IdManager.getNextPersonnelId());
        personnelDAO.addPersonnel(personnel);
    }
    
    
    /**
    * Met à jour les informations d’un personnel existant.
    */

    @Override
    public void modifierPersonnel(Personnel personnel) {
        personnelDAO.updatePersonnel(personnel);
    }

    /**
    * Supprime un membre du personnel par son ID.
    */

    @Override
    public void supprimerPersonnel(int personnelId) {
        personnelDAO.deletePersonnel(personnelId);
    }

    
    /**
    * Retourne la liste complète du personnel enregistré.
    */
    @Override
    public List<Personnel> getAllPersonnel() {
        return personnelDAO.getAllPersonnel();
    }
    
    
    /**
    * Affecte un personnel à une séance précise après vérification d'existence.
    */
    @Override
    public void affecterPersonnelASeance(int personnelId, int seanceId) throws Exception {
        if (personnelDAO.getPersonnelById(personnelId).isEmpty()) throw new Exception("Personnel non trouvé.");
        if (seanceDAO.getSeanceById(seanceId).isEmpty()) throw new Exception("Séance non trouvée.");
        AffectationSeance nouvelleAffectation = new AffectationSeance(seanceId, personnelId);
        affectationSeanceDAO.addAffectation(nouvelleAffectation);
    }
    
    /**
     * Supprime une affectation de personnel à une séance.
     */
    @Override
    public void desaffecterPersonnelDeSeance(int personnelId, int seanceId) throws Exception {
        affectationSeanceDAO.deleteAffectation(seanceId, personnelId);
    }
    
    /**
     * Crée un planning pour un membre du personnel sur une période donnée avec poste.
     */
    @Override
    public Planning creerPlanning(int personnelId, LocalDateTime debut, LocalDateTime fin, String poste) throws Exception {
        if (personnelDAO.getPersonnelById(personnelId).isEmpty()) {
            throw new Exception("Personnel non trouvé pour créer un planning.");
        }
        int newId = IdManager.getNextPlanningId();
        Planning nouveauPlanning = new Planning(newId, debut, fin, poste, personnelId);
        planningDAO.addPlanning(nouveauPlanning);
        return nouveauPlanning;
    }
    
    
    
    /**
    * Récupère tous les plannings associés à un membre du personnel.
    */
    @Override
    public List<Planning> getPlanningPourPersonnel(int personnelId) {
        return planningDAO.getPlanningsByPersonnelId(personnelId);
    }

    /**
    * Récupère toutes les ventes snack effectuées un jour donné.
    */
    @Override
    public List<VenteSnack> getVentesSnackParJour(LocalDate date) {
        return venteSnackDAO.getVentesByDate(date);
    }
    
    
    /**
    * Retourne l'ensemble des réservations effectuées, pour usage administratif.
    */
    @Override
    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }
    
    
    /**
    * Retourne toutes les ventes snack enregistrées.
    */
    @Override
    public List<VenteSnack> getAllVentesSnack() {
        return venteSnackDAO.getAllVentesSnack();
    }
    /**
     * Calcule le chiffre d’affaires généré par les réservations pour une séance donnée.
     */
    @Override
    public double calculerChiffreAffairesReservationsPourSeance(int seanceId) {
        double total = 0.0;
        List<Billet> billets = billetDAO.getBilletsBySeanceId(seanceId);
        for (Billet billet : billets) {
            Optional<Tarif> tarifOpt = tarifDAO.getTarifById(billet.getIdTarif());
            if (tarifOpt.isPresent()) {
                total += tarifOpt.get().getPrix();
            }
        }
        return total;
    }

    /**
    * Récupère tous les billets associés à un film (via ses séances).
    */
    @Override
    public List<Billet> getBilletsPourFilm(int filmId) {
        List<Billet> billetsPourFilm = new ArrayList<>();
        List<Seance> seancesDuFilm = seanceDAO.getSeancesByFilmId(filmId);
        for (Seance seance : seancesDuFilm) {
            billetsPourFilm.addAll(billetDAO.getBilletsBySeanceId(seance.getId()));
        }
        return billetsPourFilm;
    }
    
    
    public Optional<Client> getClientById(int clientId) {
        // Délégation simple à la couche DAO.
        return clientDAO.getClientById(clientId);
    }

    public List<Billet> getBilletsByReservationId(int reservationId) {
        // Délégation simple à la couche DAO.
        return billetDAO.getBilletsByReservationId(reservationId);
    }
    
    public double calculerTotalPourVenteSnack(VenteSnack vente) {
        // Logique métier : parcourir les lignes de la vente et additionner les totaux.
        double total = 0.0;
        List<Comporte> lignes = comporteDAO.getLignesByVenteId(vente.getIdVente());
        // Boucle for-each, conformément aux contraintes.
        for (Comporte ligne : lignes) {
            total += ligne.getPrixUnitaire() * ligne.getQuantite();
        }
        return total;
    }
    
    public Optional<Seance> getSeanceById(int seanceId) {
        // Délégation simple à la couche DAO.
        return seanceDAO.getSeanceById(seanceId);
    }

    
    
    /**
    * Calcule le chiffre d'affaires cumulé pour toutes les séances d’un film donné.
    */
    @Override
    public double calculerChiffreAffairesPourFilm(int filmId) {
        double total = 0.0;
        List<Seance> seancesDuFilm = seanceDAO.getSeancesByFilmId(filmId);
        for (Seance seance : seancesDuFilm) {
            total += calculerChiffreAffairesReservationsPourSeance(seance.getId());
        }
        return total;
    }

    /**
    * Calcule le chiffre d'affaires pour toutes les séances ayant lieu à une date donnée.
    */
    @Override
    public double calculerChiffreAffairesPourJour(LocalDate date) {
        double total = 0.0;
        List<Seance> seancesDuJour = seanceDAO.getSeancesByDate(date);
        for (Seance seance : seancesDuJour) {
            total += calculerChiffreAffairesReservationsPourSeance(seance.getId());
        }
        return total;
    }

    /**
    * Calcule le chiffre d’affaires pour les ventes snack d’une journée.
    * Multiplie la quantité vendue par le prix unitaire de chaque article.
    */
    @Override
    public double calculerChiffreAffairesSnackPourJour(LocalDate date) {
        double total = 0.0;
        List<VenteSnack> ventesDuJour = venteSnackDAO.getVentesByDate(date);
        for (VenteSnack vente : ventesDuJour) {
            List<Comporte> lignesDeVente = comporteDAO.getLignesByVenteId(vente.getIdVente());
            for (Comporte ligne : lignesDeVente) {
                total += ligne.getQuantite() * ligne.getPrixUnitaire();
            }
        }
        return total;
    }
    
    /**
     * Vérifie si une séance proposée entre en conflit avec une autre séance
     * déjà programmée dans la même salle.
     * @param seanceAVerifier La séance à ajouter ou à modifier.
     * @throws Exception si un conflit est détecté.
     */
        private void verifierConflitPlanning(Seance seanceAVerifier) throws Exception {
        Optional<Film> filmOpt = filmDAO.getFilmById(seanceAVerifier.getIdFilm());
        if (filmOpt.isEmpty()) {
            throw new Exception("Film non trouvé pour la vérification du planning.");
        }
        int dureeFilm = filmOpt.get().getDureeMinutes();
        
        LocalDateTime debutSeanceAVerifier = seanceAVerifier.getDateHeureDebut();
        LocalDateTime finSeanceAVerifier = debutSeanceAVerifier.plusMinutes(dureeFilm);

        // On ne récupère QUE les séances du MÊME JOUR dans la MÊME SALLE
        List<Seance> seancesDuJourDansLaSalle = new ArrayList<>();
        List<Seance> seancesDuJour = seanceDAO.getSeancesByDate(debutSeanceAVerifier.toLocalDate());
        for (Seance s : seancesDuJour) {
            if (s.getIdSalle() == seanceAVerifier.getIdSalle()) {
                seancesDuJourDansLaSalle.add(s);
            }
        }
        
        for (Seance existante : seancesDuJourDansLaSalle) {
            // On ne se compare pas à soi-même (cas d'une modification)
            if (existante.getId() == seanceAVerifier.getId()) {
                continue;
            }

            Optional<Film> filmExistantOpt = filmDAO.getFilmById(existante.getIdFilm());
            if (filmExistantOpt.isEmpty()) continue;
            
            int dureeFilmExistant = filmExistantOpt.get().getDureeMinutes();
            LocalDateTime debutExistant = existante.getDateHeureDebut();
            LocalDateTime finExistant = debutExistant.plusMinutes(dureeFilmExistant);

            // Logique de détection de chevauchement [D1, F1] et [D2, F2]
            // Il y a conflit si le début de l'un est avant la fin de l'autre, et vice-versa.
            if (debutSeanceAVerifier.isBefore(finExistant) && debutExistant.isBefore(finSeanceAVerifier)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                throw new Exception("Conflit de planning : La salle est déjà occupée par le film '" 
                                    + filmExistantOpt.get().getTitre() + "' de " 
                                    + debutExistant.toLocalTime().format(formatter) 
                                    + " à " + finExistant.toLocalTime().format(formatter) + ".");
            }
        }
    }
        
        
         // =========================================================================
    // === DÉBUT DE L'IMPLÉMENTATION : Section Gestion Snacking              ===
    // =========================================================================

    /**
     * Retourne la liste de tous les produits de snacking. Délégation simple au DAO.
     */
    @Override
    public List<ProduitSnack> getAllProduitsSnack() {
        return produitSnackDAO.getAllProduits();
    }

    /**
     * Ajoute un nouveau produit après validation des données.
     */
    @Override
    public void ajouterProduitSnack(ProduitSnack produit) throws Exception {
        if (produit.getPrixVente() < 0 || produit.getStock() < 0) {
            throw new Exception("Le prix et le stock ne peuvent pas être négatifs.");
        }
        produit.setId(IdManager.getNextProduitSnackId());
        produitSnackDAO.addProduit(produit);
    }

    /**
     * Modifie un produit existant après validation des données.
     */
    @Override
    public void modifierProduitSnack(ProduitSnack produit) throws Exception {
        if (produit.getPrixVente() < 0 || produit.getStock() < 0) {
            throw new Exception("Le prix et le stock ne peuvent pas être négatifs.");
        }
        produitSnackDAO.updateProduit(produit);
    }

    /**
     * Supprime un produit.
     * Note : Une logique métier plus avancée pourrait interdire la suppression si le produit est dans une vente.
     */
    @Override
    public void supprimerProduitSnack(int produitId) throws Exception {
        // La méthode delete n'existe pas dans l'interface ProduitSnackDAO, nous devons l'ajouter.
        // En attendant, cette méthode ne fera rien.
        // CORRECTION À APPORTER DANS ProduitSnackDAO et ProduitSnackDAOImpl
        System.out.println("LOGIQUE DE SUPPRESSION DE PRODUIT À IMPLÉMENTER DANS LE DAO");
    }
    
    
     // =========================================================================
    // === DÉBUT DE L'IMPLÉMENTATION : Enregistrement de Vente de Snacks     ===
    // =========================================================================
    
    @Override
    public VenteSnack enregistrerVenteSnack(int idPersonnel, int idCaisse, java.util.Map<ProduitSnack, Integer> panier) throws Exception {
        // --- Étape 1 : Validation du stock ---
        // On vérifie d'abord que tous les produits sont en quantité suffisante AVANT de modifier quoi que ce soit.
        for (java.util.Map.Entry<ProduitSnack, Integer> entry : panier.entrySet()) {
            ProduitSnack produit = entry.getKey();
            Integer quantiteDemandee = entry.getValue();
            if (produit.getStock() < quantiteDemandee) {
                throw new Exception("Stock insuffisant pour le produit '" + produit.getNomProduit() + "'. Stock restant : " + produit.getStock());
            }
        }
        
        // --- Étape 2 : Création de la transaction principale (VenteSnack) ---
        int venteId = IdManager.getNextVenteSnackId();
        // Pour l'instant, les ventes sont anonymes (pas de client associé). On passe null.
        VenteSnack nouvelleVente = new VenteSnack(venteId, LocalDateTime.now(), idPersonnel, idCaisse, null);
        venteSnackDAO.addVenteSnack(nouvelleVente);
        
        // --- Étape 3 : Création des lignes de détail (Comporte) et mise à jour du stock ---
        for (java.util.Map.Entry<ProduitSnack, Integer> entry : panier.entrySet()) {
            ProduitSnack produit = entry.getKey();
            Integer quantiteVendue = entry.getValue();
            
            // Créer la ligne de détail de la vente.
            Comporte ligneVente = new Comporte(venteId, produit.getId(), quantiteVendue, produit.getPrixVente());
            comporteDAO.addLigneVente(ligneVente);
            
            // Mettre à jour le stock du produit.
            produit.setStock(produit.getStock() - quantiteVendue);
            produitSnackDAO.updateProduit(produit);
        }
        
        return nouvelleVente;
    }
    
    
    // =========================================================================
    // === DÉBUT DE L'IMPLÉMENTATION : Logique d'Évaluation                  ===
    // =========================================================================
    
    @Override
    public double getNoteMoyenneSpectateurs(int filmId) {
        List<EvaluationClient> evaluations = evaluationClientDAO.getEvaluationsByFilmId(filmId);
        if (evaluations.isEmpty()) {
            return 0.0;
        }
        double somme = 0;
        for (EvaluationClient eval : evaluations) {
            somme += eval.getNote();
        }
        return somme / evaluations.size();
    }

    @Override
    public void ajouterEvaluation(EvaluationClient evaluation) throws Exception {
        // Règle métier : un client ne peut pas noter deux fois le même film.
        if (aDejaEvalue(evaluation.getIdClient(), evaluation.getIdFilm())) {
            throw new Exception("Vous avez déjà noté ce film.");
        }
        // Autre règle : la note doit être dans un intervalle valide (ex: 1 à 5).
        if (evaluation.getNote() < 1 || evaluation.getNote() > 5) {
            throw new Exception("La note doit être comprise entre 1 et 5.");
        }
        evaluationClientDAO.addEvaluation(evaluation);
    }
    
    @Override
    public boolean aDejaEvalue(int clientId, int filmId) {
        return evaluationClientDAO.getEvaluationByClientAndFilm(clientId, filmId).isPresent();
    }

}