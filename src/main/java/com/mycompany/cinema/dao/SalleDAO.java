// Fichier : SalleDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Salle;
import java.util.List;
import java.util.Optional;

/**
 * Définit le contrat pour la gestion des salles de projection.
 * 
 * L'interface graphique utilise ce contrat pour :
 * - Savoir quel plan de sièges dessiner (`getSalleById` via le service).
 * - Permettre à l'administrateur de gérer les salles dans `GestionSallesPanel` (CRUD complet).
 */
public interface SalleDAO {
    void addSalle(Salle salle);
    Optional<Salle> getSalleById(int id);
    List<Salle> getAllSalles();
    void updateSalle(Salle salle);
    void deleteSalle(int id);
    void rechargerDonnees();
}