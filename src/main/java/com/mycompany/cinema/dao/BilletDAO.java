package com.mycompany.cinema.dao;

import com.mycompany.cinema.Billet;
import java.util.List;

/**
 * Définit le contrat pour la gestion des tickets de cinéma en base de données.
 * Toute classe de gestion des billets doit respecter ces règles.
 * 
 * Pour l'interface graphique client, c'est fondamental. Le panneau d'historique
 * utilisera une implémentation de `getBilletsByReservationId` pour afficher le détail
 * d'une commande. Le panneau de sélection des sièges (`SiegePanel`) utilisera
 * `getBilletsBySeanceId` pour savoir quelles places sont déjà prises.
 */
public interface BilletDAO {

    /** Règle n°1 : Savoir enregistrer un nouveau billet. */
    void addBillet(Billet billet);

    /** Règle n°2 : Savoir retrouver tous les billets d'une même réservation. */
    List<Billet> getBilletsByReservationId(int reservationId);

    /** Règle n°3 : Savoir retrouver tous les billets vendus pour une séance. */
    List<Billet> getBilletsBySeanceId(int seanceId);

    /** Règle n°4 : Savoir lister tous les billets existants (utile pour l'admin). */
    List<Billet> getAllBillets();

    /** Règle n°5 : Savoir supprimer tous les billets liés à une réservation annulée. */
    void deleteBilletsByReservationId(int reservationId);

    /** Règle n°6 : Savoir recharger les données depuis le disque. */
    void rechargerDonnees();
}