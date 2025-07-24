package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Tarif;
import com.mycompany.cinema.dao.TarifDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TarifDAOImpl extends GenericDAOImpl<Tarif> implements TarifDAO {

    public TarifDAOImpl() {
        super("tarifs.dat");
    }

    @Override
    public void addTarif(Tarif tarif) {
        this.data.add(tarif);
        saveToFile();
    }

    @Override
    public Optional<Tarif> getTarifById(int id) {
        for (Tarif tarif : this.data) {
            if (tarif.getId() == id) {
                return Optional.of(tarif);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Tarif> getAllTarifs() {
        return new ArrayList<>(this.data);
    }

    @Override
    public void updateTarif(Tarif updatedTarif) {
        Optional<Tarif> existingTarifOpt = getTarifById(updatedTarif.getId());
        if (existingTarifOpt.isPresent()) {
            int index = this.data.indexOf(existingTarifOpt.get());
            this.data.set(index, updatedTarif);
            saveToFile();
        }
    }

    @Override
    public void deleteTarif(int id) {
        if (this.data.removeIf(tarif -> tarif.getId() == id)) {
            saveToFile();
        }
    }
}