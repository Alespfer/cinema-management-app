package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Seance;
import com.mycompany.cinema.dao.SeanceDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        for (Seance seance : this.data) {
            // On compare uniquement la partie "date" du LocalDateTime de la séance.
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
        boolean changed = this.data.removeIf(seance -> seance.getId() == id);
        if(changed) {
            saveToFile();
        }
    }
}