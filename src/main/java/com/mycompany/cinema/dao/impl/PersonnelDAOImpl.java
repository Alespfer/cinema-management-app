package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.dao.PersonnelDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DAO pour gérer les employés du cinéma.
public class PersonnelDAOImpl extends GenericDAOImpl<Personnel> implements PersonnelDAO {

    // Initialise la DAO avec le fichier personnel.
    public PersonnelDAOImpl() {
        super("personnel.dat");
    }

    // Ajoute un nouvel employé à la liste.
    @Override
    public void addPersonnel(Personnel personnel) {
        this.data.add(personnel);
        saveToFile();
    }

    // Recherche un employé par identifiant.
    @Override
    public Optional<Personnel> getPersonnelById(int id) {
        for (Personnel p : this.data) {
            if (p.getId() == id) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    // Retourne la liste complète des membres du personnel.
    @Override
    public List<Personnel> getAllPersonnel() {
        return new ArrayList<>(this.data);
    }

    // Met à jour les informations d’un employé existant.
    @Override
    public void updatePersonnel(Personnel updatedPersonnel) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == updatedPersonnel.getId()) {
                this.data.set(i, updatedPersonnel);
                saveToFile();
                return;
            }
        }
    }

    // Supprime un employé à partir de son identifiant.
    @Override
    public void deletePersonnel(int id) {
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
