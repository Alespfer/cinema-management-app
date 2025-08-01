package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Représente une commande ou un "panier" validé par un client.
 * Cet objet est le conteneur principal qui groupe un ou plusieurs `Billet`s
 * achetés lors d'une même transaction.
 * 
 * Son rôle principal est de lier un client à un ensemble de billets.
 * 
 * Pour l'interface graphique, vous afficherez une liste d'objets `Reservation`
 * dans le panneau `HistoriqueReservationsPanel` du client. Chaque ligne du
 * tableau d'historique correspondra à une réservation. En cliquant sur une ligne,
 * vous utiliserez l'ID de la réservation pour demander au service de vous
 * fournir la liste des billets (`Billet`) associés afin d'afficher le détail.
 */
public class Reservation implements Serializable {
    
    private int idReservation;      // Le numéro unique de la commande.
    private LocalDateTime dateReservation; // Quand la commande a-t-elle été passée ?
    
    // Fait le lien avec le client qui a passé la commande.
    private int idClient;

    /**
     * Constructeur vide (nécessité technique).
     */
    public Reservation() {}

    /**
     * Crée une nouvelle réservation (commande).
     * @param idReservation L'ID unique de la réservation.
     * @param dateReservation Le moment exact de l'achat.
     * @param idClient L'ID du client concerné.
     */
    public Reservation(int idReservation, LocalDateTime dateReservation, int idClient) {
        this.idReservation = idReservation;
        this.dateReservation = dateReservation;
        this.idClient = idClient;
    }

    // --- ACCESSEURS (Getters and Setters) ---

    public int getId() { 
        return idReservation; 
    }
    public void setId(int idReservation) { 
        this.idReservation = idReservation; 
    }

    public LocalDateTime getDateReservation() { 
        return dateReservation; 
    }
    public void setDateReservation(LocalDateTime dateReservation) { 
        this.dateReservation = dateReservation; 
    }

    public int getIdClient() { 
        return idClient; 
    }
    public void setIdClient(int idClient) { 
        this.idClient = idClient; 
    }
}