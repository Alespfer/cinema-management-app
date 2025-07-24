package com.mycompany.cinema.dao;

import com.mycompany.cinema.Role;
import java.util.List;
import java.util.Optional;

public interface RoleDAO {
    void addRole(Role role);
    Optional<Role> getRoleById(int id);
    List<Role> getAllRoles();
}