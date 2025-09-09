// ========================================================================
// FICHIER : ReservationDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Reservation;
import com.mycompany.cinema.dao.ReservationDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation pour la gestion des réservations (commandes). Interagit avec
 * le fichier "reservations.dat".
 *
 * Cœur de l'historique des commandes du client.
 */
public class ReservationDAOImpl extends GenericDAOImpl<Reservation> implements ReservationDAO {

    public ReservationDAOImpl() {
        super("reservations.dat");
    }

    /**
     * Ajoute une nouvelle réservation.
     *
     * @param reservation L'objet Reservation à enregistrer.
     */
    @Override
    public void ajouterReservation(Reservation reservation) {
        this.data.add(reservation);
        sauvegarderDansFichier();
    }

    /**
     * Recherche une réservation par son identifiant unique.
     * @param id L'identifiant unique de la réservation.
     * @return L'objet Reservation, ou `null` si non trouvé.
     */
    @Override
    public Reservation trouverReservationParId(int id) {
        for (Reservation reservation : this.data) {
            if (reservation.getId() == id) {
                return reservation;
            }
        }
        return null;
    }
    

    /**
     * Retourne la liste de toutes les réservations.
     * @return Une copie de la liste des réservations.
     */
    @Override
    public List<Reservation> trouverToutesLesReservations() {
        return new ArrayList<>(this.data);
    }


     /**
     * Recherche toutes les réservations effectuées par un client spécifique.
     * @param idClient L'identifiant du client.
     * @return Une liste des réservations de ce client.
     */
    @Override
    public List<Reservation> trouverReservationsParIdClient(int idClient) {
        List<Reservation> reservationsTrouvees = new ArrayList<>();
        for (Reservation reservation : this.data) {
            if (reservation.getIdClient() == idClient) {
                reservationsTrouvees.add(reservation);
            }
        }
        return reservationsTrouvees;
    }

   /**
     * Supprime une réservation à partir de son identifiant.
     * @param id L'identifiant de la réservation à supprimer.
     */
    @Override
    public void supprimerReservationParId(int id) {
        int indexASupprimer = -1;
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == id) {
                indexASupprimer = i;
                break;
            }
        }
        if (indexASupprimer != -1) {
            this.data.remove(indexASupprimer);
            sauvegarderDansFichier();
        }
    }
}
