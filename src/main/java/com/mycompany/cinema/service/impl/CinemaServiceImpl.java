// Fichier : src/main/java/com/mycompany/cinema/service/impl/CinemaServiceImpl.java
package com.mycompany.cinema.service.impl;

import com.mycompany.cinema.*;
import com.mycompany.cinema.dao.*;
import com.mycompany.cinema.dao.impl.*;
import com.mycompany.cinema.service.CinemaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CinemaServiceImpl implements CinemaService {

    private final FilmDAO filmDAO = new FilmDAOImpl();
    private final SeanceDAO seanceDAO = new SeanceDAOImpl();
    private final SalleDAO salleDAO = new SalleDAOImpl();
    private final SiegeDAO siegeDAO = new SiegeDAOImpl();
    private final BilletDAO billetDAO = new BilletDAOImpl();
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final ClientDAO clientDAO = new ClientDAOImpl();
    private final TarifDAO tarifDAO = new TarifDAOImpl();

    // --- Fonctions Côté Utilisateur ---

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
        List<Seance> toutesLesSeances = seanceDAO.getAllSeances();
        List<Seance> seancesFiltrees = new ArrayList<>();
        for (Seance seance : toutesLesSeances) {
            if (seance.getIdFilm() == filmId && seance.getDateHeureDebut().toLocalDate().isEqual(date)) {
                seancesFiltrees.add(seance);
            }
        }
        return seancesFiltrees;
    }

    public List<Siege> getSiegesDisponibles(int seanceId) {
        List<Siege> siegesDisponibles = new ArrayList<>();
        Optional<Seance> seanceOpt = seanceDAO.getSeanceById(seanceId);
        if (seanceOpt.isEmpty()) {
            return siegesDisponibles;
        }
        Seance seance = seanceOpt.get();
        List<Siege> tousLesSiegesDeLaSalle = siegeDAO.getSiegesBySalleId(seance.getIdSalle());
        List<Billet> billetsVendus = billetDAO.getBilletsBySeanceId(seanceId);
        List<Integer> idsSiegesOccupes = new ArrayList<>();
        for (Billet billet : billetsVendus) {
            idsSiegesOccupes.add(billet.getIdSiege());
        }
        for (Siege siege : tousLesSiegesDeLaSalle) {
            if (!idsSiegesOccupes.contains(siege.getId())) {
                siegesDisponibles.add(siege);
            }
        }
        return siegesDisponibles;
    }
    
    @Override
    public List<Siege> getSiegesPourSalle(int salleId){
        return siegeDAO.getSiegesBySalleId(salleId);
    }
    
    @Override
    public List<Billet> getBilletsPourSeance(int seanceId) {
        return billetDAO.getBilletsBySeanceId(seanceId);
    }

    @Override
    public Reservation effectuerReservation(int clientId, int seanceId, List<Integer> siegeIds, int tarifId) throws Exception {
        clientDAO.getClientById(clientId).orElseThrow(() -> new Exception("Erreur: Client avec ID " + clientId + " non trouvé."));
        seanceDAO.getSeanceById(seanceId).orElseThrow(() -> new Exception("Erreur: Séance avec ID " + seanceId + " non trouvée."));
        tarifDAO.getTarifById(tarifId).orElseThrow(() -> new Exception("Erreur: Tarif avec ID " + tarifId + " non trouvé."));
        
        List<Siege> siegesDisponibles = getSiegesDisponibles(seanceId);
        List<Integer> idsSiegesDisponibles = new ArrayList<>();
        for (Siege siege : siegesDisponibles) {
            idsSiegesDisponibles.add(siege.getId());
        }

        // L'ERREUR ÉTAIT ICI. LE NOM DE LA VARIABLE EST 'siegeIdDemande', SANS ESPACE.
        for (Integer siegeIdDemande : siegeIds) {
            if (!idsSiegesDisponibles.contains(siegeIdDemande)) {
                throw new Exception("Erreur: Le siège ID " + siegeIdDemande + " n'est plus disponible.");
            }
        }
        
        int newReservationId = reservationDAO.getAllReservations().size() + 1;
        Reservation nouvelleReservation = new Reservation(newReservationId, LocalDateTime.now(), clientId);
        
        int lastBilletId = billetDAO.getAllBillets().size();
        for (Integer siegeId : siegeIds) {
            lastBilletId++;
            Billet nouveauBillet = new Billet(lastBilletId, nouvelleReservation.getId(), tarifId, siegeId, seanceId);
            billetDAO.addBillet(nouveauBillet);
        }
        
        reservationDAO.addReservation(nouvelleReservation);
        
        return nouvelleReservation;
    }

    @Override
    public void annulerReservation(int reservationId) throws Exception {
        reservationDAO.getReservationById(reservationId).orElseThrow(() -> new Exception("Erreur: Réservation ID " + reservationId + " non trouvée."));
        billetDAO.deleteBilletsByReservationId(reservationId);
        reservationDAO.deleteReservation(reservationId);
    }
    
    @Override
    public List<Reservation> getHistoriqueReservationsClient(int clientId) {
        return reservationDAO.getReservationsByClientId(clientId);
    }

    // --- Fonctions Côté Admin ---
    
    @Override
    public void ajouterFilm(Film film) {
        filmDAO.addFilm(film);
    }

    @Override
    public void mettreAJourFilm(Film film){
        filmDAO.updateFilm(film);
    }

    @Override
    public void supprimerFilm(int filmId) throws Exception {
        List<Seance> seancesPourCeFilm = seanceDAO.getSeancesByFilmId(filmId);
        if (!seancesPourCeFilm.isEmpty()) {
            throw new Exception("Impossible de supprimer le film ID " + filmId + " car il est programmé dans " + seancesPourCeFilm.size() + " séance(s).");
        }
        filmDAO.deleteFilm(filmId);
    }
    
    public void ajouterSeance(Seance seance){
        seanceDAO.addSeance(seance);
    }
}