// ========================================================================
// SeanceDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Seance;
import com.mycompany.cinema.dao.SeanceDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation pour la gestion des séances de projection. Interagit avec le
 * fichier "seances.dat".
 *
 */
public class SeanceDAOImpl extends GenericDAOImpl<Seance> implements SeanceDAO {

    public SeanceDAOImpl() {
        super("seances.dat");
    }

    /**
     * Ajoute une nouvelle séance à la programmation.
     *
     * @param seance L'objet Seance à enregistrer.
     */
    @Override
    public void ajouterSeance(Seance seance) {
        this.data.add(seance);
        sauvegarderDansFichier();
    }

    /**
     * Recherche une séance par son identifiant unique.
     *
     * @param id L'identifiant de la séance.
     * @return L'objet Seance correspondant, ou `null` si non trouvé.
     */
    @Override
    public Seance trouverSeanceParId(int id) {
        for (Seance seance : this.data) {
            if (seance.getId() == id) {
                return seance;
            }
        }
        return null;
    }

    /**
     * Retourne la liste de toutes les séances programmées.
     *
     * @return Une copie de la liste de toutes les séances.
     */
    @Override
    public List<Seance> trouverToutesLesSeances() {
        return new ArrayList<>(this.data);
    }

    /**
     * Recherche toutes les séances pour un film spécifique.
     *
     * @param idFilm L'identifiant unique du film.
     * @return Une liste des séances pour ce film.
     */
    @Override
    public List<Seance> trouverSeancesParIdFilm(int idFilm) {
        List<Seance> seancesTrouvees = new ArrayList<>();
        for (Seance seance : this.data) {
            if (seance.getIdFilm() == idFilm) {
                seancesTrouvees.add(seance);
            }
        }
        return seancesTrouvees;
    }

    /**
     * Recherche toutes les séances pour un jour donné.
     *
     * @param date La date pour laquelle rechercher les séances.
     * @return Une liste des séances pour cette date.
     */
    @Override
    public List<Seance> trouverSeancesParDate(LocalDate date) {
        List<Seance> seancesTrouvees = new ArrayList<>();
        for (Seance seance : this.data) {
            // On extrait la date à partir de l'objet LocalDateTime.
            if (seance.getDateHeureDebut().toLocalDate().isEqual(date)) {
                seancesTrouvees.add(seance);
            }
        }
        return seancesTrouvees;
    }

    /**
     * Met à jour les informations d'une séance existante.
     *
     * @param seanceMiseAJour L'objet Seance avec les données mises à jour.
     */
    @Override
    public void mettreAJourSeance(Seance seanceMiseAJour) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == seanceMiseAJour.getId()) {
                this.data.set(i, seanceMiseAJour);
                sauvegarderDansFichier();
                return;
            }
        }
    }

    /**
     * Supprime une séance de la programmation.
     *
     * @param id L'identifiant de la séance à supprimer.
     */
    @Override
    public void supprimerSeanceParId(int id) {
        int indexASupprimer = -1;
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == id) {
                indexASupprimer = i;
                break;
            }
        }
        if (indexASupprimer != -1) {
            this.data.remove(indexASupprimer);
            sauvegarderDansFichier();
        }
    }
}
