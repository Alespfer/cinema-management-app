package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Reservation;
import com.mycompany.cinema.dao.ReservationDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationDAOImpl extends GenericDAOImpl<Reservation> implements ReservationDAO {

    public ReservationDAOImpl() { super("reservations.dat"); }

    @Override
    public void addReservation(Reservation reservation) {
        this.data.add(reservation);
        saveToFile();
    }

    @Override
    public Optional<Reservation> getReservationById(int id) {
        for (Reservation r : this.data) {
            if (r.getId() == id) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(this.data);
    }

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

    @Override
    public void deleteReservation(int id) {
        if (this.data.removeIf(r -> r.getId() == id)) {
            saveToFile();
        }
    }
}