package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Caisse;
import com.mycompany.cinema.dao.CaisseDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DAO pour la gestion des caisses
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
        // Recherche d'une caisse par ID
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
        // Mise à jour d'une caisse existante (par ID)
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
        // Remplacement de removeIf par boucle explicite pour conformité
        boolean changed = false;
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == id) {
                this.data.remove(i);
                changed = true;
                break; // Suppression d'un seul élément
            }
        }
        if (changed) {
            saveToFile();
        }
    }
}
