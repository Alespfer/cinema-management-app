// Fichier : RoleDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Role;
import java.util.List;
import java.util.Optional;

/**
 * Définit le contrat pour la gestion des rôles du personnel (Admin, Vendeur...).
 * 
 * Ce contrat est essentiel pour la partie administrative de l'interface. Il est utilisé
 * dans `LoginFrame` pour la redirection après connexion, et dans `GestionPersonnelPanel`
 * pour peupler la liste déroulante des rôles assignables.
 */
public interface RoleDAO {
    void addRole(Role role);
    Optional<Role> getRoleById(int id);
    List<Role> getAllRoles();
    void rechargerDonnees();
}