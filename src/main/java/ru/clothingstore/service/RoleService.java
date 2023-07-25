package ru.clothingstore.service;

import ru.clothingstore.model.person.Role;

import java.util.Set;

public interface RoleService {
    Role getRoleByName(String name);

    Role getRoleById(int id);

    Set<Role> getAllRoles();
}
