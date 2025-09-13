// ========================================================================
// RoleDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Role;
import com.mycompany.cinema.dao.RoleDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation pour la gestion des rôles du personnel.
 * Interagit avec le fichier "roles.dat".
 *
 */
public class RoleDAOImpl extends GenericDAOImpl<Role> implements RoleDAO {

    public RoleDAOImpl() {
        super("roles.dat");
    }

    /**
     * Ajoute un nouveau rôle.
     * @param role Le rôle à enregistrer.
     */
    @Override
    public void ajouterRole(Role role) {
        this.data.add(role);
        sauvegarderDansFichier();
    }

    /**
     * Recherche un rôle par son identifiant.
     * @param id L'identifiant du rôle.
     * @return L'objet Role, ou `null` si non trouvé.
     */
    @Override
    public Role trouverRoleParId(int id) {
        for (Role role : this.data) {
            if (role.getId() == id) {
                return role;
            }
        }
        return null;
    }
    
    
    /**
     * Retourne la liste de tous les rôles disponibles.
     * @return Une copie de la liste des rôles.
     */
    @Override
    public List<Role> trouverTousLesRoles() {
        return new ArrayList<>(this.data);
    }
}
