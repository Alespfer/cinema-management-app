package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Seance;
import com.mycompany.cinema.dao.SeanceDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DAO pour la gestion des séances de projection.
public class SeanceDAOImpl extends GenericDAOImpl<Seance> implements SeanceDAO {

    // Initialise le DAO avec le fichier des séances.
    public SeanceDAOImpl() {
        super("seances.dat");
    }

    // Ajoute une nouvelle séance.
    @Override
    public void addSeance(Seance seance) {
        this.data.add(seance);
        saveToFile();
    }

    // Recherche une séance à partir de son identifiant.
    @Override
    public Optional<Seance> getSeanceById(int id) {
        for (Seance seance : this.data) {
            if (seance.getId() == id) {
                return Optional.of(seance);
            }
        }
        return Optional.empty();
    }

    // Retourne toutes les séances enregistrées.
    @Override
    public List<Seance> getAllSeances() {
        return new ArrayList<>(this.data);
    }

    // Retourne toutes les séances associées à un film.
    @Override
    public List<Seance> getSeancesByFilmId(int filmId) {
        List<Seance> resultat = new ArrayList<>();
        for (Seance seance : this.data) {
            if (seance.getIdFilm() == filmId) {
                resultat.add(seance);
            }
        }
        return resultat;
    }

    // Retourne toutes les séances ayant lieu à une date donnée.
    @Override
    public List<Seance> getSeancesByDate(LocalDate date) {
        List<Seance> resultat = new ArrayList<>();
        for (Seance seance : this.data) {
            if (seance.getDateHeureDebut().toLocalDate().isEqual(date)) {
                resultat.add(seance);
            }
        }
        return resultat;
    }

    // Met à jour les informations d’une séance existante.
    @Override
    public void updateSeance(Seance updatedSeance) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == updatedSeance.getId()) {
                this.data.set(i, updatedSeance);
                saveToFile();
                return;
            }
        }
    }

    // Supprime une séance à partir de son identifiant.
    @Override
    public void deleteSeance(int id) {
        boolean changed = false;
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == id) {
                this.data.remove(i);
                changed = true;
                break;
            }
        }
        if (changed) {
            saveToFile();
        }
    }
}
