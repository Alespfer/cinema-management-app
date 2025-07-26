package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Caisse;
import com.mycompany.cinema.dao.CaisseDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CaisseDAOImpl extends GenericDAOImpl<Caisse> implements CaisseDAO {

    public CaisseDAOImpl() { super("caisses.dat"); }

    @Override
    public void addCaisse(Caisse caisse) {
        this.data.add(caisse);
        saveToFile();
    }

    @Override
    public Optional<Caisse> getCaisseById(int id) {
        for (Caisse c : this.data) {
            if (c.getId() == id) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Caisse> getAllCaisses() {
        return new ArrayList<>(this.data);
    }

    @Override
    public void updateCaisse(Caisse updatedCaisse) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == updatedCaisse.getId()) {
                this.data.set(i, updatedCaisse);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void deleteCaisse(int id) {
        if (this.data.removeIf(c -> c.getId() == id)) {
            saveToFile();
        }
    }
}