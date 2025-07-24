package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Role;
import com.mycompany.cinema.dao.RoleDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return this.data.stream().filter(r -> r.getId() == id).findFirst();
    }

    @Override
    public List<Role> getAllRoles() {
        return new ArrayList<>(this.data);
    }
}