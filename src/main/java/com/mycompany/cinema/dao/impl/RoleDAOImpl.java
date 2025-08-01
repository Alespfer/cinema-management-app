// Fichier : RoleDAOImpl.java
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Role;
import com.mycompany.cinema.dao.RoleDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation concrète pour la gestion des rôles du personnel dans "roles.dat".
 * 
 * Pour le développeur de l'interface graphique : cette classe est essentielle pour la
 * logique de connexion et de gestion du personnel.
 * - `getAllRoles` et `getRoleById` sont utilisés par `LoginFrame` pour la redirection
 *   et par `GestionPersonnelPanel` pour remplir la liste déroulante des rôles.
 */
public class RoleDAOImpl extends GenericDAOImpl<Role> implements RoleDAO {

    public RoleDAOImpl() {
        super("roles.dat");
    }

    @Override
    public void addRole(Role role) {
        this.data.add(role);
        saveToFile();
    }

    @Override
    public Optional<Role> getRoleById(int id) {
        for (Role role : this.data) {
            if (role.getId() == id) {
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<Role> getAllRoles() {
        return new ArrayList<>(this.data);
    }
}