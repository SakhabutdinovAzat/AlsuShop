package ru.clothingstore.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import ru.clothingstore.model.cart.Cart;
import ru.clothingstore.model.order.Order;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "Person")
@EqualsAndHashCode(exclude = {"id", "role", "cart", "orders"})
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Schema(description = "Сущность пользователя")
public class User {

    @ToString.Include
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @ToString.Include
    @Column(name = "username")
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 30, message = "Username should be between 2 and 30")
    @Schema(description = "Имя пользователя", example = "Azat123")
    private String username;

    @Column(name = "email")
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Invalid email")
    @Schema(description = "Электронная почта пользователя", example = "azat@yandex.ru")
    private String email;

    @Column(name = "activation_code")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String activationCode;

    @Column(name = "active", columnDefinition = "boolean default true")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean active = true;

    @NotEmpty(message = "Password should not be empty")
    @Column(name = "password")
    @Schema(description = "Пароль", example = "password123")
    private String password;

    @Transient
    @Column(name = "password")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String password2;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @Schema(description = "Роль пользователя")
    private Role role;

    @Column(name = "firstname")
    @Size(min = 2, max = 30, message = "Size should be between 2 and 30 characters")
    @Pattern(regexp = "[A-Z]\\w+", message = "Incorrect first name")
    @Schema(description = "Имя", example = "Азат")
    private String firstName;

    @Column(name = "lastname")
    @Size(min = 2, max = 30, message = "Size should be between 2 and 50 characters")
    @Pattern(regexp = "[A-Z]\\w+", message = "Incorrect last name")
    @Schema(description = "Фамилия", example = "Caхабутдинов")
    private String lastName;

    @Column(name = "age")
    @Min(value = 0, message = "Age should be more than 0")
    @Schema(description = "Возвраст", example = "25")
    private int age;

    @Column(name = "address")
    @Pattern(regexp = "[A-Z]\\w+, [A-Z]\\w+ \\d{2}, \\d{6}", message = "Incorrect address")
    @Schema(description = "Адрес", example = "Kazan, Pushkina 43, 512484")
    private String address;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Date createdAt;

    @Enumerated(EnumType.ORDINAL)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Reputation reputation;

    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.REFRESH})
    private List<Order> orders;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void addOrder(Order order) {
        if (this.orders == null) {
            this.orders = new ArrayList<>();
        }

        this.orders.add(order);
        order.setOwner(this);
    }


}
