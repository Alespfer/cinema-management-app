package com.mycompany.cinema.dao;

import com.mycompany.cinema.Personnel;
import java.util.List;
import java.util.Optional;

/**
 * Contrat pour la gestion de la persistance du Personnel.
 */
public interface PersonnelDAO {
    void addPersonnel(Personnel personnel);
    Optional<Personnel> getPersonnelById(int id);
    List<Personnel> getAllPersonnel();
    void updatePersonnel(Personnel personnel);
    void deletePersonnel(int id);
}