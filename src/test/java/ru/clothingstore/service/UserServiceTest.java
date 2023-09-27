package ru.clothingstore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.person.User;
import ru.clothingstore.repository.UserRepository;
import ru.clothingstore.service.Impl.MailServiceImpl;
import ru.clothingstore.service.Impl.UserServiceImpl;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private User user;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailServiceImpl mailService;

    @BeforeEach
    private void beforeEach() {
        user = new User();
        user.setId(1);
    }

    @Test
    void findAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> userList = userService.getAll();

        verify(userRepository, times(1)).findAll();
        assertTrue(userList.contains(user));
    }

    @Test
    void findOne() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User user1 = userService.getOne(1);

        verify(userRepository, times(1)).findById(1);
        assertEquals(user, user1);
    }

    @Test
    void save() {
        user.setPassword("password");
        userService.save(user);

        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void update() {
        User user1 = new User();
        user1.setId(1);
        user1.setPassword("password");
        user.setPassword("passwordOld");
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user));
        userService.update(user1);

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode(user1.getPassword());
    }

    @Test
    void delete() {
        userService.delete(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void findByLastnameAndFirstname() {
        when(userRepository.findByLastNameAndFirstName("last", "first")).thenReturn(Optional.of(user));
        User user1 = userService.getByLastnameAndFirstname("last", "first").get();

        verify(userRepository, times(1)).findByLastNameAndFirstName("last", "first");
        assertEquals(user, user1);
    }

    @Test
    void findByEmail() {
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(user));
        User user1 = userService.getByEmail("email").get();

        verify(userRepository, times(1)).findByEmail("email");
        assertEquals(user, user1);

    }

    @Test
    void findByUsername() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        User user1 = userService.getByUsername("username").get();

        verify(userRepository, times(1)).findByUsername("username");
        assertEquals(user, user1);
    }

    @Disabled
    @Test
    void getOrdersById() {
    }

    @Test
    void activateUser() {
        when(userRepository.findByActivationCode("code")).thenReturn(user);

        boolean activate = userService.activateUser("code");

        verify(userRepository, times(1)).findByActivationCode("code");
        verify(userRepository, times(1)).save(user);
        assertTrue(activate);
    }

    @Test
    void updateProfileNotUpdate() {
        user.setPassword("password");
        user.setEmail("email");
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("username");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        userService.updateProfile(principal, "", "email");

        verify(userRepository, times(1)).findByUsername("username");
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, never()).encode("password");
    }

    @Test
    void updateProfileOnlyPassword() {
        user.setPassword("password");
        user.setEmail("email");
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("username");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        userService.updateProfile(principal, "password2", "email");

        verify(userRepository, times(1)).findByUsername("username");
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("password2");
    }

    @Test
    void updateProfileOnlyEmail() {
        user.setPassword("password");
        user.setEmail("email");
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("username");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        userService.updateProfile(principal, "", "email2");

        verify(userRepository, times(1)).findByUsername("username");
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, never()).encode("password");
        verify(mailService, times(1)).sendEmail(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void updateProfilePasswordAndEmail() {
        user.setPassword("password");
        user.setEmail("email");
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("username");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        userService.updateProfile(principal, "password2", "email2");

        verify(userRepository, times(1)).findByUsername("username");
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("password2");
        verify(mailService, times(1)).sendEmail(Mockito.any(), Mockito.any(), Mockito.any());
    }
}