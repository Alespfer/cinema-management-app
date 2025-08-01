package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Seance;
import com.mycompany.cinema.dao.SeanceDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation concrète pour la gestion des séances de projection dans "seances.dat".
 * 
 * Pour le développeur de l'interface graphique : cette classe est le moteur de
 * la programmation du cinéma.
 * - Le `ProgrammationPanel` utilise massivement cette classe (via le service) pour filtrer
 *   et afficher les séances disponibles selon les critères du client.
 * - Le `FilmDetailPanel` l'utilise pour afficher les horaires d'un film pour un jour donné.
 * - Le `GestionSeancesPanel` de l'administrateur s'en sert pour créer, modifier et
 *   supprimer les séances.
 */
public class SeanceDAOImpl extends GenericDAOImpl<Seance> implements SeanceDAO {

    public SeanceDAOImpl() {
        super("seances.dat");
    }

    @Override
    public void addSeance(Seance seance) {
        this.data.add(seance);
        saveToFile();
    }

    @Override
    public Optional<Seance> getSeanceById(int id) {
        for (Seance seance : this.data) {
            if (seance.getId() == id) {
                return Optional.of(seance);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Seance> getAllSeances() {
        return new ArrayList<>(this.data);
    }

    @Override
    public List<Seance> getSeancesByFilmId(int filmId) {
        List<Seance> resultat = new ArrayList<>();
        // Filtre les séances pour ne garder que celles d'un film spécifique.
        for (Seance seance : this.data) {
            if (seance.getIdFilm() == filmId) {
                resultat.add(seance);
            }
        }
        return resultat;
    }

    @Override
    public List<Seance> getSeancesByDate(LocalDate date) {
        List<Seance> resultat = new ArrayList<>();
        // Filtre les séances pour ne garder que celles d'un jour donné.
        for (Seance seance : this.data) {
            // On compare uniquement la partie "date" de l'objet LocalDateTime.
            if (seance.getDateHeureDebut().toLocalDate().isEqual(date)) {
                resultat.add(seance);
            }
        }
        return resultat;
    }

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