package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Reservation;
import com.mycompany.cinema.dao.ReservationDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DAO pour la gestion des réservations faites par les clients.
public class ReservationDAOImpl extends GenericDAOImpl<Reservation> implements ReservationDAO {

    // Initialise le DAO avec le fichier contenant les réservations.
    public ReservationDAOImpl() {
        super("reservations.dat");
    }

    // Ajoute une réservation à la base de données.
    @Override
    public void addReservation(Reservation reservation) {
        this.data.add(reservation);
        saveToFile();
    }

    // Recherche une réservation par son identifiant.
    @Override
    public Optional<Reservation> getReservationById(int id) {
        for (Reservation r : this.data) {
            if (r.getId() == id) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    // Retourne toutes les réservations enregistrées.
    @Override
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(this.data);
    }

    // Retourne toutes les réservations faites par un client spécifique.
    @Override
    public List<Reservation> getReservationsByClientId(int clientId) {
        List<Reservation> resultat = new ArrayList<>();
        for (Reservation r : this.data) {
            if (r.getIdClient() == clientId) {
                resultat.add(r);
            }
        }
        return resultat;
    }

    // Supprime une réservation à partir de son identifiant.
    @Override
    public void deleteReservation(int id) {
        boolean changed = false;
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == id) {
                this.data.remove(i);
                changed = true;
                break;
            }
        }
        if (changed) {
            saveToFile();
        }
    }
}
