package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Reservation;
import com.mycompany.cinema.dao.ReservationDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationDAOImpl extends GenericDAOImpl<Reservation> implements ReservationDAO {

    public ReservationDAOImpl() {
        super("reservations.dat");
    }

    @Override
    public void addReservation(Reservation reservation) {
        this.data.add(reservation);
        saveToFile();
    }

    @Override
    public Optional<Reservation> getReservationById(int id) {
        return this.data.stream().filter(r -> r.getId() == id).findFirst();
    }

    @Override
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(this.data);
    }

    @Override
    public List<Reservation> getReservationsByClientId(int clientId) {
        return this.data.stream()
                .filter(r -> r.getIdClient() == clientId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReservation(int id) {
        if (this.data.removeIf(r -> r.getId() == id)) {
            saveToFile();
        }
    }
}