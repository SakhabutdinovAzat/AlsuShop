package ru.clothingstore.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class Profile {

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Invalid email")
    private String email;

    @NotEmpty(message = "Password should not be empty")
    private String passwordActual;

    private String password1;

    private String password2;
}
