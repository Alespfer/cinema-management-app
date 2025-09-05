package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Reservation;
import com.mycompany.cinema.dao.ReservationDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation concrète pour la gestion des réservations (commandes) dans
 * "reservations.dat".
 *
 * Pour le développeur de l'interface graphique : cette classe est le cœur de
 * l'historique client. Le `HistoriqueReservationsPanel` dépend entièrement de
 * `getReservationsByClientId` pour afficher la liste des commandes passées par
 * l'utilisateur connecté. La méthode `deleteReservation` est appelée lorsque
 * l'utilisateur décide d'annuler sa commande.
 */
public class ReservationDAOImpl extends GenericDAOImpl<Reservation> implements ReservationDAO {

    public ReservationDAOImpl() {
        super("reservations.dat");
    }

    @Override
    public void addReservation(Reservation reservation) {
        this.data.add(reservation);
        saveToFile();
    }

    // Dans ReservationDAOImpl.java
    @Override
    public Reservation getReservationById(int id) {
        for (Reservation r : this.data) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    @Override
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(this.data);
    }

    @Override
    public List<Reservation> getReservationsByClientId(int clientId) {
        List<Reservation> resultat = new ArrayList<>();
        // On filtre toutes les réservations pour ne garder que celles du client spécifié.
        for (Reservation r : this.data) {
            if (r.getIdClient() == clientId) {
                resultat.add(r);
            }
        }
        return resultat;
    }

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
