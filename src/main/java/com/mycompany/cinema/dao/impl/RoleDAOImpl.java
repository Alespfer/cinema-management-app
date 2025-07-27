package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Role;
import com.mycompany.cinema.dao.RoleDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleDAOImpl extends GenericDAOImpl<Role> implements RoleDAO {

    // Initialise le DAO avec le fichier des rôles.
    public RoleDAOImpl() {
        super("roles.dat");
    }
    // Ajoute un rôle dans la base de données.
    @Override
    public void addRole(Role role) {
        this.data.add(role);
        saveToFile();
    }

    /**
     * Recherche un rôle par son identifiant en parcourant la liste.
     * C'est la méthode de base pour trouver un objet par sa clé.
     */
    @Override
    public Optional<Role> getRoleById(int id) {
        // On parcourt toute la liste des rôles en mémoire.
        for (Role role : this.data) {
            // Si l'ID du rôle actuel correspond à celui qu'on cherche...
            if (role.getId() == id) {
                // ... on l'encapsule dans un Optional et on le retourne.
                return Optional.of(role);
            }
        }
        // Si on a fini la boucle sans rien trouver, on retourne un Optional vide.
        return Optional.empty();
    }
    
    // Retourne tous les rôles disponibles.
    @Override
    public List<Role> getAllRoles() {
        // On retourne une nouvelle ArrayList pour éviter que le code extérieur
        // ne modifie directement notre liste de données interne (this.data).
        return new ArrayList<>(this.data);
    }
}