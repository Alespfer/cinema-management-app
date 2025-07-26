package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.dao.PersonnelDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonnelDAOImpl extends GenericDAOImpl<Personnel> implements PersonnelDAO {

    public PersonnelDAOImpl() { super("personnel.dat"); }

    @Override
    public void addPersonnel(Personnel personnel) {
        this.data.add(personnel);
        saveToFile();
    }

    @Override
    public Optional<Personnel> getPersonnelById(int id) {
        for (Personnel p : this.data) {
            if (p.getId() == id) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Personnel> getAllPersonnel() {
        return new ArrayList<>(this.data);
    }

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

    @Override
    public void deletePersonnel(int id) {
        this.data.removeIf(p -> p.getId() == id);
        saveToFile();
    }
}