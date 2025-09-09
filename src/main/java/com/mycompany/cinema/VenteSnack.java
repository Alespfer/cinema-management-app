package com.mycompany.cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Représente une vente de snacks effectuée à une caisse.
 */
public class VenteSnack implements Serializable {

    // Identifiant unique de la vente.
    private int idVente;

    // Date et heure de la vente.
    private LocalDateTime dateVente;

    // Identifiant de l’employé ayant réalisé la vente.
    private int idPersonnel;

    // Identifiant de la caisse utilisée.
    private int idCaisse;

    // Identifiant du client (peut être null si client non identifié).
    private Integer idClient;

    // Identifiant de la réservation associée (peut être null).
    private Integer idReservation;

   
    public VenteSnack() {
    }

    /**
     * Constructeur principal.
     *
     * @param idVente identifiant unique de la vente
     * @param dateVente date et heure
     * @param idPersonnel employé concerné
     * @param idCaisse caisse utilisée
     * @param idClient client associé (ou null)
     */
    public VenteSnack(int idVente, LocalDateTime dateVente, int idPersonnel, int idCaisse, Integer idClient) {
        this.idVente = idVente;
        this.dateVente = dateVente;
        this.idPersonnel = idPersonnel;
        this.idCaisse = idCaisse;
        this.idClient = idClient;
    }

    /**
     * Constructeur simplifié (vente sans client identifié).
     *
     * @param idVente identifiant unique de la vente 
     * @param dateVente date et heure
     * @param idPersonnel employé concerné
     * @param idCaisse caisse utilisée
     */
    public VenteSnack(int idVente, LocalDateTime dateVente, int idPersonnel, int idCaisse) {
        this(idVente, dateVente, idPersonnel, idCaisse, null);
    }

    
    // --- Getters / Setters ---

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
