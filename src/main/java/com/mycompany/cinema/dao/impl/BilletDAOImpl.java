package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Billet;
import com.mycompany.cinema.dao.BilletDAO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BilletDAOImpl extends GenericDAOImpl<Billet> implements BilletDAO {

    public BilletDAOImpl() { super("billets.dat"); }

    @Override
    public void addBillet(Billet billet) {
        this.data.add(billet);
        saveToFile();
    }

    @Override
    public List<Billet> getBilletsByReservationId(int reservationId) {
        List<Billet> resultat = new ArrayList<>();
        for (Billet b : this.data) {
            if (b.getIdReservation() == reservationId) {
                resultat.add(b);
            }
        }
        return resultat;
    }

    @Override
    public List<Billet> getBilletsBySeanceId(int seanceId) {
        List<Billet> resultat = new ArrayList<>();
        for (Billet b : this.data) {
            if (b.getIdSeance() == seanceId) {
                resultat.add(b);
            }
        }
        return resultat;
    }

    @Override
    public List<Billet> getAllBillets() {
        return new ArrayList<>(this.data);
    }

    @Override
    public void deleteBilletsByReservationId(int reservationId) {
        // removeIf est pratique, mais une boucle avec Iterator est plus conforme au cours
        Iterator<Billet> iterator = this.data.iterator();
        boolean changed = false;
        while (iterator.hasNext()) {
            Billet b = iterator.next();
            if (b.getIdReservation() == reservationId) {
                iterator.remove();
                changed = true;
            }
        }
        if (changed) {
            saveToFile();
        }
    }
}