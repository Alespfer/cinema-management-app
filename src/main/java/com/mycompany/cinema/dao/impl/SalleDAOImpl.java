package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Salle;
import com.mycompany.cinema.dao.SalleDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalleDAOImpl extends GenericDAOImpl<Salle> implements SalleDAO {

    public SalleDAOImpl() {
        super("salles.dat");
    }

    @Override
    public void addSalle(Salle salle) {
        this.data.add(salle);
        saveToFile();
    }

    @Override
    public Optional<Salle> getSalleById(int id) {
        return this.data.stream().filter(s -> s.getId() == id).findFirst();
    }

    @Override
    public List<Salle> getAllSalles() {
        return new ArrayList<>(this.data);
    }

    @Override
    public void updateSalle(Salle updatedSalle) {
        getSalleById(updatedSalle.getId()).ifPresent(s -> {
            int index = this.data.indexOf(s);
            this.data.set(index, updatedSalle);
            saveToFile();
        });
    }

    @Override
    public void deleteSalle(int id) {
        if (this.data.removeIf(s -> s.getId() == id)) {
            saveToFile();
        }
    }
}