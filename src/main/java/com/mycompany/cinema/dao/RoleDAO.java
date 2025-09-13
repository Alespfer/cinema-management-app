// ========================================================================
// RoleDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Role;
import java.util.List;

/**
 * Définit le contrat pour la gestion des rôles du personnel (Admin, Vendeur...).
 */
public interface RoleDAO {

    void ajouterRole(Role role);
    Role trouverRoleParId(int id);
    List<Role> trouverTousLesRoles();
    void rechargerDonnees();
}