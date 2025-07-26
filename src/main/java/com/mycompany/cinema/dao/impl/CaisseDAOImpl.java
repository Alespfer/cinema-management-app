// Fichier : src/main/java/com/mycompany/cinema/dao/impl/CaisseDAOImpl.java
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Caisse;
import com.mycompany.cinema.dao.CaisseDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CaisseDAOImpl extends GenericDAOImpl<Caisse> implements CaisseDAO {

    public CaisseDAOImpl() {
        super("caisses.dat");
    }

    @Override
    public void addCaisse(Caisse caisse) {
        this.data.add(caisse);
        saveToFile();
    }

    @Override
    public Optional<Caisse> getCaisseById(int id) {
        return this.data.stream().filter(c -> c.getId() == id).findFirst();
    }

    @Override
    public List<Caisse> getAllCaisses() {
        return new ArrayList<>(this.data);
    }

    @Override
    public void updateCaisse(Caisse updatedCaisse) {
        getCaisseById(updatedCaisse.getId()).ifPresent(c -> {
            int index = this.data.indexOf(c);
            this.data.set(index, updatedCaisse);
            saveToFile();
        });
    }

    @Override
    public void deleteCaisse(int id) {
        if (this.data.removeIf(c -> c.getId() == id)) {
            saveToFile();
        }
    }
}