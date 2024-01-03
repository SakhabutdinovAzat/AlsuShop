package ru.clothingstore.service;

import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.user.Profile;
import ru.clothingstore.model.user.User;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> getAll();

    public User getOne(int id);

    public void save(User user);

    public void update(User updateUser);

    public void delete(int id);

    public Optional<User> getByLastnameAndFirstname(String lastName, String firstName);

    public Optional<User> getByEmail(String email);

    public Optional<User> getByUsername(String username);

    public List<Order> getOrdersById(int id);

    boolean activateUser(String code);

    void updateProfile(Principal principal, Profile profile);
}
