package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Billet;
import com.mycompany.cinema.dao.BilletDAO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implémentation concrète pour gérer la sauvegarde et la lecture des billets de cinéma.
 * Cette classe s'occupe du fichier "billets.dat".
 * 
 * Pour le développeur de l'interface graphique : cette classe est le pilier de la réservation.
 * - Le `SiegePanel` l'utilisera (via le service) pour savoir quels sièges sont déjà vendus
 *   pour une séance donnée (`getBilletsBySeanceId`).
 * - Le `HistoriqueReservationsPanel` s'en servira pour afficher les détails d'une
 *   commande passée (`getBilletsByReservationId`).
 */
public class BilletDAOImpl extends GenericDAOImpl<Billet> implements BilletDAO {

    public BilletDAOImpl() {
        super("billets.dat");
    }

    @Override
    public void addBillet(Billet billet) {
        this.data.add(billet);
        saveToFile();
    }

    @Override
    public List<Billet> getBilletsByReservationId(int reservationId) {
        List<Billet> resultat = new ArrayList<>();
        // On parcourt tous les billets pour trouver ceux qui appartiennent à la même commande.
        for (Billet b : this.data) {
            if (b.getIdReservation() == reservationId) {
                resultat.add(b);
            }
        }
        return resultat;
    }
    
    @Override
    public List<Billet> getBilletsBySeanceId(int seanceId) {
        List<Billet> resultat = new ArrayList<>();
        // On parcourt tous les billets pour trouver ceux qui ont été vendus pour une séance précise.
        for (Billet b : this.data) {
            if (b.getIdSeance() == seanceId) {
                resultat.add(b);
            }
        }
        return resultat;
    }

    @Override
    public List<Billet> getAllBillets() {
        // On retourne une COPIE de la liste pour protéger les données internes.
        // Ainsi, si l'interface modifie cette liste, elle ne modifie pas nos données sources.
        return new ArrayList<>(this.data);
    }

    @Override
    public void deleteBilletsByReservationId(int reservationId) {
        Iterator<Billet> iterator = this.data.iterator();
        boolean changed = false;
        // On parcourt la liste de manière sécurisée...
        while (iterator.hasNext()) {
            Billet b = iterator.next();
            if (b.getIdReservation() == reservationId) {
                // ...et on supprime tous les billets liés à la réservation annulée.
                iterator.remove();
                changed = true;
            }
        }
        // On sauvegarde le fichier uniquement si des billets ont été supprimés.
        if (changed) {
            saveToFile();
        }
    }
}