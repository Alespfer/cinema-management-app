package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Représente une transaction de vente au comptoir de snacks.
 * C'est l'équivalent d'un ticket de caisse, qui est ensuite détaillé
 * par un ou plusieurs objets 'Comporte'.
 * 
 * Implémente Serializable pour la sauvegarde.
 * 
 */
public class VenteSnack implements Serializable {
    
    private int idVente;
    private LocalDateTime dateVente;
    
    // Clés étrangères
    private int idPersonnel;
    private int idCaisse;
    
    // L'ID client est un 'Integer' (objet) et non un 'int' (primitif)
    // pour pouvoir accepter la valeur 'null'. C'est utile pour les ventes
    // aux clients non enregistrés (anonymes).
    private Integer idClient;
    
    private Integer idReservation; // Clé étrangère nullable vers la réservation


    public VenteSnack() {}

    /**
     * Constructeur principal pour créer une nouvelle vente.
     * Il garantit qu'un objet VenteSnack est toujours complet.
     */
    public VenteSnack(int idVente, LocalDateTime dateVente, int idPersonnel, int idCaisse, Integer idClient) {
        this.idVente = idVente;
        this.dateVente = dateVente;
        this.idPersonnel = idPersonnel;
        this.idCaisse = idCaisse;
        this.idClient = idClient;
    }

    /**
     * Constructeur de convenance pour une vente anonyme (sans client associé).
     * Il appelle le constructeur principal en passant 'null' pour l'ID client.
     */
    public VenteSnack(int idVente, LocalDateTime dateVente, int idPersonnel, int idCaisse) {
        this(idVente, dateVente, idPersonnel, idCaisse, null);
    }
    
    // --- Getters ---

    public int getIdVente() { 
        return idVente; 
    }
    public LocalDateTime getDateVente() { 
        return dateVente; 
    }
    public int getIdPersonnel() { 
        return idPersonnel; 
    }
    public int getIdCaisse() { 
        return idCaisse; 
    }
    public Integer getIdClient() { 
        return idClient; 
    }
    
    public Integer getIdReservation() {
        return idReservation;
    }

    
    // --- Setters ---

    public void setIdVente(int idVente) { 
        this.idVente = idVente; 
    }
    public void setDateVente(LocalDateTime dateVente) { 
        this.dateVente = dateVente; 
    }
    public void setIdPersonnel(int idPersonnel) { 
        this.idPersonnel = idPersonnel; 
    }
    public void setIdCaisse(int idCaisse) { 
        this.idCaisse = idCaisse; 
    }
    public void setIdClient(Integer idClient) { 
        this.idClient = idClient; 
    }  
      
    public void setIdReservation(Integer idReservation) {
        this.idReservation = idReservation;
    }
}