package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Tarif;
import com.mycompany.cinema.dao.TarifDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation concrète pour la gestion des tarifs dans le fichier "tarifs.dat".
 * 
 * Pour le développeur de l'interface graphique : cette classe fournit les données pour :
 * - Le menu déroulant de sélection du tarif dans le `SiegePanel` (via `getAllTarifs`).
 * - Le panneau de gestion des tarifs de l'administrateur, `GestionTarifsPanel`, qui
 *   utilise l'ensemble des méthodes CRUD pour la maintenance.
 */
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
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == updatedTarif.getId()) {
                this.data.set(i, updatedTarif);
                saveToFile();
                return;
            }
        }
    }

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