// Fichier : src/main/java/com/mycompany/cinema/dao/impl/PersonnelDAOImpl.java
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.dao.PersonnelDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonnelDAOImpl extends GenericDAOImpl<Personnel> implements PersonnelDAO {

    public PersonnelDAOImpl() {
        super("personnel.dat");
    }

    @Override
    public void addPersonnel(Personnel personnel) {
        this.data.add(personnel);
        saveToFile();
    }

    @Override
    public Optional<Personnel> getPersonnelById(int id) {
        return this.data.stream().filter(p -> p.getId() == id).findFirst();
    }

    @Override
    public List<Personnel> getAllPersonnel() {
        return new ArrayList<>(this.data);
    }

    @Override
    public void updatePersonnel(Personnel updatedPersonnel) {
        getPersonnelById(updatedPersonnel.getId()).ifPresent(p -> {
            int index = this.data.indexOf(p);
            this.data.set(index, updatedPersonnel);
            saveToFile();
        });
    }

    @Override
    public void deletePersonnel(int id) {
        if (this.data.removeIf(p -> p.getId() == id)) {
            saveToFile();
        }
    }
}