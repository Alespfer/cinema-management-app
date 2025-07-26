/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema;

// package com.mycompany.cinema; // Garde ton package

// package com.mycompany.cinema; // Garde ton package

import java.io.Serializable;
import java.time.LocalDateTime;

public class VenteSnack implements Serializable {
    private int idVente;
    private LocalDateTime dateVente;
    private int idPersonnel;
    private int idCaisse;       // Non-nullable, car une vente a toujours lieu quelque part.
    private Integer idClient;   // Nullable, pour les ventes anonymes.

    public VenteSnack() {}

    /**
     * Le constructeur principal. Toutes les autres formes doivent l'appeler.
     * Il garantit qu'un objet VenteSnack est toujours complet.
     */
    public VenteSnack(int idVente, LocalDateTime dateVente, int idPersonnel, int idCaisse, Integer idClient) {
        this.idVente = idVente;
        this.dateVente = dateVente;
        this.idPersonnel = idPersonnel;
        this.idCaisse = idCaisse; // L'initialisation manquante est maintenant là. C'est obligatoire.
        this.idClient = idClient;
    }

    /**
     * Constructeur de convenance pour une vente anonyme à une caisse donnée.
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
}