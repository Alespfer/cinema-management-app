package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Représente un ticket de caisse pour une vente de snacks.
 * C'est l'objet "parent" qui regroupe toutes les lignes de produits
 * achetés lors d'une transaction au comptoir (via les objets `Comporte`).
 * 
 * Cette classe est utilisée à la fois par le client et le personnel :
 * - Côté Client : Un objet `VenteSnack` est créé en arrière-plan lorsque le client
 *   valide son panier de snacks dans le `SnackSelectionPanel` pour lier sa commande
 *   de snacks à sa réservation de billets.
 * - Côté Personnel : C'est l'objet principal créé par le `PointDeVenteFrame` lorsqu'un
 *   vendeur effectue une transaction au comptoir.
 * - Côté Admin : Une liste de ces objets sera affichée dans le `ReportingPanel` pour
 *   suivre les ventes de snacks.
 */
public class VenteSnack implements Serializable {
    
    private int idVente;
    private LocalDateTime dateVente; // Quand la vente a-t-elle eu lieu ?
    
    // --- Clés de liaison ---
    private int idPersonnel; // Quel employé a réalisé la vente ?
    private int idCaisse;    // À quelle caisse ?
    
    // L'ID du client est un objet 'Integer'. Cela lui permet d'être 'null',
    // ce qui est utile si un client achète des snacks sans être connecté.
    private Integer idClient;
    
    // De même, l'ID de réservation peut être 'null' si la vente de snack
    // n'est pas attachée à une réservation de billets en ligne.
    private Integer idReservation;


    /**
     * Constructeur vide (nécessité technique).
     */
    public VenteSnack() {}

    /**
     * Constructeur principal pour créer une nouvelle vente.
     */
    public VenteSnack(int idVente, LocalDateTime dateVente, int idPersonnel, int idCaisse, Integer idClient) {
        this.idVente = idVente;
        this.dateVente = dateVente;
        this.idPersonnel = idPersonnel;
        this.idCaisse = idCaisse;
        this.idClient = idClient;
    }

    /**
     * Constructeur pratique pour une vente rapide au comptoir, où le client n'est pas identifié.
     * Il appelle simplement le constructeur principal en passant 'null' pour le client.
     */
    public VenteSnack(int idVente, LocalDateTime dateVente, int idPersonnel, int idCaisse) {
        this(idVente, dateVente, idPersonnel, idCaisse, null);
    }
    
    // --- ACCESSEURS (Getters & Setters) ---

    public int getIdVente() { 
        return idVente; 
    }
    public void setIdVente(int idVente) { 
        this.idVente = idVente; 
    }

    public LocalDateTime getDateVente() { 
        return dateVente; 
    }
    public void setDateVente(LocalDateTime dateVente) { 
        this.dateVente = dateVente; 
    }

    public int getIdPersonnel() { 
        return idPersonnel; 
    }
    public void setIdPersonnel(int idPersonnel) { 
        this.idPersonnel = idPersonnel; 
    }

    public int getIdCaisse() { 
        return idCaisse; 
    }
    public void setIdCaisse(int idCaisse) { 
        this.idCaisse = idCaisse; 
    }

    public Integer getIdClient() { 
        return idClient; 
    }
    public void setIdClient(Integer idClient) { 
        this.idClient = idClient; 
    }  
      
    public Integer getIdReservation() {
        return idReservation;
    }
    public void setIdReservation(Integer idReservation) {
        this.idReservation = idReservation;
    }
}