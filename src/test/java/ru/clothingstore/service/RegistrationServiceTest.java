package ru.clothingstore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clothingstore.model.person.User;
import ru.clothingstore.repository.UserRepository;
import ru.clothingstore.service.Impl.MailServiceImpl;
import ru.clothingstore.service.Impl.RegistrationServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Mock
    private RoleService roleService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MailServiceImpl mailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void register() {
        User user = new User();
        user.setUsername("user");
        //user.setPassword("password");
        user.setEmail("email");

        registrationService.register(user);

        verify(roleService, times(1)).getRoleByName(Mockito.any());
        verify(passwordEncoder, times(1)).encode(user.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(mailService, times(1)).sendEmail(Mockito.any(), Mockito.any(), Mockito.any());
    }
}