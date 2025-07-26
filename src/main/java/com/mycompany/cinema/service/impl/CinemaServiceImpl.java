package com.mycompany.cinema.service.impl;

import com.mycompany.cinema.*;
import com.mycompany.cinema.dao.*;
import com.mycompany.cinema.dao.impl.*;
import com.mycompany.cinema.service.CinemaService;
import com.mycompany.cinema.util.IdManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation du contrat CinemaService.
 * C'est le cerveau de l'application, contenant toute la logique métier.
 * Elle orchestre les DAOs pour manipuler les données.
 */
public class CinemaServiceImpl implements CinemaService {

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

    // =========================================================================
    // SECTION UTILISATEUR
    // =========================================================================

    @Override
    public Client creerCompteClient(String nom, String email, String motDePasse) throws Exception {
        // Règle métier : l'email doit être unique.
        List<Client> tousLesClients = clientDAO.getAllClients();
        for (Client clientExistant : tousLesClients) {
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
        List<Client> tousLesClients = clientDAO.getAllClients();
        for (Client client : tousLesClients) {
            if (client.getEmail().equalsIgnoreCase(email) && client.getMotDePasse().equals(motDePasse)) {
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Film> getFilmsAffiche() {
        return filmDAO.getAllFilms();
    }

    @Override
    public Film getFilmDetails(int filmId) {
        return filmDAO.getFilmById(filmId).orElse(null);
    }

    @Override
    public List<Seance> getSeancesPourFilmEtDate(int filmId, LocalDate date) {
        return seanceDAO.getSeancesByDate(date).stream()
                .filter(seance -> seance.getIdFilm() == filmId)
                .collect(Collectors.toList());
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
        List<Integer> idsSiegesOccupes = getBilletsPourSeance(seanceId).stream()
                                            .map(Billet::getIdSiege)
                                            .collect(Collectors.toList());
        
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
        clientDAO.getClientById(clientId).orElseThrow(() -> new Exception("Client non trouvé."));
        seanceDAO.getSeanceById(seanceId).orElseThrow(() -> new Exception("Séance non trouvée."));
        tarifDAO.getTarifById(tarifId).orElseThrow(() -> new Exception("Tarif non trouvé."));

        for (Integer siegeId : siegeIds) {
            if (getSiegesDisponibles(seanceId).stream().noneMatch(s -> s.getId() == siegeId)) {
                throw new Exception("Le siège " + siegeId + " n'est pas disponible.");
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
        reservationDAO.getReservationById(reservationId).orElseThrow(() -> new Exception("Réservation non trouvée."));
        billetDAO.deleteBilletsByReservationId(reservationId);
        reservationDAO.deleteReservation(reservationId);
    }

    @Override
    public List<Reservation> getHistoriqueReservationsClient(int clientId) {
        return reservationDAO.getReservationsByClientId(clientId);
    }

    // =========================================================================
    // SECTION ADMINISTRATEUR
    // =========================================================================

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
    public void ajouterSeance(Seance seance) {
        seance.setId(IdManager.getNextSeanceId());
        seanceDAO.addSeance(seance);
    }

    @Override
    public void modifierSeance(Seance seance) {
        // Implémentation basique, pourrait inclure des vérifications métier
        seanceDAO.getAllSeances().stream()
            .filter(s -> s.getId() == seance.getId())
            .findFirst()
            .ifPresent(s -> {
                int index = seanceDAO.getAllSeances().indexOf(s);
                seanceDAO.getAllSeances().set(index, seance); // Ceci ne sauvegardera pas. Il faut une méthode update dans le DAO.
                // NOTE : Il manque updateSeance() dans SeanceDAO et son implémentation.
            });
    }

    @Override
    public void supprimerSeance(int seanceId) throws Exception {
        if (!billetDAO.getBilletsBySeanceId(seanceId).isEmpty()) {
            throw new Exception("Impossible de supprimer une séance avec des billets vendus.");
        }
        seanceDAO.deleteSeance(seanceId);
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
    public void ajouterTarif(Tarif tarif) {
        tarif.setId(IdManager.getNextTarifId());
        tarifDAO.addTarif(tarif);
    }

    @Override
    public void modifierTarif(Tarif tarif) {
        tarifDAO.updateTarif(tarif);
    }

    @Override
    public void supprimerTarif(int tarifId) {
        tarifDAO.deleteTarif(tarifId);
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
        // On pourrait ajouter une logique pour vérifier si le personnel est dans un planning
        personnelDAO.deletePersonnel(personnelId);
    }

    @Override
    public List<VenteSnack> getVentesSnackParJour(LocalDate date) {
        return venteSnackDAO.getVentesByDate(date);
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
}