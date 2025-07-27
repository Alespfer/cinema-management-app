package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Billet;
import com.mycompany.cinema.dao.BilletDAO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Classe DAO pour gérer les billets (accès fichier, filtrage)
public class BilletDAOImpl extends GenericDAOImpl<Billet> implements BilletDAO {

    // Constructeur : nom du fichier pour la persistance
    public BilletDAOImpl() {
        super("billets.dat");
    }

    // Ajoute un nouveau billet à la liste et sauvegarde les données.
    @Override
    public void addBillet(Billet billet) {
        this.data.add(billet); // Ajout direct à la liste
        saveToFile(); // Sauvegarde immédiate
    }
    // Retourne tous les billets liés à une réservation donnée.
    @Override
    public List<Billet> getBilletsByReservationId(int reservationId) {
        List<Billet> resultat = new ArrayList<>();
        for (Billet b : this.data) {
            if (b.getIdReservation() == reservationId) {
                resultat.add(b);
            }
        }
        return resultat; //liste des billets
    }
    // Retourne tous les billets associés à une séance donnée.
    @Override
    public List<Billet> getBilletsBySeanceId(int seanceId) {
        List<Billet> resultat = new ArrayList<>();
        for (Billet b : this.data) {
            if (b.getIdSeance() == seanceId) {
                resultat.add(b);
            }
        }
        return resultat; //liste des billets
    }

    // Retourne une copie complète de tous les billets enregistrés.
    @Override
    public List<Billet> getAllBillets() {
        // Retourne une copie de la liste (sécurité contre modifications externes)
        return new ArrayList<>(this.data);
    }

    // Supprime tous les billets associés à une réservation spécifique.
    @Override
    public void deleteBilletsByReservationId(int reservationId) {
        // Suppression sécurisée avec un Iterator
        Iterator<Billet> iterator = this.data.iterator();
        boolean changed = false;
        while (iterator.hasNext()) {
            Billet b = iterator.next();
            if (b.getIdReservation() == reservationId) {
                iterator.remove(); // suppression élément par élément
                changed = true;
            }
        }
        if (changed) {
            saveToFile(); // Sauvegarde uniquement si nécessaire
        }
    }
}
