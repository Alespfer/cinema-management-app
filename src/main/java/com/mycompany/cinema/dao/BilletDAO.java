// Fichier : src/main/java/com/mycompany/cinema/dao/BilletDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Billet;
import java.util.List;

/**
 * Contrat pour la gestion de la persistance des Billets.
 * Toutes les implémentations DOIVENT fournir ces méthodes.
 */
public interface BilletDAO {

    void addBillet(Billet billet);

    List<Billet> getBilletsByReservationId(int reservationId);

    List<Billet> getBilletsBySeanceId(int seanceId);

    List<Billet> getAllBillets();

    void deleteBilletsByReservationId(int reservationId);

    void rechargerDonnees();
}