package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Salle;
import com.mycompany.cinema.dao.SalleDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DAO pour la gestion des salles de projection.

public class SalleDAOImpl extends GenericDAOImpl<Salle> implements SalleDAO {

    // Initialise le DAO avec le fichier des salles.
    public SalleDAOImpl() {
        super("salles.dat");
    }
    
    // Ajoute une nouvelle salle à la base de données.
    @Override
    public void addSalle(Salle salle) {
        this.data.add(salle);
        saveToFile();
    }

    // Recherche une salle par son identifiant.
    @Override
    public Optional<Salle> getSalleById(int id) {
        for (Salle salle : this.data) {
            if (salle.getId() == id) {
                return Optional.of(salle);
            }
        }
        return Optional.empty();
    }

    // Retourne toutes les salles existantes.
    @Override
    public List<Salle> getAllSalles() {
        return new ArrayList<>(this.data);
    }

    // Met à jour une salle existante.
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
    
    // Supprime une salle en fonction de son identifiant.
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