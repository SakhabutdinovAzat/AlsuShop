package ru.clothingstore.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clothingstore.model.user.Role;
import ru.clothingstore.repository.RoleRepository;
import ru.clothingstore.service.RoleService;
import ru.clothingstore.util.exception.RoleNotFoundException;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) {
        this.roleRepository = repository;
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new RoleNotFoundException("Role with name: " + name + " not found"));
    }

    @Override
    public Role getRoleById(int id) {
        return roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException("Role with id = " + id + " not found"));
    }

    @Override
    public Set<Role> getAllRoles() {
        return new HashSet<>(roleRepository.findAll());
    }
}
