package com.mycompany.cinema.service;

import com.mycompany.cinema.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClientService extends CinemaService { // Hérite des méthodes communes
    // --- Compte Client ---
    Client creerCompteClient(String nom, String email, String motDePasse) throws Exception;
    Optional<Client> authentifierClient(String email, String motDePasse);
    void modifierCompteClient(Client client) throws Exception;
    void supprimerCompteClient(int clientId) throws Exception;
    
    // --- Consultation ---
    List<Seance> getSeancesPourFilmEtDate(int filmId, LocalDate date);
    List<Seance> findSeancesFiltrees(LocalDate date, Integer filmId, Integer salleId);
    List<Siege> getSiegesPourSalle(int salleId);
    List<Billet> getBilletsPourSeance(int seanceId);
    List<Siege> getSiegesDisponibles(int seanceId);

    // --- Transactionnel ---
    Reservation effectuerReservation(int clientId, int seanceId, List<Integer> siegeIds, int tarifId) throws Exception;
    void annulerReservation(int reservationId) throws Exception;
    List<Reservation> getHistoriqueReservationsClient(int clientId);
}