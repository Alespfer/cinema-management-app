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

    @Override
    public void deleteSeance(int id) {
        if (this.data.removeIf(s -> s.getId() == id)) {
            saveToFile();
        }
    }
}