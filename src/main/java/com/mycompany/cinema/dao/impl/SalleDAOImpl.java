// Fichier : SalleDAOImpl.java
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Salle;
import com.mycompany.cinema.dao.SalleDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation concrète pour la gestion des salles de projection dans "salles.dat".
 * 
 * Pour le développeur de l'interface graphique : cette classe fournit les données
 * nécessaires pour tout ce qui concerne les salles.
 * - Le `SiegePanel` a besoin de `getSalleById` pour connaître la capacité de la salle
 *   afin de dessiner le plan des sièges.
 * - Le `GestionSallesPanel` de l'administrateur utilise toutes les méthodes CRUD
 *   de cette classe pour permettre la gestion complète des salles.
 */
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
        for (Salle salle : this.data) {
            if (salle.getId() == id) {
                return Optional.of(salle);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Salle> getAllSalles() {
        return new ArrayList<>(this.data);
    }

    @Override
    public void updateSalle(Salle updatedSalle) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == updatedSalle.getId()) {
                this.data.set(i, updatedSalle);
                saveToFile();
                return;
            }
        }
    }
    
    @Override
    public void deleteSalle(int id) {
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