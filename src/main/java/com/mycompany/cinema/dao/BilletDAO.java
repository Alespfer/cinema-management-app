// ========================================================================
// FICHIER : BilletDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Billet;
import java.util.List;

/**
 * Définit le contrat pour la gestion de la persistance des billets de cinéma.
 */
public interface BilletDAO {

    
    // Enregistre un nouveau billet.
    void ajouterBillet(Billet billet);

    // Retrouve tous les billets appartenant à une même réservation.
    List<Billet> trouverBilletsParIdReservation(int idReservation);

    // Retrouve tous les billets vendus pour une séance donnée.
    List<Billet> trouverBilletsParIdSeance(int idSeance);

    // Retrouve la liste complète de tous les billets existants.
    List<Billet> trouverTousLesBillets();

    // Supprime tous les billets liés à une réservation qui a été annulée.
    void supprimerBilletsParIdReservation(int idReservation);

    // Force le rechargement des données depuis la source.

    void rechargerDonnees();
}