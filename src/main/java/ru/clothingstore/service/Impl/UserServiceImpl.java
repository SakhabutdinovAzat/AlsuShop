package ru.clothingstore.service.Impl;

import org.hibernate.Hibernate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clothingstore.model.cart.Cart;
import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.person.Reputation;
import ru.clothingstore.model.person.User;
import ru.clothingstore.repository.UserRepository;
import ru.clothingstore.service.MailService;
import ru.clothingstore.service.NewsService;
import ru.clothingstore.service.UserService;

import java.security.Principal;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(NewsService.class);

    @Value("${hostname}")
    private String hostname;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @Override
    public User getOne(int id) {
        Optional<User> foundUser = userRepository.findById(id);

        return foundUser.orElse(null);
    }

    @Override
    @Transactional
    public void save(User user) {
        user.setReputation(Reputation.NORMAL);
        user.setCreatedAt(new Date());
        user.setCart(new Cart());
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        LOGGER.info("User has added successfully ", user);
    }

    // TODO
    @Override
    @Transactional
    public void update(User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(updatedUser.getId());
        if (optionalUser.isPresent()) {
            User userToBeUpdate = optionalUser.get();
            userToBeUpdate.setUsername(updatedUser.getUsername());
            userToBeUpdate.setActive(updatedUser.getActive());
            userToBeUpdate.setEmail(updatedUser.getEmail());
            userToBeUpdate.setRole(updatedUser.getRole());

            if (userToBeUpdate.getPassword().equals(updatedUser.getPassword())) {
                userToBeUpdate.setPassword(updatedUser.getPassword());
            } else {
                userToBeUpdate.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
//        updatePerson.setUsername(personToBeUpdate.getUsername());
//        updatePerson.setPassword(personToBeUpdate.getPassword());
//            updatePerson.setReputation(personToBeUpdate.getReputation());
//            updatePerson.setCreatedAt(personToBeUpdate.getCreatedAt());
//            updatePerson.setCart(personToBeUpdate.getCart());
//            updatePerson.setId(personToBeUpdate.getId());
//            updatePerson.setReputation(personToBeUpdate.getReputation());
//        updatePerson.setRole(personToBeUpdate.getRole());
            userRepository.save(userToBeUpdate);
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getByLastnameAndFirstname(String lastName, String firstName) {
        return userRepository.findByLastNameAndFirstName(lastName, firstName);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    // TODO не реализовано
    public List<Order> getOrdersById(int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()){
            Hibernate.initialize(user.get().getOrders());

            // Проверка просрочености оплаты
            user.get().getOrders().forEach(order -> {
                long diffInMillies = Math.abs(order.getOrderDate().getTime() - new Date().getTime());
                // 2592000000 миллисекунд = 3 суток
                if (diffInMillies >= 259200000) {
                    order.setExpired(true); // оплата просрочена
                }
            });
            return user.get().getOrders();
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    @Override
    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);

        userRepository.save(user);
        return true;
    }

    @Transactional
    @Override
    public void updateProfile(Principal principal, String password, String email) {
        User user = userRepository.findByUsername(principal.getName()).get();
        String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) || (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged) {
            user.setEmail(email);

            if (!email.isEmpty()) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if (!password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);
        if (isEmailChanged) {
            sendMessage(user);
        }
    }


    private void sendMessage(User user) {
        if (!user.getEmail().isEmpty()) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welkome to ClothingStore. You are change your profile. Please, visit next link: http://%s/auth/activate/%s",
                    user.getUsername(),
                    hostname,
                    user.getActivationCode()
            );

            mailService.sendEmail(user.getEmail(), "Activation code", message);
        }
    }
}
