package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Seance;
import com.mycompany.cinema.dao.SeanceDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return this.data.stream().filter(s -> s.getId() == id).findFirst();
    }

    @Override
    public List<Seance> getAllSeances() {
        return new ArrayList<>(this.data);
    }

    @Override
    public List<Seance> getSeancesByFilmId(int filmId) {
        return this.data.stream()
                .filter(s -> s.getIdFilm() == filmId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Seance> getSeancesByDate(LocalDate date) {
        return this.data.stream()
                .filter(s -> s.getDateHeureDebut().toLocalDate().isEqual(date))
                .collect(Collectors.toList());
    }
 // NOUVELLE MÉTHODE (syntaxe du cours)
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
        // La méthode removeIf est pratique mais non vue en cours.
        // Faisons-le manuellement.
        Seance seanceASupprimer = null;
        for (Seance seance : this.data) {
            if (seance.getId() == id) {
                seanceASupprimer = seance;
                break;
            }
        }
        if (seanceASupprimer != null) {
            this.data.remove(seanceASupprimer);
            saveToFile();
        }
    }
}