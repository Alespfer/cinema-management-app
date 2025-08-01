package com.mycompany.cinema;

import java.io.Serializable;

/**
 * Représente un ticket de cinéma numérique. C'est le cœur d'une réservation.
 * Cet objet est la "colle" qui lie tout ensemble pour une place achetée :
 * il sait à quelle réservation il appartient, pour quelle séance, quel siège exact,
 * et à quel prix il a été vendu.
 * 
 * Pour l'interface graphique, lorsque vous afficherez le détail d'une réservation
 * d'un client, vous manipulerez une liste de ces objets 'Billet'. À partir d'un seul billet,
 * vous pourrez, via ses identifiants (idSeance, idSiege...), demander au service de vous
 * fournir les objets complets (Film, Salle, etc.) pour afficher toutes les informations.
 */
public class Billet implements Serializable {
    
    // Le numéro unique de ce ticket.
    private int idBillet;
    
    // Chaque 'id' est un lien vers une autre information.
    private int idReservation; // À quel "panier" appartient ce billet ?
    private int idTarif;       // Quel tarif a été appliqué (plein, étudiant...) ?
    private int idSiege;       // Quelle place exacte dans la salle ?
    private int idSeance;      // Pour quelle projection (quel film, à quelle heure) ?

    /**
     * Constructeur vide (nécessité technique).
     */
    public Billet() {}

    /**
     * Crée un ticket complet avec tous ses liens.
     */
    public Billet(int idBillet, int idReservation, int idTarif, int idSiege, int idSeance) {
        this.idBillet = idBillet;
        this.idReservation = idReservation;
        this.idTarif = idTarif;
        this.idSiege = idSiege;
        this.idSeance = idSeance;
    }

    // --- ACCESSEURS (Getters & Setters) ---

    public int getId() { 
        return idBillet; 
    }
    public void setId(int idBillet) { 
        this.idBillet = idBillet; 
    }

    public int getIdReservation() { 
        return idReservation; 
    }
    public void setIdReservation(int idReservation) { 
        this.idReservation = idReservation; 
    }

    public int getIdTarif() { 
        return idTarif; 
    }
    public void setIdTarif(int idTarif) { 
        this.idTarif = idTarif; 
    }

    public int getIdSiege() { 
        return idSiege; 
    }
    public void setIdSiege(int idSiege) { 
        this.idSiege = idSiege; 
    }
    
    public int getIdSeance() { 
        return idSeance; 
    }
    public void setIdSeance(int idSeance) { 
        this.idSeance = idSeance; 
    }
}