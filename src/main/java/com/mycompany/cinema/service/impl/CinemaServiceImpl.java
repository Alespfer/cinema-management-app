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
    
    // =========================================================================
    // SECTION CLIENT (implémentation de ClientService)
    // =========================================================================

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

    @Override
    public Optional<Client> authentifierClient(String email, String motDePasse) {
        for (Client client : clientDAO.getAllClients()) {
            if (client.getEmail().equalsIgnoreCase(email) && client.getMotDePasse().equals(motDePasse)) {
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }

    @Override
    public void modifierCompteClient(Client client) throws Exception {
        if (clientDAO.getClientById(client.getId()).isEmpty()) {
            throw new Exception("Client non trouvé.");
        }
        clientDAO.updateClient(client);
    }

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

    @Override
    public List<Siege> getSiegesPourSalle(int salleId) {
        return siegeDAO.getSiegesBySalleId(salleId);
    }

    @Override
    public List<Billet> getBilletsPourSeance(int seanceId) {
        return billetDAO.getBilletsBySeanceId(seanceId);
    }

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

    @Override
    public void annulerReservation(int reservationId) throws Exception {
        if (reservationDAO.getReservationById(reservationId).isEmpty()) {
            throw new Exception("Réservation non trouvée.");
        }
        billetDAO.deleteBilletsByReservationId(reservationId);
        reservationDAO.deleteReservation(reservationId);
    }

    @Override
    public List<Reservation> getHistoriqueReservationsClient(int clientId) {
        return reservationDAO.getReservationsByClientId(clientId);
    }

    // =========================================================================
    // SECTION ADMINISTRATEUR (implémentation de AdminService)
    // =========================================================================
    
    @Override
    public Optional<Personnel> authentifierPersonnel(String nomUtilisateur, String motDePasse) {
        for (Personnel p : personnelDAO.getAllPersonnel()) {
            if (p.getNom().equalsIgnoreCase(nomUtilisateur) && p.getMotDePasse().equals(motDePasse)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }
    
    @Override
    public void ajouterFilm(Film film) {
        film.setId(IdManager.getNextFilmId());
        filmDAO.addFilm(film);
    }

    @Override
    public void mettreAJourFilm(Film film) {
        filmDAO.updateFilm(film);
    }

    @Override
    public void supprimerFilm(int filmId) throws Exception {
        if (!seanceDAO.getSeancesByFilmId(filmId).isEmpty()) {
            throw new Exception("Impossible de supprimer un film programmé dans une séance.");
        }
        filmDAO.deleteFilm(filmId);
    }

    @Override
    public void ajouterSeance(Seance seance) throws Exception {
        verifierConflitPlanning(seance);
        seance.setId(IdManager.getNextSeanceId());
        seanceDAO.addSeance(seance);
    }

    @Override
    public void modifierSeance(Seance seance) throws Exception {
        verifierConflitPlanning(seance);
        seanceDAO.updateSeance(seance);
    }

    @Override
    public void supprimerSeance(int seanceId) throws Exception {
        if (!billetDAO.getBilletsBySeanceId(seanceId).isEmpty()) {
            throw new Exception("Impossible de supprimer une séance avec des billets vendus.");
        }
        seanceDAO.deleteSeance(seanceId);
    }

    @Override
    public List<Seance> getAllSeances() {
        return seanceDAO.getAllSeances();
    }

    @Override
    public void ajouterSalle(Salle salle) {
        salle.setId(IdManager.getNextSalleId());
        salleDAO.addSalle(salle);
    }

    @Override
    public void modifierSalle(Salle salle) {
        salleDAO.updateSalle(salle);
    }

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

    @Override
    public List<Salle> getAllSalles() {
        return salleDAO.getAllSalles();
    }

    @Override
    public void ajouterTarif(Tarif tarif) throws Exception {
        if (tarif.getPrix() < 0) {
            throw new Exception("Le prix d'un tarif ne peut pas être négatif.");
        }
        tarif.setId(IdManager.getNextTarifId());
        tarifDAO.addTarif(tarif);
    }

    @Override
    public void modifierTarif(Tarif tarif) throws Exception {
        if (tarif.getPrix() < 0) {
            throw new Exception("Le prix d'un tarif ne peut pas être négatif.");
        }
        tarifDAO.updateTarif(tarif);
    }

    @Override
    public void supprimerTarif(int tarifId) {
        tarifDAO.deleteTarif(tarifId);
    }
    
    @Override
    public List<Tarif> getAllTarifs() {
        return tarifDAO.getAllTarifs();
    }
    
    @Override
    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }

    @Override
    public void ajouterPersonnel(Personnel personnel) {
        personnel.setId(IdManager.getNextPersonnelId());
        personnelDAO.addPersonnel(personnel);
    }

    @Override
    public void modifierPersonnel(Personnel personnel) {
        personnelDAO.updatePersonnel(personnel);
    }

    @Override
    public void supprimerPersonnel(int personnelId) {
        personnelDAO.deletePersonnel(personnelId);
    }

    @Override
    public List<Personnel> getAllPersonnel() {
        return personnelDAO.getAllPersonnel();
    }

    @Override
    public void affecterPersonnelASeance(int personnelId, int seanceId) throws Exception {
        if (personnelDAO.getPersonnelById(personnelId).isEmpty()) throw new Exception("Personnel non trouvé.");
        if (seanceDAO.getSeanceById(seanceId).isEmpty()) throw new Exception("Séance non trouvée.");
        AffectationSeance nouvelleAffectation = new AffectationSeance(seanceId, personnelId);
        affectationSeanceDAO.addAffectation(nouvelleAffectation);
    }

    @Override
    public void desaffecterPersonnelDeSeance(int personnelId, int seanceId) throws Exception {
        affectationSeanceDAO.deleteAffectation(seanceId, personnelId);
    }

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

    @Override
    public List<Planning> getPlanningPourPersonnel(int personnelId) {
        return planningDAO.getPlanningsByPersonnelId(personnelId);
    }

    @Override
    public List<VenteSnack> getVentesSnackParJour(LocalDate date) {
        return venteSnackDAO.getVentesByDate(date);
    }
    
    @Override
    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }

    @Override
    public List<VenteSnack> getAllVentesSnack() {
        return venteSnackDAO.getAllVentesSnack();
    }

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

    @Override
    public List<Billet> getBilletsPourFilm(int filmId) {
        List<Billet> billetsPourFilm = new ArrayList<>();
        List<Seance> seancesDuFilm = seanceDAO.getSeancesByFilmId(filmId);
        for (Seance seance : seancesDuFilm) {
            billetsPourFilm.addAll(billetDAO.getBilletsBySeanceId(seance.getId()));
        }
        return billetsPourFilm;
    }

    @Override
    public double calculerChiffreAffairesPourFilm(int filmId) {
        double total = 0.0;
        List<Seance> seancesDuFilm = seanceDAO.getSeancesByFilmId(filmId);
        for (Seance seance : seancesDuFilm) {
            total += calculerChiffreAffairesReservationsPourSeance(seance.getId());
        }
        return total;
    }

    @Override
    public double calculerChiffreAffairesPourJour(LocalDate date) {
        double total = 0.0;
        List<Seance> seancesDuJour = seanceDAO.getSeancesByDate(date);
        for (Seance seance : seancesDuJour) {
            total += calculerChiffreAffairesReservationsPourSeance(seance.getId());
        }
        return total;
    }

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
}