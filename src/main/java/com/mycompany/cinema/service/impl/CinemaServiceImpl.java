package com.mycompany.cinema.service.impl;

import com.mycompany.cinema.*;
import com.mycompany.cinema.dao.*;
import com.mycompany.cinema.dao.impl.*;
import com.mycompany.cinema.service.AdminService;
import com.mycompany.cinema.service.ClientService;
import com.mycompany.cinema.util.IdManager;
import com.mycompany.cinema.LignePanier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CinemaServiceImpl implements ClientService, AdminService {

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
        comporteDAO.rechargerDonnees();
        roleDAO.rechargerDonnees();
        genreDAO.rechargerDonnees();
        produitSnackDAO.rechargerDonnees();
        evaluationClientDAO.rechargerDonnees();
        caisseDAO.rechargerDonnees();
    }

    // --- SECTION COMMUNE ---
    @Override
    public List<Film> getFilmsAffiche() {
        return filmDAO.getAllFilms();
    }

    @Override
    public Film getFilmDetails(int filmId) {
        Optional<Film> filmOpt = filmDAO.getFilmById(filmId);
        if (filmOpt.isPresent()) {
            return filmOpt.get();
        }
        return null;
    }

    @Override
    public List<Film> findFilmsByTitre(String keyword) {
        return filmDAO.findFilmsByTitre(keyword);
    }

    @Override
    public double getNoteMoyenneSpectateurs(int filmId) {
        List<EvaluationClient> evaluations = evaluationClientDAO.getEvaluationsByFilmId(filmId);
        if (evaluations.isEmpty()) {
            return 0.0;
        }
        double sommeDesNotes = 0;
        for (EvaluationClient eval : evaluations) {
            sommeDesNotes = sommeDesNotes + eval.getNote();
        }
        return sommeDesNotes / evaluations.size();
    }

    // --- SECTION CLIENT ---
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
        clientDAO.updateClient(client);
    }

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

    @Override
    public List<Seance> rechercherSeances(LocalDate date, Integer genreId, String titreKeyword) {
        List<Seance> resultatFinal = new ArrayList<>();
        for (Seance seance : seanceDAO.getAllSeances()) {
            boolean correspond = true;
            if (date != null && !seance.getDateHeureDebut().toLocalDate().isEqual(date)) {
                correspond = false;
            }
            if (correspond) {
                Optional<Film> filmOpt = filmDAO.getFilmById(seance.getIdFilm());
                if (filmOpt.isPresent()) {
                    Film film = filmOpt.get();
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
        List<Siege> tousLesSieges = siegeDAO.getSiegesBySalleId(seanceOpt.get().getIdSalle());
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
        if (clientDAO.getClientById(clientId).isEmpty()) {
            throw new Exception("Client non trouvé.");
        }
        if (tarifDAO.getTarifById(tarifId).isEmpty()) {
            throw new Exception("Tarif non trouvé.");
        }
        if (seanceDAO.getSeanceById(seanceId).isEmpty()) {
            throw new Exception("Séance non trouvée.");
        }

        List<Siege> siegesDisponibles = getSiegesDisponibles(seanceId);
        List<Integer> siegesDisponiblesIds = new ArrayList<>();
        for (Siege s : siegesDisponibles) {
            siegesDisponiblesIds.add(s.getId());
        }
        for (Integer siegeId : siegeIds) {
            if (!siegesDisponiblesIds.contains(siegeId)) {
                throw new Exception("Le siège " + siegeId + " n'est plus disponible. La réservation a été annulée.");
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

    @Override
    public void ajouterEvaluation(EvaluationClient evaluation) throws Exception {
        if (aDejaEvalue(evaluation.getIdClient(), evaluation.getIdFilm())) {
            throw new Exception("Vous avez déjà noté ce film.");
        }
        evaluationClientDAO.addEvaluation(evaluation);
    }

    public void modifierEvaluation(EvaluationClient evaluation) throws Exception {
        evaluationClientDAO.updateEvaluation(evaluation);
    }

    @Override
    public boolean aDejaEvalue(int clientId, int filmId) {
        return evaluationClientDAO.getEvaluationByClientAndFilm(clientId, filmId).isPresent();
    }

    // --- MÉTHODE À AJOUTER ---
    /**
     *
     * @param clientId
     * @param filmId
     * @return
     */
    @Override
    public Optional<EvaluationClient> getEvaluation(int clientId, int filmId) {
        // Le service délègue simplement l'ordre au DAO spécialisé.
        return evaluationClientDAO.getEvaluationByClientAndFilm(clientId, filmId);
    }

    @Override
    public List<EvaluationClient> getEvaluationsByFilmId(int filmId) {
        return evaluationClientDAO.getEvaluationsByFilmId(filmId);
    }

    @Override
    public Reservation finaliserCommandeComplete(int clientId, int seanceId, List<Integer> siegeIds, int tarifId, List<LignePanier> panierSnacks) throws Exception {
        Reservation reservation = effectuerReservation(clientId, seanceId, siegeIds, tarifId);

        if (panierSnacks != null && !panierSnacks.isEmpty()) {
            VenteSnack venteSnack = enregistrerVenteSnack(3, 1, panierSnacks, clientId);
            venteSnack.setIdReservation(reservation.getId());
            // Il faudrait ici une méthode venteSnackDAO.updateVente(venteSnack) pour sauvegarder la liaison.
        }
        return reservation;
    }

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

    // --- SECTION ADMIN ---
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

    public void ajouterSalle(Salle salle, int nbRangees, int nbSiegesParRangee) throws Exception {
        salleDAO.addSalle(salle);
        for (int r = 1; r <= nbRangees; r++) {
            for (int s = 1; s <= nbSiegesParRangee; s++) {
                int siegeId = IdManager.getNextSiegeId();
                siegeDAO.addSiege(new Siege(siegeId, r, s, salle.getId()));
            }
        }
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

    @Override
    public void supprimerProduitSnack(int pId) throws Exception {
        /* ... */ }

    // AJOUTEZ CETTE NOUVELLE MÉTHODE DANS CinemaServiceImpl.java (version acceptant List<LignePanier>)
    // CORRECTION FINALE : Nouvelle méthode de support utilisant List<LignePanier>
    public VenteSnack enregistrerVenteSnack(int idPersonnel, int idCaisse, List<LignePanier> panier, Integer idClient) throws Exception {
        int venteId = IdManager.getNextVenteSnackId();
        VenteSnack nouvelleVente = new VenteSnack(venteId, LocalDateTime.now(), idPersonnel, idCaisse, idClient);
        venteSnackDAO.addVenteSnack(nouvelleVente);

        for (int i = 0; i < panier.size(); i++) {
            LignePanier ligne = panier.get(i);
            ProduitSnack produit = ligne.produit;
            int quantite = ligne.quantite;

            if (produit.getStock() < quantite) {
                throw new Exception("Stock insuffisant pour " + produit.getNomProduit());
            }

            Comporte ligneDeVente = new Comporte(venteId, produit.getId(), quantite, produit.getPrixVente());
            comporteDAO.addLigneVente(ligneDeVente);

            produit.setStock(produit.getStock() - quantite);
            produitSnackDAO.updateProduit(produit);
        }
        return nouvelleVente;
    }

    // --- SECTION REPORTING (CONFORME) ---
    @Override
    public List<Billet> getBilletsPourFilm(int filmId) {
        List<Billet> billets = new ArrayList<>();
        List<Seance> seancesDuFilm = seanceDAO.getSeancesByFilmId(filmId);
        for (Seance s : seancesDuFilm) {
            // CONFORMITÉ : Remplacement de addAll par une boucle for.
            List<Billet> billetsDeLaSeance = billetDAO.getBilletsBySeanceId(s.getId());
            for (Billet b : billetsDeLaSeance) {
                billets.add(b);
            }
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

    @Override
    public void ajouterSalle(Salle salle) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public VenteSnack enregistrerVenteSnack(int idPersonnel, int idCaisse, Map<ProduitSnack, Integer> panier) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
