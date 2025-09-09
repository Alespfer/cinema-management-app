// ========================================================================
// FICHIER : BilletDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Billet;
import com.mycompany.cinema.dao.BilletDAO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implémentation de l'interface BilletDAO. Cette classe gère la persistance des
 * objets Billet dans le fichier "billets.dat", fondamentale pour le processus
 * de réservation et la consultation de l'historique.
 *
 */
public class BilletDAOImpl extends GenericDAOImpl<Billet> implements BilletDAO {

    /**
     * Constructeur. Spécifie le fichier de données "billets.dat" au
     * gestionnaire générique parent.
     */
    public BilletDAOImpl() {
        super("billets.dat");
    }

    /**
     * Ajoute un nouveau billet à la source de données.
     *
     * @param billet L'objet Billet à sauvegarder.
     */
    public void addBillet(Billet billet) {
        this.data.add(billet);
        sauvegarderDansFichier();
    }

    /**
     * Recherche et retourne tous les billets associés à un identifiant de
     * réservation.
     *
     * @param idReservation L'identifiant de la réservation pour laquelle on
     * cherche les billets.
     * @return Une liste de billets. Retourne une liste vide si la réservation
     * est introuvable.
     */
    @Override
    public List<Billet> trouverBilletsParIdReservation(int idReservation) {
        List<Billet> billetsTrouves = new ArrayList<>();
        // Parcours de tous les billets pour filtrer ceux qui appartiennent à la même commande.
        for (Billet billet : this.data) {
            if (billet.getIdReservation() == idReservation) {
                billetsTrouves.add(billet);
            }
        }
        return billetsTrouves;
    }

    /**
     * Recherche et retourne tous les billets vendus pour une séance spécifique.
     * Cette méthode permet de déterminer quels sièges sont occupés.
     *
     * @param idSeance L'identifiant de la séance.
     * @return Une liste de billets. Retourne une liste vide si aucun billet n'a
     * été vendu.
     */
    @Override
    public List<Billet> trouverBilletsParIdSeance(int idSeance) {
        List<Billet> billetsTrouves = new ArrayList<>();
        // Parcours de tous les billets pour ne garder que ceux de la séance demandée.
        for (Billet billet : this.data) {
            if (billet.getIdSeance() == idSeance) {
                billetsTrouves.add(billet);
            }
        }
        return billetsTrouves;
    }

    /**
     * Retourne une liste de tous les billets enregistrés dans le système. * la
     * liste interne n'est pas modifiable de l'extérieur (principe
     * d'encapsulation).
     */
    @Override
    public List<Billet> trouverTousLesBillets() {
        return new ArrayList<>(this.data);
    }

    /**
     * Supprime tous les billets associés à un identifiant de réservation. Cette
     * opération est typiquement déclenchée lors de l'annulation d'une
     * réservation.
     *
     *
     * @param idReservation L'identifiant de la réservation dont les billets
     * doivent être supprimés.
     */
    @Override
    public void supprimerBilletsParIdReservation(int idReservation) {
        // Variable pour suivre si au moins un billet a été supprimé.
        boolean modificationEffectuee = false;

        for (int i = this.data.size() - 1; i >= 0; i--) {
            // On récupère le billet à l'index courant.
            Billet billet = this.data.get(i);

            // On vérifie si le billet correspond à la réservation à annuler.
            if (billet.getIdReservation() == idReservation) {
                // Si c'est le cas, on supprime le billet de la liste en mémoire.
                this.data.remove(i);

                // On note qu'une modification a eu lieu.
                modificationEffectuee = true;
            }
        }

        // Si des billets ont été supprimés, on sauvegarde la liste mise à jour dans le fichier.
        if (modificationEffectuee) {
            sauvegarderDansFichier();
        }
    }

    @Override
    public void ajouterBillet(Billet billet) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
