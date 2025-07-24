package com.mycompany.cinema.dao;

import com.mycompany.cinema.Salle;
import java.util.List;
import java.util.Optional;

/**
 * Contrat pour la gestion de la persistance des Salles.
 */
public interface SalleDAO {
    void addSalle(Salle salle);
    Optional<Salle> getSalleById(int id);
    List<Salle> getAllSalles();
    void updateSalle(Salle salle);
    void deleteSalle(int id);
}