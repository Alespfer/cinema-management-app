package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Billet;
import com.mycompany.cinema.dao.BilletDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BilletDAOImpl extends GenericDAOImpl<Billet> implements BilletDAO {

    public BilletDAOImpl() {
        super("billets.dat");
    }

    @Override
    public void addBillet(Billet billet) {
        this.data.add(billet);
        saveToFile();
    }

    @Override
    public List<Billet> getBilletsByReservationId(int reservationId) {
        return this.data.stream()
                .filter(b -> b.getIdReservation() == reservationId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Billet> getAllBillets() {
        return new ArrayList<>(this.data);
    }

    @Override
    public void deleteBilletsByReservationId(int reservationId) {
        if (this.data.removeIf(b -> b.getIdReservation() == reservationId)) {
            saveToFile();
        }
    }
}