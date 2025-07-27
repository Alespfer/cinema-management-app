package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Tarif;
import com.mycompany.cinema.dao.TarifDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DAO pour gérer les tarifs appliqués aux billets.
public class TarifDAOImpl extends GenericDAOImpl<Tarif> implements TarifDAO {

    // Initialise le DAO avec le fichier des tarifs.
    public TarifDAOImpl() {
        super("tarifs.dat");
    }

    // Ajoute un nouveau tarif.
    @Override
    public void addTarif(Tarif tarif) {
        this.data.add(tarif);
        saveToFile();
    }

    // Recherche un tarif selon son identifiant.
    @Override
    public Optional<Tarif> getTarifById(int id) {
        for (Tarif tarif : this.data) {
            if (tarif.getId() == id) {
                return Optional.of(tarif);
            }
        }
        return Optional.empty();
    }

    // Retourne tous les tarifs enregistrés.
    @Override
    public List<Tarif> getAllTarifs() {
        return new ArrayList<>(this.data);
    }

    // Met à jour un tarif existant.
    @Override
    public void updateTarif(Tarif updatedTarif) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == updatedTarif.getId()) {
                this.data.set(i, updatedTarif);
                saveToFile();
                return;
            }
        }
    }

    // Supprime un tarif en fonction de son identifiant.
    @Override
    public void deleteTarif(int id) {
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
